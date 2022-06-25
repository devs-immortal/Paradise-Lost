package net.id.paradiselost.screen.handler;

import net.id.paradiselost.screen.ParadiseLostScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public final class LoreHandler extends ScreenHandler{
    private final PlayerEntity player;
    
    public LoreHandler(int syncId, PlayerInventory playerInventory){
        super(ParadiseLostScreens.LORE, syncId);
        player = playerInventory.player;
    }
    
    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        throw new AssertionError("Please don't call me, I'm unimplemented. :-(");
    }
    
    @Override
    public boolean canUse(PlayerEntity player){
        return this.player.equals(player);
    }
}
