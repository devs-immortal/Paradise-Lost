package net.id.paradise_lost.item.tool.surtrum;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SurtrumSwordItem extends SwordItem {
    public SurtrumSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        target.setRemainingFireTicks(120);
    }
}
