package com.aether.items;

import com.aether.api.AetherAPI;
import com.aether.api.moa.MoaType;
import com.aether.entities.passive.MoaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;

public class MoaEgg extends Item {
    public MoaEgg(Settings settings) {
        super(settings);
    }

    public static ItemStack getStack(MoaType type) {
        ItemStack stack = new ItemStack(AetherItems.MOA_EGG);
        NbtCompound tag = new NbtCompound();
        tag.putInt("moaType", AetherAPI.instance().getMoaId(type));
        stack.setTag(tag);
        return stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext contextIn) {
        if (contextIn.getPlayer().isCreative()) {
            MoaEntity moa = new MoaEntity(contextIn.getWorld(), AetherAPI.instance().getMoa(contextIn.getStack().getTag().getInt("moaType")));

            moa.refreshPositionAndAngles(contextIn.getBlockPos().up(), 1.0F, 1.0F);
            moa.setPlayerGrown(true);

            if (!contextIn.getWorld().isClient) contextIn.getWorld().spawnEntity(moa);

            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(contextIn);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> subItems) {
        for (int moaTypeSize = 0; moaTypeSize < AetherAPI.instance().getMoaRegistrySize(); ++moaTypeSize) {
            ItemStack stack = new ItemStack(this);
            NbtCompound compound = new NbtCompound();
            MoaType moaType = AetherAPI.instance().getMoa(moaTypeSize);

            if (moaType.getItemGroup() == group || group == ItemGroup.SEARCH) {
                compound.putInt("moaType", moaTypeSize);
                stack.setTag(compound);
                subItems.add(stack);
            }
        }
    }

    public int getColor(ItemStack stack) {
        return this.getMoaType(stack).getMoaEggColor();
    }

    public MoaType getMoaType(ItemStack stack) {
        NbtCompound tag = stack.getTag();
        if (tag != null) return AetherAPI.instance().getMoa(tag.getInt("moaType"));
        return AetherAPI.instance().getMoa(0);
    }

    @Override
    public boolean shouldSyncTagToClient() {
        return true;
    }
}