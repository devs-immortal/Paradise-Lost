package net.id.paradise_lost.client.screen;

import net.minecraft.client.renderer.RenderType;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.screen.handler.MoaScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static net.id.paradise_lost.ModConstants.id;

public class MoaScreen extends AbstractContainerScreen<MoaScreenHandler> {
    private static final ResourceLocation TEXTURE = id("textures/gui/container/moa.png");
    private final MoaEntity moa;

    public MoaScreen(MoaScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        moa = handler.moa();
        imageWidth = 176;
        imageHeight = 184;
        inventoryLabelY += 18;
    }

    @Override
    public void render(GuiGraphics matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) >> 1;
        int y = (height - imageHeight) >> 1;
        context.blit(RenderType::guiTextured, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.hasMoaInventory()) {
            context.blit(RenderType::guiTextured, TEXTURE, x + 79, y + 17, 0, 184, 90, 72, 256, 256);
        }

        InventoryScreen.renderEntityInInventoryFollowsMouse(context, x + 26, y + 18, x + 78, y + 70, 17, 0.25F, mouseX, mouseY, moa);
    }
}
