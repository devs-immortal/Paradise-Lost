package net.id.aether.client.rendering.ui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class AetherOverlayRegistrar {

    private static final List<Overlay> OVERLAYS = new ArrayList<>();

    public static void register(Overlay overlay) {
        OVERLAYS.add(overlay);
    }

    public static List<Overlay> getOverlays() {
        return OVERLAYS;
    }

    public record Overlay(Identifier path, Predicate<LivingEntity> renderPredicate, Function<LivingEntity, Float> opacityProvider) {}
}
