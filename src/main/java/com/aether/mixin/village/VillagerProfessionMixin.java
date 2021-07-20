package com.aether.mixin.village;

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

    public void addSecondaryJobSite(Block jobSite) {
        secondaryJobSites = ImmutableSet.<Block>builder().addAll(secondaryJobSites).add(jobSite).build();
    }
}
