package net.id.paradiselost.items;

import com.chocohead.mm.api.ClassTinkerers;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.items.armor.ParadiseLostArmorMaterials;
import net.id.paradiselost.items.food.ParadiseLostFoodComponent;
import net.id.paradiselost.items.misc.*;
import net.id.paradiselost.items.tools.ParadiseLostToolMaterials;
import net.id.paradiselost.items.tools.AurelBucketItem;
import net.id.paradiselost.items.tools.base_tools.*;
import net.id.paradiselost.items.tools.bloodstone.CherineBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.SurtrumBloodstoneItem;
import net.id.paradiselost.items.tools.bloodstone.OlviteBloodstoneItem;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

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

    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource()));
    public static final Item CHERINE = add("cherine", new Item(resource()), fuel(500));
    public static final Item OLVITE = add("olvite", new Item(resource()));
    public static final Item OLVITE_NUGGET = add("olvite_nugget", new Item(resource()));
    public static final Item REFINED_SURTRUM = add("refined_surtrum", new Item(resource().fireproof()));
    public static final Item RAW_SURTRUM = add("raw_surtrum", new Item(resource().fireproof()));
    public static final Item LEVITA_GEM = add("levita_gem", new Item(resource()));
    public static final Item FLAX_THREAD = add("flax_thread", new Item(resource()));
    public static final Item FLAXWEAVE = add("flaxweave", new Item(resource()));
    public static final Item SWEDROOT_PULP = add("swedroot_pulp", new Item(resource()), compostable30);


    private static Settings tool() {
        return new Settings();
    }
    private static Settings shovel(ToolMaterial material, float attackDamage, float attackSpeed) {
        return tool().attributeModifiers(ShovelItem.createAttributeModifiers(material, attackDamage, attackSpeed));
    }
    private static Settings pickaxe(ToolMaterial material, float attackDamage, float attackSpeed) {
        return tool().attributeModifiers(PickaxeItem.createAttributeModifiers(material, attackDamage, attackSpeed));
    }
    private static Settings axe(ToolMaterial material, float attackDamage, float attackSpeed) {
        return tool().attributeModifiers(AxeItem.createAttributeModifiers(material, attackDamage, attackSpeed));
    }
    private static Settings sword(ToolMaterial material, int attackDamage, float attackSpeed) {
        return tool().attributeModifiers(SwordItem.createAttributeModifiers(material, attackDamage, attackSpeed));
    }
    private static Settings hoe(ToolMaterial material, float attackDamage, float attackSpeed) {
        return tool().attributeModifiers(HoeItem.createAttributeModifiers(material, attackDamage, attackSpeed));
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
    public static final ShovelItem OLVITE_SHOVEL = add("olvite_shovel", new ShovelItem(ParadiseLostToolMaterials.OLVITE, shovel(ParadiseLostToolMaterials.OLVITE, 1.5F, -3F)));
    public static final PickaxeItem OLVITE_PICKAXE = add("olvite_pickaxe", new PickaxeItem(ParadiseLostToolMaterials.OLVITE, pickaxe(ParadiseLostToolMaterials.OLVITE, 1F, -2.8F)));
    public static final AxeItem OLVITE_AXE = add("olvite_axe", new AxeItem(ParadiseLostToolMaterials.OLVITE, axe(ParadiseLostToolMaterials.OLVITE, 6f, -3.1f)));
    public static final SwordItem OLVITE_SWORD = add("olvite_sword", new SwordItem(ParadiseLostToolMaterials.OLVITE, sword(ParadiseLostToolMaterials.OLVITE, 3, -2.4f)));
    public static final HoeItem OLVITE_HOE = add("olvite_hoe", new HoeItem(ParadiseLostToolMaterials.OLVITE, hoe(ParadiseLostToolMaterials.OLVITE, -2, -1f)));

    // Surtrum
    public static final ShovelItem SURTRUM_SHOVEL = add("surtrum_shovel", new ShovelItem(ParadiseLostToolMaterials.SURTRUM, shovel(ParadiseLostToolMaterials.SURTRUM, 2.5f, -3f).fireproof()));
    public static final PickaxeItem SURTRUM_PICKAXE = add("surtrum_pickaxe", new PickaxeItem(ParadiseLostToolMaterials.SURTRUM, pickaxe(ParadiseLostToolMaterials.SURTRUM, 2, -2.8f).fireproof()));
    public static final AxeItem SURTRUM_AXE = add("surtrum_axe", new AxeItem(ParadiseLostToolMaterials.SURTRUM, axe(ParadiseLostToolMaterials.SURTRUM, 6f, -3.1f).fireproof()));
    public static final SwordItem SURTRUM_SWORD = add("surtrum_sword", new SwordItem(ParadiseLostToolMaterials.SURTRUM, sword(ParadiseLostToolMaterials.SURTRUM, 4, -2.4f).fireproof()));
    public static final HoeItem SURTRUM_HOE = add("surtrum_hoe", new HoeItem(ParadiseLostToolMaterials.SURTRUM, hoe(ParadiseLostToolMaterials.SURTRUM, -3, 0f).fireproof()));

    // Glazed Gold
    public static final ShovelItem GLAZED_GOLD_SHOVEL = add("glazed_gold_shovel", new ShovelItem(ParadiseLostToolMaterials.GLAZED_GOLD, shovel(ParadiseLostToolMaterials.GLAZED_GOLD, 1.5f, -3f)));
    public static final PickaxeItem GLAZED_GOLD_PICKAXE = add("glazed_gold_pickaxe", new PickaxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, pickaxe(ParadiseLostToolMaterials.GLAZED_GOLD, 1, -2.8f)));
    public static final AxeItem GLAZED_GOLD_AXE = add("glazed_gold_axe", new AxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, axe(ParadiseLostToolMaterials.GLAZED_GOLD, 6f, -3.0f)));
    public static final SwordItem GLAZED_GOLD_SWORD = add("glazed_gold_sword", new SwordItem(ParadiseLostToolMaterials.GLAZED_GOLD, sword(ParadiseLostToolMaterials.GLAZED_GOLD, 3, -2.4f)));
    public static final HoeItem GLAZED_GOLD_HOE = add("glazed_gold_hoe", new HoeItem(ParadiseLostToolMaterials.GLAZED_GOLD, hoe(ParadiseLostToolMaterials.GLAZED_GOLD, -2, -2.0f)));

    public static final GravityWandItem LEVITA_WAND = add("levita_wand", new GravityWandItem(unstackableRareTool().maxDamage(100)));
    public static final CherineBloodstoneItem CHERINE_BLOODSTONE = add("cherine_bloodstone", new CherineBloodstoneItem(unstackableTool()));
    public static final OlviteBloodstoneItem OLVITE_BLOODSTONE = add("olvite_bloodstone", new OlviteBloodstoneItem(unstackableTool()));
    public static final SurtrumBloodstoneItem SURTRUM_BLOODSTONE = add("surtrum_bloodstone", new SurtrumBloodstoneItem(unstackableTool().fireproof()));


    private static final Text GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.applies_to"))).formatted(Formatting.BLUE);
    private static final Text GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.ingredients"))).formatted(Formatting.BLUE);
    private static final Text GLAZED_GOLD_UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("upgrade", locate("glazed_gold_upgrade"))).formatted(Formatting.GRAY);
    private static final Text GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.base_slot_description")));
    private static final Text GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", locate("smithing_template.glazed_gold_upgrade.additions_slot_description")));

    public static final Item GLAZED_GOLD_UPGRADE = add("glazed_gold_upgrade_smithing_template", new SmithingTemplateItem(
            GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT, GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT, GLAZED_GOLD_UPGRADE_TEXT, GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
            List.of(Identifier.of("item/empty_armor_slot_helmet"), Identifier.of("item/empty_armor_slot_chestplate"), Identifier.of("item/empty_armor_slot_leggings"), Identifier.of("item/empty_armor_slot_boots"),
                    Identifier.of("item/empty_slot_hoe"), Identifier.of("item/empty_slot_axe"), Identifier.of("item/empty_slot_sword"), Identifier.of("item/empty_slot_shovel"), Identifier.of("item/empty_slot_pickaxe")),
            List.of(Identifier.of("item/empty_slot_ingot"))
    ));

    private static Settings wearable() {
        return new Settings();
    }

    private static final Settings WEARABLE = wearable();
    private static final Settings RARE_WEARABLE = wearable().rarity(RARE);

    private static ArmorItem armorHelper(RegistryEntry<ArmorMaterial> mat, ArmorItem.Type type, int durabilityMultiplier, Item.Settings settings) {
        return new ArmorItem(mat, type, WEARABLE.maxDamage(type.getMaxDamage(durabilityMultiplier)));
    }

    private static ArmorItem armorHelper(RegistryEntry<ArmorMaterial> mat, ArmorItem.Type type, int durabilityMultiplier) {
        return armorHelper(mat, type, durabilityMultiplier, WEARABLE);
    }

    // Olvite
    public static final ArmorItem OLVITE_HELMET = add("olvite_helmet", armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.HELMET, 15));
    public static final ArmorItem OLVITE_CHESTPLATE = add("olvite_chestplate", armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.CHESTPLATE, 15));
    public static final ArmorItem OLVITE_LEGGINGS = add("olvite_leggings", armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.LEGGINGS, 15));
    public static final ArmorItem OLVITE_BOOTS = add("olvite_boots", armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.BOOTS, 15));

    // Glazed Gold
    public static final ArmorItem GLAZED_GOLD_HELMET = add("glazed_gold_helmet", armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.HELMET, 21));
    public static final ArmorItem GLAZED_GOLD_CHESTPLATE = add("glazed_gold_chestplate", armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.CHESTPLATE, 21));
    public static final ArmorItem GLAZED_GOLD_LEGGINGS = add("glazed_gold_leggings", armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.LEGGINGS, 21));
    public static final ArmorItem GLAZED_GOLD_BOOTS = add("glazed_gold_boots", armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.BOOTS, 21));

    // Surtrum
    public static final ArmorItem SURTRUM_HELMET = add("surtrum_helmet", armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.HELMET, 27, wearable().fireproof()));
    public static final ArmorItem SURTRUM_CHESTPLATE = add("surtrum_chestplate", armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.CHESTPLATE, 27, wearable().fireproof()));
    public static final ArmorItem SURTRUM_LEGGINGS = add("surtrum_leggings", armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.LEGGINGS, 27, wearable().fireproof()));
    public static final ArmorItem SURTRUM_BOOTS = add("surtrum_boots", armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.BOOTS, 27, wearable().fireproof()));


    private static Settings food() {
        return new Settings();
    }

    private static Settings food(FoodComponent foodComponent) {
        return new Settings().food(foodComponent);
    }

    public static final AliasedBlockItem BLACKCURRANT = add("blackcurrant", new AliasedBlockItem(ParadiseLostBlocks.BLACKCURRANT_BUSH, food(ParadiseLostFoodComponent.BLACKCURRANT)), compostable30);
    public static final Item ORANGE = add("orange", new Item(food(ParadiseLostFoodComponent.ORANGE)), compostable65);
    public static final AliasedBlockItem AMADRYS_BUSHEL = add("amadrys_bushel", new AliasedBlockItem(ParadiseLostBlocks.AMADRYS, food(ParadiseLostFoodComponent.GENERIC_WORSE)), compostable30);
    public static final AliasedBlockItem NITRA_SEED = add("nitra", new AliasedBlockItem(ParadiseLostBlocks.NITRA, food()), compostable15);
    public static final Item NITRA_BULB = add("nitra_bulb", new NitraItem(food()), compostable50);
    public static final Item AMADRYS_NOODLES = add("amadrys_noodles", new StewItem(food(ParadiseLostFoodComponent.AMADRYS_NOODLES)));
    public static final Item AMADRYS_BREAD = add("amadrys_bread", new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD)), compostable50);
    public static final Item AMADRYS_BREAD_GLAZED = add("amadrys_bread_glazed", new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED)), compostable50);
    public static final AliasedBlockItem SWEDROOT = add("swedroot", new AliasedBlockItem(ParadiseLostBlocks.SWEDROOT, food(ParadiseLostFoodComponent.SWEDROOT)), compostable30);
    public static final AliasedBlockItem FLAXSEED = add("flaxseed", new AliasedBlockItem(ParadiseLostBlocks.FLAX, food()), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", new Item(food(ParadiseLostFoodComponent.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", new Item(food(ParadiseLostFoodComponent.COOKED_MOA_MEAT)));

    public static final ParadiseLostPortalItem PARADISE_LOST_PORTAL = add("portal", new ParadiseLostPortalItem(new Settings()));

    public static final MoaEggItem MOA_EGG = add("moa_egg", new MoaEggItem(new Settings().maxCount(1)));
    public static final BlockItem INCUBATOR = add(ParadiseLostBlocks.INCUBATOR, fuel(300));
    public static final BlockItem FOOD_BOWL = add(ParadiseLostBlocks.FOOD_BOWL, fuel(300));
    public static final BlockItem TREE_TAP = add(ParadiseLostBlocks.TREE_TAP, fuel(300));
    public static final BlockItem NITRA_BUNCH = add(ParadiseLostBlocks.NITRA_BUNCH, fuel(3200));

    public static final AurelBucketItem AUREL_BUCKET = add("aurel_bucket", new AurelBucketItem(new Settings().maxCount(16)), fuel(200), emptyBucketBehavior);

    private static final Settings aurelBucket = new Settings().maxCount(1).recipeRemainder(AUREL_BUCKET);
    public static final AurelBucketItem AUREL_WATER_BUCKET = add("aurel_water_bucket", new AurelBucketItem(Fluids.WATER, aurelBucket), emptiableBucketBehavior);
    public static final AurelBucketItem AUREL_MILK_BUCKET = add("aurel_milk_bucket", new AurelBucketItem(aurelBucket));

    public static final SpawnEggItem ENVOY_SPAWN_EGG = add("envoy_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.ENVOY, 0xc5b1af, 0x993c3c, new Settings()), spawnEggBehavior);
    public static final SpawnEggItem MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(ParadiseLostEntityTypes.MOA, 0xC55C2E4, 0xB3A8BB, new Settings()), spawnEggBehavior);

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
    // planks
    public static final BlockItem AUREL_PLANKS = add(ParadiseLostBlocks.AUREL_WOODSTUFF.plank());
    public static final BlockItem MOTHER_AUREL_PLANKS = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.plank());
    public static final BlockItem ORANGE_PLANKS = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.plank());
    public static final BlockItem WISTERIA_PLANKS = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.plank());
    public static final BlockItem AUREL_BOOKSHELF = add(ParadiseLostBlocks.AUREL_BOOKSHELF);
    // ores
    public static final BlockItem CHERINE_ORE = add(ParadiseLostBlocks.CHERINE_ORE);
    public static final BlockItem OLVITE_ORE = add(ParadiseLostBlocks.OLVITE_ORE);
    public static final BlockItem SURTRUM = add(ParadiseLostBlocks.SURTRUM, new Settings().fireproof());
    public static final BlockItem METAMORPHIC_SHELL = add(ParadiseLostBlocks.METAMORPHIC_SHELL);
    public static final BlockItem LEVITA_ORE = add(ParadiseLostBlocks.LEVITA_ORE);
    // ore blocks
    public static final BlockItem CHERINE_BLOCK = add(ParadiseLostBlocks.CHERINE_BLOCK, fuel(5000));
    public static final BlockItem OLVITE_BLOCK = add(ParadiseLostBlocks.OLVITE_BLOCK);
    public static final BlockItem REFINED_SURTRUM_BLOCK = add(ParadiseLostBlocks.REFINED_SURTRUM_BLOCK, new Settings().fireproof());
    // move this somewhere else
    public static final BlockItem LEVITATOR = add(ParadiseLostBlocks.LEVITATOR);
    // logs
    public static final BlockItem AUREL_LOG = add(ParadiseLostBlocks.AUREL_WOODSTUFF.log());
    public static final BlockItem MOTTLED_AUREL_LOG = add(ParadiseLostBlocks.MOTTLED_AUREL_LOG);
    public static final BlockItem MOTTLED_AUREL_FALLEN_LOG = add(ParadiseLostBlocks.MOTTLED_AUREL_FALLEN_LOG);
    public static final BlockItem MOTHER_AUREL_LOG = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.log());
    public static final BlockItem ORANGE_LOG = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.log());
    public static final BlockItem WISTERIA_LOG = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.log());
    // stripped logs
    public static final BlockItem STRIPPED_AUREL_LOG = add(ParadiseLostBlocks.AUREL_WOODSTUFF.strippedLog());
    public static final BlockItem STRIPPED_MOTHER_AUREL_LOG = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.strippedLog());
    public static final BlockItem STRIPPED_ORANGE_LOG = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.strippedLog());
    public static final BlockItem STRIPPED_WISTERIA_LOG = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.strippedLog());
    // stripped wood
    public static final BlockItem STRIPPED_AUREL_WOOD = add(ParadiseLostBlocks.AUREL_WOODSTUFF.strippedWood());
    public static final BlockItem STRIPPED_MOTHER_AUREL_WOOD = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.strippedWood());
    public static final BlockItem STRIPPED_ORANGE_WOOD = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.strippedWood());
    public static final BlockItem STRIPPED_WISTERIA_WOOD = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.strippedWood());
    // wood
    public static final BlockItem AUREL_WOOD = add(ParadiseLostBlocks.AUREL_WOODSTUFF.wood());
    public static final BlockItem MOTHER_AUREL_WOOD = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.wood());
    public static final BlockItem ORANGE_WOOD = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.wood());
    public static final BlockItem WISTERIA_WOOD = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.wood());
    // glass

    // slabs
    public static final BlockItem AUREL_SLAB = add(ParadiseLostBlocks.AUREL_WOODSTUFF.plankSlab());
    public static final BlockItem MOTHER_AUREL_SLAB = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.plankSlab());
    public static final BlockItem ORANGE_SLAB = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.plankSlab());
    public static final BlockItem WISTERIA_SLAB = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.plankSlab());
    // smooth stuff
    // cobble variants
    public static final BlockItem MOSSY_FLOESTONE = add(ParadiseLostBlocks.MOSSY_FLOESTONE);
    public static final BlockItem GOLDEN_MOSSY_FLOESTONE = add(ParadiseLostBlocks.GOLDEN_MOSSY_FLOESTONE);

    // bricks
    public static final BlockItem FLOESTONE_BRICK = add(ParadiseLostBlocks.FLOESTONE_BRICK);
    public static final BlockItem CHISELED_FLOESTONE = add(ParadiseLostBlocks.CHISELED_FLOESTONE);
    public static final BlockItem SMOOTH_HELIOLITH = add(ParadiseLostBlocks.SMOOTH_HELIOLITH);
    public static final BlockItem LEVITA_BRICK = add(ParadiseLostBlocks.LEVITA_BRICK_SET.block());
    public static final BlockItem GOLDEN_AMBER_TILE = add(ParadiseLostBlocks.GOLDEN_AMBER_TILE);

    // stairs
    public static final BlockItem AUREL_STAIRS = add(ParadiseLostBlocks.AUREL_WOODSTUFF.plankStairs());
    public static final BlockItem MOTHER_AUREL_STAIRS = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.plankStairs());
    public static final BlockItem ORANGE_STAIRS = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.plankStairs());
    public static final BlockItem WISTERIA_STAIRS = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.plankStairs());
    // stone stairs + slabs
    public static final BlockItem FLOESTONE_STAIRS = add(ParadiseLostBlocks.FLOESTONE_STAIRS);
    public static final BlockItem COBBLED_FLOESTONE_STAIRS = add(ParadiseLostBlocks.COBBLED_FLOESTONE_STAIRS);
    public static final BlockItem MOSSY_FLOESTONE_STAIRS = add(ParadiseLostBlocks.MOSSY_FLOESTONE_STAIRS);
    public static final BlockItem HELIOLITH_STAIRS = add(ParadiseLostBlocks.HELIOLITH_STAIRS);
    public static final BlockItem FLOESTONE_BRICK_STAIRS = add(ParadiseLostBlocks.FLOESTONE_BRICK_STAIRS);
    public static final BlockItem SMOOTH_HELIOLITH_STAIRS = add(ParadiseLostBlocks.SMOOTH_HELIOLITH_STAIRS);
    public static final BlockItem LEVITA_BRICK_STAIRS = add(ParadiseLostBlocks.LEVITA_BRICK_SET.stairs());
    public static final BlockItem GOLDEN_AMBER_TILE_STAIRS = add(ParadiseLostBlocks.GOLDEN_AMBER_TILE_STAIRS);

    public static final BlockItem FLOESTONE_SLAB = add(ParadiseLostBlocks.FLOESTONE_SLAB);
    public static final BlockItem COBBLED_FLOESTONE_SLAB = add(ParadiseLostBlocks.COBBLED_FLOESTONE_SLAB);
    public static final BlockItem MOSSY_FLOESTONE_SLAB = add(ParadiseLostBlocks.MOSSY_FLOESTONE_SLAB);
    public static final BlockItem HELIOLITH_SLAB = add(ParadiseLostBlocks.HELIOLITH_SLAB);
    public static final BlockItem FLOESTONE_BRICK_SLAB = add(ParadiseLostBlocks.FLOESTONE_BRICK_SLAB);
    public static final BlockItem SMOOTH_HELIOLITH_SLAB = add(ParadiseLostBlocks.SMOOTH_HELIOLITH_SLAB);
    public static final BlockItem LEVITA_BRICK_SLAB = add(ParadiseLostBlocks.LEVITA_BRICK_SET.slab());
    public static final BlockItem GOLDEN_AMBER_TILE_SLAB = add(ParadiseLostBlocks.GOLDEN_AMBER_TILE_SLAB);

    // saplings
    public static final BlockItem AUREL_SAPLING = add(ParadiseLostBlocks.AUREL_WOODSTUFF.sapling(), compostable30);
    public static final BlockItem MOTHER_AUREL_SAPLING = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.sapling(), compostable30);
    public static final BlockItem ORANGE_SAPLING = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.sapling(), compostable30);
    public static final BlockItem ROSE_WISTERIA_SAPLING = add(ParadiseLostBlocks.ROSE_WISTERIA_SAPLING, compostable30);
    public static final BlockItem FROST_WISTERIA_SAPLING = add(ParadiseLostBlocks.FROST_WISTERIA_SAPLING, compostable30);
    public static final BlockItem LAVENDER_WISTERIA_SAPLING = add(ParadiseLostBlocks.LAVENDER_WISTERIA_SAPLING, compostable30);
    // leaves
    public static final BlockItem AUREL_LEAVES = add(ParadiseLostBlocks.AUREL_WOODSTUFF.leaves(), compostable30);
    public static final BlockItem MOTHER_AUREL_LEAVES = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.leaves(), compostable30);
    public static final BlockItem ORANGE_LEAVES = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.leaves(), compostable30);
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
    public static final BlockItem DRIGEAN = add(ParadiseLostBlocks.DRIGEAN, compostable65);
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

    public static final BlockItem LIVERWORT = add(ParadiseLostBlocks.LIVERWORT, compostable100);
    public static final BlockItem LIVERWORT_CARPET = add(ParadiseLostBlocks.LIVERWORT_CARPET, compostable65);

    public static final BlockItem ROOTCAP = add(ParadiseLostBlocks.ROOTCAP, compostable65);
    public static final BlockItem BROWN_SPORECAP = add(ParadiseLostBlocks.BROWN_SPORECAP, compostable65);
    public static final BlockItem PINK_SPORECAP = add(ParadiseLostBlocks.PINK_SPORECAP, compostable65);

    public static final BlockItem SWEDROOT_SPREAD = add(ParadiseLostBlocks.SWEDROOT_SPREAD, compostable65);

    public static final BlockItem FLAXWEAVE_CUSHION = add(ParadiseLostBlocks.FLAXWEAVE_CUSHION, fuel(300));

    public static final BlockItem CHEESECAKE = add(ParadiseLostBlocks.CHEESECAKE);

    public static final BlockItem AMADRYS_BUNDLE = add(ParadiseLostBlocks.AMADRYS_BUNDLE, compostable85);

    // lights
    public static final BlockItem CHERINE_LANTERN = add(ParadiseLostBlocks.CHERINE_LANTERN);
    public static final VerticallyAttachableBlockItem CHERINE_TORCH = add("cherine_torch", new VerticallyAttachableBlockItem(ParadiseLostBlocks.CHERINE_TORCH, ParadiseLostBlocks.CHERINE_TORCH_WALL, new Settings(), Direction.DOWN));
    // util blocks (enchanter, freezer, etc.)

    // redstone
    public static final BlockItem FLOESTONE_BUTTON = add(ParadiseLostBlocks.FLOESTONE_BUTTON);
    public static final BlockItem FLOESTONE_PRESSURE_PLATE = add(ParadiseLostBlocks.FLOESTONE_PRESSURE_PLATE);

    public static final BlockItem CHERINE_CAMPFIRE = add(ParadiseLostBlocks.CHERINE_CAMPFIRE);

    // door-like things
    public static final BlockItem AUREL_DOOR = add(ParadiseLostBlocks.AUREL_WOODSTUFF.door());
    public static final BlockItem MOTHER_AUREL_DOOR = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.door());
    public static final BlockItem ORANGE_DOOR = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.door());
    public static final BlockItem WISTERIA_DOOR = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.door());

    public static final BlockItem AUREL_TRAPDOOR = add(ParadiseLostBlocks.AUREL_WOODSTUFF.trapdoor());
    public static final BlockItem MOTHER_AUREL_TRAPDOOR = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.trapdoor());
    public static final BlockItem ORANGE_TRAPDOOR = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.trapdoor());
    public static final BlockItem WISTERIA_TRAPDOOR = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.trapdoor());

    public static final BlockItem AUREL_FENCE_GATE = add(ParadiseLostBlocks.AUREL_WOODSTUFF.fenceGate());
    public static final BlockItem MOTHER_AUREL_FENCE_GATE = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.fenceGate());
    public static final BlockItem ORANGE_FENCE_GATE = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.fenceGate());
    public static final BlockItem WISTERIA_FENCE_GATE = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.fenceGate());
    // fences
    public static final BlockItem AUREL_FENCE = add(ParadiseLostBlocks.AUREL_WOODSTUFF.fence());
    public static final BlockItem MOTHER_AUREL_FENCE = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.fence());
    public static final BlockItem ORANGE_FENCE = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.fence());
    public static final BlockItem WISTERIA_FENCE = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.fence());
    // walls
    public static final BlockItem FLOESTONE_WALL = add(ParadiseLostBlocks.FLOESTONE_WALL);
    public static final BlockItem COBBLED_FLOESTONE_WALL = add(ParadiseLostBlocks.COBBLED_FLOESTONE_WALL);
    public static final BlockItem MOSSY_FLOESTONE_WALL = add(ParadiseLostBlocks.MOSSY_FLOESTONE_WALL);
    public static final BlockItem HELIOLITH_WALL = add(ParadiseLostBlocks.HELIOLITH_WALL);
    public static final BlockItem FLOESTONE_BRICK_WALL = add(ParadiseLostBlocks.FLOESTONE_BRICK_WALL);

    // panes & chains
    public static final BlockItem OLVITE_CHAIN = add(ParadiseLostBlocks.OLVITE_CHAIN);
    // carpets

    // path & farmland
    public static final BlockItem FARMLAND = add(ParadiseLostBlocks.FARMLAND);
    public static final BlockItem DIRT_PATH = add(ParadiseLostBlocks.DIRT_PATH);
    public static final BlockItem PERMAFROST_PATH = add(ParadiseLostBlocks.PERMAFROST_PATH);
    // signs, wall stuff.
    private static final Settings sign = new Settings().maxCount(16);
    public static final SignItem AUREL_SIGN = add("aurel_sign", new SignItem(sign, ParadiseLostBlocks.AUREL_SIGNS.sign(), ParadiseLostBlocks.AUREL_SIGNS.wallSign()));
    public static final SignItem AUREL_HANGING_SIGN = add("aurel_hanging_sign", new HangingSignItem(ParadiseLostBlocks.AUREL_SIGNS.hangingSign(), ParadiseLostBlocks.AUREL_SIGNS.wallHangingSign(), sign));
    public static final SignItem MOTHER_AUREL_SIGN = add("mother_aurel_sign", new SignItem(sign, ParadiseLostBlocks.MOTHER_AUREL_SIGNS.sign(), ParadiseLostBlocks.MOTHER_AUREL_SIGNS.wallSign()));
    public static final SignItem MOTHER_AUREL_HANGING_SIGN = add("mother_aurel_hanging_sign", new HangingSignItem(ParadiseLostBlocks.MOTHER_AUREL_SIGNS.hangingSign(), ParadiseLostBlocks.MOTHER_AUREL_SIGNS.wallHangingSign(), sign));
    public static final SignItem ORANGE_SIGN = add("orange_sign", new SignItem(sign, ParadiseLostBlocks.ORANGE_SIGNS.sign(), ParadiseLostBlocks.ORANGE_SIGNS.wallSign()));
    public static final SignItem ORANGE_HANGING_SIGN = add("orange_hanging_sign", new HangingSignItem(ParadiseLostBlocks.ORANGE_SIGNS.hangingSign(), ParadiseLostBlocks.ORANGE_SIGNS.wallHangingSign(), sign));
    public static final SignItem WISTERIA_SIGN = add("wisteria_sign", new SignItem(sign, ParadiseLostBlocks.WISTERIA_SIGNS.sign(), ParadiseLostBlocks.WISTERIA_SIGNS.wallSign()));
    public static final SignItem WISTERIA_HANGING_SIGN = add("wisteria_hanging_sign", new HangingSignItem(ParadiseLostBlocks.WISTERIA_SIGNS.hangingSign(), ParadiseLostBlocks.WISTERIA_SIGNS.wallHangingSign(), sign));
    // beds

    // etc.
    public static final BlockItem AUREL_BUTTON = add(ParadiseLostBlocks.AUREL_WOODSTUFF.button());
    public static final BlockItem MOTHER_AUREL_BUTTON = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.button());
    public static final BlockItem ORANGE_BUTTON = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.button());
    public static final BlockItem WISTERIA_BUTTON = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.button());

    public static final BlockItem AUREL_PRESSURE_PLATE = add(ParadiseLostBlocks.AUREL_WOODSTUFF.pressurePlate());
    public static final BlockItem MOTHER_AUREL_PRESSURE_PLATE = add(ParadiseLostBlocks.MOTHER_AUREL_WOODSTUFF.pressurePlate());
    public static final BlockItem ORANGE_PRESSURE_PLATE = add(ParadiseLostBlocks.ORANGE_WOODSTUFF.pressurePlate());
    public static final BlockItem WISTERIA_PRESSURE_PLATE = add(ParadiseLostBlocks.WISTERIA_WOODSTUFF.pressurePlate());

    public static final BoatSet AUREL_BOATS = addBoatItems("aurel", "PARADISE_LOST_AUREL");
    public static final BoatSet MOTHER_AUREL_BOATS = addBoatItems("mother_aurel", "PARADISE_LOST_MOTHER_AUREL");
    public static final BoatSet ORANGE_BOATS = addBoatItems("orange", "PARADISE_LOST_ORANGE");
    public static final BoatSet WISTERIA_BOATS = addBoatItems("wisteria", "PARADISE_LOST_WISTERIA");

    public static final BoatSet[] BOAT_SETS = new BoatSet[] {AUREL_BOATS, MOTHER_AUREL_BOATS, ORANGE_BOATS, WISTERIA_BOATS};

    public static void init() {
    }

    @SafeVarargs
    private static <V extends Item> V add(String id, V item, Consumer<ItemConvertible>... additionalActions) {
        var registeredItem = Registry.register(Registries.ITEM, locate(id), item);
        for (var action : additionalActions) {
            action.accept(registeredItem);
        }
        return registeredItem;
    }

    @SafeVarargs
    private static <V extends Item> V add(Identifier id, V item, Consumer<ItemConvertible>... additionalActions) {
        var registeredItem = Registry.register(Registries.ITEM, id, item);
        for (var action : additionalActions) {
            action.accept(registeredItem);
        }
        return registeredItem;
    }

    @SafeVarargs
    private static BlockItem add(Block block, Consumer<ItemConvertible>... additionalActions) {
        return add(Registries.BLOCK.getId(block),
                (block instanceof DoorBlock || block instanceof TallPlantBlock)
                        ? new TallBlockItem(block, new Settings())
                        : new BlockItem(block, new Settings()),
                additionalActions);
    }

    @SafeVarargs
    private static BlockItem add(Block block, Settings settings, Consumer<ItemConvertible>... additionalActions) {
        return add(Registries.BLOCK.getId(block),
                (block instanceof DoorBlock || block instanceof TallPlantBlock)
                        ? new TallBlockItem(block, settings)
                        : new BlockItem(block, settings),
                additionalActions);
    }

    private static BoatSet addBoatItems(String woodId, String boatTypeId) {
        String boatId = (MOD_ID + "_" + woodId);

        BoatEntity.Type boatType = ClassTinkerers.getEnum(BoatEntity.Type.class, boatTypeId);

        BoatItem boat = add(woodId + "_boat", new BoatItem(false, boatType, new Settings().maxCount(1)));
        BoatItem chestBoat = add(woodId + "_chest_boat", new BoatItem(true, boatType, new Settings().maxCount(1)));

        return new BoatSet(boatType, boat, chestBoat);
    }

    public record BoatSet(
            BoatEntity.Type type,
            BoatItem boat,
            BoatItem chestBoat
    ) implements Iterable<Item> {
        public @NotNull Iterator<Item> iterator() {
            return Arrays.stream(new Item[]{boat, chestBoat}).iterator();
        }
    }
}
