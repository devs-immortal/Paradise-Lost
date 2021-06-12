package com.aether.mixin.tags;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockTagsProvider.class)
public abstract class BlockTagsProviderMixin extends TagsProvider<Block> {

    protected BlockTagsProviderMixin(DataGenerator dataGenerator, Registry<Block> registry) {
        super(dataGenerator, registry);
    }

    @Inject(method = "addTags", at = @At("TAIL"))
    protected void modifyTags(CallbackInfo cir) {
        tag(BlockTags.PLANKS).add(AetherBlocks.SKYROOT_PLANKS, AetherBlocks.CRYSTAL_PLANKS, AetherBlocks.GOLDEN_OAK_PLANKS, AetherBlocks.WISTERIA_PLANKS);
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_ENCHANTED_GRASS, AetherBlocks.QUICKSOIL, AetherBlocks.AETHER_FARMLAND);
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AetherBlocks.GRAVITITE_ORE, AetherBlocks.AMBROSIUM_ORE, AetherBlocks.ZANITE_ORE);
        tag(BlockTags.NEEDS_IRON_TOOL).add(AetherBlocks.GRAVITITE_ORE);
        tag(BlockTags.WALL_POST_OVERRIDE).add(AetherBlocks.AMBROSIUM_TORCH);
        tag(BlockTags.SAPLINGS).add(AetherBlocks.CRYSTAL_SAPLING, AetherBlocks.GOLDEN_OAK_SAPLING, AetherBlocks.SKYROOT_SAPLING, AetherBlocks.BOREAL_WISTERIA_SAPLING, AetherBlocks.FROST_WISTERIA_SAPLING, AetherBlocks.LAVENDER_WISTERIA_SAPLING);
        tag(BlockTags.LEAVES).add(AetherBlocks.CRYSTAL_LEAVES, AetherBlocks.GOLDEN_OAK_LEAVES, AetherBlocks.SKYROOT_LEAVES, AetherBlocks.BOREAL_WISTERIA_LEAVES, AetherBlocks.FROST_WISTERIA_LEAVES, AetherBlocks.LAVENDER_WISTERIA_LEAVES);
    }
}
