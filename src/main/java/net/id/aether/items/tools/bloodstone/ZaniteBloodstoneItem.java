package net.id.aether.items.tools.bloodstone;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ZaniteBloodstoneItem extends BloodstoneItem {
    public ZaniteBloodstoneItem(Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(Text.translatable("info.paradise_lost.bloodstone.zanite").formatted(Formatting.GOLD));
    }
}