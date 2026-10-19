package net.id.paradise_lost.datagen;

import net.id.paradise_lost.PLConstants;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ParadiseLostParticleDescriptionProvider extends ParticleDescriptionProvider {
    public ParadiseLostParticleDescriptionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
    }

    @Override
    protected void addDescriptions() {
        sprite(ParadiseLostParticleTypes.CHERINE_FLAME, PLConstants.id("cherine_flame"));
        spriteSet(ParadiseLostParticleTypes.MOTHER_AUREL_LEAF,
                PLConstants.id("golden_leaf_1"),
                PLConstants.id("golden_leaf_2"),
                PLConstants.id("golden_leaf_3"));
        sprite(ParadiseLostParticleTypes.LEVITA_BLOOP, ResourceLocation.withDefaultNamespace("generic_0"));
        spriteSet(ParadiseLostParticleTypes.LEVITATION_TOTEM,
                ResourceLocation.withDefaultNamespace("glitter_7"),
                ResourceLocation.withDefaultNamespace("glitter_6"),
                ResourceLocation.withDefaultNamespace("glitter_5"),
                ResourceLocation.withDefaultNamespace("glitter_4"),
                ResourceLocation.withDefaultNamespace("glitter_3"),
                ResourceLocation.withDefaultNamespace("glitter_2"),
                ResourceLocation.withDefaultNamespace("glitter_1"),
                ResourceLocation.withDefaultNamespace("glitter_0"));
        spriteSet(ParadiseLostParticleTypes.LIT_CLOUD,
                ResourceLocation.withDefaultNamespace("generic_7"),
                ResourceLocation.withDefaultNamespace("generic_6"),
                ResourceLocation.withDefaultNamespace("generic_5"),
                ResourceLocation.withDefaultNamespace("generic_4"),
                ResourceLocation.withDefaultNamespace("generic_3"),
                ResourceLocation.withDefaultNamespace("generic_2"),
                ResourceLocation.withDefaultNamespace("generic_1"),
                ResourceLocation.withDefaultNamespace("generic_0"));
        spriteSet(ParadiseLostParticleTypes.LEVITA_SPARKLE,
                PLConstants.id("levita_sparkle_0"),
                PLConstants.id("levita_sparkle_1"),
                PLConstants.id("levita_sparkle_2"),
                PLConstants.id("levita_sparkle_3"),
                PLConstants.id("levita_sparkle_4"),
                PLConstants.id("levita_sparkle_5"));
    }
}
