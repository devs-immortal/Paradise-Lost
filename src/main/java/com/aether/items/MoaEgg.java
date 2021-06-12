package com.aether.items;

import com.aether.api.AetherAPI;
import com.aether.api.moa.MoaType;
import com.aether.entities.passive.MoaEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class MoaEgg extends Item {
    public MoaEgg(Properties settings) {
        super(settings);
    }

    public static ItemStack getStack(MoaType type) {
        ItemStack stack = new ItemStack(AetherItems.MOA_EGG);
        CompoundTag tag = new CompoundTag();
        tag.putInt("moaType", AetherAPI.instance().getMoaId(type));
        stack.setTag(tag);
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext contextIn) {
        if (contextIn.getPlayer().isCreative()) {
            MoaEntity moa = new MoaEntity(contextIn.getLevel(), AetherAPI.instance().getMoa(contextIn.getItemInHand().getTag().getInt("moaType")));

            moa.moveTo(contextIn.getClickedPos().above(), 1.0F, 1.0F);
            moa.setPlayerGrown(true);

            if (!contextIn.getLevel().isClientSide) contextIn.getLevel().addFreshEntity(moa);

            return InteractionResult.SUCCESS;
        }
        return super.useOn(contextIn);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> subItems) {
        for (int moaTypeSize = 0; moaTypeSize < AetherAPI.instance().getMoaRegistrySize(); ++moaTypeSize) {
            ItemStack stack = new ItemStack(this);
            CompoundTag compound = new CompoundTag();
            MoaType moaType = AetherAPI.instance().getMoa(moaTypeSize);

            if (moaType.getItemGroup() == group || group == CreativeModeTab.TAB_SEARCH) {
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
        CompoundTag tag = stack.getTag();
        if (tag != null) return AetherAPI.instance().getMoa(tag.getInt("moaType"));
        return AetherAPI.instance().getMoa(0);
    }

    @Override
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }
}