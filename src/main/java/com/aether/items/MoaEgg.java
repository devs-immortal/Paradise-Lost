package com.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
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

    @Override
    public boolean shouldSyncTagToClient() {
        return true;
    }
}