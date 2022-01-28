package net.id.aether.client.rendering.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

/**
 * The common data for the colored splash particle.
 *
 * @param type The effect type
 * @param red The amount of red, 0-255
 * @param green The amount of green, 0-255
 * @param blue The amount of blue, 0-255
 */
public record ColoredSplashParticleEffect(
    ParticleType<ColoredSplashParticleEffect> type,
    int red,
    int green,
    int blue
) implements ParticleEffect {
    public static final Factory<ColoredSplashParticleEffect> FACTORY = new Factory<>() {
        // Parses the command that can be used to create this effect
        @Override
        public ColoredSplashParticleEffect read(ParticleType<ColoredSplashParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int red = MathHelper.clamp(reader.readInt(), 0, 255);
            reader.expect(' ');
            int green = MathHelper.clamp(reader.readInt(), 0, 255);
            reader.expect(' ');
            int blue = MathHelper.clamp(reader.readInt(), 0, 255);
            return new ColoredSplashParticleEffect(type, red, green, blue);
        }
    
        @Override
        public ColoredSplashParticleEffect read(ParticleType<ColoredSplashParticleEffect> type, PacketByteBuf buf) {
            return new ColoredSplashParticleEffect(type, Byte.toUnsignedInt(buf.readByte()), Byte.toUnsignedInt(buf.readByte()), Byte.toUnsignedInt(buf.readByte()));
        }
    };
    
    public ColoredSplashParticleEffect(ParticleType<ColoredSplashParticleEffect> type, int color){
        this(type, (color >>> 16) & 0xFF, (color >>> 8) & 0xFF, color & 0xFF);
    }
    
    @Override
    public ParticleType<?> getType() {
        return type;
    }
    
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(red);
        buf.writeByte(green);
        buf.writeByte(blue);
    }
    
    // Seems to be used to convert to a command, unsure if that is the case.
    @Override
    public String asString() {
        return Registry.PARTICLE_TYPE.getId(getType()) + " " + red + " " + green + " " + blue;
    }
}
