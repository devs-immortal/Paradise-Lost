package net.id.paradiselost.items.weapons;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;

public class LightningSwordItem extends SwordItem {
    public LightningSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, attacker.world);
        if (attacker instanceof ServerPlayerEntity player) {
            lightning.setChanneler(player);
        }
        return super.postHit(stack, victim, attacker);
    }
}
