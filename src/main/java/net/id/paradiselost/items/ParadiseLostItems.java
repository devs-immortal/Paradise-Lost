package net.id.paradiselost.items;

import dev.thomasglasser.sherdsapi.api.SherdsApiDataComponents;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.armor.ParadiseLostArmorMaterials;
import net.id.paradiselost.items.armor.XpCircletItem;
import net.id.paradiselost.items.food.ParadiseLostFoodComponent;
import net.id.paradiselost.items.misc.*;
import net.id.paradiselost.items.tools.AurelMilkBucketItem;
import net.id.paradiselost.items.tools.ParadiseLostToolMaterials;
import net.id.paradiselost.items.tools.AurelBucketItem;
import net.id.paradiselost.items.tools.SoulSwordItem;
import net.id.paradiselost.items.tools.WardedJarItem;
import net.id.paradiselost.items.tools.base_tools.*;
import net.id.paradiselost.items.tools.bloodstone.CherineBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.SurtrumBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.OlviteBloodstoneItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.id.paradiselost.ParadiseLost.MOD_ID;
import static net.id.paradiselost.ParadiseLost.locate;
import static net.id.paradiselost.items.ParadiseLostItemActions.*;
import static net.minecraft.util.Rarity.*;

@SuppressWarnings("unused")
public class ParadiseLostItems {
    /*
    Begin items
     */

    private static Settings resource() {
        return new Settings();
    }

    public static final Item GOLDEN_AMBER = add("golden_amber", Item::new, resource());
    public static final Item CHERINE = add("cherine", Item::new, resource(), fuel(500));
    public static final Item OLVITE = add("olvite", Item::new, resource());
    public static final Item OLVITE_NUGGET = add("olvite_nugget", Item::new, resource());
    public static final Item REFINED_SURTRUM = add("refined_surtrum", Item::new, resource().fireproof());
    public static final Item RAW_SURTRUM = add("raw_surtrum", Item::new, resource().fireproof());
    public static final Item LEVITA_GEM = add("levita_gem", Item::new, resource());
    public static final Item FLAX_THREAD = add("flax_thread", Item::new, resource());
    public static final Item FLAXWEAVE = add("flaxweave", Item::new, resource());
    public static final Item SWEDROOT_PULP = add("swedroot_pulp", Item::new, resource(), compostable30);

    // Loot
    public static final Item SOL_POTTERY_SHERD = add("sol_pottery_sherd", Item::new, resource().component(SherdsApiDataComponents.SHERD_PATTERN.get(), ParadiseLost.locate("sol_pottery_pattern")));
    public static final Item COO_POTTERY_SHERD = add("coo_pottery_sherd", Item::new, resource().component(SherdsApiDataComponents.SHERD_PATTERN.get(), ParadiseLost.locate("coo_pottery_pattern")));

    private static Settings tool() {
        return new Settings();
    }

    private static final Settings tool = tool();
    private static final Settings rareTool = tool().rarity(RARE);

    private static Settings unstackableTool() {
        return tool().maxCount(1);
    }

    private static Settings unstackableRareTool() {
        return tool().maxCount(1).rarity(RARE);
    }

    // Olvite
    public static final ShovelItem OLVITE_SHOVEL = add("olvite_shovel", settings -> new ShovelItem(ParadiseLostToolMaterials.OLVITE, 1.5F, -3F, settings), tool());
    public static final PickaxeItem OLVITE_PICKAXE = add("olvite_pickaxe", settings -> new PickaxeItem(ParadiseLostToolMaterials.OLVITE, 1F, -2.8F, settings), tool());
    public static final AxeItem OLVITE_AXE = add("olvite_axe", settings -> new AxeItem(ParadiseLostToolMaterials.OLVITE, 6f, -3.1f, settings), tool());
    public static final SwordItem OLVITE_SWORD = add("olvite_sword", settings -> new SwordItem(ParadiseLostToolMaterials.OLVITE, 3, -2.4f, settings), tool());
    public static final HoeItem OLVITE_HOE = add("olvite_hoe", settings -> new HoeItem(ParadiseLostToolMaterials.OLVITE, -2, -1f, settings), tool());

    // Surtrum
    public static final ShovelItem SURTRUM_SHOVEL = add("surtrum_shovel", settings -> new ShovelItem(ParadiseLostToolMaterials.SURTRUM, 2.5f, -3f, settings), tool().fireproof());
    public static final PickaxeItem SURTRUM_PICKAXE = add("surtrum_pickaxe", settings -> new PickaxeItem(ParadiseLostToolMaterials.SURTRUM, 2, -2.8f, settings), tool().fireproof());
    public static final AxeItem SURTRUM_AXE = add("surtrum_axe", settings -> new AxeItem(ParadiseLostToolMaterials.SURTRUM, 6f, -3.1f, settings), tool().fireproof());
    public static final SwordItem SURTRUM_SWORD = add("surtrum_sword", settings -> new SwordItem(ParadiseLostToolMaterials.SURTRUM, 4, -2.4f, settings), tool().fireproof());
    public static final HoeItem SURTRUM_HOE = add("surtrum_hoe", settings -> new HoeItem(ParadiseLostToolMaterials.SURTRUM, -3, 0f, settings), tool().fireproof());

    // Glazed Gold
    public static final ShovelItem GLAZED_GOLD_SHOVEL = add("glazed_gold_shovel", settings -> new ShovelItem(ParadiseLostToolMaterials.GLAZED_GOLD, 1.5f, -3f, settings), tool());
    public static final PickaxeItem GLAZED_GOLD_PICKAXE = add("glazed_gold_pickaxe", settings -> new PickaxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, 1, -2.8f, settings), tool());
    public static final AxeItem GLAZED_GOLD_AXE = add("glazed_gold_axe", settings -> new AxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, 6f, -3.0f, settings), tool());
    public static final SwordItem GLAZED_GOLD_SWORD = add("glazed_gold_sword", settings -> new SwordItem(ParadiseLostToolMaterials.GLAZED_GOLD, 3, -2.4f, settings), tool());
    public static final HoeItem GLAZED_GOLD_HOE = add("glazed_gold_hoe", settings -> new HoeItem(ParadiseLostToolMaterials.GLAZED_GOLD, -2, -2.0f, settings), tool());

    public static final SwordItem SOUL_BLADE = add("soul_blade", settings -> new SoulSwordItem(ParadiseLostToolMaterials.SOUL_BLADE, 1, -2.8f, settings), tool().rarity(Rarity.EPIC));

    // misc
    public static final SpyglassItem OLVITE_SPYGLASS = add("olvite_spyglass", SpyglassItem::new, unstackableTool());
    public static final Item TOTEM_OF_LEVITATION = add("totem_of_levitation", Item::new, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    // wands
    public static final GravityWandItem LEVITA_WAND = add("levita_wand", GravityWandItem::new, unstackableRareTool().maxDamage(100));
    public static final CherineBloodstoneItem CHERINE_BLOODSTONE = add("cherine_bloodstone", CherineBloodstoneItem::new, unstackableTool());
    public static final OlviteBloodstoneItem OLVITE_BLOODSTONE = add("olvite_bloodstone", OlviteBloodstoneItem::new, unstackableTool());
    public static final SurtrumBloodstoneItem SURTRUM_BLOODSTONE = add("surtrum_bloodstone", SurtrumBloodstoneItem::new, unstackableTool().fireproof());


    private static final Text GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.applies_to"))).formatted(Formatting.BLUE);
    private static final Text GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.ingredients"))).formatted(Formatting.BLUE);
    private static final Text GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.base_slot_description")));
    private static final Text GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.additions_slot_description")));

    public static final Item GLAZED_GOLD_UPGRADE = add("glazed_gold_upgrade_smithing_template", settings -> new SmithingTemplateItem(GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT, GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT, GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT, List.of(Identifier.of("item/empty_armor_slot_helmet"), Identifier.of("item/empty_armor_slot_chestplate"), Identifier.of("item/empty_armor_slot_leggings"), Identifier.of("item/empty_armor_slot_boots"),
                    Identifier.of("item/empty_slot_hoe"), Identifier.of("item/empty_slot_axe"), Identifier.of("item/empty_slot_sword"), Identifier.of("item/empty_slot_shovel"), Identifier.of("item/empty_slot_pickaxe")), List.of(Identifier.of("item/empty_slot_ingot")), settings), new Settings());

    private static Settings wearable() {
        return new Settings();
    }

    private static final Settings WEARABLE = wearable();
    private static final Settings RARE_WEARABLE = wearable().rarity(RARE);

    // Olvite
    public static final ArmorItem OLVITE_HELMET = add("olvite_helmet", armor(ParadiseLostArmorMaterials.OLVITE, EquipmentType.HELMET), WEARABLE);
    public static final ArmorItem OLVITE_CHESTPLATE = add("olvite_chestplate", armor(ParadiseLostArmorMaterials.OLVITE, EquipmentType.CHESTPLATE), WEARABLE);
    public static final ArmorItem OLVITE_LEGGINGS = add("olvite_leggings", armor(ParadiseLostArmorMaterials.OLVITE, EquipmentType.LEGGINGS), WEARABLE);
    public static final ArmorItem OLVITE_BOOTS = add("olvite_boots", armor(ParadiseLostArmorMaterials.OLVITE, EquipmentType.BOOTS), WEARABLE);
    public static final ArmorItem OLVITE_HELMET_ORNATE = add("ornate_olvite_helmet", armor(ParadiseLostArmorMaterials.OLVITE, EquipmentType.HELMET), WEARABLE);

    // Glazed Gold
    public static final ArmorItem GLAZED_GOLD_HELMET = add("glazed_gold_helmet", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, EquipmentType.HELMET), WEARABLE);
    public static final ArmorItem GLAZED_GOLD_CHESTPLATE = add("glazed_gold_chestplate", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, EquipmentType.CHESTPLATE), WEARABLE);
    public static final ArmorItem GLAZED_GOLD_LEGGINGS = add("glazed_gold_leggings", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, EquipmentType.LEGGINGS), WEARABLE);
    public static final ArmorItem GLAZED_GOLD_BOOTS = add("glazed_gold_boots", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, EquipmentType.BOOTS), WEARABLE);

    // Surtrum
    public static final ArmorItem SURTRUM_HELMET = add("surtrum_helmet", armor(ParadiseLostArmorMaterials.SURTRUM, EquipmentType.HELMET), wearable().fireproof());
    public static final ArmorItem SURTRUM_CHESTPLATE = add("surtrum_chestplate", armor(ParadiseLostArmorMaterials.SURTRUM, EquipmentType.CHESTPLATE), wearable().fireproof());
    public static final ArmorItem SURTRUM_LEGGINGS = add("surtrum_leggings", armor(ParadiseLostArmorMaterials.SURTRUM, EquipmentType.LEGGINGS), wearable().fireproof());
    public static final ArmorItem SURTRUM_BOOTS = add("surtrum_boots", armor(ParadiseLostArmorMaterials.SURTRUM, EquipmentType.BOOTS), wearable().fireproof());

    // Relic
    public static final XpCircletItem XP_CIRCLET = add("xp_circlet", settings -> new XpCircletItem(ParadiseLostArmorMaterials.RELIC, EquipmentType.HELMET, settings), WEARABLE.rarity(RARE));

    private static Settings food() {
        return new Settings();
    }

    private static Settings food(FoodComponent foodComponent) {
        return new Settings().food(foodComponent);
    }

    private static Settings food(FoodComponent foodComponent, ConsumableComponent consumableComponent) {
        return new Settings().food(foodComponent, consumableComponent);
    }

    public static final BlockItem BLACKCURRANT = add("blackcurrant", ParadiseLostBlocks.BLACKCURRANT_BUSH, food(ParadiseLostFoodComponent.BLACKCURRANT, ParadiseLostFoodComponent.BLACKCURRANT_CONSUMABLE), compostable30);
    public static final BlockItem AMADRYS_BUSHEL = add("amadrys_bushel", ParadiseLostBlocks.AMADRYS, food(ParadiseLostFoodComponent.GENERIC_WORSE, ParadiseLostFoodComponent.GENERIC_WORSE_CONSUMABLE), compostable30);
    public static final BlockItem NITRA_SEED = add("nitra", ParadiseLostBlocks.NITRA, food(), compostable15);
    public static final Item NITRA_BULB = add("nitra_bulb", NitraItem::new, food(), compostable50);
    public static final Item AMADRYS_NOODLES = add("amadrys_noodles", Item::new, food(ParadiseLostFoodComponent.AMADRYS_NOODLES).useRemainder(Items.BOWL));
    public static final Item AMADRYS_BREAD = add("amadrys_bread", Item::new, food(ParadiseLostFoodComponent.AMADRYS_BREAD), compostable50);
    public static final Item AMADRYS_BREAD_GLAZED = add("amadrys_bread_glazed", Item::new, food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED), compostable50);
    public static final Item AMADRYS_BREAD_GLAZED_FILLED = add("amadrys_bread_glazed_filled", Item::new, food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED_FILLED, ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED_FILLED_CONSUMABLE), compostable50);
    public static final BlockItem SWEDROOT = add("swedroot", ParadiseLostBlocks.SWEDROOT, food(ParadiseLostFoodComponent.SWEDROOT), compostable30);

    public static final Item BLACKCURRANT_PIE = add("blackcurrant_pie", Item::new, food(ParadiseLostFoodComponent.BLACKCURRANT_PIE), compostable100);
    public static final Item BLACKCURRANT_COOKIE = add("blackcurrant_cookie", Item::new, food(ParadiseLostFoodComponent.BLACKCURRANT_COOKIE), compostable85);
    public static final Item ROOT_STEW = add("root_stew", Item::new, food(ParadiseLostFoodComponent.ROOT_STEW).useRemainder(Items.BOWL));

    public static final BlockItem FLAXSEED = add("flaxseed", ParadiseLostBlocks.FLAX, food(), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", Item::new, food(ParadiseLostFoodComponent.MOA_MEAT));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", Item::new, food(ParadiseLostFoodComponent.COOKED_MOA_MEAT));
    public static final Item POPOM_JELLY = add("popom_jelly", Item::new, food(ParadiseLostFoodComponent.POPOM_JELLY, ParadiseLostFoodComponent.POPOM_JELLY_CONSUMABLE), compostable15);

    public static final ParadiseLostPortalItem PARADISE_LOST_PORTAL = add("portal", ParadiseLostPortalItem::new, new Settings());
    public static final PalaceDoorPlacerItem PALACE_DOOR_PLACER = add("palace_door_placer", PalaceDoorPlacerItem::new, new Settings());

    public static final MoaEggItem MOA_EGG = add("moa_egg", MoaEggItem::new, new Settings().maxCount(1));
    public static final BlockItem NITRA_BUNCH = add(ParadiseLostBlocks.NITRA_BUNCH, fuel(3200));

    public static final AurelBucketItem AUREL_BUCKET = add("aurel_bucket", AurelBucketItem::new, new Settings().maxCount(16), fuel(200), emptyBucketBehavior);

    private static final Settings aurelBucket = new Settings().maxCount(1).recipeRemainder(AUREL_BUCKET);
    public static final AurelBucketItem AUREL_WATER_BUCKET = add("aurel_water_bucket", settings -> new AurelBucketItem(Fluids.WATER, settings), aurelBucket, emptiableBucketBehavior);
    public static final AurelBucketItem AUREL_POWDER_SNOW_BUCKET = add("aurel_powder_snow_bucket", settings -> new AurelBucketItem(Blocks.POWDER_SNOW, settings), aurelBucket, emptiableBucketBehavior);
    public static final AurelMilkBucketItem AUREL_MILK_BUCKET = add("aurel_milk_bucket", AurelMilkBucketItem::new, new Item.Settings().recipeRemainder(AUREL_BUCKET).component(DataComponentTypes.CONSUMABLE, ConsumableComponents.MILK_BUCKET).maxCount(1));


    public static final WardedJarItem WARDED_JAR = add("warded_jar", WardedJarItem::new, new Settings());

    private static Settings wardedJar() {
        return new Settings().maxCount(1).recipeRemainder(WARDED_JAR);
    }

    public static final WardedJarItem WARDED_JAR_ALLAY = add("warded_jar_allay", settings -> new WardedJarItem(EntityType.ALLAY, settings), wardedJar());
    public static final WardedJarItem WARDED_JAR_QUINT = add("warded_jar_quint", settings -> new WardedJarItem(ParadiseLostEntityTypes.QUINT, settings), wardedJar());

    public static final Item PALACE_KEY = add("palace_key", Item::new, new Settings());

    // Creative spawn eggs
    public static final SpawnEggItem ENVOY_SPAWN_EGG = add("envoy_spawn_egg", settings -> new SpawnEggItem(ParadiseLostEntityTypes.ENVOY, 0xc5b1af, 0x993c3c, settings), new Settings(), spawnEggBehavior);
    public static final SpawnEggItem SENTINEL_SPAWN_EGG = add("sentinel_spawn_egg", settings -> new SpawnEggItem(ParadiseLostEntityTypes.SENTINEL, 0xf7eeec, 0xffc0bf, settings), new Settings(), spawnEggBehavior);
    public static final SpawnEggItem MOA_SPAWN_EGG = add("moa_spawn_egg", settings -> new SpawnEggItem(ParadiseLostEntityTypes.MOA, 0xC55C2E4, 0xB3A8BB, settings), new Settings(), spawnEggBehavior);
    public static final SpawnEggItem POPOM_SPAWN_EGG = add("popom_spawn_egg", settings -> new SpawnEggItem(ParadiseLostEntityTypes.POPOM, 0xd984e8, 0xd4d0cf, settings), new Settings(), spawnEggBehavior);
    public static final SpawnEggItem QUINT_SPAWN_EGG = add("quint_spawn_egg", settings -> new SpawnEggItem(ParadiseLostEntityTypes.QUINT, 0xd9d0d9, 0xeeebf0, settings), new Settings(), spawnEggBehavior);

    public static final BlockItem BLOOMED_CALCITE = add(ParadiseLostBlocks.BLOOMED_CALCITE);

    // stone
    public static final BlockItem FLOESTONE = add(ParadiseLostBlocks.FLOESTONE);
    public static final BlockItem COBBLED_FLOESTONE = add(ParadiseLostBlocks.COBBLED_FLOESTONE);
    public static final BlockItem HELIOLITH = add(ParadiseLostBlocks.HELIOLITH);

    // nature
    public static final BlockItem HIGHLANDS_GRASS = add(ParadiseLostBlocks.HIGHLANDS_GRASS);

    public static final BlockItem FROZEN_GRASS = add(ParadiseLostBlocks.FROZEN_GRASS);
    public static final BlockItem DIRT = add(ParadiseLostBlocks.DIRT);
    public static final BlockItem COARSE_DIRT = add(ParadiseLostBlocks.COARSE_DIRT);
    public static final BlockItem PERMAFROST = add(ParadiseLostBlocks.PERMAFROST);
    public static final BlockItem LEVITA = add(ParadiseLostBlocks.LEVITA);
    public static final BlockItem PACKED_SWEDROOT = add(ParadiseLostBlocks.PACKED_SWEDROOT, compostable85);
    public static final BlockItem COLD_CLOUD = add(ParadiseLostBlocks.COLD_CLOUD);
    public static final BlockItem BLUE_CLOUD = add(ParadiseLostBlocks.BLUE_CLOUD);
    public static final BlockItem GOLDEN_CLOUD = add(ParadiseLostBlocks.GOLDEN_CLOUD);
    public static final BlockItem GREEN_CLOUD = add(ParadiseLostBlocks.GREEN_CLOUD);

    // planks
    public static final BlockItem AUREL_PLANKS = add(ParadiseLostBlocks.AUREL_WOODSTUFF.plank(), fuel(300));
    public static final BlockItem MOTHER_AUREL_PLANKS = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.plank(), fuel(300));
    public static final BlockItem MENTH_PLANKS = add(ParadiseLostBlocks.MENTH_WOODSTUFF.plank(), fuel(300));
    public static final BlockItem WISTERIA_PLANKS = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.plank(), fuel(300));
    public static final BlockItem AUREL_BOOKSHELF = add(ParadiseLostBlocks.AUREL_BOOKSHELF, fuel(300));

    // ores
    public static final BlockItem CHERINE_ORE = add(ParadiseLostBlocks.CHERINE_ORE);
    public static final BlockItem OLVITE_ORE = add(ParadiseLostBlocks.OLVITE_ORE);
    public static final BlockItem FLOESTONE_REDSTONE_ORE = add(ParadiseLostBlocks.FLOESTONE_REDSTONE_ORE);
    public static final BlockItem SURTRUM = add(ParadiseLostBlocks.SURTRUM, new Settings().fireproof());
    public static final BlockItem METAMORPHIC_SHELL = add(ParadiseLostBlocks.METAMORPHIC_SHELL);
    public static final BlockItem LEVITA_ORE = add(ParadiseLostBlocks.LEVITA_ORE);

    // ore blocks
    public static final BlockItem CHERINE_BLOCK = add(ParadiseLostBlocks.CHERINE_BLOCK, fuel(5000));
    public static final BlockItem OLVITE_BLOCK = add(ParadiseLostBlocks.OLVITE_BLOCK);
    public static final BlockItem REFINED_SURTRUM_BLOCK = add(ParadiseLostBlocks.REFINED_SURTRUM_BLOCK, new Settings().fireproof());

    // logs
    public static final BlockItem AUREL_LOG = add(ParadiseLostBlocks.AUREL_WOODSTUFF.log(), fuel(300));
    public static final BlockItem MOTTLED_AUREL_LOG = add(ParadiseLostBlocks.MOTTLED_AUREL_LOG, fuel(300));
    public static final BlockItem MOTTLED_AUREL_WOOD = add(ParadiseLostBlocks.MOTTLED_AUREL_WOOD, fuel(300));
    public static final BlockItem MOTTLED_AUREL_FALLEN_LOG = add(ParadiseLostBlocks.MOTTLED_AUREL_FALLEN_LOG, fuel(300));
    public static final BlockItem MOTHER_AUREL_LOG = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.log(), fuel(300));
    public static final BlockItem MENTH_LOG = add(ParadiseLostBlocks.MENTH_WOODSTUFF.log(), fuel(300));
    public static final BlockItem WISTERIA_LOG = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.log(), fuel(300));

    // stripped logs
    public static final BlockItem STRIPPED_AUREL_LOG = add(ParadiseLostBlocks.AUREL_WOODSTUFF.strippedLog(), fuel(300));
    public static final BlockItem STRIPPED_MOTHER_AUREL_LOG = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.strippedLog(), fuel(300));
    public static final BlockItem STRIPPED_MENTH_LOG = add(ParadiseLostBlocks.MENTH_WOODSTUFF.strippedLog(), fuel(300));
    public static final BlockItem STRIPPED_WISTERIA_LOG = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.strippedLog(), fuel(300));

    // stripped wood
    public static final BlockItem STRIPPED_AUREL_WOOD = add(ParadiseLostBlocks.AUREL_WOODSTUFF.strippedWood(), fuel(300));
    public static final BlockItem STRIPPED_MOTHER_AUREL_WOOD = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.strippedWood(), fuel(300));
    public static final BlockItem STRIPPED_MENTH_WOOD = add(ParadiseLostBlocks.MENTH_WOODSTUFF.strippedWood(), fuel(300));
    public static final BlockItem STRIPPED_WISTERIA_WOOD = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.strippedWood(), fuel(300));

    // wood
    public static final BlockItem AUREL_WOOD = add(ParadiseLostBlocks.AUREL_WOODSTUFF.wood(), fuel(300));
    public static final BlockItem MOTHER_AUREL_WOOD = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.wood(), fuel(300));
    public static final BlockItem MENTH_WOOD = add(ParadiseLostBlocks.MENTH_WOODSTUFF.wood(), fuel(300));
    public static final BlockItem WISTERIA_WOOD = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.wood(), fuel(300));
    // glass

    // slabs
    public static final BlockItem AUREL_SLAB = add(ParadiseLostBlocks.AUREL_WOODSTUFF.plankSlab(), fuel(150));
    public static final BlockItem MOTHER_AUREL_SLAB = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.plankSlab(), fuel(150));
    public static final BlockItem MENTH_SLAB = add(ParadiseLostBlocks.MENTH_WOODSTUFF.plankSlab(), fuel(150));
    public static final BlockItem WISTERIA_SLAB = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.plankSlab(), fuel(150));

    // smooth stuff
    // cobble variants
    public static final BlockItem MOSSY_FLOESTONE = add(ParadiseLostBlocks.MOSSY_FLOESTONE);
    public static final BlockItem GOLDEN_MOSSY_FLOESTONE = add(ParadiseLostBlocks.GOLDEN_MOSSY_FLOESTONE);

    // bricks
    public static final BlockItem FLOESTONE_BRICK = add(ParadiseLostBlocks.FLOESTONE_BRICK);
    public static final BlockItem CHISELED_FLOESTONE = add(ParadiseLostBlocks.CHISELED_FLOESTONE);
    public static final BlockItem SMOOTH_FLOESTONE = add(ParadiseLostBlocks.SMOOTH_FLOESTONE);
    public static final BlockItem SMOOTH_HELIOLITH = add(ParadiseLostBlocks.SMOOTH_HELIOLITH);
    public static final BlockItem LEVITA_BRICK = add(ParadiseLostBlocks.LEVITA_BRICK_SET.block());
    public static final BlockItem CHISELED_LEVITA_BRICK = add(ParadiseLostBlocks.CHISELED_LEVITA_BRICK);
    public static final BlockItem BURNISHED_STONE = add(ParadiseLostBlocks.BURNISHED_STONE_SET.block());
    public static final BlockItem BURNISHED_STONE_PLAQUE = add(ParadiseLostBlocks.BURNISHED_STONE_PLAQUE);
    public static final BlockItem BURNISHED_STONE_SCRIPT = add(ParadiseLostBlocks.BURNISHED_STONE_SCRIPT);
    public static final BlockItem GOLDEN_AMBER_TILE = add(ParadiseLostBlocks.GOLDEN_AMBER_TILE);
    public static final BlockItem CALCITE_TILES = add(ParadiseLostBlocks.CALCITE_TILES_SET.block());
    public static final BlockItem BLOOMED_CALCITE_TILES = add(ParadiseLostBlocks.BLOOMED_CALCITE_TILES_SET.block());

    // stairs
    public static final BlockItem AUREL_STAIRS = add(ParadiseLostBlocks.AUREL_WOODSTUFF.plankStairs(), fuel(300));
    public static final BlockItem MOTHER_AUREL_STAIRS = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.plankStairs(), fuel(300));
    public static final BlockItem MENTH_STAIRS = add(ParadiseLostBlocks.MENTH_WOODSTUFF.plankStairs(), fuel(300));
    public static final BlockItem WISTERIA_STAIRS = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.plankStairs(), fuel(300));

    // stone stairs + slabs
    public static final BlockItem FLOESTONE_STAIRS = add(ParadiseLostBlocks.FLOESTONE_STAIRS);
    public static final BlockItem COBBLED_FLOESTONE_STAIRS = add(ParadiseLostBlocks.COBBLED_FLOESTONE_STAIRS);
    public static final BlockItem MOSSY_FLOESTONE_STAIRS = add(ParadiseLostBlocks.MOSSY_FLOESTONE_STAIRS);
    public static final BlockItem HELIOLITH_STAIRS = add(ParadiseLostBlocks.HELIOLITH_STAIRS);
    public static final BlockItem FLOESTONE_BRICK_STAIRS = add(ParadiseLostBlocks.FLOESTONE_BRICK_STAIRS);
    public static final BlockItem SMOOTH_FLOESTONE_STAIRS = add(ParadiseLostBlocks.SMOOTH_FLOESTONE_STAIRS);
    public static final BlockItem SMOOTH_HELIOLITH_STAIRS = add(ParadiseLostBlocks.SMOOTH_HELIOLITH_STAIRS);
    public static final BlockItem LEVITA_BRICK_STAIRS = add(ParadiseLostBlocks.LEVITA_BRICK_SET.stairs());
    public static final BlockItem BURNISHED_STONE_STAIRS = add(ParadiseLostBlocks.BURNISHED_STONE_SET.stairs());
    public static final BlockItem GOLDEN_AMBER_TILE_STAIRS = add(ParadiseLostBlocks.GOLDEN_AMBER_TILE_STAIRS);
    public static final BlockItem CALCITE_TILES_STAIRS = add(ParadiseLostBlocks.CALCITE_TILES_SET.stairs());
    public static final BlockItem BLOOMED_CALCITE_TILES_STAIRS = add(ParadiseLostBlocks.BLOOMED_CALCITE_TILES_SET.stairs());

    public static final BlockItem FLOESTONE_SLAB = add(ParadiseLostBlocks.FLOESTONE_SLAB);
    public static final BlockItem COBBLED_FLOESTONE_SLAB = add(ParadiseLostBlocks.COBBLED_FLOESTONE_SLAB);
    public static final BlockItem MOSSY_FLOESTONE_SLAB = add(ParadiseLostBlocks.MOSSY_FLOESTONE_SLAB);
    public static final BlockItem HELIOLITH_SLAB = add(ParadiseLostBlocks.HELIOLITH_SLAB);
    public static final BlockItem FLOESTONE_BRICK_SLAB = add(ParadiseLostBlocks.FLOESTONE_BRICK_SLAB);
    public static final BlockItem SMOOTH_FLOESTONE_SLAB = add(ParadiseLostBlocks.SMOOTH_FLOESTONE_SLAB);
    public static final BlockItem SMOOTH_HELIOLITH_SLAB = add(ParadiseLostBlocks.SMOOTH_HELIOLITH_SLAB);
    public static final BlockItem LEVITA_BRICK_SLAB = add(ParadiseLostBlocks.LEVITA_BRICK_SET.slab());
    public static final BlockItem BURNISHED_STONE_SLAB = add(ParadiseLostBlocks.BURNISHED_STONE_SET.slab());
    public static final BlockItem GOLDEN_AMBER_TILE_SLAB = add(ParadiseLostBlocks.GOLDEN_AMBER_TILE_SLAB);
    public static final BlockItem CALCITE_TILES_SLAB = add(ParadiseLostBlocks.CALCITE_TILES_SET.slab());
    public static final BlockItem BLOOMED_CALCITE_TILES_SLAB = add(ParadiseLostBlocks.BLOOMED_CALCITE_TILES_SET.slab());

    // saplings
    public static final BlockItem AUREL_SAPLING = add(ParadiseLostBlocks.AUREL_WOODSTUFF.sapling(), compostable30, fuel(100));
    public static final BlockItem MOTHER_AUREL_SAPLING = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.sapling(), compostable30, fuel(100));
    public static final BlockItem MENTH_SAPLING = add(ParadiseLostBlocks.MENTH_WOODSTUFF.sapling(), compostable30, fuel(100));
    public static final BlockItem ROSE_WISTERIA_SAPLING = add(ParadiseLostBlocks.ROSE_WISTERIA_SAPLING, compostable30, fuel(100));
    public static final BlockItem FROST_WISTERIA_SAPLING = add(ParadiseLostBlocks.FROST_WISTERIA_SAPLING, compostable30, fuel(100));
    public static final BlockItem LAVENDER_WISTERIA_SAPLING = add(ParadiseLostBlocks.LAVENDER_WISTERIA_SAPLING, compostable30, fuel(100));

    // leaves
    public static final BlockItem AUREL_LEAVES = add(ParadiseLostBlocks.AUREL_WOODSTUFF.leaves(), compostable30);
    public static final BlockItem MOTHER_AUREL_LEAVES = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.leaves(), compostable30);
    public static final BlockItem MENTH_LEAVES = add(ParadiseLostBlocks.MENTH_WOODSTUFF.leaves(), compostable30);
    public static final BlockItem ROSE_WISTERIA_LEAVES = add(ParadiseLostBlocks.ROSE_WISTERIA_LEAVES, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAVES = add(ParadiseLostBlocks.FROST_WISTERIA_LEAVES, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAVES = add(ParadiseLostBlocks.LAVENDER_WISTERIA_LEAVES, compostable30);

    // plants
    public static final BlockItem GRASS = add(ParadiseLostBlocks.GRASS, compostable30);
    public static final BlockItem GRASS_FLOWERING = add(ParadiseLostBlocks.GRASS_FLOWERING, compostable30);
    public static final BlockItem SHORT_GRASS = add(ParadiseLostBlocks.SHORT_GRASS, compostable30);
    public static final BlockItem FERN = add(ParadiseLostBlocks.FERN, compostable30);
    public static final BlockItem BUSH = add(ParadiseLostBlocks.BUSH, compostable30);
    public static final BlockItem SHAMROCK = add(ParadiseLostBlocks.SHAMROCK, compostable50);
    public static final BlockItem MALT_SPRIG = add(ParadiseLostBlocks.MALT_SPRIG, compostable30);

    public static final BlockItem ANCIENT_FLOWER = add(ParadiseLostBlocks.ANCIENT_FLOWER, compostable65);
    public static final BlockItem ATARAXIA = add(ParadiseLostBlocks.ATARAXIA, compostable65);
    public static final BlockItem CLOUDSBLUFF = add(ParadiseLostBlocks.CLOUDSBLUFF, compostable65);
    public static final BlockItem DRIGEAN = add(ParadiseLostBlocks.DRIGEAN, compostable65, fuel(1600));
    public static final BlockItem LUMINAR = add(ParadiseLostBlocks.LUMINAR, compostable65);

    public static final BlockItem WILD_FLAX = add(ParadiseLostBlocks.WILD_FLAX, compostable100);

    public static final BlockItem ROSE_WISTERIA_HANGER = add(ParadiseLostBlocks.ROSE_WISTERIA_HANGER, compostable30);
    public static final BlockItem FROST_WISTERIA_HANGER = add(ParadiseLostBlocks.FROST_WISTERIA_HANGER, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_HANGER = add(ParadiseLostBlocks.LAVENDER_WISTERIA_HANGER, compostable30);

    public static final BlockItem AUREL_LEAF_PILE = add(ParadiseLostBlocks.AUREL_LEAF_PILE, compostable30);
    public static final BlockItem ROSE_WISTERIA_LEAF_PILE = add(ParadiseLostBlocks.ROSE_WISTERIA_LEAF_PILE, compostable30);
    public static final BlockItem FROST_WISTERIA_LEAF_PILE = add(ParadiseLostBlocks.FROST_WISTERIA_LEAF_PILE, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_LEAF_PILE = add(ParadiseLostBlocks.LAVENDER_WISTERIA_LEAF_PILE, compostable30);

    // tall plants
    public static final BlockItem TALL_GRASS = add(ParadiseLostBlocks.TALL_GRASS, compostable50);
    public static final BlockItem HONEY_NETTLE = add(ParadiseLostBlocks.HONEY_NETTLE, compostable50);

    public static final BlockItem LIVERWORT = add(ParadiseLostBlocks.LIVERWORT, compostable100, fuel(150));
    public static final BlockItem LIVERWORT_CARPET = add(ParadiseLostBlocks.LIVERWORT_CARPET, compostable65, fuel(100));

    public static final BlockItem THATCH_BLOCK = add(ParadiseLostBlocks.THATCH_SET.block(), compostable15, fuel(100));
    public static final BlockItem THATCH_STAIRS = add(ParadiseLostBlocks.THATCH_SET.stairs(), compostable15, fuel(100));
    public static final BlockItem THATCH_SLAB = add(ParadiseLostBlocks.THATCH_SET.slab(), compostable15, fuel(50));

    public static final BlockItem ROOTCAP = add(ParadiseLostBlocks.ROOTCAP, compostable65);
    public static final BlockItem BROWN_SPORECAP = add(ParadiseLostBlocks.BROWN_SPORECAP, compostable65);
    public static final BlockItem PINK_SPORECAP = add(ParadiseLostBlocks.PINK_SPORECAP, compostable65);
    public static final BlockItem ROOTCAP_BLOCK = add(ParadiseLostBlocks.ROOTCAP_BLOCK, compostable85);
    public static final BlockItem BROWN_SPORECAP_BLOCK = add(ParadiseLostBlocks.BROWN_SPORECAP_BLOCK, compostable85);
    public static final BlockItem PINK_SPORECAP_BLOCK = add(ParadiseLostBlocks.PINK_SPORECAP_BLOCK, compostable85);

    public static final BlockItem FLAXWEAVE_CUSHION = add(ParadiseLostBlocks.FLAXWEAVE_CUSHION, fuel(300));
    public static final BlockItem FLAXWEAVE_CUSHION_SLAB = add(ParadiseLostBlocks.FLAXWEAVE_CUSHION_SLAB, fuel(150));
    public static final BlockItem AMADRYS_BUNDLE = add(ParadiseLostBlocks.AMADRYS_BUNDLE, compostable85);
    public static final BlockItem CHEESECAKE = add(ParadiseLostBlocks.CHEESECAKE);

    // lights
    public static final BlockItem CHERINE_LANTERN = add(ParadiseLostBlocks.CHERINE_LANTERN);
    public static final VerticallyAttachableBlockItem CHERINE_TORCH = add("cherine_torch", settings -> new VerticallyAttachableBlockItem(ParadiseLostBlocks.CHERINE_TORCH, ParadiseLostBlocks.CHERINE_TORCH_WALL, Direction.DOWN, settings), new Settings().translationKey(ParadiseLostBlocks.CHERINE_TORCH.getTranslationKey()));

    // util blocks (enchanter, freezer, etc.)
    public static final BlockItem CALCITE_FLOWER_POT = add(ParadiseLostBlocks.CALCITE_FLOWER_POT);
    public static final BlockItem CALCITE_DECORATED_POT = add(ParadiseLostBlocks.CALCITE_DECORATED_POT);
    public static final BlockItem CHERINE_CAMPFIRE = add(ParadiseLostBlocks.CHERINE_CAMPFIRE);
    public static final BlockItem SUSPICIOUS_DIRT = add(ParadiseLostBlocks.SUSPICIOUS_DIRT);
    public static final BlockItem INCUBATOR = add(ParadiseLostBlocks.INCUBATOR, fuel(300));
    public static final BlockItem NEST = add(ParadiseLostBlocks.NEST, fuel(300));
    public static final BlockItem FOOD_BOWL = add(ParadiseLostBlocks.FOOD_BOWL, fuel(300));
    public static final BlockItem TREE_TAP = add(ParadiseLostBlocks.TREE_TAP, fuel(300));

    // door-like things
    public static final BlockItem AUREL_DOOR = add(ParadiseLostBlocks.AUREL_WOODSTUFF.door(), fuel(200));
    public static final BlockItem MOTHER_AUREL_DOOR = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.door(), fuel(200));
    public static final BlockItem MENTH_DOOR = add(ParadiseLostBlocks.MENTH_WOODSTUFF.door(), fuel(200));
    public static final BlockItem WISTERIA_DOOR = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.door(), fuel(200));

    public static final BlockItem AUREL_TRAPDOOR = add(ParadiseLostBlocks.AUREL_WOODSTUFF.trapdoor(), fuel(300));
    public static final BlockItem MOTHER_AUREL_TRAPDOOR = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.trapdoor(), fuel(300));
    public static final BlockItem MENTH_TRAPDOOR = add(ParadiseLostBlocks.MENTH_WOODSTUFF.trapdoor(), fuel(300));
    public static final BlockItem WISTERIA_TRAPDOOR = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.trapdoor(), fuel(300));

    public static final BlockItem AUREL_FENCE_GATE = add(ParadiseLostBlocks.AUREL_WOODSTUFF.fenceGate(), fuel(300));
    public static final BlockItem MOTHER_AUREL_FENCE_GATE = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.fenceGate(), fuel(300));
    public static final BlockItem MENTH_FENCE_GATE = add(ParadiseLostBlocks.MENTH_WOODSTUFF.fenceGate(), fuel(300));
    public static final BlockItem WISTERIA_FENCE_GATE = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.fenceGate(), fuel(300));

    // fences
    public static final BlockItem AUREL_FENCE = add(ParadiseLostBlocks.AUREL_WOODSTUFF.fence(), fuel(300));
    public static final BlockItem MOTHER_AUREL_FENCE = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.fence(), fuel(300));
    public static final BlockItem MENTH_FENCE = add(ParadiseLostBlocks.MENTH_WOODSTUFF.fence(), fuel(300));
    public static final BlockItem WISTERIA_FENCE = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.fence(), fuel(300));

    // walls
    public static final BlockItem FLOESTONE_WALL = add(ParadiseLostBlocks.FLOESTONE_WALL);
    public static final BlockItem COBBLED_FLOESTONE_WALL = add(ParadiseLostBlocks.COBBLED_FLOESTONE_WALL);
    public static final BlockItem MOSSY_FLOESTONE_WALL = add(ParadiseLostBlocks.MOSSY_FLOESTONE_WALL);
    public static final BlockItem HELIOLITH_WALL = add(ParadiseLostBlocks.HELIOLITH_WALL);
    public static final BlockItem FLOESTONE_BRICK_WALL = add(ParadiseLostBlocks.FLOESTONE_BRICK_WALL);
    public static final BlockItem BURNISHED_STONE_WALL = add(ParadiseLostBlocks.BURNISHED_STONE_WALL);
    public static final BlockItem CALCITE_TILES_WALL = add(ParadiseLostBlocks.CALCITE_TILES_WALL);
    public static final BlockItem BLOOMED_CALCITE_TILES_WALL = add(ParadiseLostBlocks.BLOOMED_CALCITE_TILES_WALL);

    // panes & chains
    public static final BlockItem OLVITE_CHAIN = add(ParadiseLostBlocks.OLVITE_CHAIN);
    public static final BlockItem GOLDEN_AMBER_BARS = add(ParadiseLostBlocks.GOLDEN_AMBER_BARS);
    // carpets

    // path & farmland
    public static final BlockItem FARMLAND = add(ParadiseLostBlocks.FARMLAND);
    public static final BlockItem DIRT_PATH = add(ParadiseLostBlocks.DIRT_PATH);
    public static final BlockItem PERMAFROST_PATH = add(ParadiseLostBlocks.PERMAFROST_PATH);

    // signs, wall stuff.
    private static Settings sign(Block signBlock) {
        return new Settings().maxCount(16).translationKey(signBlock.getTranslationKey());
    }

    public static final SignItem AUREL_SIGN = add("aurel_sign", settings -> new SignItem(ParadiseLostBlocks.AUREL_SIGNS.sign(), ParadiseLostBlocks.AUREL_SIGNS.wallSign(), settings), sign(ParadiseLostBlocks.AUREL_SIGNS.sign()), fuel(200));
    public static final SignItem AUREL_HANGING_SIGN = add("aurel_hanging_sign", settings -> new HangingSignItem(ParadiseLostBlocks.AUREL_SIGNS.hangingSign(), ParadiseLostBlocks.AUREL_SIGNS.wallHangingSign(), settings), sign(ParadiseLostBlocks.AUREL_SIGNS.hangingSign()), fuel(200));
    public static final SignItem MOTHER_AUREL_SIGN = add("mother_aurel_sign", settings -> new SignItem(ParadiseLostBlocks.MOTHER_AUREL_SIGNS.sign(), ParadiseLostBlocks.MOTHER_AUREL_SIGNS.wallSign(), settings), sign(ParadiseLostBlocks.MOTHER_AUREL_SIGNS.sign()), fuel(200));
    public static final SignItem MOTHER_AUREL_HANGING_SIGN = add("mother_aurel_hanging_sign", settings -> new HangingSignItem(ParadiseLostBlocks.MOTHER_AUREL_SIGNS.hangingSign(), ParadiseLostBlocks.MOTHER_AUREL_SIGNS.wallHangingSign(), settings), sign(ParadiseLostBlocks.MOTHER_AUREL_SIGNS.hangingSign()), fuel(200));
    public static final SignItem MENTH_SIGN = add("menth_sign", settings -> new SignItem(ParadiseLostBlocks.MENTH_SIGNS.sign(), ParadiseLostBlocks.MENTH_SIGNS.wallSign(), settings), sign(ParadiseLostBlocks.MENTH_SIGNS.sign()), fuel(200));
    public static final SignItem MENTH_HANGING_SIGN = add("menth_hanging_sign", settings -> new HangingSignItem(ParadiseLostBlocks.MENTH_SIGNS.hangingSign(), ParadiseLostBlocks.MENTH_SIGNS.wallHangingSign(), settings), sign(ParadiseLostBlocks.MENTH_SIGNS.hangingSign()), fuel(200));
    public static final SignItem WISTERIA_SIGN = add("wisteria_sign", settings -> new SignItem(ParadiseLostBlocks.WISTERIA_SIGNS.sign(), ParadiseLostBlocks.WISTERIA_SIGNS.wallSign(), settings), sign(ParadiseLostBlocks.WISTERIA_SIGNS.sign()), fuel(200));
    public static final SignItem WISTERIA_HANGING_SIGN = add("wisteria_hanging_sign", settings -> new HangingSignItem(ParadiseLostBlocks.WISTERIA_SIGNS.hangingSign(), ParadiseLostBlocks.WISTERIA_SIGNS.wallHangingSign(), settings), sign(ParadiseLostBlocks.WISTERIA_SIGNS.hangingSign()), fuel(200));

    // beds

    // Redstone items, buttons n pressure plates etc.
    public static final BlockItem FLOESTONE_BUTTON = add(ParadiseLostBlocks.FLOESTONE_BUTTON);
    public static final BlockItem FLOESTONE_PRESSURE_PLATE = add(ParadiseLostBlocks.FLOESTONE_PRESSURE_PLATE);
    public static final BlockItem OLVITE_PRESSURE_PLATE = add(ParadiseLostBlocks.OLVITE_PRESSURE_PLATE);

    public static final BlockItem AUREL_BUTTON = add(ParadiseLostBlocks.AUREL_WOODSTUFF.button(), fuel(100));
    public static final BlockItem MOTHER_AUREL_BUTTON = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.button(), fuel(100));
    public static final BlockItem MENTH_BUTTON = add(ParadiseLostBlocks.MENTH_WOODSTUFF.button(), fuel(100));
    public static final BlockItem WISTERIA_BUTTON = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.button(), fuel(100));

    public static final BlockItem AUREL_PRESSURE_PLATE = add(ParadiseLostBlocks.AUREL_WOODSTUFF.pressurePlate(), fuel(100));
    public static final BlockItem MOTHER_AUREL_PRESSURE_PLATE = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.pressurePlate(), fuel(100));
    public static final BlockItem MENTH_PRESSURE_PLATE = add(ParadiseLostBlocks.MENTH_WOODSTUFF.pressurePlate(), fuel(100));
    public static final BlockItem WISTERIA_PRESSURE_PLATE = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.pressurePlate(), fuel(100));

    public static final BlockItem LEVITA_RAIL = add(ParadiseLostBlocks.LEVITA_RAIL);

    public static final BlockItem LEVITATOR = add(ParadiseLostBlocks.LEVITATOR);

    public static final BoatSet AUREL_BOATS = addBoatItems("aurel", ParadiseLostEntityTypes.AUREL_BOAT, ParadiseLostEntityTypes.AUREL_CHEST_BOAT);
    public static final BoatSet MOTHER_AUREL_BOATS = addBoatItems("mother_aurel", ParadiseLostEntityTypes.MOTHER_AUREL_BOAT, ParadiseLostEntityTypes.MOTHER_AUREL_CHEST_BOAT);
    public static final BoatSet MENTH_BOATS = addBoatItems("menth", ParadiseLostEntityTypes.MENTH_BOAT, ParadiseLostEntityTypes.MENTH_CHEST_BOAT);
    public static final BoatSet WISTERIA_BOATS = addBoatItems("wisteria", ParadiseLostEntityTypes.WISTERIA_BOAT, ParadiseLostEntityTypes.WISTERIA_CHEST_BOAT);

    public static final BoatSet[] BOAT_SETS = new BoatSet[]{AUREL_BOATS, MOTHER_AUREL_BOATS, MENTH_BOATS, WISTERIA_BOATS};


    public static final RegistryEntry<Potion> HEALTH_BOOST_POTION = registerPotion(
            "health_boost", new Potion("health_boost", new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 6000, 1))
    );
    public static final RegistryEntry<Potion> LONG_HEALTH_BOOST_POTION = registerPotion(
            "long_health_boost", new Potion("health_boost", new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 12000, 1))
    );
    public static final RegistryEntry<Potion> STRONG_HEALTH_BOOST_POTION = registerPotion(
            "strong_health_boost", new Potion("health_boost", new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 3000, 3))
    );


    public static void init() {
    }

    @SafeVarargs
    private static <V extends Item> V add(String id, Function<Settings, V> factory, Settings settings, Consumer<ItemConvertible>... additionalActions) {
        return add(locate(id), factory, settings, additionalActions);
    }

    @SafeVarargs
    private static <V extends Item> V add(Identifier id, Function<Settings, V> factory, Settings settings, Consumer<ItemConvertible>... additionalActions) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        var registeredItem = Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
        for (var action : additionalActions) {
            action.accept(registeredItem);
        }
        return registeredItem;
    }

    @SafeVarargs
    private static BlockItem add(String id, Block block, Settings settings, Consumer<ItemConvertible>... additionalActions) {
        return add(locate(id), block, settings, additionalActions);
    }

    @SafeVarargs
    private static BlockItem add(Block block, Consumer<ItemConvertible>... additionalActions) {
        return add(Registries.BLOCK.getId(block), block, new Settings(), additionalActions);
    }

    @SafeVarargs
    private static BlockItem add(Block block, Settings settings, Consumer<ItemConvertible>... additionalActions) {
        return add(Registries.BLOCK.getId(block), block, settings, additionalActions);
    }

    @SafeVarargs
    private static BlockItem add(Identifier id, Block block, Settings settings, Consumer<ItemConvertible>... additionalActions) {
        return add(id,
                (block instanceof DoorBlock || block instanceof TallPlantBlock)
                        ? blockItemSettings -> new TallBlockItem(block, blockItemSettings)
                        : blockItemSettings -> new BlockItem(block, blockItemSettings),
                settings.translationKey(block.getTranslationKey()),
                additionalActions);
    }

    private static Function<Settings, ArmorItem> armor(ArmorMaterial material, EquipmentType type) {
        return settings -> new ArmorItem(material, type, settings);
    }

    private static BoatSet addBoatItems(String woodId, EntityType<? extends AbstractBoatEntity> boatType, EntityType<? extends AbstractBoatEntity> chestBoatType) {
        String boatId = (MOD_ID + "_" + woodId);

        BoatItem boat = add(woodId + "_boat", settings -> new BoatItem(boatType, settings), new Item.Settings().maxCount(1), fuel(1200));
        BoatItem chestBoat = add(woodId + "_chest_boat", settings -> new BoatItem(chestBoatType, settings), new Item.Settings().maxCount(1), fuel(1200));

        return new BoatSet(boat, chestBoat);
    }

    public record BoatSet(
            BoatItem boat,
            BoatItem chestBoat
    ) implements Iterable<Item> {
        public @NotNull Iterator<Item> iterator() {
            return Arrays.stream(new Item[]{boat, chestBoat}).iterator();
        }
    }

    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, locate(name), potion);
    }
}
