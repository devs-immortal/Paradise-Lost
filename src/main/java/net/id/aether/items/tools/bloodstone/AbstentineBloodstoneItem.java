package net.id.aether.items.tools.bloodstone;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AbstentineBloodstoneItem extends BloodstoneItem {
    public AbstentineBloodstoneItem(Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(Text.translatable("info.the_aether.bloodstone.abstentine").formatted(Formatting.GOLD));
    }
}
