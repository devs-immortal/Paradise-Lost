package net.id.paradiselost.items;

import com.google.common.collect.ImmutableList;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.accessories.ParachuteTrinketItem;
import net.id.paradiselost.items.armor.ParadiseLostArmorMaterials;
import net.id.paradiselost.items.food.ParadiseLostFoodComponent;
import net.id.paradiselost.items.misc.*;
import net.id.paradiselost.items.tools.ParadiseLostToolMaterials;
import net.id.paradiselost.items.tools.AurelBucketItem;
import net.id.paradiselost.items.tools.base_tools.*;
import net.id.paradiselost.items.tools.bloodstone.AbstentineBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.CherineBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.SurtrumBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.OlviteBloodstoneItem;
import net.id.paradiselost.items.utils.ParadiseLostRarity;
import net.id.paradiselost.registry.ParadiseLostRegistryQueues;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.items.ParadiseLostItemActions.*;
import static net.minecraft.entity.EquipmentSlot.*;
import static net.minecraft.util.Rarity.*;

@SuppressWarnings("unused")
public class ParadiseLostItems {
    /*
    Begin items
     */

    private static FabricItemSettings nightmare() {
        return new FabricItemSettings().fireproof();
    }

    private static FabricItemSettings resource() {
        return new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_RESOURCES);
    }

    private static final Settings resource = resource();

    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource));
    public static final Item HELLENROSE_PETAL = add("hellenrose_petal", new Item(resource), compostable65);
    public static final Item NIGTHMARE_FUEL = add("nightmare_fuel", new LoreItem(nightmare().rarity(UNCOMMON), ImmutableList.of(Text.translatable("item.paradise_lost.nightmare_fuel.tooltip").formatted(Formatting.GRAY))));
    public static final Item CROW_EYE = add("crow_eye", new LoreItem(nightmare().maxCount(1).rarity(UNCOMMON), ImmutableList.of(Text.translatable("item.paradise_lost.crow_eye.tooltip").formatted(Formatting.GRAY))));
    public static final Item CHERINE = add("cherine", new Item(resource), fuel(500));
    public static final Item OLVITE = add("olvite", new Item(resource));
    public static final Item OLVITE_NUGGET = add("olvite_nugget", new Item(resource));
    public static final Item REFINED_SURTRUM = add("refined_surtrum", new Item(resource));
    public static final Item RAW_SURTRUM = add("raw_surtrum", new Item(resource));
    public static final Item FLAX_THREAD = add("flax_thread", new Item(resource));
    public static final Item FLAXWEAVE = add("flaxweave", new Item(resource));
    public static final Item SWEDROOT_PULP = add("swedroot_pulp", new Item(resource), compostable30);


    private static Settings tool() {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_TOOLS);
    }

    private static final Settings tool = tool();
    private static final Settings rareTool = tool().rarity(RARE);
    private static final Settings paradiseLostLootTool = tool().rarity(ParadiseLostRarity.PARADISE_LOST_LOOT);
    private static final Settings unstackableTool = tool().maxCount(1);
    private static final Settings unstackableRareTool = tool().maxCount(1).rarity(RARE);

    // Olvite
    public static final ShovelItem OLVITE_SHOVEL = add("olvite_shovel", new ShovelItem(ParadiseLostToolMaterials.OLVITE, 1.5f, -3f, tool()));
    public static final PickaxeItem OLVITE_PICKAXE = add("olvite_pickaxe", new PickaxeItem(ParadiseLostToolMaterials.OLVITE, 1, -2.8f, tool()));
    public static final AxeItem OLVITE_AXE = add("olvite_axe", new AxeItem(ParadiseLostToolMaterials.OLVITE, 6f, -3.1f, tool()));
    public static final SwordItem OLVITE_SWORD = add("olvite_sword", new SwordItem(ParadiseLostToolMaterials.OLVITE, 3, -2.4f, tool()));
    public static final HoeItem OLVITE_HOE = add("olvite_hoe", new ParadiseLostHoeItem(ParadiseLostToolMaterials.OLVITE, -2, -1f, tool()));

    // Surtrum
    public static final GravityShovelItem SURTRUM_SHOVEL = add("surtrum_shovel", new GravityShovelItem(ParadiseLostToolMaterials.SURTRUM, 2.5f, -3f, tool()));
    public static final GravityPickaxeItem SURTRUM_PICKAXE = add("surtrum_pickaxe", new GravityPickaxeItem(ParadiseLostToolMaterials.SURTRUM, 2, -2.8f, tool()));
    public static final GravityAxeItem SURTRUM_AXE = add("surtrum_axe", new GravityAxeItem(ParadiseLostToolMaterials.SURTRUM, 6f, -3.1f, tool()));
    public static final SwordItem SURTRUM_SWORD = add("surtrum_sword", new SwordItem(ParadiseLostToolMaterials.SURTRUM, 4, -2.4f, tool()));
    public static final GravityHoeItem SURTRUM_HOE = add("surtrum_hoe", new GravityHoeItem(ParadiseLostToolMaterials.SURTRUM, -3, 0f, tool()));

    // Glazed Gold
    public static final ShovelItem GLAZED_GOLD_SHOVEL = add("glazed_gold_shovel", new ShovelItem(ParadiseLostToolMaterials.GLAZED_GOLD, 1.5f, -3f, tool()));
    public static final PickaxeItem GLAZED_GOLD_PICKAXE = add("glazed_gold_pickaxe", new PickaxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, 1, -2.8f, tool()));
    public static final AxeItem GLAZED_GOLD_AXE = add("glazed_gold_axe", new AxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, 6f, -3.0f, tool()));
    public static final SwordItem GLAZED_GOLD_SWORD = add("glazed_gold_sword", new SwordItem(ParadiseLostToolMaterials.GLAZED_GOLD, 3, -2.4f, tool()));
    public static final HoeItem GLAZED_GOLD_HOE = add("glazed_gold_hoe", new ParadiseLostHoeItem(ParadiseLostToolMaterials.GLAZED_GOLD, -2, -2.0f, tool()));

    public static final TrinketItem CLOUD_PARACHUTE = add("cold_parachute", new ParachuteTrinketItem(unstackableTool, "cloud_parachute"));
    public static final TrinketItem GOLDEN_CLOUD_PARACHUTE = add("golden_parachute", new ParachuteTrinketItem(tool().maxCount(1).maxDamage(20), "golden_parachute"));

    public static final CherineBloodstoneItem CHERINE_BLOODSTONE = add("cherine_bloodstone", new CherineBloodstoneItem(unstackableTool));
    public static final OlviteBloodstoneItem OLVITE_BLOODSTONE = add("olvite_bloodstone", new OlviteBloodstoneItem(unstackableTool));
    public static final SurtrumBloodstoneItem SURTRUM_BLOODSTONE = add("surtrum_bloodstone", new SurtrumBloodstoneItem(unstackableTool));
    public static final AbstentineBloodstoneItem ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(unstackableTool));

    public static final Item GLAZED_GOLD_UPGRADE = add("glazed_gold_upgrade_smithing_template", new Item(tool()));

    private static Settings wearable() {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_WEARABLES);
    }

    private static final Settings WEARABLE = wearable();
    private static final Settings RARE_WEARABLE = wearable().rarity(RARE);
    private static final Settings PARADISE_LOST_LOOT_WEARABLE = wearable().rarity(ParadiseLostRarity.PARADISE_LOST_LOOT);

    // Olvite
    public static final ArmorItem OLVITE_HELMET = add("olvite_helmet", new ArmorItem(ParadiseLostArmorMaterials.OLVITE, HEAD, WEARABLE));
    public static final ArmorItem OLVITE_CHESTPLATE = add("olvite_chestplate", new ArmorItem(ParadiseLostArmorMaterials.OLVITE, CHEST, WEARABLE));
    public static final ArmorItem OLVITE_LEGGINGS = add("olvite_leggings", new ArmorItem(ParadiseLostArmorMaterials.OLVITE, LEGS, WEARABLE));
    public static final ArmorItem OLVITE_BOOTS = add("olvite_boots", new ArmorItem(ParadiseLostArmorMaterials.OLVITE, FEET, WEARABLE));

    // Glazed Gold
    public static final ArmorItem GLAZED_GOLD_HELMET = add("glazed_gold_helmet", new ArmorItem(ParadiseLostArmorMaterials.GLAZED_GOLD, HEAD, WEARABLE));
    public static final ArmorItem GLAZED_GOLD_CHESTPLATE = add("glazed_gold_chestplate", new ArmorItem(ParadiseLostArmorMaterials.GLAZED_GOLD, CHEST, WEARABLE));
    public static final ArmorItem GLAZED_GOLD_LEGGINGS = add("glazed_gold_leggings", new ArmorItem(ParadiseLostArmorMaterials.GLAZED_GOLD, LEGS, WEARABLE));
    public static final ArmorItem GLAZED_GOLD_BOOTS = add("glazed_gold_boots", new ArmorItem(ParadiseLostArmorMaterials.GLAZED_GOLD, FEET, WEARABLE));

    // Surtrum
    public static final ArmorItem SURTRUM_HELMET = add("surtrum_helmet", new ArmorItem(ParadiseLostArmorMaterials.SURTRUM, HEAD, wearable()));
    public static final ArmorItem SURTRUM_CHESTPLATE = add("surtrum_chestplate", new ArmorItem(ParadiseLostArmorMaterials.SURTRUM, CHEST, wearable()));
    public static final ArmorItem SURTRUM_LEGGINGS = add("surtrum_leggings", new ArmorItem(ParadiseLostArmorMaterials.SURTRUM, LEGS, wearable()));
    public static final ArmorItem SURTRUM_BOOTS = add("surtrum_boots", new ArmorItem(ParadiseLostArmorMaterials.SURTRUM, FEET, wearable()));


    private static Settings food() {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_FOOD);
    }

    private static Settings food(FoodComponent foodComponent) {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_FOOD).food(foodComponent);
    }

    private static Settings food(FoodComponent foodComponent, Rarity rarity) {
        return food(foodComponent).rarity(rarity);
    }

    public static final AliasedBlockItem BLACKCURRANT = add("blackcurrant", new AliasedBlockItem(ParadiseLostBlocks.BLACKCURRANT_BUSH, food(ParadiseLostFoodComponent.BLACKCURRANT)), compostable30);
    public static final Item ORANGE = add("orange", new Item(food(ParadiseLostFoodComponent.ORANGE)), compostable65);
    public static final AliasedBlockItem AMADRYS_BUSHEL = add("amadrys_bushel", new AliasedBlockItem(ParadiseLostBlocks.AMADRYS, food(ParadiseLostFoodComponent.GENERIC_WORSE)), compostable30);
    public static final Item AMADRYS_NOODLES = add("amadrys_noodles", new StewItem(food(ParadiseLostFoodComponent.AMADRYS_NOODLES)));
    public static final Item AMADRYS_BREAD = add("amadrys_bread", new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD)));
    public static final Item AMADRYS_BREAD_GLAZED = add("amadrys_bread_glazed", new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED)));
    public static final AliasedBlockItem SWEDROOT = add("swedroot", new AliasedBlockItem(ParadiseLostBlocks.SWEDROOT, food(ParadiseLostFoodComponent.SWEDROOT)), compostable30);
    public static final AliasedBlockItem FLAXSEED = add("flaxseed", new AliasedBlockItem(ParadiseLostBlocks.FLAX, food()), compostable30);
    public static final Item GINGERBREAD_MAN = add("gingerbread_man", new Item(food(ParadiseLostFoodComponent.SWEDROOT)), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", new Item(food(ParadiseLostFoodComponent.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", new Item(food(ParadiseLostFoodComponent.COOKED_MOA_MEAT)));

    private static Settings misc() {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_MISC);
    }

    private static final Settings misc = misc();

    public static final ParadiseLostPortalItem PARADISE_LOST_PORTAL = add("portal", new ParadiseLostPortalItem(misc));

    public static final BookOfLoreItem LORE_BOOK = add("lore_book", new BookOfLoreItem(misc().maxCount(1)));
    public static final MoaEggItem MOA_EGG = add("moa_egg", new MoaEggItem(misc().maxCount(1)));
    public static final BlockItem INCUBATOR = add("incubator", ParadiseLostBlocks.INCUBATOR, misc, fuel(300));
    public static final BlockItem FOOD_BOWL = add("food_bowl", ParadiseLostBlocks.FOOD_BOWL, misc, fuel(300));
    public static final BlockItem TREE_TAP = add("tree_tap", ParadiseLostBlocks.TREE_TAP, misc, fuel(300));

    public static final AurelBucketItem AUREL_BUCKET = add("aurel_bucket", new AurelBucketItem(misc().maxCount(16)), fuel(200), emptyBucketBehavior);

    private static final Settings aurelBucket = misc().maxCount(1).recipeRemainder(AUREL_BUCKET);
    public static final AurelBucketItem AUREL_WATER_BUCKET = add("aurel_water_bucket", new AurelBucketItem(Fluids.WATER, aurelBucket), emptiableBucketBehavior);
    public static final AurelBucketItem AUREL_MILK_BUCKET = add("aurel_milk_bucket", new AurelBucketItem(aurelBucket));

    public static final SpawnEggItem HELLENROSE_SPAWN_EGG = add("hellenrose_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.HELLENROSE, 0x97DED4, 0x31897D, misc), spawnEggBehavior);
    public static final SpawnEggItem PARADISE_HARE_SPAWN_EGG = add("corsican_hare_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.PARADISE_HARE, 0xC5D6ED, 0x82A6D9, misc), spawnEggBehavior);
    public static final SpawnEggItem MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.MOA, 0xC55C2E4, 0xB3A8BB, misc), spawnEggBehavior);

    private static FabricItemSettings building_block() {
        return new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_BUILDING_BLOCKS);
    }
    private static final FabricItemSettings building_block = building_block();

    // stone
    public static final BlockItem FLOESTONE = add("floestone", ParadiseLostBlocks.FLOESTONE, building_block);
    public static final BlockItem COBBLED_FLOESTONE = add("cobbled_floestone", ParadiseLostBlocks.COBBLED_FLOESTONE, building_block);
    public static final BlockItem VITROULITE = add("vitroulite", ParadiseLostBlocks.VITROULITE, building_block);
    // nature
    public static final BlockItem HIGHLANDS_GRASS = add("highlands_grass", ParadiseLostBlocks.HIGHLANDS_GRASS, building_block);

    public static final BlockItem FROZEN_GRASS = add("frozen_grass", ParadiseLostBlocks.FROZEN_GRASS, building_block);
    public static final BlockItem DIRT = add("dirt", ParadiseLostBlocks.DIRT, building_block);
    public static final BlockItem COARSE_DIRT = add("coarse_dirt", ParadiseLostBlocks.COARSE_DIRT, building_block);
    public static final BlockItem PERMAFROST = add("permafrost", ParadiseLostBlocks.PERMAFROST, building_block);
    public static final BlockItem PACKED_SWEDROOT = add("packed_swedroot", ParadiseLostBlocks.PACKED_SWEDROOT, building_block, compostable85);
    public static final BlockItem COLD_CLOUD = add("cold_cloud", ParadiseLostBlocks.COLD_CLOUD, building_block);
    public static final BlockItem BLUE_CLOUD = add("blue_cloud", ParadiseLostBlocks.BLUE_CLOUD, building_block);
    public static final BlockItem PINK_CLOUD = add("pink_cloud", ParadiseLostBlocks.PINK_CLOUD, building_block);
    public static final BlockItem GOLDEN_CLOUD = add("golden_cloud", ParadiseLostBlocks.GOLDEN_CLOUD, building_block);
    // planks
    public static final BlockItem AUREL_PLANKS = add("aurel_planks", ParadiseLostBlocks.AUREL_PLANKS, building_block);
    public static final BlockItem MOTHER_AUREL_PLANKS = add("mother_aurel_planks", ParadiseLostBlocks.MOTHER_AUREL_PLANKS, building_block);
    public static final BlockItem ORANGE_PLANKS = add("orange_planks", ParadiseLostBlocks.ORANGE_PLANKS, building_block);
    public static final BlockItem WISTERIA_PLANKS = add("wisteria_planks", ParadiseLostBlocks.WISTERIA_PLANKS, building_block);
    public static final BlockItem AUREL_BOOKSHELF = add("aurel_bookshelf", ParadiseLostBlocks.AUREL_BOOKSHELF, building_block);
    // ores
    public static final BlockItem CHERINE_ORE = add("cherine_ore", ParadiseLostBlocks.CHERINE_ORE, building_block);
    public static final BlockItem OLVITE_ORE = add("olvite_ore", ParadiseLostBlocks.OLVITE_ORE, building_block);
    public static final BlockItem SURTRUM = add("surtrum", ParadiseLostBlocks.SURTRUM, building_block);
    public static final BlockItem METAMORPHIC_SHELL = add("metamorphic_shell", ParadiseLostBlocks.METAMORPHIC_SHELL, building_block);
    // ore blocks
    public static final BlockItem CHERINE_BLOCK = add("cherine_block", ParadiseLostBlocks.CHERINE_BLOCK, building_block, fuel(5000));
    public static final BlockItem OLVITE_BLOCK = add("olvite_block", ParadiseLostBlocks.OLVITE_BLOCK, building_block);
    public static final BlockItem REFINED_SURTRUM_BLOCK = add("refined_surtrum_block", ParadiseLostBlocks.REFINED_SURTRUM_BLOCK, building_block);
    // move this somewhere else
    public static final BlockItem LEVITATOR = add("levitator", ParadiseLostBlocks.LEVITATOR, building_block);
    // logs
    public static final BlockItem AUREL_LOG = add("aurel_log", ParadiseLostBlocks.AUREL_LOG, building_block);
    public static final BlockItem MOTTLED_AUREL_LOG = add("mottled_aurel_log", ParadiseLostBlocks.MOTTLED_AUREL_LOG, building_block);
    public static final BlockItem MOTTLED_AUREL_FALLEN_LOG = add("mottled_aurel_fallen_log", ParadiseLostBlocks.MOTTLED_AUREL_FALLEN_LOG, building_block);
    public static final BlockItem MOTHER_AUREL_LOG = add("mother_aurel_log", ParadiseLostBlocks.MOTHER_AUREL_LOG, building_block);
    public static final BlockItem ORANGE_LOG = add("orange_log", ParadiseLostBlocks.ORANGE_LOG, building_block);
    public static final BlockItem WISTERIA_LOG = add("wisteria_log", ParadiseLostBlocks.WISTERIA_LOG, building_block);
    // stripped logs
    public static final BlockItem STRIPPED_AUREL_LOG = add("stripped_aurel_log", ParadiseLostBlocks.STRIPPED_AUREL_LOG, building_block);
    public static final BlockItem STRIPPED_MOTHER_AUREL_LOG = add("stripped_mother_aurel_log", ParadiseLostBlocks.STRIPPED_MOTHER_AUREL_LOG, building_block);
    public static final BlockItem STRIPPED_ORANGE_LOG = add("stripped_orange_log", ParadiseLostBlocks.STRIPPED_ORANGE_LOG, building_block);
    public static final BlockItem STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", ParadiseLostBlocks.STRIPPED_WISTERIA_LOG, building_block);
    // stripped wood
    public static final BlockItem STRIPPED_AUREL_WOOD = add("stripped_aurel_wood", ParadiseLostBlocks.STRIPPED_AUREL_WOOD, building_block);
    public static final BlockItem STRIPPED_MOTHER_AUREL_WOOD = add("stripped_mother_aurel_wood", ParadiseLostBlocks.STRIPPED_MOTHER_AUREL_WOOD, building_block);
    public static final BlockItem STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", ParadiseLostBlocks.STRIPPED_ORANGE_WOOD, building_block);
    public static final BlockItem STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", ParadiseLostBlocks.STRIPPED_WISTERIA_WOOD, building_block);
    // wood
    public static final BlockItem AUREL_WOOD = add("aurel_wood", ParadiseLostBlocks.AUREL_WOOD, building_block);
    public static final BlockItem MOTHER_AUREL_WOOD = add("mother_aurel_wood", ParadiseLostBlocks.MOTHER_AUREL_WOOD, building_block);
    public static final BlockItem ORANGE_WOOD = add("orange_wood", ParadiseLostBlocks.ORANGE_WOOD, building_block);
    public static final BlockItem WISTERIA_WOOD = add("wisteria_wood", ParadiseLostBlocks.WISTERIA_WOOD, building_block);
    // glass

    // slabs
    public static final BlockItem AUREL_SLAB = add("aurel_slab", ParadiseLostBlocks.AUREL_SLAB, building_block);
    public static final BlockItem MOTHER_AUREL_SLAB = add("mother_aurel_slab", ParadiseLostBlocks.MOTHER_AUREL_SLAB, building_block);
    public static final BlockItem ORANGE_SLAB = add("orange_slab", ParadiseLostBlocks.ORANGE_SLAB, building_block);
    public static final BlockItem WISTERIA_SLAB = add("wisteria_slab", ParadiseLostBlocks.WISTERIA_SLAB, building_block);
    // smooth stuff
    // cobble variants
    public static final BlockItem MOSSY_FLOESTONE = add("mossy_floestone", ParadiseLostBlocks.MOSSY_FLOESTONE, building_block);
    public static final BlockItem GOLDEN_MOSSY_FLOESTONE = add("golden_mossy_floestone", ParadiseLostBlocks.GOLDEN_MOSSY_FLOESTONE, building_block);

    // bricks
    public static final BlockItem FLOESTONE_BRICK = add("floestone_brick", ParadiseLostBlocks.FLOESTONE_BRICK, building_block);
    public static final BlockItem CHISELED_FLOESTONE = add("chiseled_floestone", ParadiseLostBlocks.CHISELED_FLOESTONE, building_block);
    public static final BlockItem CARVED_STONE = add("carved_stone", ParadiseLostBlocks.CARVED_STONE, building_block);
    public static final BlockItem MOSSY_CARVED_STONE = add("mossy_carved_stone", ParadiseLostBlocks.MOSSY_CARVED_STONE, building_block);
    public static final BlockItem CRACKED_CARVED_STONE = add("cracked_carved_stone", ParadiseLostBlocks.CRACKED_CARVED_STONE, building_block);
    public static final BlockItem GLYPHED_CARVED_STONE = add("glyphed_carved_stone", ParadiseLostBlocks.GLYPHED_CARVED_STONE, building_block);
    public static final BlockItem CARVED_STONE_PANEL = add("carved_stone_panel", ParadiseLostBlocks.CARVED_STONE_PANEL, building_block);
    public static final BlockItem CARVED_STONE_PANEL_LIT = add("carved_stone_panel_lit", ParadiseLostBlocks.CARVED_STONE_PANEL_LIT, building_block);
    public static final BlockItem CARVED_STONE_EYE = add("carved_stone_eye", ParadiseLostBlocks.CARVED_STONE_EYE, building_block);
    public static final BlockItem CARVED_STONE_EYE_LIT = add("carved_stone_eye_lit", ParadiseLostBlocks.CARVED_STONE_EYE_LIT, building_block);

    public static final BlockItem GOLDEN_AMBER_TILE = add("golden_amber_tile", ParadiseLostBlocks.GOLDEN_AMBER_TILE, building_block);

    // stairs
    public static final BlockItem AUREL_STAIRS = add("aurel_stairs", ParadiseLostBlocks.AUREL_STAIRS, building_block);
    public static final BlockItem MOTHER_AUREL_STAIRS = add("mother_aurel_stairs", ParadiseLostBlocks.MOTHER_AUREL_STAIRS, building_block);
    public static final BlockItem ORANGE_STAIRS = add("orange_stairs", ParadiseLostBlocks.ORANGE_STAIRS, building_block);
    public static final BlockItem WISTERIA_STAIRS = add("wisteria_stairs", ParadiseLostBlocks.WISTERIA_STAIRS, building_block);
    // stone stairs + slabs
    public static final BlockItem FLOESTONE_STAIRS = add("floestone_stairs", ParadiseLostBlocks.FLOESTONE_STAIRS, building_block);
    public static final BlockItem COBBLED_FLOESTONE_STAIRS = add("cobbled_floestone_stairs", ParadiseLostBlocks.COBBLED_FLOESTONE_STAIRS, building_block);
    public static final BlockItem MOSSY_FLOESTONE_STAIRS = add("mossy_floestone_stairs", ParadiseLostBlocks.MOSSY_FLOESTONE_STAIRS, building_block);
    public static final BlockItem FLOESTONE_BRICK_STAIRS = add("floestone_brick_stairs", ParadiseLostBlocks.FLOESTONE_BRICK_STAIRS, building_block);
    public static final BlockItem CARVED_STAIRS = add("carved_stone_stairs", ParadiseLostBlocks.CARVED_STONE_STAIRS, building_block);
    public static final BlockItem MOSSY_CARVED_STAIRS = add("mossy_carved_stone_stairs", ParadiseLostBlocks.MOSSY_CARVED_STONE_STAIRS, building_block);
    public static final BlockItem FLOESTONE_SLAB = add("floestone_slab", ParadiseLostBlocks.FLOESTONE_SLAB, building_block);

    public static final BlockItem COBBLED_FLOESTONE_SLAB = add("cobbled_floestone_slab", ParadiseLostBlocks.COBBLED_FLOESTONE_SLAB, building_block);
    public static final BlockItem MOSSY_FLOESTONE_SLAB = add("mossy_floestone_slab", ParadiseLostBlocks.MOSSY_FLOESTONE_SLAB, building_block);
    public static final BlockItem FLOESTONE_BRICK_SLAB = add("floestone_brick_slab", ParadiseLostBlocks.FLOESTONE_BRICK_SLAB, building_block);
    public static final BlockItem CARVED_SLAB = add("carved_stone_slab", ParadiseLostBlocks.CARVED_STONE_SLAB, building_block);
    public static final BlockItem MOSSY_CARVED_SLAB = add("mossy_carved_stone_slab", ParadiseLostBlocks.MOSSY_CARVED_STONE_SLAB, building_block);
    public static final BlockItem GOLDEN_AMBER_TILE_SLAB = add("golden_amber_tile_slab", ParadiseLostBlocks.GOLDEN_AMBER_TILE_SLAB, building_block);
    public static final BlockItem GOLDEN_AMBER_TILE_STAIRS = add("golden_amber_tile_stairs", ParadiseLostBlocks.GOLDEN_AMBER_TILE_STAIRS, building_block);
    // colorfuls

    private static FabricItemSettings decoration() {
        return new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS);
    }

    private static final FabricItemSettings decoration = decoration();
    private static final FabricItemSettings sign = decoration().maxCount(16);
    private static final FabricItemSettings boat = decoration().maxCount(1);
    private static final FabricItemSettings hat = decoration().equipmentSlot(stack -> HEAD);

    // saplings
    public static final BlockItem AUREL_SAPLING = add("aurel_sapling", ParadiseLostBlocks.AUREL_SAPLING, decoration, compostable30);
    public static final BlockItem MOTHER_AUREL_SAPLING = add("mother_aurel_sapling", ParadiseLostBlocks.MOTHER_AUREL_SAPLING, decoration, compostable30);
    public static final BlockItem ORANGE_SAPLING = add("orange_sapling", ParadiseLostBlocks.ORANGE_SAPLING, decoration, compostable30);
    public static final BlockItem ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", ParadiseLostBlocks.ROSE_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", ParadiseLostBlocks.FROST_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", ParadiseLostBlocks.LAVENDER_WISTERIA_SAPLING, decoration, compostable30);
    // leaves
    public static final BlockItem AUREL_LEAVES = add("aurel_leaves", ParadiseLostBlocks.AUREL_LEAVES, decoration, compostable30);
    public static final BlockItem MOTHER_AUREL_LEAVES = add("mother_aurel_leaves", ParadiseLostBlocks.MOTHER_AUREL_LEAVES, decoration, compostable30);
    public static final BlockItem ORANGE_LEAVES = add("orange_leaves", ParadiseLostBlocks.ORANGE_LEAVES, decoration, compostable30);
    public static final BlockItem ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", ParadiseLostBlocks.ROSE_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", ParadiseLostBlocks.FROST_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", ParadiseLostBlocks.LAVENDER_WISTERIA_LEAVES, decoration, compostable30);
    // plants
    public static final BlockItem GRASS = add("grass_plant", ParadiseLostBlocks.GRASS, decoration, compostable30);
    public static final BlockItem GRASS_FLOWERING = add("grass_flowering", ParadiseLostBlocks.GRASS_FLOWERING, decoration, compostable30);
    public static final BlockItem SHORT_GRASS = add("short_grass", ParadiseLostBlocks.SHORT_GRASS, decoration, compostable30);
    public static final BlockItem FERN = add("fern", ParadiseLostBlocks.FERN, decoration, compostable30);
    public static final BlockItem BUSH = add("bush", ParadiseLostBlocks.BUSH, decoration, compostable30);
    public static final BlockItem SHAMROCK = add("shamrock", ParadiseLostBlocks.SHAMROCK, decoration, compostable50);
    public static final BlockItem MALT_SPRIG = add("malt_sprig", ParadiseLostBlocks.MALT_SPRIG, decoration, compostable30);
    public static final BlockItem HALOPHIA = add("halophia", ParadiseLostBlocks.HALOPHIA, decoration, compostable30);
    public static final BlockItem GIANT_LILY = add("giant_lily", new BlockItem(ParadiseLostBlocks.GIANT_LILY, hat), compostable100);
    public static final BlockItem WEEPING_CLOUDBURST = add("weeping_cloudburst", ParadiseLostBlocks.WEEPING_CLOUDBURST, decoration, compostable30);
    public static final BlockItem MOSS_STAR = add("moss_star", ParadiseLostBlocks.MOSS_STAR, decoration, compostable50);
    public static final BlockItem MOSS_BALL = add("moss_ball", ParadiseLostBlocks.MOSS_BALL, decoration, compostable30);

    public static final BlockItem ANCIENT_FLOWER = add("ancient_flower", ParadiseLostBlocks.ANCIENT_FLOWER, decoration, compostable65);
    public static final BlockItem ATARAXIA = add("ataraxia", ParadiseLostBlocks.ATARAXIA, decoration, compostable65);
    public static final BlockItem CLOUDSBLUFF = add("cloudsbluff", ParadiseLostBlocks.CLOUDSBLUFF, decoration, compostable65);
    public static final BlockItem DRIGEAN = add("drigean", ParadiseLostBlocks.DRIGEAN, decoration, compostable65);
    public static final BlockItem LUMINAR = add("luminar", ParadiseLostBlocks.LUMINAR, decoration, compostable65);

    public static final BlockItem WILD_FLAX = add("wild_flax", ParadiseLostBlocks.WILD_FLAX, decoration, compostable100);

    public static final BlockItem ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", ParadiseLostBlocks.ROSE_WISTERIA_HANGER, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", ParadiseLostBlocks.FROST_WISTERIA_HANGER, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", ParadiseLostBlocks.LAVENDER_WISTERIA_HANGER, decoration, compostable30);

    public static final BlockItem AUREL_LEAF_PILE = add("aurel_leaf_pile", ParadiseLostBlocks.AUREL_LEAF_PILE, decoration, compostable30);
    public static final BlockItem ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", ParadiseLostBlocks.ROSE_WISTERIA_LEAF_PILE, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", ParadiseLostBlocks.FROST_WISTERIA_LEAF_PILE, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", ParadiseLostBlocks.LAVENDER_WISTERIA_LEAF_PILE, decoration, compostable30);
    // tall plants
    public static final BlockItem TALL_GRASS = add("tall_grass", ParadiseLostBlocks.TALL_GRASS, decoration, compostable50);
    public static final BlockItem HONEY_NETTLE = add("honey_nettle", ParadiseLostBlocks.HONEY_NETTLE, decoration, compostable50);

    public static final BlockItem LIVERWORT = add("liverwort", ParadiseLostBlocks.LIVERWORT, decoration, compostable100);
    public static final BlockItem LIVERWORT_CARPET = add("liverwort_carpet", ParadiseLostBlocks.LIVERWORT_CARPET, decoration, compostable65);

    // FUNGI BAYBEEE
    public static final BlockItem LICHEN = add("lichen", ParadiseLostBlocks.LICHEN, decoration, compostable50);
    public static final BlockItem LUCATIEL_LICHEN = add("lucatiel_lichen", ParadiseLostBlocks.LUCATIEL_LICHEN, decoration, compostable50);
    public static final BlockItem LICHEN_PILE = add("lichen_pile", ParadiseLostBlocks.LICHEN_PILE, decoration, compostable30);
    public static final BlockItem LUCATIEL_LICHEN_PILE = add("lucatiel_lichen_pile", ParadiseLostBlocks.LUCATIEL_LICHEN_PILE, decoration, compostable100);

    public static final BlockItem ROOTCAP = add("rootcap", ParadiseLostBlocks.ROOTCAP, decoration(), compostable65);
    public static final BlockItem BROWN_SPORECAP = add("brown_sporecap", ParadiseLostBlocks.BROWN_SPORECAP, decoration(), compostable65);
    public static final BlockItem PINK_SPORECAP = add("pink_sporecap", ParadiseLostBlocks.PINK_SPORECAP, decoration(), compostable65);

    public static final BlockItem SWEDROOT_SPREAD = add("swedroot_spread", ParadiseLostBlocks.SWEDROOT_SPREAD, decoration(), compostable65);

    public static final BlockItem FLAXWEAVE_CUSHION = add("flaxweave_cushion", ParadiseLostBlocks.FLAXWEAVE_CUSHION, decoration, fuel(300));

    public static final BlockItem CHEESECAKE = add("halflight_cheesecake", ParadiseLostBlocks.CHEESECAKE, food());

    public static final BlockItem AMADRYS_BUNDLE = add("amadrys_bundle", ParadiseLostBlocks.AMADRYS_BUNDLE, decoration());

    // lights
    public static final BlockItem CHERINE_LANTERN = add("cherine_lantern", ParadiseLostBlocks.CHERINE_LANTERN, decoration);
    public static final WallStandingBlockItem CHERINE_TORCH = add("cherine_torch", new WallStandingBlockItem(ParadiseLostBlocks.CHERINE_TORCH, ParadiseLostBlocks.CHERINE_TORCH_WALL, decoration));
    // util blocks (enchanter, freezer, etc.)

    public static final BlockItem CHERINE_CAMPFIRE = add("cherine_campfire", ParadiseLostBlocks.CHERINE_CAMPFIRE, decoration);

    // door-like things
    public static final BlockItem AUREL_DOOR = add("aurel_door", ParadiseLostBlocks.AUREL_DOOR, decoration);
    public static final BlockItem MOTHER_AUREL_DOOR = add("mother_aurel_door", ParadiseLostBlocks.MOTHER_AUREL_DOOR, decoration);
    public static final BlockItem ORANGE_DOOR = add("orange_door", ParadiseLostBlocks.ORANGE_DOOR, decoration);
    public static final BlockItem WISTERIA_DOOR = add("wisteria_door", ParadiseLostBlocks.WISTERIA_DOOR, decoration);

    public static final BlockItem AUREL_TRAPDOOR = add("aurel_trapdoor", ParadiseLostBlocks.AUREL_TRAPDOOR, decoration);
    public static final BlockItem MOTHER_AUREL_TRAPDOOR = add("mother_aurel_trapdoor", ParadiseLostBlocks.MOTHER_AUREL_TRAPDOOR, decoration);
    public static final BlockItem ORANGE_TRAPDOOR = add("orange_trapdoor", ParadiseLostBlocks.ORANGE_TRAPDOOR, decoration);
    public static final BlockItem WISTERIA_TRAPDOOR = add("wisteria_trapdoor", ParadiseLostBlocks.WISTERIA_TRAPDOOR, decoration);

    public static final BlockItem AUREL_FENCE_GATE = add("aurel_fence_gate", ParadiseLostBlocks.AUREL_FENCE_GATE, decoration);
    public static final BlockItem MOTHER_AUREL_FENCE_GATE = add("mother_aurel_fence_gate", ParadiseLostBlocks.MOTHER_AUREL_FENCE_GATE, decoration);
    public static final BlockItem ORANGE_FENCE_GATE = add("orange_fence_gate", ParadiseLostBlocks.ORANGE_FENCE_GATE, decoration);
    public static final BlockItem WISTERIA_FENCE_GATE = add("wisteria_fence_gate", ParadiseLostBlocks.WISTERIA_FENCE_GATE, decoration);
    // fences
    public static final BlockItem AUREL_FENCE = add("aurel_fence", ParadiseLostBlocks.AUREL_FENCE, decoration);
    public static final BlockItem MOTHER_AUREL_FENCE = add("mother_aurel_fence", ParadiseLostBlocks.MOTHER_AUREL_FENCE, decoration);
    public static final BlockItem ORANGE_FENCE = add("orange_fence", ParadiseLostBlocks.ORANGE_FENCE, decoration);
    public static final BlockItem WISTERIA_FENCE = add("wisteria_fence", ParadiseLostBlocks.WISTERIA_FENCE, decoration);
    // walls
    public static final BlockItem FLOESTONE_WALL = add("floestone_wall", ParadiseLostBlocks.FLOESTONE_WALL, decoration);
    public static final BlockItem COBBLED_FLOESTONE_WALL = add("cobbled_floestone_wall", ParadiseLostBlocks.COBBLED_FLOESTONE_WALL, decoration);
    public static final BlockItem MOSSY_FLOESTONE_WALL = add("mossy_floestone_wall", ParadiseLostBlocks.MOSSY_FLOESTONE_WALL, decoration);
    public static final BlockItem FLOESTONE_BRICK_WALL = add("floestone_brick_wall", ParadiseLostBlocks.FLOESTONE_BRICK_WALL, decoration);
    public static final BlockItem CARVED_WALL = add("carved_stone_wall", ParadiseLostBlocks.CARVED_STONE_WALL, decoration);
    public static final BlockItem MOSSY_CARVED_WALL = add("mossy_carved_stone_wall", ParadiseLostBlocks.MOSSY_CARVED_STONE_WALL, decoration);

    // panes & chains
    public static final BlockItem OLVITE_CHAIN = add("olvite_chain", ParadiseLostBlocks.OLVITE_CHAIN, decoration);
    // carpets

    // path & farmland
    public static final BlockItem FARMLAND = add("farmland", ParadiseLostBlocks.FARMLAND, decoration);
    public static final BlockItem DIRT_PATH = add("grass_path", ParadiseLostBlocks.DIRT_PATH, decoration);
    // signs, wall stuff.
    public static final SignItem AUREL_SIGN = add("aurel_sign", new SignItem(sign, ParadiseLostBlocks.AUREL_SIGN, ParadiseLostBlocks.AUREL_WALL_SIGN));
    public static final SignItem MOTHER_AUREL_SIGN = add("mother_aurel_sign", new SignItem(sign, ParadiseLostBlocks.MOTHER_AUREL_SIGN, ParadiseLostBlocks.MOTHER_AUREL_WALL_SIGN));
    public static final SignItem ORANGE_SIGN = add("orange_sign", new SignItem(sign, ParadiseLostBlocks.ORANGE_SIGN, ParadiseLostBlocks.ORANGE_WALL_SIGN));
    public static final SignItem WISTERIA_SIGN = add("wisteria_sign", new SignItem(sign, ParadiseLostBlocks.WISTERIA_SIGN, ParadiseLostBlocks.WISTERIA_WALL_SIGN));
    // beds

    // etc.
    public static final BlockItem AUREL_BUTTON = add("aurel_button", ParadiseLostBlocks.AUREL_BUTTON, decoration);
    public static final BlockItem MOTHER_AUREL_BUTTON = add("mother_aurel_button", ParadiseLostBlocks.MOTHER_AUREL_BUTTON, decoration);
    public static final BlockItem ORANGE_BUTTON = add("orange_button", ParadiseLostBlocks.ORANGE_BUTTON, decoration);
    public static final BlockItem WISTERIA_BUTTON = add("wisteria_button", ParadiseLostBlocks.WISTERIA_BUTTON, decoration);

    public static final BlockItem AUREL_PRESSURE_PLATE = add("aurel_pressure_plate", ParadiseLostBlocks.AUREL_PRESSURE_PLATE, decoration);
    public static final BlockItem MOTHER_AUREL_PRESSURE_PLATE = add("mother_aurel_pressure_plate", ParadiseLostBlocks.MOTHER_AUREL_PRESSURE_PLATE, decoration);
    public static final BlockItem ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", ParadiseLostBlocks.ORANGE_PRESSURE_PLATE, decoration);
    public static final BlockItem WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", ParadiseLostBlocks.WISTERIA_PRESSURE_PLATE, decoration);

    //TODO: Implement dungeon switch block
//    public static final BlockItem DUNGEON_SWITCH = add("dungeonswitch", ParadiseLostBlocks.DUNGEON_SWITCH, decoration);

    // these should be moved... somewhere?
    public static final BoatItem AUREL_BOAT = ParadiseLostBlocks.AUREL.boatFactory(boat).item;
    public static final BoatItem MOTHER_AUREL_BOAT = ParadiseLostBlocks.MOTHER_AUREL.boatFactory(boat).item;
    public static final BoatItem ORANGE_BOAT = ParadiseLostBlocks.ORANGE.boatFactory(boat).item;
    public static final BoatItem WISTERIA_BOAT = ParadiseLostBlocks.WISTERIA.boatFactory(boat).item;

    // Chests
    public static final BlockItem AUREL_CHEST = add("skyroot_chest", ParadiseLostBlocks.AUREL_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));
    public static final BlockItem MOTHER_AUREL_CHEST = add("golden_oak_chest", ParadiseLostBlocks.MOTHER_AUREL_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));
    public static final BlockItem ORANGE_CHEST = add("orange_chest", ParadiseLostBlocks.ORANGE_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));
    public static final BlockItem CRYSTAL_CHEST = add("crystal_chest", ParadiseLostBlocks.CRYSTAL_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));
    public static final BlockItem WISTERIA_CHEST = add("wisteria_chest", ParadiseLostBlocks.WISTERIA_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));

    public static void init() {
        ParadiseLostRegistryQueues.ITEM.register();
    }

    @SafeVarargs
    private static <V extends Item> V add(String id, V item, Action<? super V>... additionalActions) {
        if (id.equals("grass"))
            ParadiseLost.LOG.error("id");
        return ParadiseLostRegistryQueues.ITEM.add(locate(id), item, additionalActions);
    }

    @SafeVarargs
    private static BlockItem add(String id, Block block, Settings settings, Action<? super BlockItem>... additionalActions) {
        return add(id,
                (block instanceof DoorBlock
                        || block instanceof TallPlantBlock
                        || block instanceof TallFlowerBlock
                )
                        ? new TallBlockItem(block, settings)
                        : new BlockItem(block, settings),
                additionalActions);
    }

    // For access to protected constructors:

    private static class ParadiseLostPickaxeItem extends PickaxeItem {
        protected ParadiseLostPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class ParadiseLostAxeItem extends AxeItem {
        protected ParadiseLostAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class ParadiseLostHoeItem extends HoeItem {
        protected ParadiseLostHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class ParadiseLostMusicDiscItem extends MusicDiscItem {
        protected ParadiseLostMusicDiscItem(int comparatorValueIn, SoundEvent soundIn, Settings settings) {
            // TODO: Length will probably need to be changed
            super(comparatorValueIn, soundIn, settings, 0);
        }
    }
}
