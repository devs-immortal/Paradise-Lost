package net.id.paradiselost.screen.slot;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

/**
 * A slot with a custom item "preview", like the armor slots in the Player's inventory.
 */
public class PreviewSlot extends Slot {
    private final Identifier image;
    
    public PreviewSlot(Identifier image, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.image = image;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        return Pair.of(BLOCK_ATLAS_TEXTURE, image);
    }

}
