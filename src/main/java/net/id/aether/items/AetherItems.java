package net.id.aether.items;

import com.google.common.collect.ImmutableList;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.vehicle.AetherBoatTypes;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.items.armor.AetherArmorMaterials;
import net.id.aether.items.food.AetherFoodComponent;
import net.id.aether.items.food.ValkyrieMilkItem;
import net.id.aether.items.food.WhiteAppleItem;
import net.id.aether.items.misc.*;
import net.id.aether.items.resources.AmbrosiumShardItem;
import net.id.aether.items.tools.AetherToolMaterials;
import net.id.aether.items.tools.SkyrootBucketItem;
import net.id.aether.items.tools.VialItem;
import net.id.aether.items.tools.base_tools.*;
import net.id.aether.items.tools.bloodstone.AbstentineBloodstoneItem;
import net.id.aether.items.tools.bloodstone.AmbrosiumBloodstoneItem;
import net.id.aether.items.tools.bloodstone.GravititeBloodstoneItem;
import net.id.aether.items.tools.bloodstone.ZaniteBloodstoneItem;
import net.id.aether.items.utils.AetherRarity;
import net.id.aether.items.weapons.*;
import net.id.aether.registry.AetherRegistryQueues;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

import static net.id.aether.Aether.locate;
import static net.id.aether.items.AetherItemActions.*;
import static net.minecraft.entity.EquipmentSlot.*;
import static net.minecraft.util.Rarity.*;

@SuppressWarnings("unused")
public class AetherItems {
    /*
    Begin items
     */

    private static FabricItemSettings nightmare() {
        return new FabricItemSettings().fireproof();
    }

    private static FabricItemSettings resource() {
        return new FabricItemSettings().group(AetherItemGroups.AETHER_RESOURCES);
    }

    private static final Settings resource = resource();

    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource));
    public static final Item AECHOR_PETAL = add("aechor_petal", new Item(resource), compostable65);
    public static final Item NIGTHMARE_FUEL = add("nightmare_fuel", new LoreItem(nightmare().rarity(UNCOMMON), ImmutableList.of(new TranslatableText("item.the_aether.nightmare_fuel.tooltip").formatted(Formatting.GRAY))));
    public static final Item CROW_EYE = add("crow_eye", new LoreItem(nightmare().maxCount(1).rarity(UNCOMMON), ImmutableList.of(new TranslatableText("item.the_aether.crow_eye.tooltip").formatted(Formatting.GRAY))));
    public static final Item SWET_BALL = add("swet_ball", new Item(resource), swetColor);
    public static final AmbrosiumShardItem AMBROSIUM_SHARD = add("ambrosium_shard", new AmbrosiumShardItem(resource), fuel(500));
    public static final Item ZANITE_GEM = add("zanite_gemstone", new Item(resource));
    public static final Item ZANITE_FRAGMENT = add("zanite_fragment", new Item(resource));
    public static final Item GRAVITITE_GEM = add("gravitite_gemstone", new Item(resource));
    public static final Item FLAX_THREAD = add("flax_thread", new Item(resource));
    public static final Item FLAXWEAVE = add("flaxweave", new Item(resource));
    public static final Item GROUND_SWETROOT = add("ground_swetroot", new Item(resource));
    public static final Item SPROUTING_SWETROOT = add("sprouting_swetroot", new Item(resource));


    private static Settings tool() {
        return new Settings().group(AetherItemGroups.AETHER_TOOLS);
    }

    private static final Settings tool = tool();
    private static final Settings rareTool = tool().rarity(RARE);
    private static final Settings aetherLootTool = tool().rarity(AetherRarity.AETHER_LOOT);
    private static final Settings unstackableTool = tool().maxCount(1);
    private static final Settings unstackableRareTool = tool().maxCount(1).rarity(RARE);
    public static final ZaniteShovelItem ZANITE_SHOVEL = add("zanite_shovel", new ZaniteShovelItem(AetherToolMaterials.ZANITE, 1.5f, -3f, tool));
    public static final ZanitePickaxeItem ZANITE_PICKAXE = add("zanite_pickaxe", new ZanitePickaxeItem(AetherToolMaterials.ZANITE, 1, -2.8f, tool));
    public static final ZaniteAxeItem ZANITE_AXE = add("zanite_axe", new ZaniteAxeItem(AetherToolMaterials.ZANITE, 6f, -3.1f, tool));
    public static final ZaniteSwordItem ZANITE_SWORD = add("zanite_sword", new ZaniteSwordItem(AetherToolMaterials.ZANITE, 3, -2.4f, tool));
    public static final ZaniteHoeItem ZANITE_HOE = add("zanite_hoe", new ZaniteHoeItem(AetherToolMaterials.ZANITE, 1, 3f, tool));

    public static final GravititeShovelItem GRAVITITE_SHOVEL = add("gravitite_shovel", new GravititeShovelItem(AetherToolMaterials.GRAVITITE, 1.5f, -3f, rareTool));
    public static final GravititePickaxeItem GRAVITITE_PICKAXE = add("gravitite_pickaxe", new GravititePickaxeItem(AetherToolMaterials.GRAVITITE, 1, -2.8f, rareTool));
    public static final GravititeAxeItem GRAVITITE_AXE = add("gravitite_axe", new GravititeAxeItem(AetherToolMaterials.GRAVITITE, 5f, -3f, rareTool));
    public static final SwordItem GRAVITITE_SWORD = add("gravitite_sword", new SwordItem(AetherToolMaterials.GRAVITITE, 3, -2.4f, rareTool));
    public static final GravititeHoeItem GRAVITITE_HOE = add("gravitite_hoe", new GravititeHoeItem(AetherToolMaterials.GRAVITITE, 1, 4f, rareTool));

    public static final ShovelItem VALKYRIE_SHOVEL = add("valkyrie_shovel", new ShovelItem(AetherToolMaterials.VALKYRIE, 1.5f, -3f, aetherLootTool));
    public static final AetherPickaxeItem VALKYRIE_PICKAXE = add("valkyrie_pickaxe", new AetherPickaxeItem(AetherToolMaterials.VALKYRIE, 1, -2.8f, aetherLootTool));
    public static final AetherAxeItem VALKYRIE_AXE = add("valkyrie_axe", new AetherAxeItem(AetherToolMaterials.VALKYRIE, 4f, -2.9f, aetherLootTool));
    public static final ValkyrieLanceItem VALKYRIE_LANCE = add("valkyrie_lance", new ValkyrieLanceItem(AetherToolMaterials.VALKYRIE, 10, -3f, 6f, 4f, aetherLootTool));
    public static final AetherHoeItem VALKYRIE_HOE = add("valkyrie_hoe", new AetherHoeItem(AetherToolMaterials.VALKYRIE, 1, 5f, aetherLootTool));

    public static final DartItem GOLDEN_DART = add("golden_dart", new DartItem(tool));
    public static final DartItem ENCHANTED_DART = add("enchanted_dart", new DartItem(rareTool));
    public static final DartItem POISON_DART = add("poison_dart", new DartItem(tool));
    public static final DartShooterItem GOLDEN_DART_SHOOTER = add("golden_dart_shooter", new DartShooterItem(GOLDEN_DART, unstackableTool));
    public static final DartShooterItem ENCHANTED_DART_SHOOTER = add("enchanted_dart_shooter", new DartShooterItem(ENCHANTED_DART, unstackableRareTool));
    public static final DartShooterItem POISON_DART_SHOOTER = add("poison_dart_shooter", new DartShooterItem(POISON_DART, unstackableTool));

    public static final BowItem PHOENIX_BOW = add("phoenix_bow", new BowItem(tool().maxDamage(384)));
    public static final FlamingSwordItem FLAMING_SWORD = add("flaming_sword", new FlamingSwordItem(AetherToolMaterials.LEGENDARY, 4, -2.4f, aetherLootTool));
    public static final LightningSwordItem LIGHTNING_SWORD = add("lightning_sword", new LightningSwordItem(AetherToolMaterials.LEGENDARY, 4, -2.4f, aetherLootTool));
    public static final HolySwordItem HOLY_SWORD = add("holy_sword", new HolySwordItem(AetherToolMaterials.LEGENDARY, 4, -2.4f, aetherLootTool));
    public static final VampireBladeItem VAMPIRE_BLADE = add("vampire_blade", new VampireBladeItem(AetherToolMaterials.LEGENDARY, 3, -2.4f, aetherLootTool));
    public static final PigSlayerItem PIG_SLAYER = add("pig_slayer", new PigSlayerItem(AetherToolMaterials.LEGENDARY, 3, -2.4f, aetherLootTool));
    public static final CandyCaneSwordItem CANDY_CANE_SWORD = add("candy_cane_sword", new CandyCaneSwordItem(AetherToolMaterials.CANDY, 3, -2f, aetherLootTool));

    public static final TrinketItem CLOUD_PARACHUTE = add("cold_parachute", new TrinketItem(unstackableTool));
    public static final TrinketItem GOLDEN_CLOUD_PARACHUTE = add("golden_parachute", new TrinketItem(tool().maxCount(1).maxDamage(20)));

    public static final AmbrosiumBloodstoneItem AMBROSIUM_BLOODSTONE = add("ambrosium_bloodstone", new AmbrosiumBloodstoneItem(unstackableTool));
    public static final ZaniteBloodstoneItem ZANITE_BLOODSTONE = add("zanite_bloodstone", new ZaniteBloodstoneItem(unstackableTool));
    public static final GravititeBloodstoneItem GRAVITITE_BLOODSTONE = add("gravitite_bloodstone", new GravititeBloodstoneItem(unstackableTool));
    public static final AbstentineBloodstoneItem ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(unstackableTool));

    private static Settings wearable() {
        return new Settings().group(AetherItemGroups.AETHER_WEARABLES);
    }

    private static final Settings wearable = wearable();
    private static final Settings rareWearable = wearable().rarity(RARE);
    private static final Settings aetherLootWearable = wearable().rarity(AetherRarity.AETHER_LOOT);
    public static final ArmorItem ZANITE_HELMET = add("zanite_helmet", new ArmorItem(AetherArmorMaterials.ZANITE, HEAD, wearable));
    public static final ArmorItem ZANITE_CHESTPLATE = add("zanite_chestplate", new ArmorItem(AetherArmorMaterials.ZANITE, CHEST, wearable));
    public static final ArmorItem ZANITE_LEGGINGS = add("zanite_leggings", new ArmorItem(AetherArmorMaterials.ZANITE, LEGS, wearable));
    public static final ArmorItem ZANITE_BOOTS = add("zanite_boots", new ArmorItem(AetherArmorMaterials.ZANITE, FEET, wearable));

    public static final ArmorItem GRAVITITE_HELMET = add("gravitite_helmet", new ArmorItem(AetherArmorMaterials.GRAVITITE, HEAD, rareWearable));
    public static final ArmorItem GRAVITITE_CHESTPLATE = add("gravitite_chestplate", new ArmorItem(AetherArmorMaterials.GRAVITITE, CHEST, rareWearable));
    public static final ArmorItem GRAVITITE_LEGGINGS = add("gravitite_leggings", new ArmorItem(AetherArmorMaterials.GRAVITITE, LEGS, rareWearable));
    public static final ArmorItem GRAVITITE_BOOTS = add("gravitite_boots", new ArmorItem(AetherArmorMaterials.GRAVITITE, FEET, rareWearable));

    public static final ArmorItem NEPTUNE_HELMET = add("neptune_helmet", new ArmorItem(AetherArmorMaterials.NEPTUNE, HEAD, aetherLootWearable));
    public static final ArmorItem NEPTUNE_CHESTPLATE = add("neptune_chestplate", new ArmorItem(AetherArmorMaterials.NEPTUNE, CHEST, aetherLootWearable));
    public static final ArmorItem NEPTUNE_LEGGINGS = add("neptune_leggings", new ArmorItem(AetherArmorMaterials.NEPTUNE, LEGS, aetherLootWearable));
    public static final ArmorItem NEPTUNE_BOOTS = add("neptune_boots", new ArmorItem(AetherArmorMaterials.NEPTUNE, FEET, aetherLootWearable));

    public static final ArmorItem PHOENIX_HELMET = add("phoenix_helmet", new ArmorItem(AetherArmorMaterials.PHOENIX, HEAD, aetherLootWearable));
    public static final ArmorItem PHOENIX_CHESTPLATE = add("phoenix_chestplate", new ArmorItem(AetherArmorMaterials.PHOENIX, CHEST, aetherLootWearable));
    public static final ArmorItem PHOENIX_LEGGINGS = add("phoenix_leggings", new ArmorItem(AetherArmorMaterials.PHOENIX, LEGS, aetherLootWearable));
    public static final ArmorItem PHOENIX_BOOTS = add("phoenix_boots", new ArmorItem(AetherArmorMaterials.PHOENIX, FEET, aetherLootWearable));
    /* TODO: Implement obsidian armor
    public static final ArmorItem OBSIDIAN_HELMET = add("obsidian_helmet", new ArmorItem(AetherArmorMaterials.OBSIDIAN, HEAD, aetherLootWearable));
    public static final ArmorItem OBSIDIAN_CHESTPLATE = add("obsidian_chestplate", new ArmorItem(AetherArmorMaterials.OBSIDIAN, CHEST, aetherLootWearable));
    public static final ArmorItem OBSIDIAN_LEGGINGS = add("obsidian_leggings", new ArmorItem(AetherArmorMaterials.OBSIDIAN, LEGS, aetherLootWearable));
    public static final ArmorItem OBSIDIAN_BOOTS = add("obsidian_boots", new ArmorItem(AetherArmorMaterials.OBSIDIAN, FEET, aetherLootWearable));
    */
    /* TODO: Implement valkyrie armor
    public static final ArmorItem VALKYRIE_HELMET = add("valkyrie_helmet", new ArmorItem(AetherArmorMaterials.VALKYRIE, HEAD, aetherLootWearable));
    public static final ArmorItem VALKYRIE_CHESTPLATE = add("valkyrie_chestplate", new ArmorItem(AetherArmorMaterials.VALKYRIE, CHEST, aetherLootWearable));
    public static final ArmorItem VALKYRIE_LEGGINGS = add("valkyrie_leggings", new ArmorItem(AetherArmorMaterials.VALKYRIE, LEGS, aetherLootWearable));
    public static final ArmorItem VALKYRIE_BOOTS = add("valkyrie_boots", new ArmorItem(AetherArmorMaterials.VALKYRIE, FEET, aetherLootWearable));
    */
    /* TODO: Implement sentry boots
    public static final ArmorItem SENTRY_BOOTS = add("sentry_boots", new ArmorItem(AetherArmorMaterials.SENTRY, FEET, aetherLootWearable));

    // TODO: Implement Gloves
    public static final AccessoryItem LEATHER_GLOVES = add("leather_gloves", new AccessoryItem(AccessoryType.GLOVES, 1.5f, wearable));
    public static final AccessoryItem IRON_GLOVES = add("iron_gloves", new AccessoryItem(AccessoryType.GLOVES, 2.5f, wearable));
    public static final AccessoryItem GOLDEN_GLOVES = add("golden_gloves", new AccessoryItem(AccessoryType.GLOVES, 2f, wearable));
    public static final AccessoryItem CHAIN_GLOVES = add("chain_gloves", new AccessoryItem(AccessoryType.GLOVES, "chain", 2f, wearable));
    public static final AccessoryItem DIAMOND_GLOVES = add("diamond_gloves", new AccessoryItem(AccessoryType.GLOVES, 4.5f, wearable));
    public static final AccessoryItem ZANITE_GLOVES = add("zanite_gloves", new AccessoryItem(AccessoryType.GLOVES, 3.0f, wearable));
    public static final AccessoryItem GRAVITITE_GLOVES = add("gravitite_gloves", new AccessoryItem(AccessoryType.GLOVES, 4f, rareWearable));
    public static final AccessoryItem NEPTUNE_GLOVES = add("neptune_gloves", new AccessoryItem(AccessoryType.GLOVES, 4.5f, aetherLootWearable));
    public static final AccessoryItem PHOENIX_GLOVES = add("phoenix_gloves", new AccessoryItem(AccessoryType.GLOVES, "phoenix", 4f, aetherLootWearable));
    public static final AccessoryItem OBSIDIAN_GLOVES = add("obsidian_gloves", new AccessoryItem(AccessoryType.GLOVES, 5f, aetherLootWearable));
    public static final AccessoryItem VALKYRIE_GLOVES = add("valkyrie_gloves", new AccessoryItem(AccessoryType.GLOVES, "valkyrie", 5f, aetherLootWearable));

    // TODO: Implement Rings
    public static final AccessoryItem IRON_RING = add("iron_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final AccessoryItem GOLDEN_RING = add("golden_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final AccessoryItem ZANITE_RING = add("zanite_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final AccessoryItem ICE_RING = add("ice_ring", new AccessoryItem(AccessoryType.RING, rareWearable));

    // TODO: Implement Pendants
    public static final AccessoryItem GOLDEN_PENDANT = add("golden_pendant", new AccessoryItem(AccessoryType.PENDANT, wearable));
    public static final AccessoryItem ZANITE_PENDANT = add("zanite_pendant", new AccessoryItem(AccessoryType.PENDANT, wearable));
    public static final AccessoryItem ICE_PENDANT = add("ice_pendant", new AccessoryItem(AccessoryType.PENDANT, rareWearable));

    // TODO: Implement Capes
    public static final AccessoryItem WHITE_CAPE = add("white_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem RED_CAPE = add("red_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem BLUE_CAPE = add("blue_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem YELLOW_CAPE = add("yellow_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem SWET_CAPE = add("swet_cape", new AccessoryItem(AccessoryType.CAPE, "swet", aetherLootWearable));
    public static final AccessoryItem AGILITY_CAPE = add("agility_cape", new AccessoryItem(AccessoryType.CAPE, "agility", aetherLootWearable));
    public static final AccessoryItem INVISIBILITY_CAPE = add("invisibility_cape", new AccessoryItem(AccessoryType.CAPE, aetherLootWearable));

    // TODO: Implement misc. accessories
    public static final AccessoryItem GOLDEN_FEATHER = add("golden_feather", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    public static final AccessoryItem REGENERATION_STONE = add("regeneration_stone", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    public static final AccessoryItem IRON_BUBBLE = add("iron_bubble", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    */

    private static Settings food() {
        return new Settings().group(AetherItemGroups.AETHER_FOOD);
    }

    private static Settings food(FoodComponent foodComponent) {
        return new Settings().group(AetherItemGroups.AETHER_FOOD).food(foodComponent);
    }

    private static Settings food(FoodComponent foodComponent, Rarity rarity) {
        return food(foodComponent).rarity(rarity);
    }

    public static final AliasedBlockItem BLUEBERRY = add("blue_berry", new AliasedBlockItem(AetherBlocks.BLUEBERRY_BUSH, food(AetherFoodComponent.BLUEBERRY)), compostable30);
    public static final Item ENCHANTED_BLUEBERRY = add("enchanted_blueberry", new Item(food(AetherFoodComponent.ENCHANTED_BLUEBERRY, RARE)), compostable50);
    public static final Item ORANGE = add("orange", new Item(food(AetherFoodComponent.ORANGE)), compostable65);
    public static final WhiteAppleItem WHITE_APPLE = add("white_apple", new WhiteAppleItem(food(AetherFoodComponent.WHITE_APPLE)), compostable(0f));
    public static final AliasedBlockItem AMADRYS_BUSHEL = add("amadrys_bushel", new AliasedBlockItem(AetherBlocks.AMADRYS, food(AetherFoodComponent.GENERIC_WORSE)), compostable30);
    public static final AliasedBlockItem SWETROOT = add("swetroot", new AliasedBlockItem(AetherBlocks.SWETROOT, food(AetherFoodComponent.GENERIC)), compostable30);
    public static final AliasedBlockItem FLAXSEED = add("flaxseed", new AliasedBlockItem(AetherBlocks.FLAX, food()), compostable30);
    public static final Item BLUE_GUMMY_SWET = add("blue_gummy_swet", new Item(food(AetherFoodComponent.GUMMY_SWET, AetherRarity.AETHER_LOOT)));
    public static final Item GOLDEN_GUMMY_SWET = add("golden_gummy_swet", new Item(food(AetherFoodComponent.GUMMY_SWET, AetherRarity.AETHER_LOOT)));
    public static final ValkyrieMilkItem VALKYRIE_MILK = add("valkyrie_milk", new ValkyrieMilkItem(food(AetherFoodComponent.VALKYRIE_MILK, EPIC).maxCount(1)));
    public static final Item CANDY_CANE = add("candy_cane", new Item(food(AetherFoodComponent.GENERIC)), compostable30);
    public static final Item GINGERBREAD_MAN = add("ginger_bread_man", new Item(food(AetherFoodComponent.GENERIC)), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", new Item(food(AetherFoodComponent.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", new Item(food(AetherFoodComponent.COOKED_MOA_MEAT)));

    private static Settings misc() {
        return new Settings().group(AetherItemGroups.AETHER_MISC);
    }

    private static final Settings misc = misc();

    public static final AetherPortalItem AETHER_PORTAL = add("aether_portal", new AetherPortalItem(misc));
    /* TODO: Implement magical items
    public static final LifeShardItem LIFE_SHARD = add("life_shard", new LifeShardItem(misc().rarity(AetherRarity.AETHER_LOOT).maxCount(1)));
    public static final CloudStaffItem CLOUD_STAFF = add("cloud_staff", new CloudStaffItem(misc().maxCount(1).maxDamage(60)));
    public static final NatureStaffItem NATURE_STAFF = add("nature_staff", new NatureStaffItem(misc().maxCount(1).maxDamage(100)));
     */
    public static final BookOfLoreItem LORE_BOOK = add("lore_book", new BookOfLoreItem(misc().maxCount(1)));
    public static final MoaEggItem MOA_EGG = add("moa_egg", new MoaEggItem(misc().maxCount(1)));
    public static final BlockItem INCUBATOR = add("incubator", AetherBlocks.INCUBATOR, misc, fuel(300));
    public static final BlockItem FOOD_BOWL = add("food_bowl", AetherBlocks.FOOD_BOWL, misc, fuel(300));

    public static final SkyrootBucketItem SKYROOT_BUCKET = add("skyroot_bucket", new SkyrootBucketItem(misc().maxCount(16)), fuel(200), emptyBucketBehavior);

    private static final Settings skyrootBucket = misc().maxCount(1).recipeRemainder(SKYROOT_BUCKET);
    public static final SkyrootBucketItem SKYROOT_WATER_BUCKET = add("skyroot_water_bucket", new SkyrootBucketItem(Fluids.WATER, skyrootBucket), emptiableBucketBehavior);
    public static final SkyrootBucketItem SKYROOT_MILK_BUCKET = add("skyroot_milk_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_POISON_BUCKET = add("skyroot_poison_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_REMEDY_BUCKET = add("skyroot_remedy_bucket", new SkyrootBucketItem(skyrootBucket));

    public static final HealingStoneItem HEALING_STONE = add("healing_stone", new HealingStoneItem(misc.rarity(RARE)));

    public static final VialItem QUICKSOIL_VIAL = add("quicksoil_vial", new VialItem(Fluids.EMPTY, misc().maxCount(32)));
    public static final VialItem AERCLOUD_VIAL = add("aercloud_vial", new VialItem(AetherFluids.DENSE_AERCLOUD, misc().maxCount(32)));
    /* TODO: Implement dungeon keys
    public static final Item BRONZE_KEY = add("bronze_key", new Item(misc().rarity(AetherRarity.AETHER_LOOT)));
    public static final Item SILVER_KEY = add("silver_key", new Item(misc().rarity(AetherRarity.AETHER_LOOT)));
    public static final Item GOLDEN_KEY = add("golden_key", new Item(misc().rarity(AetherRarity.AETHER_LOOT)));
    */

    public static final SpawnEggItem AECHOR_PLANT_SPAWN_EGG = add("aechor_plant_spawn_egg", new SpawnEggItem(AetherEntityTypes.AECHOR_PLANT, 0x97DED4, 0x31897D, misc), spawnEggBehavior);
    //    public static final SpawnEggItem CHEST_MIMIC_SPAWN_EGG = null;
    public static final SpawnEggItem COCKATRICE_SPAWN_EGG = add("cockatrice_spawn_egg", new SpawnEggItem(AetherEntityTypes.COCKATRICE, 0x9FC3F7, 0x3D2338, misc), spawnEggBehavior);
    public static final SpawnEggItem AERBUNNY_SPAWN_EGG = add("aerbunny_spawn_egg", new SpawnEggItem(AetherEntityTypes.AERBUNNY, 0xC5D6ED, 0x82A6D9, misc), spawnEggBehavior);
    public static final SpawnEggItem AERWHALE_SPAWN_EGG = add("aerwhale_spawn_egg", new SpawnEggItem(AetherEntityTypes.AERWHALE, 0x5C6D91, 0xDEDBCE, misc), spawnEggBehavior);
    public static final SpawnEggItem MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(AetherEntityTypes.MOA, 0xC55C2E4, 0xB3A8BB, misc), spawnEggBehavior);
    public static final SpawnEggItem SWET_SPAWN_EGG = add("swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.WHITE_SWET, 0x717375, 0xffffff, misc), spawnEggBehavior);
    public static final SpawnEggItem BLUE_SWET_SPAWN_EGG = add("blue_swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.BLUE_SWET, 0x46699E, 0xf2f2f2, misc), spawnEggBehavior);
    public static final SpawnEggItem PURPLE_SWET_SPAWN_EGG = add("purple_swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.PURPLE_SWET, 0x5D548C, 0xf2f2f2, misc), spawnEggBehavior);
    public static final SpawnEggItem GOLDEN_SWET_SPAWN_EGG = add("golden_swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.GOLDEN_SWET, 0xC99D36, 0xf2f2f2, misc), spawnEggBehavior);

    private static FabricItemSettings building_block() {
        return new FabricItemSettings().group(AetherItemGroups.AETHER_BUILDING_BLOCKS);
    }
    private static final FabricItemSettings building_block = building_block();

    // stone
    public static final BlockItem HOLYSTONE = add("holystone", AetherBlocks.HOLYSTONE, building_block);
    public static final BlockItem COBBLED_HOLYSTONE = add("cobbled_holystone", AetherBlocks.COBBLED_HOLYSTONE, building_block);
    public static final BlockItem ICESTONE = add("icestone", AetherBlocks.ICESTONE, building_block);
    public static final BlockItem AEROGEL = add("aerogel", AetherBlocks.AEROGEL, building_block);
    // nature
    public static final BlockItem AETHER_GRASS_BLOCK = add("aether_grass", AetherBlocks.AETHER_GRASS_BLOCK, building_block);
    public static final BlockItem AETHER_ENCHANTED_GRASS = add("enchanted_aether_grass", AetherBlocks.AETHER_ENCHANTED_GRASS, building_block);
    public static final BlockItem AETHER_FROZEN_GRASS = add("aether_frozen_grass", AetherBlocks.AETHER_FROZEN_GRASS, building_block);
    public static final BlockItem AETHER_DIRT = add("aether_dirt", AetherBlocks.AETHER_DIRT, building_block);
    public static final BlockItem COARSE_AETHER_DIRT = add("coarse_aether_dirt", AetherBlocks.COARSE_AETHER_DIRT, building_block);
    public static final BlockItem PERMAFROST = add("permafrost", AetherBlocks.PERMAFROST, building_block);
    public static final BlockItem QUICKSOIL = add("quicksoil", AetherBlocks.QUICKSOIL, building_block);
    //Aerclouds
    public static final BlockItem COLD_AERCLOUD = add("cold_aercloud", AetherBlocks.COLD_AERCLOUD, building_block);
    public static final BlockItem STORM_AERCLOUD = add("storm_aercloud", AetherBlocks.STORM_AERCLOUD, building_block);
    public static final BlockItem TEAL_AERCLOUD = add("teal_aercloud", AetherBlocks.TEAL_AERCLOUD, building_block);
    public static final BlockItem BLUE_AERCLOUD = add("blue_aercloud", AetherBlocks.BLUE_AERCLOUD, building_block);
    public static final BlockItem PINK_AERCLOUD = add("pink_aercloud", AetherBlocks.PINK_AERCLOUD, building_block);
    public static final BlockItem GOLDEN_AERCLOUD = add("golden_aercloud", AetherBlocks.GOLDEN_AERCLOUD, building_block);
    public static final BlockItem PURPLE_AERCLOUD = add("purple_aercloud", AetherBlocks.PURPLE_AERCLOUD, building_block);
    public static final BlockItem GREEN_AERCLOUD = add("green_aercloud", AetherBlocks.GREEN_AERCLOUD, building_block);
    public static final BlockItem IRRADIATED_AERCLOUD = add("irradiated_aercloud", AetherBlocks.IRRADIATED_AERCLOUD, building_block);
    public static final BlockItem BLAZING_AERCLOUD = add("blazing_aercloud", AetherBlocks.BLAZING_AERCLOUD, building_block);
    // planks
    public static final BlockItem SKYROOT_PLANKS = add("skyroot_planks", AetherBlocks.SKYROOT_PLANKS, building_block);
    public static final BlockItem GOLDEN_OAK_PLANKS = add("golden_oak_planks", AetherBlocks.GOLDEN_OAK_PLANKS, building_block);
    public static final BlockItem ORANGE_PLANKS = add("orange_planks", AetherBlocks.ORANGE_PLANKS, building_block);
    public static final BlockItem CRYSTAL_PLANKS = add("crystal_planks", AetherBlocks.CRYSTAL_PLANKS, building_block);
    public static final BlockItem WISTERIA_PLANKS = add("wisteria_planks", AetherBlocks.WISTERIA_PLANKS, building_block);
    public static final BlockItem SKYROOT_BOOKSHELF = add("skyroot_bookshelf", AetherBlocks.SKYROOT_BOOKSHELF, building_block);
    // ores
    public static final BlockItem AMBROSIUM_ORE = add("ambrosium_ore", AetherBlocks.AMBROSIUM_ORE, building_block);
    public static final BlockItem ZANITE_ORE = add("zanite_ore", AetherBlocks.ZANITE_ORE, building_block);
    public static final BlockItem GRAVITITE_ORE = add("gravitite_ore", AetherBlocks.GRAVITITE_ORE, building_block);
    // ore blocks
    public static final BlockItem ZANITE_BLOCK = add("zanite_block", AetherBlocks.ZANITE_BLOCK, building_block);
    public static final BlockItem BLOCK_OF_GRAVITITE = add("block_of_gravitite", AetherBlocks.BLOCK_OF_GRAVITITE, building_block);
    // move this somewhere else
    public static final BlockItem GRAVITITE_LEVITATOR = add("gravitite_levitator", AetherBlocks.GRAVITITE_LEVITATOR, building_block);
    // logs
    public static final BlockItem SKYROOT_LOG = add("skyroot_log", AetherBlocks.SKYROOT_LOG, building_block);
    public static final BlockItem MOTTLED_SKYROOT_LOG = add("mottled_skyroot_log", AetherBlocks.MOTTLED_SKYROOT_LOG, building_block);
    public static final BlockItem MOTTLED_SKYROOT_FALLEN_LOG = add("mottled_skyroot_fallen_log", AetherBlocks.MOTTLED_SKYROOT_FALLEN_LOG, building_block);
    public static final BlockItem GOLDEN_OAK_LOG = add("golden_oak_log", AetherBlocks.GOLDEN_OAK_LOG, building_block);
    public static final BlockItem ORANGE_LOG = add("orange_log", AetherBlocks.ORANGE_LOG, building_block);
    public static final BlockItem CRYSTAL_LOG = add("crystal_log", AetherBlocks.CRYSTAL_LOG, building_block);
    public static final BlockItem WISTERIA_LOG = add("wisteria_log", AetherBlocks.WISTERIA_LOG, building_block);
    // stripped logs
    public static final BlockItem STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", AetherBlocks.STRIPPED_SKYROOT_LOG, building_block);
    public static final BlockItem STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", AetherBlocks.STRIPPED_GOLDEN_OAK_LOG, building_block);
    public static final BlockItem STRIPPED_ORANGE_LOG = add("stripped_orange_log", AetherBlocks.STRIPPED_ORANGE_LOG, building_block);
    public static final BlockItem STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", AetherBlocks.STRIPPED_CRYSTAL_LOG, building_block);
    public static final BlockItem STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", AetherBlocks.STRIPPED_WISTERIA_LOG, building_block);
    // stripped wood
    public static final BlockItem STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", AetherBlocks.STRIPPED_SKYROOT_WOOD, building_block);
    public static final BlockItem STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", AetherBlocks.STRIPPED_GOLDEN_OAK_WOOD, building_block);
    public static final BlockItem STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", AetherBlocks.STRIPPED_ORANGE_WOOD, building_block);
    public static final BlockItem STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", AetherBlocks.STRIPPED_CRYSTAL_WOOD, building_block);
    public static final BlockItem STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", AetherBlocks.STRIPPED_WISTERIA_WOOD, building_block);
    // wood
    public static final BlockItem SKYROOT_WOOD = add("skyroot_wood", AetherBlocks.SKYROOT_WOOD, building_block);
    public static final BlockItem GOLDEN_OAK_WOOD = add("golden_oak_wood", AetherBlocks.GOLDEN_OAK_WOOD, building_block);
    public static final BlockItem ORANGE_WOOD = add("orange_wood", AetherBlocks.ORANGE_WOOD, building_block);
    public static final BlockItem CRYSTAL_WOOD = add("crystal_wood", AetherBlocks.CRYSTAL_WOOD, building_block);
    public static final BlockItem WISTERIA_WOOD = add("wisteria_wood", AetherBlocks.WISTERIA_WOOD, building_block);
    // glass
    public static final BlockItem QUICKSOIL_GLASS = add("quicksoil_glass", AetherBlocks.QUICKSOIL_GLASS, building_block);
    // slabs
    public static final BlockItem SKYROOT_SLAB = add("skyroot_slab", AetherBlocks.SKYROOT_SLAB, building_block);
    public static final BlockItem GOLDEN_OAK_SLAB = add("golden_oak_slab", AetherBlocks.GOLDEN_OAK_SLAB, building_block);
    public static final BlockItem ORANGE_SLAB = add("orange_slab", AetherBlocks.ORANGE_SLAB, building_block);
    public static final BlockItem CRYSTAL_SLAB = add("crystal_slab", AetherBlocks.CRYSTAL_SLAB, building_block);
    public static final BlockItem WISTERIA_SLAB = add("wisteria_slab", AetherBlocks.WISTERIA_SLAB, building_block);
    // smooth stuff
    // cobble variants
    public static final BlockItem MOSSY_HOLYSTONE = add("mossy_holystone", AetherBlocks.MOSSY_HOLYSTONE, building_block);
    public static final BlockItem GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", AetherBlocks.GOLDEN_MOSSY_HOLYSTONE, building_block);
    /* TODO: Implement Angelic and hellfire blocks
    public static final BlockItem ANGELIC_STONE = add("angelic_stone", AetherBlocks.ANGELIC_STONE, block);
    public static final BlockItem ANGELIC_CRACKED_STONE = add("angelic_stone_cracked", AetherBlocks.ANGELIC_CRACKED_STONE, block);
    public static final BlockItem ANGELIC_SLAB = add("angelic_slab", AetherBlocks.ANGELIC_SLAB, block);
    public static final BlockItem ANGELIC_STAIRS = add("angelic_stairs", AetherBlocks.ANGELIC_STAIRS, block);
    public static final BlockItem ANGELIC_WALL = add("angelic_wall", AetherBlocks.ANGELIC_WALL, block);

    public static final BlockItem LIGHT_ANGELIC_STONE = add("light_angelic_stone", AetherBlocks.LIGHT_ANGELIC_STONE, block);
    public static final BlockItem LIGHT_ANGELIC_STONE_TRAP = add("light_angelic_stone_trap", AetherBlocks.LIGHT_ANGELIC_STONE_TRAP, block);
    public static final BlockItem LIGHT_ANGELIC_SLAB = add("light_angelic_slab", AetherBlocks.LIGHT_ANGELIC_SLAB, block);
    public static final BlockItem LIGHT_ANGELIC_STAIRS = add("light_angelic_stairs", AetherBlocks.LIGHT_ANGELIC_STAIRS, block);
    public static final BlockItem LIGHT_ANGELIC_WALL = add("light_angelic_wall", AetherBlocks.LIGHT_ANGELIC_WALL, block);

    public static final BlockItem HELLFIRE_STONE = add("hellfire_stone", AetherBlocks.HELLFIRE_STONE, block);
    public static final BlockItem HELLFIRE_CRACKED_STONE = add("hellfire_stone_cracked", AetherBlocks.HELLFIRE_CRACKED_STONE, block);
    public static final BlockItem HELLFIRE_STONE_TRAP = add("hellfire_stone_trap", AetherBlocks.HELLFIRE_STONE_TRAP, block);
    public static final BlockItem HELLFIRE_SLAB = add("hellfire_slab", AetherBlocks.HELLFIRE_SLAB, block);
    public static final BlockItem HELLFIRE_STAIRS = add("hellfire_stairs", AetherBlocks.HELLFIRE_STAIRS, block);
    public static final BlockItem HELLFIRE_WALL = add("hellfire_wall", AetherBlocks.HELLFIRE_WALL, block);

    public static final BlockItem LIGHT_HELLFIRE_STONE = add("light_hellfire_stone", AetherBlocks.LIGHT_HELLFIRE_STONE, block);
    public static final BlockItem LIGHT_HELLFIRE_STONE_TRAP = add("light_hellfire_stone_trap", AetherBlocks.LIGHT_HELLFIRE_STONE_TRAP, block);
    public static final BlockItem LIGHT_HELLFIRE_SLAB = add("light_hellfire_slab", AetherBlocks.LIGHT_HELLFIRE_SLAB, block);
    public static final BlockItem LIGHT_HELLFIRE_STAIRS = add("light_hellfire_stairs", AetherBlocks.LIGHT_HELLFIRE_STAIRS, block);
    public static final BlockItem LIGHT_HELLFIRE_WALL = add("light_hellfire_wall", AetherBlocks.LIGHT_HELLFIRE_WALL, block);
    */
    // bricks
    public static final BlockItem HOLYSTONE_BRICK = add("holystone_brick", AetherBlocks.HOLYSTONE_BRICK, building_block);
    public static final BlockItem CARVED_STONE = add("carved_stone", AetherBlocks.CARVED_STONE, building_block);
    public static final BlockItem MOSSY_CARVED_STONE = add("mossy_carved_stone", AetherBlocks.MOSSY_CARVED_STONE, building_block);
    public static final BlockItem CRACKED_CARVED_STONE = add("cracked_carved_stone", AetherBlocks.CRACKED_CARVED_STONE, building_block);
    public static final BlockItem GLYPHED_CARVED_STONE = add("glyphed_carved_stone", AetherBlocks.GLYPHED_CARVED_STONE, building_block);
    public static final BlockItem CARVED_STONE_PANEL = add("carved_stone_panel", AetherBlocks.CARVED_STONE_PANEL, building_block);
    public static final BlockItem CARVED_STONE_PANEL_LIT = add("carved_stone_panel_lit", AetherBlocks.CARVED_STONE_PANEL_LIT, building_block);
    public static final BlockItem CARVED_STONE_EYE = add("carved_stone_eye", AetherBlocks.CARVED_STONE_EYE, building_block);
    public static final BlockItem CARVED_STONE_EYE_LIT = add("carved_stone_eye_lit", AetherBlocks.CARVED_STONE_EYE_LIT, building_block);
    /* TODO: Implement carved and sentry blocks
    public static final BlockItem LIGHT_CARVED_STONE = add("light_carved_stone", AetherBlocks.LIGHT_CARVED_STONE, block);
    public static final BlockItem LIGHT_CARVED_STONE_TRAP = add("light_carved_stone_trap", AetherBlocks.LIGHT_CARVED_STONE_TRAP, block);
    public static final BlockItem LIGHT_CARVED_SLAB = add("light_carved_slab", AetherBlocks.LIGHT_CARVED_SLAB, block);
    public static final BlockItem LIGHT_CARVED_STAIRS = add("light_carved_stairs", AetherBlocks.LIGHT_CARVED_STAIRS, block);
    public static final BlockItem LIGHT_CARVED_WALL = add("light_carved_wall", AetherBlocks.LIGHT_CARVED_WALL, block);

    public static final BlockItem SENTRY_STONE = add("sentry_stone", AetherBlocks.SENTRY_STONE, block);
    public static final BlockItem SENTRY_CRACKED_STONE = add("sentry_stone_cracked", AetherBlocks.SENTRY_CRACKED_STONE, block);
    public static final BlockItem SENTRY_STONE_TRAP = add("sentry_stone_trap", AetherBlocks.SENTRY_STONE_TRAP, block);
    public static final BlockItem SENTRY_SLAB = add("sentry_slab", AetherBlocks.SENTRY_SLAB, block);
    public static final BlockItem SENTRY_STAIRS = add("sentry_stairs", AetherBlocks.SENTRY_STAIRS, block);
    public static final BlockItem SENTRY_WALL = add("sentry_wall", AetherBlocks.SENTRY_WALL, block);

    public static final BlockItem LIGHT_SENTRY_STONE = add("light_sentry_stone", AetherBlocks.LIGHT_SENTRY_STONE, block);
    public static final BlockItem LIGHT_SENTRY_SLAB = add("light_sentry_slab", AetherBlocks.LIGHT_SENTRY_SLAB, block);
    public static final BlockItem LIGHT_SENTRY_STAIRS = add("light_sentry_stairs", AetherBlocks.LIGHT_SENTRY_STAIRS, block);
    public static final BlockItem LIGHT_SENTRY_WALL = add("light_sentry_wall", AetherBlocks.LIGHT_SENTRY_WALL, block);
    */
    // stairs
    public static final BlockItem SKYROOT_STAIRS = add("skyroot_stairs", AetherBlocks.SKYROOT_STAIRS, building_block);
    public static final BlockItem GOLDEN_OAK_STAIRS = add("golden_oak_stairs", AetherBlocks.GOLDEN_OAK_STAIRS, building_block);
    public static final BlockItem ORANGE_STAIRS = add("orange_stairs", AetherBlocks.ORANGE_STAIRS, building_block);
    public static final BlockItem CRYSTAL_STAIRS = add("crystal_stairs", AetherBlocks.CRYSTAL_STAIRS, building_block);
    public static final BlockItem WISTERIA_STAIRS = add("wisteria_stairs", AetherBlocks.WISTERIA_STAIRS, building_block);
    // stone stairs + slabs
    public static final BlockItem HOLYSTONE_STAIRS = add("holystone_stairs", AetherBlocks.HOLYSTONE_STAIRS, building_block);
    public static final BlockItem COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", AetherBlocks.COBBLED_HOLYSTONE_STAIRS, building_block);
    public static final BlockItem MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", AetherBlocks.MOSSY_HOLYSTONE_STAIRS, building_block);
    public static final BlockItem HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", AetherBlocks.HOLYSTONE_BRICK_STAIRS, building_block);
    public static final BlockItem CARVED_STAIRS = add("carved_stone_stairs", AetherBlocks.CARVED_STONE_STAIRS, building_block);
    public static final BlockItem MOSSY_CARVED_STAIRS = add("mossy_carved_stone_stairs", AetherBlocks.MOSSY_CARVED_STONE_STAIRS, building_block);
    public static final BlockItem HOLYSTONE_SLAB = add("holystone_slab", AetherBlocks.HOLYSTONE_SLAB, building_block);

    public static final BlockItem COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", AetherBlocks.COBBLED_HOLYSTONE_SLAB, building_block);
    public static final BlockItem MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", AetherBlocks.MOSSY_HOLYSTONE_SLAB, building_block);
    public static final BlockItem HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", AetherBlocks.HOLYSTONE_BRICK_SLAB, building_block);
    public static final BlockItem CARVED_SLAB = add("carved_stone_slab", AetherBlocks.CARVED_STONE_SLAB, building_block);
    public static final BlockItem MOSSY_CARVED_SLAB = add("mossy_carved_stone_slab", AetherBlocks.MOSSY_CARVED_STONE_SLAB, building_block);
    // colorfuls

    private static FabricItemSettings decoration() {
        return new FabricItemSettings().group(AetherItemGroups.AETHER_DECORATIONS);
    }

    private static final FabricItemSettings decoration = decoration();
    private static final FabricItemSettings sign = decoration().maxCount(16);
    private static final FabricItemSettings boat = decoration().maxCount(1);
    private static final FabricItemSettings hat = decoration().equipmentSlot(stack -> HEAD);

    // saplings
    public static final BlockItem SKYROOT_SAPLING = add("skyroot_sapling", AetherBlocks.SKYROOT_SAPLING, decoration, compostable30);
    public static final BlockItem GOLDEN_OAK_SAPLING = add("golden_oak_sapling", AetherBlocks.GOLDEN_OAK_SAPLING, decoration, compostable30);
    public static final BlockItem ORANGE_SAPLING = add("orange_sapling", AetherBlocks.ORANGE_SAPLING, decoration, compostable30);
    public static final BlockItem CRYSTAL_SAPLING = add("crystal_sapling", AetherBlocks.CRYSTAL_SAPLING, decoration, compostable50);
    public static final BlockItem ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", AetherBlocks.ROSE_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", AetherBlocks.FROST_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", AetherBlocks.LAVENDER_WISTERIA_SAPLING, decoration, compostable30);
    public static final BlockItem BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", AetherBlocks.BOREAL_WISTERIA_SAPLING, decoration, compostable30);
    // leaves
    public static final BlockItem SKYROOT_LEAVES = add("skyroot_leaves", AetherBlocks.SKYROOT_LEAVES, decoration, compostable30);
    public static final BlockItem GOLDEN_OAK_LEAVES = add("golden_oak_leaves", AetherBlocks.GOLDEN_OAK_LEAVES, decoration, compostable30);
    public static final BlockItem ORANGE_LEAVES = add("orange_leaves", AetherBlocks.ORANGE_LEAVES, decoration, compostable30);
    public static final BlockItem CRYSTAL_LEAVES = add("crystal_leaves", AetherBlocks.CRYSTAL_LEAVES, decoration, compostable50);
    public static final BlockItem ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", AetherBlocks.ROSE_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", AetherBlocks.FROST_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", AetherBlocks.LAVENDER_WISTERIA_LEAVES, decoration, compostable30);
    public static final BlockItem BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", AetherBlocks.BOREAL_WISTERIA_LEAVES, decoration, compostable30);
    // plants
    public static final BlockItem AETHER_GRASS = add("aether_grass_plant", AetherBlocks.AETHER_GRASS, decoration, compostable30);
    public static final BlockItem AETHER_GRASS_FLOWERING = add("aether_grass_flowering", AetherBlocks.AETHER_GRASS_FLOWERING, decoration, compostable30);
    public static final BlockItem AETHER_SHORT_GRASS = add("aether_short_grass", AetherBlocks.AETHER_SHORT_GRASS, decoration, compostable30);
    public static final BlockItem AETHER_FERN = add("aether_fern", AetherBlocks.AETHER_FERN, decoration, compostable30);
    public static final BlockItem AETHER_BUSH = add("aether_bush", AetherBlocks.AETHER_BUSH, decoration, compostable30);
    public static final BlockItem FLUTEGRASS = add("flutegrass", AetherBlocks.FLUTEGRASS, decoration, compostable30);
    public static final BlockItem SHAMROCK = add("shamrock", AetherBlocks.SHAMROCK, decoration, compostable50);
    public static final BlockItem MALT_SPRIG = add("malt_sprig", AetherBlocks.MALT_SPRIG, decoration, compostable30);
    public static final BlockItem HALOPHIA = add("halophia", AetherBlocks.HALOPHIA, decoration, compostable30);
    public static final BlockItem GIANT_LILY = add("giant_lily", new LilyPadItem(AetherBlocks.GIANT_LILY, hat), compostable100);
    public static final BlockItem WEEPING_CLOUDBURST = add("weeping_cloudburst", AetherBlocks.WEEPING_CLOUDBURST, decoration, compostable30);
    public static final BlockItem MOSS_STAR = add("moss_star", AetherBlocks.MOSS_STAR, decoration, compostable50);
    public static final BlockItem MOSS_BALL = add("moss_ball", AetherBlocks.MOSS_BALL, decoration, compostable30);
    public static final BlockItem WILD_SWETROOT = add("wild_swetroot", AetherBlocks.WILD_SWETROOT, decoration, compostable30);

    public static final BlockItem ANCIENT_FLOWER = add("ancient_flower", AetherBlocks.ANCIENT_FLOWER, decoration, compostable65);
    public static final BlockItem ATARAXIA = add("ataraxia", AetherBlocks.ATARAXIA, decoration, compostable65);
    public static final BlockItem CLOUDSBLUFF = add("cloudsbluff", AetherBlocks.CLOUDSBLUFF, decoration, compostable65);
    public static final BlockItem DRIGEAN = add("drigean", AetherBlocks.DRIGEAN, decoration, compostable65);
    public static final BlockItem LUMINAR = add("luminar", AetherBlocks.LUMINAR, decoration, compostable65);

    public static final BlockItem WILD_FLAX = add("wild_flax", AetherBlocks.WILD_FLAX, decoration, compostable100);

    public static final BlockItem ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", AetherBlocks.ROSE_WISTERIA_HANGER, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", AetherBlocks.FROST_WISTERIA_HANGER, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", AetherBlocks.LAVENDER_WISTERIA_HANGER, decoration, compostable30);
    public static final BlockItem BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", AetherBlocks.BOREAL_WISTERIA_HANGER, decoration, compostable30);

    public static final BlockItem SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", AetherBlocks.SKYROOT_LEAF_PILE, decoration, compostable30);
    public static final BlockItem ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", AetherBlocks.ROSE_WISTERIA_LEAF_PILE, decoration, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", AetherBlocks.FROST_WISTERIA_LEAF_PILE, decoration, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", AetherBlocks.LAVENDER_WISTERIA_LEAF_PILE, decoration, compostable30);
    // tall plants
    public static final BlockItem AETHER_TALL_GRASS = add("aether_tall_grass", AetherBlocks.AETHER_TALL_GRASS, decoration, compostable50);
    public static final BlockItem HONEY_NETTLE = add("honey_nettle", AetherBlocks.HONEY_NETTLE, decoration, compostable50);

    public static final BlockItem LIVERWORT = add("liverwort", AetherBlocks.LIVERWORT, decoration, compostable100);
    public static final BlockItem LIVERWORT_CARPET = add("liverwort_carpet", AetherBlocks.LIVERWORT_CARPET, decoration, compostable65);

    // FUNGI BAYBEEE
    public static final BlockItem LICHEN = add("lichen", AetherBlocks.LICHEN, decoration, compostable50);
    public static final BlockItem LUCATIEL_LICHEN = add("lucatiel_lichen", AetherBlocks.LUCATIEL_LICHEN, decoration, compostable50);
    public static final BlockItem LICHEN_PILE = add("lichen_pile", AetherBlocks.LICHEN_PILE, decoration, compostable30);
    public static final BlockItem LUCATIEL_LICHEN_PILE = add("lucatiel_lichen_pile", AetherBlocks.LUCATIEL_LICHEN_PILE, decoration, compostable100);
    public static final BlockItem ROOTCAP = add("rootcap", AetherBlocks.ROOTCAP, decoration(), compostable100);

    public static final BlockItem FLAXWEAVE_CUSHION = add("flaxweave_cushion", AetherBlocks.FLAXWEAVE_CUSHION, decoration, fuel(300));

    // lights
    public static final BlockItem AMBROSIUM_LANTERN = add("ambrosium_lantern", AetherBlocks.AMBROSIUM_LANTERN, decoration);
    public static final WallStandingBlockItem AMBROSIUM_TORCH = add("ambrosium_torch", new WallStandingBlockItem(AetherBlocks.AMBROSIUM_TORCH, AetherBlocks.AMBROSIUM_TORCH_WALL, decoration));
    // util blocks (enchanter, freezer, etc.)

    // door-like things
    public static final BlockItem SKYROOT_DOOR = add("skyroot_door", AetherBlocks.SKYROOT_DOOR, decoration);
    public static final BlockItem GOLDEN_OAK_DOOR = add("golden_oak_door", AetherBlocks.GOLDEN_OAK_DOOR, decoration);
    public static final BlockItem ORANGE_DOOR = add("orange_door", AetherBlocks.ORANGE_DOOR, decoration);
    public static final BlockItem CRYSTAL_DOOR = add("crystal_door", AetherBlocks.CRYSTAL_DOOR, decoration);
    public static final BlockItem WISTERIA_DOOR = add("wisteria_door", AetherBlocks.WISTERIA_DOOR, decoration);

    public static final BlockItem SKYROOT_TRAPDOOR = add("skyroot_trapdoor", AetherBlocks.SKYROOT_TRAPDOOR, decoration);
    public static final BlockItem GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", AetherBlocks.GOLDEN_OAK_TRAPDOOR, decoration);
    public static final BlockItem ORANGE_TRAPDOOR = add("orange_trapdoor", AetherBlocks.ORANGE_TRAPDOOR, decoration);
    public static final BlockItem CRYSTAL_TRAPDOOR = add("crystal_trapdoor", AetherBlocks.CRYSTAL_TRAPDOOR, decoration);
    public static final BlockItem WISTERIA_TRAPDOOR = add("wisteria_trapdoor", AetherBlocks.WISTERIA_TRAPDOOR, decoration);

    public static final BlockItem SKYROOT_FENCE_GATE = add("skyroot_fence_gate", AetherBlocks.SKYROOT_FENCE_GATE, decoration);
    public static final BlockItem GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", AetherBlocks.GOLDEN_OAK_FENCE_GATE, decoration);
    public static final BlockItem ORANGE_FENCE_GATE = add("orange_fence_gate", AetherBlocks.ORANGE_FENCE_GATE, decoration);
    public static final BlockItem CRYSTAL_FENCE_GATE = add("crystal_fence_gate", AetherBlocks.CRYSTAL_FENCE_GATE, decoration);
    public static final BlockItem WISTERIA_FENCE_GATE = add("wisteria_fence_gate", AetherBlocks.WISTERIA_FENCE_GATE, decoration);
    // fences
    public static final BlockItem SKYROOT_FENCE = add("skyroot_fence", AetherBlocks.SKYROOT_FENCE, decoration);
    public static final BlockItem GOLDEN_OAK_FENCE = add("golden_oak_fence", AetherBlocks.GOLDEN_OAK_FENCE, decoration);
    public static final BlockItem ORANGE_FENCE = add("orange_fence", AetherBlocks.ORANGE_FENCE, decoration);
    public static final BlockItem CRYSTAL_FENCE = add("crystal_fence", AetherBlocks.CRYSTAL_FENCE, decoration);
    public static final BlockItem WISTERIA_FENCE = add("wisteria_fence", AetherBlocks.WISTERIA_FENCE, decoration);
    // walls
    public static final BlockItem HOLYSTONE_WALL = add("holystone_wall", AetherBlocks.HOLYSTONE_WALL, decoration);
    public static final BlockItem COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", AetherBlocks.COBBLED_HOLYSTONE_WALL, decoration);
    public static final BlockItem MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", AetherBlocks.MOSSY_HOLYSTONE_WALL, decoration);
    public static final BlockItem HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", AetherBlocks.HOLYSTONE_BRICK_WALL, decoration);
    public static final BlockItem CARVED_WALL = add("carved_stone_wall", AetherBlocks.CARVED_STONE_WALL, decoration);
    public static final BlockItem MOSSY_CARVED_WALL = add("mossy_carved_stone_wall", AetherBlocks.MOSSY_CARVED_STONE_WALL, decoration);
    // infested blocks
    /* TODO: Implement swet drop block items
    public static final BlockItem SWET_DROP = add("swet_drop", AetherBlocks.SWET_DROP, misc);
    public static final BlockItem BLUE_SWET_DROP = add("blue_swet_drop", AetherBlocks.BLUE_SWET_DROP, misc);
    public static final BlockItem GOLDEN_SWET_DROP = add("golden_swet_drop", AetherBlocks.GOLDEN_SWET_DROP, misc);
    public static final BlockItem PURPLE_SWET_DROP = add("purple_swet_drop", AetherBlocks.PURPLE_SWET_DROP, misc);
    */
    // panes & chains
    public static final BlockItem ZANITE_CHAIN = add("zanite_chain", AetherBlocks.ZANITE_CHAIN, decoration);
    public static final BlockItem QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", AetherBlocks.QUICKSOIL_GLASS_PANE, decoration);
    // carpets

    // path & farmland
    public static final BlockItem AETHER_FARMLAND = add("aether_farmland", AetherBlocks.AETHER_FARMLAND, decoration);
    public static final BlockItem AETHER_DIRT_PATH = add("aether_grass_path", AetherBlocks.AETHER_DIRT_PATH, decoration);
    // signs, wall stuff.
    public static final SignItem SKYROOT_SIGN = add("skyroot_sign", new SignItem(sign, AetherBlocks.SKYROOT_SIGN, AetherBlocks.SKYROOT_WALL_SIGN));
    public static final SignItem GOLDEN_OAK_SIGN = add("golden_oak_sign", new SignItem(sign, AetherBlocks.GOLDEN_OAK_SIGN, AetherBlocks.GOLDEN_OAK_WALL_SIGN));
    public static final SignItem ORANGE_SIGN = add("orange_sign", new SignItem(sign, AetherBlocks.ORANGE_SIGN, AetherBlocks.ORANGE_WALL_SIGN));
    public static final SignItem CRYSTAL_SIGN = add("crystal_sign", new SignItem(sign, AetherBlocks.CRYSTAL_SIGN, AetherBlocks.CRYSTAL_WALL_SIGN));
    public static final SignItem WISTERIA_SIGN = add("wisteria_sign", new SignItem(sign, AetherBlocks.WISTERIA_SIGN, AetherBlocks.WISTERIA_WALL_SIGN));
    // beds

    // etc.
    public static final BlockItem SKYROOT_BUTTON = add("skyroot_button", AetherBlocks.SKYROOT_BUTTON, decoration);
    public static final BlockItem GOLDEN_OAK_BUTTON = add("golden_oak_button", AetherBlocks.GOLDEN_OAK_BUTTON, decoration);
    public static final BlockItem ORANGE_BUTTON = add("orange_button", AetherBlocks.ORANGE_BUTTON, decoration);
    public static final BlockItem CRYSTAL_BUTTON = add("crystal_button", AetherBlocks.CRYSTAL_BUTTON, decoration);
    public static final BlockItem WISTERIA_BUTTON = add("wisteria_button", AetherBlocks.WISTERIA_BUTTON, decoration);

    public static final BlockItem SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", AetherBlocks.SKYROOT_PRESSURE_PLATE, decoration);
    public static final BlockItem GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", AetherBlocks.GOLDEN_OAK_PRESSURE_PLATE, decoration);
    public static final BlockItem ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", AetherBlocks.ORANGE_PRESSURE_PLATE, decoration);
    public static final BlockItem CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", AetherBlocks.CRYSTAL_PRESSURE_PLATE, decoration);
    public static final BlockItem WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", AetherBlocks.WISTERIA_PRESSURE_PLATE, decoration);

    //TODO: Implement dungeon switch block
//    public static final BlockItem DUNGEON_SWITCH = add("dungeonswitch", AetherBlocks.DUNGEON_SWITCH, decoration);

    // these should be moved... somewhere?
    public static final BoatItem SKYROOT_BOAT = add("skyroot_boat", new BoatItem(AetherBoatTypes.SKYROOT, boat));
    public static final BoatItem GOLDEN_OAK_BOAT = add("golden_oak_boat", new BoatItem(AetherBoatTypes.GOLDEN_OAK, boat));
    public static final BoatItem ORANGE_BOAT = add("orange_boat", new BoatItem(AetherBoatTypes.ORANGE, boat));
    public static final BoatItem CRYSTAL_BOAT = add("crystal_boat", new BoatItem(AetherBoatTypes.CRYSTAL, boat));
    public static final BoatItem WISTERIA_BOAT = add("wisteria_boat", new BoatItem(AetherBoatTypes.WISTERIA, boat));

    // Chests
    public static final BlockItem SKYROOT_CHEST = add("skyroot_chest", AetherBlocks.SKYROOT_CHEST, new FabricItemSettings().group(AetherItemGroups.AETHER_DECORATIONS));
    // TODO: Implement remaining chests (PL-1.7)
    public static final BlockItem GOLDEN_OAK_CHEST = add("golden_oak_chest", AetherBlocks.GOLDEN_OAK_CHEST, new FabricItemSettings().group(AetherItemGroups.AETHER_DECORATIONS));
    public static final BlockItem ORANGE_CHEST = add("orange_chest", AetherBlocks.ORANGE_CHEST, new FabricItemSettings().group(AetherItemGroups.AETHER_DECORATIONS));
    public static final BlockItem CRYSTAL_CHEST = add("crystal_chest", AetherBlocks.CRYSTAL_CHEST, new FabricItemSettings().group(AetherItemGroups.AETHER_DECORATIONS));
    public static final BlockItem WISTERIA_CHEST = add("wisteria_chest", AetherBlocks.WISTERIA_CHEST, new FabricItemSettings().group(AetherItemGroups.AETHER_DECORATIONS));

    public static void init() {
        AetherRegistryQueues.ITEM.register();
    }

    @SafeVarargs
    private static <V extends Item> V add(String id, V item, Action<? super V>... additionalActions) {
        return AetherRegistryQueues.ITEM.add(locate(id), item, additionalActions);
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

    private static class AetherPickaxeItem extends PickaxeItem {
        protected AetherPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class AetherAxeItem extends AxeItem {
        protected AetherAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class AetherHoeItem extends HoeItem {
        protected AetherHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
            super(material, attackDamage, attackSpeed, settings);
        }
    }

    private static class AetherMusicDiscItem extends MusicDiscItem {
        protected AetherMusicDiscItem(int comparatorValueIn, SoundEvent soundIn, Settings settings) {
            super(comparatorValueIn, soundIn, settings);
        }
    }
}
