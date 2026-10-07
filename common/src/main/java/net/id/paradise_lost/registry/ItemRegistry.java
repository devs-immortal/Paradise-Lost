package net.id.paradise_lost.registry;

import net.id.paradise_lost.entity.vehicle.ParadiseLostBoatType;
import net.id.paradise_lost.item.ParadiseLostBoatItem;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.item.armor.ParadiseLostArmorMaterials;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.item.food.ParadiseLostFoodComponent;
import net.id.paradise_lost.item.misc.*;
import net.id.paradise_lost.item.tool.AurelMilkBucketItem;
import net.id.paradise_lost.item.tool.ParadiseLostToolMaterials;
import net.id.paradise_lost.item.tool.AurelBucketItem;
import net.id.paradise_lost.item.tool.SoulSwordItem;
import net.id.paradise_lost.item.tool.WardedJarItem;
import net.id.paradise_lost.item.tool.base_tools.*;
import net.id.paradise_lost.item.tool.bloodstone.CherineBloodstoneItem;
import net.id.paradise_lost.item.tool.bloodstone.SurtrumBloodstoneItem;
import net.id.paradise_lost.item.tool.bloodstone.OlviteBloodstoneItem;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.id.paradise_lost.ModConstants.id;
import static net.id.paradise_lost.item.ParadiseLostItemActions.*;
import static net.minecraft.world.item.Rarity.*;
import net.id.paradise_lost.ModConstants;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static final RegistrationProvider<Item> ITEMS =
            RegistrationProvider.get(Registries.ITEM, ModConstants.MODID);

    private static Properties resource() {
        return new Properties();
    }

    @SuppressWarnings("unchecked")
    private static Properties withSherdPattern(Properties properties, String patternPath) {
        DataComponentType<ResourceLocation> type = (DataComponentType<ResourceLocation>) BuiltInRegistries.DATA_COMPONENT_TYPE.get(
                ResourceLocation.fromNamespaceAndPath("sherdsapi", "sherd_pattern"));
        return properties.component(type, id(patternPath));
    }

    public static final RegistryObject<Item, Item> GOLDEN_AMBER = add("golden_amber", () -> new Item(resource()));
    public static final RegistryObject<Item, Item> CHERINE = add("cherine", () -> new Item(resource()), fuel(500));
    public static final RegistryObject<Item, Item> OLVITE = add("olvite", () -> new Item(resource()));
    public static final RegistryObject<Item, Item> OLVITE_NUGGET = add("olvite_nugget", () -> new Item(resource()));
    public static final RegistryObject<Item, Item> REFINED_SURTRUM = add("refined_surtrum", () -> new Item(resource().fireResistant()));
    public static final RegistryObject<Item, Item> RAW_SURTRUM = add("raw_surtrum", () -> new Item(resource().fireResistant()));
    public static final RegistryObject<Item, Item> LEVITA_GEM = add("levita_gem", () -> new Item(resource()));
    public static final RegistryObject<Item, Item> LEVITA_SHARD = add("levita_shard", () -> new Item(resource()));
    public static final RegistryObject<Item, LevitaArrowItem> LEVITA_ARROW = add("levita_arrow", () -> new LevitaArrowItem(resource()), projectileBehavior);
    public static final RegistryObject<Item, Item> FLAX_THREAD = add("flax_thread", () -> new Item(resource()));
    public static final RegistryObject<Item, Item> FLAXWEAVE = add("flaxweave", () -> new Item(resource()));
    public static final RegistryObject<Item, Item> SWEDROOT_PULP = add("swedroot_pulp", () -> new Item(resource()), compostable30);

    public static final RegistryObject<Item, Item> SOL_POTTERY_SHERD = add("sol_pottery_sherd", () -> new Item(withSherdPattern(resource(), "sol_pottery_pattern")));
    public static final RegistryObject<Item, Item> COO_POTTERY_SHERD = add("coo_pottery_sherd", () -> new Item(withSherdPattern(resource(), "coo_pottery_pattern")));
    private static Properties tool() {
        return new Properties();
    }
    private static Properties shovel(Tier material, float attackDamage, float attackSpeed) {
        return tool().attributes(ShovelItem.createAttributes(material, attackDamage, attackSpeed));
    }
    private static Properties pickaxe(Tier material, float attackDamage, float attackSpeed) {
        return tool().attributes(PickaxeItem.createAttributes(material, attackDamage, attackSpeed));
    }
    private static Properties axe(Tier material, float attackDamage, float attackSpeed) {
        return tool().attributes(AxeItem.createAttributes(material, attackDamage, attackSpeed));
    }
    private static Properties sword(Tier material, int attackDamage, float attackSpeed) {
        return tool().attributes(SwordItem.createAttributes(material, attackDamage, attackSpeed));
    }
    private static Properties hoe(Tier material, float attackDamage, float attackSpeed) {
        return tool().attributes(HoeItem.createAttributes(material, attackDamage, attackSpeed));
    }

    private static final Properties tool = tool();
    private static final Properties rareTool = tool().rarity(RARE);
    private static Properties unstackableTool() {
        return tool().stacksTo(1);
    }
    private static Properties unstackableRareTool() {
        return tool().stacksTo(1).rarity(RARE);
    }

    public static final RegistryObject<Item, ShovelItem> OLVITE_SHOVEL = add("olvite_shovel", () -> new ShovelItem(ParadiseLostToolMaterials.OLVITE, shovel(ParadiseLostToolMaterials.OLVITE, 1.5F, -3F)));
    public static final RegistryObject<Item, PickaxeItem> OLVITE_PICKAXE = add("olvite_pickaxe", () -> new PickaxeItem(ParadiseLostToolMaterials.OLVITE, pickaxe(ParadiseLostToolMaterials.OLVITE, 1F, -2.8F)));
    public static final RegistryObject<Item, AxeItem> OLVITE_AXE = add("olvite_axe", () -> new AxeItem(ParadiseLostToolMaterials.OLVITE, axe(ParadiseLostToolMaterials.OLVITE, 6f, -3.1f)));
    public static final RegistryObject<Item, SwordItem> OLVITE_SWORD = add("olvite_sword", () -> new SwordItem(ParadiseLostToolMaterials.OLVITE, sword(ParadiseLostToolMaterials.OLVITE, 3, -2.4f)));
    public static final RegistryObject<Item, HoeItem> OLVITE_HOE = add("olvite_hoe", () -> new HoeItem(ParadiseLostToolMaterials.OLVITE, hoe(ParadiseLostToolMaterials.OLVITE, -2, -1f)));

    public static final RegistryObject<Item, ShovelItem> SURTRUM_SHOVEL = add("surtrum_shovel", () -> new ShovelItem(ParadiseLostToolMaterials.SURTRUM, shovel(ParadiseLostToolMaterials.SURTRUM, 2.5f, -3f).fireResistant()));
    public static final RegistryObject<Item, PickaxeItem> SURTRUM_PICKAXE = add("surtrum_pickaxe", () -> new PickaxeItem(ParadiseLostToolMaterials.SURTRUM, pickaxe(ParadiseLostToolMaterials.SURTRUM, 2, -2.8f).fireResistant()));
    public static final RegistryObject<Item, AxeItem> SURTRUM_AXE = add("surtrum_axe", () -> new AxeItem(ParadiseLostToolMaterials.SURTRUM, axe(ParadiseLostToolMaterials.SURTRUM, 6f, -3.1f).fireResistant()));
    public static final RegistryObject<Item, SwordItem> SURTRUM_SWORD = add("surtrum_sword", () -> new SwordItem(ParadiseLostToolMaterials.SURTRUM, sword(ParadiseLostToolMaterials.SURTRUM, 4, -2.4f).fireResistant()));
    public static final RegistryObject<Item, HoeItem> SURTRUM_HOE = add("surtrum_hoe", () -> new HoeItem(ParadiseLostToolMaterials.SURTRUM, hoe(ParadiseLostToolMaterials.SURTRUM, -3, 0f).fireResistant()));

    public static final RegistryObject<Item, ShovelItem> GLAZED_GOLD_SHOVEL = add("glazed_gold_shovel", () -> new ShovelItem(ParadiseLostToolMaterials.GLAZED_GOLD, shovel(ParadiseLostToolMaterials.GLAZED_GOLD, 1.5f, -3f)));
    public static final RegistryObject<Item, PickaxeItem> GLAZED_GOLD_PICKAXE = add("glazed_gold_pickaxe", () -> new PickaxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, pickaxe(ParadiseLostToolMaterials.GLAZED_GOLD, 1, -2.8f)));
    public static final RegistryObject<Item, AxeItem> GLAZED_GOLD_AXE = add("glazed_gold_axe", () -> new AxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, axe(ParadiseLostToolMaterials.GLAZED_GOLD, 6f, -3.0f)));
    public static final RegistryObject<Item, SwordItem> GLAZED_GOLD_SWORD = add("glazed_gold_sword", () -> new SwordItem(ParadiseLostToolMaterials.GLAZED_GOLD, sword(ParadiseLostToolMaterials.GLAZED_GOLD, 3, -2.4f)));
    public static final RegistryObject<Item, HoeItem> GLAZED_GOLD_HOE = add("glazed_gold_hoe", () -> new HoeItem(ParadiseLostToolMaterials.GLAZED_GOLD, hoe(ParadiseLostToolMaterials.GLAZED_GOLD, -2, -2.0f)));
    public static final RegistryObject<Item, SwordItem> SOUL_BLADE = add("soul_blade", () -> new SoulSwordItem(ParadiseLostToolMaterials.SOUL_BLADE, sword(ParadiseLostToolMaterials.SOUL_BLADE, 1, -2.8f).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item, SpyglassItem> OLVITE_SPYGLASS = add("olvite_spyglass", () -> new SpyglassItem(unstackableTool()));
    public static final RegistryObject<Item, Item> TOTEM_OF_LEVITATION = add("totem_of_levitation", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item, GravityWandItem> LEVITA_WAND = add("levita_wand", () -> new GravityWandItem(unstackableRareTool().durability(100)));
    public static final RegistryObject<Item, CherineBloodstoneItem> CHERINE_BLOODSTONE = add("cherine_bloodstone", () -> new CherineBloodstoneItem(unstackableTool()));
    public static final RegistryObject<Item, OlviteBloodstoneItem> OLVITE_BLOODSTONE = add("olvite_bloodstone", () -> new OlviteBloodstoneItem(unstackableTool()));
    public static final RegistryObject<Item, SurtrumBloodstoneItem> SURTRUM_BLOODSTONE = add("surtrum_bloodstone", () -> new SurtrumBloodstoneItem(unstackableTool().fireResistant()));
    private static final Component GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component GLAZED_GOLD_UPGRADE_TEXT = Component.translatable(Util.makeDescriptionId("upgrade", id("glazed_gold_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.base_slot_description")));
    private static final Component GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.additions_slot_description")));

    public static final RegistryObject<Item, Item> GLAZED_GOLD_UPGRADE = add("glazed_gold_upgrade_smithing_template", () -> new SmithingTemplateItem(
            GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT, GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT, GLAZED_GOLD_UPGRADE_TEXT, GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
            List.of(ResourceLocation.parse("item/empty_armor_slot_helmet"), ResourceLocation.parse("item/empty_armor_slot_chestplate"), ResourceLocation.parse("item/empty_armor_slot_leggings"), ResourceLocation.parse("item/empty_armor_slot_boots"),
                    ResourceLocation.parse("item/empty_slot_hoe"), ResourceLocation.parse("item/empty_slot_axe"), ResourceLocation.parse("item/empty_slot_sword"), ResourceLocation.parse("item/empty_slot_shovel"), ResourceLocation.parse("item/empty_slot_pickaxe")),
            List.of(ResourceLocation.parse("item/empty_slot_ingot"))
    ));
    private static Properties wearable() {
        return new Properties();
    }

    private static ArmorItem armorHelper(Holder<ArmorMaterial> mat, ArmorItem.Type type, int durabilityMultiplier, Item.Properties settings) {
        return new ArmorItem(mat, type, settings.durability(type.getDurability(durabilityMultiplier)));
    }

    private static ArmorItem armorHelper(Holder<ArmorMaterial> mat, ArmorItem.Type type, int durabilityMultiplier) {
        return armorHelper(mat, type, durabilityMultiplier, wearable());
    }

    public static final RegistryObject<Item, ArmorItem> OLVITE_HELMET = add("olvite_helmet", () -> armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.HELMET, 15));
    public static final RegistryObject<Item, ArmorItem> OLVITE_CHESTPLATE = add("olvite_chestplate", () -> armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.CHESTPLATE, 15));
    public static final RegistryObject<Item, ArmorItem> OLVITE_LEGGINGS = add("olvite_leggings", () -> armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.LEGGINGS, 15));
    public static final RegistryObject<Item, ArmorItem> OLVITE_BOOTS = add("olvite_boots", () -> armorHelper(ParadiseLostArmorMaterials.OLVITE, ArmorItem.Type.BOOTS, 15));
    public static final RegistryObject<Item, ArmorItem> OLVITE_HELMET_ORNATE = add("ornate_olvite_helmet", () -> armorHelper(ParadiseLostArmorMaterials.ORNATE_OLVITE, ArmorItem.Type.HELMET, 15));

    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_HELMET = add("glazed_gold_helmet", () -> armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.HELMET, 21));
    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_CHESTPLATE = add("glazed_gold_chestplate", () -> armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.CHESTPLATE, 21));
    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_LEGGINGS = add("glazed_gold_leggings", () -> armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.LEGGINGS, 21));
    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_BOOTS = add("glazed_gold_boots", () -> armorHelper(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorItem.Type.BOOTS, 21));

    public static final RegistryObject<Item, ArmorItem> SURTRUM_HELMET = add("surtrum_helmet", () -> armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.HELMET, 27, wearable().fireResistant()));
    public static final RegistryObject<Item, ArmorItem> SURTRUM_CHESTPLATE = add("surtrum_chestplate", () -> armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.CHESTPLATE, 27, wearable().fireResistant()));
    public static final RegistryObject<Item, ArmorItem> SURTRUM_LEGGINGS = add("surtrum_leggings", () -> armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.LEGGINGS, 27, wearable().fireResistant()));
    public static final RegistryObject<Item, ArmorItem> SURTRUM_BOOTS = add("surtrum_boots", () -> armorHelper(ParadiseLostArmorMaterials.SURTRUM, ArmorItem.Type.BOOTS, 27, wearable().fireResistant()));

    public static final RegistryObject<Item, XpCircletItem> XP_CIRCLET = add("xp_circlet", () -> new XpCircletItem(ParadiseLostArmorMaterials.RELIC, ArmorItem.Type.HELMET, wearable().durability(ArmorItem.Type.HELMET.getDurability(15)).rarity(RARE)));
    public static final RegistryObject<Item, FloatyLeggingsItem> FLOATY_LEGGINGS = add("floaty_leggings",
            () -> new FloatyLeggingsItem(ParadiseLostArmorMaterials.FLOATY,
                    wearable().durability(ArmorItem.Type.LEGGINGS.getDurability(66)).rarity(UNCOMMON)));
    public static final RegistryObject<Item, FloatyBootsItem> FLOATY_BOOTS = add("floaty_boots",
            () -> new FloatyBootsItem(ParadiseLostArmorMaterials.FLOATY,
                    wearable().durability(ArmorItem.Type.BOOTS.getDurability(66)).rarity(UNCOMMON)));
    private static Properties food() {
        return new Properties();
    }

    private static Properties food(FoodProperties foodComponent) {
        return new Properties().food(foodComponent);
    }

    public static final RegistryObject<Item, ItemNameBlockItem> BLACKCURRANT = add("blackcurrant", () -> new ItemNameBlockItem(BlockRegistry.BLACKCURRANT_BUSH.get(), food(ParadiseLostFoodComponent.BLACKCURRANT)), compostable30);
    public static final RegistryObject<Item, ItemNameBlockItem> AMADRYS_BUSHEL = add("amadrys_bushel", () -> new ItemNameBlockItem(BlockRegistry.AMADRYS.get(), food(ParadiseLostFoodComponent.GENERIC_WORSE)), compostable30);
    public static final RegistryObject<Item, ItemNameBlockItem> NITRA_SEED = add("nitra", () -> new ItemNameBlockItem(BlockRegistry.NITRA.get(), food()), compostable15);
    public static final RegistryObject<Item, Item> NITRA_BULB = add("nitra_bulb", () -> new NitraItem(food()), compostable50);
    public static final RegistryObject<Item, Item> AMADRYS_NOODLES = add("amadrys_noodles", () -> new Item(food(ParadiseLostFoodComponent.AMADRYS_NOODLES)));
    public static final RegistryObject<Item, Item> AMADRYS_BREAD = add("amadrys_bread", () -> new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD)), compostable50);
    public static final RegistryObject<Item, Item> AMADRYS_BREAD_GLAZED = add("amadrys_bread_glazed", () -> new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED)), compostable50);
    public static final RegistryObject<Item, Item> AMADRYS_BREAD_GLAZED_FILLED = add("amadrys_bread_glazed_filled", () -> new Item(food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED_FILLED)), compostable50);
    public static final RegistryObject<Item, ItemNameBlockItem> SWEDROOT = add("swedroot", () -> new ItemNameBlockItem(BlockRegistry.SWEDROOT.get(), food(ParadiseLostFoodComponent.SWEDROOT)), compostable30);
    public static final RegistryObject<Item, Item> BLACKCURRANT_PIE = add("blackcurrant_pie", () -> new Item(food(ParadiseLostFoodComponent.BLACKCURRANT_PIE)), compostable100);
    public static final RegistryObject<Item, Item> BLACKCURRANT_COOKIE = add("blackcurrant_cookie", () -> new Item(food(ParadiseLostFoodComponent.BLACKCURRANT_COOKIE)), compostable85);
    public static final RegistryObject<Item, Item> ROOT_STEW = add("root_stew", () -> new Item(food(ParadiseLostFoodComponent.ROOT_STEW)));
    public static final RegistryObject<Item, ItemNameBlockItem> FLAXSEED = add("flaxseed", () -> new ItemNameBlockItem(BlockRegistry.FLAX.get(), food()), compostable30);
    public static final RegistryObject<Item, Item> MOA_MEAT = add("moa_meat", () -> new Item(food(ParadiseLostFoodComponent.MOA_MEAT)));
    public static final RegistryObject<Item, Item> COOKED_MOA_MEAT = add("moa_meat_cooked", () -> new Item(food(ParadiseLostFoodComponent.COOKED_MOA_MEAT)));
    public static final RegistryObject<Item, Item> POPOM_JELLY = add("popom_jelly", () -> new Item(food(ParadiseLostFoodComponent.POPOM_JELLY)), compostable15);
    public static final RegistryObject<Item, ParadiseLostPortalItem> PARADISE_LOST_PORTAL = add("portal", () -> new ParadiseLostPortalItem(new Properties()));
    public static final RegistryObject<Item, PalaceDoorPlacerItem> PALACE_DOOR_PLACER = add("palace_door_placer", () -> new PalaceDoorPlacerItem(new Properties()));
    public static final RegistryObject<Item, MoaEggItem> MOA_EGG = add("moa_egg", () -> new MoaEggItem(new Properties().stacksTo(1)));
    public static final RegistryObject<Item, AurelBucketItem> AUREL_BUCKET = add("aurel_bucket", () -> new AurelBucketItem(new Properties().stacksTo(16)), fuel(200), emptyBucketBehavior);
    private static Properties aurelBucket() {
        return new Properties().stacksTo(1).craftRemainder(AUREL_BUCKET.get());
    }
    public static final RegistryObject<Item, AurelBucketItem> AUREL_WATER_BUCKET = add("aurel_water_bucket", () -> new AurelBucketItem(Fluids.WATER, aurelBucket()), emptiableBucketBehavior);
    public static final RegistryObject<Item, AurelBucketItem> AUREL_POWDER_SNOW_BUCKET = add("aurel_powder_snow_bucket", () -> new AurelBucketItem(Blocks.POWDER_SNOW, aurelBucket()), emptiableBucketBehavior);
    public static final RegistryObject<Item, AurelMilkBucketItem> AUREL_MILK_BUCKET = add("aurel_milk_bucket", () -> new AurelMilkBucketItem(new Item.Properties().craftRemainder(AUREL_BUCKET.get()).stacksTo(1)));
    public static final RegistryObject<Item, WardedJarItem> WARDED_JAR = add("warded_jar", () -> new WardedJarItem(new Properties()));
    private static Properties wardedJar() {
        return new Properties().stacksTo(1).craftRemainder(WARDED_JAR.get());
    }
    public static final RegistryObject<Item, WardedJarItem> WARDED_JAR_ALLAY = add("warded_jar_allay", () -> new WardedJarItem(EntityType.ALLAY, wardedJar()));
    public static final RegistryObject<Item, WardedJarItem> WARDED_JAR_QUINT = add("warded_jar_quint", () -> new WardedJarItem(EntityRegistry.QUINT.get(), wardedJar()));
    public static final RegistryObject<Item, Item> PALACE_KEY = add("palace_key", () -> new Item(new Properties()));

    public static final RegistryObject<Item, SpawnEggItem> ENVOY_SPAWN_EGG = add("envoy_spawn_egg", () -> new SpawnEggItem(EntityRegistry.ENVOY.get(), 0xc5b1af, 0x993c3c, new Properties()), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> SENTINEL_SPAWN_EGG = add("sentinel_spawn_egg", () -> new SpawnEggItem(EntityRegistry.SENTINEL.get(), 0xf7eeec, 0xffc0bf, new Properties()), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> MOA_SPAWN_EGG = add("moa_spawn_egg", () -> new SpawnEggItem(EntityRegistry.MOA.get(), 0xC55C2E4, 0xB3A8BB, new Properties()), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> POPOM_SPAWN_EGG = add("popom_spawn_egg", () -> new SpawnEggItem(EntityRegistry.POPOM.get(), 0xd984e8, 0xd4d0cf, new Properties()), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> QUINT_SPAWN_EGG = add("quint_spawn_egg", () -> new SpawnEggItem(EntityRegistry.QUINT.get(), 0xd9d0d9, 0xeeebf0, new Properties()), spawnEggBehavior);

    public static final RegistryObject<Item, StandingAndWallBlockItem> CHERINE_TORCH = add("cherine_torch", () -> new StandingAndWallBlockItem(BlockRegistry.CHERINE_TORCH.get(), BlockRegistry.CHERINE_TORCH_WALL.get(), new Properties(), Direction.DOWN));

    private static final Properties sign = new Properties().stacksTo(16);
    public static final RegistryObject<Item, SignItem> AUREL_SIGN = add("aurel_sign", () -> new SignItem(sign, BlockRegistry.AUREL_SIGNS.sign().get(), BlockRegistry.AUREL_SIGNS.wallSign().get()), fuel(200));
    public static final RegistryObject<Item, SignItem> AUREL_HANGING_SIGN = add("aurel_hanging_sign", () -> new HangingSignItem(BlockRegistry.AUREL_SIGNS.hangingSign().get(), BlockRegistry.AUREL_SIGNS.wallHangingSign().get(), sign), fuel(200));
    public static final RegistryObject<Item, SignItem> MOTHER_AUREL_SIGN = add("mother_aurel_sign", () -> new SignItem(sign, BlockRegistry.MOTHER_AUREL_SIGNS.sign().get(), BlockRegistry.MOTHER_AUREL_SIGNS.wallSign().get()), fuel(200));
    public static final RegistryObject<Item, SignItem> MOTHER_AUREL_HANGING_SIGN = add("mother_aurel_hanging_sign", () -> new HangingSignItem(BlockRegistry.MOTHER_AUREL_SIGNS.hangingSign().get(), BlockRegistry.MOTHER_AUREL_SIGNS.wallHangingSign().get(), sign), fuel(200));
    public static final RegistryObject<Item, SignItem> MENTH_SIGN = add("menth_sign", () -> new SignItem(sign, BlockRegistry.MENTH_SIGNS.sign().get(), BlockRegistry.MENTH_SIGNS.wallSign().get()), fuel(200));
    public static final RegistryObject<Item, SignItem> MENTH_HANGING_SIGN = add("menth_hanging_sign", () -> new HangingSignItem(BlockRegistry.MENTH_SIGNS.hangingSign().get(), BlockRegistry.MENTH_SIGNS.wallHangingSign().get(), sign), fuel(200));
    public static final RegistryObject<Item, SignItem> WISTERIA_SIGN = add("wisteria_sign", () -> new SignItem(sign, BlockRegistry.WISTERIA_SIGNS.sign().get(), BlockRegistry.WISTERIA_SIGNS.wallSign().get()));
    public static final RegistryObject<Item, SignItem> WISTERIA_HANGING_SIGN = add("wisteria_hanging_sign", () -> new HangingSignItem(BlockRegistry.WISTERIA_SIGNS.hangingSign().get(), BlockRegistry.WISTERIA_SIGNS.wallHangingSign().get(), sign), fuel(200));

    public static BoatSet AUREL_BOATS;
    public static BoatSet MOTHER_AUREL_BOATS;
    public static BoatSet MENTH_BOATS;
    public static BoatSet WISTERIA_BOATS;

    public static BoatSet[] BOAT_SETS;

    private static final RegistrationProvider<Potion> POTIONS =
            RegistrationProvider.get(Registries.POTION, ModConstants.MODID);

    public static final Holder<Potion> HEALTH_BOOST_POTION = registerPotion("health_boost", () -> new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 1)));
    public static final Holder<Potion> LONG_HEALTH_BOOST_POTION = registerPotion(
            "long_health_boost", () -> new Potion("health_boost", new MobEffectInstance(MobEffects.HEALTH_BOOST, 12000, 1))
    );
    public static final Holder<Potion> STRONG_HEALTH_BOOST_POTION = registerPotion(
            "strong_health_boost", () -> new Potion("health_boost", new MobEffectInstance(MobEffects.HEALTH_BOOST, 3000, 3))
    );

    public static void init() {
        AUREL_BOATS = addBoatItems(ParadiseLostBoatType.AUREL);
        MOTHER_AUREL_BOATS = addBoatItems(ParadiseLostBoatType.MOTHER_AUREL);
        MENTH_BOATS = addBoatItems(ParadiseLostBoatType.MENTH);
        WISTERIA_BOATS = addBoatItems(ParadiseLostBoatType.WISTERIA);
        BOAT_SETS = new BoatSet[] {AUREL_BOATS, MOTHER_AUREL_BOATS, MENTH_BOATS, WISTERIA_BOATS};
    }

    @SafeVarargs
    private static <V extends Item> RegistryObject<Item, V> add(String id, Supplier<V> item, Consumer<ItemLike>... additionalActions) {
        return ITEMS.register(id, () -> {
            V i = item.get();
            for (var action : additionalActions) {
                action.accept(i);
            }
            return i;
        });
    }

    private static BoatSet addBoatItems(ParadiseLostBoatType type) {
        String woodId = type.getName();
        ParadiseLostBoatType wood = type;

        RegistryObject<Item, ParadiseLostBoatItem> boat =
                add(woodId + "_boat", () -> new ParadiseLostBoatItem(wood, false, new Properties().stacksTo(1)), fuel(1200));
        RegistryObject<Item, ParadiseLostBoatItem> chestBoat =
                add(woodId + "_chest_boat", () -> new ParadiseLostBoatItem(wood, true, new Properties().stacksTo(1)), fuel(1200));

        return new BoatSet(type, boat, chestBoat);
    }

    public record BoatSet(
            ParadiseLostBoatType type,
            RegistryObject<Item, ParadiseLostBoatItem> boat,
            RegistryObject<Item, ParadiseLostBoatItem> chestBoat
    ) implements Iterable<Item> {
        public @NotNull Iterator<Item> iterator() {
            return Arrays.stream(new Item[]{boat.get(), chestBoat.get()}).iterator();
        }
    }

    private static Holder<Potion> registerPotion(String name, Supplier<Potion> potion) {
        return POTIONS.register(name, potion).asHolder();
    }
}
