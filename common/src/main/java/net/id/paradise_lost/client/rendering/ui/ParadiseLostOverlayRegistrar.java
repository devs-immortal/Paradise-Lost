package net.id.paradise_lost.client.rendering.ui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParadiseLostOverlayRegistrar {

    private static final List<Overlay> OVERLAYS = new ArrayList<>();

    public static void register(Overlay overlay) {
        OVERLAYS.add(overlay);
    }

    public static List<Overlay> getOverlays() {
        return OVERLAYS;
    }

    public record Overlay(ResourceLocation path, Predicate<LivingEntity> renderPredicate, Function<LivingEntity, Float> opacityProvider) {
    }
}
