package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.util.BloomedCalciteUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mixin(ThrownPotion.class)
public class PotionEntityMixin extends ThrowableItemProjectile {

    public PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "onHitBlock", at = @At("TAIL"), cancellable = true)
    protected void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (!this.level().isClientSide) {
            ItemStack itemStack = this.getItem();
            PotionContents potionContentsComponent = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            boolean healingPotion = potionContentsComponent.is(Potions.HEALING) || potionContentsComponent.is(Potions.STRONG_HEALING);
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockPos landBlock = blockPos.relative(direction);
            if (healingPotion) {
                List<BlockPos> affected = new LinkedList<>();
                if (level().getBlockState(landBlock.above()).is(Blocks.CALCITE)) {
                    affected.add(landBlock.above());
                }
                if (level().getBlockState(landBlock.below()).is(Blocks.CALCITE)) {
                    affected.add(landBlock.below());
                }
                addIfValid(landBlock, affected);
                addIfValid(landBlock.above(), affected);
                addIfValid(landBlock.below(), affected);
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    addIfValid(landBlock.above().relative(dir), affected);
                    addIfValid(landBlock.below().relative(dir), affected);
                }
                for (BlockPos pos : new BlockPos[] {landBlock, landBlock.north(), landBlock.south()}) {
                    addIfValid(pos, affected);
                    addIfValid(pos.east(), affected);
                    addIfValid(pos.west(), affected);
                }
                if (!affected.isEmpty()) {
                    Collections.shuffle(affected);
                    BloomedCalciteUtil.applyHealing(this.getOwner(), level(), affected.get(0), this.level().random, itemStack);
                    if (affected.size() > 1 && this.level().random.nextBoolean()) BloomedCalciteUtil.applyHealing(this.getOwner(), level(), affected.get(1), this.level().random, itemStack);
                }
            }

        }
    }

    private void addIfValid(BlockPos pos, List<BlockPos> list) {
        if (level().getBlockState(pos).is(Blocks.CALCITE)) {
            list.add(pos);
        }
    }

    @Override
    public Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }
}
