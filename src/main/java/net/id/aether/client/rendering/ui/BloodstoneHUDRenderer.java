package net.id.aether.client.rendering.ui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.Arrays;
import net.id.aether.Aether;
import net.id.aether.effect.condition.Severity;
import net.id.aether.items.tools.bloodstone.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//probably can be cleaned up
public class BloodstoneHUDRenderer {
    public static void render(MatrixStack matrixStack, float delta) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof BloodstoneItem) {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains(BloodstoneCapturedData.NBT_TAG)) {
                MinecraftClient client = MinecraftClient.getInstance();
                BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromNBT(nbt.getCompound(BloodstoneCapturedData.NBT_TAG));
                if (isLookingAtMatchingEntity(client, capturedData) || doUUIDMatch(player, capturedData)) {
                    int centerX = client.getWindow().getScaledWidth() / 2;
                    int centerY = client.getWindow().getScaledHeight() / 2;

                    if (stack.getItem() instanceof AmbrosiumBloodstoneItem)
                        renderAmbrosium(matrixStack, client, capturedData, centerX, centerY);
                    else if (stack.getItem() instanceof ZaniteBloodstoneItem)
                        renderZanite(matrixStack, client, capturedData, centerX, centerY);
                    else if (stack.getItem() instanceof GravititeBloodstoneItem)
                        renderGravitite(matrixStack, client, capturedData, centerX, centerY);
                    else if (stack.getItem() instanceof AbstentineBloodstoneItem)
                        renderAbstentine(matrixStack, client, capturedData, centerX, centerY);
                }
            }
        }
    }

    private static boolean isLookingAtMatchingEntity(MinecraftClient client, BloodstoneCapturedData capturedData) {
        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.ENTITY)
            return false;
        return doUUIDMatch((LivingEntity) ((EntityHitResult) client.crosshairTarget).getEntity(), capturedData);
    }

    private static boolean doUUIDMatch(LivingEntity entity, BloodstoneCapturedData capturedData) {
        return capturedData.uuid.equals(entity.getUuid());
    }

    private static void renderAbstentine(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData, int centerX, int centerY) {
        renderRing(matrixStack, centerX, centerY);
        renderTextCentered(matrixStack, bloodstoneCapturedData.name, centerX, centerY - 80);
        for (int i = 0; i < bloodstoneCapturedData.conditionDataList.size(); i++)
            renderCondition(matrixStack, bloodstoneCapturedData.conditionDataList.get(i), centerX, centerY, getCircularPosition(80, i + 1, bloodstoneCapturedData.conditionDataList.size()));
    }

    private static void renderAmbrosium(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData, int centerX, int centerY) {
        StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
        renderRing(matrixStack, centerX, centerY);
        renderTextCentered(matrixStack, bloodstoneCapturedData.name, centerX, centerY - 80);
        renderIconWTextCentered(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.REGENERATION), new LiteralText(bloodstoneCapturedData.HP), centerX, centerY + 80);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.RESISTANCE), new LiteralText(bloodstoneCapturedData.DF), centerX - 80, centerY, false);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.ABSORPTION), new LiteralText(bloodstoneCapturedData.TF), centerX + 80, centerY, true);
    }

    private static void renderZanite(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData, int centerX, int centerY) {
        StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
        Sprite affinitySprite = statusEffectSpriteManager.getSprite(StatusEffects.BAD_OMEN).getAtlas().getSprite(Aether.locate("hud/bloodstone/affinity"));
        Sprite raceSprite = statusEffectSpriteManager.getSprite(StatusEffects.BAD_OMEN).getAtlas().getSprite(Aether.locate("hud/bloodstone/race"));

        renderRing(matrixStack, centerX, centerY);
        renderTextCentered(matrixStack, bloodstoneCapturedData.name, centerX, centerY - 80);
        renderIconWText(matrixStack, client, affinitySprite, new TranslatableText(bloodstoneCapturedData.Affinity), centerX, centerY, getCircularPosition(80, 1, 5));
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.INVISIBILITY), new LiteralText(bloodstoneCapturedData.Owner), centerX, centerY, getCircularPosition(80, 2, 5));
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.HUNGER), new LiteralText(bloodstoneCapturedData.Hunger), centerX, centerY, getCircularPosition(80, 3, 5));
        renderIconWText(matrixStack, client, raceSprite, new TranslatableText(bloodstoneCapturedData.Race), centerX + 5, centerY, getCircularPosition(80, 4, 5));
    }

    private static void renderGravitite(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData, int centerX, int centerY) {
        renderRing(matrixStack, centerX, centerY);
        renderTextCentered(matrixStack, bloodstoneCapturedData.name, centerX, centerY - 80);
        renderText(matrixStack, new TranslatableText("moa.attribute.ground_speed").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.GROUND_SPEED)), centerX, centerY, getCircularPosition(80, 1, 7));
        renderText(matrixStack, new TranslatableText("moa.attribute.gliding_speed").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.GLIDING_SPEED)), centerX, centerY, getCircularPosition(80, 2, 7));
        renderText(matrixStack, new TranslatableText("moa.attribute.gliding_decay").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.GLIDING_DECAY)), centerX, centerY, getCircularPosition(80, 3, 7));
        renderText(matrixStack, new TranslatableText("moa.attribute.jumping_strength").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.JUMPING_STRENGTH)), centerX, centerY, getCircularPosition(80, 4, 7));
        renderText(matrixStack, new TranslatableText("moa.attribute.drop_multiplier").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.DROP_MULTIPLIER)), centerX, centerY, getCircularPosition(80, 5, 7));
        renderText(matrixStack, new TranslatableText("moa.attribute.max_health").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.MAX_HEALTH)), centerX, centerY, getCircularPosition(80, 6, 7));
    }

    private static void renderCondition(MatrixStack matrixStack, BloodstoneCapturedData.ConditionData conditionData, int centerX, int centerY, Pair<Integer, Integer> circleOffsets) {
        centerX += circleOffsets.getLeft();
        centerY += circleOffsets.getRight() - 7;
        if (circleOffsets.getLeft() == 0) {
            centerX -= 130 / 2;
            centerY += 11;
        } else if (circleOffsets.getLeft() < 0)
            centerX -= 130;
        int renderWidth = (int) MathHelper.clamp(114 * conditionData.severity(), 0, 114);
        RenderSystem.setShaderTexture(0, Aether.locate("textures/hud/bloodstone/" + conditionData.id() + "_bar.png"));
        DrawableHelper.drawTexture(matrixStack, centerX, centerY, 0, 0, 18 + renderWidth, 14, 130, 14);
        RenderSystem.setShaderTexture(0, Aether.locate("textures/hud/bloodstone/condition_bar.png"));
        DrawableHelper.drawTexture(matrixStack, centerX, centerY, 0, 0, 130, 14, 130, 14);

        Text title = new TranslatableText("condition.condition." + conditionData.id()).append(" - ").append(getSeverityWithColor(conditionData.severity()));
        renderText(matrixStack, title, centerX + 17, centerY - 4, true);
    }

    public static Text getSeverityWithColor(Float rawSeverity) {
        Severity sev = Severity.getSeverity(rawSeverity);
        MutableText text = new TranslatableText(sev.getTranslationKey());
        return switch (sev) {
            case EXTREME -> text.formatted(Formatting.GRAY);
            case DIRE -> text.formatted(Formatting.RED);
            case ACUTE -> text.formatted(Formatting.YELLOW);
            case MILD -> text.formatted(Formatting.GREEN);
            case NEGLIGIBLE -> text.formatted(Formatting.AQUA);
        };
    }

    private static void renderRing(MatrixStack matrixStack, int centerX, int centerY) {
        RenderSystem.setShaderTexture(0, Aether.locate("textures/hud/bloodstone/bloodstone_ring.png"));
        DrawableHelper.drawTexture(matrixStack, centerX - 75, centerY - 75, 0, 0, 150, 150, 150, 150);
    }

    private static void renderIconWText(MatrixStack matrixStack, MinecraftClient client, Sprite sprite, Text text, int centerX, int centerY, Pair<Integer, Integer> circleOffsets) {
        renderIconWText(matrixStack, client, sprite, text, centerX + circleOffsets.getLeft(), centerY + circleOffsets.getRight(), circleOffsets.getLeft() > 0);
    }

    private static void renderIconWText(MatrixStack matrixStack, MinecraftClient client, Sprite sprite, Text text, int x, int y, boolean rightSide) {
        int totalWidth = ((sprite.getWidth() + 2 + client.textRenderer.getWidth(text)));
        int textHeight = client.textRenderer.fontHeight / 2;

        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        int startX = rightSide ? x : (x - totalWidth);
        DrawableHelper.drawSprite(matrixStack, startX, y - 9, client.inGameHud.getZOffset(), 18, 18, sprite);
        client.textRenderer.drawWithShadow(matrixStack, text, startX + sprite.getWidth() + 2, y - textHeight, 14737632);
    }

    private static void renderIconWTextCentered(MatrixStack matrixStack, MinecraftClient client, Sprite sprite, Text text, int x, int y) {
        int totalWidth = ((sprite.getWidth() + 2 + client.textRenderer.getWidth(text)));
        int textHeight = client.textRenderer.fontHeight / 2;

        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        int startX = x - (totalWidth / 2);
        DrawableHelper.drawSprite(matrixStack, startX, y - 9, client.inGameHud.getZOffset(), 18, 18, sprite);
        client.textRenderer.drawWithShadow(matrixStack, text, startX + sprite.getWidth() + 2, y - textHeight, 14737632);
    }

    private static void renderTextCentered(MatrixStack matrixStack, Text text, int x, int y) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text) / 2;
        int textHeight = MinecraftClient.getInstance().textRenderer.fontHeight / 2;
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, text, x - textWidth, y - textHeight, 14737632);
    }

    private static void renderText(MatrixStack matrixStack, Text text, int centerX, int centerY, Pair<Integer, Integer> circleOffsets) {
        renderText(matrixStack, text, centerX + circleOffsets.getRight(), centerY - circleOffsets.getLeft(), circleOffsets.getRight() > 0);
    }

    private static void renderText(MatrixStack matrixStack, Text text, int x, int y, boolean rightSide) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
        int textHeight = MinecraftClient.getInstance().textRenderer.fontHeight / 2;
        x = rightSide ? x : (x - textWidth);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, text, x, y - textHeight, 14737632);
    }

    private final static Pair<Integer, Integer>[] basicPositions = ArrayUtils.toArray(new Pair<>(0, -80), new Pair<>(80, 0), new Pair<>(-80, 0), new Pair<>(0, 80));

    private static Pair<Integer, Integer> getCircularPosition(int radius, int itemNum, int totalItems) {
        if (totalItems < 5)
            return basicPositions[itemNum];

        double angle = ((2 * Math.PI) / totalItems) * itemNum;
        int y = (int) Math.round(Math.cos(angle) * radius);
        int x = (int) Math.round(Math.sin(angle) * radius);
        return new Pair<>(x, -y);
    }
}