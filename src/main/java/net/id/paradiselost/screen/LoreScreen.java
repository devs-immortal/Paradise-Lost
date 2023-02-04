package net.id.paradiselost.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.component.ParadiseLostComponents;
import net.id.paradiselost.lore.LoreStatus;
import net.id.paradiselost.lore.LoreType;
import net.id.paradiselost.registry.ParadiseLostRegistries;
import net.id.paradiselost.screen.handler.LoreHandler;
import net.id.paradiselost.screen.util.FloatyDrawableHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static net.id.paradiselost.ParadiseLost.locate;

// TODO: Requirements, requirement lines and status
@Environment(EnvType.CLIENT)
public final class LoreScreen extends HandledScreen<LoreHandler> {
    public static final Identifier WIDGET_TEXTURE = locate("textures/gui/lore/widgets.png");
    
    private static final LoreEntry LORE_TEST = new LoreEntry(0, 0, LoreType.NORMAL, Items.DIAMOND, Text.of("Test!"), Text.of("This is a test lore entry.\nWhat fun!"));
    private static final LoreEntry PARENT_TEST = new LoreEntry(64, 0, LoreType.RARE, Items.EMERALD, Text.of("Parent Test"), Text.of("This is a test of the lineage system"), LORE_TEST);
    
    private final int viewportWidth = 200;
    private final int viewportHeight = 200;
    
    private final List<LoreEntry> loreEntries;
    
    private int scrollX = 0;
    private int scrollY = 0;
    private int scrollLimitX1 = 0;
    private int scrollLimitY1 = 0;
    private int scrollLimitX0 = 0;
    private int scrollLimitY0 = 0;
    
    private LoreEntry hoveredEntry = null;
    
    public LoreScreen(LoreHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        
        backgroundWidth = viewportWidth;
        backgroundHeight = viewportHeight;
    
        var state = ParadiseLostComponents.LORE_STATE.get(MinecraftClient.getInstance().player);
        loreEntries = ParadiseLostRegistries.LORE_REGISTRY.stream().map((entry) -> {
            var status = state.getLoreStatus(entry);
            if (status == LoreStatus.FREE || status == LoreStatus.UNLOCKED || status == LoreStatus.COMPLETED) {
                return new LoreEntry(entry);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).toList();
        
        var coreLoreType = LoreType.NORMAL;
        scrollX = (viewportWidth - coreLoreType.getWidth()) >> 2;
        scrollY = (viewportHeight - coreLoreType.getHeight()) >> 1;
    }
    
    @Override
    protected void init() {
        super.init();
    
        int scrollLimitX1 = 0;
        int scrollLimitY1 = 0;
        int scrollLimitX0 = 0;
        int scrollLimitY0 = 0;
    
        for (LoreEntry loreEntry : loreEntries) {
            scrollLimitX0 = Math.min(scrollLimitX0, loreEntry.x);
            scrollLimitY0 = Math.min(scrollLimitY0, loreEntry.y);
            scrollLimitX1 = Math.max(scrollLimitX1, loreEntry.x + loreEntry.type.getWidth());
            scrollLimitY1 = Math.max(scrollLimitY1, loreEntry.y + loreEntry.type.getHeight());
        }
        
        this.scrollLimitX0 = scrollLimitX0 - (viewportWidth >> 1);
        this.scrollLimitY0 = scrollLimitY0 - (viewportHeight >> 1);
        this.scrollLimitX1 = scrollLimitX1 + (viewportWidth >> 1);
        this.scrollLimitY1 = scrollLimitY1 + (viewportHeight >> 1);
        
        this.scrollX = MathHelper.clamp(scrollX, scrollLimitX0, scrollLimitX1);
        this.scrollY = MathHelper.clamp(scrollX, scrollLimitY0, scrollLimitY1);
    }
    
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    }
    
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        matrices.push();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, WIDGET_TEXTURE);
    
        drawFrame(matrices);
    
        var scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor();
        RenderSystem.enableScissor(
                (int) (x * scaleFactor),
                (int) ((height - y - backgroundHeight) * scaleFactor),
                (int) ((backgroundWidth) * scaleFactor),
                (int) ((backgroundHeight) * scaleFactor)
        );
    
        var filterState = new Object() {
            int mouseX;
            int mouseY;
        };
        filterState.mouseX = mouseX - this.x - scrollX;
        filterState.mouseY = mouseY - this.y - scrollY;
        var currentlyHovered = loreEntries.stream().filter((entry) ->
                filterState.mouseX >= entry.x && filterState.mouseX <= entry.x + entry.type.getWidth()
                        && filterState.mouseY >= entry.y && filterState.mouseY <= entry.y + entry.type.getHeight()
        ).findFirst().orElse(null);
        if (hoveredEntry != currentlyHovered) {
            if (hoveredEntry != null) {
                hoveredEntry.setHovered(false);
            }
            hoveredEntry = currentlyHovered;
            if (hoveredEntry != null) {
                hoveredEntry.setHovered(true);
            }
        }
        
        int offsetX = x + scrollX;
        int offsetY = y + scrollY;
    
        loreEntries.forEach((entry) -> entry.tick(delta));
        
//        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
//        RenderSystem.setShaderColor(1.0F, 0.0F, 1.0F, 1.0F);

        /*
        var entityBuilders = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        var lineBuffer = entityBuilders.getBuffer(RenderLayer.getLines());
        loreEntries.forEach((entry)->entry.drawLines(matrices, lineBuffer, offsetX, offsetY));
        entityBuilders.draw();
         */
    
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, WIDGET_TEXTURE);
        RenderSystem.enableBlend();
        drawBorder(matrices);
        loreEntries.forEach((entry) -> entry.drawBackground(matrices, offsetX, offsetY));
        loreEntries.forEach((entry) -> entry.drawForeground(matrices, offsetX, offsetY));
    
        setZOffset(200);
        itemRenderer.zOffset = 200;
        RenderSystem.enableDepthTest();
        loreEntries.forEach((entry) -> entry.drawStack(offsetX, offsetY));
        setZOffset(0);
        itemRenderer.zOffset = 0;
        RenderSystem.disableDepthTest();
        
        RenderSystem.disableScissor();
    }
    
    private int dragStartX;
    private int dragStartY;
    private int scrollStartX;
    private int scrollStartY;
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        dragStartX = (int) mouseX;
        dragStartY = (int) mouseY;
        scrollStartX = scrollX;
        scrollStartY = scrollY;
        
        return false;
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        scrollX = MathHelper.clamp((int) (scrollStartX + mouseX - dragStartX), scrollLimitX0, scrollLimitX1);
        scrollY = MathHelper.clamp((int) (scrollStartY + mouseY - dragStartY), scrollLimitY0, scrollLimitY1);
        
        return true;
    }
    
    private void drawFrame(MatrixStack matrices) {
        int x = this.x;
        int y = this.y;
        int width = backgroundWidth;
        int height = backgroundHeight;
        
        matrices.push();
        matrices.translate(x, y, 0);
    
        // Top left corner
        drawTexture(matrices, -8, -8, 8, 8, 0, 0, 8, 8, 256, 256);
        // Top right corner
        drawTexture(matrices, width, -8, 8, 8, 9, 0, 8, 8, 256, 256);
        // Bottom right corner
        drawTexture(matrices, width, height, 8, 8, 9, 9, 8, 8, 256, 256);
        // Bottom left corner
        drawTexture(matrices, -8, height, 8, 8, 0, 9, 8, 8, 256, 256);
    
        // Top middle
        drawTexture(matrices, 0, -8, width, 8, 8, 0, 1, 8, 256, 256);
        // Left middle
        drawTexture(matrices, -8, 0, 8, height, 0, 8, 8, 1, 256, 256);
        // Right middle
        drawTexture(matrices, width, 0, 8, height, 9, 8, 8, 1, 256, 256);
        // Bottom middle
        drawTexture(matrices, 0, height, width, 8, 8, 9, 1, 8, 256, 256);
    
        // Middle
        drawTexture(matrices, 0, 0, width, height, 8, 8, 1, 1, 256, 256);
    
        matrices.pop();
    }
    
    //FIXME Figure out why this is so far off.
    private void drawBorder(MatrixStack matrices) {
        matrices.push();
        matrices.translate(x + scrollLimitX0 + scrollX - 69, y + scrollLimitX0 + scrollY - 5, 0);
        int width = scrollLimitX1 - scrollLimitX0 + viewportWidth;
        int height = scrollLimitY1 - scrollLimitY0 + viewportHeight;
    
        // Top left corner
        drawTexture(matrices, 0, 0, 4, 4, 0, 72, 4, 4, 256, 256);
        // Top right corner
        drawTexture(matrices, width - 4, 0, 4, 4, 5, 72, 4, 4, 256, 256);
        // Bottom right corner
        drawTexture(matrices, width - 4, height - 4, 4, 4, 5, 77, 4, 4, 256, 256);
        // Bottom left corner
        drawTexture(matrices, 0, height - 4, 4, 4, 0, 77, 4, 4, 256, 256);
        
        // Top middle
        drawTexture(matrices, 4, 0, width - 8, 4, 4, 72, 1, 4, 256, 256);
        // Left middle
        drawTexture(matrices, 0, 4, 4, height - 8, 0, 76, 4, 1, 256, 256);
        // Right middle
        drawTexture(matrices, width - 4, 4, 4, height - 8, 5, 76, 4, 1, 256, 256);
        // Bottom middle
        drawTexture(matrices, 4, height - 4, width - 8, 4, 4, 77, 1, 4, 256, 256);
        
        matrices.pop();
    }
    
    private static final class LoreEntry implements FloatyDrawableHelper {
        private static final int HOVER_LIMIT = 20;
        
        private final int x;
        private final int y;
        private final @NotNull LoreType type;
        private final @NotNull ItemStack stack;
        private final @NotNull Text title;
        private final List<OrderedText> description;
        private final @NotNull Set<@NotNull LoreEntry> requirements;
        
        private final int width;
        private final int height;
    
        private boolean hover = false;
        private float hoverTime = 0;
        
        private LoreEntry(int x, int y, @NotNull LoreType type, @NotNull ItemStack stack, @NotNull Text title, @NotNull Text description, @NotNull Set<@NotNull LoreEntry> requirements) {
            Objects.requireNonNull(type, "type was null");
            Objects.requireNonNull(stack, "stack was null");
            Objects.requireNonNull(title, "title was null");
            Objects.requireNonNull(description, "description was null");
            Objects.requireNonNull(requirements, "requirements was null");
            this.x = x - (type.getWidth() >> 1);
            this.y = y - (type.getHeight() >> 1);
            this.type = type;
            this.stack = stack;
            
            if (title instanceof MutableText mutableTitle) {
                this.title = mutableTitle.setStyle(title.getStyle().withBold(true));
            } else {
                this.title = title;
            }
            this.requirements = requirements;
            
            var textRenderer = MinecraftClient.getInstance().textRenderer;
            this.width = Math.max(textRenderer.getWidth(this.title), 100) + 2;
    
            this.description = textRenderer.wrapLines(description, width);
            
            this.height = 9 * (this.description.size() + 1);
        }
    
        private LoreEntry(int x, int y, @NotNull LoreType type, @NotNull ItemStack stack, @NotNull Text title, @NotNull Text description, @NotNull LoreEntry @NotNull... requirements) {
            this(
                    x, y, type, stack, title, description,
                    Set.of(Objects.requireNonNull(requirements, "requirements was null"))
            );
        }
    
        private LoreEntry(int x, int y, @NotNull LoreType type, @NotNull ItemConvertible item, @NotNull Text title, @NotNull Text description) {
            this(
                    x, y, type,
                    new ItemStack(Objects.requireNonNull(item, "item was null")),
                    title, description, Set.of()
            );
        }
    
        private LoreEntry(int x, int y, @NotNull LoreType type, @NotNull ItemConvertible item, @NotNull Text title, @NotNull Text description, @NotNull LoreEntry @NotNull... requirements) {
            this(
                    x, y, type,
                    new ItemStack(Objects.requireNonNull(item, "item was null")),
                    title, description,
                    Set.of(Objects.requireNonNull(requirements, "requirements was null"))
            );
        }
    
        LoreEntry(net.id.paradiselost.lore.LoreEntry<?> entry) {
            this(entry.x(), entry.y(), LoreType.NORMAL, entry.stack(), entry.getTitleText(), entry.getDescriptionText());
        }
    
        public void setHovered(boolean hovered) {
            hover = hovered;
        }
    
        public void tick(float delta) {
            if (hover) {
                if (hoverTime < HOVER_LIMIT) {
                    hoverTime = Math.min(hoverTime + delta, HOVER_LIMIT);
                }
            } else {
                if (hoverTime > 0) {
                    hoverTime = Math.max(hoverTime - delta, 0);
                }
            }
        }
        
        private void drawLines(MatrixStack matrices, VertexConsumer lineBuffer, int x, int y) {
            if (!requirements.isEmpty()) {
                int startX = this.x + x;
                int startY = this.y + y;
                var entry = matrices.peek();
                var model = entry.getPositionMatrix();
                var normal = entry.getNormalMatrix();
                for (var child : requirements) {
                    lineBuffer.vertex(model, startX, startY, 0).color(1, 0, 1, 0).normal(normal, 0, 1, 0).next();
                    lineBuffer.vertex(model, child.x + x, child.y + y, 0).color(1, 0, 1, 0).normal(normal, 0, 1, 0).next();
                }
                /*
                vertexConsumer.vertex(entry.getModel(), (float)(k + d), (float)(l + e), (float)(m + f)).color(g, h, i, j).normal(entry.getNormal(), q, r, s).next();
			    vertexConsumer.vertex(entry.getModel(), (float)(n + d), (float)(o + e), (float)(p + f)).color(g, h, i, j).normal(entry.getNormal(), q, r, s).next();
                 */
            }
        }
        
        private void drawBackground(MatrixStack matrices, int x, int y) {
            x += this.x;
            y += this.y;
        
            if (hover) {
                drawTexture(matrices, x, y, type.getU() + type.getWidth(), type.getV(), type.getWidth(), type.getHeight());
            } else {
                drawTexture(matrices, x, y, type.getU(), type.getV(), type.getWidth(), type.getHeight());
            }
            
            if (hoverTime > 0) {
                float boxWidth = width;
                boxWidth *= Math.min(hoverTime / HOVER_LIMIT, HOVER_LIMIT);
                boxWidth += 8;
    
                float boxHeight = height;
                boxHeight *= Math.min(hoverTime / HOVER_LIMIT, HOVER_LIMIT);
                boxHeight += 8;
    
                matrices.push();
                matrices.translate(0, 0, 1);
                drawFrame(matrices, x, y + type.getHeight(), boxWidth, boxHeight);
                matrices.pop();
            }
        }
        
        private void drawForeground(MatrixStack matrices, int x, int y) {
            if (hoverTime > 0) {
                matrices.push();
                
                float scale = hoverTime / HOVER_LIMIT;
                matrices.translate(this.x + x + 4, this.y + y + type.getHeight() + 4, 1);
                matrices.scale(scale, scale, 1);
                
                /*x += this.x;
                y += this.y;*/
                
                var textRenderer = MinecraftClient.getInstance().textRenderer;
        
                // public int draw(MatrixStack matrices, Text text, float x, float y, int color) {
                textRenderer.drawWithShadow(matrices, title, 1, 1, 0xFFFFFF);
                int yOff = 10;
                for (var line : description) {
                    textRenderer.drawWithShadow(matrices, line, 1, yOff, 0xFFFFFF);
                    yOff += 9;
                }
                matrices.pop();
            }
        }
    
        private void drawStack(int x, int y) {
            x += this.x + type.getItemX();
            y += this.y + type.getItemY();
            
            var client = MinecraftClient.getInstance();
            var itemRenderer = client.getItemRenderer();
            var textRenderer = client.textRenderer;
            
            itemRenderer.renderInGuiWithOverrides(stack, x, y);
            itemRenderer.renderGuiItemOverlay(textRenderer, stack, x, y, "");
        }
        
        private void drawFrame(MatrixStack matrices, int x, int y, float width, float height) {
            matrices.push();
            matrices.translate(x, y, 0);
    
            // Top left corner
            drawTexture(matrices, 0, 0, 4, 4, 17, 0, 4, 4, 256, 256);
            // Top right corner
            drawTexture(matrices, width - 4, 0, 4, 4, 22, 0, 4, 4, 256, 256);
            // Bottom right corner
            drawTexture(matrices, width - 4, height - 4, 4, 4, 22, 5, 4, 4, 256, 256);
            // Bottom left corner
            drawTexture(matrices, 0, height - 4, 4, 4, 17, 5, 4, 4, 256, 256);
    
            // Top middle
            drawTexture(matrices, 4, 0, width - 8, 4, 21, 0, 1, 4, 256, 256);
            // Left middle
            drawTexture(matrices, 0, 4, 4, height - 8, 17, 4, 4, 1, 256, 256);
            // Right middle
            drawTexture(matrices, width - 4, 4, 4, height - 8, 22, 4, 4, 1, 256, 256);
            // Bottom middle
            drawTexture(matrices, 4, height - 4, width - 8, 4, 21, 5, 1, 4, 256, 256);
    
            // Middle
            drawTexture(matrices, 4, 4, width - 8, height - 8, 21, 4, 1, 1, 256, 256);
    
            matrices.pop();
        }
    }
}
