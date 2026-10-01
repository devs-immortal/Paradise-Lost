package net.id.paradise_lost.item;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ParadiseLostDispenserBehaviors {
    public static DispenseItemBehavior emptiableBucket = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior fallbackBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource pointer, ItemStack stack) {
            DispensibleContainerItem fluidModificationItem = (DispensibleContainerItem) stack.getItem();
            BlockPos blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));
            Level world = pointer.level();
            if (stack.is(ItemRegistry.AUREL_POWDER_SNOW_BUCKET.get())) {
                if (world.isInWorldBounds(blockPos) && world.getBlockState(blockPos).canBeReplaced()) {
                    world.setBlockAndUpdate(blockPos, Blocks.POWDER_SNOW.defaultBlockState());
                    return new ItemStack(ItemRegistry.AUREL_BUCKET.get());
                }
                return this.fallbackBehavior.dispense(pointer, stack);
            } else if (fluidModificationItem.emptyContents(null, world, blockPos, null)) {
                fluidModificationItem.checkExtraContent(null, world, stack, blockPos);
                return new ItemStack(ItemRegistry.AUREL_BUCKET.get());
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };

    public static DispenseItemBehavior emptyBucket = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior fallbackBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource pointer, ItemStack stack) {
            BlockPos blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));
            Level world = pointer.level();
            FluidState fluidState = world.getFluidState(blockPos);
            if (fluidState.is(Fluids.WATER) && fluidState.isSource()) {
                world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                return this.consumeWithRemainder(pointer, stack, new ItemStack(ItemRegistry.AUREL_WATER_BUCKET.get()));
            } else if (world.getBlockState(blockPos).is(Blocks.POWDER_SNOW)) {
                world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                return this.consumeWithRemainder(pointer, stack, new ItemStack(ItemRegistry.AUREL_POWDER_SNOW_BUCKET.get()));
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };

    public static DispenseItemBehavior spawnEgg = new DefaultDispenseItemBehavior() {
        @Override
        public ItemStack execute(BlockSource pointer, ItemStack stack) {
            Direction direction = pointer.state().getValue(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack);

            try {
                entityType.spawn(pointer.level(), stack, null, pointer.pos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
            } catch (Exception var6) {
                LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pointer.pos(), var6);
                return ItemStack.EMPTY;
            }

            stack.shrink(1);
            pointer.level().gameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
            return stack;
        }
    };
}
