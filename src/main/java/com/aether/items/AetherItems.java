package com.aether.items;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import com.aether.entities.AetherEntityTypes;
import com.aether.fluids.AetherFluids;
import com.aether.items.accessories.AccessoryType;
import com.aether.items.accessories.ItemAccessory;
import com.aether.items.armor.AetherArmorMaterials;
import com.aether.items.food.*;
import com.aether.items.resources.AmbrosiumShard;
import com.aether.items.staff.CloudStaff;
import com.aether.items.staff.NatureStaff;
import com.aether.items.tools.*;
import com.aether.items.utils.AetherTiers;
import com.aether.items.weapons.*;
import com.aether.registry.RegistryQueue;
import com.aether.util.item.AetherRarity;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
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

import java.util.function.Consumer;

import static com.aether.items.AetherItemGroups.*;

@SuppressWarnings("unused")
public class AetherItems {
    private static final RegistryQueue<Item> queue = new RegistryQueue<>(Registry.ITEM, 256);

    private static Settings resource() { return new Settings().group(AETHER_BLOCKS); }
    public static final Item ZANITE_GEM = add("zanite_gemstone", new Item(resource()));
    public static final Item ZANITE_FRAGMENT = add("zanite_fragment", new Item(resource()));
    public static final Item GRAVITITE_GEM = add("gravitite_gemstone", new Item(resource()));
    public static final Item AMBROSIUM_SHARD = add("ambrosium_shard", new AmbrosiumShard(resource()));
    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource()));
    public static final Item AECHOR_PETAL = add("aechor_petal", new Item(resource()));
    public static final Item SWET_BALL = add("swet_ball", new Item(resource()));
    //SKYROOT_STICK = add("skyroot_stick", new Item(resource()));
    public static final Item GOLD_AERDUST = add("gold_aerdust", new Item(resource()));
    public static final Item FROZEN_AERDUST = add("frozen_aerdust", new Item(resource()));

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

    private static Settings tool() { return new Settings().group(AETHER_TOOLS); }
    public static final Item ZANITE_SHOVEL = add("zanite_shovel", new AetherShovel(AetherTiers.ZANITE, tool(), 1.5f, -3f));
    public static final Item ZANITE_PICKAXE = add("zanite_pickaxe", new AetherPickaxe(AetherTiers.ZANITE, tool(), 1, -2.8f));
    public static final Item ZANITE_AXE = add("zanite_axe", new AetherAxe(AetherTiers.ZANITE, tool(), 6f, -3.1f));
    public static final Item ZANITE_SWORD = add("zanite_sword", new AetherSword(AetherTiers.ZANITE, -2.4f, 3, tool()));
    public static final Item ZANITE_HOE = add("zanite_hoe", new AetherHoe(AetherTiers.ZANITE, tool(), 3f));

    private static Settings rareTool() { return tool().rarity(Rarity.RARE); }
    public static final Item GRAVITITE_SHOVEL = add("gravitite_shovel", new AetherShovel(AetherTiers.GRAVITITE, rareTool(), 1.5f, -3f));
    public static final Item GRAVITITE_PICKAXE = add("gravitite_pickaxe", new AetherPickaxe(AetherTiers.GRAVITITE, rareTool(), 1, -2.8f));
    public static final Item GRAVITITE_AXE = add("gravitite_axe", new AetherAxe(AetherTiers.GRAVITITE, rareTool(), 5f, -3f));
    public static final Item GRAVITITE_SWORD = add("gravitite_sword", new AetherSword(AetherTiers.GRAVITITE, -2.4f, 3, rareTool()));
    public static final Item GRAVITITE_HOE = add("gravitite_hoe", new AetherHoe(AetherTiers.GRAVITITE, rareTool(), 4f));

    private static Settings aetherLootTool() { return tool().rarity(AetherRarity.AETHER_LOOT); }
    public static final Item VALKYRIE_SHOVEL = add("valkyrie_shovel", new AetherShovel(AetherTiers.VALKYRIE, aetherLootTool(), 1.5f, -3f));
    public static final Item VALKYRIE_PICKAXE = add("valkyrie_pickaxe", new AetherPickaxe(AetherTiers.VALKYRIE, aetherLootTool(), 1, -2.8f));
    public static final Item VALKYRIE_AXE = add("valkyrie_axe", new AetherAxe(AetherTiers.VALKYRIE, aetherLootTool(), 4f, -2.9f));
    public static final Item VALKYRIE_LANCE = add("valkyrie_lance", new ValkyrieLance(tool().rarity(Rarity.EPIC)));
    public static final Item VALKYRIE_HOE = add("valkyrie_hoe", new AetherHoe(AetherTiers.VALKYRIE, aetherLootTool(), 5f));

    private static Settings bloodstone() { return new Settings().group(AETHER_TOOLS).maxCount(1); }
    public static final Item AMBROSIUM_BLOODSTONE = add("ambrosium_bloodstone", new AmbrosiumBloodstoneItem(bloodstone()));
    public static final Item ZANITE_BLOODSTONE = add("zanite_bloodstone", new ZaniteBloodstoneItem(bloodstone()));
    public static final Item GRAVITITE_BLOODSTONE = add("gravitite_bloodstone", new GravititeBloodstoneItem(bloodstone()));
    public static final Item ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(bloodstone()));

    private static Settings armor() { return new Settings().group(AETHER_WEARABLES); }
    public static final Item ZANITE_HELMET = add("zanite_helmet", new ArmorItem(AetherArmorMaterials.ZANITE, EquipmentSlot.HEAD, armor()));
    public static final Item ZANITE_CHESTPLATE = add("zanite_chestplate", new ArmorItem(AetherArmorMaterials.ZANITE, EquipmentSlot.CHEST, armor()));
    public static final Item ZANITE_LEGGINGS = add("zanite_leggings", new ArmorItem(AetherArmorMaterials.ZANITE, EquipmentSlot.LEGS, armor()));
    public static final Item ZANITE_BOOTS = add("zanite_boots", new ArmorItem(AetherArmorMaterials.ZANITE, EquipmentSlot.FEET, armor()));

    private static Settings gravititeArmor() { return armor().rarity(Rarity.RARE); }
    public static final Item GRAVITITE_HELMET = add("gravitite_helmet", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.HEAD, gravititeArmor()));
    public static final Item GRAVITITE_CHESTPLATE = add("gravitite_chestplate", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.CHEST, gravititeArmor()));
    public static final Item GRAVITITE_LEGGINGS = add("gravitite_leggings", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.LEGS, gravititeArmor()));
    public static final Item GRAVITITE_BOOTS = add("gravitite_boots", new ArmorItem(AetherArmorMaterials.GRAVITITE, EquipmentSlot.FEET, gravititeArmor()));

    private static Settings aetherLootArmor() { return armor().rarity(AetherRarity.AETHER_LOOT); }
    public static final Item NEPTUNE_HELMET = add("neptune_helmet", new ArmorItem(AetherArmorMaterials.NEPTUNE, EquipmentSlot.HEAD, aetherLootArmor()));
    public static final Item NEPTUNE_CHESTPLATE = add("neptune_chestplate", new ArmorItem(AetherArmorMaterials.NEPTUNE, EquipmentSlot.CHEST, aetherLootArmor()));
    public static final Item NEPTUNE_LEGGINGS = add("neptune_leggings", new ArmorItem(AetherArmorMaterials.NEPTUNE, EquipmentSlot.LEGS, aetherLootArmor()));
    public static final Item NEPTUNE_BOOTS = add("neptune_boots", new ArmorItem(AetherArmorMaterials.NEPTUNE, EquipmentSlot.FEET, aetherLootArmor()));
    public static final Item PHOENIX_HELMET = add("phoenix_helmet", new ArmorItem(AetherArmorMaterials.PHOENIX, EquipmentSlot.HEAD, aetherLootArmor()));
    public static final Item PHOENIX_CHESTPLATE = add("phoenix_chestplate", new ArmorItem(AetherArmorMaterials.PHOENIX, EquipmentSlot.CHEST, aetherLootArmor()));
    public static final Item PHOENIX_LEGGINGS = add("phoenix_leggings", new ArmorItem(AetherArmorMaterials.PHOENIX, EquipmentSlot.LEGS, aetherLootArmor()));
    public static final Item PHOENIX_BOOTS = add("phoenix_boots", new ArmorItem(AetherArmorMaterials.PHOENIX, EquipmentSlot.FEET, aetherLootArmor()));
    public static final Item OBSIDIAN_HELMET = add("obsidian_helmet", new ArmorItem(AetherArmorMaterials.OBSIDIAN, EquipmentSlot.HEAD, aetherLootArmor()));
    public static final Item OBSIDIAN_CHESTPLATE = add("obsidian_chestplate", new ArmorItem(AetherArmorMaterials.OBSIDIAN, EquipmentSlot.CHEST, aetherLootArmor()));
    public static final Item OBSIDIAN_LEGGINGS = add("obsidian_leggings", new ArmorItem(AetherArmorMaterials.OBSIDIAN, EquipmentSlot.LEGS, aetherLootArmor()));
    public static final Item OBSIDIAN_BOOTS = add("obsidian_boots", new ArmorItem(AetherArmorMaterials.OBSIDIAN, EquipmentSlot.FEET, aetherLootArmor()));
    public static final Item VALKYRIE_HELMET = add("valkyrie_helmet", new ArmorItem(AetherArmorMaterials.VALKYRIE, EquipmentSlot.HEAD, aetherLootArmor()));
    public static final Item VALKYRIE_CHESTPLATE = add("valkyrie_chestplate", new ArmorItem(AetherArmorMaterials.VALKYRIE, EquipmentSlot.CHEST, aetherLootArmor()));
    public static final Item VALKYRIE_LEGGINGS = add("valkyrie_leggings", new ArmorItem(AetherArmorMaterials.VALKYRIE, EquipmentSlot.LEGS, aetherLootArmor()));
    public static final Item VALKYRIE_BOOTS = add("valkyrie_boots", new ArmorItem(AetherArmorMaterials.VALKYRIE, EquipmentSlot.FEET, aetherLootArmor()));
    public static final Item SENTRY_BOOTS = add("sentry_boots", new ArmorItem(AetherArmorMaterials.SENTRY, EquipmentSlot.FEET, aetherLootArmor()));

    private static Settings food() { return new Settings().group(AETHER_FOOD); }
    public static final Item BLUEBERRY = register("blue_berry", new AliasedBlockItem(AetherBlocks.BLUEBERRY_BUSH, food().food(AetherFoodComponent.BLUEBERRY)));
    public static final Item ENCHANTED_BLUEBERRY = register("enchanted_blueberry", new Item(new Settings().group(AETHER_FOOD).rarity(Rarity.RARE).food(AetherFoodComponent.ENCHANTED_BLUEBERRY)));
    public static final Item ORANGE = register("orange", new Item(new Settings().group(AETHER_FOOD).food(AetherFoodComponent.ORANGE)));
    public static final Item WHITE_APPLE = register("white_apple", new WhiteApple(new Settings().group(AETHER_FOOD).food(AetherFoodComponent.WHITE_APPLE)));
    public static final Item BLUE_GUMMY_SWET = register("blue_gummy_swet", new Item(LOOT_FOOD.food(AetherFoodComponent.GUMMY_SWET)));
    public static final Item GOLDEN_GUMMY_SWET = register("golden_gummy_swet", new Item(LOOT_FOOD.food(AetherFoodComponent.GUMMY_SWET)));
    public static final Item AETHER_MILK = register("valkyrie_milk", new DrinkableItem(new Settings().rarity(Rarity.EPIC).food(AetherFoodComponent.MILK).maxCount(1)));
    public static final Item HEALING_STONE = register("healing_stone", new HealingStone(new Settings().group(AETHER_FOOD).rarity(Rarity.RARE).food(AetherFoodComponent.HEALING_STONE)));
    public static final Item CANDY_CANE = register("candy_cane", new Item(new Settings().group(AETHER_FOOD).food(AetherFoodComponent.GENERIC)));
    public static final Item GINGERBREAD_MAN = register("ginger_bread_man", new Item(new Settings().group(AETHER_FOOD).food(AetherFoodComponent.GENERIC)));
    public static final Item MOA_MEAT = register("moa_meat", new Item(new FabricItemSettings().group(AETHER_FOOD).food(AetherFoodComponent.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = register("moa_meat_cooked", new Item(new FabricItemSettings().group(AETHER_FOOD).food(AetherFoodComponent.COOKED_MOA_MEAT)));

    // Misc + Materials
    private static final AetherItemSettings LOOT_ACCESSORY = new AetherItemSettings().rarity(AetherRarity.AETHER_LOOT).group(AETHER_WEARABLES);
    public static final Item GOLDEN_FEATHER = register("golden_feather", new ItemAccessory(AccessoryType.MISC, LOOT_ACCESSORY));
    public static final Item REGENERATION_STONE = register("regeneration_stone", new ItemAccessory(AccessoryType.MISC, LOOT_ACCESSORY));
    public static final Item IRON_BUBBLE = register("iron_bubble", new ItemAccessory(AccessoryType.MISC, LOOT_ACCESSORY));
    public static final Item LIFE_SHARD = register("life_shard", new LifeShard(new AetherItemSettings().rarity(AetherRarity.AETHER_LOOT).maxCount(1).group(AETHER_MISC)));
    public static final Item CLOUD_STAFF = register("cloud_staff", new CloudStaff(new Settings().maxCount(1).maxDamage(60).group(AETHER_MISC)));
    public static final Item NATURE_STAFF = register("nature_staff", new NatureStaff(new Settings().maxCount(1).maxDamage(100).group(AETHER_MISC)));
    public static final Item MOA_EGG = register("moa_egg", new MoaEgg(new Settings().maxCount(1).group(AETHER_MISC)));
    public static final Item LORE_BOOK = register("lore_book", new BookOfLore((new Settings()).maxCount(1).group(AETHER_MISC)));
    public static final Item SKYROOT_BUCKET = register("skyroot_bucket", new SkyrootBucket(new Settings().maxCount(16).group(AETHER_MISC)));

    private static final Settings BUCKET = new Settings().maxCount(1).group(AETHER_MISC).recipeRemainder(SKYROOT_BUCKET);
    public static final Item SKYROOT_WATER_BUCKET = register("skyroot_water_bucket", new SkyrootBucket(Fluids.WATER, BUCKET));
    public static final Item SKYROOT_MILK_BUCKET = register("skyroot_milk_bucket", new SkyrootBucket(BUCKET));
    public static final Item SKYROOT_POISON_BUCKET = register("skyroot_poison_bucket", new SkyrootBucket(BUCKET));
    public static final Item SKYROOT_REMEDY_BUCKET = register("skyroot_remedy_bucket", new SkyrootBucket(BUCKET));
    public static final Item QUICKSOIL_VIAL = register("quicksoil_vial", new VialItem(Fluids.EMPTY, new Settings().group(AETHER_MISC)));
    public static final Item AERCLOUD_VIAL = register("aercloud_vial", new VialItem(AetherFluids.DENSE_AERCLOUD, new Settings().group(AETHER_MISC)));
    public static final Item CLOUD_PARACHUTE = register("cold_parachute", new Parachute(new Settings().maxCount(1).group(AETHER_MISC)));
    public static final Item GOLDEN_CLOUD_PARACHUTE = register("golden_parachute", new Parachute(new Settings().maxCount(1).maxDamage(20).group(AETHER_MISC)));

    private static final AetherItemSettings KEYS = new AetherItemSettings().group(AETHER_MISC).rarity(AetherRarity.AETHER_LOOT);
    public static final Item BRONZE_KEY = register("bronze_key", new Item(KEYS));
    public static final Item SILVER_KEY = register("silver_key", new Item(KEYS));
    public static final Item GOLDEN_KEY = register("golden_key", new Item(KEYS));

    // Materials

    // Weapons
    private static final AetherItemSettings LOOT_WEAPON = new AetherItemSettings().rarity(AetherRarity.AETHER_LOOT).group(AETHER_TOOLS);
    public static final Item GOLDEN_DART = register("golden_dart", new Dart(new Settings().group(AETHER_TOOLS)));
    public static final Item ENCHANTED_DART = register("enchanted_dart", new Dart(new Settings().rarity(Rarity.RARE).group(AETHER_TOOLS)));
    public static final Item POISON_DART = register("poison_dart", new Dart(new Settings().group(AETHER_TOOLS)));
    public static final Item GOLDEN_DART_SHOOTER = register("golden_dart_shooter", new DartShooter((Dart) GOLDEN_DART, new Settings().maxCount(1).group(AETHER_TOOLS)));
    public static final Item ENCHANTED_DART_SHOOTER = register("enchanted_dart_shooter", new DartShooter((Dart) ENCHANTED_DART, new Settings().maxCount(1).rarity(Rarity.RARE).group(AETHER_TOOLS)));
    public static final Item POISON_DART_SHOOTER = register("poison_dart_shooter", new DartShooter((Dart) POISON_DART, new Settings().maxCount(1).group(AETHER_TOOLS)));
    public static final Item PHOENIX_BOW = register("phoenix_bow", new BowItem(new Settings().maxDamage(384).group(AETHER_TOOLS)));
    public static final Item FLAMING_SWORD = register("flaming_sword", new ElementalSword(LOOT_WEAPON));
    public static final Item LIGHTNING_SWORD = register("lightning_sword", new ElementalSword(LOOT_WEAPON));
    public static final Item HOLY_SWORD = register("holy_sword", new ElementalSword(LOOT_WEAPON));
    public static final Item VAMPIRE_BLADE = register("vampire_blade", new VampireBlade(LOOT_WEAPON));
    public static final Item PIG_SLAYER = register("pig_slayer", new PigSlayer(LOOT_WEAPON));
    public static final Item CANDY_CANE_SWORD = register("candy_cane_sword", new CandyCaneSword(tool()));

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
     public static final Item LEATHER_GLOVES = register("leather_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xC65C35)).setDamageMultiplier(1.5F));
     public static final Item IRON_GLOVES = register("iron_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings()).setDamageMultiplier(2.5F));
     public static final Item GOLDEN_GLOVES = register("golden_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xFBF424)).setDamageMultiplier(2.0F));
     public static final Item CHAIN_GLOVES = register("chain_gloves", new ItemAccessory(AccessoryType.GLOVES, "chain", new AetherItemSettings()).setDamageMultiplier(2.0F));
     public static final Item DIAMOND_GLOVES = register("diamond_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x33EBCB)).setDamageMultiplier(4.5F));
     public static final Item ZANITE_GLOVES = register("zanite_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x711AE8)).setDamageMultiplier(3.0F));
     public static final Item GRAVITITE_GLOVES = register("gravitite_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xE752DB).rarity(Rarity.RARE)).setDamageMultiplier(4.0F));
     public static final Item NEPTUNE_GLOVES = register("neptune_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x2654FF).rarity(AetherRarity.AETHER_LOOT)).setDamageMultiplier(4.5F));
     public static final Item PHOENIX_GLOVES = register("phoenix_gloves", new ItemAccessory(AccessoryType.GLOVES, "phoenix", new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xff7700).rarity(AetherRarity.AETHER_LOOT)).setDamageMultiplier(4.0F));
     public static final Item OBSIDIAN_GLOVES = register("obsidian_gloves", new ItemAccessory(AccessoryType.GLOVES, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x1B1447)).setDamageMultiplier(5.0F));
     public static final Item VALKYRIE_GLOVES = register("valkyrie_gloves", new ItemAccessory(AccessoryType.GLOVES, "valkyrie", new AetherItemSettings().group(AETHER_WEARABLES).rarity(AetherRarity.AETHER_LOOT)).setDamageMultiplier(5.0F));
     public static final Item IRON_RING = register("iron_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AETHER_WEARABLES)));
     public static final Item GOLDEN_RING = register("golden_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xEAEE57)));
     public static final Item ZANITE_RING = register("zanite_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x711AE8)));
     public static final Item ICE_RING = register("ice_ring", new ItemAccessory(AccessoryType.RING, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x95E6E7).rarity(Rarity.RARE)));
     public static final Item GOLDEN_PENDANT = register("golden_pendant", new ItemAccessory(AccessoryType.PENDANT, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xEAEE57)));
     public static final Item ZANITE_PENDANT = register("zanite_pendant", new ItemAccessory(AccessoryType.PENDANT, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x711AE8)));
     public static final Item ICE_PENDANT = register("ice_pendant", new ItemAccessory(AccessoryType.PENDANT, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x95E6E7).rarity(Rarity.RARE)));
     public static final Item WHITE_CAPE = register("white_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings()));
     public static final Item RED_CAPE = register("red_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xE81111)));
     public static final Item BLUE_CAPE = register("blue_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0x137FB7)));
     public static final Item YELLOW_CAPE = register("yellow_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().group(AETHER_WEARABLES).enchantmentGlintColor(0xCDCB0E)));
     public static final Item SWET_CAPE = register("swet_cape", new ItemAccessory(AccessoryType.CAPE, "swet", new AetherItemSettings().rarity(AetherRarity.AETHER_LOOT)));
     public static final Item AGILITY_CAPE = register("agility_cape", new ItemAccessory(AccessoryType.CAPE, "agility", new AetherItemSettings().rarity(AetherRarity.AETHER_LOOT)));
     public static final Item INVISIBILITY_CAPE = register("invisibility_cape", new ItemAccessory(AccessoryType.CAPE, new AetherItemSettings().rarity(AetherRarity.AETHER_LOOT)));
     public static final Item AETHER_PORTAL = register("aether_portal", new AetherPortalItem(new Settings().group(AETHER_MISC)));

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

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            String[] tintFields = new String[]{"primaryColor", "secondaryColor", "tertiaryColor"};
            NbtCompound sv = stack.getSubNbt("stackableVariant");
            if (sv != null && sv.contains(tintFields[tintIndex])) {
                return sv.getInt(tintFields[tintIndex]);
            }
            return 0xDADADA;
        }, SWET_BALL);
    }

    private static <T extends Item> T register(String id, T item) {
        return Registry.register(Registry.ITEM, Aether.locate(id), item);
    }

    private static Item registerSpawnEgg(String id, EntityType<? extends MobEntity> type, int mainColor, int secColor) {
        return register(id, new SpawnEggItem(type, mainColor, secColor, new Settings().group(AETHER_MISC)));
    }

    public static void init() {
        // Empty void. Eternal emptiness
    }

    public static void initClient() {
        // Empty void. Eternal emptiness
    }

    @SafeVarargs
    private static Item add(String id, Item item, Consumer<Item>... additionalActions) {
        return queue.add(id, item, additionalActions);
    }
}
