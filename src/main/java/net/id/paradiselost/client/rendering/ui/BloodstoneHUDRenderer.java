package net.id.paradiselost.client.rendering.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.api.MoaAPI;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.items.tools.bloodstone.*;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
//probably can be cleaned up
public class BloodstoneHUDRenderer {
    public static void render(DrawContext context) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof BloodstoneItem) {
            var hasBloodstoneData = stack.getComponents().stream().anyMatch((c) -> c.type() == ParadiseLostDataComponentTypes.MOA_GENES || c.type() == ParadiseLostDataComponentTypes.BLOODSTONE);
            if (hasBloodstoneData) {
                MinecraftClient client = MinecraftClient.getInstance();
                BloodstoneCapturedData capturedData = BloodstoneCapturedData.fromComponents(stack);
                if (client.currentScreen == null && isLookingAtMatchingEntity(client, capturedData) || doUUIDMatch(player, capturedData)) {
                    var matrixStack = context.getMatrices();
                    matrixStack.push();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    matrixStack.translate(client.getWindow().getScaledWidth() / 2f, client.getWindow().getScaledHeight() / 2f, 0);
                    if (stack.getItem() instanceof CherineBloodstoneItem) {
                        renderCherine(context, client, capturedData);
                    } else if (stack.getItem() instanceof OlviteBloodstoneItem) {
                        renderOlvite(context, client, capturedData);
                    } else if (stack.getItem() instanceof SurtrumBloodstoneItem) {
                        renderSurtrum(context, client, capturedData);
                    }
                    RenderSystem.disableBlend();
                    matrixStack.pop();
                }
            }
        }
    }

    private static boolean isLookingAtMatchingEntity(MinecraftClient client, BloodstoneCapturedData capturedData) {
        if (
                client.crosshairTarget == null
                || client.crosshairTarget.getType() != HitResult.Type.ENTITY
                || !(((EntityHitResult) client.crosshairTarget).getEntity() instanceof LivingEntity)
        ) {
            return false;
        }

        if (((EntityHitResult) client.crosshairTarget).getEntity() instanceof LivingEntity entity) {
            return doUUIDMatch(entity, capturedData);
        }

        return false;
    }

    private static boolean doUUIDMatch(LivingEntity entity, BloodstoneCapturedData capturedData) {
        return capturedData.bloodstoneComponent.uuid().equals(entity.getUuid());
    }

    private static void renderCherine(DrawContext context, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
        renderRing(context, 0, 0);
        var component = bloodstoneCapturedData.bloodstoneComponent;
        renderText(context, client, component.name(), 0, -80);
        renderIconWText(context, client, statusEffectSpriteManager.getSprite(StatusEffects.REGENERATION), Text.literal(component.health()), 0, 80);
        renderIconWText(context, client, statusEffectSpriteManager.getSprite(StatusEffects.RESISTANCE), Text.literal(component.defense()), -80, 0);
        renderIconWText(context, client, statusEffectSpriteManager.getSprite(StatusEffects.ABSORPTION), Text.literal(component.toughness()), 80, 0);
    }

    private static void renderOlvite(DrawContext context, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
        var effectAtlas = client.getSpriteAtlas(new Identifier("textures/atlas/blocks.png"));
        Sprite affinitySprite = effectAtlas.apply(ParadiseLost.locate("item/icons/affinity"));
        Sprite raceSprite = effectAtlas.apply(ParadiseLost.locate("item/icons/race"));

        renderRing(context, 0, 0);
        renderText(context, client, bloodstoneCapturedData.bloodstoneComponent.name(), 0, -80);

        if (bloodstoneCapturedData.moaGeneComponent != null) {
            renderIconWText(context, client, affinitySprite, Text.translatable(bloodstoneCapturedData.moaGeneComponent.affinity()), 76, -25);
            renderIconWText(context, client, statusEffectSpriteManager.getSprite(StatusEffects.INVISIBILITY), Text.literal(bloodstoneCapturedData.bloodstoneComponent.owner()), 47, 65);
            renderIconWText(context, client, statusEffectSpriteManager.getSprite(StatusEffects.HUNGER), Text.literal(String.format("%.1f", bloodstoneCapturedData.moaGeneComponent.hunger()) + "/" + 100.0), -47, 65);
            renderIconWText(context, client, raceSprite, Text.translatable(MoaAPI.getRace(bloodstoneCapturedData.moaGeneComponent.race()).getTranslationKey()), -76, -25);
        }
    }

    private static void renderSurtrum(DrawContext context, MinecraftClient client, BloodstoneCapturedData bloodstoneCapturedData) {
        renderRing(context, 0, 0);
        renderText(context, client, bloodstoneCapturedData.bloodstoneComponent.name(), 0, -80);
        if (bloodstoneCapturedData.moaGeneComponent != null) {
            renderText(context, client, Text.translatable("moa.attribute.ground_speed").append(": ").append(bloodstoneCapturedData.getRatingWithColor(MoaAttributes.GROUND_SPEED.getRatingTierTranslationKey(bloodstoneCapturedData.moaGeneComponent.attributes().groundSpeed()))), 63, -50);
            renderText(context, client, Text.translatable("moa.attribute.gliding_speed").append(": ").append(bloodstoneCapturedData.getRatingWithColor(MoaAttributes.GLIDING_SPEED.getRatingTierTranslationKey(bloodstoneCapturedData.moaGeneComponent.attributes().glidingSpeed()))), 80, 0);
            renderText(context, client, Text.translatable("moa.attribute.gliding_decay").append(": ").append(bloodstoneCapturedData.getRatingWithColor(MoaAttributes.GLIDING_DECAY.getRatingTierTranslationKey(bloodstoneCapturedData.moaGeneComponent.attributes().glidingDecay()))), 63, 50);
            renderText(context, client, Text.translatable("moa.attribute.jumping_strength").append(": ").append(bloodstoneCapturedData.getRatingWithColor(MoaAttributes.JUMPING_STRENGTH.getRatingTierTranslationKey(bloodstoneCapturedData.moaGeneComponent.attributes().jumpStrength()))), -63, -50);
            renderText(context, client, Text.translatable("moa.attribute.drop_multiplier").append(": ").append(bloodstoneCapturedData.getRatingWithColor(MoaAttributes.DROP_MULTIPLIER.getRatingTierTranslationKey(bloodstoneCapturedData.moaGeneComponent.attributes().dropMultiplier()))), -80, 0);
            renderText(context, client, Text.translatable("moa.attribute.max_health").append(": ").append(bloodstoneCapturedData.getRatingWithColor(MoaAttributes.MAX_HEALTH.getRatingTierTranslationKey(bloodstoneCapturedData.moaGeneComponent.attributes().maxHealth()))), -63, 50);
        }
    }

    private static void renderRing(DrawContext context, int offsetX, int offsetY) {
        context.drawTexture(ParadiseLost.locate("textures/hud/bloodstone/bloodstone_ring.png"), offsetX - 75, offsetY - 75, 0, 0, 150, 150, 150, 150);
    }

    private static void renderIconWText(DrawContext context, MinecraftClient client, Sprite sprite, Text text, int offsetX, int offsetY) {
        int totalWidth = ((sprite.getContents().getWidth()) + 2 + client.textRenderer.getWidth(text));
        int totalHeight = client.textRenderer.fontHeight / 2;

        int startX = offsetX;
        if (offsetX == 0) {
            startX = offsetX - (totalWidth / 2);
        } else if (offsetX < 0) {
            startX = (offsetX - totalWidth);
        }

        RenderSystem.setShaderTexture(0, sprite.getAtlasId());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        context.drawSprite(startX, offsetY - 9, 0, 18, 18, sprite);  //  0 z correct?
        context.drawTextWithShadow(client.textRenderer, text, startX + sprite.getContents().getWidth() + 2, offsetY - totalHeight, 14737632);
    }

    private static void renderText(DrawContext context, MinecraftClient client, Text text, int offsetX, int offsetY) {
        int totalWidth = client.textRenderer.getWidth(text);
        int totalHeight = client.textRenderer.fontHeight / 2;

        int startX = offsetX;
        if (offsetX == 0) {
            startX = offsetX - (totalWidth / 2);
        } else if (offsetX < 0) {
            startX = (offsetX - totalWidth);
        }

        context.drawTextWithShadow(client.textRenderer, text, startX, offsetY - totalHeight, 14737632);
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
