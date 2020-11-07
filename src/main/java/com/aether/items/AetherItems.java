package com.aether.items;

import com.aether.Aether;
import com.aether.items.accessories.AccessoryTypes;
import com.aether.items.accessories.ItemAccessory;
import com.aether.items.armor.AetherArmor;
import com.aether.items.armor.AetherArmorType;
import com.aether.items.dungeon.DungeonKey;
import com.aether.items.food.*;
import com.aether.items.materials.*;
import com.aether.items.staff.CloudStaff;
import com.aether.items.staff.NatureStaff;
import com.aether.items.tools.*;
import com.aether.items.utils.AetherTiers;
import com.aether.items.weapons.*;
import com.aether.util.EnumHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class AetherItems {
    public static final Item zanite_gem, ambrosium_shard, golden_amber, aechor_petal, swet_ball;
    public static final Item skyroot_stick/*, victory_medal*/;
    public static final Item skyroot_pickaxe, skyroot_axe, skyroot_shovel, skyroot_sword;
    public static final Item holystone_pickaxe, holystone_axe, holystone_shovel, holystone_sword;
    public static final Item zanite_pickaxe, zanite_axe, zanite_shovel, zanite_sword;
    public static final Item gravitite_pickaxe, gravitite_axe, gravitite_shovel, gravitite_sword;
    public static final Item valkyrie_pickaxe, valkyrie_axe, valkyrie_shovel;
    public static final Item zanite_helmet, zanite_chestplate, zanite_leggings, zanite_boots;
    public static final Item gravitite_helmet, gravitite_chestplate, gravitite_leggings, gravitite_boots;
    public static final Item neptune_helmet, neptune_chestplate, neptune_leggings, neptune_boots;
    public static final Item phoenix_helmet, phoenix_chestplate, phoenix_leggings, phoenix_boots;
    public static final Item obsidian_helmet, obsidian_chestplate, obsidian_leggings, obsidian_boots;
    public static final Item valkyrie_helmet, valkyrie_chestplate, valkyrie_leggings, valkyrie_boots;
    public static final Item blue_berry, enchanted_blueberry, blue_gummy_swet, golden_gummy_swet, healing_stone, white_apple, gingerbread_man, candy_cane;
    public static final Item skyroot_bucket, skyroot_water_bucket, skyroot_poison_bucket, skyroot_remedy_bucket, skyroot_milk_bucket;
    public static final Item cloud_parachute, golden_cloud_parachute;
    public static final Item bronze_key, silver_key, golden_key;
    public static final Item nature_staff, cloud_staff, moa_egg;
    public static final Item golden_dart, enchanted_dart, poison_dart;
    public static final Item golden_dart_shooter, enchanted_dart_shooter, poison_dart_shooter;
    public static final Item phoenix_bow;
    public static final Item flaming_sword, lightning_sword, holy_sword;
    public static final Item vampire_blade, pig_slayer, candy_cane_sword, /*notch_hammer,*/
            valkyrie_lance;
    public static final Item leather_gloves, iron_gloves, golden_gloves, chain_gloves, diamond_gloves;
    public static final Item zanite_gloves, gravitite_gloves, neptune_gloves, phoenix_gloves, obsidian_gloves, valkyrie_gloves;
    public static final Item iron_ring, golden_ring, zanite_ring, ice_ring, iron_pendant, golden_pendant, zanite_pendant, ice_pendant;
    public static final Item red_cape, blue_cape, yellow_cape, white_cape, swet_cape, invisibility_cape, agility_cape;
    public static final Item golden_feather, regeneration_stone, iron_bubble;
    public static final Item life_shard;
    public static final Item sentry_boots/*, lightning_knife*/;
//    public static Item aether_tune, ascending_dawn, welcoming_skies, legacy;
//    public static Item repulsion_shield;
    public static Item lore_book;

    public static final Rarity aether_loot = EnumHelper.addRarity("aether_loot", Formatting.GREEN);

    static {
        // Resources
        zanite_gem = register("zanite_gemstone", new ZaniteGemstone());
        ambrosium_shard = register("ambrosium_shard", new AmbrosiumShard());
        golden_amber = register("golden_amber", new GoldenAmber());
        aechor_petal = register("aechor_petal", new AechorPetal());
        swet_ball = register("swet_ball", new SwetBall());
        skyroot_stick = register("skyroot_stick", new SkyrootStick());

        // Tools
        skyroot_shovel = register("skyroot_shovel", new AetherShovel(AetherTiers.SKYROOT, 1.5F, -3.0F));
        skyroot_pickaxe = register("skyroot_pickaxe", new AetherPickaxe(AetherTiers.SKYROOT, 1, -2.8F));
        skyroot_axe = register("skyroot_axe", new AetherAxe(AetherTiers.SKYROOT, 6.0F, -3.2F));
        skyroot_sword = register("skyroot_sword", new AetherSword(AetherTiers.SKYROOT, 3, -2.4F));

        holystone_shovel = register("holystone_shovel", new AetherShovel(AetherTiers.HOLYSTONE, 1.5F, -3.0F));
        holystone_pickaxe = register("holystone_pickaxe", new AetherPickaxe(AetherTiers.HOLYSTONE, 1, -2.8F));
        holystone_axe = register("holystone_axe", new AetherAxe(AetherTiers.HOLYSTONE, 7.0F, -3.2F));
        holystone_sword = register("holystone_sword", new AetherSword(AetherTiers.HOLYSTONE, 3, -2.4F));

        zanite_shovel = register("zanite_shovel", new AetherShovel(AetherTiers.ZANITE, 1.5F, -3.0F));
        zanite_pickaxe = register("zanite_pickaxe", new AetherPickaxe(AetherTiers.ZANITE, 1, -2.8F));
        zanite_axe = register("zanite_axe", new AetherAxe(AetherTiers.ZANITE, 6.0F, -3.1F));
        zanite_sword = register("zanite_sword", new AetherSword(AetherTiers.ZANITE, 3, -2.4F));

        gravitite_shovel = register("gravitite_shovel", new AetherShovel(AetherTiers.GRAVITITE, Rarity.RARE, 1.5F, -3.0F));
        gravitite_pickaxe = register("gravitite_pickaxe", new AetherPickaxe(AetherTiers.GRAVITITE, Rarity.RARE, 1, -2.8F));
        gravitite_axe = register("gravitite_axe", new AetherAxe(AetherTiers.GRAVITITE, Rarity.RARE, 5.0F, -3.0F));
        gravitite_sword = register("gravitite_sword", new AetherSword(AetherTiers.GRAVITITE, Rarity.RARE, 3, -2.4F));

        valkyrie_shovel = register("valkyrie_shovel", new AetherShovel(AetherTiers.VALKYRIE, aether_loot, 1.5F, -3.0F));
        valkyrie_pickaxe = register("valkyrie_pickaxe", new AetherPickaxe(AetherTiers.VALKYRIE, aether_loot, 1, -2.8F));
        valkyrie_axe = register("valkyrie_axe", new AetherAxe(AetherTiers.VALKYRIE, aether_loot, 4.0F, -2.9F));
        valkyrie_lance = register("valkyrie_lance", new ValkyrieLance());

        // Armor
        zanite_helmet = register("zanite_helmet", new AetherArmor(AetherArmorType.ZANITE, EquipmentSlot.HEAD));
        zanite_chestplate = register("zanite_chestplate", new AetherArmor(AetherArmorType.ZANITE, EquipmentSlot.CHEST));
        zanite_leggings = register("zanite_leggings", new AetherArmor(AetherArmorType.ZANITE, EquipmentSlot.LEGS));
        zanite_boots = register("zanite_boots", new AetherArmor(AetherArmorType.ZANITE, EquipmentSlot.FEET));

        gravitite_helmet = register("gravitite_helmet", new AetherArmor(AetherArmorType.GRAVITITE, Rarity.RARE, EquipmentSlot.HEAD));
        gravitite_chestplate = register("gravitite_chestplate", new AetherArmor(AetherArmorType.GRAVITITE, Rarity.RARE, EquipmentSlot.CHEST));
        gravitite_leggings = register("gravitite_leggings", new AetherArmor(AetherArmorType.GRAVITITE, Rarity.RARE, EquipmentSlot.LEGS));
        gravitite_boots = register("gravitite_boots", new AetherArmor(AetherArmorType.GRAVITITE, Rarity.RARE, EquipmentSlot.FEET));

        neptune_helmet = register("neptune_helmet", new AetherArmor(AetherArmorType.NEPTUNE, aether_loot, EquipmentSlot.HEAD));
        neptune_chestplate = register("neptune_chestplate", new AetherArmor(AetherArmorType.NEPTUNE, aether_loot, EquipmentSlot.CHEST));
        neptune_leggings = register("neptune_leggings", new AetherArmor(AetherArmorType.NEPTUNE, aether_loot, EquipmentSlot.LEGS));
        neptune_boots = register("neptune_boots", new AetherArmor(AetherArmorType.NEPTUNE, aether_loot, EquipmentSlot.FEET));

        phoenix_helmet = register("phoenix_helmet", new AetherArmor("phoenix", AetherArmorType.PHOENIX, aether_loot, EquipmentSlot.HEAD));
        phoenix_chestplate = register("phoenix_chestplate", new AetherArmor("phoenix", AetherArmorType.PHOENIX, aether_loot, EquipmentSlot.CHEST));
        phoenix_leggings = register("phoenix_leggings", new AetherArmor("phoenix", AetherArmorType.PHOENIX, aether_loot, EquipmentSlot.LEGS));
        phoenix_boots = register("phoenix_boots", new AetherArmor("phoenix", AetherArmorType.PHOENIX, aether_loot, EquipmentSlot.FEET));

        obsidian_helmet = register("obsidian_helmet", new AetherArmor(AetherArmorType.OBSIDIAN, aether_loot, EquipmentSlot.HEAD));
        obsidian_chestplate = register("obsidian_chestplate", new AetherArmor(AetherArmorType.OBSIDIAN, aether_loot, EquipmentSlot.CHEST));
        obsidian_leggings = register("obsidian_leggings", new AetherArmor(AetherArmorType.OBSIDIAN, aether_loot, EquipmentSlot.LEGS));
        obsidian_boots = register("obsidian_boots", new AetherArmor(AetherArmorType.OBSIDIAN, aether_loot, EquipmentSlot.FEET));

        valkyrie_helmet = register("valkyrie_helmet", new AetherArmor("valkyrie", AetherArmorType.VALKYRIE, aether_loot, EquipmentSlot.HEAD));
        valkyrie_chestplate = register("valkyrie_chestplate", new AetherArmor("valkyrie", AetherArmorType.VALKYRIE, aether_loot, EquipmentSlot.CHEST));
        valkyrie_leggings = register("valkyrie_leggings", new AetherArmor("valkyrie", AetherArmorType.VALKYRIE, aether_loot, EquipmentSlot.LEGS));
        valkyrie_boots = register("valkyrie_boots", new AetherArmor("valkyrie", AetherArmorType.VALKYRIE, aether_loot, EquipmentSlot.FEET));

        sentry_boots = register("sentry_boots", new AetherArmor("sentry", AetherArmorType.VALKYRIE, aether_loot, EquipmentSlot.FEET));

        // Food
        blue_berry = register("blue_berry", new Item(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.DEFAULT)));
        enchanted_blueberry = register("enchanted_blueberry", new Item(new Item.Settings().group(AetherItemGroups.Food).rarity(Rarity.RARE).food(AetherFood.ENCHANTED_BLUEBERRY)));
        white_apple = register("white_apple", new WhiteApple());
        blue_gummy_swet = register("blue_gummy_swet", new GummySwet());
        golden_gummy_swet = register("golden_gummy_swet", new GummySwet());
        healing_stone = register("healing_stone", new HealingStone());
        candy_cane = register("candy_cane", new Item(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.DEFAULT)));
        gingerbread_man = register("ginger_bread_man", new Item(new Item.Settings().group(AetherItemGroups.Food).food(AetherFood.DEFAULT)));

        // Misc
        skyroot_bucket = register("skyroot_bucket", new SkyrootBucket());
        skyroot_water_bucket = register("skyroot_water_bucket", new SkyrootBucket(Fluids.WATER, skyroot_bucket));
        skyroot_milk_bucket = register("skyroot_milk_bucket", new SkyrootBucket(skyroot_bucket));
        skyroot_poison_bucket = register("skyroot_poison_bucket", new SkyrootBucket(skyroot_bucket));
        skyroot_remedy_bucket = register("skyroot_remedy_bucket", new SkyrootBucket(skyroot_bucket));

        cloud_parachute = register("cold_parachute", new Parachute());
        golden_cloud_parachute = register("golden_parachute", new Parachute(20));

        bronze_key = register("bronze_key", new DungeonKey());
        silver_key = register("silver_key", new DungeonKey());
        golden_key = register("golden_key", new DungeonKey());

        // Weapons
        golden_dart = register("golden_dart", new Dart(Rarity.COMMON));
        enchanted_dart = register("enchanted_dart", new Dart(Rarity.RARE));
        poison_dart = register("poison_dart", new Dart(Rarity.COMMON));

        golden_dart_shooter = register("golden_dart_shooter", new DartShooter((Dart) golden_dart, Rarity.COMMON));
        enchanted_dart_shooter = register("enchanted_dart_shooter", new DartShooter((Dart) enchanted_dart, Rarity.RARE));
        poison_dart_shooter = register("poison_dart_shooter", new DartShooter((Dart) poison_dart, Rarity.COMMON));

        phoenix_bow = register("phoenix_bow", new ItemPhoenixBow());

        flaming_sword = register("flaming_sword", new ElementalSword());
        lightning_sword = register("lightning_sword", new ElementalSword());
        holy_sword = register("holy_sword", new ElementalSword());

        vampire_blade = register("vampire_blade", new VampireBlade());
        pig_slayer = register("pig_slayer", new PigSlayer());
        candy_cane_sword = register("candy_cane_sword", new CandyCaneSword());

        // Accessories
        leather_gloves = register("leather_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0xC65C35).setDamageMultiplier(1.5F));
        iron_gloves = register("iron_gloves", new ItemAccessory(AccessoryTypes.GLOVES).setDamageMultiplier(2.5F));
        golden_gloves = register("golden_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0xFBF424).setDamageMultiplier(2.0F));
        chain_gloves = register("chain_gloves", new ItemAccessory("chain", AccessoryTypes.GLOVES).setDamageMultiplier(2.0F));
        diamond_gloves = register("diamond_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0x33EBCB).setDamageMultiplier(4.5F));
        zanite_gloves = register("zanite_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0x711AE8).setDamageMultiplier(3.0F));
        gravitite_gloves = register("gravitite_gloves", new ItemAccessory(AccessoryTypes.GLOVES, Rarity.RARE, 0xE752DB).setDamageMultiplier(4.0F));
        neptune_gloves = register("neptune_gloves", new ItemAccessory(AccessoryTypes.GLOVES, aether_loot, 0x2654FF).setDamageMultiplier(4.5F));
        phoenix_gloves = register("phoenix_gloves", new ItemAccessory("phoenix", AccessoryTypes.GLOVES, aether_loot, 0xff7700).setDamageMultiplier(4.0F));
        obsidian_gloves = register("obsidian_gloves", new ItemAccessory(AccessoryTypes.GLOVES, 0x1B1447).setDamageMultiplier(5.0F));
        valkyrie_gloves = register("valkyrie_gloves", new ItemAccessory("valkyrie", AccessoryTypes.GLOVES, aether_loot).setDamageMultiplier(5.0F));

        iron_ring = register("iron_ring", new ItemAccessory(AccessoryTypes.RING));
        golden_ring = register("golden_ring", new ItemAccessory(AccessoryTypes.RING, 0xEAEE57));
        zanite_ring = register("zanite_ring", new ItemAccessory(AccessoryTypes.RING, 0x711AE8));
        ice_ring = register("ice_ring", new ItemAccessory(AccessoryTypes.RING, Rarity.RARE, 0x95E6E7));

        iron_pendant = register("iron_pendant", new ItemAccessory(AccessoryTypes.PENDANT));
        golden_pendant = register("golden_pendant", new ItemAccessory(AccessoryTypes.PENDANT, 0xEAEE57));
        zanite_pendant = register("zanite_pendant", new ItemAccessory(AccessoryTypes.PENDANT, 0x711AE8));
        ice_pendant = register("ice_pendant", new ItemAccessory(AccessoryTypes.PENDANT, Rarity.RARE, 0x95E6E7));

        white_cape = register("white_cape", new ItemAccessory(AccessoryTypes.CAPE));
        red_cape = register("red_cape", new ItemAccessory(AccessoryTypes.CAPE, 0xE81111));
        blue_cape = register("blue_cape", new ItemAccessory(AccessoryTypes.CAPE, 0x137FB7));
        yellow_cape = register("yellow_cape", new ItemAccessory(AccessoryTypes.CAPE, 0xCDCB0E));
        swet_cape = register("swet_cape", new ItemAccessory("swet", AccessoryTypes.CAPE, aether_loot));
        agility_cape = register("agility_cape", new ItemAccessory("agility", AccessoryTypes.CAPE, aether_loot));
        invisibility_cape = register("invisibility_cape", new ItemAccessory(AccessoryTypes.CAPE, aether_loot));

        // More misc I guess
        golden_feather = register("golden_feather", new ItemAccessory(AccessoryTypes.MISC, aether_loot));
        regeneration_stone = register("regeneration_stone", new ItemAccessory(AccessoryTypes.MISC, aether_loot));
        iron_bubble = register("iron_bubble", new ItemAccessory(AccessoryTypes.MISC, aether_loot));

        life_shard = register("life_shard", new LifeShard());

        cloud_staff = register("cloud_staff", new CloudStaff());
        nature_staff = register("nature_staff", new NatureStaff());

        moa_egg = register("moa_egg", new MoaEgg());

        lore_book = register("lore_book", new BookOfLore((new Item.Settings()).maxCount(1).group(AetherItemGroups.Misc)));

        // Some music, I think the original ones are copyrighted but whatever
        //TODO: Add AetherSounds
//        aether_tune = register("aether_tune", new AetherDisc(20, AetherSounds.aether_tune));
//        ascending_dawn = register("ascending_dawn", new AetherDisc(12, AetherSounds.ascending_dawn));
//        welcoming_skies = register("welcoming_skies", new AetherDisc(10, AetherSounds.welcoming_skies));
//        legacy = register("legacy", new AetherDisc(6, AetherSounds.legacy));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, Aether.locate(id), item);
    }

    public static void clientInitialization() {
        // Empty void. Eternal emptiness
    }
}