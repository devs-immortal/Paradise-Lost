package net.id.paradiselost.items.utils;

import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ParadiseLostDispenserBehaviors {
    public static DispenserBehavior emptiableBucket = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            FluidModificationItem fluidModificationItem = (FluidModificationItem) stack.getItem();
            BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            World world = pointer.world();
            if (stack.isOf(ParadiseLostItems.AUREL_POWDER_SNOW_BUCKET)) {
                if (world.isInBuildLimit(blockPos) && world.getBlockState(blockPos).isReplaceable()) {
                    world.setBlockState(blockPos, Blocks.POWDER_SNOW.getDefaultState());
                    return new ItemStack(ParadiseLostItems.AUREL_BUCKET);
                }
                return this.fallbackBehavior.dispense(pointer, stack);
            } else if (fluidModificationItem.placeFluid(null, world, blockPos, null)) {
                fluidModificationItem.onEmptied(null, world, stack, blockPos);
                return new ItemStack(ParadiseLostItems.AUREL_BUCKET);
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };

    public static DispenserBehavior emptyBucket = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            World world = pointer.world();
            FluidState fluidState = world.getFluidState(blockPos);
            if (fluidState.isOf(Fluids.WATER) && fluidState.isStill()) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                return this.decrementStackWithRemainder(pointer, stack, new ItemStack(ParadiseLostItems.AUREL_WATER_BUCKET));
            } else if (world.getBlockState(blockPos).isOf(Blocks.POWDER_SNOW)) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                return this.decrementStackWithRemainder(pointer, stack, new ItemStack(ParadiseLostItems.AUREL_POWDER_SNOW_BUCKET));
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };

    public static DispenserBehavior spawnEgg = new ItemDispenserBehavior() {
        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            Direction direction = pointer.state().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getEntityType(stack);

            try {
                entityType.spawnFromItemStack(pointer.world(), stack, null, pointer.pos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
            } catch (Exception var6) {
                LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pointer.pos(), var6);
                return ItemStack.EMPTY;
            }

            stack.decrement(1);
            pointer.world().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
            return stack;
        }
    };
}
