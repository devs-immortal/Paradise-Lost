package net.id.paradiselost.mixin.util;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(SimpleDefaultedRegistry.class)
public class SimpleDefaultedRegistryMixin {

    @Unique
    private static final Map<String, Identifier> RENAMES = createMap(
            "floestone_brick", ParadiseLost.locate("floestone_bricks"),
            "floestone_brick_slab", ParadiseLost.locate("floestone_bricks_slab"),
            "floestone_brick_stairs", ParadiseLost.locate("floestone_bricks_stairs"),
            "floestone_brick_wall", ParadiseLost.locate("floestone_bricks_wall"),
            "levita_brick", ParadiseLost.locate("levita_bricks"),
            "chiseled_levita_brick", ParadiseLost.locate("chiseled_levita_bricks")
    );

    @Unique
    @SafeVarargs
    private static <T, V> Map<T, V> createMap(Object... values) {
        if ((values.length & 1) != 0) {
            throw new IllegalArgumentException("Odd number of values");
        }
        Map<T, V> map = new HashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put((T) values[i], (V) values[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }

    @ModifyVariable(at = @At("HEAD"), method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", ordinal = 0, argsOnly = true)
    Identifier fixMissingFromRegistry(@Nullable Identifier id) {
        if (id != null && id.getNamespace().equals(ParadiseLost.MOD_ID)) {
            String path = id.getPath();
            if (RENAMES.containsKey(path)) {
                Identifier newId = RENAMES.get(id.getPath());
                if (!newId.getPath().equals("")) return newId;
            }
        }
        return id;
    }
}