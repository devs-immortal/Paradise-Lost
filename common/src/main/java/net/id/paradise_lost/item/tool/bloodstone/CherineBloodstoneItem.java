package net.id.paradise_lost.item.tool.bloodstone;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class CherineBloodstoneItem extends BloodstoneItem {
    public CherineBloodstoneItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    protected List<Component> getDefaultText() {
        return ImmutableList.of(Component.translatable("info.paradise_lost.bloodstone.cherine").withStyle(ChatFormatting.GOLD));
    }
}
