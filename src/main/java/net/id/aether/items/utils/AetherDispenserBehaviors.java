package net.id.aether.items.utils;

import net.id.aether.items.AetherItems;
import net.id.aether.items.tools.SkyrootBucketItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class AetherDispenserBehaviors {
    public static DispenserBehavior emptiableBucket = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            if (!(stack.getItem() instanceof SkyrootBucketItem bucket)) {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
            BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            World world = pointer.getWorld();
            if (bucket.placeLiquid(null, world, blockPos, null)) {
                return new ItemStack(AetherItems.SKYROOT_BUCKET);
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };

    public static DispenserBehavior spawnEgg = new ItemDispenserBehavior() {
        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getEntityType(stack.getNbt());

            try {
                entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
            } catch (Exception var6) {
                LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pointer.getPos(), var6);
                return ItemStack.EMPTY;
            }

            stack.decrement(1);
            pointer.getWorld().emitGameEvent(GameEvent.ENTITY_PLACE, pointer.getPos());
            return stack;
        }
    };

    public static DispenserBehavior emptyBucket = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            if (!(stack.getItem() instanceof SkyrootBucketItem bucket)) {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
            WorldAccess worldAccess = pointer.getWorld();
            BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            BlockState blockState = worldAccess.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block instanceof FluidDrainable && blockState.getFluidState().getFluid() == Fluids.WATER) {
                ItemStack itemStack = ((FluidDrainable) block).tryDrainFluid(worldAccess, blockPos, blockState);
                if (itemStack.isEmpty()) {
                    return super.dispenseSilently(pointer, stack);
                } else {
                    worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        return new ItemStack(AetherItems.SKYROOT_WATER_BUCKET);
                    } else {
                        if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(AetherItems.SKYROOT_WATER_BUCKET)) < 0) {
                            this.fallbackBehavior.dispense(pointer, new ItemStack(AetherItems.SKYROOT_WATER_BUCKET));
                        }
                        return stack;
                    }
                }
            }
            return super.dispenseSilently(pointer, stack);
        }
    };
}
