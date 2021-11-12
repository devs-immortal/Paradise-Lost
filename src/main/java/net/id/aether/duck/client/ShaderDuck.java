package net.id.aether.duck.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.GlUniform;

@Environment(EnvType.CLIENT)
public interface ShaderDuck{
    GlUniform the_aether$getTime();
}
