package net.id.aether.screen.handler;

import net.id.aether.screen.AetherScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public final class LoreHandler extends ScreenHandler{
    private final PlayerEntity player;
    
    public LoreHandler(int syncId, PlayerInventory playerInventory){
        super(AetherScreens.LORE, syncId);
        this.player = playerInventory.player;
    }
    
    @Override
    public boolean canUse(PlayerEntity player){
        return this.player.equals(player);
    }
}
