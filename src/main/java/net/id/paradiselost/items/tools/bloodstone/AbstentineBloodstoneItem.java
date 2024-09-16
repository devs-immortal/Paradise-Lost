package net.id.paradiselost.items.tools.bloodstone;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AbstentineBloodstoneItem extends BloodstoneItem {
    public AbstentineBloodstoneItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(Text.translatable("info.paradise_lost.bloodstone.abstentine").formatted(Formatting.GOLD));
    }
}
