package net.id.aether.screen.slot;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import static net.id.aether.Aether.locate;
import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

/**
 * A slot with a custom item "preview", like the armor slots in the Player's inventory.
 */
public class PreviewSlot extends Slot {
    private final Image image;
    
    public PreviewSlot(Image image, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.image = image;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        return Pair.of(BLOCK_ATLAS_TEXTURE, image.location);
    }
    
    public enum Image {
        CHEST("item/slot/chest"),
        SADDLE("item/slot/saddle"),
        ;
    
        private final Identifier location;
    
        Image(String location) {
            if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
                this.location = locate(location);
            }else{
                this.location = null;
            }
        }
    
        public Identifier location() {
            return location;
        }
    }
}
