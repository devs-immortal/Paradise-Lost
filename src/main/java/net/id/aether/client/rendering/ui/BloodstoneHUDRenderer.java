package net.id.aether.client.rendering.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.effect.condition.Severity;
import net.id.aether.items.tools.bloodstone.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
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

@Environment(EnvType.CLIENT)
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
                if (client.currentScreen == null && isLookingAtMatchingEntity(client, capturedData) || doUUIDMatch(player, capturedData)) {
                    matrixStack.push();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    matrixStack.translate(client.getWindow().getScaledWidth() / 2f, client.getWindow().getScaledHeight() / 2f, 0);
                    if (stack.getItem() instanceof AmbrosiumBloodstoneItem)
                        renderAmbrosium(matrixStack, client, capturedData);
                    else if (stack.getItem() instanceof ZaniteBloodstoneItem)
                        renderZanite(matrixStack, client, capturedData);
                    else if (stack.getItem() instanceof GravititeBloodstoneItem)
                        renderGravitite(matrixStack, client, capturedData);
                    else if (stack.getItem() instanceof AbstentineBloodstoneItem)
                        renderAbstentine(matrixStack, client, capturedData);
                    RenderSystem.disableBlend();
                    matrixStack.pop();
                }
            }
        }
    }

    private static boolean isLookingAtMatchingEntity(MinecraftClient client, BloodstoneCapturedData capturedData) {
        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.ENTITY)
            return false;

        if(((EntityHitResult) client.crosshairTarget).getEntity() instanceof LivingEntity entity)
            return doUUIDMatch(entity, capturedData);

        return false;
    }

    private static boolean doUUIDMatch(LivingEntity entity, BloodstoneCapturedData capturedData) {
        return capturedData.uuid.equals(entity.getUuid());
    }

    private static void renderAmbrosium(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
        renderRing(matrixStack, 0, 0);
        renderText(matrixStack, client, bloodstoneCapturedData.name, 0, -80);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.REGENERATION), new LiteralText(bloodstoneCapturedData.HP), 0, 80);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.RESISTANCE), new LiteralText(bloodstoneCapturedData.DF), -80, 0);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.ABSORPTION), new LiteralText(bloodstoneCapturedData.TF), 80, 0);
    }

    private static void renderZanite(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
        Sprite affinitySprite = statusEffectSpriteManager.getSprite(StatusEffects.BAD_OMEN).getAtlas().getSprite(Aether.locate("hud/bloodstone/affinity"));
        Sprite raceSprite = statusEffectSpriteManager.getSprite(StatusEffects.BAD_OMEN).getAtlas().getSprite(Aether.locate("hud/bloodstone/race"));

        renderRing(matrixStack, 0, 0);
        renderText(matrixStack, client, bloodstoneCapturedData.name, 0, -80);

        renderIconWText(matrixStack, client, affinitySprite, new TranslatableText(bloodstoneCapturedData.Affinity), 76, -25);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.INVISIBILITY), new LiteralText(bloodstoneCapturedData.Owner), 47, 65);
        renderIconWText(matrixStack, client, statusEffectSpriteManager.getSprite(StatusEffects.HUNGER), new LiteralText(bloodstoneCapturedData.Hunger), -47, 65);
        renderIconWText(matrixStack, client, raceSprite, new TranslatableText(bloodstoneCapturedData.Race), -76, -25);
    }

    private static void renderGravitite(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        renderRing(matrixStack, 0, 0);
        renderText(matrixStack, client, bloodstoneCapturedData.name, 0, -80);
        renderText(matrixStack, client, new TranslatableText("moa.attribute.ground_speed").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.GROUND_SPEED)), 63, -50);
        renderText(matrixStack, client, new TranslatableText("moa.attribute.gliding_speed").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.GLIDING_SPEED)), 80, 0);
        renderText(matrixStack, client, new TranslatableText("moa.attribute.gliding_decay").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.GLIDING_DECAY)), 63, 50);
        renderText(matrixStack, client, new TranslatableText("moa.attribute.jumping_strength").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.JUMPING_STRENGTH)), -63, -50);
        renderText(matrixStack, client, new TranslatableText("moa.attribute.drop_multiplier").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.DROP_MULTIPLIER)), -80, 0);
        renderText(matrixStack, client, new TranslatableText("moa.attribute.max_health").append(": ").append(bloodstoneCapturedData.getRatingWithColor(bloodstoneCapturedData.MAX_HEALTH)), -63, 50);
    }

    private static void renderAbstentine(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        renderRing(matrixStack, 0, 0);
        renderText(matrixStack, client, bloodstoneCapturedData.name, 0, -80);
        for (int i = 0; i < bloodstoneCapturedData.conditionDataList.size(); i++) {
            Pair<Integer, Integer> offset = getCircularPosition(80, i + 1, bloodstoneCapturedData.conditionDataList.size());
            renderCondition(matrixStack, client, bloodstoneCapturedData.conditionDataList.get(i), offset.getLeft(), offset.getRight());
        }
    }

    private static void renderCondition(MatrixStack matrixStack, MinecraftClient client, BloodstoneCapturedData.ConditionData conditionData, int offsetX, int offsetY) {
        if (offsetX == 0) offsetX = 109 / 2;
        else if (offsetX < 0) offsetX = offsetX - (109 / 2);

        int renderWidth = (int) MathHelper.clamp((109 - 15) * conditionData.severity(), 0, (109 - 15));
        matrixStack.push();
        matrixStack.translate(offsetX, offsetY, 0);
        matrixStack.translate(-54.5, -6, 0);
        matrixStack.scale(1.15f, 1.15f, 1);
        matrixStack.translate(54.5, 6, 0);
        RenderSystem.setShaderTexture(0, Aether.locate("textures/hud/bloodstone/" + conditionData.id() + "_bar.png"));
        DrawableHelper.drawTexture(matrixStack, -7, -7, 0, 0, 15 + renderWidth, 12, 109, 12);
        RenderSystem.setShaderTexture(0, Aether.locate("textures/hud/bloodstone/condition_bar.png"));
        DrawableHelper.drawTexture(matrixStack, -7, -7, 0, 0, 109, 12, 109, 12);
        matrixStack.pop();

        Text title = new TranslatableText("condition.condition." + conditionData.id()).append(" - ").append(getSeverityWithColor(conditionData.severity()));
        renderText(matrixStack, client, title, offsetX + 17, offsetY - 9);
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

    private static void renderRing(MatrixStack matrixStack, int offsetX, int offsetY) {
        RenderSystem.setShaderTexture(0, Aether.locate("textures/hud/bloodstone/bloodstone_ring.png"));
        DrawableHelper.drawTexture(matrixStack, offsetX - 75, offsetY - 75, 0, 0, 150, 150, 150, 150);
    }

    private static void renderIconWText(MatrixStack matrixStack, MinecraftClient client, Sprite sprite, Text text, int offsetX, int offsetY) {
        int totalWidth = ((sprite.getWidth() + 2 + client.textRenderer.getWidth(text)));
        int totalHeight = client.textRenderer.fontHeight / 2;

        int startX = offsetX;
        if (offsetX == 0) startX = offsetX - (totalWidth / 2);
        else if (offsetX < 0) startX = (offsetX - totalWidth);

        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        DrawableHelper.drawSprite(matrixStack, startX, offsetY - 9, client.inGameHud.getZOffset(), 18, 18, sprite);
        client.textRenderer.drawWithShadow(matrixStack, text, startX + sprite.getWidth() + 2, offsetY - totalHeight, 14737632);
    }

    private static void renderText(MatrixStack matrixStack, MinecraftClient client, Text text, int offsetX, int offsetY) {
        int totalWidth = client.textRenderer.getWidth(text);
        int totalHeight = client.textRenderer.fontHeight / 2;

        int startX = offsetX;
        if (offsetX == 0) startX = offsetX - (totalWidth / 2);
        else if (offsetX < 0) startX = (offsetX - totalWidth);

        client.textRenderer.drawWithShadow(matrixStack, text, startX, offsetY - totalHeight, 14737632);
    }

    private static Pair<Integer, Integer> getCircularPosition(int radius, int itemNum, int totalItems) {
        if (totalItems < 5) {
            return switch (itemNum) {
                case 0 -> new Pair<>(0, -80);
                case 1 -> new Pair<>(80, 0);
                case 2 -> new Pair<>(-80, 0);
                case 3 -> new Pair<>(0, 80);
                default -> new Pair<>(0, 0);
            };
        }
        double angle = ((2 * Math.PI) / totalItems) * itemNum;
        int y = (int) Math.round(Math.cos(angle) * radius);
        int x = (int) Math.round(Math.sin(angle) * radius);
        return new Pair<>(x, -y);
    }
}
