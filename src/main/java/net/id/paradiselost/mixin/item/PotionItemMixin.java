package net.id.paradiselost.mixin.item;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    @Inject(method = "useOnBlock", at = @At("RETURN"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockState blockState = world.getBlockState(blockPos);
        Random random = world.random;
        if (blockState.isOf(Blocks.CALCITE) && (PotionUtil.getPotion(itemStack) == Potions.HEALING || PotionUtil.getPotion(itemStack) == Potions.STRONG_HEALING)) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }
            playerEntity.setStackInHand(context.getHand(), ItemUsage.exchangeStack(itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));
            world.setBlockState(blockPos, ParadiseLostBlocks.BLOOMED_CALCITE.getDefaultState());
            // particles
            for (int i = 0; i < 16; i++) {
                double xOffset = random.nextDouble();
                double yOffset = random.nextDouble();
                double zOffset = random.nextDouble();
                world.addParticle(ParticleTypes.ENTITY_EFFECT, blockPos.getX() + xOffset, blockPos.getY() + yOffset, blockPos.getZ() + zOffset, 0.97, 0.15, 0.14);
            }
            if (!world.isClient) {
                // sound
                world.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
            cir.cancel();
        }
    }
}
