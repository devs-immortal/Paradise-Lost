package net.id.aether.mixin.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.BlockFilterPlacementModifier;
import net.minecraft.world.gen.decorator.DecoratorContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(BlockFilterPlacementModifier.class)
public interface BlockFilterPlacementModifierAccessor {
    @Invoker("shouldPlace")
    public boolean shouldPlace(DecoratorContext ctx, Random random, BlockPos pos);
}
