package net.id.paradiselost.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.screen.handler.MoaScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
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
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
    
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) >> 1;
        int y = (height - backgroundHeight) >> 1;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        
        if (handler.hasMoaInventory()) {
            context.drawTexture(TEXTURE, x + 79, y + 17, 0, 184, 90, 72);
        }

        InventoryScreen.drawEntity(context, x + 26, y + 18, x + 78, y + 70, 17, 0.25F, mouseX, mouseY, moa);
    }
}
