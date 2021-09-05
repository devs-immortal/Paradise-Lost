package net.id.aether.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class GravititeAxeItem extends AxeItem {
    public GravititeAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        return GravititeTool.flipEntity(stack, player, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return GravititeTool.tryFloatBlock(context, super.useOnBlock(context));
    }
}
