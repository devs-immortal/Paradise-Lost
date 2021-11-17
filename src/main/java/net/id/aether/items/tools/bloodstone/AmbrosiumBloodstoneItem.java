package net.id.aether.items.tools.bloodstone;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class AmbrosiumBloodstoneItem extends BloodstoneItem {
    public AmbrosiumBloodstoneItem(Settings settings) {
        super(settings);
    }

    @Override
    protected List<Text> getDefaultText() {
        return ImmutableList.of(new TranslatableText("info.the_aether.bloodstone.ambrosium").formatted(Formatting.GOLD));
    }
}
