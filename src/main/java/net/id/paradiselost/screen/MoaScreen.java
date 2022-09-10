package net.id.paradiselost.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.screen.handler.MoaScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.id.paradiselost.ParadiseLost.locate;

/**
 * The screen for the Moa's inventory.
 */
@Environment(EnvType.CLIENT)
public class MoaScreen extends HandledScreen<MoaScreenHandler> {
    private static final Identifier TEXTURE = locate("textures/gui/container/moa.png");
    private final MoaEntity moa;
    
    public MoaScreen(MoaScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        moa = handler.moa();
        
        backgroundWidth = 176;
        backgroundHeight = 184;
        playerInventoryTitleY += 18;
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
    
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) >> 1;
        int y = (height - backgroundHeight) >> 1;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        
        if(handler.hasMoaInventory()) {
            drawTexture(matrices, x + 79, y + 17, 0, 184, 90, 72);
        }
    
        //FIXME This would prevent the entity from escaping the box bounds
        var scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        //RenderSystem.enableScissor(this.x + (int) ((x + 26) * scale), this.y + (int) ((y + 18) * scale), (int) (52 * scale), (int) (52 * scale));
        try {
            InventoryScreen.drawEntity(x + 51, y + 60, 17, x + 51 - mouseX, y + 25 - mouseY, moa);
        } finally {
            RenderSystem.disableScissor();
        }
    }
}
