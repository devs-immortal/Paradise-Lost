package net.id.paradiselost.items.armor;

import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class XpCircletItem extends ArmorItem {
    public XpCircletItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    public static void chargeCirclet(ItemStack stack, PlayerEntity player) {
        stack.set(ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE, new ParadiseLostDataComponentTypes.XpCircletChargeComponent(getExperienceFromPlayer(player)));
    }

    public static void dischargeCirclet(ItemStack stack, PlayerEntity player) {
        var circletComponent = stack.getOrDefault(ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE, new ParadiseLostDataComponentTypes.XpCircletChargeComponent(0));
        player.addExperience(circletComponent.storedXp());
        stack.remove(ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE);
    }

    public static boolean isCharged(ItemStack stack) {
        var circletComponent = stack.getOrDefault(ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE, new ParadiseLostDataComponentTypes.XpCircletChargeComponent(0));
        return circletComponent.charged();
    }


    private static int getExperienceFromPlayer(PlayerEntity player) {
        int experience = experienceForTotalLevel(player.experienceLevel);
        experience += Math.round(player.experienceProgress * experienceForNextLevel(player.experienceLevel));
        return experience;
    }

    private static int experienceForTotalLevel(int level) {
        if (level < 17)
            return level * level + 6 * level;
        else if (level < 32)
            return (int) Math.floor((2.5 * level * level) - (40.5 * level) + 360);
        else
            return (int) Math.floor((4.5 * level * level) - (162.5 * level) + 2220);
    }

    private static int experienceForNextLevel(int level) {
        if (level < 16)
            return (2 * level) + 7;
        else if (level < 31)
            return (5 * level) - 38;
        else
            return (9 * level) - 158;
    }

}
