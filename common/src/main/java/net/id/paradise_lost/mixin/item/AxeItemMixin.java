package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();

        if (world.getBlockState(blockPos).getBlock() == BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get() && !world.isClientSide) {
            ServerLevel server = (ServerLevel) world;
            LootTable supplier = server.getServer().reloadableRegistries().getLootTable(ParadiseLostLootTables.MOTHER_AUREL_STRIPPING);
            List<ItemStack> items = supplier.getRandomItems(new LootParams.Builder(server)
                    .withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(blockPos))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atLowerCornerOf(blockPos))
                    .withParameter(LootContextParams.TOOL, context.getItemInHand())
                    .create(LootContextParamSets.BLOCK)
            );
            Vec3 offsetDirection = context.getClickLocation();
            for (ItemStack item : items) {
                ItemEntity itemEntity = new ItemEntity(context.getLevel(), offsetDirection.x, offsetDirection.y, offsetDirection.z, item);
                world.addFreshEntity(itemEntity);
            }
        }
    }
}
