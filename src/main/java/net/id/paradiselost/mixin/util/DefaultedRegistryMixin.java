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
            "quicksoil", ParadiseLost.locate("dirt"),
            "quicksoil_glass", new Identifier("minecraft", "yellow_stained_glass"),
            "quicksoil_glass_pane", new Identifier("minecraft", "yellow_stained_glass_pane"),
            "cold_aercloud", ParadiseLost.locate("cold_cloud"),
            "blue_aercloud", ParadiseLost.locate("blue_cloud"),
            "pink_aercloud", ParadiseLost.locate("pink_cloud"),
            "golden_aercloud", ParadiseLost.locate("golden_cloud"),
            "icestone", ParadiseLost.locate("vitroulite"),
            "holystone", ParadiseLost.locate("floestone"),
            "holystone_slab", ParadiseLost.locate("floestone_slab"),
            "holystone_stairs", ParadiseLost.locate("floestone_stairs"),
            "holystone_wall", ParadiseLost.locate("floestone_wall"),
            "cobbled_holystone", ParadiseLost.locate("cobbled_floestone"),
            "cobbled_holystone_slab", ParadiseLost.locate("cobbled_floestone_slab"),
            "cobbled_holystone_stairs", ParadiseLost.locate("cobbled_floestone_stairs"),
            "cobbled_holystone_wall", ParadiseLost.locate("cobbled_floestone_wall"),
            "mossy_holystone", ParadiseLost.locate("mossy_floestone"),
            "golden_mossy_holystone", ParadiseLost.locate("golden_mossy_floestone"),
            "mossy_holystone_slab", ParadiseLost.locate("mossy_floestone_slab"),
            "mossy_holystone_stairs", ParadiseLost.locate("mossy_floestone_stairs"),
            "mossy_holystone_wall", ParadiseLost.locate("mossy_floestone_wall"),
            "holystone_brick", ParadiseLost.locate("floestone_brick"),
            "holystone_brick_slab", ParadiseLost.locate("floestone_brick_slab"),
            "holystone_brick_stairs", ParadiseLost.locate("floestone_brick_stairs"),
            "holystone_brick_wall", ParadiseLost.locate("floestone_brick_wall"),
            "ambrosium_campfire", ParadiseLost.locate("cherine_campfire"),
            "ambrosium_ore", ParadiseLost.locate("cherine_ore"),
            "zanite_ore", ParadiseLost.locate("olvite_ore"),
            "gravitite_ore", ParadiseLost.locate("floestone"),
            "ambrosium_block", ParadiseLost.locate("cherine_block"),
            "zanite_block", ParadiseLost.locate("olvite_block"),
            "block_of_gravitite", new Identifier("minecraft", "diamond_block"),
            "gravitite_levitator", ParadiseLost.locate("levitator"),
            "zanite_chain", ParadiseLost.locate("olvite_chain"),
            "ambrosium_lantern", ParadiseLost.locate("cherine_lantern"),
            "ambrosium_torch", ParadiseLost.locate("cherine_torch"),
            "ambrosium_wall_torch", ParadiseLost.locate("cherine_wall_torch"),
            "flutegrass", ParadiseLost.locate("grass_plant"),
            "skyroot_sign", ParadiseLost.locate("aurel_sign"),
            "mottled_skyroot_fallen_log", ParadiseLost.locate("mottled_aurel_fallen_log"),
            "mottled_skyroot_log", ParadiseLost.locate("mottled_aurel_log"),
            "potted_skyroot_sapling", ParadiseLost.locate("potted_aurel_sapling"),
            "skyroot_bookshelf", ParadiseLost.locate("aurel_bookshelf"),
            "skyroot_button", ParadiseLost.locate("aurel_button"),
            "skyroot_door", ParadiseLost.locate("aurel_door"),
            "skyroot_fence", ParadiseLost.locate("aurel_fence"),
            "skyroot_fence_gate", ParadiseLost.locate("aurel_fence_gate"),
            "skyroot_leaf_pile", ParadiseLost.locate("aurel_leaf_pile"),
            "skyroot_leaves", ParadiseLost.locate("aurel_leaves"),
            "skyroot_log", ParadiseLost.locate("aurel_log"),
            "skyroot_planks", ParadiseLost.locate("aurel_planks"),
            "skyroot_pressure_plate", ParadiseLost.locate("aurel_pressure_plate"),
            "skyroot_sapling", ParadiseLost.locate("aurel_sapling"),
            "skyroot_slab", ParadiseLost.locate("aurel_slab"),
            "skyroot_stairs", ParadiseLost.locate("aurel_stairs"),
            "skyroot_trapdoor", ParadiseLost.locate("aurel_trapdoor"),
            "skyroot_wall_sign", ParadiseLost.locate("aurel_wall_sign"),
            "skyroot_wood", ParadiseLost.locate("aurel_wood"),
            "stripped_skyroot_log", ParadiseLost.locate("stripped_aurel_log"),
            "stripped_skyroot_wood", ParadiseLost.locate("stripped_aurel_wood"),
            "golden_oak_sign", ParadiseLost.locate("mother_aurel_sign"),
            "potted_golden_oak_sapling", ParadiseLost.locate("potted_mother_aurel_sapling"),
            "golden_oak_button", ParadiseLost.locate("mother_aurel_button"),
            "golden_oak_door", ParadiseLost.locate("mother_aurel_door"),
            "golden_oak_fence", ParadiseLost.locate("mother_aurel_fence"),
            "golden_oak_fence_gate", ParadiseLost.locate("mother_aurel_fence_gate"),
            "golden_oak_leaves", ParadiseLost.locate("mother_aurel_leaves"),
            "golden_oak_log", ParadiseLost.locate("mother_aurel_log"),
            "golden_oak_planks", ParadiseLost.locate("mother_aurel_planks"),
            "golden_oak_pressure_plate", ParadiseLost.locate("mother_aurel_pressure_plate"),
            "golden_oak_sapling", ParadiseLost.locate("mother_aurel_sapling"),
            "golden_oak_slab", ParadiseLost.locate("mother_aurel_slab"),
            "golden_oak_stairs", ParadiseLost.locate("mother_aurel_stairs"),
            "golden_oak_trapdoor", ParadiseLost.locate("mother_aurel_trapdoor"),
            "golden_oak_wall_sign", ParadiseLost.locate("mother_aurel_wall_sign"),
            "golden_oak_wood", ParadiseLost.locate("mother_aurel_wood"),
            "stripped_golden_oak_log", ParadiseLost.locate("stripped_mother_aurel_log"),
            "stripped_golden_oak_wood", ParadiseLost.locate("stripped_mother_aurel_wood"),
            "blueberry_bush", ParadiseLost.locate("blackcurrant_bush"),
            /* ITEMS */
            "aechor_petal", ParadiseLost.locate("hellenrose_petal"),
            "ambrosium_shard", ParadiseLost.locate("cherine"),
            "quicksoil_vial", ParadiseLost.locate("vial"),
            "aercloud_vial", ParadiseLost.locate("cloud_vial"),
            "zanite_gemstone", ParadiseLost.locate("olvite"),
            "zanite_fragment", ParadiseLost.locate("olvite_nugget"),
            "gravitite_gemstone", new Identifier("minecraft", "diamond"),
            "zanite_shovel", ParadiseLost.locate("olvite_shovel"),
            "zanite_pickaxe", ParadiseLost.locate("olvite_pickaxe"),
            "zanite_axe", ParadiseLost.locate("olvite_axe"),
            "zanite_sword", ParadiseLost.locate("olvite_sword"),
            "zanite_hoe", ParadiseLost.locate("olvite_hoe"),
            "gravitite_shovel", new Identifier("minecraft", "diamond_shovel"),
            "gravitite_pickaxe", new Identifier("minecraft", "diamond_pickaxe"),
            "gravitite_axe", new Identifier("minecraft", "diamond_axe"),
            "gravitite_sword", new Identifier("minecraft", "diamond_sword"),
            "gravitite_hoe", new Identifier("minecraft", "diamond_hoe"),
            "ambrosium_bloodstone", ParadiseLost.locate("cherine_bloodstone"),
            "zanite_bloodstone", ParadiseLost.locate("olvite_bloodstone"),
            "zanite_helmet", ParadiseLost.locate("olvite_helmet"),
            "zanite_chestplate", ParadiseLost.locate("olvite_chestplate"),
            "zanite_leggings", ParadiseLost.locate("olvite_leggings"),
            "zanite_boots", ParadiseLost.locate("olvite_boots"),
            "gravitite_helmet", new Identifier("minecraft", "diamond_helmet"),
            "gravitite_chestplate", new Identifier("minecraft", "diamond_chestplate"),
            "gravitite_leggings", new Identifier("minecraft", "diamond_leggings"),
            "gravitite_boots", new Identifier("minecraft", "diamond_boots"),
            "blue_berry", ParadiseLost.locate("blackcurrant"),
            "blue_gummy_swet", ParadiseLost.locate("amadrys_bread_glazed"),
            "golden_gummy_swet", ParadiseLost.locate("amadrys_bread_glazed"),
            "valkyrie_milk", ParadiseLost.locate("mystery_milk"),
            "ginger_bread_man", ParadiseLost.locate("gingerbread_man"),
            "skyroot_bucket", ParadiseLost.locate("aurel_bucket"),
            "skyroot_water_bucket", ParadiseLost.locate("aurel_water_bucket"),
            "skyroot_milk_bucket", ParadiseLost.locate("aurel_milk_bucket"),
            "skyroot_boat", ParadiseLost.locate("aurel_boat"),
            "skyroot_chest_boat", ParadiseLost.locate("aurel_chest_boat"),
            "golden_dart", new Identifier("minecraft", "arrow"),
            "enchanted_dart", new Identifier("minecraft", "arrow"),
            "poison_dart", new Identifier("minecraft", "arrow"),
            "golden_dart_shooter", new Identifier("minecraft", "bow"),
            "enchanted_dart_shooter", new Identifier("minecraft", "bow"),
            "poison_dart_shooter", new Identifier("minecraft", "bow"),
            "valkyrie_lance", new Identifier("minecraft", "netherite_sword")
    );

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
            if (renames.containsKey(path)) {
                Identifier newId = renames.get(id.getPath());
                if (!newId.getPath().equals("")) return newId;
            }
        }
        return id;
    }
}
