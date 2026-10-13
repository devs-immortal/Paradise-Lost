package net.id.paradise_lost.screen.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

public class PreviewSlot extends Slot {
    private final ResourceLocation image;

    public PreviewSlot(ResourceLocation image, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.image = image;
    }

    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(BLOCK_ATLAS, image);
    }

}
