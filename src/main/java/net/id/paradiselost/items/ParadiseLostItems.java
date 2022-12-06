package net.id.paradiselost.items;

import com.google.common.collect.ImmutableList;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.fluids.ParadiseLostFluids;
import net.id.paradiselost.items.accessories.ParachuteTrinketItem;
import net.id.paradiselost.items.armor.ParadiseLostArmorMaterials;
import net.id.paradiselost.items.food.ParadiseLostFoodComponent;
import net.id.paradiselost.items.food.ValkyrieMilkItem;
import net.id.paradiselost.items.food.WhiteAppleItem;
import net.id.paradiselost.items.misc.*;
import net.id.paradiselost.items.resources.AmbrosiumShardItem;
import net.id.paradiselost.items.tools.ParadiseLostToolMaterials;
import net.id.paradiselost.items.tools.SkyrootBucketItem;
import net.id.paradiselost.items.tools.VialItem;
import net.id.paradiselost.items.tools.base_tools.*;
import net.id.paradiselost.items.tools.bloodstone.AbstentineBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.AmbrosiumBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.GravititeBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.ZaniteBloodstoneItem;
import net.id.paradiselost.items.utils.ParadiseLostRarity;
import net.id.paradiselost.items.weapons.*;
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
    public static final Item AECHOR_PETAL = add("aechor_petal", new Item(resource), compostable65);
    public static final Item NIGTHMARE_FUEL = add("nightmare_fuel", new LoreItem(nightmare().rarity(UNCOMMON), ImmutableList.of(Text.translatable("item.paradise_lost.nightmare_fuel.tooltip").formatted(Formatting.GRAY))));
    public static final Item CROW_EYE = add("crow_eye", new LoreItem(nightmare().maxCount(1).rarity(UNCOMMON), ImmutableList.of(Text.translatable("item.paradise_lost.crow_eye.tooltip").formatted(Formatting.GRAY))));
    public static final Item SWET_BALL = add("swet_ball", new Item(resource), swetColor);
    public static final AmbrosiumShardItem AMBROSIUM_SHARD = add("ambrosium_shard", new AmbrosiumShardItem(resource), fuel(500));
    public static final Item ZANITE_GEM = add("zanite_gemstone", new Item(resource));
    public static final Item ZANITE_FRAGMENT = add("zanite_fragment", new Item(resource));
    public static final Item GRAVITITE_GEM = add("gravitite_gemstone", new Item(resource));
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
    public static final ZaniteShovelItem ZANITE_SHOVEL = add("zanite_shovel", new ZaniteShovelItem(ParadiseLostToolMaterials.ZANITE, 1.5f, -3f, tool));
    public static final ZanitePickaxeItem ZANITE_PICKAXE = add("zanite_pickaxe", new ZanitePickaxeItem(ParadiseLostToolMaterials.ZANITE, 1, -2.8f, tool));
    public static final ZaniteAxeItem ZANITE_AXE = add("zanite_axe", new ZaniteAxeItem(ParadiseLostToolMaterials.ZANITE, 6f, -3.1f, tool));
    public static final ZaniteSwordItem ZANITE_SWORD = add("zanite_sword", new ZaniteSwordItem(ParadiseLostToolMaterials.ZANITE, 3, -2.4f, tool));
    public static final ZaniteHoeItem ZANITE_HOE = add("zanite_hoe", new ZaniteHoeItem(ParadiseLostToolMaterials.ZANITE, 1, 3f, tool));

    public static final GravititeShovelItem GRAVITITE_SHOVEL = add("gravitite_shovel", new GravititeShovelItem(ParadiseLostToolMaterials.GRAVITITE, 1.5f, -3f, rareTool));
    public static final GravititePickaxeItem GRAVITITE_PICKAXE = add("gravitite_pickaxe", new GravititePickaxeItem(ParadiseLostToolMaterials.GRAVITITE, 1, -2.8f, rareTool));
    public static final GravititeAxeItem GRAVITITE_AXE = add("gravitite_axe", new GravititeAxeItem(ParadiseLostToolMaterials.GRAVITITE, 5f, -3f, rareTool));
    public static final SwordItem GRAVITITE_SWORD = add("gravitite_sword", new SwordItem(ParadiseLostToolMaterials.GRAVITITE, 3, -2.4f, rareTool));
    public static final GravititeHoeItem GRAVITITE_HOE = add("gravitite_hoe", new GravititeHoeItem(ParadiseLostToolMaterials.GRAVITITE, 1, 4f, rareTool));

    public static final ShovelItem VALKYRIE_SHOVEL = add("valkyrie_shovel", new ShovelItem(ParadiseLostToolMaterials.VALKYRIE, 1.5f, -3f, paradiseLostLootTool));
    public static final ParadiseLostPickaxeItem VALKYRIE_PICKAXE = add("valkyrie_pickaxe", new ParadiseLostPickaxeItem(ParadiseLostToolMaterials.VALKYRIE, 1, -2.8f, paradiseLostLootTool));
    public static final ParadiseLostAxeItem VALKYRIE_AXE = add("valkyrie_axe", new ParadiseLostAxeItem(ParadiseLostToolMaterials.VALKYRIE, 4f, -2.9f, paradiseLostLootTool));
    public static final ValkyrieLanceItem VALKYRIE_LANCE = add("valkyrie_lance", new ValkyrieLanceItem(ParadiseLostToolMaterials.VALKYRIE, 10, -3f, 6f, 4f, paradiseLostLootTool));
    public static final ParadiseLostHoeItem VALKYRIE_HOE = add("valkyrie_hoe", new ParadiseLostHoeItem(ParadiseLostToolMaterials.VALKYRIE, 1, 5f, paradiseLostLootTool));
    
    public static final DartItem GOLDEN_DART = add("golden_dart", new DartItem(tool()));
    public static final DartItem ENCHANTED_DART = add("enchanted_dart", new DartItem(tool().rarity(RARE)));
    public static final DartItem POISON_DART = add("poison_dart", new DartItem(tool()));
    public static final DartShooterItem GOLDEN_DART_SHOOTER = add("golden_dart_shooter", new DartShooterItem(GOLDEN_DART, unstackableTool));
    public static final DartShooterItem ENCHANTED_DART_SHOOTER = add("enchanted_dart_shooter", new DartShooterItem(ENCHANTED_DART, unstackableRareTool));
    public static final DartShooterItem POISON_DART_SHOOTER = add("poison_dart_shooter", new DartShooterItem(POISON_DART, unstackableTool));

    public static final TrinketItem CLOUD_PARACHUTE = add("cold_parachute", new ParachuteTrinketItem(unstackableTool, "cloud_parachute"));
    public static final TrinketItem GOLDEN_CLOUD_PARACHUTE = add("golden_parachute", new ParachuteTrinketItem(tool().maxCount(1).maxDamage(20), "golden_parachute"));

    public static final AmbrosiumBloodstoneItem AMBROSIUM_BLOODSTONE = add("ambrosium_bloodstone", new AmbrosiumBloodstoneItem(unstackableTool));
    public static final ZaniteBloodstoneItem ZANITE_BLOODSTONE = add("zanite_bloodstone", new ZaniteBloodstoneItem(unstackableTool));
    public static final GravititeBloodstoneItem GRAVITITE_BLOODSTONE = add("gravitite_bloodstone", new GravititeBloodstoneItem(unstackableTool));
    public static final AbstentineBloodstoneItem ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(unstackableTool));

    private static Settings wearable() {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_WEARABLES);
    }

    private static final Settings WEARABLE = wearable();
    private static final Settings RARE_WEARABLE = wearable().rarity(RARE);
    private static final Settings PARADISE_LOST_LOOT_WEARABLE = wearable().rarity(ParadiseLostRarity.PARADISE_LOST_LOOT);
    public static final ArmorItem ZANITE_HELMET = add("zanite_helmet", new ArmorItem(ParadiseLostArmorMaterials.ZANITE, HEAD, WEARABLE));
    public static final ArmorItem ZANITE_CHESTPLATE = add("zanite_chestplate", new ArmorItem(ParadiseLostArmorMaterials.ZANITE, CHEST, WEARABLE));
    public static final ArmorItem ZANITE_LEGGINGS = add("zanite_leggings", new ArmorItem(ParadiseLostArmorMaterials.ZANITE, LEGS, WEARABLE));
    public static final ArmorItem ZANITE_BOOTS = add("zanite_boots", new ArmorItem(ParadiseLostArmorMaterials.ZANITE, FEET, WEARABLE));

    public static final ArmorItem GRAVITITE_HELMET = add("gravitite_helmet", new ArmorItem(ParadiseLostArmorMaterials.GRAVITITE, HEAD, RARE_WEARABLE));
    public static final ArmorItem GRAVITITE_CHESTPLATE = add("gravitite_chestplate", new ArmorItem(ParadiseLostArmorMaterials.GRAVITITE, CHEST, RARE_WEARABLE));
    public static final ArmorItem GRAVITITE_LEGGINGS = add("gravitite_leggings", new ArmorItem(ParadiseLostArmorMaterials.GRAVITITE, LEGS, RARE_WEARABLE));
    public static final ArmorItem GRAVITITE_BOOTS = add("gravitite_boots", new ArmorItem(ParadiseLostArmorMaterials.GRAVITITE, FEET, RARE_WEARABLE));

    public static final ArmorItem NEPTUNE_HELMET = add("neptune_helmet", new ArmorItem(ParadiseLostArmorMaterials.NEPTUNE, HEAD, PARADISE_LOST_LOOT_WEARABLE));
    public static final ArmorItem NEPTUNE_CHESTPLATE = add("neptune_chestplate", new ArmorItem(ParadiseLostArmorMaterials.NEPTUNE, CHEST, PARADISE_LOST_LOOT_WEARABLE));
    public static final ArmorItem NEPTUNE_LEGGINGS = add("neptune_leggings", new ArmorItem(ParadiseLostArmorMaterials.NEPTUNE, LEGS, PARADISE_LOST_LOOT_WEARABLE));
    public static final ArmorItem NEPTUNE_BOOTS = add("neptune_boots", new ArmorItem(ParadiseLostArmorMaterials.NEPTUNE, FEET, PARADISE_LOST_LOOT_WEARABLE));

    private static Settings food() {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_FOOD);
    }

    private static Settings food(FoodComponent foodComponent) {
        return new Settings().group(ParadiseLostItemGroups.PARADISE_LOST_FOOD).food(foodComponent);
    }

    private static Settings food(FoodComponent foodComponent, Rarity rarity) {
        return food(foodComponent).rarity(rarity);
    }

    public static final AliasedBlockItem BLUEBERRY = add("blue_berry", new AliasedBlockItem(ParadiseLostBlocks.BLUEBERRY_BUSH, food(ParadiseLostFoodComponent.BLUEBERRY)), compostable30);
    public static final Item ENCHANTED_BLUEBERRY = add("enchanted_blueberry", new Item(food(ParadiseLostFoodComponent.ENCHANTED_BLUEBERRY, RARE)), compostable50);
    public static final Item ORANGE = add("orange", new Item(food(ParadiseLostFoodComponent.ORANGE)), compostable65);
    public static final WhiteAppleItem WHITE_APPLE = add("white_apple", new WhiteAppleItem(food(ParadiseLostFoodComponent.WHITE_APPLE)), compostable(0f));
    public static final AliasedBlockItem AMADRYS_BUSHEL = add("amadrys_bushel", new AliasedBlockItem(ParadiseLostBlocks.AMADRYS, food(ParadiseLostFoodComponent.GENERIC_WORSE)), compostable30);
    public static final Item AMADRYS_NOODLES = add("amadrys_noodles", new StewItem(food(ParadiseLostFoodComponent.AMADRYS_NOODLES)));
    public static final Item AMADRYS_BREAD = add("amadrys_bread", new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD)));
    public static final Item AMADRYS_BREAD_GLAZED = add("amadrys_bread_glazed", new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED)));
    public static final AliasedBlockItem SWEDROOT = add("swedroot", new AliasedBlockItem(ParadiseLostBlocks.SWEDROOT, food(ParadiseLostFoodComponent.GENERIC)), compostable30);
    public static final AliasedBlockItem FLAXSEED = add("flaxseed", new AliasedBlockItem(ParadiseLostBlocks.FLAX, food()), compostable30);
    public static final Item BLUE_GUMMY_SWET = add("blue_gummy_swet", new Item(food(ParadiseLostFoodComponent.GUMMY_SWET, ParadiseLostRarity.PARADISE_LOST_LOOT)));
    public static final Item GOLDEN_GUMMY_SWET = add("golden_gummy_swet", new Item(food(ParadiseLostFoodComponent.GUMMY_SWET, ParadiseLostRarity.PARADISE_LOST_LOOT)));
    public static final ValkyrieMilkItem VALKYRIE_MILK = add("valkyrie_milk", new ValkyrieMilkItem(food(ParadiseLostFoodComponent.VALKYRIE_MILK, EPIC).maxCount(1)));
    public static final Item CANDY_CANE = add("candy_cane", new Item(food(ParadiseLostFoodComponent.GENERIC)), compostable30);
    public static final Item GINGERBREAD_MAN = add("ginger_bread_man", new Item(food(ParadiseLostFoodComponent.GENERIC)), compostable30);
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

    public static final SkyrootBucketItem SKYROOT_BUCKET = add("skyroot_bucket", new SkyrootBucketItem(misc().maxCount(16)), fuel(200), emptyBucketBehavior);

    private static final Settings skyrootBucket = misc().maxCount(1).recipeRemainder(SKYROOT_BUCKET);
    public static final SkyrootBucketItem SKYROOT_WATER_BUCKET = add("skyroot_water_bucket", new SkyrootBucketItem(Fluids.WATER, skyrootBucket), emptiableBucketBehavior);
    public static final SkyrootBucketItem SKYROOT_MILK_BUCKET = add("skyroot_milk_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_POISON_BUCKET = add("skyroot_poison_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_REMEDY_BUCKET = add("skyroot_remedy_bucket", new SkyrootBucketItem(skyrootBucket));

    public static final VialItem QUICKSOIL_VIAL = add("quicksoil_vial", new VialItem(Fluids.EMPTY, misc().maxCount(32)));
    public static final VialItem AERCLOUD_VIAL = add("aercloud_vial", new VialItem(ParadiseLostFluids.DENSE_AERCLOUD, misc().maxCount(32)));
    /* TODO: Implement dungeon keys
    public static final Item BRONZE_KEY = add("bronze_key", new Item(misc().rarity(ParadiseLostRarity.PARADISE_LOST_LOOT)));
    public static final Item SILVER_KEY = add("silver_key", new Item(misc().rarity(ParadiseLostRarity.PARADISE_LOST_LOOT)));
    public static final Item GOLDEN_KEY = add("golden_key", new Item(misc().rarity(ParadiseLostRarity.PARADISE_LOST_LOOT)));
    */

    public static final SpawnEggItem AECHOR_PLANT_SPAWN_EGG = add("aechor_plant_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.AECHOR_PLANT, 0x97DED4, 0x31897D, misc), spawnEggBehavior);
    //    public static final SpawnEggItem CHEST_MIMIC_SPAWN_EGG = null;
    public static final SpawnEggItem COCKATRICE_SPAWN_EGG = add("cockatrice_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.COCKATRICE, 0x9FC3F7, 0x3D2338, misc), spawnEggBehavior);
    public static final SpawnEggItem AERBUNNY_SPAWN_EGG = add("aerbunny_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.AERBUNNY, 0xC5D6ED, 0x82A6D9, misc), spawnEggBehavior);
    public static final SpawnEggItem AERWHALE_SPAWN_EGG = add("aerwhale_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.AERWHALE, 0x5C6D91, 0xDEDBCE, misc), spawnEggBehavior);
    public static final SpawnEggItem MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.MOA, 0xC55C2E4, 0xB3A8BB, misc), spawnEggBehavior);
    public static final SpawnEggItem SWET_SPAWN_EGG = add("swet_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.WHITE_SWET, 0x717375, 0xffffff, misc), spawnEggBehavior);
    public static final SpawnEggItem BLUE_SWET_SPAWN_EGG = add("blue_swet_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.BLUE_SWET, 0x46699E, 0xf2f2f2, misc), spawnEggBehavior);
    public static final SpawnEggItem PURPLE_SWET_SPAWN_EGG = add("purple_swet_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.PURPLE_SWET, 0x5D548C, 0xf2f2f2, misc), spawnEggBehavior);
    public static final SpawnEggItem GOLDEN_SWET_SPAWN_EGG = add("golden_swet_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.GOLDEN_SWET, 0xC99D36, 0xf2f2f2, misc), spawnEggBehavior);

    private static FabricItemSettings building_block() {
        return new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_BUILDING_BLOCKS);
    }
    private static final FabricItemSettings building_block = building_block();

    // stone
    public static final BlockItem HOLYSTONE = add("holystone", ParadiseLostBlocks.HOLYSTONE, building_block);
    public static final BlockItem COBBLED_HOLYSTONE = add("cobbled_holystone", ParadiseLostBlocks.COBBLED_HOLYSTONE, building_block);
    public static final BlockItem ICESTONE = add("icestone", ParadiseLostBlocks.ICESTONE, building_block);
    public static final BlockItem AEROGEL = add("aerogel", ParadiseLostBlocks.AEROGEL, building_block);
    // nature
    public static final BlockItem HIGHLANDS_GRASS = add("highlands_grass", ParadiseLostBlocks.HIGHLANDS_GRASS, building_block);

    public static final BlockItem ENCHANTED_GRASS = add("enchanted_grass", ParadiseLostBlocks.ENCHANTED_GRASS, building_block);
    public static final BlockItem FROZEN_GRASS = add("frozen_grass", ParadiseLostBlocks.FROZEN_GRASS, building_block);
    public static final BlockItem DIRT = add("dirt", ParadiseLostBlocks.DIRT, building_block);
    public static final BlockItem COARSE_DIRT = add("coarse_dirt", ParadiseLostBlocks.COARSE_DIRT, building_block);
    public static final BlockItem PERMAFROST = add("permafrost", ParadiseLostBlocks.PERMAFROST, building_block);
    public static final BlockItem QUICKSOIL = add("quicksoil", ParadiseLostBlocks.QUICKSOIL, building_block);
    public static final BlockItem PACKED_SWEDROOT = add("packed_swedroot", ParadiseLostBlocks.PACKED_SWEDROOT, building_block, compostable85);
    public static final BlockItem COLD_AERCLOUD = add("cold_aercloud", ParadiseLostBlocks.COLD_AERCLOUD, building_block);
    public static final BlockItem BLUE_AERCLOUD = add("blue_aercloud", ParadiseLostBlocks.BLUE_AERCLOUD, building_block);
    public static final BlockItem PINK_AERCLOUD = add("pink_aercloud", ParadiseLostBlocks.PINK_AERCLOUD, building_block);
    public static final BlockItem GOLDEN_AERCLOUD = add("golden_aercloud", ParadiseLostBlocks.GOLDEN_AERCLOUD, building_block);
    // planks
    public static final BlockItem SKYROOT_PLANKS = add("skyroot_planks", ParadiseLostBlocks.SKYROOT_PLANKS, building_block);
    public static final BlockItem GOLDEN_OAK_PLANKS = add("golden_oak_planks", ParadiseLostBlocks.GOLDEN_OAK_PLANKS, building_block);
    public static final BlockItem ORANGE_PLANKS = add("orange_planks", ParadiseLostBlocks.ORANGE_PLANKS, building_block);
    public static final BlockItem CRYSTAL_PLANKS = add("crystal_planks", ParadiseLostBlocks.CRYSTAL_PLANKS, building_block);
    public static final BlockItem WISTERIA_PLANKS = add("wisteria_planks", ParadiseLostBlocks.WISTERIA_PLANKS, building_block);
    public static final BlockItem SKYROOT_BOOKSHELF = add("skyroot_bookshelf", ParadiseLostBlocks.SKYROOT_BOOKSHELF, building_block);
    // ores
    public static final BlockItem AMBROSIUM_ORE = add("ambrosium_ore", ParadiseLostBlocks.AMBROSIUM_ORE, building_block);
    public static final BlockItem ZANITE_ORE = add("zanite_ore", ParadiseLostBlocks.ZANITE_ORE, building_block);
    public static final BlockItem GRAVITITE_ORE = add("gravitite_ore", ParadiseLostBlocks.GRAVITITE_ORE, building_block);
    // ore blocks
    public static final BlockItem AMBROSIUM_BLOCK = add("ambrosium_block", ParadiseLostBlocks.AMBROSIUM_BLOCK, building_block, fuel(5000));
    public static final BlockItem ZANITE_BLOCK = add("zanite_block", ParadiseLostBlocks.ZANITE_BLOCK, building_block);
    public static final BlockItem BLOCK_OF_GRAVITITE = add("block_of_gravitite", ParadiseLostBlocks.BLOCK_OF_GRAVITITE, building_block);
    // move this somewhere else
    public static final BlockItem GRAVITITE_LEVITATOR = add("gravitite_levitator", ParadiseLostBlocks.GRAVITITE_LEVITATOR, building_block);
    // logs
    public static final BlockItem SKYROOT_LOG = add("skyroot_log", ParadiseLostBlocks.SKYROOT_LOG, building_block);
    public static final BlockItem MOTTLED_SKYROOT_LOG = add("mottled_skyroot_log", ParadiseLostBlocks.MOTTLED_SKYROOT_LOG, building_block);
    public static final BlockItem MOTTLED_SKYROOT_FALLEN_LOG = add("mottled_skyroot_fallen_log", ParadiseLostBlocks.MOTTLED_SKYROOT_FALLEN_LOG, building_block);
    public static final BlockItem GOLDEN_OAK_LOG = add("golden_oak_log", ParadiseLostBlocks.GOLDEN_OAK_LOG, building_block);
    public static final BlockItem ORANGE_LOG = add("orange_log", ParadiseLostBlocks.ORANGE_LOG, building_block);
    public static final BlockItem CRYSTAL_LOG = add("crystal_log", ParadiseLostBlocks.CRYSTAL_LOG, building_block);
    public static final BlockItem WISTERIA_LOG = add("wisteria_log", ParadiseLostBlocks.WISTERIA_LOG, building_block);
    // stripped logs
    public static final BlockItem STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", ParadiseLostBlocks.STRIPPED_SKYROOT_LOG, building_block);
    public static final BlockItem STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", ParadiseLostBlocks.STRIPPED_GOLDEN_OAK_LOG, building_block);
    public static final BlockItem STRIPPED_ORANGE_LOG = add("stripped_orange_log", ParadiseLostBlocks.STRIPPED_ORANGE_LOG, building_block);
    public static final BlockItem STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", ParadiseLostBlocks.STRIPPED_CRYSTAL_LOG, building_block);
    public static final BlockItem STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", ParadiseLostBlocks.STRIPPED_WISTERIA_LOG, building_block);
    // stripped wood
    public static final BlockItem STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", ParadiseLostBlocks.STRIPPED_SKYROOT_WOOD, building_block);
    public static final BlockItem STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", ParadiseLostBlocks.STRIPPED_GOLDEN_OAK_WOOD, building_block);
    public static final BlockItem STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", ParadiseLostBlocks.STRIPPED_ORANGE_WOOD, building_block);
    public static final BlockItem STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", ParadiseLostBlocks.STRIPPED_CRYSTAL_WOOD, building_block);
    public static final BlockItem STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", ParadiseLostBlocks.STRIPPED_WISTERIA_WOOD, building_block);
    // wood
    public static final BlockItem SKYROOT_WOOD = add("skyroot_wood", ParadiseLostBlocks.SKYROOT_WOOD, building_block);
    public static final BlockItem GOLDEN_OAK_WOOD = add("golden_oak_wood", ParadiseLostBlocks.GOLDEN_OAK_WOOD, building_block);
    public static final BlockItem ORANGE_WOOD = add("orange_wood", ParadiseLostBlocks.ORANGE_WOOD, building_block);
    public static final BlockItem CRYSTAL_WOOD = add("crystal_wood", ParadiseLostBlocks.CRYSTAL_WOOD, building_block);
    public static final BlockItem WISTERIA_WOOD = add("wisteria_wood", ParadiseLostBlocks.WISTERIA_WOOD, building_block);
    // glass
    public static final BlockItem QUICKSOIL_GLASS = add("quicksoil_glass", ParadiseLostBlocks.QUICKSOIL_GLASS, building_block);
    // slabs
    public static final BlockItem SKYROOT_SLAB = add("skyroot_slab", ParadiseLostBlocks.SKYROOT_SLAB, building_block);
    public static final BlockItem GOLDEN_OAK_SLAB = add("golden_oak_slab", ParadiseLostBlocks.GOLDEN_OAK_SLAB, building_block);
    public static final BlockItem ORANGE_SLAB = add("orange_slab", ParadiseLostBlocks.ORANGE_SLAB, building_block);
    public static final BlockItem CRYSTAL_SLAB = add("crystal_slab", ParadiseLostBlocks.CRYSTAL_SLAB, building_block);
    public static final BlockItem WISTERIA_SLAB = add("wisteria_slab", ParadiseLostBlocks.WISTERIA_SLAB, building_block);
    // smooth stuff
    // cobble variants
    public static final BlockItem MOSSY_HOLYSTONE = add("mossy_holystone", ParadiseLostBlocks.MOSSY_HOLYSTONE, building_block);
    public static final BlockItem GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", ParadiseLostBlocks.GOLDEN_MOSSY_HOLYSTONE, building_block);

    // bricks
    public static final BlockItem HOLYSTONE_BRICK = add("holystone_brick", ParadiseLostBlocks.HOLYSTONE_BRICK, building_block);
    public static final BlockItem CARVED_STONE = add("carved_stone", ParadiseLostBlocks.CARVED_STONE, building_block);
    public static final BlockItem MOSSY_CARVED_STONE = add("mossy_carved_stone", ParadiseLostBlocks.MOSSY_CARVED_STONE, building_block);
    public static final BlockItem CRACKED_CARVED_STONE = add("cracked_carved_stone", ParadiseLostBlocks.CRACKED_CARVED_STONE, building_block);
    public static final BlockItem GLYPHED_CARVED_STONE = add("glyphed_carved_stone", ParadiseLostBlocks.GLYPHED_CARVED_STONE, building_block);
    public static final BlockItem CARVED_STONE_PANEL = add("carved_stone_panel", ParadiseLostBlocks.CARVED_STONE_PANEL, building_block);
    public static final BlockItem CARVED_STONE_PANEL_LIT = add("carved_stone_panel_lit", ParadiseLostBlocks.CARVED_STONE_PANEL_LIT, building_block);
    public static final BlockItem CARVED_STONE_EYE = add("carved_stone_eye", ParadiseLostBlocks.CARVED_STONE_EYE, building_block);
    public static final BlockItem CARVED_STONE_EYE_LIT = add("carved_stone_eye_lit", ParadiseLostBlocks.CARVED_STONE_EYE_LIT, building_block);

    // stairs
    public static final BlockItem SKYROOT_STAIRS = add("skyroot_stairs", ParadiseLostBlocks.SKYROOT_STAIRS, building_block);
    public static final BlockItem GOLDEN_OAK_STAIRS = add("golden_oak_stairs", ParadiseLostBlocks.GOLDEN_OAK_STAIRS, building_block);
    public static final BlockItem ORANGE_STAIRS = add("orange_stairs", ParadiseLostBlocks.ORANGE_STAIRS, building_block);
    public static final BlockItem CRYSTAL_STAIRS = add("crystal_stairs", ParadiseLostBlocks.CRYSTAL_STAIRS, building_block);
    public static final BlockItem WISTERIA_STAIRS = add("wisteria_stairs", ParadiseLostBlocks.WISTERIA_STAIRS, building_block);
    // stone stairs + slabs
    public static final BlockItem HOLYSTONE_STAIRS = add("holystone_stairs", ParadiseLostBlocks.HOLYSTONE_STAIRS, building_block);
    public static final BlockItem COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", ParadiseLostBlocks.COBBLED_HOLYSTONE_STAIRS, building_block);
    public static final BlockItem MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", ParadiseLostBlocks.MOSSY_HOLYSTONE_STAIRS, building_block);
    public static final BlockItem HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", ParadiseLostBlocks.HOLYSTONE_BRICK_STAIRS, building_block);
    public static final BlockItem CARVED_STAIRS = add("carved_stone_stairs", ParadiseLostBlocks.CARVED_STONE_STAIRS, building_block);
    public static final BlockItem MOSSY_CARVED_STAIRS = add("mossy_carved_stone_stairs", ParadiseLostBlocks.MOSSY_CARVED_STONE_STAIRS, building_block);
    public static final BlockItem HOLYSTONE_SLAB = add("holystone_slab", ParadiseLostBlocks.HOLYSTONE_SLAB, building_block);

    public static final BlockItem COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", ParadiseLostBlocks.COBBLED_HOLYSTONE_SLAB, building_block);
    public static final BlockItem MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", ParadiseLostBlocks.MOSSY_HOLYSTONE_SLAB, building_block);
    public static final BlockItem HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", ParadiseLostBlocks.HOLYSTONE_BRICK_SLAB, building_block);
    public static final BlockItem CARVED_SLAB = add("carved_stone_slab", ParadiseLostBlocks.CARVED_STONE_SLAB, building_block);
    public static final BlockItem MOSSY_CARVED_SLAB = add("mossy_carved_stone_slab", ParadiseLostBlocks.MOSSY_CARVED_STONE_SLAB, building_block);
    // colorfuls

    private static FabricItemSettings decoration() {
        return new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS);
    }

    private static final FabricItemSettings decoration = decoration();
    private static final FabricItemSettings sign = decoration().maxCount(16);
    private static final FabricItemSettings boat = decoration().maxCount(1);
    private static final FabricItemSettings hat = decoration().equipmentSlot(stack -> HEAD);

    // saplings
    public static final BlockItem SKYROOT_SAPLING = add("skyroot_sapling", ParadiseLostBlocks.SKYROOT_SAPLING, decoration, compostable30);
    public static final BlockItem GOLDEN_OAK_SAPLING = add("golden_oak_sapling", ParadiseLostBlocks.GOLDEN_OAK_SAPLING, decoration, compostable30);
    public static final BlockItem ORANGE_SAPLING = add("orange_sapling", ParadiseLostBlocks.ORANGE_SAPLING, decoration, compostable30);
    public static final BlockItem CRYSTAL_SAPLING = add("crystal_sapling", ParadiseLostBlocks.CRYSTAL_SAPLING, decoration, compostable50);
    public static final BlockItem ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", ParadiseLostBlocks.ROSE_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", ParadiseLostBlocks.FROST_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", ParadiseLostBlocks.LAVENDER_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", ParadiseLostBlocks.BOREAL_WISTERIA_SAPLING, decoration, compostable30);
    // leaves
    public static final BlockItem SKYROOT_LEAVES = add("skyroot_leaves", ParadiseLostBlocks.SKYROOT_LEAVES, decoration, compostable30);
    public static final BlockItem GOLDEN_OAK_LEAVES = add("golden_oak_leaves", ParadiseLostBlocks.GOLDEN_OAK_LEAVES, decoration, compostable30);
    public static final BlockItem ORANGE_LEAVES = add("orange_leaves", ParadiseLostBlocks.ORANGE_LEAVES, decoration, compostable30);
    public static final BlockItem CRYSTAL_LEAVES = add("crystal_leaves", ParadiseLostBlocks.CRYSTAL_LEAVES, decoration, compostable50);
    public static final BlockItem ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", ParadiseLostBlocks.ROSE_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", ParadiseLostBlocks.FROST_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", ParadiseLostBlocks.LAVENDER_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", ParadiseLostBlocks.BOREAL_WISTERIA_LEAVES, decoration, compostable30);
    // plants
    public static final BlockItem GRASS = add("grass_plant", ParadiseLostBlocks.GRASS, decoration, compostable30);
    public static final BlockItem GRASS_FLOWERING = add("grass_flowering", ParadiseLostBlocks.GRASS_FLOWERING, decoration, compostable30);
    public static final BlockItem SHORT_GRASS = add("short_grass", ParadiseLostBlocks.SHORT_GRASS, decoration, compostable30);
    public static final BlockItem FERN = add("fern", ParadiseLostBlocks.FERN, decoration, compostable30);
    public static final BlockItem BUSH = add("bush", ParadiseLostBlocks.BUSH, decoration, compostable30);
    public static final BlockItem FLUTEGRASS = add("flutegrass", ParadiseLostBlocks.FLUTEGRASS, decoration, compostable30);
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
    public static final BlockItem BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", ParadiseLostBlocks.BOREAL_WISTERIA_HANGER, decoration, compostable30);

    public static final BlockItem SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", ParadiseLostBlocks.SKYROOT_LEAF_PILE, decoration, compostable30);
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
    public static final BlockItem AMBROSIUM_LANTERN = add("ambrosium_lantern", ParadiseLostBlocks.AMBROSIUM_LANTERN, decoration);
    public static final WallStandingBlockItem AMBROSIUM_TORCH = add("ambrosium_torch", new WallStandingBlockItem(ParadiseLostBlocks.AMBROSIUM_TORCH, ParadiseLostBlocks.AMBROSIUM_TORCH_WALL, decoration));
    // util blocks (enchanter, freezer, etc.)

    public static final BlockItem AMBROSIUM_CAMPFIRE = add("ambrosium_campfire", ParadiseLostBlocks.AMBROSIUM_CAMPFIRE, decoration);

    // door-like things
    public static final BlockItem SKYROOT_DOOR = add("skyroot_door", ParadiseLostBlocks.SKYROOT_DOOR, decoration);
    public static final BlockItem GOLDEN_OAK_DOOR = add("golden_oak_door", ParadiseLostBlocks.GOLDEN_OAK_DOOR, decoration);
    public static final BlockItem ORANGE_DOOR = add("orange_door", ParadiseLostBlocks.ORANGE_DOOR, decoration);
    public static final BlockItem CRYSTAL_DOOR = add("crystal_door", ParadiseLostBlocks.CRYSTAL_DOOR, decoration);
    public static final BlockItem WISTERIA_DOOR = add("wisteria_door", ParadiseLostBlocks.WISTERIA_DOOR, decoration);

    public static final BlockItem SKYROOT_TRAPDOOR = add("skyroot_trapdoor", ParadiseLostBlocks.SKYROOT_TRAPDOOR, decoration);
    public static final BlockItem GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", ParadiseLostBlocks.GOLDEN_OAK_TRAPDOOR, decoration);
    public static final BlockItem ORANGE_TRAPDOOR = add("orange_trapdoor", ParadiseLostBlocks.ORANGE_TRAPDOOR, decoration);
    public static final BlockItem CRYSTAL_TRAPDOOR = add("crystal_trapdoor", ParadiseLostBlocks.CRYSTAL_TRAPDOOR, decoration);
    public static final BlockItem WISTERIA_TRAPDOOR = add("wisteria_trapdoor", ParadiseLostBlocks.WISTERIA_TRAPDOOR, decoration);

    public static final BlockItem SKYROOT_FENCE_GATE = add("skyroot_fence_gate", ParadiseLostBlocks.SKYROOT_FENCE_GATE, decoration);
    public static final BlockItem GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", ParadiseLostBlocks.GOLDEN_OAK_FENCE_GATE, decoration);
    public static final BlockItem ORANGE_FENCE_GATE = add("orange_fence_gate", ParadiseLostBlocks.ORANGE_FENCE_GATE, decoration);
    public static final BlockItem CRYSTAL_FENCE_GATE = add("crystal_fence_gate", ParadiseLostBlocks.CRYSTAL_FENCE_GATE, decoration);
    public static final BlockItem WISTERIA_FENCE_GATE = add("wisteria_fence_gate", ParadiseLostBlocks.WISTERIA_FENCE_GATE, decoration);
    // fences
    public static final BlockItem SKYROOT_FENCE = add("skyroot_fence", ParadiseLostBlocks.SKYROOT_FENCE, decoration);
    public static final BlockItem GOLDEN_OAK_FENCE = add("golden_oak_fence", ParadiseLostBlocks.GOLDEN_OAK_FENCE, decoration);
    public static final BlockItem ORANGE_FENCE = add("orange_fence", ParadiseLostBlocks.ORANGE_FENCE, decoration);
    public static final BlockItem CRYSTAL_FENCE = add("crystal_fence", ParadiseLostBlocks.CRYSTAL_FENCE, decoration);
    public static final BlockItem WISTERIA_FENCE = add("wisteria_fence", ParadiseLostBlocks.WISTERIA_FENCE, decoration);
    // walls
    public static final BlockItem HOLYSTONE_WALL = add("holystone_wall", ParadiseLostBlocks.HOLYSTONE_WALL, decoration);
    public static final BlockItem COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", ParadiseLostBlocks.COBBLED_HOLYSTONE_WALL, decoration);
    public static final BlockItem MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", ParadiseLostBlocks.MOSSY_HOLYSTONE_WALL, decoration);
    public static final BlockItem HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", ParadiseLostBlocks.HOLYSTONE_BRICK_WALL, decoration);
    public static final BlockItem CARVED_WALL = add("carved_stone_wall", ParadiseLostBlocks.CARVED_STONE_WALL, decoration);
    public static final BlockItem MOSSY_CARVED_WALL = add("mossy_carved_stone_wall", ParadiseLostBlocks.MOSSY_CARVED_STONE_WALL, decoration);
    // infested blocks
    /* TODO: Implement swet drop block items
    public static final BlockItem SWET_DROP = add("swet_drop", ParadiseLostBlocks.SWET_DROP, misc);
    public static final BlockItem BLUE_SWET_DROP = add("blue_swet_drop", ParadiseLostBlocks.BLUE_SWET_DROP, misc);
    public static final BlockItem GOLDEN_SWET_DROP = add("golden_swet_drop", ParadiseLostBlocks.GOLDEN_SWET_DROP, misc);
    public static final BlockItem PURPLE_SWET_DROP = add("purple_swet_drop", ParadiseLostBlocks.PURPLE_SWET_DROP, misc);
    */
    // panes & chains
    public static final BlockItem ZANITE_CHAIN = add("zanite_chain", ParadiseLostBlocks.ZANITE_CHAIN, decoration);
    public static final BlockItem QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", ParadiseLostBlocks.QUICKSOIL_GLASS_PANE, decoration);
    // carpets

    // path & farmland
    public static final BlockItem FARMLAND = add("farmland", ParadiseLostBlocks.FARMLAND, decoration);
    public static final BlockItem DIRT_PATH = add("grass_path", ParadiseLostBlocks.DIRT_PATH, decoration);
    // signs, wall stuff.
    public static final SignItem SKYROOT_SIGN = add("skyroot_sign", new SignItem(sign, ParadiseLostBlocks.SKYROOT_SIGN, ParadiseLostBlocks.SKYROOT_WALL_SIGN));
    public static final SignItem GOLDEN_OAK_SIGN = add("golden_oak_sign", new SignItem(sign, ParadiseLostBlocks.GOLDEN_OAK_SIGN, ParadiseLostBlocks.GOLDEN_OAK_WALL_SIGN));
    public static final SignItem ORANGE_SIGN = add("orange_sign", new SignItem(sign, ParadiseLostBlocks.ORANGE_SIGN, ParadiseLostBlocks.ORANGE_WALL_SIGN));
    public static final SignItem CRYSTAL_SIGN = add("crystal_sign", new SignItem(sign, ParadiseLostBlocks.CRYSTAL_SIGN, ParadiseLostBlocks.CRYSTAL_WALL_SIGN));
    public static final SignItem WISTERIA_SIGN = add("wisteria_sign", new SignItem(sign, ParadiseLostBlocks.WISTERIA_SIGN, ParadiseLostBlocks.WISTERIA_WALL_SIGN));
    // beds

    // etc.
    public static final BlockItem SKYROOT_BUTTON = add("skyroot_button", ParadiseLostBlocks.SKYROOT_BUTTON, decoration);
    public static final BlockItem GOLDEN_OAK_BUTTON = add("golden_oak_button", ParadiseLostBlocks.GOLDEN_OAK_BUTTON, decoration);
    public static final BlockItem ORANGE_BUTTON = add("orange_button", ParadiseLostBlocks.ORANGE_BUTTON, decoration);
    public static final BlockItem CRYSTAL_BUTTON = add("crystal_button", ParadiseLostBlocks.CRYSTAL_BUTTON, decoration);
    public static final BlockItem WISTERIA_BUTTON = add("wisteria_button", ParadiseLostBlocks.WISTERIA_BUTTON, decoration);

    public static final BlockItem SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", ParadiseLostBlocks.SKYROOT_PRESSURE_PLATE, decoration);
    public static final BlockItem GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", ParadiseLostBlocks.GOLDEN_OAK_PRESSURE_PLATE, decoration);
    public static final BlockItem ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", ParadiseLostBlocks.ORANGE_PRESSURE_PLATE, decoration);
    public static final BlockItem CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", ParadiseLostBlocks.CRYSTAL_PRESSURE_PLATE, decoration);
    public static final BlockItem WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", ParadiseLostBlocks.WISTERIA_PRESSURE_PLATE, decoration);

    //TODO: Implement dungeon switch block
//    public static final BlockItem DUNGEON_SWITCH = add("dungeonswitch", ParadiseLostBlocks.DUNGEON_SWITCH, decoration);

    // these should be moved... somewhere?
    public static final BoatItem SKYROOT_BOAT = ParadiseLostBlocks.SKYROOT.boatFactory(boat).item;
    public static final BoatItem GOLDEN_OAK_BOAT = ParadiseLostBlocks.GOLDEN_OAK.boatFactory(boat).item;
    public static final BoatItem ORANGE_BOAT = ParadiseLostBlocks.ORANGE.boatFactory(boat).item;
    public static final BoatItem CRYSTAL_BOAT = ParadiseLostBlocks.CRYSTAL.boatFactory(boat).item;
    public static final BoatItem WISTERIA_BOAT = ParadiseLostBlocks.WISTERIA.boatFactory(boat).item;

    // Chests
    public static final BlockItem SKYROOT_CHEST = add("skyroot_chest", ParadiseLostBlocks.SKYROOT_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));
    // TODO: Implement remaining chests (PL-1.7)
    public static final BlockItem GOLDEN_OAK_CHEST = add("golden_oak_chest", ParadiseLostBlocks.GOLDEN_OAK_CHEST, new FabricItemSettings().group(ParadiseLostItemGroups.PARADISE_LOST_DECORATIONS));
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
                (block instanceof DoorBlock ||
                        block instanceof TallPlantBlock ||
                        block instanceof TallFlowerBlock
                ) ?
                        new TallBlockItem(block, settings) :
                        new BlockItem(block, settings),
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
