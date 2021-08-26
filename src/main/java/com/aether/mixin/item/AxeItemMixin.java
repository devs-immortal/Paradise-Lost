package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import com.aether.loot.AetherLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin extends MiningToolItem {

    protected AxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Tag<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    // TODO: delegate strippable blocks to FAPI
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Map<Block, Block> AETHER_STRIPPED_BLOCKS = new HashMap<>();
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.CRYSTAL_LOG, AetherBlocks.STRIPPED_CRYSTAL_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.GOLDEN_OAK_LOG, AetherBlocks.STRIPPED_GOLDEN_OAK_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.SKYROOT_LOG, AetherBlocks.STRIPPED_SKYROOT_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.WISTERIA_LOG, AetherBlocks.STRIPPED_WISTERIA_LOG);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.ORANGE_LOG, AetherBlocks.STRIPPED_ORANGE_LOG);
        
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.CRYSTAL_WOOD, AetherBlocks.STRIPPED_CRYSTAL_WOOD);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.GOLDEN_OAK_WOOD, AetherBlocks.STRIPPED_GOLDEN_OAK_WOOD);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.SKYROOT_WOOD, AetherBlocks.STRIPPED_SKYROOT_WOOD);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.WISTERIA_WOOD, AetherBlocks.STRIPPED_WISTERIA_WOOD);
        AETHER_STRIPPED_BLOCKS.put(AetherBlocks.ORANGE_WOOD, AetherBlocks.STRIPPED_ORANGE_WOOD);

        Block block = AETHER_STRIPPED_BLOCKS.get(blockState.getBlock());

        if (block != null) {
            PlayerEntity playerEntity = context.getPlayer();
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isClient) {
                world.setBlockState(blockPos, block.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS)), 11);
                if (playerEntity != null)
                    context.getStack().damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));

                if (block == AetherBlocks.STRIPPED_GOLDEN_OAK_LOG) {
                    ServerWorld server = (ServerWorld) world;
                    LootTable supplier = server.getServer().getLootManager().getTable(AetherLootTables.GOLDEN_OAK_STRIPPING);
                    List<ItemStack> items = supplier.generateLoot(new LootContext.Builder(server).parameter(LootContextParameters.BLOCK_STATE, world.getBlockState(blockPos)).parameter(LootContextParameters.ORIGIN, new Vec3d(0,0,0)).parameter(LootContextParameters.TOOL, context.getStack()).build(LootContextTypes.BLOCK));
                    Vec3d offsetDirection = context.getHitPos();
                    for (ItemStack item : items) {
                        ItemEntity itemEntity = new ItemEntity(context.getWorld(), offsetDirection.x, offsetDirection.y, offsetDirection.z, item);
                        context.getWorld().spawnEntity(itemEntity);
                    }
                }
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
