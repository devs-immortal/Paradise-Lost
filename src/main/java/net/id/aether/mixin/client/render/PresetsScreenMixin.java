package net.id.aether.mixin.client.render;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.world.dimension.AetherBiomes;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.PresetsScreen;
import net.minecraft.item.ItemConvertible;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(PresetsScreen.class)
public class PresetsScreenMixin {

    @Shadow
    private static void addPreset(Text presetName, ItemConvertible icon, RegistryKey<Biome> presetBiome, Set<RegistryKey<StructureSet>> structureKeys, boolean generateStronghold, boolean generateFeatures, FlatChunkGeneratorLayer... layers) {
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void extendPresetList(CallbackInfo ci) {
        addPreset(new TranslatableText("createWorld.customize.preset.the_aether.sandbox"), AetherBlocks.AETHER_GRASS_BLOCK, AetherBiomes.HIGHLANDS_PLAINS_KEY, Set.of(StructureSetKeys.VILLAGES), false, false, new FlatChunkGeneratorLayer(1, AetherBlocks.AETHER_GRASS_BLOCK), new FlatChunkGeneratorLayer(2, AetherBlocks.AETHER_DIRT), new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
    }
}
