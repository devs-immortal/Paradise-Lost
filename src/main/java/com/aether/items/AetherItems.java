package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.vehicle.AetherBoatTypes;
import com.aether.items.accessories.AccessoryType;
import com.aether.items.accessories.ItemAccessory;
import com.aether.items.armor.AetherArmorType;
import com.aether.items.food.DrinkableItem;
import com.aether.items.food.HealingStone;
import com.aether.items.food.LifeShard;
import com.aether.items.food.WhiteApple;
import com.aether.items.resources.AmbrosiumShard;
import com.aether.items.staff.CloudStaff;
import com.aether.items.staff.NatureStaff;
import com.aether.items.tools.*;
import com.aether.items.utils.AetherTiers;
import com.aether.items.weapons.*;
import com.aether.util.item.AetherRarity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

@SuppressWarnings("unused")
public class AetherItems {

    public static final AetherRarity AETHER_LOOT = new AetherRarity(Formatting.GREEN);

    // Resources
    private static final Settings RESOURCES = new Settings().group(AetherItemGroups.Resources);
    public static final Item ZANITE_GEM = register("zanite_gemstone", new Item(RESOURCES));
    public static final Item ZANITE_FRAGMENT = register("zanite_fragment", new Item(RESOURCES));
    public static final Item GRAVITITE_GEM = register("gravitite_gemstone", new Item(RESOURCES));
    public static final Item AMBROSIUM_SHARD = register("ambrosium_shard", new AmbrosiumShard(RESOURCES));
    public static final Item GOLDEN_AMBER = register("golden_amber", new Item(RESOURCES));
    public static final Item AECHOR_PETAL = register("aechor_petal", new Item(RESOURCES));
    public static final Item SWET_BALL = register("swet_ball", new Item(RESOURCES));
    //SKYROOT_STICK = register("skyroot_stick", new Item(RESOURCES));
    public static final Item GOLD_AERDUST = register("gold_aerdust", new Item(RESOURCES));
    public static final Item FROZEN_AERDUST = register("frozen_aerdust", new Item(RESOURCES));

    // Tools
    private static final Settings TOOLS = new Settings().group(AetherItemGroups.Tools);
    private static final Settings WEAPONS = new Settings().group(AetherItemGroups.Tools);
    /*SKYROOT_SHOVEL = register("skyroot_shovel", new AetherShovel(AetherTiers.Skyroot, TOOLS, 1.5F, -3.0F));
    SKYROOT_PICKAXE = register("skyroot_pickaxe", new AetherPickaxe(AetherTiers.Skyroot, TOOLS, 1, -2.8F));
    SKYROOT_AXE = register("skyroot_axe", new AetherAxe(AetherTiers.Skyroot, TOOLS, 6.0F, -3.2F));
    SKYROOT_SWORD = register("skyroot_sword", new AetherSword(AetherTiers.Skyroot, -2.4F, 3, WEAPONS));
    SKYROOT_HOE = register("skyroot_hoe", new AetherHoe(AetherTiers.Skyroot, TOOLS, 1));
    HOLYSTONE_SHOVEL = register("holystone_shovel", new AetherShovel(AetherTiers.Holystone, TOOLS,1.5F, -3.0F));
    HOLYSTONE_PICKAXE = register("holystone_pickaxe", new AetherPickaxe(AetherTiers.Holystone, TOOLS, 1, -2.8F));
    HOLYSTONE_AXE = register("holystone_axe", new AetherAxe(AetherTiers.Holystone, TOOLS, 7.0F, -3.2F));
    HOLYSTONE_SWORD = register("holystone_sword", new AetherSword(AetherTiers.Holystone, -2.4F, 3, WEAPONS));
    HOLYSTONE_HOE = register("holystone_hoe", new AetherHoe(AetherTiers.Holystone, TOOLS, 2));
    */
    public static final Item ZANITE_SHOVEL = register("zanite_shovel", new AetherShovel(AetherTiers.ZANITE, TOOLS, 1.5F, -3.0F));
    public static final Item ZANITE_PICKAXE = register("zanite_pickaxe", new AetherPickaxe(AetherTiers.ZANITE, TOOLS, 1, -2.8F));
    public static final Item ZANITE_AXE = register("zanite_axe", new AetherAxe(AetherTiers.ZANITE, TOOLS, 6.0F, -3.1F));
    public static final Item ZANITE_SWORD = register("zanite_sword", new AetherSword(AetherTiers.ZANITE, -2.4F, 3, WEAPONS));
    public static final Item ZANITE_HOE = register("zanite_hoe", new AetherHoe(AetherTiers.ZANITE, TOOLS, 3));

    private static final Settings GRAVITITE_TOOLS = new AetherItemSettings().rarity(Rarity.RARE).group(AetherItemGroups.Tools);
    public static final Item GRAVITITE_SHOVEL = register("gravitite_shovel", new AetherShovel(AetherTiers.GRAVITITE, GRAVITITE_TOOLS, 1.5F, -3.0F));
    public static final Item GRAVITITE_PICKAXE = register("gravitite_pickaxe", new AetherPickaxe(AetherTiers.GRAVITITE, GRAVITITE_TOOLS, 1, -2.8F));
    public static final Item GRAVITITE_AXE = register("gravitite_axe", new AetherAxe(AetherTiers.GRAVITITE, GRAVITITE_TOOLS, 5.0F, -3.0F));
    public static final Item GRAVITITE_SWORD = register("gravitite_sword", new AetherSword(AetherTiers.GRAVITITE, -2.4F, 3, new AetherItemSettings().rarity(Rarity.RARE).group(AetherItemGroups.Tools)));
    public static final Item GRAVITITE_HOE = register("gravitite_hoe", new AetherHoe(AetherTiers.GRAVITITE, GRAVITITE_TOOLS, 4));

    private static final Settings VALKYRIE_TOOLS = new AetherItemSettings().rarity(AETHER_LOOT).group(AetherItemGroups.Tools);
    public static final Item VALKYRIE_SHOVEL = register("valkyrie_shovel", new AetherShovel(AetherTiers.VALKYRIE, VALKYRIE_TOOLS, 1.5F, -3.0F));
    public static final Item VALKYRIE_PICKAXE = register("valkyrie_pickaxe", new AetherPickaxe(AetherTiers.VALKYRIE, VALKYRIE_TOOLS, 1, -2.8F));
    public static final Item VALKYRIE_AXE = register("valkyrie_axe", new AetherAxe(AetherTiers.VALKYRIE, VALKYRIE_TOOLS, 4.0F, -2.9F));
    public static final Item VALKYRIE_LANCE = register("valkyrie_lance", new ValkyrieLance(new AetherItemSettings().rarity(Rarity.EPIC).group(AetherItemGroups.Tools)));
    public static final Item VALKYRIE_HOE = register("valkyrie_hoe", new AetherHoe(AetherTiers.VALKYRIE, VALKYRIE_TOOLS, 5));
    public static final Item AMBROSIUM_BLOODSTONE = register("ambrosium_bloodstone", new AmbrosiumBloodstoneItem());
    public static final Item ZANITE_BLOODSTONE = register("zanite_bloodstone", new ZaniteBloodstoneItem());
    public static final Item GRAVITITE_BLOODSTONE = register("gravitite_bloodstone", new GravititeBloodstoneItem());
    public static final Item ABSTENTINE_BLOODSTONE = register("abstentine_bloodstone", new AbstentineBloodstoneItem());

    // Armor
    private static final AetherItemSettings ARMOR = new AetherItemSettings().group(AetherItemGroups.Wearable);
    public static final Item ZANITE_HELMET = register("zanite_helmet", new ArmorItem(AetherArmorType.Zanite.getMaterial(), EquipmentSlot.HEAD, ARMOR));
    public static final Item ZANITE_CHESTPLATE = register("zanite_chestplate", new ArmorItem(AetherArmorType.Zanite.getMaterial(), EquipmentSlot.CHEST, ARMOR));
    public static final Item ZANITE_LEGGINGS = register("zanite_leggings", new ArmorItem(AetherArmorType.Zanite.getMaterial(), EquipmentSlot.LEGS, ARMOR));
    public static final Item ZANITE_BOOTS = register("zanite_boots", new ArmorItem(AetherArmorType.Zanite.getMaterial(), EquipmentSlot.FEET, ARMOR));

    private static final AetherItemSettings GRAVITITE_ARMOR = new AetherItemSettings().group(AetherItemGroups.Wearable).rarity(Rarity.RARE);
    public static final Item GRAVITITE_HELMET = register("gravitite_helmet", new ArmorItem(AetherArmorType.Gravitite.getMaterial(), EquipmentSlot.HEAD, GRAVITITE_ARMOR));
    public static final Item GRAVITITE_CHESTPLATE = register("gravitite_chestplate", new ArmorItem(AetherArmorType.Gravitite.getMaterial(), EquipmentSlot.CHEST, GRAVITITE_ARMOR));
    public static final Item GRAVITITE_LEGGINGS = register("gravitite_leggings", new ArmorItem(AetherArmorType.Gravitite.getMaterial(), EquipmentSlot.LEGS, GRAVITITE_ARMOR));
    public static final Item GRAVITITE_BOOTS = register("gravitite_boots", new ArmorItem(AetherArmorType.Gravitite.getMaterial(), EquipmentSlot.FEET, GRAVITITE_ARMOR));

    private static final AetherItemSettings LOOT_ARMOR = new AetherItemSettings().rarity(AETHER_LOOT).group(AetherItemGroups.Wearable);
    public static final Item NEPTUNE_HELMET = register("neptune_helmet", new ArmorItem(AetherArmorType.Neptune.getMaterial(), EquipmentSlot.HEAD, LOOT_ARMOR));
    public static final Item NEPTUNE_CHESTPLATE = register("neptune_chestplate", new ArmorItem(AetherArmorType.Neptune.getMaterial(), EquipmentSlot.CHEST, LOOT_ARMOR));
    public static final Item NEPTUNE_LEGGINGS = register("neptune_leggings", new ArmorItem(AetherArmorType.Neptune.getMaterial(), EquipmentSlot.LEGS, LOOT_ARMOR));
    public static final Item NEPTUNE_BOOTS = register("neptune_boots", new ArmorItem(AetherArmorType.Neptune.getMaterial(), EquipmentSlot.FEET, LOOT_ARMOR));

    public static final Item PHOENIX_HELMET = register("phoenix_helmet", new ArmorItem(AetherArmorType.Phoenix.getMaterial(), EquipmentSlot.HEAD, LOOT_ARMOR));
    public static final Item PHOENIX_CHESTPLATE = register("phoenix_chestplate", new ArmorItem(AetherArmorType.Phoenix.getMaterial(), EquipmentSlot.CHEST, LOOT_ARMOR));
    public static final Item PHOENIX_LEGGINGS = register("phoenix_leggings", new ArmorItem(AetherArmorType.Phoenix.getMaterial(), EquipmentSlot.LEGS, LOOT_ARMOR));
    public static final Item PHOENIX_BOOTS = register("phoenix_boots", new ArmorItem(AetherArmorType.Phoenix.getMaterial(), EquipmentSlot.FEET, LOOT_ARMOR));

    public static final Item OBSIDIAN_HELMET = register("obsidian_helmet", new ArmorItem(AetherArmorType.Obsidian.getMaterial(), EquipmentSlot.HEAD, LOOT_ARMOR));
    public static final Item OBSIDIAN_CHESTPLATE = register("obsidian_chestplate", new ArmorItem(AetherArmorType.Obsidian.getMaterial(), EquipmentSlot.CHEST, LOOT_ARMOR));
    public static final Item OBSIDIAN_LEGGINGS = register("obsidian_leggings", new ArmorItem(AetherArmorType.Obsidian.getMaterial(), EquipmentSlot.LEGS, LOOT_ARMOR));
    public static final Item OBSIDIAN_BOOTS = register("obsidian_boots", new ArmorItem(AetherArmorType.Obsidian.getMaterial(), EquipmentSlot.FEET, LOOT_ARMOR));

    public static final Item VALKYRIE_HELMET = register("valkyrie_helmet", new ArmorItem(AetherArmorType.Valkyrie.getMaterial(), EquipmentSlot.HEAD, LOOT_ARMOR));
    public static final Item VALKYRIE_CHESTPLATE = register("valkyrie_chestplate", new ArmorItem(AetherArmorType.Valkyrie.getMaterial(), EquipmentSlot.CHEST, LOOT_ARMOR));
    public static final Item VALKYRIE_LEGGINGS = register("valkyrie_leggings", new ArmorItem(AetherArmorType.Valkyrie.getMaterial(), EquipmentSlot.LEGS, LOOT_ARMOR));
    public static final Item VALKYRIE_BOOTS = register("valkyrie_boots", new ArmorItem(AetherArmorType.Valkyrie.getMaterial(), EquipmentSlot.FEET, LOOT_ARMOR));

    public static final Item SENTRY_BOOTS = register("sentry_boots", new ArmorItem(AetherArmorType.Sentry.getMaterial(), EquipmentSlot.FEET, LOOT_ARMOR));

    // Food
    private static final AetherItemSettings LOOT_FOOD = new AetherItemSettings().group(AetherItemGroups.Food).rarity(AetherItems.AETHER_LOOT);
    public static final Item BLUEBERRY = register("blue_berry", new AliasedBlockItem(AetherBlocks.BLUEBERRY_BUSH, new Settings().group(AetherItemGroups.Food).food(AetherFood.BLUEBERRY)));
    public static final Item ENCHANTED_BLUEBERRY = register("enchanted_blueberry", new Item(new Settings().group(AetherItemGroups.Food).rarity(Rarity.RARE).food(AetherFood.ENCHANTED_BLUEBERRY)));
    public static final Item ORANGE = register("orange", new Item(new Settings().group(AetherItemGroups.Food).food(AetherFood.ORANGE)));
    public static final Item WHITE_APPLE = register("white_apple", new WhiteApple(new Settings().group(AetherItemGroups.Food).food(AetherFood.WHITE_APPLE)));
    public static final Item BLUE_GUMMY_SWET = register("blue_gummy_swet", new Item(LOOT_FOOD.food(AetherFood.GUMMY_SWET)));
    public static final Item GOLDEN_GUMMY_SWET = register("golden_gummy_swet", new Item(LOOT_FOOD.food(AetherFood.GUMMY_SWET)));
    public static final Item AETHER_MILK = register("valkyrie_milk", new DrinkableItem(new Settings().rarity(Rarity.EPIC).food(AetherFood.MILK).maxCount(1)));
    public static final Item HEALING_STONE = register("healing_stone", new HealingStone(new Settings().group(AetherItemGroups.Food).rarity(Rarity.RARE).food(AetherFood.HEALING_STONE)));
    public static final Item CANDY_CANE = register("candy_cane", new Item(new Settings().group(AetherItemGroups.Food).food(AetherFood.GENERIC)));
    public static final Item GINGERBREAD_MAN = register("ginger_bread_man", new Item(new Settings().group(AetherItemGroups.Food).food(AetherFood.GENERIC)));
    public static final Item MOA_MEAT = register("moa_meat", new Item(new FabricItemSettings().group(AetherItemGroups.Food).food(AetherFood.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = register("moa_meat_cooked", new Item(new FabricItemSettings().group(AetherItemGroups.Food).food(AetherFood.COOKED_MOA_MEAT)));

    // Misc + Materials
    private static final AetherItemSettings LOOT_ACCESSORY = new AetherItemSettings().rarity(AETHER_LOOT).group(AetherItemGroups.Wearable);
    public static final Item GOLDEN_FEATHER = register("golden_feather", new ItemAccessory(AccessoryType.MISC, LOOT_ACCESSORY));
    public static final Item REGENERATION_STONE = register("regeneration_stone", new ItemAccessory(AccessoryType.MISC, LOOT_ACCESSORY));
    public static final Item IRON_BUBBLE = register("iron_bubble", new ItemAccessory(AccessoryType.MISC, LOOT_ACCESSORY));
    public static final Item LIFE_SHARD = register("life_shard", new LifeShard(new AetherItemSettings().rarity(AetherItems.AETHER_LOOT).maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item CLOUD_STAFF = register("cloud_staff", new CloudStaff(new Settings().maxCount(1).maxDamage(60).group(AetherItemGroups.Misc)));
    public static final Item NATURE_STAFF = register("nature_staff", new NatureStaff(new Settings().maxCount(1).maxDamage(100).group(AetherItemGroups.Misc)));
    public static final Item MOA_EGG = register("moa_egg", new MoaEgg(new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item LORE_BOOK = register("lore_book", new BookOfLore((new Settings()).maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item SKYROOT_BUCKET = register("skyroot_bucket", new SkyrootBucket(new Settings().maxCount(16).group(AetherItemGroups.Misc)));

    private static final Settings BUCKET = new Settings().maxCount(1).group(AetherItemGroups.Misc).recipeRemainder(SKYROOT_BUCKET);
    public static final Item SKYROOT_WATER_BUCKET = register("skyroot_water_bucket", new SkyrootBucket(Fluids.WATER, BUCKET));
    public static final Item SKYROOT_MILK_BUCKET = register("skyroot_milk_bucket", new SkyrootBucket(BUCKET));
    public static final Item SKYROOT_POISON_BUCKET = register("skyroot_poison_bucket", new SkyrootBucket(BUCKET));
    public static final Item SKYROOT_REMEDY_BUCKET = register("skyroot_remedy_bucket", new SkyrootBucket(BUCKET));
    public static final Item QUICKSOIL_VIAL = register("quicksoil_vial", new VialItem(Fluids.EMPTY, new Settings().group(AetherItemGroups.Misc)));
    public static final Item AERCLOUD_VIAL = register("aercloud_vial", new VialItem(AetherBlocks.DENSE_AERCLOUD_STILL, new Settings().group(AetherItemGroups.Misc)));
    public static final Item SKYROOT_BOAT = register("skyroot_boat", new BoatItem(AetherBoatTypes.SKYROOT, new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item GOLDEN_OAK_BOAT = register("golden_oak_boat", new BoatItem(AetherBoatTypes.GOLDEN_OAK, new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item ORANGE_BOAT = register("orange_boat", new BoatItem(AetherBoatTypes.ORANGE, new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item CRYSTAL_BOAT = register("crystal_boat", new BoatItem(AetherBoatTypes.CRYSTAL, new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item WISTERIA_BOAT = register("wisteria_boat", new BoatItem(AetherBoatTypes.WISTERIA, new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item CLOUD_PARACHUTE = register("cold_parachute", new Parachute(new Settings().maxCount(1).group(AetherItemGroups.Misc)));
    public static final Item GOLDEN_CLOUD_PARACHUTE = register("golden_parachute", new Parachute(new Settings().maxCount(1).maxDamage(20).group(AetherItemGroups.Misc)));

    private static final AetherItemSettings KEYS = new AetherItemSettings().group(AetherItemGroups.Misc).rarity(AETHER_LOOT);
    public static final Item BRONZE_KEY = register("bronze_key", new Item(KEYS));
    public static final Item SILVER_KEY = register("silver_key", new Item(KEYS));
    public static final Item GOLDEN_KEY = register("golden_key", new Item(KEYS));

    // Materials

    // Weapons
    private static final AetherItemSettings LOOT_WEAPON = new AetherItemSettings().rarity(AETHER_LOOT).group(AetherItemGroups.Tools);
    public static final Item GOLDEN_DART = register("golden_dart", new Dart(new Settings().group(AetherItemGroups.Tools)));
    public static final Item ENCHANTED_DART = register("enchanted_dart", new Dart(new Settings().rarity(Rarity.RARE).group(AetherItemGroups.Tools)));
    public static final Item POISON_DART = register("poison_dart", new Dart(new Settings().group(AetherItemGroups.Tools)));
    public static final Item GOLDEN_DART_SHOOTER = register("golden_dart_shooter", new DartShooter((Dart) GOLDEN_DART, new Settings().maxCount(1).group(AetherItemGroups.Tools)));
    public static final Item ENCHANTED_DART_SHOOTER = register("enchanted_dart_shooter", new DartShooter((Dart) ENCHANTED_DART, new Settings().maxCount(1).rarity(Rarity.RARE).group(AetherItemGroups.Tools)));
    public static final Item POISON_DART_SHOOTER = register("poison_dart_shooter", new DartShooter((Dart) POISON_DART, new Settings().maxCount(1).group(AetherItemGroups.Tools)));
    public static final Item PHOENIX_BOW = register("phoenix_bow", new BowItem(new Settings().maxDamage(384).group(AetherItemGroups.Tools)));
    public static final Item FLAMING_SWORD = register("flaming_sword", new ElementalSword(LOOT_WEAPON));
    public static final Item LIGHTNING_SWORD = register("lightning_sword", new ElementalSword(LOOT_WEAPON));
    public static final Item HOLY_SWORD = register("holy_sword", new ElementalSword(LOOT_WEAPON));
    public static final Item VAMPIRE_BLADE = register("vampire_blade", new VampireBlade(LOOT_WEAPON));
    public static final Item PIG_SLAYER = register("pig_slayer", new PigSlayer(LOOT_WEAPON));
    public static final Item CANDY_CANE_SWORD = register("candy_cane_sword", new CandyCaneSword(WEAPONS));

    // Spawn Eggs
    public static final Item AECHOR_PLANT_SPAWN_EGG = registerSpawnEgg("aechor_plant_spawn_egg", AetherEntityTypes.AECHOR_PLANT, 0x97ded4, 0x31897d);
    public static final Item CHEST_MIMIC_SPAWN_EGG = null;
    public static final Item COCKATRICE_SPAWN_EGG = registerSpawnEgg("cockatrice_spawn_egg", AetherEntityTypes.COCKATRICE, 0x9fc3f7, 0x3d2338);
    public static final Item AERBUNNY_SPAWN_EGG = registerSpawnEgg("aerbunny_spawn_egg", AetherEntityTypes.AERBUNNY, 0xc5d6ed, 0x82a6d9);
    public static final Item AERWHALE_SPAWN_EGG = registerSpawnEgg("aerwhale_spawn_egg", AetherEntityTypes.AERWHALE, 0x5c6d91, 0xdedbce);
    public static final Item FLYING_COW_SPAWN_EGG = null;
    public static final Item MOA_SPAWN_EGG = registerSpawnEgg("moa_spawn_egg", AetherEntityTypes.MOA, 0xc55c2e4, 0xb3a8bb);
    public static final Item SWET_SPAWN_EGG = registerSpawnEgg("swet_spawn_egg", AetherEntityTypes.WHITE_SWET, 0x8f9294, 0xe6eaeb);
    public static final Item BLUE_SWET_SPAWN_EGG = registerSpawnEgg("blue_swet_spawn_egg", AetherEntityTypes.BLUE_SWET, 0x46699e, 0xe6eaeb);
    public static final Item PURPLE_SWET_SPAWN_EGG = registerSpawnEgg("purple_swet_spawn_egg", AetherEntityTypes.PURPLE_SWET, 0x5d548c, 0xe6eaeb);
    public static final Item GOLDEN_SWET_SPAWN_EGG = registerSpawnEgg("golden_swet_spawn_egg", AetherEntityTypes.GOLDEN_SWET, 0xc99d36, 0xe6eaeb);
    public static final Item PHYG_SPAWN_EGG = null;
    public static final Item SHEEPUFF_SPAWN_EGG = null;

    // Accessories
     public static final Item LEATHER_GLOVES = register("leather_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xC65C35)).setDamageMultiplier(1.5F));
     public static final Item IRON_GLOVES = register("iron_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings()).setDamageMultiplier(2.5F));
     public static final Item GOLDEN_GLOVES = register("golden_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xFBF424)).setDamageMultiplier(2.0F));
     public static final Item CHAIN_GLOVES = register("chain_gloves", new ItemAccessory(AccessoryType.GLOVES, "chain", new AetherItemSettings()).setDamageMultiplier(2.0F));
     public static final Item DIAMOND_GLOVES = register("diamond_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x33EBCB)).setDamageMultiplier(4.5F));
     public static final Item ZANITE_GLOVES = register("zanite_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x711AE8)).setDamageMultiplier(3.0F));
     public static final Item GRAVITITE_GLOVES = register("gravitite_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xE752DB).rarity(Rarity.RARE)).setDamageMultiplier(4.0F));
     public static final Item NEPTUNE_GLOVES = register("neptune_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x2654FF).rarity(AETHER_LOOT)).setDamageMultiplier(4.5F));
     public static final Item PHOENIX_GLOVES = register("phoenix_gloves", new ItemAccessory(AccessoryType.GLOVES, "phoenix", new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xff7700).rarity(AETHER_LOOT)).setDamageMultiplier(4.0F));
     public static final Item OBSIDIAN_GLOVES = register("obsidian_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x1B1447)).setDamageMultiplier(5.0F));
     public static final Item VALKYRIE_GLOVES = register("valkyrie_gloves", new ItemAccessory(AccessoryType.GLOVES, "valkyrie", new AetherItemSettings().group(AetherItemGroups.Wearable).rarity(AETHER_LOOT)).setDamageMultiplier(5.0F));
     public static final Item IRON_RING = register("iron_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AetherItemGroups.Wearable)));
     public static final Item GOLDEN_RING = register("golden_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xEAEE57)));
     public static final Item ZANITE_RING = register("zanite_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x711AE8)));
     public static final Item ICE_RING = register("ice_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x95E6E7).rarity(Rarity.RARE)));
     public static final Item GOLDEN_PENDANT = register("golden_pendant", new ItemAccessory(AccessoryType.PENDANT, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xEAEE57)));
     public static final Item ZANITE_PENDANT = register("zanite_pendant", new ItemAccessory(AccessoryType.PENDANT, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x711AE8)));
     public static final Item ICE_PENDANT = register("ice_pendant", new ItemAccessory(AccessoryType.PENDANT, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x95E6E7).rarity(Rarity.RARE)));
     public static final Item WHITE_CAPE = register("white_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings()));
     public static final Item RED_CAPE = register("red_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xE81111)));
     public static final Item BLUE_CAPE = register("blue_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0x137FB7)));
     public static final Item YELLOW_CAPE = register("yellow_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().group(AetherItemGroups.Wearable).enchantmentGlintColor(0xCDCB0E)));
     public static final Item SWET_CAPE = register("swet_cape", new ItemAccessory(AccessoryType.CAPE, "swet", new AetherItemSettings().rarity(AETHER_LOOT)));
     public static final Item AGILITY_CAPE = register("agility_cape", new ItemAccessory(AccessoryType.CAPE, "agility", new AetherItemSettings().rarity(AETHER_LOOT)));
     public static final Item INVISIBILITY_CAPE = register("invisibility_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().rarity(AETHER_LOOT)));
     public static final Item AETHER_PORTAL = register("aether_portal", new AetherPortalItem(new Settings().group(AetherItemGroups.Misc)));

    public static class AetherFood {

        //  Fruits
        public static final FoodComponent BLUEBERRY = new FoodComponent.Builder().hunger(2).saturationModifier(0.5F).snack().build();
        public static final FoodComponent ENCHANTED_BLUEBERRY = new FoodComponent.Builder().hunger(8).saturationModifier(1.0F).snack().build();
        public static final FoodComponent ORANGE = new FoodComponent.Builder().hunger(5).saturationModifier(0.8F).snack().build();

        //  Meat
        public static final FoodComponent MOA_MEAT = new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build();
        public static final FoodComponent COOKED_MOA_MEAT = new FoodComponent.Builder().hunger(6).saturationModifier(1F).meat().build();

        //  Confectionery
        public static final FoodComponent GUMMY_SWET = new FoodComponent.Builder().hunger(8).saturationModifier(0.5F).build();

        //  Consumables
        public static final FoodComponent WHITE_APPLE = new FoodComponent.Builder().alwaysEdible().build();
        public static final FoodComponent HEALING_STONE = new FoodComponent.Builder().saturationModifier(2.5F).alwaysEdible().snack().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 610, 0), 1.0F).build();

        //  Misc
        public static final FoodComponent GENERIC = new FoodComponent.Builder().hunger(2).saturationModifier(1.5F).build();
        public static final FoodComponent MILK = new FoodComponent.Builder().hunger(12).saturationModifier(2F).snack().alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 2), 1F).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 4), 1F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 1), 1F).build();
    }

    static {
        for (Item item : new Item[]{
                BLUEBERRY,
                GINGERBREAD_MAN,
                CANDY_CANE
        }) {
            ComposterBlock.registerCompostableItem(0.3F, item);
        }

        for (Item item : new Item[]{
                WHITE_APPLE,
                ORANGE,
                AECHOR_PETAL
        }) {
            ComposterBlock.registerCompostableItem(0.65F, item);
        }

        DispenserBehavior skyrootBucketBehavior = new ItemDispenserBehavior() {
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                if (!(stack.getItem() instanceof SkyrootBucket bucket))
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
                if (!(stack.getItem() instanceof SkyrootBucket bucket))
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

    private static <T extends Item> T register(String id, T item) {
        return Registry.register(Registry.ITEM, Aether.locate(id), item);
    }

    private static Item registerSpawnEgg(String id, EntityType<? extends MobEntity> type, int mainColor, int secColor) {
        return register(id, new SpawnEggItem(type, mainColor, secColor, new Settings().group(AetherItemGroups.Misc)));
    }

    public static void init() {
        // Empty void. Eternal emptiness
    }

    public static void initClient() {
        // Empty void. Eternal emptiness
    }
}
