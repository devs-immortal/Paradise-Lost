package net.id.paradise_lost.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.platform.Services;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

import java.util.function.UnaryOperator;

/**
 * Everything a levita minecart needs to remember while it floats, kept together so it saves and syncs as one piece.
 */
public class FloatyMinecartAttachment extends CommonDataAttachment<FloatyMinecartAttachment.State> {

    public FloatyMinecartAttachment() {
        super(entity -> State.DEFAULT, ModConstants.locate("floaty_minecart"), false, State.CODEC, State.STREAM_CODEC);
    }

    public State get(Entity cart) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(cart, this);
    }

    public void set(Entity cart, State state) {
        Services.ATTACHMENTS.setAttachedValue(cart, this, state);
    }

    /**
     * Applies the change and only writes it back when something actually differs, so nothing gets synced for nothing.
     */
    public void update(Entity cart, UnaryOperator<State> change) {
        State current = this.get(cart);
        State next = change.apply(current);
        if (!next.equals(current)) {
            this.set(cart, next);
        }
    }

    public int getFloatTicks(Entity cart) {
        return this.get(cart).floatTicks();
    }

    public void setFloatTicks(Entity cart, int ticks) {
        this.update(cart, state -> state.withFloatTicks(ticks));
    }

    public int getIncline(Entity cart) {
        return this.get(cart).incline();
    }

    public void setIncline(Entity cart, int incline) {
        this.update(cart, state -> state.withIncline(incline));
    }

    public String getRailShapeName(Entity cart) {
        return this.get(cart).railShape();
    }

    public void setRailShapeName(Entity cart, String shapeName) {
        this.update(cart, state -> state.withRailShape(shapeName));
    }

    public boolean isOffRail(Entity cart) {
        return this.get(cart).offRail();
    }

    public void setOffRail(Entity cart, boolean offRail) {
        this.update(cart, state -> state.withOffRail(offRail));
    }

    public float getPoseYaw(Entity cart) {
        return this.get(cart).poseYaw();
    }

    public float getPosePitch(Entity cart) {
        return this.get(cart).posePitch();
    }

    public void setPose(Entity cart, float yaw, float pitch) {
        this.update(cart, state -> state.withPose(yaw, pitch));
    }

    /**
     * @param floatTicks ticks of floating left, zero or less means the cart is not floating
     * @param incline    -1 going down a slope, 0 flat, 1 going up a slope
     * @param railShape  serialized name of the last rail shape the cart was on, empty if none
     * @param offRail    true while the cart is in the air instead of sitting on a rail
     * @param poseYaw    yaw the client should draw the cart with
     * @param posePitch  pitch the client should draw the cart with
     */
    public record State(int floatTicks, int incline, String railShape, boolean offRail, float poseYaw,
                        float posePitch) {
        public static final State DEFAULT = new State(0, 0, "", false, 0.0F, 0.0F);

        public static final Codec<State> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("float_ticks", 0).forGetter(State::floatTicks),
                Codec.INT.optionalFieldOf("incline", 0).forGetter(State::incline),
                Codec.STRING.optionalFieldOf("rail_shape", "").forGetter(State::railShape),
                Codec.BOOL.optionalFieldOf("off_rail", false).forGetter(State::offRail),
                Codec.FLOAT.optionalFieldOf("pose_yaw", 0.0F).forGetter(State::poseYaw),
                Codec.FLOAT.optionalFieldOf("pose_pitch", 0.0F).forGetter(State::posePitch)
        ).apply(instance, State::new));

        public static final StreamCodec<ByteBuf, State> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, State::floatTicks,
                ByteBufCodecs.VAR_INT, State::incline,
                ByteBufCodecs.STRING_UTF8, State::railShape,
                ByteBufCodecs.BOOL, State::offRail,
                ByteBufCodecs.FLOAT, State::poseYaw,
                ByteBufCodecs.FLOAT, State::posePitch,
                State::new
        );

        public State withFloatTicks(int value) {
            return new State(value, this.incline, this.railShape, this.offRail, this.poseYaw, this.posePitch);
        }

        public State withIncline(int value) {
            return new State(this.floatTicks, value, this.railShape, this.offRail, this.poseYaw, this.posePitch);
        }

        public State withRailShape(String value) {
            return new State(this.floatTicks, this.incline, value, this.offRail, this.poseYaw, this.posePitch);
        }

        public State withOffRail(boolean value) {
            return new State(this.floatTicks, this.incline, this.railShape, value, this.poseYaw, this.posePitch);
        }

        public State withPose(float yaw, float pitch) {
            return new State(this.floatTicks, this.incline, this.railShape, this.offRail, yaw, pitch);
        }
    }
}
