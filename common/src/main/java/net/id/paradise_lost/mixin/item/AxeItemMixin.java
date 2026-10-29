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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Unique
    private static final ThreadLocal<BlockState> paradiseLost$originalTreeLike = new ThreadLocal<>();

    @Inject(method = "useOn", at = @At("HEAD"))
    private void paradiseLost$captureState(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        paradiseLost$originalTreeLike.set(context.getLevel().getBlockState(context.getClickedPos()));
    }

    @Inject(method = "useOn", at = @At("RETURN"))
    private void paradiseLost$dropLoot(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        BlockState original = paradiseLost$originalTreeLike.get();
        paradiseLost$originalTreeLike.remove();

        Level level = context.getLevel();
        if (original == null || !(level instanceof ServerLevel server)) return;
        if (!cir.getReturnValue().consumesAction()) return;

        Block treeLike = original.getBlock();
        if (!treeLike.equals(BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get())
                && !treeLike.equals(BlockRegistry.MOTHER_AUREL_WOODSTUFF.wood().get())) return;

        BlockPos pos = context.getClickedPos();
        LootTable table = server.getServer().reloadableRegistries()
                .getLootTable(ParadiseLostLootTables.MOTHER_AUREL_STRIPPING);

        List<ItemStack> items = table.getRandomItems(new LootParams.Builder(server)
                .withParameter(LootContextParams.BLOCK_STATE, original)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, context.getItemInHand())
                .create(LootContextParamSets.BLOCK));

        Vec3 loc = context.getClickLocation();
        for (ItemStack stack : items) {
            server.addFreshEntity(new ItemEntity(server, loc.x, loc.y, loc.z, stack));
        }
    }
}
