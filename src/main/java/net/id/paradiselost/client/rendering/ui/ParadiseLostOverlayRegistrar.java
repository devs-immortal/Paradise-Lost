package net.id.paradiselost.client.rendering.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class ParadiseLostOverlayRegistrar {

    private static final List<Overlay> OVERLAYS = new ArrayList<>();

    public static void register(Overlay overlay) {
        OVERLAYS.add(overlay);
    }

    public static List<Overlay> getOverlays() {
        return OVERLAYS;
    }

    public record Overlay(Identifier path, Predicate<LivingEntity> renderPredicate, Function<LivingEntity, Float> opacityProvider) {
    }
}
