package net.id.paradise_lost.mixin.item;

import net.id.paradise_lost.item.tool.surtrum.SurtrumSwordItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SurtrumSwordItem.class)
public abstract class SurtrumSwordMixin extends SwordItem {
    public SurtrumSwordMixin(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.FIRE_ASPECT)) return false;
        return super.supportsEnchantment(stack, enchantment);
    }
}
