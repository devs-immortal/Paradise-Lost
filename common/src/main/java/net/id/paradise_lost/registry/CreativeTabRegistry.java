package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static net.id.paradise_lost.registry.ItemRegistry.*;

public class CreativeTabRegistry {
    public static void init() {
        Services.REGISTRATION.registerCreativeTabEntries();
    }

    protected static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS =
            RegistrationProvider.get(BuiltInRegistries.CREATIVE_MODE_TAB, ModConstants.MODID);

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> PARADISE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("building_blocks",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(BlockRegistry.CHISELED_FLOESTONE.get()))
                    .title(Component.translatable(creativeTabKey("building_blocks")))
                    .displayItems((params, output) -> {

                        output.accept(BlockRegistry.AUREL_WOODSTUFF.log().get());
                        output.accept(BlockRegistry.MOTTLED_AUREL_LOG.get());
                        output.accept(BlockRegistry.MOTTLED_AUREL_WOOD.get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.wood().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.strippedLog().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.strippedWood().get());
                        output.accept(BlockRegistry.MOTTLED_AUREL_FALLEN_LOG.get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.plank().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.plankStairs().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.plankSlab().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.fence().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.fenceGate().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.door().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.trapdoor().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.pressurePlate().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.button().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.wood().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.strippedLog().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.strippedWood().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.plank().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.plankStairs().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.plankSlab().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.fence().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.fenceGate().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.door().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.trapdoor().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.pressurePlate().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.button().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.log().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.wood().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.strippedLog().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.strippedWood().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.plank().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.plankStairs().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.plankSlab().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.fence().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.fenceGate().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.door().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.trapdoor().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.pressurePlate().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.button().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.log().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.wood().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.strippedLog().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.strippedWood().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.plank().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.plankStairs().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.plankSlab().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.fence().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.fenceGate().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.door().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.trapdoor().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.pressurePlate().get());
                        output.accept(BlockRegistry.WISTERIA_WOODSTUFF.button().get());
                        output.accept(BlockRegistry.THATCH_SET.block().get());
                        output.accept(BlockRegistry.THATCH_SET.stairs().get());
                        output.accept(BlockRegistry.THATCH_SET.slab().get());

                        output.accept(BlockRegistry.FLOESTONE.get());
                        output.accept(BlockRegistry.FLOESTONE_STAIRS.get());
                        output.accept(BlockRegistry.FLOESTONE_SLAB.get());
                        output.accept(BlockRegistry.FLOESTONE_WALL.get());
                        output.accept(BlockRegistry.FLOESTONE_PRESSURE_PLATE.get());
                        output.accept(BlockRegistry.FLOESTONE_BUTTON.get());
                        output.accept(BlockRegistry.COBBLED_FLOESTONE.get());
                        output.accept(BlockRegistry.COBBLED_FLOESTONE_STAIRS.get());
                        output.accept(BlockRegistry.COBBLED_FLOESTONE_SLAB.get());
                        output.accept(BlockRegistry.COBBLED_FLOESTONE_WALL.get());
                        output.accept(BlockRegistry.MOSSY_FLOESTONE.get());
                        output.accept(BlockRegistry.MOSSY_FLOESTONE_STAIRS.get());
                        output.accept(BlockRegistry.MOSSY_FLOESTONE_SLAB.get());
                        output.accept(BlockRegistry.MOSSY_FLOESTONE_WALL.get());
                        output.accept(BlockRegistry.GOLDEN_MOSSY_FLOESTONE.get());
                        output.accept(BlockRegistry.FLOESTONE_BRICK.get());
                        output.accept(BlockRegistry.FLOESTONE_BRICK_STAIRS.get());
                        output.accept(BlockRegistry.FLOESTONE_BRICK_SLAB.get());
                        output.accept(BlockRegistry.FLOESTONE_BRICK_WALL.get());
                        output.accept(BlockRegistry.SMOOTH_FLOESTONE.get());
                        output.accept(BlockRegistry.SMOOTH_FLOESTONE_STAIRS.get());
                        output.accept(BlockRegistry.SMOOTH_FLOESTONE_SLAB.get());
                        output.accept(BlockRegistry.CHISELED_FLOESTONE.get());
                        output.accept(BlockRegistry.HELIOLITH.get());
                        output.accept(BlockRegistry.HELIOLITH_STAIRS.get());
                        output.accept(BlockRegistry.HELIOLITH_SLAB.get());
                        output.accept(BlockRegistry.HELIOLITH_WALL.get());
                        output.accept(BlockRegistry.SMOOTH_HELIOLITH.get());
                        output.accept(BlockRegistry.SMOOTH_HELIOLITH_STAIRS.get());
                        output.accept(BlockRegistry.SMOOTH_HELIOLITH_SLAB.get());
                        output.accept(BlockRegistry.LEVITA_BRICK_SET.block().get());
                        output.accept(BlockRegistry.LEVITA_BRICK_SET.stairs().get());
                        output.accept(BlockRegistry.LEVITA_BRICK_SET.slab().get());
                        output.accept(BlockRegistry.CHISELED_LEVITA_BRICK.get());
                        output.accept(BlockRegistry.BURNISHED_STONE_SET.block().get());
                        output.accept(BlockRegistry.BURNISHED_STONE_SET.stairs().get());
                        output.accept(BlockRegistry.BURNISHED_STONE_SET.slab().get());
                        output.accept(BlockRegistry.BURNISHED_STONE_WALL.get());
                        output.accept(BlockRegistry.BURNISHED_STONE_PLAQUE.get());
                        output.accept(BlockRegistry.BURNISHED_STONE_SCRIPT.get());
                        output.accept(BlockRegistry.GOLDEN_AMBER_TILE.get());
                        output.accept(BlockRegistry.GOLDEN_AMBER_TILE_STAIRS.get());
                        output.accept(BlockRegistry.GOLDEN_AMBER_TILE_SLAB.get());
                        output.accept(BlockRegistry.CALCITE_TILES_SET.block().get());
                        output.accept(BlockRegistry.CALCITE_TILES_SET.stairs().get());
                        output.accept(BlockRegistry.CALCITE_TILES_SET.slab().get());
                        output.accept(BlockRegistry.CALCITE_TILES_WALL.get());
                        output.accept(BlockRegistry.BLOOMED_CALCITE_TILES_SET.block().get());
                        output.accept(BlockRegistry.BLOOMED_CALCITE_TILES_SET.stairs().get());
                        output.accept(BlockRegistry.BLOOMED_CALCITE_TILES_SET.slab().get());
                        output.accept(BlockRegistry.BLOOMED_CALCITE_TILES_WALL.get());

                        output.accept(BlockRegistry.BLOOMED_CALCITE.get());

                        output.accept(BlockRegistry.CHERINE_ORE.get());
                        output.accept(BlockRegistry.OLVITE_ORE.get());
                        output.accept(BlockRegistry.FLOESTONE_REDSTONE_ORE.get());
                        output.accept(BlockRegistry.SURTRUM.get());
                        output.accept(BlockRegistry.METAMORPHIC_SHELL.get());
                        output.accept(BlockRegistry.LEVITA_ORE.get());
                        output.accept(BlockRegistry.CHERINE_BLOCK.get());
                        output.accept(BlockRegistry.OLVITE_BLOCK.get());
                        output.accept(BlockRegistry.OLVITE_CHAIN.get());
                        output.accept(BlockRegistry.REFINED_SURTRUM_BLOCK.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> PARADISE_PLANTS_TAB = CREATIVE_MODE_TABS.register("plants",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .icon(() -> new ItemStack(BlockRegistry.HIGHLANDS_GRASS.get()))
                    .title(Component.translatable(creativeTabKey("plants")))
                    .displayItems((params, output) -> {

                        output.accept(BlockRegistry.HIGHLANDS_GRASS.get());
                        output.accept(BlockRegistry.DIRT_PATH.get());
                        output.accept(BlockRegistry.DIRT.get());
                        output.accept(BlockRegistry.FARMLAND.get());
                        output.accept(BlockRegistry.COARSE_DIRT.get());
                        output.accept(BlockRegistry.FROZEN_GRASS.get());
                        output.accept(BlockRegistry.PERMAFROST.get());
                        output.accept(BlockRegistry.PERMAFROST_PATH.get());
                        output.accept(BlockRegistry.LIVERWORT.get());
                        output.accept(BlockRegistry.LIVERWORT_CARPET.get());
                        output.accept(BlockRegistry.LEVITA.get());
                        output.accept(BlockRegistry.THATCH_SET.block().get());
                        output.accept(BlockRegistry.THATCH_SET.stairs().get());
                        output.accept(BlockRegistry.THATCH_SET.slab().get());

                        output.accept(BlockRegistry.PACKED_SWEDROOT.get());
                        output.accept(BlockRegistry.AMADRYS_BUNDLE.get());
                        output.accept(BlockRegistry.NITRA_BUNCH.get());

                        output.accept(BlockRegistry.AUREL_WOODSTUFF.sapling().get());
                        output.accept(BlockRegistry.AUREL_WOODSTUFF.leaves().get());
                        output.accept(BlockRegistry.AUREL_LEAF_PILE.get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.sapling().get());
                        output.accept(BlockRegistry.MOTHER_AUREL_WOODSTUFF.leaves().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.sapling().get());
                        output.accept(BlockRegistry.MENTH_WOODSTUFF.leaves().get());
                        output.accept(BlockRegistry.ROSE_WISTERIA_SAPLING.get());
                        output.accept(BlockRegistry.ROSE_WISTERIA_LEAVES.get());
                        output.accept(BlockRegistry.ROSE_WISTERIA_HANGER.get());
                        output.accept(BlockRegistry.ROSE_WISTERIA_LEAF_PILE.get());
                        output.accept(BlockRegistry.FROST_WISTERIA_SAPLING.get());
                        output.accept(BlockRegistry.FROST_WISTERIA_LEAVES.get());
                        output.accept(BlockRegistry.FROST_WISTERIA_HANGER.get());
                        output.accept(BlockRegistry.FROST_WISTERIA_LEAF_PILE.get());
                        output.accept(BlockRegistry.LAVENDER_WISTERIA_SAPLING.get());
                        output.accept(BlockRegistry.LAVENDER_WISTERIA_LEAVES.get());
                        output.accept(BlockRegistry.LAVENDER_WISTERIA_HANGER.get());
                        output.accept(BlockRegistry.LAVENDER_WISTERIA_LEAF_PILE.get());

                        output.accept(BlockRegistry.GRASS.get());
                        output.accept(BlockRegistry.TALL_GRASS.get());
                        output.accept(BlockRegistry.SHORT_GRASS.get());
                        output.accept(BlockRegistry.GRASS_FLOWERING.get());
                        output.accept(BlockRegistry.FERN.get());
                        output.accept(BlockRegistry.BUSH.get());
                        output.accept(BlockRegistry.SHAMROCK.get());
                        output.accept(BlockRegistry.MALT_SPRIG.get());

                        output.accept(BlockRegistry.ATARAXIA.get());
                        output.accept(BlockRegistry.CLOUDSBLUFF.get());
                        output.accept(BlockRegistry.DRIGEAN.get());
                        output.accept(BlockRegistry.LUMINAR.get());
                        output.accept(BlockRegistry.ANCIENT_FLOWER.get());
                        output.accept(BlockRegistry.WILD_FLAX.get());
                        output.accept(BlockRegistry.HONEY_NETTLE.get());

                        output.accept(BlockRegistry.ROOTCAP.get());
                        output.accept(BlockRegistry.BROWN_SPORECAP.get());
                        output.accept(BlockRegistry.PINK_SPORECAP.get());
                        output.accept(BlockRegistry.ROOTCAP_BLOCK.get());
                        output.accept(BlockRegistry.BROWN_SPORECAP_BLOCK.get());
                        output.accept(BlockRegistry.PINK_SPORECAP_BLOCK.get());

                        output.accept(BLACKCURRANT.get());
                        output.accept(AMADRYS_BUSHEL.get());
                        output.accept(FLAXSEED.get());
                        output.accept(NITRA_SEED.get());
                        output.accept(NITRA_BULB.get());
                        output.accept(SWEDROOT.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> PARADISE_DECO_TAB = CREATIVE_MODE_TABS.register("decoration",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
                    .icon(() -> new ItemStack(BlockRegistry.CHERINE_LANTERN.get()))
                    .title(Component.translatable(creativeTabKey("decoration")))
                    .displayItems((params, output) -> {

                        output.accept(BlockRegistry.COLD_CLOUD.get());
                        output.accept(BlockRegistry.BLUE_CLOUD.get());
                        output.accept(BlockRegistry.GOLDEN_CLOUD.get());
                        output.accept(BlockRegistry.GREEN_CLOUD.get());

                        output.accept(CHERINE_TORCH.get());
                        output.accept(BlockRegistry.CHERINE_LANTERN.get());
                        output.accept(BlockRegistry.OLVITE_CHAIN.get());
                        output.accept(BlockRegistry.CALCITE_FLOWER_POT.get());
                        output.accept(BlockRegistry.CALCITE_DECORATED_POT.get());
                        output.accept(BlockRegistry.CHERINE_CAMPFIRE.get());
                        output.accept(BlockRegistry.AUREL_BOOKSHELF.get());
                        output.accept(BlockRegistry.FLAXWEAVE_CUSHION.get());
                        output.accept(BlockRegistry.FLAXWEAVE_CUSHION_SLAB.get());
                        output.accept(BlockRegistry.GOLDEN_AMBER_BARS.get());
                        output.accept(BlockRegistry.AMADRYS_BUNDLE.get());
                        output.accept(BlockRegistry.SUSPICIOUS_DIRT.get());
                        output.accept(BlockRegistry.NITRA_BUNCH.get());
                        output.accept(BlockRegistry.LEVITATOR.get());
                        output.accept(BlockRegistry.LEVITA_RAIL.get());
                        output.accept(BlockRegistry.OLVITE_PRESSURE_PLATE.get());
                        output.accept(BlockRegistry.INCUBATOR.get());
                        output.accept(BlockRegistry.NEST.get());
                        output.accept(BlockRegistry.FOOD_BOWL.get());
                        output.accept(BlockRegistry.TREE_TAP.get());
                        output.accept(AUREL_SIGN.get());
                        output.accept(AUREL_HANGING_SIGN.get());
                        output.accept(MOTHER_AUREL_SIGN.get());
                        output.accept(MOTHER_AUREL_HANGING_SIGN.get());
                        output.accept(MENTH_SIGN.get());
                        output.accept(MENTH_HANGING_SIGN.get());
                        output.accept(WISTERIA_SIGN.get());
                        output.accept(WISTERIA_HANGING_SIGN.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> PARADISE_EQUIPMENT_TAB = CREATIVE_MODE_TABS.register("equipment",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
                    .icon(() -> new ItemStack(SURTRUM_PICKAXE.get()))
                    .title(Component.translatable(creativeTabKey("equipment")))
                    .displayItems((params, output) -> {

                        output.accept(OLVITE_SWORD.get());
                        output.accept(OLVITE_SHOVEL.get());
                        output.accept(OLVITE_PICKAXE.get());
                        output.accept(OLVITE_AXE.get());
                        output.accept(OLVITE_HOE.get());
                        output.accept(SURTRUM_SWORD.get());
                        output.accept(SURTRUM_SHOVEL.get());
                        output.accept(SURTRUM_PICKAXE.get());
                        output.accept(SURTRUM_AXE.get());
                        output.accept(SURTRUM_HOE.get());
                        output.accept(GLAZED_GOLD_SWORD.get());
                        output.accept(GLAZED_GOLD_SHOVEL.get());
                        output.accept(GLAZED_GOLD_PICKAXE.get());
                        output.accept(GLAZED_GOLD_AXE.get());
                        output.accept(GLAZED_GOLD_HOE.get());
                        output.accept(SOUL_BLADE.get());

                        output.accept(OLVITE_SPYGLASS.get());
                        output.accept(TOTEM_OF_LEVITATION.get());
                        output.accept(LEVITA_ARROW.get());

                        output.accept(LEVITA_WAND.get());
                        output.accept(CHERINE_BLOODSTONE.get());
                        output.accept(OLVITE_BLOODSTONE.get());
                        output.accept(SURTRUM_BLOODSTONE.get());

                        output.accept(WARDED_JAR.get());
                        output.accept(WARDED_JAR_ALLAY.get());
                        output.accept(WARDED_JAR_QUINT.get());

                        output.accept(AUREL_BUCKET.get());
                        output.accept(AUREL_WATER_BUCKET.get());
                        output.accept(AUREL_POWDER_SNOW_BUCKET.get());
                        output.accept(AUREL_MILK_BUCKET.get());
                        output.accept(NITRA_BULB.get());
                        output.accept(PALACE_KEY.get());

                        output.accept(OLVITE_HELMET.get());
                        output.accept(OLVITE_HELMET_ORNATE.get());
                        output.accept(OLVITE_CHESTPLATE.get());
                        output.accept(OLVITE_LEGGINGS.get());
                        output.accept(OLVITE_BOOTS.get());
                        output.accept(SURTRUM_HELMET.get());
                        output.accept(SURTRUM_CHESTPLATE.get());
                        output.accept(SURTRUM_LEGGINGS.get());
                        output.accept(SURTRUM_BOOTS.get());
                        output.accept(GLAZED_GOLD_HELMET.get());
                        output.accept(GLAZED_GOLD_CHESTPLATE.get());
                        output.accept(GLAZED_GOLD_LEGGINGS.get());
                        output.accept(GLAZED_GOLD_BOOTS.get());
                        output.accept(XP_CIRCLET.get());
                        output.accept(FLOATY_LEGGINGS.get());

                        output.accept(GLAZED_GOLD_UPGRADE.get());

                        output.accept(AUREL_BOATS.boat().get());
                        output.accept(AUREL_BOATS.chestBoat().get());
                        output.accept(MOTHER_AUREL_BOATS.boat().get());
                        output.accept(MOTHER_AUREL_BOATS.chestBoat().get());
                        output.accept(MENTH_BOATS.boat().get());
                        output.accept(MENTH_BOATS.chestBoat().get());
                        output.accept(WISTERIA_BOATS.boat().get());
                        output.accept(WISTERIA_BOATS.chestBoat().get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> PARADISE_RESOURCES_TAB = CREATIVE_MODE_TABS.register("resources",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 4)
                    .icon(() -> new ItemStack(CHERINE.get()))
                    .title(Component.translatable(creativeTabKey("resources")))
                    .displayItems((params, output) -> {
                        output.accept(CHERINE.get());
                        output.accept(OLVITE.get());
                        output.accept(OLVITE_NUGGET.get());
                        output.accept(RAW_SURTRUM.get());
                        output.accept(REFINED_SURTRUM.get());
                        output.accept(LEVITA_GEM.get());
                        output.accept(LEVITA_SHARD.get());
                        output.accept(GOLDEN_AMBER.get());
                        output.accept(FLAX_THREAD.get());
                        output.accept(FLAXWEAVE.get());
                        output.accept(SOL_POTTERY_SHERD.get());
                        output.accept(COO_POTTERY_SHERD.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> PARADISE_FOOD_TAB = CREATIVE_MODE_TABS.register("food",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 5)
                    .icon(() -> new ItemStack(AMADRYS_NOODLES.get()))
                    .title(Component.translatable(creativeTabKey("food")))
                    .displayItems((params, output) -> {

                        output.accept(BLACKCURRANT.get());
                        output.accept(AMADRYS_BUSHEL.get());
                        output.accept(AMADRYS_BREAD.get());
                        output.accept(AMADRYS_BREAD_GLAZED.get());
                        output.accept(AMADRYS_BREAD_GLAZED_FILLED.get());
                        output.accept(POPOM_JELLY.get());
                        output.accept(BLACKCURRANT_PIE.get());
                        output.accept(BLACKCURRANT_COOKIE.get());
                        output.accept(AMADRYS_NOODLES.get());
                        output.accept(ROOT_STEW.get());
                        output.accept(SWEDROOT.get());
                        output.accept(SWEDROOT_PULP.get());

                        output.accept(MOA_MEAT.get());
                        output.accept(COOKED_MOA_MEAT.get());

                        output.accept(BlockRegistry.CHEESECAKE.get());
                    })
                    .build());

    private static String creativeTabKey(String name) {
        return "itemGroup.paradise_lost." + name;
    }
}
