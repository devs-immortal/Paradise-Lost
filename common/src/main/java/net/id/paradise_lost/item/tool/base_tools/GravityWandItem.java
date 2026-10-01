package net.id.paradise_lost.item.tool.base_tools;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class GravityWandItem extends Item {
    public GravityWandItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        return GravityTool.flipEntity(stack, player, entity, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return GravityTool.tryFloatBlock(context, super.useOn(context));
    }
}
