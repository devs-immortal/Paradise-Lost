package com.aether.mixin.village;

import com.aether.blocks.AetherBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerProfession.class)
public abstract class VillagerProfessionMixin {
    @Shadow
    @Mutable
    @Final
    private ImmutableSet<Block> secondaryJobSites;

    @Shadow
    @Final
    public static VillagerProfession FARMER;

    static {
        ((VillagerProfessionMixin) (Object) FARMER).secondaryJobSites = ImmutableSet.<Block>builder()
                .addAll(((VillagerProfessionMixin) (Object) FARMER).secondaryJobSites)
                .add(AetherBlocks.AETHER_FARMLAND)
                .build();
    }
}
