package net.id.paradiselost.items.misc;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DeferedSherdItem extends Item {

    public DeferedSherdItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!ParadiseLostDataComponentTypes.SHERDS_AVAILABLE) return;
        if (!stack.getComponents().contains(ParadiseLostDataComponentTypes.SHERDS_STANDIN.get())) {
            if (stack.isOf(ParadiseLostItems.SOL_POTTERY_SHERD)) {
                stack.set(ParadiseLostDataComponentTypes.SHERDS_STANDIN.get(), ParadiseLost.locate("sol_pottery_pattern"));
            } else if (stack.isOf(ParadiseLostItems.COO_POTTERY_SHERD)) {
                stack.set(ParadiseLostDataComponentTypes.SHERDS_STANDIN.get(), ParadiseLost.locate("coo_pottery_pattern"));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

}
