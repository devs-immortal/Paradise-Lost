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
 * Whether a player is anchored in the air by the floaty leggings, plus what to restore after a gamemode switch.
 */
public class AnchoringAttachment extends CommonDataAttachment<AnchoringAttachment.State> {

    public AnchoringAttachment() {
        super(entity -> State.DEFAULT, ModConstants.locate("anchoring"), false, State.CODEC, State.STREAM_CODEC);
    }

    public State get(Entity player) {
        return Services.ATTACHMENTS.getOrCreateAttachedValue(player, this);
    }

    public void set(Entity player, State state) {
        Services.ATTACHMENTS.setAttachedValue(player, this, state);
    }

    public void update(Entity player, UnaryOperator<State> change) {
        State current = this.get(player);
        State next = change.apply(current);
        if (!next.equals(current)) {
            this.set(player, next);
        }
    }

    public boolean isAnchored(Entity player) {
        return this.get(player).anchored();
    }

    public void setAnchored(Entity player, boolean anchored) {
        this.update(player, state -> state.withAnchored(anchored));
    }

    public boolean wasAnchoredBeforeLeavingSurvival(Entity player) {
        return this.get(player).anchoredBeforeLeavingSurvival();
    }

    public void setAnchoredBeforeLeavingSurvival(Entity player, boolean anchored) {
        this.update(player, state -> state.withAnchoredBeforeLeavingSurvival(anchored));
    }

    /**
     * @param anchored                      true while the player hovers in place
     * @param anchoredBeforeLeavingSurvival whether the player was anchored when they switched to creative or spectator
     */
    public record State(boolean anchored, boolean anchoredBeforeLeavingSurvival) {
        public static final State DEFAULT = new State(false, false);

        public static final Codec<State> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("anchored", false).forGetter(State::anchored),
                Codec.BOOL.optionalFieldOf("anchored_before_leaving_survival", false)
                        .forGetter(State::anchoredBeforeLeavingSurvival)
        ).apply(instance, State::new));

        public static final StreamCodec<ByteBuf, State> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, State::anchored,
                ByteBufCodecs.BOOL, State::anchoredBeforeLeavingSurvival,
                State::new
        );

        public State withAnchored(boolean value) {
            return new State(value, this.anchoredBeforeLeavingSurvival);
        }

        public State withAnchoredBeforeLeavingSurvival(boolean value) {
            return new State(this.anchored, value);
        }
    }
}
