package com.aether.items;

import com.aether.api.AetherAPI;
import com.aether.api.moa.MoaType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;

public class MoaEgg extends Item {

    public MoaEgg() {
        super(new Item.Settings().maxCount(1).group(AetherItemGroups.Misc));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext contextIn) {
        if (contextIn.getPlayer().abilities.creativeMode) {

            //TODO: Spawn Moa

            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(contextIn);
    }

    public static ItemStack getStack(MoaType type) {
        ItemStack stack = new ItemStack(AetherItems.MOA_EGG);
        CompoundTag tag = new CompoundTag();
        tag.putInt("moaType", AetherAPI.instance().getMoaId(type));
        stack.setTag(tag);
        return stack;
    }

    @Override
    public boolean shouldSyncTagToClient() {
        return true;
    }
}