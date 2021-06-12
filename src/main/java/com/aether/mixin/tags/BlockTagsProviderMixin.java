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
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_ENCHANTED_GRASS, AetherBlocks.QUICKSOIL);
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AetherBlocks.GRAVITITE_ORE);
        tag(BlockTags.NEEDS_IRON_TOOL).add(AetherBlocks.GRAVITITE_ORE);
        tag(BlockTags.WALL_POST_OVERRIDE).add(AetherBlocks.AMBROSIUM_TORCH);
    }
}
