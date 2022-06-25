package net.id.paradiselost.items.accessories;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

// unused
public class ParadiseLostAccessory {

    private final ItemStack accessoryStack;
    private final AccessoryType accessoryType;
    private final AccessoryType extraType;

    public ParadiseLostAccessory(ItemConvertible provider, AccessoryType type) {
        this(new ItemStack(provider), type);
    }

    public ParadiseLostAccessory(ItemStack stack, AccessoryType type) {
        accessoryType = type;
        accessoryStack = stack;

        extraType = type == AccessoryType.RING ? AccessoryType.EXTRA_RING : type == AccessoryType.MISC ? AccessoryType.EXTRA_MISC : null;
    }

    public Identifier getRegistryName() {
        return Registry.ITEM.getId(accessoryStack.getItem());
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public AccessoryType getExtraType() {
        return extraType;
    }

    public ItemStack getAccessoryStack() {
        return accessoryStack;
    }
}
