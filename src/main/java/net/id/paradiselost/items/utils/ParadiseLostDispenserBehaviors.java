package net.id.paradiselost.items.utils;

import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.tools.AurelBucketItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class ParadiseLostDispenserBehaviors {
    public static DispenserBehavior emptiableBucket = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            FluidModificationItem fluidModificationItem = (FluidModificationItem)stack.getItem();
            BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            World world = pointer.world();
            if (fluidModificationItem.placeFluid(null, world, blockPos, null)) {
                fluidModificationItem.onEmptied(null, world, stack, blockPos);
                return new ItemStack(ParadiseLostItems.AUREL_BUCKET);
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };

    public static DispenserBehavior spawnEgg = new ItemDispenserBehavior() {
        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            Direction direction = pointer.state().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack.getNbt());

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

    public static DispenserBehavior emptyBucket = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            WorldAccess worldAccess = pointer.world();
            BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
            BlockState blockState = worldAccess.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block instanceof FluidDrainable fluidDrainable) {
                ItemStack itemStack = fluidDrainable.tryDrainFluid(null, worldAccess, blockPos, blockState);
                if (itemStack.isEmpty()) {
                    return super.dispenseSilently(pointer, stack);
                } else {
                    worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    Item item = itemStack.getItem();
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        return new ItemStack(item);
                    } else {
                        if (pointer.blockEntity().addToFirstFreeSlot(new ItemStack(item)) < 0) {
                            this.fallbackBehavior.dispense(pointer, new ItemStack(item));
                        }
                        return stack;
                    }
                }
            } else {
                return super.dispenseSilently(pointer, stack);
            }
        }
    };
}
