package com.aether.items;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.vehicle.AetherBoatTypes;
import com.aether.fluids.AetherFluids;
import com.aether.items.accessories.AccessoryItem;
import com.aether.items.accessories.AccessoryType;
import com.aether.items.armor.AetherArmorMaterials;
import com.aether.items.food.*;
import com.aether.items.resources.AmbrosiumShardItem;
import com.aether.items.utils.StackableVariantColorizer;
import com.aether.items.staff.CloudStaffItem;
import com.aether.items.staff.NatureStaffItem;
import com.aether.items.tools.*;
import com.aether.items.tools.AetherToolMaterials;
import com.aether.items.weapons.*;
import com.aether.registry.RegistryQueue;
import com.aether.registry.RegistryQueue.Action;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

import static com.aether.Aether.locate;
import static com.aether.entities.AetherEntityTypes.*;
import static com.aether.items.AetherItemGroups.*;
import static com.aether.registry.RegistryQueue.onClient;
import static com.aether.util.item.AetherRarity.*;
import static net.minecraft.entity.EquipmentSlot.*;
import static net.minecraft.util.Rarity.*;

@SuppressWarnings("unused")
public class AetherItems {
    private static Action<ItemConvertible> compostable(float chance) { return (id, item) -> ComposterBlock.registerCompostableItem(chance, item); }
    private static final Action<ItemConvertible> compostable30 = compostable(0.3f);
    private static final Action<ItemConvertible> compostable50 = compostable(0.5f);
    private static final Action<ItemConvertible> compostable65 = compostable(0.65f);
    private static final Action<ItemConvertible> swetColor = onClient(new StackableVariantColorizer(0xDADADA, 0x939393, 0x4F4F4F));



    /*
    Begin items
     */

    private static final Settings resource = new Settings().group(AETHER_RESOURCES);
    public static final Item ZANITE_GEM = add("zanite_gemstone", new Item(resource));
    public static final Item ZANITE_FRAGMENT = add("zanite_fragment", new Item(resource));
    public static final Item GRAVITITE_GEM = add("gravitite_gemstone", new Item(resource));
    public static final AmbrosiumShardItem AMBROSIUM_SHARD = add("ambrosium_shard", new AmbrosiumShardItem(resource));
    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource));
    public static final Item AECHOR_PETAL = add("aechor_petal", new Item(resource), compostable65);
    public static final Item SWET_BALL = add("swet_ball", new Item(resource), swetColor);
    public static final Item GOLD_AERDUST = add("gold_aerdust", new Item(resource));
    public static final Item FROZEN_AERDUST = add("frozen_aerdust", new Item(resource));


    private static Settings tool() { return new Settings().group(AETHER_TOOLS); }
    private static final Settings tool = tool();
    private static final Settings rareTool = tool().rarity(RARE);
    private static final Settings aetherLootTool = tool().rarity(AETHER_LOOT);
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
    public static final GravititeHoeItem GRAVITITE_HOE = add("gravitite_hoe", new GravititeHoeItem(AetherToolMaterials.GRAVITITE, 1,4f, rareTool));

    public static final ShovelItem VALKYRIE_SHOVEL = add("valkyrie_shovel", new ShovelItem(AetherToolMaterials.VALKYRIE, 1.5f, -3f,  aetherLootTool));
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

    public static final ParachuteItem CLOUD_PARACHUTE = add("cold_parachute", new ParachuteItem(unstackableTool));
    public static final ParachuteItem GOLDEN_CLOUD_PARACHUTE = add("golden_parachute", new ParachuteItem(tool().maxCount(1).maxDamage(20)));

    public static final AmbrosiumBloodstoneItem AMBROSIUM_BLOODSTONE = add("ambrosium_bloodstone", new AmbrosiumBloodstoneItem(unstackableTool));
    public static final ZaniteBloodstoneItem ZANITE_BLOODSTONE = add("zanite_bloodstone", new ZaniteBloodstoneItem(unstackableTool));
    public static final GravititeBloodstoneItem GRAVITITE_BLOODSTONE = add("gravitite_bloodstone", new GravititeBloodstoneItem(unstackableTool));
    public static final AbstentineBloodstoneItem ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(unstackableTool));


    private static Settings wearable() { return new Settings().group(AETHER_WEARABLES); }
    private static final Settings wearable = wearable();
    private static final Settings rareWearable = wearable().rarity(RARE);
    private static final Settings aetherLootWearable = wearable().rarity(AETHER_LOOT);
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

    public static final ArmorItem OBSIDIAN_HELMET = add("obsidian_helmet", new ArmorItem(AetherArmorMaterials.OBSIDIAN, HEAD, aetherLootWearable));
    public static final ArmorItem OBSIDIAN_CHESTPLATE = add("obsidian_chestplate", new ArmorItem(AetherArmorMaterials.OBSIDIAN, CHEST, aetherLootWearable));
    public static final ArmorItem OBSIDIAN_LEGGINGS = add("obsidian_leggings", new ArmorItem(AetherArmorMaterials.OBSIDIAN, LEGS, aetherLootWearable));
    public static final ArmorItem OBSIDIAN_BOOTS = add("obsidian_boots", new ArmorItem(AetherArmorMaterials.OBSIDIAN, FEET, aetherLootWearable));

    public static final ArmorItem VALKYRIE_HELMET = add("valkyrie_helmet", new ArmorItem(AetherArmorMaterials.VALKYRIE, HEAD, aetherLootWearable));
    public static final ArmorItem VALKYRIE_CHESTPLATE = add("valkyrie_chestplate", new ArmorItem(AetherArmorMaterials.VALKYRIE, CHEST, aetherLootWearable));
    public static final ArmorItem VALKYRIE_LEGGINGS = add("valkyrie_leggings", new ArmorItem(AetherArmorMaterials.VALKYRIE, LEGS, aetherLootWearable));
    public static final ArmorItem VALKYRIE_BOOTS = add("valkyrie_boots", new ArmorItem(AetherArmorMaterials.VALKYRIE, FEET, aetherLootWearable));

    public static final ArmorItem SENTRY_BOOTS = add("sentry_boots", new ArmorItem(AetherArmorMaterials.SENTRY, FEET, aetherLootWearable));

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

    public static final AccessoryItem IRON_RING = add("iron_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final AccessoryItem GOLDEN_RING = add("golden_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final AccessoryItem ZANITE_RING = add("zanite_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final AccessoryItem ICE_RING = add("ice_ring", new AccessoryItem(AccessoryType.RING, rareWearable));

    public static final AccessoryItem GOLDEN_PENDANT = add("golden_pendant", new AccessoryItem(AccessoryType.PENDANT, wearable));
    public static final AccessoryItem ZANITE_PENDANT = add("zanite_pendant", new AccessoryItem(AccessoryType.PENDANT, wearable));
    public static final AccessoryItem ICE_PENDANT = add("ice_pendant", new AccessoryItem(AccessoryType.PENDANT, rareWearable));

    public static final AccessoryItem WHITE_CAPE = add("white_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem RED_CAPE = add("red_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem BLUE_CAPE = add("blue_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem YELLOW_CAPE = add("yellow_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final AccessoryItem SWET_CAPE = add("swet_cape", new AccessoryItem(AccessoryType.CAPE, "swet", aetherLootWearable));
    public static final AccessoryItem AGILITY_CAPE = add("agility_cape", new AccessoryItem(AccessoryType.CAPE, "agility", aetherLootWearable));
    public static final AccessoryItem INVISIBILITY_CAPE = add("invisibility_cape", new AccessoryItem(AccessoryType.CAPE, aetherLootWearable));

    public static final AccessoryItem GOLDEN_FEATHER = add("golden_feather", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    public static final AccessoryItem REGENERATION_STONE = add("regeneration_stone", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    public static final AccessoryItem IRON_BUBBLE = add("iron_bubble", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));


    private static Settings food(FoodComponent foodComponent) { return new Settings().group(AETHER_FOOD).food(foodComponent); }
    private static Settings food(FoodComponent foodComponent, Rarity rarity) { return food(foodComponent).rarity(rarity); }
    public static final AliasedBlockItem BLUEBERRY = add("blue_berry", new AliasedBlockItem(AetherBlocks.BLUEBERRY_BUSH, food(AetherFoodComponent.BLUEBERRY)), compostable30);
    public static final Item ENCHANTED_BLUEBERRY = add("enchanted_blueberry", new Item(food(AetherFoodComponent.ENCHANTED_BLUEBERRY, RARE)), compostable50);
    public static final Item ORANGE = add("orange", new Item(food(AetherFoodComponent.ORANGE)), compostable65);
    public static final WhiteAppleItem WHITE_APPLE = add("white_apple", new WhiteAppleItem(food(AetherFoodComponent.WHITE_APPLE)), compostable(0f));
    public static final Item BLUE_GUMMY_SWET = add("blue_gummy_swet", new Item(food(AetherFoodComponent.GUMMY_SWET, AETHER_LOOT)));
    public static final Item GOLDEN_GUMMY_SWET = add("golden_gummy_swet", new Item(food(AetherFoodComponent.GUMMY_SWET, AETHER_LOOT)));
    public static final ValkyrieMilkItem VALKYRIE_MILK = add("valkyrie_milk", new ValkyrieMilkItem(food(AetherFoodComponent.VALKYRIE_MILK, EPIC).maxCount(1)));
    public static final HealingStoneItem HEALING_STONE = add("healing_stone", new HealingStoneItem(food(AetherFoodComponent.HEALING_STONE, RARE)));
    public static final Item CANDY_CANE = add("candy_cane", new Item(food(AetherFoodComponent.GENERIC)), compostable30);
    public static final Item GINGERBREAD_MAN = add("ginger_bread_man", new Item(food(AetherFoodComponent.GENERIC)), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", new Item(food(AetherFoodComponent.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", new Item(food(AetherFoodComponent.COOKED_MOA_MEAT)));


    private static Settings misc() { return new Settings().group(AETHER_MISC); }
    private static final Settings misc = misc();
    public static final LifeShardItem LIFE_SHARD = add("life_shard", new LifeShardItem(misc().rarity(AETHER_LOOT).maxCount(1)));
    public static final CloudStaffItem CLOUD_STAFF = add("cloud_staff", new CloudStaffItem(misc().maxCount(1).maxDamage(60)));
    public static final NatureStaffItem NATURE_STAFF = add("nature_staff", new NatureStaffItem(misc().maxCount(1).maxDamage(100)));
    public static final MoaEggItem MOA_EGG = add("moa_egg", new MoaEggItem(misc().maxCount(1)));
    public static final BookOfLoreItem LORE_BOOK = add("lore_book", new BookOfLoreItem(misc().maxCount(1)));
    public static final BlockItem INCUBATOR = add("incubator", AetherBlocks.INCUBATOR, misc);
    public static final BlockItem FOOD_BOWL = add("food_bowl", AetherBlocks.FOOD_BOWL, misc);

    public static final SkyrootBucketItem SKYROOT_BUCKET = add("skyroot_bucket", new SkyrootBucketItem(misc().maxCount(16)));

    private static final Settings skyrootBucket = misc().maxCount(1).recipeRemainder(SKYROOT_BUCKET);
    public static final SkyrootBucketItem SKYROOT_WATER_BUCKET = add("skyroot_water_bucket", new SkyrootBucketItem(Fluids.WATER, skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_MILK_BUCKET = add("skyroot_milk_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_POISON_BUCKET = add("skyroot_poison_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final SkyrootBucketItem SKYROOT_REMEDY_BUCKET = add("skyroot_remedy_bucket", new SkyrootBucketItem(skyrootBucket));

    public static final VialItem QUICKSOIL_VIAL = add("quicksoil_vial", new VialItem(Fluids.EMPTY, misc().maxCount(32)));
    public static final VialItem AERCLOUD_VIAL = add("aercloud_vial", new VialItem(AetherFluids.DENSE_AERCLOUD, misc().maxCount(32)));

    public static final Item BRONZE_KEY = add("bronze_key", new Item(misc().rarity(AETHER_LOOT)));
    public static final Item SILVER_KEY = add("silver_key", new Item(misc().rarity(AETHER_LOOT)));
    public static final Item GOLDEN_KEY = add("golden_key", new Item(misc().rarity(AETHER_LOOT)));

    public static final AetherPortalItem AETHER_PORTAL = add("aether_portal", new AetherPortalItem(misc));

    public static final SpawnEggItem AECHOR_PLANT_SPAWN_EGG = add("aechor_plant_spawn_egg", new SpawnEggItem(AECHOR_PLANT, 0x97DED4, 0x31897D, misc));
    public static final SpawnEggItem CHEST_MIMIC_SPAWN_EGG = null;
    public static final SpawnEggItem COCKATRICE_SPAWN_EGG = add("cockatrice_spawn_egg", new SpawnEggItem(COCKATRICE, 0x9FC3F7, 0x3D2338, misc));
    public static final SpawnEggItem AERBUNNY_SPAWN_EGG = add("aerbunny_spawn_egg", new SpawnEggItem(AERBUNNY, 0xC5D6ED, 0x82A6D9, misc));
    public static final SpawnEggItem AERWHALE_SPAWN_EGG = add("aerwhale_spawn_egg", new SpawnEggItem(AERWHALE, 0x5C6D91, 0xDEDBCE, misc));
    public static final SpawnEggItem FLYING_COW_SPAWN_EGG = null;
    public static final SpawnEggItem MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(MOA, 0xC55C2E4, 0xB3A8BB, misc));
    public static final SpawnEggItem SWET_SPAWN_EGG = add("swet_spawn_egg", new SpawnEggItem(WHITE_SWET, 0x8F9294, 0xE6EAEB, misc));
    public static final SpawnEggItem BLUE_SWET_SPAWN_EGG = add("blue_swet_spawn_egg", new SpawnEggItem(BLUE_SWET, 0x46699E, 0xE6EAEB, misc));
    public static final SpawnEggItem PURPLE_SWET_SPAWN_EGG = add("purple_swet_spawn_egg", new SpawnEggItem(PURPLE_SWET, 0x5D548C, 0xE6EAEB, misc));
    public static final SpawnEggItem GOLDEN_SWET_SPAWN_EGG = add("golden_swet_spawn_egg", new SpawnEggItem(GOLDEN_SWET, 0xC99D36, 0xE6EAEB, misc));
    public static final SpawnEggItem PHYG_SPAWN_EGG = null;
    public static final SpawnEggItem SHEEPUFF_SPAWN_EGG = null;

    /* todo swet drop block items
    public static final BlockItem SWET_DROP = add("swet_drop", AetherBlocks.SWET_DROP, misc);
    public static final BlockItem BLUE_SWET_DROP = add("blue_swet_drop", AetherBlocks.BLUE_SWET_DROP, misc);
    public static final BlockItem GOLDEN_SWET_DROP = add("golden_swet_drop", AetherBlocks.GOLDEN_SWET_DROP, misc);
    public static final BlockItem PURPLE_SWET_DROP = add("purple_swet_drop", AetherBlocks.PURPLE_SWET_DROP, misc);*/

    
    private static Settings block() { return new Settings().group(AETHER_BLOCKS); }
    private static final Settings block = block();
    private static final Settings sign = block().maxCount(16);
    public static final BlockItem AETHER_GRASS_BLOCK = add("aether_grass", AetherBlocks.AETHER_GRASS_BLOCK, block);
    public static final BlockItem AETHER_ENCHANTED_GRASS = add("enchanted_aether_grass", AetherBlocks.AETHER_ENCHANTED_GRASS, block);

    public static final BlockItem AETHER_DIRT = add("aether_dirt", AetherBlocks.AETHER_DIRT, block);
    public static final BlockItem AETHER_FARMLAND = add("aether_farmland", AetherBlocks.AETHER_FARMLAND, block);
    public static final BlockItem AETHER_DIRT_PATH = add("aether_grass_path", AetherBlocks.AETHER_DIRT_PATH, block);
    public static final BlockItem QUICKSOIL = add("quicksoil", AetherBlocks.QUICKSOIL, block);

    public static final BlockItem QUICKSOIL_GLASS = add("quicksoil_glass", AetherBlocks.QUICKSOIL_GLASS, block);
    public static final BlockItem QUICKSOIL_GLASS_PANE = add("quicksoil_glass_pane", AetherBlocks.QUICKSOIL_GLASS_PANE, block);

    public static final BlockItem COLD_AERCLOUD = add("cold_aercloud", AetherBlocks.COLD_AERCLOUD, block);
    public static final BlockItem BLUE_AERCLOUD = add("blue_aercloud", AetherBlocks.BLUE_AERCLOUD, block);
    public static final BlockItem PINK_AERCLOUD = add("pink_aercloud", AetherBlocks.PINK_AERCLOUD, block);
    public static final BlockItem GOLDEN_AERCLOUD = add("golden_aercloud", AetherBlocks.GOLDEN_AERCLOUD, block);

    public static final BlockItem ICESTONE = add("icestone", AetherBlocks.ICESTONE, block);
    public static final BlockItem AEROGEL = add("aerogel", AetherBlocks.AEROGEL, block);

    public static final BlockItem HOLYSTONE = add("holystone", AetherBlocks.HOLYSTONE, block);
    public static final BlockItem HOLYSTONE_SLAB = add("holystone_slab", AetherBlocks.HOLYSTONE_SLAB, block);
    public static final BlockItem HOLYSTONE_STAIRS = add("holystone_stairs", AetherBlocks.HOLYSTONE_STAIRS, block);
    public static final BlockItem HOLYSTONE_WALL = add("holystone_wall", AetherBlocks.HOLYSTONE_WALL, block);

    public static final BlockItem COBBLED_HOLYSTONE = add("cobbled_holystone", AetherBlocks.COBBLED_HOLYSTONE, block);
    public static final BlockItem COBBLED_HOLYSTONE_SLAB = add("cobbled_holystone_slab", AetherBlocks.COBBLED_HOLYSTONE_SLAB, block);
    public static final BlockItem COBBLED_HOLYSTONE_STAIRS = add("cobbled_holystone_stairs", AetherBlocks.COBBLED_HOLYSTONE_STAIRS, block);
    public static final BlockItem COBBLED_HOLYSTONE_WALL = add("cobbled_holystone_wall", AetherBlocks.COBBLED_HOLYSTONE_WALL, block);

    public static final BlockItem MOSSY_HOLYSTONE = add("mossy_holystone", AetherBlocks.MOSSY_HOLYSTONE, block);
    public static final BlockItem GOLDEN_MOSSY_HOLYSTONE = add("golden_mossy_holystone", AetherBlocks.GOLDEN_MOSSY_HOLYSTONE, block);
    public static final BlockItem MOSSY_HOLYSTONE_SLAB = add("mossy_holystone_slab", AetherBlocks.MOSSY_HOLYSTONE_SLAB, block);
    public static final BlockItem MOSSY_HOLYSTONE_STAIRS = add("mossy_holystone_stairs", AetherBlocks.MOSSY_HOLYSTONE_STAIRS, block);
    public static final BlockItem MOSSY_HOLYSTONE_WALL = add("mossy_holystone_wall", AetherBlocks.MOSSY_HOLYSTONE_WALL, block);

    public static final BlockItem HOLYSTONE_BRICK = add("holystone_brick", AetherBlocks.HOLYSTONE_BRICK, block);
    public static final BlockItem HOLYSTONE_BRICK_SLAB = add("holystone_brick_slab", AetherBlocks.HOLYSTONE_BRICK_SLAB, block);
    public static final BlockItem HOLYSTONE_BRICK_STAIRS = add("holystone_brick_stairs", AetherBlocks.HOLYSTONE_BRICK_STAIRS, block);
    public static final BlockItem HOLYSTONE_BRICK_WALL = add("holystone_brick_wall", AetherBlocks.HOLYSTONE_BRICK_WALL, block);

    public static final BlockItem ANGELIC_STONE = add("angelic_stone", AetherBlocks.ANGELIC_STONE, block);
    public static final BlockItem ANGELIC_CRACKED_STONE = add("angelic_stone_cracked", AetherBlocks.ANGELIC_CRACKED_STONE, block);
    public static final BlockItem ANGELIC_SLAB = add("angelic_slab", AetherBlocks.ANGELIC_SLAB, block);
    public static final BlockItem ANGELIC_STAIRS = add("angelic_stairs", AetherBlocks.ANGELIC_STAIRS, block);
    public static final BlockItem ANGELIC_WALL = add("angelic_wall", AetherBlocks.ANGELIC_WALL, block);

    public static final BlockItem LIGHT_ANGELIC_STONE = add("light_angelic_stone", AetherBlocks.LIGHT_ANGELIC_STONE, block);
    //todo public static final BlockItem LIGHT_ANGELIC_STONE_TRAP = add("light_angelic_stone_trap", AetherBlocks.LIGHT_ANGELIC_STONE_TRAP, block);
    public static final BlockItem LIGHT_ANGELIC_SLAB = add("light_angelic_slab", AetherBlocks.LIGHT_ANGELIC_SLAB, block);
    public static final BlockItem LIGHT_ANGELIC_STAIRS = add("light_angelic_stairs", AetherBlocks.LIGHT_ANGELIC_STAIRS, block);
    public static final BlockItem LIGHT_ANGELIC_WALL = add("light_angelic_wall", AetherBlocks.LIGHT_ANGELIC_WALL, block);

    public static final BlockItem HELLFIRE_STONE = add("hellfire_stone", AetherBlocks.HELLFIRE_STONE, block);
    public static final BlockItem HELLFIRE_CRACKED_STONE = add("hellfire_stone_cracked", AetherBlocks.HELLFIRE_CRACKED_STONE, block);
    //public static final BlockItem HELLFIRE_STONE_TRAP = add("hellfire_stone_trap", AetherBlocks.HELLFIRE_STONE_TRAP, block);
    public static final BlockItem HELLFIRE_SLAB = add("hellfire_slab", AetherBlocks.HELLFIRE_SLAB, block);
    public static final BlockItem HELLFIRE_STAIRS = add("hellfire_stairs", AetherBlocks.HELLFIRE_STAIRS, block);
    public static final BlockItem HELLFIRE_WALL = add("hellfire_wall", AetherBlocks.HELLFIRE_WALL, block);

    public static final BlockItem LIGHT_HELLFIRE_STONE = add("light_hellfire_stone", AetherBlocks.LIGHT_HELLFIRE_STONE, block);
    //public static final BlockItem LIGHT_HELLFIRE_STONE_TRAP = add("light_hellfire_stone_trap", AetherBlocks.LIGHT_HELLFIRE_STONE_TRAP, block);
    public static final BlockItem LIGHT_HELLFIRE_SLAB = add("light_hellfire_slab", AetherBlocks.LIGHT_HELLFIRE_SLAB, block);
    public static final BlockItem LIGHT_HELLFIRE_STAIRS = add("light_hellfire_stairs", AetherBlocks.LIGHT_HELLFIRE_STAIRS, block);
    public static final BlockItem LIGHT_HELLFIRE_WALL = add("light_hellfire_wall", AetherBlocks.LIGHT_HELLFIRE_WALL, block);

    public static final BlockItem CARVED_STONE = add("carved_stone", AetherBlocks.CARVED_STONE, block);
    //public static final BlockItem CARVED_STONE_TRAP = add("carved_stone_trap", AetherBlocks.CARVED_STONE_TRAP, block);
    public static final BlockItem CARVED_SLAB = add("carved_slab", AetherBlocks.CARVED_SLAB, block);
    public static final BlockItem CARVED_STAIRS = add("carved_stairs", AetherBlocks.CARVED_STAIRS, block);
    public static final BlockItem CARVED_WALL = add("carved_wall", AetherBlocks.CARVED_WALL, block);

    public static final BlockItem LIGHT_CARVED_STONE = add("light_carved_stone", AetherBlocks.LIGHT_CARVED_STONE, block);
    //public static final BlockItem LIGHT_CARVED_STONE_TRAP = add("light_carved_stone_trap", AetherBlocks.LIGHT_CARVED_STONE_TRAP, block);
    public static final BlockItem LIGHT_CARVED_SLAB = add("light_carved_slab", AetherBlocks.LIGHT_CARVED_SLAB, block);
    public static final BlockItem LIGHT_CARVED_STAIRS = add("light_carved_stairs", AetherBlocks.LIGHT_CARVED_STAIRS, block);
    public static final BlockItem LIGHT_CARVED_WALL = add("light_carved_wall", AetherBlocks.LIGHT_CARVED_WALL, block);

    public static final BlockItem SENTRY_STONE = add("sentry_stone", AetherBlocks.SENTRY_STONE, block);
    public static final BlockItem SENTRY_CRACKED_STONE = add("sentry_stone_cracked", AetherBlocks.SENTRY_CRACKED_STONE, block);
    //public static final BlockItem SENTRY_STONE_TRAP = add("sentry_stone_trap", AetherBlocks.SENTRY_STONE_TRAP, block);
    public static final BlockItem SENTRY_SLAB = add("sentry_slab", AetherBlocks.SENTRY_SLAB, block);
    public static final BlockItem SENTRY_STAIRS = add("sentry_stairs", AetherBlocks.SENTRY_STAIRS, block);
    public static final BlockItem SENTRY_WALL = add("sentry_wall", AetherBlocks.SENTRY_WALL, block);

    public static final BlockItem LIGHT_SENTRY_STONE = add("light_sentry_stone", AetherBlocks.LIGHT_SENTRY_STONE, block);
    public static final BlockItem LIGHT_SENTRY_SLAB = add("light_sentry_slab", AetherBlocks.LIGHT_SENTRY_SLAB, block);
    public static final BlockItem LIGHT_SENTRY_STAIRS = add("light_sentry_stairs", AetherBlocks.LIGHT_SENTRY_STAIRS, block);
    public static final BlockItem LIGHT_SENTRY_WALL = add("light_sentry_wall", AetherBlocks.LIGHT_SENTRY_WALL, block);

    public static final BlockItem SKYROOT_SAPLING = add("skyroot_sapling", AetherBlocks.SKYROOT_SAPLING, block, compostable30);
    public static final BlockItem SKYROOT_LOG = add("skyroot_log", AetherBlocks.SKYROOT_LOG, block);
    public static final BlockItem SKYROOT_WOOD = add("skyroot_wood", AetherBlocks.SKYROOT_WOOD, block);
    public static final BlockItem STRIPPED_SKYROOT_LOG = add("stripped_skyroot_log", AetherBlocks.STRIPPED_SKYROOT_LOG, block);
    public static final BlockItem STRIPPED_SKYROOT_WOOD = add("stripped_skyroot_wood", AetherBlocks.STRIPPED_SKYROOT_WOOD, block);
    public static final BlockItem SKYROOT_LEAVES = add("skyroot_leaves", AetherBlocks.SKYROOT_LEAVES, block, compostable30);
    public static final BlockItem SKYROOT_LEAF_PILE = add("skyroot_leaf_pile", AetherBlocks.SKYROOT_LEAF_PILE, block, compostable30);
    public static final BlockItem SKYROOT_PLANKS = add("skyroot_planks", AetherBlocks.SKYROOT_PLANKS, block);
    public static final BlockItem SKYROOT_BOOKSHELF = add("skyroot_bookshelf", AetherBlocks.SKYROOT_BOOKSHELF, block);
    public static final BlockItem SKYROOT_FENCE = add("skyroot_fence", AetherBlocks.SKYROOT_FENCE, block);
    public static final BlockItem SKYROOT_FENCE_GATE = add("skyroot_fence_gate", AetherBlocks.SKYROOT_FENCE_GATE, block);
    public static final BlockItem SKYROOT_SLAB = add("skyroot_slab", AetherBlocks.SKYROOT_SLAB, block);
    public static final BlockItem SKYROOT_STAIRS = add("skyroot_stairs", AetherBlocks.SKYROOT_STAIRS, block);
    public static final BlockItem SKYROOT_TRAPDOOR = add("skyroot_trapdoor", AetherBlocks.SKYROOT_TRAPDOOR, block);
    public static final BlockItem SKYROOT_DOOR = add("skyroot_door", AetherBlocks.SKYROOT_DOOR, block);
    public static final BlockItem SKYROOT_BUTTON = add("skyroot_button", AetherBlocks.SKYROOT_BUTTON, block);
    public static final BlockItem SKYROOT_PRESSURE_PLATE = add("skyroot_pressure_plate", AetherBlocks.SKYROOT_PRESSURE_PLATE, block);
    public static final SignItem SKYROOT_SIGN = add("skyroot_sign", new SignItem(sign, AetherBlocks.SKYROOT_SIGN, AetherBlocks.SKYROOT_WALL_SIGN));
    public static final BoatItem SKYROOT_BOAT = add("skyroot_boat", new BoatItem(AetherBoatTypes.SKYROOT, block().maxCount(1)));

    public static final BlockItem GOLDEN_OAK_SAPLING = add("golden_oak_sapling", AetherBlocks.GOLDEN_OAK_SAPLING, block, compostable30);
    public static final BlockItem GOLDEN_OAK_LOG = add("golden_oak_log", AetherBlocks.GOLDEN_OAK_LOG, block);
    public static final BlockItem GOLDEN_OAK_WOOD = add("golden_oak_wood", AetherBlocks.GOLDEN_OAK_WOOD, block);
    public static final BlockItem STRIPPED_GOLDEN_OAK_LOG = add("stripped_golden_oak_log", AetherBlocks.STRIPPED_GOLDEN_OAK_LOG, block);
    public static final BlockItem STRIPPED_GOLDEN_OAK_WOOD = add("stripped_golden_oak_wood", AetherBlocks.STRIPPED_GOLDEN_OAK_WOOD, block);
    public static final BlockItem GOLDEN_OAK_LEAVES = add("golden_oak_leaves", AetherBlocks.GOLDEN_OAK_LEAVES, block, compostable30);
    public static final BlockItem GOLDEN_OAK_PLANKS = add("golden_oak_planks", AetherBlocks.GOLDEN_OAK_PLANKS, block);
    public static final BlockItem GOLDEN_OAK_FENCE = add("golden_oak_fence", AetherBlocks.GOLDEN_OAK_FENCE, block);
    public static final BlockItem GOLDEN_OAK_FENCE_GATE = add("golden_oak_fence_gate", AetherBlocks.GOLDEN_OAK_FENCE_GATE, block);
    public static final BlockItem GOLDEN_OAK_SLAB = add("golden_oak_slab", AetherBlocks.GOLDEN_OAK_SLAB, block);
    public static final BlockItem GOLDEN_OAK_STAIRS = add("golden_oak_stairs", AetherBlocks.GOLDEN_OAK_STAIRS, block);
    public static final BlockItem GOLDEN_OAK_TRAPDOOR = add("golden_oak_trapdoor", AetherBlocks.GOLDEN_OAK_TRAPDOOR, block);
    public static final BlockItem GOLDEN_OAK_DOOR = add("golden_oak_door", AetherBlocks.GOLDEN_OAK_DOOR, block);
    public static final BlockItem GOLDEN_OAK_BUTTON = add("golden_oak_button", AetherBlocks.GOLDEN_OAK_BUTTON, block);
    public static final BlockItem GOLDEN_OAK_PRESSURE_PLATE = add("golden_oak_pressure_plate", AetherBlocks.GOLDEN_OAK_PRESSURE_PLATE, block);
    public static final SignItem GOLDEN_OAK_SIGN = add("golden_oak_sign", new SignItem(sign, AetherBlocks.GOLDEN_OAK_SIGN, AetherBlocks.GOLDEN_OAK_WALL_SIGN));
    public static final BoatItem GOLDEN_OAK_BOAT = add("golden_oak_boat", new BoatItem(AetherBoatTypes.GOLDEN_OAK, block().maxCount(1)));

    public static final BlockItem ORANGE_SAPLING = add("orange_sapling", AetherBlocks.ORANGE_SAPLING, block, compostable30);
    public static final BlockItem ORANGE_LOG = add("orange_log", AetherBlocks.ORANGE_LOG, block);
    public static final BlockItem ORANGE_WOOD = add("orange_wood", AetherBlocks.ORANGE_WOOD, block);
    public static final BlockItem STRIPPED_ORANGE_LOG = add("stripped_orange_log", AetherBlocks.STRIPPED_ORANGE_LOG, block);
    public static final BlockItem STRIPPED_ORANGE_WOOD = add("stripped_orange_wood", AetherBlocks.STRIPPED_ORANGE_WOOD, block);
    public static final BlockItem ORANGE_LEAVES = add("orange_leaves", AetherBlocks.ORANGE_LEAVES, block, compostable30);
    public static final BlockItem ORANGE_PLANKS = add("orange_planks", AetherBlocks.ORANGE_PLANKS, block);
    public static final BlockItem ORANGE_FENCE = add("orange_fence", AetherBlocks.ORANGE_FENCE, block);
    public static final BlockItem ORANGE_FENCE_GATE = add("orange_fence_gate", AetherBlocks.ORANGE_FENCE_GATE, block);
    public static final BlockItem ORANGE_SLAB = add("orange_slab", AetherBlocks.ORANGE_SLAB, block);
    public static final BlockItem ORANGE_STAIRS = add("orange_stairs", AetherBlocks.ORANGE_STAIRS, block);
    public static final BlockItem ORANGE_TRAPDOOR = add("orange_trapdoor", AetherBlocks.ORANGE_TRAPDOOR, block);
    public static final BlockItem ORANGE_DOOR = add("orange_door", AetherBlocks.ORANGE_DOOR, block);
    public static final BlockItem ORANGE_BUTTON = add("orange_button", AetherBlocks.ORANGE_BUTTON, block);
    public static final BlockItem ORANGE_PRESSURE_PLATE = add("orange_pressure_plate", AetherBlocks.ORANGE_PRESSURE_PLATE, block);
    public static final SignItem ORANGE_SIGN = add("orange_sign", new SignItem(sign, AetherBlocks.ORANGE_SIGN, AetherBlocks.ORANGE_WALL_SIGN));
    public static final BoatItem ORANGE_BOAT = add("orange_boat", new BoatItem(AetherBoatTypes.ORANGE, block().maxCount(1)));

    public static final BlockItem CRYSTAL_SAPLING = add("crystal_sapling", AetherBlocks.CRYSTAL_SAPLING, block, compostable50);
    public static final BlockItem CRYSTAL_LOG = add("crystal_log", AetherBlocks.CRYSTAL_LOG, block);
    public static final BlockItem CRYSTAL_WOOD = add("crystal_wood", AetherBlocks.CRYSTAL_WOOD, block);
    public static final BlockItem STRIPPED_CRYSTAL_LOG = add("stripped_crystal_log", AetherBlocks.STRIPPED_CRYSTAL_LOG, block);
    public static final BlockItem STRIPPED_CRYSTAL_WOOD = add("stripped_crystal_wood", AetherBlocks.STRIPPED_CRYSTAL_WOOD, block);
    public static final BlockItem CRYSTAL_LEAVES = add("crystal_leaves", AetherBlocks.CRYSTAL_LEAVES, block, compostable50);
    public static final BlockItem CRYSTAL_PLANKS = add("crystal_planks", AetherBlocks.CRYSTAL_PLANKS, block);
    public static final BlockItem CRYSTAL_FENCE = add("crystal_fence", AetherBlocks.CRYSTAL_FENCE, block);
    public static final BlockItem CRYSTAL_FENCE_GATE = add("crystal_fence_gate", AetherBlocks.CRYSTAL_FENCE_GATE, block);
    public static final BlockItem CRYSTAL_SLAB = add("crystal_slab", AetherBlocks.CRYSTAL_SLAB, block);
    public static final BlockItem CRYSTAL_STAIRS = add("crystal_stairs", AetherBlocks.CRYSTAL_STAIRS, block);
    public static final BlockItem CRYSTAL_TRAPDOOR = add("crystal_trapdoor", AetherBlocks.CRYSTAL_TRAPDOOR, block);
    public static final BlockItem CRYSTAL_DOOR = add("crystal_door", AetherBlocks.CRYSTAL_DOOR, block);
    public static final BlockItem CRYSTAL_BUTTON = add("crystal_button", AetherBlocks.CRYSTAL_BUTTON, block);
    public static final BlockItem CRYSTAL_PRESSURE_PLATE = add("crystal_pressure_plate", AetherBlocks.CRYSTAL_PRESSURE_PLATE, block);
    public static final SignItem CRYSTAL_SIGN = add("crystal_sign", new SignItem(sign, AetherBlocks.CRYSTAL_SIGN, AetherBlocks.CRYSTAL_WALL_SIGN));
    public static final BoatItem CRYSTAL_BOAT = add("crystal_boat", new BoatItem(AetherBoatTypes.CRYSTAL, block().maxCount(1)));

    public static final BlockItem WISTERIA_LOG = add("wisteria_log", AetherBlocks.WISTERIA_LOG, block);
    public static final BlockItem WISTERIA_WOOD = add("wisteria_wood", AetherBlocks.WISTERIA_WOOD, block);
    public static final BlockItem STRIPPED_WISTERIA_LOG = add("stripped_wisteria_log", AetherBlocks.STRIPPED_WISTERIA_LOG, block);
    public static final BlockItem STRIPPED_WISTERIA_WOOD = add("stripped_wisteria_wood", AetherBlocks.STRIPPED_WISTERIA_WOOD, block);
    public static final BlockItem WISTERIA_PLANKS = add("wisteria_planks", AetherBlocks.WISTERIA_PLANKS, block);
    public static final BlockItem WISTERIA_FENCE = add("wisteria_fence", AetherBlocks.WISTERIA_FENCE, block);
    public static final BlockItem WISTERIA_FENCE_GATE = add("wisteria_fence_gate", AetherBlocks.WISTERIA_FENCE_GATE, block);
    public static final BlockItem WISTERIA_SLAB = add("wisteria_slab", AetherBlocks.WISTERIA_SLAB, block);
    public static final BlockItem WISTERIA_STAIRS = add("wisteria_stairs", AetherBlocks.WISTERIA_STAIRS, block);
    public static final BlockItem WISTERIA_TRAPDOOR = add("wisteria_trapdoor", AetherBlocks.WISTERIA_TRAPDOOR, block);
    public static final BlockItem WISTERIA_DOOR = add("wisteria_door", AetherBlocks.WISTERIA_DOOR, block);
    public static final BlockItem WISTERIA_BUTTON = add("wisteria_button", AetherBlocks.WISTERIA_BUTTON, block);
    public static final BlockItem WISTERIA_PRESSURE_PLATE = add("wisteria_pressure_plate", AetherBlocks.WISTERIA_PRESSURE_PLATE, block);
    public static final SignItem WISTERIA_SIGN = add("wisteria_sign", new SignItem(sign, AetherBlocks.WISTERIA_SIGN, AetherBlocks.WISTERIA_WALL_SIGN));
    public static final BoatItem WISTERIA_BOAT = add("wisteria_boat", new BoatItem(AetherBoatTypes.WISTERIA, block().maxCount(1)));

    public static final BlockItem ROSE_WISTERIA_LEAVES = add("rose_wisteria_leaves", AetherBlocks.ROSE_WISTERIA_LEAVES, block, compostable30);
    public static final BlockItem ROSE_WISTERIA_LEAF_PILE = add("rose_wisteria_leaf_pile", AetherBlocks.ROSE_WISTERIA_LEAF_PILE, block, compostable30);
    public static final BlockItem ROSE_WISTERIA_SAPLING = add("rose_wisteria_sapling", AetherBlocks.ROSE_WISTERIA_SAPLING, block, compostable30);
    public static final BlockItem ROSE_WISTERIA_HANGER = add("rose_wisteria_hanger", AetherBlocks.ROSE_WISTERIA_HANGER, block, compostable30);

    public static final BlockItem FROST_WISTERIA_LEAVES = add("frost_wisteria_leaves", AetherBlocks.FROST_WISTERIA_LEAVES, block, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAF_PILE = add("frost_wisteria_leaf_pile", AetherBlocks.FROST_WISTERIA_LEAF_PILE, block, compostable30);
    public static final BlockItem FROST_WISTERIA_SAPLING = add("frost_wisteria_sapling", AetherBlocks.FROST_WISTERIA_SAPLING, block, compostable30);
    public static final BlockItem FROST_WISTERIA_HANGER = add("frost_wisteria_hanger", AetherBlocks.FROST_WISTERIA_HANGER, block, compostable30);

    public static final BlockItem LAVENDER_WISTERIA_LEAVES = add("lavender_wisteria_leaves", AetherBlocks.LAVENDER_WISTERIA_LEAVES, block, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAF_PILE = add("lavender_wisteria_leaf_pile", AetherBlocks.LAVENDER_WISTERIA_LEAF_PILE, block, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_SAPLING = add("lavender_wisteria_sapling", AetherBlocks.LAVENDER_WISTERIA_SAPLING, block, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_HANGER = add("lavender_wisteria_hanger", AetherBlocks.LAVENDER_WISTERIA_HANGER, block, compostable30);

    public static final BlockItem BOREAL_WISTERIA_LEAVES = add("boreal_wisteria_leaves", AetherBlocks.BOREAL_WISTERIA_LEAVES, block, compostable30);
    public static final BlockItem BOREAL_WISTERIA_SAPLING = add("boreal_wisteria_sapling", AetherBlocks.BOREAL_WISTERIA_SAPLING, block, compostable30);
    public static final BlockItem BOREAL_WISTERIA_HANGER = add("boreal_wisteria_hanger", AetherBlocks.BOREAL_WISTERIA_HANGER, block, compostable30);

    public static final BlockItem AETHER_GRASS = add("aether_grass_plant", AetherBlocks.AETHER_GRASS, block, compostable30);
    public static final BlockItem AETHER_TALL_GRASS = add("aether_tall_grass", AetherBlocks.AETHER_TALL_GRASS, block, compostable50);
    public static final BlockItem AETHER_FERN = add("aether_fern", AetherBlocks.AETHER_FERN, block, compostable30);
    public static final BlockItem AETHER_BUSH = add("aether_bush", AetherBlocks.AETHER_BUSH, block, compostable30);
    public static final BlockItem FLUTEGRASS = add("flutegrass", AetherBlocks.FLUTEGRASS, block, compostable30);

    public static final BlockItem ANCIENT_FLOWER = add("ancient_flower", AetherBlocks.ANCIENT_FLOWER, block, compostable65);
    public static final BlockItem ATARAXIA = add("ataraxia", AetherBlocks.ATARAXIA, block, compostable65);
    public static final BlockItem CLOUDSBLUFF = add("cloudsbluff", AetherBlocks.CLOUDSBLUFF, block, compostable65);
    public static final BlockItem DRIGEAN = add("drigean", AetherBlocks.DRIGEAN, block, compostable65);
    public static final BlockItem LUMINAR = add("luminar", AetherBlocks.LUMINAR, block, compostable65);

    public static final BlockItem AMBROSIUM_ORE = add("ambrosium_ore", AetherBlocks.AMBROSIUM_ORE, block);
    public static final BlockItem ZANITE_ORE = add("zanite_ore", AetherBlocks.ZANITE_ORE, block);
    public static final BlockItem GRAVITITE_ORE = add("gravitite_ore", AetherBlocks.GRAVITITE_ORE, block);
    public static final BlockItem ZANITE_BLOCK = add("zanite_block", AetherBlocks.ZANITE_BLOCK, block);
    public static final BlockItem BLOCK_OF_GRAVITITE = add("block_of_gravitite", AetherBlocks.BLOCK_OF_GRAVITITE, block);
    public static final BlockItem GRAVITITE_LEVITATOR = add("gravitite_levitator", AetherBlocks.GRAVITITE_LEVITATOR, block);
    public static final BlockItem ZANITE_CHAIN = add("zanite_chain", AetherBlocks.ZANITE_CHAIN, block);
    public static final BlockItem AMBROSIUM_LANTERN = add("ambrosium_lantern", AetherBlocks.AMBROSIUM_LANTERN, block);
    public static final WallStandingBlockItem AMBROSIUM_TORCH = add("ambrosium_torch", new WallStandingBlockItem(AetherBlocks.AMBROSIUM_TORCH, AetherBlocks.AMBROSIUM_TORCH_WALL, block));


    static {
        // TODO clean up this mess
        DispenserBehavior skyrootBucketBehavior = new ItemDispenserBehavior() {
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                if (!(stack.getItem() instanceof SkyrootBucketItem bucket))
                    return this.fallbackBehavior.dispense(pointer, stack);
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                World world = pointer.getWorld();
                if (bucket.placeLiquid(null, world, blockPos, null)) {
                    return new ItemStack(SKYROOT_BUCKET);
                } else {
                    return this.fallbackBehavior.dispense(pointer, stack);
                }
            }
        };

        DispenserBlock.registerBehavior(SKYROOT_WATER_BUCKET, skyrootBucketBehavior);
        DispenserBlock.registerBehavior(SKYROOT_BUCKET, new ItemDispenserBehavior() {
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                if (!(stack.getItem() instanceof SkyrootBucketItem bucket))
                    return this.fallbackBehavior.dispense(pointer, stack);
                WorldAccess worldAccess = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                BlockState blockState = worldAccess.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (block instanceof FluidDrainable && blockState.getFluidState().getFluid() == Fluids.WATER) {
                    ItemStack itemStack = ((FluidDrainable) block).tryDrainFluid(worldAccess, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.dispenseSilently(pointer, stack);
                    } else {
                        worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                        stack.decrement(1);
                        if (stack.isEmpty()) {
                            return new ItemStack(SKYROOT_WATER_BUCKET);
                        } else {
                            if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(SKYROOT_WATER_BUCKET)) < 0) {
                                this.fallbackBehavior.dispense(pointer, new ItemStack(SKYROOT_WATER_BUCKET));
                            }
                            return stack;
                        }
                    }
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
    }

    public static void init() {
        RegistryQueue.ITEM.register();
    }

    @SafeVarargs
    private static <V extends Item> V add(String id, V item, Action<? super V>... additionalActions) {
        return RegistryQueue.ITEM.add(locate(id), item, additionalActions);
    }

    @SafeVarargs
    private static BlockItem add(String id, Block block, Settings settings, Action<? super BlockItem>... additionalActions) {
        return add(id, new BlockItem(block, settings), additionalActions);
    }

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
}
