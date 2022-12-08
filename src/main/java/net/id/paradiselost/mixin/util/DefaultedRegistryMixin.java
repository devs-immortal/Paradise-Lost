package net.id.paradiselost.mixin.util;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mixin(DefaultedRegistry.class)
public class DefaultedRegistryMixin {

    private static final Map<String, String> renames = createMap(
            /* BLOCKS */
            "quicksoil", "",
            "quicksoil_glass", "",
            "quicksoil_glass_pane", "",
            "cold_aercloud", "",
            "blue_aercloud", "",
            "pink_aercloud", "",
            "golden_aercloud", "",
            "icestone", "",
            "holystone", "",
            "holystone_slab", "",
            "holystone_stairs", "",
            "holystone_wall", "",
            "cobbled_holystone", "",
            "cobbled_holystone_slab", "",
            "cobbled_holystone_stairs", "",
            "cobbled_holystone_wall", "",
            "mossy_holystone", "",
            "golden_mossy_holystone", "",
            "mossy_holystone_slab", "",
            "mossy_holystone_stairs", "",
            "mossy_holystone_wall", "",
            "holystone_brick", "",
            "holystone_brick_slab", "",
            "holystone_brick_stairs", "",
            "holystone_brick_wall", "",
            "ambrosium_campfire", "",
            "ambrosium_ore", "",
            "zanite_ore", "",
            "gravitite_ore", "",
            "ambrosium_block", "",
            "zanite_block", "",
            "block_of_gravitite", "",
            "gravitite_levitator", "",
            "zanite_chain", "",
            "ambrosium_lantern", "",
            "ambrosium_torch", "",
            "ambrosium_wall_torch", "",
            //TODO: Add skyroot and golden oak blocks
            /* ITEMS */
            "aechor_petal", "",
            "ambrosium_shard", "",
            "zanite_gemstone", "",
            "zanite_fragment", "",
            "gravitite_gemstone", "",
            "zanite_shovel", "",
            "zanite_pickaxe", "",
            "zanite_axe", "",
            "zanite_sword", "",
            "zanite_hoe", "",
            "gravitite_shovel", "",
            "gravitite_pickaxe", "",
            "gravitite_axe", "",
            "gravitite_sword", "",
            "gravitite_hoe", "",
            "ambrosium_bloodstone", "",
            "zanite_bloodstone", "",
            "gravitite_bloodstone", "",
            "zanite_helmet", "",
            "zanite_chestplate", "",
            "zanite_leggings", "",
            "zanite_boots", "",
            "gravitite_helmet", "",
            "gravitite_chestplate", "",
            "gravitite_leggings", "",
            "gravitite_boots", "",
            "blue_berry", "",
            "blue_gummy_swet", "",
            "golden_gummy_swet", "",
            "skyroot_bucket", "",
            "skyroot_water_bucket", "",
            "skyroot_milk_bucket", ""
    );

    @SafeVarargs
    private static <T> Map<T, T> createMap(T... values) {
        if ((values.length & 1) != 0) {
            throw new IllegalArgumentException("Odd number of values");
        }
        Map<T, T> map = new HashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }

    @ModifyVariable(at = @At("HEAD"), method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", ordinal = 0, argsOnly = true)
    Identifier fixMissingFromRegistry(@Nullable Identifier id) {
        if (id != null && id.getNamespace().equals(ParadiseLost.MOD_ID)) {
            String path = id.getPath();
//            if (renames.containsKey(path)) {
//                return ParadiseLost.locate(renames.get(id.getPath()));
//            }
        }
        return id;
    }
}
