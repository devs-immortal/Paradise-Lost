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

    private static final Map<String, Identifier> renames = createMap(
            /* BLOCKS */
            "quicksoil", ParadiseLost.locate(""),
            "quicksoil_glass", ParadiseLost.locate(""),
            "quicksoil_glass_pane", ParadiseLost.locate(""),
            "cold_aercloud", ParadiseLost.locate("cold_cloud"),
            "blue_aercloud", ParadiseLost.locate("blue_cloud"),
            "pink_aercloud", ParadiseLost.locate("pink_cloud"),
            "golden_aercloud", ParadiseLost.locate("golden_cloud"),
            "icestone", ParadiseLost.locate(""),
            "holystone", ParadiseLost.locate(""),
            "holystone_slab", ParadiseLost.locate(""),
            "holystone_stairs", ParadiseLost.locate(""),
            "holystone_wall", ParadiseLost.locate(""),
            "cobbled_holystone", ParadiseLost.locate(""),
            "cobbled_holystone_slab", ParadiseLost.locate(""),
            "cobbled_holystone_stairs", ParadiseLost.locate(""),
            "cobbled_holystone_wall", ParadiseLost.locate(""),
            "mossy_holystone", ParadiseLost.locate(""),
            "golden_mossy_holystone", ParadiseLost.locate(""),
            "mossy_holystone_slab", ParadiseLost.locate(""),
            "mossy_holystone_stairs", ParadiseLost.locate(""),
            "mossy_holystone_wall", ParadiseLost.locate(""),
            "holystone_brick", ParadiseLost.locate(""),
            "holystone_brick_slab", ParadiseLost.locate(""),
            "holystone_brick_stairs", ParadiseLost.locate(""),
            "holystone_brick_wall", ParadiseLost.locate(""),
            "ambrosium_campfire", ParadiseLost.locate(""),
            "ambrosium_ore", ParadiseLost.locate(""),
            "zanite_ore", ParadiseLost.locate("olvite_ore"),
            "gravitite_ore", ParadiseLost.locate(""),
            "ambrosium_block", ParadiseLost.locate(""),
            "zanite_block", ParadiseLost.locate("olvite_block"),
            "block_of_gravitite", ParadiseLost.locate(""),
            "gravitite_levitator", ParadiseLost.locate(""),
            "zanite_chain", ParadiseLost.locate("olvite_chain"),
            "ambrosium_lantern", ParadiseLost.locate(""),
            "ambrosium_torch", ParadiseLost.locate(""),
            "ambrosium_wall_torch", ParadiseLost.locate(""),
            //TODO: Add skyroot and golden oak blocks
            /* ITEMS */
            "aechor_petal", ParadiseLost.locate(""),
            "ambrosium_shard", ParadiseLost.locate(""),
            "aercloud_vial", ParadiseLost.locate("cloud_vial"),
            "zanite_gemstone", ParadiseLost.locate("olvite"),
            "zanite_fragment", ParadiseLost.locate("olvite_nugget"),
            "gravitite_gemstone", ParadiseLost.locate(""),
            "zanite_shovel", ParadiseLost.locate("olvite_shovel"),
            "zanite_pickaxe", ParadiseLost.locate("olvite_pickaxe"),
            "zanite_axe", ParadiseLost.locate("olvite_axe"),
            "zanite_sword", ParadiseLost.locate("olvite_sword"),
            "zanite_hoe", ParadiseLost.locate("olvite_hoe"),
            "gravitite_shovel", ParadiseLost.locate(""),
            "gravitite_pickaxe", ParadiseLost.locate(""),
            "gravitite_axe", ParadiseLost.locate(""),
            "gravitite_sword", ParadiseLost.locate(""),
            "gravitite_hoe", ParadiseLost.locate(""),
            "ambrosium_bloodstone", ParadiseLost.locate(""),
            "zanite_bloodstone", ParadiseLost.locate("olvite_bloodstone"),
            "gravitite_bloodstone", ParadiseLost.locate(""),
            "zanite_helmet", ParadiseLost.locate("olvite_helmet"),
            "zanite_chestplate", ParadiseLost.locate("olvite_chestplate"),
            "zanite_leggings", ParadiseLost.locate("olvite_leggings"),
            "zanite_boots", ParadiseLost.locate("olvite_boots"),
            "gravitite_helmet", ParadiseLost.locate(""),
            "gravitite_chestplate", ParadiseLost.locate(""),
            "gravitite_leggings", ParadiseLost.locate(""),
            "gravitite_boots", ParadiseLost.locate(""),
            "blue_berry", ParadiseLost.locate(""),
            "blue_gummy_swet", ParadiseLost.locate(""),
            "golden_gummy_swet", ParadiseLost.locate(""),
            "skyroot_bucket", ParadiseLost.locate(""),
            "skyroot_water_bucket", ParadiseLost.locate(""),
            "skyroot_milk_bucket", ParadiseLost.locate(""),
            "valkyrie_lance", new Identifier("minecraft", "netherite_sword")
    );

    @SafeVarargs
    private static <T> Map<String, Identifier> createMap(T... values) {
        if ((values.length & 1) != 0) {
            throw new IllegalArgumentException("Odd number of values");
        }
        Map<String, Identifier> map = new HashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put((String) values[i], (Identifier) values[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }

    @ModifyVariable(at = @At("HEAD"), method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", ordinal = 0, argsOnly = true)
    Identifier fixMissingFromRegistry(@Nullable Identifier id) {
        if (id != null && id.getNamespace().equals(ParadiseLost.MOD_ID)) {
            String path = id.getPath();
            if (renames.containsKey(path)) {
                Identifier newId = renames.get(id.getPath());
                if (!newId.getPath().equals("")) return newId;
            }
        }
        return id;
    }
}
