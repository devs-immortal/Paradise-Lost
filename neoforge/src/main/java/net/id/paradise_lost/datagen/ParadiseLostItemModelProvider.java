package net.id.paradise_lost.datagen;

import net.id.paradise_lost.item.ParadiseLostBoatItem;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParadiseLostItemModelProvider extends ItemModelProvider {

    private final Set<ResourceLocation> explicitlyModelled = new HashSet<>();

    public ParadiseLostItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerSpecialModels();
        explicitlyModelled.clear();
        explicitlyModelled.addAll(generatedModels.keySet());

        for (Item item : BuiltInRegistries.ITEM) {
            var key = BuiltInRegistries.ITEM.getKey(item);
            if (!ModConstants.MODID.equals(key.getNamespace())) {
                continue;
            }
            if (item instanceof BlockItem || item instanceof SignItem || item instanceof SpawnEggItem) {
                continue;
            }
            if (explicitlyModelled.contains(key)) {
                continue;
            }
            ResourceLocation texture = modLoc("item/" + key.getPath());
            if (!existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
                continue;
            }
            if (isHeld(item, key.getPath())) {
                handheldItem(item);
            } else {
                basicItem(item);
            }
        }
    }

    private static boolean isHeld(Item item, String path) {

        if (item instanceof TieredItem || item instanceof BoatItem || item instanceof ParadiseLostBoatItem) {
            return true;
        }
        for (String kind : HELD_SUFFIXES) {
            if (path.contains(kind)) {
                return true;
            }
        }
        return false;
    }

    private static final List<String> HELD_SUFFIXES = List.of(
            "sword", "axe", "pickaxe", "shovel", "hoe", "blade", "wand");

    private void registerSpecialModels() {
        withExistingParent("template_spawn_egg", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/spawn_egg"))
                .texture("layer1", modLoc("item/spawn_egg_overlay"));

        spawnEgg(ItemRegistry.ENVOY_SPAWN_EGG.get());
        spawnEgg(ItemRegistry.SENTINEL_SPAWN_EGG.get());
        spawnEgg(ItemRegistry.MOA_SPAWN_EGG.get());
        spawnEgg(ItemRegistry.POPOM_SPAWN_EGG.get());
        spawnEgg(ItemRegistry.QUINT_SPAWN_EGG.get());

        withExistingParent("glazed_gold_fishing_rod_cast", mcLoc("item/handheld_rod"))
                .texture("layer0", modLoc("item/glazed_gold_fishing_rod_cast"));
        withExistingParent("glazed_gold_fishing_rod", mcLoc("item/handheld_rod"))
                .texture("layer0", modLoc("item/glazed_gold_fishing_rod"))
                .override()
                .predicate(mcLoc("cast"), 1)
                .model(getExistingFile(modLoc("item/glazed_gold_fishing_rod_cast")))
                .end();

        withExistingParent("xp_circlet_charged", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/xp_circlet_charged"));
        withExistingParent("xp_circlet", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/xp_circlet"))
                .override()
                .predicate(modLoc("charged"), 1)
                .model(getExistingFile(modLoc("item/xp_circlet_charged")))
                .end();

        withExistingParent("floaty_leggings_broken", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/floaty_leggings_broken"));
        withExistingParent("floaty_leggings", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/floaty_leggings"))
                .override()
                .predicate(modLoc("broken"), 1)
                .model(getExistingFile(modLoc("item/floaty_leggings_broken")))
                .end();

        withExistingParent("soul_blade_damaged", mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/soul_blade_damaged"));
        withExistingParent("soul_blade", mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/soul_blade"))
                .override()
                .predicate(modLoc("bloomed"), 1)
                .model(getExistingFile(modLoc("item/soul_blade_damaged")))
                .end();

        generatedItem("swedroot");
        generatedItem("nitra");
        generatedItem("flaxseed");
        generatedItem("blackcurrant");
        generatedItem("amadrys_bushel");
        generatedItem("lore_book");
        withExistingParent("blackcurrant_bush", mcLoc("item/generated"))
                .texture("layer0", modLoc("block/blackcurrant_bush_stage0"));

        withExistingParent("cherine_torch", mcLoc("item/generated"))
                .texture("layer0", modLoc("block/cherine_torch"));

        withExistingParent("rootcap", mcLoc("item/generated"))
                .texture("layer0", modLoc("block/rootcap0"));
        withExistingParent("rootcap_block", modLoc("block/rootcap_block_inventory"));

        hangerTip("rose_wisteria_hanger_plant", "rose_wisteria_hanger_tip");
        hangerTip("frost_wisteria_hanger_plant", "frost_wisteria_hanger_tip");
        hangerTip("lavender_wisteria_hanger_plant", "lavender_wisteria_hanger_tip");

        decoratedPot();
        amadrysBundle();
    }

    private void decoratedPot() {
        getBuilder("calcite_decorated_pot")
                .parent(getExistingFile(mcLoc("item/decorated_pot")))
                .texture("particle", mcLoc("block/calcite"));
    }

    private void amadrysBundle() {
        getBuilder("amadrys_bundle")
                .parent(getExistingFile(mcLoc("block/cube_bottom_top")))
                .texture("top", modLoc("block/amadrys_bundle_top"))
                .texture("bottom", modLoc("block/amadrys_bundle_bottom"))
                .texture("side", modLoc("block/amadrys_bundle_side"));
    }

    private void spawnEgg(Item item) {
        withExistingParent(BuiltInRegistries.ITEM.getKey(item).getPath(), modLoc("item/template_spawn_egg"));
    }

    private void generatedItem(String path) {
        withExistingParent(path, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + path));
    }

    private void hangerTip(String modelPath, String texturePath) {
        withExistingParent(modelPath, mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + texturePath));
    }

}
