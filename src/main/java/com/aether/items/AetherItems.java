package com.aether.items;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.fluids.AetherFluids;
import com.aether.items.accessories.AccessoryItem;
import com.aether.items.accessories.AccessoryType;
import com.aether.items.armor.AetherArmorMaterials;
import com.aether.items.food.*;
import com.aether.items.resources.AmbrosiumShard;
import com.aether.items.resources.StackableVariantColorizer;
import com.aether.items.staff.CloudStaff;
import com.aether.items.staff.NatureStaff;
import com.aether.items.tools.*;
import com.aether.items.tools.AetherToolMaterials;
import com.aether.items.weapons.*;
import com.aether.registry.RegistryQueue;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.aether.Aether.locate;
import static com.aether.items.AetherItemGroups.*;
import static com.aether.util.item.AetherRarity.*;
import static net.minecraft.entity.EquipmentSlot.*;
import static net.minecraft.util.Rarity.*;

@SuppressWarnings({"x", "InstantiationOfUtilityClass", "AccessStaticViaInstance"})
public class AetherItems {
    private static final RegistryQueue<Item> queue = new RegistryQueue<>(Registry.ITEM, 256);

    // `import AetherToolMaterials as ToolMat` when?
    private static final AetherToolMaterials ToolMat = new AetherToolMaterials();
    private static final AetherArmorMaterials ArmorMat = new AetherArmorMaterials();
    private static final AetherFoodComponent FoodComp = new AetherFoodComponent();
    private static final AetherBlocks B = new AetherBlocks();

    private static Consumer<Item> compostable(float chance) { return item -> ComposterBlock.registerCompostableItem(chance, item); }
    private static final Consumer<Item> compostable30 = compostable(0.3f);
    private static final Consumer<Item> compostable50 = compostable(0.5f);
    private static final Consumer<Item> compostable65 = compostable(0.65f);
    private static final Consumer<Item> swetColor = new StackableVariantColorizer(0xDADADA, 0xDADADA, 0xDADADA)::register;


    /*
    Begin items
     */

    private static Settings resource() { return new Settings().group(AETHER_RESOURCES); }
    public static final Item ZANITE_GEM = add("zanite_gemstone", new Item(resource()));
    public static final Item ZANITE_FRAGMENT = add("zanite_fragment", new Item(resource()));
    public static final Item GRAVITITE_GEM = add("gravitite_gemstone", new Item(resource()));
    public static final Item AMBROSIUM_SHARD = add("ambrosium_shard", new AmbrosiumShard(resource()));
    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource()));
    public static final Item AECHOR_PETAL = add("aechor_petal", new Item(resource()), compostable65);
    public static final Item SWET_BALL = add("swet_ball", new Item(resource()), swetColor);
    public static final Item GOLD_AERDUST = add("gold_aerdust", new Item(resource()));
    public static final Item FROZEN_AERDUST = add("frozen_aerdust", new Item(resource()));


    private static Settings tool() { return new Settings().group(AETHER_TOOLS); }
    private static Settings tool(Rarity rarity) { return tool().rarity(rarity); }
    public static final Item ZANITE_SHOVEL = add("zanite_shovel", new ZaniteShovelItem(ToolMat.ZANITE, 1.5f, -3f, tool()));
    public static final Item ZANITE_PICKAXE = add("zanite_pickaxe", new ZanitePickaxeItem(ToolMat.ZANITE, 1, -2.8f, tool()));
    public static final Item ZANITE_AXE = add("zanite_axe", new ZaniteAxeItem(ToolMat.ZANITE, 6f, -3.1f, tool()));
    public static final Item ZANITE_SWORD = add("zanite_sword", new ZaniteSwordItem(ToolMat.ZANITE, 3, -2.4f, tool()));
    public static final Item ZANITE_HOE = add("zanite_hoe", new ZaniteHoeItem(ToolMat.ZANITE, 1, 3f, tool()));

    public static final Item GRAVITITE_SHOVEL = add("gravitite_shovel", new GravititeShovelItem(ToolMat.GRAVITITE, 1.5f, -3f, tool(RARE)));
    public static final Item GRAVITITE_PICKAXE = add("gravitite_pickaxe", new GravititePickaxeItem(ToolMat.GRAVITITE, 1, -2.8f, tool(RARE)));
    public static final Item GRAVITITE_AXE = add("gravitite_axe", new GravititeAxeItem(ToolMat.GRAVITITE, 5f, -3f, tool(RARE)));
    public static final Item GRAVITITE_SWORD = add("gravitite_sword", new SwordItem(ToolMat.GRAVITITE, 3, -2.4f, tool(RARE)));
    public static final Item GRAVITITE_HOE = add("gravitite_hoe", new GravititeHoeItem(ToolMat.GRAVITITE, 1,4f, tool(RARE)));

    public static final Item VALKYRIE_SHOVEL = add("valkyrie_shovel", new ShovelItem(ToolMat.VALKYRIE, 1.5f, -3f,  tool(AETHER_LOOT)));
    public static final Item VALKYRIE_PICKAXE = add("valkyrie_pickaxe", new AetherPickaxeItem(ToolMat.VALKYRIE, 1, -2.8f, tool(AETHER_LOOT)));
    public static final Item VALKYRIE_AXE = add("valkyrie_axe", new AetherAxeItem(ToolMat.VALKYRIE, 4f, -2.9f, tool(AETHER_LOOT)));
    public static final Item VALKYRIE_LANCE = add("valkyrie_lance", new ValkyrieLance(ToolMat.VALKYRIE, 10, -3f, 6f, 4f, tool(EPIC)));
    public static final Item VALKYRIE_HOE = add("valkyrie_hoe", new AetherHoeItem(ToolMat.VALKYRIE, 1, 5f, tool(AETHER_LOOT)));

    public static final Item GOLDEN_DART = add("golden_dart", new Dart(tool()));
    public static final Item ENCHANTED_DART = add("enchanted_dart", new Dart(tool(RARE)));
    public static final Item POISON_DART = add("poison_dart", new Dart(tool()));
    public static final Item GOLDEN_DART_SHOOTER = add("golden_dart_shooter", new DartShooter((Dart) GOLDEN_DART, tool().maxCount(1)));
    public static final Item ENCHANTED_DART_SHOOTER = add("enchanted_dart_shooter", new DartShooter((Dart) ENCHANTED_DART, tool(RARE).maxCount(1)));
    public static final Item POISON_DART_SHOOTER = add("poison_dart_shooter", new DartShooter((Dart) POISON_DART, tool().maxCount(1)));

    public static final Item PHOENIX_BOW = add("phoenix_bow", new BowItem(tool().maxDamage(384)));
    public static final Item FLAMING_SWORD = add("flaming_sword", new FlamingSwordItem(ToolMat.LEGENDARY, 4, -2.4f, tool(AETHER_LOOT)));
    public static final Item LIGHTNING_SWORD = add("lightning_sword", new LightningSwordItem(ToolMat.LEGENDARY, 4, -2.4f, tool(AETHER_LOOT)));
    public static final Item HOLY_SWORD = add("holy_sword", new HolySwordItem(ToolMat.LEGENDARY, 4, -2.4f, tool(AETHER_LOOT)));
    public static final Item VAMPIRE_BLADE = add("vampire_blade", new VampireBlade(ToolMat.LEGENDARY, 3, -2.4f, tool(AETHER_LOOT)));
    public static final Item PIG_SLAYER = add("pig_slayer", new PigSlayer(ToolMat.LEGENDARY, 3, -2.4f, tool(AETHER_LOOT)));
    public static final Item CANDY_CANE_SWORD = add("candy_cane_sword", new CandyCaneSwordItem(ToolMat.CANDY, 3, -2f, tool()));

    public static final Item CLOUD_PARACHUTE = add("cold_parachute", new ParachuteItem(tool().maxCount(1)));
    public static final Item GOLDEN_CLOUD_PARACHUTE = add("golden_parachute", new ParachuteItem(tool().maxCount(1).maxDamage(20)));

    public static final Item AMBROSIUM_BLOODSTONE = add("ambrosium_bloodstone", new AmbrosiumBloodstoneItem(tool().maxCount(1)));
    public static final Item ZANITE_BLOODSTONE = add("zanite_bloodstone", new ZaniteBloodstoneItem(tool().maxCount(1)));
    public static final Item GRAVITITE_BLOODSTONE = add("gravitite_bloodstone", new GravititeBloodstoneItem(tool().maxCount(1)));
    public static final Item ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(tool().maxCount(1)));


    private static Settings wearble() { return new Settings().group(AETHER_WEARABLES); }
    private static Settings wearble(Rarity rarity) { return wearble().rarity(rarity); }
    public static final Item ZANITE_HELMET = add("zanite_helmet", new ArmorItem(ArmorMat.ZANITE, HEAD, wearble()));
    public static final Item ZANITE_CHESTPLATE = add("zanite_chestplate", new ArmorItem(ArmorMat.ZANITE, CHEST, wearble()));
    public static final Item ZANITE_LEGGINGS = add("zanite_leggings", new ArmorItem(ArmorMat.ZANITE, LEGS, wearble()));
    public static final Item ZANITE_BOOTS = add("zanite_boots", new ArmorItem(ArmorMat.ZANITE, FEET, wearble()));

    public static final Item GRAVITITE_HELMET = add("gravitite_helmet", new ArmorItem(ArmorMat.GRAVITITE, HEAD, wearble(RARE)));
    public static final Item GRAVITITE_CHESTPLATE = add("gravitite_chestplate", new ArmorItem(ArmorMat.GRAVITITE, CHEST, wearble(RARE)));
    public static final Item GRAVITITE_LEGGINGS = add("gravitite_leggings", new ArmorItem(ArmorMat.GRAVITITE, LEGS, wearble(RARE)));
    public static final Item GRAVITITE_BOOTS = add("gravitite_boots", new ArmorItem(ArmorMat.GRAVITITE, FEET, wearble(RARE)));

    public static final Item NEPTUNE_HELMET = add("neptune_helmet", new ArmorItem(ArmorMat.NEPTUNE, HEAD, wearble(AETHER_LOOT)));
    public static final Item NEPTUNE_CHESTPLATE = add("neptune_chestplate", new ArmorItem(ArmorMat.NEPTUNE, CHEST, wearble(AETHER_LOOT)));
    public static final Item NEPTUNE_LEGGINGS = add("neptune_leggings", new ArmorItem(ArmorMat.NEPTUNE, LEGS, wearble(AETHER_LOOT)));
    public static final Item NEPTUNE_BOOTS = add("neptune_boots", new ArmorItem(ArmorMat.NEPTUNE, FEET, wearble(AETHER_LOOT)));

    public static final Item PHOENIX_HELMET = add("phoenix_helmet", new ArmorItem(ArmorMat.PHOENIX, HEAD, wearble(AETHER_LOOT)));
    public static final Item PHOENIX_CHESTPLATE = add("phoenix_chestplate", new ArmorItem(ArmorMat.PHOENIX, CHEST, wearble(AETHER_LOOT)));
    public static final Item PHOENIX_LEGGINGS = add("phoenix_leggings", new ArmorItem(ArmorMat.PHOENIX, LEGS, wearble(AETHER_LOOT)));
    public static final Item PHOENIX_BOOTS = add("phoenix_boots", new ArmorItem(ArmorMat.PHOENIX, FEET, wearble(AETHER_LOOT)));

    public static final Item OBSIDIAN_HELMET = add("obsidian_helmet", new ArmorItem(ArmorMat.OBSIDIAN, HEAD, wearble(AETHER_LOOT)));
    public static final Item OBSIDIAN_CHESTPLATE = add("obsidian_chestplate", new ArmorItem(ArmorMat.OBSIDIAN, CHEST, wearble(AETHER_LOOT)));
    public static final Item OBSIDIAN_LEGGINGS = add("obsidian_leggings", new ArmorItem(ArmorMat.OBSIDIAN, LEGS, wearble(AETHER_LOOT)));
    public static final Item OBSIDIAN_BOOTS = add("obsidian_boots", new ArmorItem(ArmorMat.OBSIDIAN, FEET, wearble(AETHER_LOOT)));

    public static final Item VALKYRIE_HELMET = add("valkyrie_helmet", new ArmorItem(ArmorMat.VALKYRIE, HEAD, wearble(AETHER_LOOT)));
    public static final Item VALKYRIE_CHESTPLATE = add("valkyrie_chestplate", new ArmorItem(ArmorMat.VALKYRIE, CHEST, wearble(AETHER_LOOT)));
    public static final Item VALKYRIE_LEGGINGS = add("valkyrie_leggings", new ArmorItem(ArmorMat.VALKYRIE, LEGS, wearble(AETHER_LOOT)));
    public static final Item VALKYRIE_BOOTS = add("valkyrie_boots", new ArmorItem(ArmorMat.VALKYRIE, FEET, wearble(AETHER_LOOT)));

    public static final Item SENTRY_BOOTS = add("sentry_boots", new ArmorItem(ArmorMat.SENTRY, FEET, wearble(AETHER_LOOT)));

    public static final Item LEATHER_GLOVES = add("leather_gloves", new AccessoryItem(AccessoryType.GLOVES, 1.5f, wearble()));
    public static final Item IRON_GLOVES = add("iron_gloves", new AccessoryItem(AccessoryType.GLOVES, 2.5f, wearble()));
    public static final Item GOLDEN_GLOVES = add("golden_gloves", new AccessoryItem(AccessoryType.GLOVES, 2f, wearble()));
    public static final Item CHAIN_GLOVES = add("chain_gloves", new AccessoryItem(AccessoryType.GLOVES, "chain", 2f, wearble()));
    public static final Item DIAMOND_GLOVES = add("diamond_gloves", new AccessoryItem(AccessoryType.GLOVES, 4.5f, wearble()));
    public static final Item ZANITE_GLOVES = add("zanite_gloves", new AccessoryItem(AccessoryType.GLOVES, 3.0f, wearble()));
    public static final Item GRAVITITE_GLOVES = add("gravitite_gloves", new AccessoryItem(AccessoryType.GLOVES, 4f, wearble(RARE)));
    public static final Item NEPTUNE_GLOVES = add("neptune_gloves", new AccessoryItem(AccessoryType.GLOVES, 4.5f, wearble(AETHER_LOOT)));
    public static final Item PHOENIX_GLOVES = add("phoenix_gloves", new AccessoryItem(AccessoryType.GLOVES, "phoenix", 4f, wearble(AETHER_LOOT)));
    public static final Item OBSIDIAN_GLOVES = add("obsidian_gloves", new AccessoryItem(AccessoryType.GLOVES, 5f, wearble()));
    public static final Item VALKYRIE_GLOVES = add("valkyrie_gloves", new AccessoryItem(AccessoryType.GLOVES, "valkyrie", 5f, wearble(AETHER_LOOT)));

    public static final Item IRON_RING = add("iron_ring", new AccessoryItem(AccessoryType.RING, wearble()));
    public static final Item GOLDEN_RING = add("golden_ring", new AccessoryItem(AccessoryType.RING, wearble()));
    public static final Item ZANITE_RING = add("zanite_ring", new AccessoryItem(AccessoryType.RING, wearble()));
    public static final Item ICE_RING = add("ice_ring", new AccessoryItem(AccessoryType.RING, wearble(RARE)));

    public static final Item GOLDEN_PENDANT = add("golden_pendant", new AccessoryItem(AccessoryType.PENDANT, wearble()));
    public static final Item ZANITE_PENDANT = add("zanite_pendant", new AccessoryItem(AccessoryType.PENDANT, wearble()));
    public static final Item ICE_PENDANT = add("ice_pendant", new AccessoryItem(AccessoryType.PENDANT, wearble(RARE)));

    public static final Item WHITE_CAPE = add("white_cape", new AccessoryItem(AccessoryType.CAPE, wearble()));
    public static final Item RED_CAPE = add("red_cape", new AccessoryItem(AccessoryType.CAPE, wearble()));
    public static final Item BLUE_CAPE = add("blue_cape", new AccessoryItem(AccessoryType.CAPE, wearble()));
    public static final Item YELLOW_CAPE = add("yellow_cape", new AccessoryItem(AccessoryType.CAPE, wearble()));
    public static final Item SWET_CAPE = add("swet_cape", new AccessoryItem(AccessoryType.CAPE, "swet", wearble(AETHER_LOOT)));
    public static final Item AGILITY_CAPE = add("agility_cape", new AccessoryItem(AccessoryType.CAPE, "agility", wearble(AETHER_LOOT)));
    public static final Item INVISIBILITY_CAPE = add("invisibility_cape", new AccessoryItem(AccessoryType.CAPE, wearble(AETHER_LOOT)));

    public static final Item GOLDEN_FEATHER = add("golden_feather", new AccessoryItem(AccessoryType.MISC, wearble(AETHER_LOOT)));
    public static final Item REGENERATION_STONE = add("regeneration_stone", new AccessoryItem(AccessoryType.MISC, wearble(AETHER_LOOT)));
    public static final Item IRON_BUBBLE = add("iron_bubble", new AccessoryItem(AccessoryType.MISC, wearble(AETHER_LOOT)));


    private static Settings food(FoodComponent foodComponent) { return new Settings().group(AETHER_FOOD).food(foodComponent); }
    private static Settings food(FoodComponent foodComponent, Rarity rarity) { return food(foodComponent).rarity(rarity); }
    public static final Item BLUEBERRY = add("blue_berry", new AliasedBlockItem(AetherBlocks.BLUEBERRY_BUSH, food(FoodComp.BLUEBERRY)), compostable30);
    public static final Item ENCHANTED_BLUEBERRY = add("enchanted_blueberry", new Item(food(FoodComp.ENCHANTED_BLUEBERRY, RARE)), compostable50);
    public static final Item ORANGE = add("orange", new Item(food(FoodComp.ORANGE)), compostable65);
    public static final Item WHITE_APPLE = add("white_apple", new WhiteApple(food(FoodComp.WHITE_APPLE)), compostable(0f));
    public static final Item BLUE_GUMMY_SWET = add("blue_gummy_swet", new Item(food(FoodComp.GUMMY_SWET, AETHER_LOOT)));
    public static final Item GOLDEN_GUMMY_SWET = add("golden_gummy_swet", new Item(food(FoodComp.GUMMY_SWET, AETHER_LOOT)));
    public static final Item VALKYRIE_MILK = add("valkyrie_milk", new ValkyrieMilkItem(food(FoodComp.VALKYRIE_MILK, EPIC).maxCount(1)));
    public static final Item HEALING_STONE = add("healing_stone", new HealingStone(food(FoodComp.HEALING_STONE, RARE)));
    public static final Item CANDY_CANE = add("candy_cane", new Item(food(FoodComp.GENERIC)), compostable30);
    public static final Item GINGERBREAD_MAN = add("ginger_bread_man", new Item(food(FoodComp.GENERIC)), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", new Item(food(FoodComp.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", new Item(food(FoodComp.COOKED_MOA_MEAT)));


    private static Settings misc() { return new Settings().group(AETHER_MISC); }
    public static final Item LIFE_SHARD = add("life_shard", new LifeShard(misc().rarity(AETHER_LOOT).maxCount(1)));
    public static final Item CLOUD_STAFF = add("cloud_staff", new CloudStaff(misc().maxCount(1).maxDamage(60)));
    public static final Item NATURE_STAFF = add("nature_staff", new NatureStaff(misc().maxCount(1).maxDamage(100)));
    public static final Item MOA_EGG = add("moa_egg", new MoaEgg(misc().maxCount(1)));
    public static final Item LORE_BOOK = add("lore_book", new BookOfLore(misc().maxCount(1)));
    public static final Item SKYROOT_BUCKET = add("skyroot_bucket", new SkyrootBucketItem(misc().maxCount(16)));

    private static Settings skyrootBucket() { return misc().maxCount(1).recipeRemainder(SKYROOT_BUCKET); }
    public static final Item SKYROOT_WATER_BUCKET = add("skyroot_water_bucket", new SkyrootBucketItem(Fluids.WATER, skyrootBucket()));
    public static final Item SKYROOT_MILK_BUCKET = add("skyroot_milk_bucket", new SkyrootBucketItem(skyrootBucket()));
    public static final Item SKYROOT_POISON_BUCKET = add("skyroot_poison_bucket", new SkyrootBucketItem(skyrootBucket()));
    public static final Item SKYROOT_REMEDY_BUCKET = add("skyroot_remedy_bucket", new SkyrootBucketItem(skyrootBucket()));

    public static final Item QUICKSOIL_VIAL = add("quicksoil_vial", new VialItem(Fluids.EMPTY, misc()));
    public static final Item AERCLOUD_VIAL = add("aercloud_vial", new VialItem(AetherFluids.DENSE_AERCLOUD, misc()));

    public static final Item BRONZE_KEY = add("bronze_key", new Item(misc().rarity(AETHER_LOOT)));
    public static final Item SILVER_KEY = add("silver_key", new Item(misc().rarity(AETHER_LOOT)));
    public static final Item GOLDEN_KEY = add("golden_key", new Item(misc().rarity(AETHER_LOOT)));

    public static final Item AETHER_PORTAL = add("aether_portal", new AetherPortalItem(misc()));
    public static final Item INCUBATOR = add(B.INCUBATOR, misc(), block -> {});
    public static final Item FOOD_BOWL = add(B.FOOD_BOWL, misc());

    public static final Item AECHOR_PLANT_SPAWN_EGG = add("aechor_plant_spawn_egg", new SpawnEggItem(AetherEntityTypes.AECHOR_PLANT, 0x97ded4, 0x31897d, misc()));
    public static final Item CHEST_MIMIC_SPAWN_EGG = null;
    public static final Item COCKATRICE_SPAWN_EGG = add("cockatrice_spawn_egg", new SpawnEggItem(AetherEntityTypes.COCKATRICE, 0x9fc3f7, 0x3d2338, misc()));
    public static final Item AERBUNNY_SPAWN_EGG = add("aerbunny_spawn_egg", new SpawnEggItem(AetherEntityTypes.AERBUNNY, 0xc5d6ed, 0x82a6d9, misc()));
    public static final Item AERWHALE_SPAWN_EGG = add("aerwhale_spawn_egg", new SpawnEggItem(AetherEntityTypes.AERWHALE, 0x5c6d91, 0xdedbce, misc()));
    public static final Item FLYING_COW_SPAWN_EGG = null;
    public static final Item MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(AetherEntityTypes.MOA, 0xc55c2e4, 0xb3a8bb, misc()));
    public static final Item SWET_SPAWN_EGG = add("swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.WHITE_SWET, 0x8f9294, 0xe6eaeb, misc()));
    public static final Item BLUE_SWET_SPAWN_EGG = add("blue_swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.BLUE_SWET, 0x46699e, 0xe6eaeb, misc()));
    public static final Item PURPLE_SWET_SPAWN_EGG = add("purple_swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.PURPLE_SWET, 0x5d548c, 0xe6eaeb, misc()));
    public static final Item GOLDEN_SWET_SPAWN_EGG = add("golden_swet_spawn_egg", new SpawnEggItem(AetherEntityTypes.GOLDEN_SWET, 0xc99d36, 0xe6eaeb, misc()));
    public static final Item PHYG_SPAWN_EGG = null;
    public static final Item SHEEPUFF_SPAWN_EGG = null;

    public static final Item SWET_DROP = add(B.SWET_DROP, misc());
    public static final Item BLUE_SWET_DROP = add(B.BLUE_SWET_DROP, misc());
    public static final Item GOLDEN_SWET_DROP = add(B.GOLDEN_SWET_DROP, misc());
    public static final Item PURPLE_SWET_DROP = add(B.PURPLE_SWET_DROP, misc());


    private static Settings block() { return new Settings().group(AETHER_BLOCKS); }
    public static final Item AETHER_GRASS_BLOCK = add(B.AETHER_GRASS_BLOCK, block());
    public static final Item AETHER_ENCHANTED_GRASS = add(B.AETHER_ENCHANTED_GRASS, block());

    public static final Item AETHER_DIRT = add(B.AETHER_DIRT, block());
    public static final Item AETHER_FARMLAND = add(B.AETHER_FARMLAND, block());
    public static final Item AETHER_DIRT_PATH = add(B.AETHER_DIRT_PATH, block());
    public static final Item QUICKSOIL = add(B.QUICKSOIL, block());

    public static final Item QUICKSOIL_GLASS = add(B.QUICKSOIL_GLASS, block());
    public static final Item QUICKSOIL_GLASS_PANE = add(B.QUICKSOIL_GLASS_PANE, block());

    public static final Item ICESTONE = add(B.ICESTONE, block());
    public static final Item AEROGEL = add(B.AEROGEL, block());

    public static final Item COLD_AERCLOUD = add(B.COLD_AERCLOUD, block());
    public static final Item BLUE_AERCLOUD = add(B.BLUE_AERCLOUD, block());
    public static final Item PINK_AERCLOUD = add(B.PINK_AERCLOUD, block());
    public static final Item GOLDEN_AERCLOUD = add(B.GOLDEN_AERCLOUD, block());

    public static final Item HOLYSTONE = add(B.HOLYSTONE, block());
    public static final Item HOLYSTONE_SLAB = add(B.HOLYSTONE_SLAB, block());
    public static final Item HOLYSTONE_STAIRS = add(B.HOLYSTONE_STAIRS, block());
    public static final Item HOLYSTONE_WALL = add(B.HOLYSTONE_WALL, block());

    public static final Item COBBLED_HOLYSTONE = add(B.COBBLED_HOLYSTONE, block());
    public static final Item COBBLED_HOLYSTONE_SLAB = add(B.COBBLED_HOLYSTONE_SLAB, block());
    public static final Item COBBLED_HOLYSTONE_STAIRS = add(B.COBBLED_HOLYSTONE_STAIRS, block());
    public static final Item COBBLED_HOLYSTONE_WALL = add(B.COBBLED_HOLYSTONE_WALL, block());

    public static final Item MOSSY_HOLYSTONE = add(B.MOSSY_HOLYSTONE, block());
    public static final Item GOLDEN_MOSSY_HOLYSTONE = add(B.GOLDEN_MOSSY_HOLYSTONE, block());
    public static final Item MOSSY_HOLYSTONE_SLAB = add(B.MOSSY_HOLYSTONE_SLAB, block());
    public static final Item MOSSY_HOLYSTONE_STAIRS = add(B.MOSSY_HOLYSTONE_STAIRS, block());
    public static final Item MOSSY_HOLYSTONE_WALL = add(B.MOSSY_HOLYSTONE_WALL, block());

    public static final Item HOLYSTONE_BRICK = add(B.HOLYSTONE_BRICK, block());
    public static final Item HOLYSTONE_BRICK_SLAB = add(B.HOLYSTONE_BRICK_SLAB, block());
    public static final Item HOLYSTONE_BRICK_STAIRS = add(B.HOLYSTONE_BRICK_STAIRS, block());
    public static final Item HOLYSTONE_BRICK_WALL = add(B.HOLYSTONE_BRICK_WALL, block());

    public static final Item ANGELIC_STONE = add(B.ANGELIC_STONE, block());
    public static final Item ANGELIC_CRACKED_STONE = add(B.ANGELIC_CRACKED_STONE, block());
    public static final Item ANGELIC_SLAB = add(B.ANGELIC_SLAB, block());
    public static final Item ANGELIC_STAIRS = add(B.ANGELIC_STAIRS, block());
    public static final Item ANGELIC_WALL = add(B.ANGELIC_WALL, block());

    public static final Item LIGHT_ANGELIC_STONE = add(B.LIGHT_ANGELIC_STONE, block());
    public static final Item LIGHT_ANGELIC_STONE_TRAP = add(B.LIGHT_ANGELIC_STONE_TRAP, block());
    public static final Item LIGHT_ANGELIC_SLAB = add(B.LIGHT_ANGELIC_SLAB, block());
    public static final Item LIGHT_ANGELIC_STAIRS = add(B.LIGHT_ANGELIC_STAIRS, block());
    public static final Item LIGHT_ANGELIC_WALL = add(B.LIGHT_ANGELIC_WALL, block());

    public static final Item HELLFIRE_STONE = add(B.HELLFIRE_STONE, block());
    public static final Item HELLFIRE_CRACKED_STONE = add(B.HELLFIRE_CRACKED_STONE, block());
    public static final Item HELLFIRE_STONE_TRAP = add(B.HELLFIRE_STONE_TRAP, block());
    public static final Item HELLFIRE_SLAB = add(B.HELLFIRE_SLAB, block());
    public static final Item HELLFIRE_STAIRS = add(B.HELLFIRE_STAIRS, block());
    public static final Item HELLFIRE_WALL = add(B.HELLFIRE_WALL, block());

    public static final Item LIGHT_HELLFIRE_STONE = add(B.LIGHT_HELLFIRE_STONE, block());
    public static final Item LIGHT_HELLFIRE_STONE_TRAP = add(B.LIGHT_HELLFIRE_STONE_TRAP, block());
    public static final Item LIGHT_HELLFIRE_SLAB = add(B.LIGHT_HELLFIRE_SLAB, block());
    public static final Item LIGHT_HELLFIRE_STAIRS = add(B.LIGHT_HELLFIRE_STAIRS, block());
    public static final Item LIGHT_HELLFIRE_WALL = add(B.LIGHT_HELLFIRE_WALL, block());

    public static final Item CARVED_STONE = add(B.CARVED_STONE, block());
    public static final Item CARVED_STONE_TRAP = add(B.CARVED_STONE_TRAP, block());
    public static final Item CARVED_SLAB = add(B.CARVED_SLAB, block());
    public static final Item CARVED_STAIRS = add(B.CARVED_STAIRS, block());
    public static final Item CARVED_WALL = add(B.CARVED_WALL, block());

    public static final Item LIGHT_CARVED_STONE = add(B.LIGHT_CARVED_STONE, block());
    public static final Item LIGHT_CARVED_STONE_TRAP = add(B.LIGHT_CARVED_STONE_TRAP, block());
    public static final Item LIGHT_CARVED_SLAB = add(B.LIGHT_CARVED_SLAB, block());
    public static final Item LIGHT_CARVED_STAIRS = add(B.LIGHT_CARVED_STAIRS, block());
    public static final Item LIGHT_CARVED_WALL = add(B.LIGHT_CARVED_WALL, block());

    public static final Item SENTRY_STONE = add(B.SENTRY_STONE, block());
    public static final Item SENTRY_CRACKED_STONE = add(B.SENTRY_CRACKED_STONE, block());
    public static final Item SENTRY_STONE_TRAP = add(B.SENTRY_STONE_TRAP, block());
    public static final Item SENTRY_SLAB = add(B.SENTRY_SLAB, block());
    public static final Item SENTRY_STAIRS = add(B.SENTRY_STAIRS, block());
    public static final Item SENTRY_WALL = add(B.SENTRY_WALL, block());

    public static final Item LIGHT_SENTRY_STONE = add(B.LIGHT_SENTRY_STONE, block());
    public static final Item LIGHT_SENTRY_SLAB = add(B.LIGHT_SENTRY_SLAB, block());
    public static final Item LIGHT_SENTRY_STAIRS = add(B.LIGHT_SENTRY_STAIRS, block());
    public static final Item LIGHT_SENTRY_WALL = add(B.LIGHT_SENTRY_WALL, block());

    public static final Item SKYROOT_SAPLING = add(B.SKYROOT_SAPLING, block(), compostable30);
    public static final Item SKYROOT_LOG = add(B.SKYROOT_LOG, block());
    public static final Item SKYROOT_WOOD = add(B.SKYROOT_WOOD, block());
    public static final Item STRIPPED_SKYROOT_LOG = add(B.STRIPPED_SKYROOT_LOG, block());
    public static final Item STRIPPED_SKYROOT_WOOD = add(B.STRIPPED_SKYROOT_WOOD, block());
    public static final Item SKYROOT_LEAVES = add(B.SKYROOT_LEAVES, block(), compostable30);
    public static final Item SKYROOT_LEAF_PILE = add(B.SKYROOT_LEAF_PILE, block(), compostable30);
    public static final Item SKYROOT_PLANKS = add(B.SKYROOT_PLANKS, block());
    public static final Item SKYROOT_BOOKSHELF = add(B.SKYROOT_BOOKSHELF, block());
    public static final Item SKYROOT_FENCE = add(B.SKYROOT_FENCE, block());
    public static final Item SKYROOT_FENCE_GATE = add(B.SKYROOT_FENCE_GATE, block());
    public static final Item SKYROOT_SLAB = add(B.SKYROOT_SLAB, block());
    public static final Item SKYROOT_STAIRS = add(B.SKYROOT_STAIRS, block());
    public static final Item SKYROOT_TRAPDOOR = add(B.SKYROOT_TRAPDOOR, block());
    public static final Item SKYROOT_DOOR = add(B.SKYROOT_DOOR, block());
    public static final Item SKYROOT_BUTTON = add(B.SKYROOT_BUTTON, block());
    public static final Item SKYROOT_PRESSURE_PLATE = add(B.SKYROOT_PRESSURE_PLATE, block());

    public static final Item GOLDEN_OAK_SAPLING = add(B.GOLDEN_OAK_SAPLING, block(), compostable30);
    public static final Item GOLDEN_OAK_LOG = add(B.GOLDEN_OAK_LOG, block());
    public static final Item GOLDEN_OAK_WOOD = add(B.GOLDEN_OAK_WOOD, block());
    public static final Item STRIPPED_GOLDEN_OAK_LOG = add(B.STRIPPED_GOLDEN_OAK_LOG, block());
    public static final Item STRIPPED_GOLDEN_OAK_WOOD = add(B.STRIPPED_GOLDEN_OAK_WOOD, block());
    public static final Item GOLDEN_OAK_LEAVES = add(B.GOLDEN_OAK_LEAVES, block(), compostable30);
    public static final Item GOLDEN_OAK_PLANKS = add(B.GOLDEN_OAK_PLANKS, block());
    public static final Item GOLDEN_OAK_FENCE = add(B.GOLDEN_OAK_FENCE, block());
    public static final Item GOLDEN_OAK_FENCE_GATE = add(B.GOLDEN_OAK_FENCE_GATE, block());
    public static final Item GOLDEN_OAK_SLAB = add(B.GOLDEN_OAK_SLAB, block());
    public static final Item GOLDEN_OAK_STAIRS = add(B.GOLDEN_OAK_STAIRS, block());
    public static final Item GOLDEN_OAK_TRAPDOOR = add(B.GOLDEN_OAK_TRAPDOOR, block());
    public static final Item GOLDEN_OAK_DOOR = add(B.GOLDEN_OAK_DOOR, block());
    public static final Item GOLDEN_OAK_BUTTON = add(B.GOLDEN_OAK_BUTTON, block());
    public static final Item GOLDEN_OAK_PRESSURE_PLATE = add(B.GOLDEN_OAK_PRESSURE_PLATE, block());

    public static final Item ORANGE_SAPLING = add(B.ORANGE_SAPLING, block(), compostable30);
    public static final Item ORANGE_LOG = add(B.ORANGE_LOG, block());
    public static final Item ORANGE_WOOD = add(B.ORANGE_WOOD, block());
    public static final Item STRIPPED_ORANGE_LOG = add(B.STRIPPED_ORANGE_LOG, block());
    public static final Item STRIPPED_ORANGE_WOOD = add(B.STRIPPED_ORANGE_WOOD, block());
    public static final Item ORANGE_LEAVES = add(B.ORANGE_LEAVES, block(), compostable30);
    public static final Item ORANGE_PLANKS = add(B.ORANGE_PLANKS, block());
    public static final Item ORANGE_FENCE = add(B.ORANGE_FENCE, block());
    public static final Item ORANGE_FENCE_GATE = add(B.ORANGE_FENCE_GATE, block());
    public static final Item ORANGE_SLAB = add(B.ORANGE_SLAB, block());
    public static final Item ORANGE_STAIRS = add(B.ORANGE_STAIRS, block());
    public static final Item ORANGE_TRAPDOOR = add(B.ORANGE_TRAPDOOR, block());
    public static final Item ORANGE_DOOR = add(B.ORANGE_DOOR, block());
    public static final Item ORANGE_BUTTON = add(B.ORANGE_BUTTON, block());
    public static final Item ORANGE_PRESSURE_PLATE = add(B.ORANGE_PRESSURE_PLATE, block());

    public static final Item CRYSTAL_SAPLING = add(B.CRYSTAL_SAPLING, block(), compostable50);
    public static final Item CRYSTAL_LOG = add(B.CRYSTAL_LOG, block());
    public static final Item CRYSTAL_WOOD = add(B.CRYSTAL_WOOD, block());
    public static final Item STRIPPED_CRYSTAL_LOG = add(B.STRIPPED_CRYSTAL_LOG, block());
    public static final Item STRIPPED_CRYSTAL_WOOD = add(B.STRIPPED_CRYSTAL_WOOD, block());
    public static final Item CRYSTAL_LEAVES = add(B.CRYSTAL_LEAVES, block(), compostable50);
    public static final Item CRYSTAL_PLANKS = add(B.CRYSTAL_PLANKS, block());
    public static final Item CRYSTAL_FENCE = add(B.CRYSTAL_FENCE, block());
    public static final Item CRYSTAL_FENCE_GATE = add(B.CRYSTAL_FENCE_GATE, block());
    public static final Item CRYSTAL_SLAB = add(B.CRYSTAL_SLAB, block());
    public static final Item CRYSTAL_STAIRS = add(B.CRYSTAL_STAIRS, block());
    public static final Item CRYSTAL_TRAPDOOR = add(B.CRYSTAL_TRAPDOOR, block());
    public static final Item CRYSTAL_DOOR = add(B.CRYSTAL_DOOR, block());
    public static final Item CRYSTAL_BUTTON = add(B.CRYSTAL_BUTTON, block());
    public static final Item CRYSTAL_PRESSURE_PLATE = add(B.CRYSTAL_PRESSURE_PLATE, block());

    public static final Item WISTERIA_LOG = add(B.WISTERIA_LOG, block());
    public static final Item WISTERIA_WOOD = add(B.WISTERIA_WOOD, block());
    public static final Item STRIPPED_WISTERIA_LOG = add(B.STRIPPED_WISTERIA_LOG, block());
    public static final Item STRIPPED_WISTERIA_WOOD = add(B.STRIPPED_WISTERIA_WOOD, block());
    public static final Item WISTERIA_PLANKS = add(B.WISTERIA_PLANKS, block());
    public static final Item WISTERIA_FENCE = add(B.WISTERIA_FENCE, block());
    public static final Item WISTERIA_FENCE_GATE = add(B.WISTERIA_FENCE_GATE, block());
    public static final Item WISTERIA_SLAB = add(B.WISTERIA_SLAB, block());
    public static final Item WISTERIA_STAIRS = add(B.WISTERIA_STAIRS, block());
    public static final Item WISTERIA_TRAPDOOR = add(B.WISTERIA_TRAPDOOR, block());
    public static final Item WISTERIA_DOOR = add(B.WISTERIA_DOOR, block());
    public static final Item WISTERIA_BUTTON = add(B.WISTERIA_BUTTON, block());
    public static final Item WISTERIA_PRESSURE_PLATE = add(B.WISTERIA_PRESSURE_PLATE, block());

    public static final Item ROSE_WISTERIA_LEAVES = add(B.ROSE_WISTERIA_LEAVES, block(), compostable30);
    public static final Item ROSE_WISTERIA_LEAF_PILE = add(B.ROSE_WISTERIA_LEAF_PILE, block(), compostable30);
    public static final Item ROSE_WISTERIA_SAPLING = add(B.ROSE_WISTERIA_SAPLING, block(), compostable30);
    public static final Item ROSE_WISTERIA_HANGER = add(B.ROSE_WISTERIA_HANGER, block(), compostable30);

    public static final Item FROST_WISTERIA_LEAVES = add(B.FROST_WISTERIA_LEAVES, block(), compostable30);
    public static final Item FROST_WISTERIA_LEAF_PILE = add(B.FROST_WISTERIA_LEAF_PILE, block(), compostable30);
    public static final Item FROST_WISTERIA_SAPLING = add(B.FROST_WISTERIA_SAPLING, block(), compostable30);
    public static final Item FROST_WISTERIA_HANGER = add(B.FROST_WISTERIA_HANGER, block(), compostable30);

    public static final Item LAVENDER_WISTERIA_LEAVES = add(B.LAVENDER_WISTERIA_LEAVES, block(), compostable30);
    public static final Item LAVENDER_WISTERIA_LEAF_PILE = add(B.LAVENDER_WISTERIA_LEAF_PILE, block(), compostable30);
    public static final Item LAVENDER_WISTERIA_SAPLING = add(B.LAVENDER_WISTERIA_SAPLING, block(), compostable30);
    public static final Item LAVENDER_WISTERIA_HANGER = add(B.LAVENDER_WISTERIA_HANGER, block(), compostable30);

    public static final Item BOREAL_WISTERIA_LEAVES = add(B.BOREAL_WISTERIA_LEAVES, block(), compostable30);
    public static final Item BOREAL_WISTERIA_SAPLING = add(B.BOREAL_WISTERIA_SAPLING, block(), compostable30);
    public static final Item BOREAL_WISTERIA_HANGER = add(B.BOREAL_WISTERIA_HANGER, block(), compostable30);

    public static final Item AETHER_GRASS = add(B.AETHER_GRASS, block(), compostable30);
    public static final Item AETHER_TALL_GRASS = add(B.AETHER_TALL_GRASS, block(), compostable50);
    public static final Item AETHER_FERN = add(B.AETHER_FERN, block(), compostable30);
    public static final Item AETHER_BUSH = add(B.AETHER_BUSH, block(), compostable30);
    public static final Item FLUTEGRASS = add(B.FLUTEGRASS, block(), compostable30);

    public static final Item ANCIENT_FLOWER = add(B.ANCIENT_FLOWER, block(), compostable65);
    public static final Item ATARAXIA = add(B.ATARAXIA, block(), compostable65);
    public static final Item CLOUDSBLUFF = add(B.CLOUDSBLUFF, block(), compostable65);
    public static final Item DRIGEAN = add(B.DRIGEAN, block(), compostable65);
    public static final Item LUMINAR = add(B.LUMINAR, block(), compostable65);

    public static final Item AMBROSIUM_ORE = add(B.AMBROSIUM_ORE, block());
    public static final Item ZANITE_ORE = add(B.ZANITE_ORE, block());
    public static final Item GRAVITITE_ORE = add(B.GRAVITITE_ORE, block());
    public static final Item ZANITE_BLOCK = add(B.ZANITE_BLOCK, block());
    public static final Item BLOCK_OF_GRAVITITE = add(B.BLOCK_OF_GRAVITITE, block());
    public static final Item GRAVITITE_LEVITATOR = add(B.GRAVITITE_LEVITATOR, block());
    public static final Item ZANITE_CHAIN = add(B.ZANITE_CHAIN, block());
    public static final Item AMBROSIUM_LANTERN = add(B.AMBROSIUM_LANTERN, block());
    public static final Item AMBROSIUM_TORCH = add(B.AMBROSIUM_TORCH, new WallStandingBlockItem(B.AMBROSIUM_TORCH, B.AMBROSIUM_TORCH_WALL, block()));



    static {
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
        queue.register();
    }

    @SafeVarargs
    private static Item add(String id, Item item, Consumer<Item>... additionalActions) {
        return queue.add(() -> locate(id), item, additionalActions);
    }

    @SafeVarargs
    private static Item add(Block block, Item item, Consumer<Item>... additionalActions) {
        return queue.add(() -> {
            System.out.println(block);
            return Registry.BLOCK.getId(block);
            }, item, additionalActions);
    }

    @SafeVarargs
    private static Item add(Block block, Settings settings, Consumer<Item>... additionalActions) {
        System.out.println(block + "fefe" + Arrays.toString(additionalActions));
        System.out.println(AetherBlocks.INCUBATOR);
        return add(block, new BlockItem(block, settings), additionalActions);
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
