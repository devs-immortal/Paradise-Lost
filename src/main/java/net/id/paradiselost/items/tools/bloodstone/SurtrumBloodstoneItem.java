package net.id.paradiselost.items.tools.bloodstone;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SurtrumBloodstoneItem extends BloodstoneItem {
    public SurtrumBloodstoneItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> getDefaultText() {
        return List.of(Text.translatable("info.paradise_lost.bloodstone.surtrum").formatted(Formatting.GOLD));
    }
}
