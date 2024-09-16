package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.util.BloomedCalciteUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mixin(PotionEntity.class)
public class PotionEntityMixin extends ThrownItemEntity {

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onBlockHit", at = @At("TAIL"), cancellable = true)
    protected void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (!this.method_48926().isClient) {
            ItemStack itemStack = this.getStack();
            List<StatusEffectInstance> list = PotionUtil.getPotionEffects(itemStack);
            boolean healingPotion = list.stream().anyMatch((e) -> e.getEffectType() == StatusEffects.INSTANT_HEALTH);
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockPos landBlock = blockPos.offset(direction);
            if (healingPotion) {
                List<BlockPos> affected = new LinkedList<>();
                if (method_48926().getBlockState(landBlock.up()).isOf(Blocks.CALCITE)) {
                    affected.add(landBlock.up());
                }
                if (method_48926().getBlockState(landBlock.down()).isOf(Blocks.CALCITE)) {
                    affected.add(landBlock.down());
                }
                addIfValid(landBlock, affected);
                addIfValid(landBlock.up(), affected);
                addIfValid(landBlock.down(), affected);
                for (Direction dir : Direction.Type.HORIZONTAL) {
                    addIfValid(landBlock.up().offset(dir), affected);
                    addIfValid(landBlock.down().offset(dir), affected);
                }
                for (BlockPos pos : new BlockPos[] {landBlock, landBlock.north(), landBlock.south()}) {
                    addIfValid(pos, affected);
                    addIfValid(pos.east(), affected);
                    addIfValid(pos.west(), affected);
                }
                if (!affected.isEmpty()) {
                    Collections.shuffle(affected);
                    BloomedCalciteUtil.applyHealing(this.getOwner(), method_48926(), affected.get(0), this.method_48926().random, itemStack);
                    if (affected.size() > 1 && this.method_48926().random.nextBoolean()) BloomedCalciteUtil.applyHealing(this.getOwner(), method_48926(), affected.get(1), this.method_48926().random, itemStack);
                }
            }

        }
    }

    private void addIfValid(BlockPos pos, List<BlockPos> list) {
        if (method_48926().getBlockState(pos).isOf(Blocks.CALCITE)) {
            list.add(pos);
        }
    }

    @Override
    public Item getDefaultItem() {
        return null;
    }
}
