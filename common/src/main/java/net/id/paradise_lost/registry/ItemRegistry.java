package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.item.armor.FloatyBootsItem;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.id.paradise_lost.item.armor.ParadiseLostArmorMaterials;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.id.paradise_lost.item.food.ParadiseLostFoodComponent;
import net.id.paradise_lost.item.misc.*;
import net.id.paradise_lost.item.tool.AurelBucketItem;
import net.id.paradise_lost.item.tool.AurelMilkBucketItem;
import net.id.paradise_lost.item.tool.ParadiseLostToolMaterials;
import net.id.paradise_lost.item.tool.SoulSwordItem;
import net.id.paradise_lost.item.tool.WardedJarItem;
import net.id.paradise_lost.item.tool.base_tools.*;
import net.id.paradise_lost.item.tool.bloodstone.CherineBloodstoneItem;
import net.id.paradise_lost.item.tool.bloodstone.OlviteBloodstoneItem;
import net.id.paradise_lost.item.tool.bloodstone.SurtrumBloodstoneItem;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.id.paradise_lost.ModConstants.id;
import static net.id.paradise_lost.item.ParadiseLostItemActions.*;
import static net.minecraft.world.item.Rarity.*;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static final RegistrationProvider<Item> ITEMS =
            RegistrationProvider.get(Registries.ITEM, ModConstants.MODID);

    private static Properties resource() {
        return new Properties();
    }

    @SuppressWarnings("unchecked")
    private static Properties withSherdPattern(Properties properties, String patternPath) {
        DataComponentType<ResourceLocation> type = (DataComponentType<ResourceLocation>) BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(
                ResourceLocation.fromNamespaceAndPath("sherdsapi", "sherd_pattern"));
        return properties.component(type, id(patternPath));
    }

    public static final RegistryObject<Item, Item> GOLDEN_AMBER = add("golden_amber", Item::new, resource());
    public static final RegistryObject<Item, Item> CHERINE = add("cherine", Item::new, resource(), fuel(500));
    public static final RegistryObject<Item, Item> OLVITE = add("olvite", Item::new, resource());
    public static final RegistryObject<Item, Item> OLVITE_NUGGET = add("olvite_nugget", Item::new, resource());
    public static final RegistryObject<Item, Item> REFINED_SURTRUM = add("refined_surtrum", Item::new, resource().fireResistant());
    public static final RegistryObject<Item, Item> RAW_SURTRUM = add("raw_surtrum", Item::new, resource().fireResistant());
    public static final RegistryObject<Item, Item> LEVITA_GEM = add("levita_gem", Item::new, resource());
    public static final RegistryObject<Item, Item> LEVITA_SHARD = add("levita_shard", Item::new, resource());
    public static final RegistryObject<Item, LevitaArrowItem> LEVITA_ARROW = add("levita_arrow", LevitaArrowItem::new, resource(), projectileBehavior);
    public static final RegistryObject<Item, Item> FLAX_THREAD = add("flax_thread", Item::new, resource());
    public static final RegistryObject<Item, Item> FLAXWEAVE = add("flaxweave", Item::new, resource());
    public static final RegistryObject<Item, Item> SWEDROOT_PULP = add("swedroot_pulp", Item::new, resource(), compostable30);

    public static final RegistryObject<Item, Item> SOL_POTTERY_SHERD = add("sol_pottery_sherd", Item::new, () -> withSherdPattern(resource(), "sol_pottery_pattern"));
    public static final RegistryObject<Item, Item> COO_POTTERY_SHERD = add("coo_pottery_sherd", Item::new, () -> withSherdPattern(resource(), "coo_pottery_pattern"));
    private static Properties tool() {
        return new Properties();
    }
    private static Properties unstackableTool() {
        return tool().stacksTo(1);
    }
    private static Properties unstackableRareTool() {
        return tool().stacksTo(1).rarity(RARE);
    }

    public static final RegistryObject<Item, ShovelItem> OLVITE_SHOVEL = add("olvite_shovel", settings -> new ShovelItem(ParadiseLostToolMaterials.OLVITE, 1.5F, -3F, settings), tool());
    public static final RegistryObject<Item, PickaxeItem> OLVITE_PICKAXE = add("olvite_pickaxe", settings -> new PickaxeItem(ParadiseLostToolMaterials.OLVITE, 1F, -2.8F, settings), tool());
    public static final RegistryObject<Item, AxeItem> OLVITE_AXE = add("olvite_axe", settings -> new AxeItem(ParadiseLostToolMaterials.OLVITE, 6f, -3.1f, settings), tool());
    public static final RegistryObject<Item, SwordItem> OLVITE_SWORD = add("olvite_sword", settings -> new SwordItem(ParadiseLostToolMaterials.OLVITE, 3, -2.4f, settings), tool());
    public static final RegistryObject<Item, HoeItem> OLVITE_HOE = add("olvite_hoe", settings -> new HoeItem(ParadiseLostToolMaterials.OLVITE, -2, -1f, settings), tool());

    public static final RegistryObject<Item, ShovelItem> SURTRUM_SHOVEL = add("surtrum_shovel", settings -> new ShovelItem(ParadiseLostToolMaterials.SURTRUM, 2.5f, -3f, settings), tool().fireResistant());
    public static final RegistryObject<Item, PickaxeItem> SURTRUM_PICKAXE = add("surtrum_pickaxe", settings -> new PickaxeItem(ParadiseLostToolMaterials.SURTRUM, 2, -2.8f, settings), tool().fireResistant());
    public static final RegistryObject<Item, AxeItem> SURTRUM_AXE = add("surtrum_axe", settings -> new AxeItem(ParadiseLostToolMaterials.SURTRUM, 6f, -3.1f, settings), tool().fireResistant());
    public static final RegistryObject<Item, SwordItem> SURTRUM_SWORD = add("surtrum_sword", settings -> new SwordItem(ParadiseLostToolMaterials.SURTRUM, 4, -2.4f, settings), tool().fireResistant());
    public static final RegistryObject<Item, HoeItem> SURTRUM_HOE = add("surtrum_hoe", settings -> new HoeItem(ParadiseLostToolMaterials.SURTRUM, -3, 0f, settings), tool().fireResistant());

    public static final RegistryObject<Item, ShovelItem> GLAZED_GOLD_SHOVEL = add("glazed_gold_shovel", settings -> new ShovelItem(ParadiseLostToolMaterials.GLAZED_GOLD, 1.5f, -3f, settings), tool());
    public static final RegistryObject<Item, PickaxeItem> GLAZED_GOLD_PICKAXE = add("glazed_gold_pickaxe", settings -> new PickaxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, 1, -2.8f, settings), tool());
    public static final RegistryObject<Item, AxeItem> GLAZED_GOLD_AXE = add("glazed_gold_axe", settings -> new AxeItem(ParadiseLostToolMaterials.GLAZED_GOLD, 6f, -3.0f, settings), tool());
    public static final RegistryObject<Item, SwordItem> GLAZED_GOLD_SWORD = add("glazed_gold_sword", settings -> new SwordItem(ParadiseLostToolMaterials.GLAZED_GOLD, 3, -2.4f, settings), tool());
    public static final RegistryObject<Item, HoeItem> GLAZED_GOLD_HOE = add("glazed_gold_hoe", settings -> new HoeItem(ParadiseLostToolMaterials.GLAZED_GOLD, -2, -2.0f, settings), tool());
    public static final RegistryObject<Item, SwordItem> SOUL_BLADE = add("soul_blade", settings -> new SoulSwordItem(ParadiseLostToolMaterials.SOUL_BLADE, 1, -2.8f, settings), tool().rarity(Rarity.EPIC));

    public static final RegistryObject<Item, SpyglassItem> OLVITE_SPYGLASS = add("olvite_spyglass", SpyglassItem::new, unstackableTool().overrideModel(id("olvite_spyglass_in_hand")));
    public static final RegistryObject<Item, Item> TOTEM_OF_LEVITATION = add("totem_of_levitation", Item::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final RegistryObject<Item, GravityWandItem> LEVITA_WAND = add("levita_wand", GravityWandItem::new, unstackableRareTool().durability(100));
    public static final RegistryObject<Item, CherineBloodstoneItem> CHERINE_BLOODSTONE = add("cherine_bloodstone", CherineBloodstoneItem::new, unstackableTool());
    public static final RegistryObject<Item, OlviteBloodstoneItem> OLVITE_BLOODSTONE = add("olvite_bloodstone", OlviteBloodstoneItem::new, unstackableTool());
    public static final RegistryObject<Item, SurtrumBloodstoneItem> SURTRUM_BLOODSTONE = add("surtrum_bloodstone", SurtrumBloodstoneItem::new, unstackableTool().fireResistant());
    private static final Component GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.base_slot_description")));
    private static final Component GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", id("smithing_template.glazed_gold_upgrade.additions_slot_description")));

    public static final RegistryObject<Item, Item> GLAZED_GOLD_UPGRADE = add("glazed_gold_upgrade_smithing_template", settings -> new SmithingTemplateItem(
            GLAZED_GOLD_UPGRADE_APPLIES_TO_TEXT, GLAZED_GOLD_UPGRADE_INGREDIENTS_TEXT, GLAZED_GOLD_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, GLAZED_GOLD_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
            List.of(ResourceLocation.parse("item/empty_armor_slot_helmet"), ResourceLocation.parse("item/empty_armor_slot_chestplate"), ResourceLocation.parse("item/empty_armor_slot_leggings"), ResourceLocation.parse("item/empty_armor_slot_boots"),
                    ResourceLocation.parse("item/empty_slot_hoe"), ResourceLocation.parse("item/empty_slot_axe"), ResourceLocation.parse("item/empty_slot_sword"), ResourceLocation.parse("item/empty_slot_shovel"), ResourceLocation.parse("item/empty_slot_pickaxe")),
            List.of(ResourceLocation.parse("item/empty_slot_ingot")),
            settings
    ), resource());
    private static Properties wearable() {
        return new Properties();
    }

    private static Function<Properties, ArmorItem> armor(ArmorMaterial material, ArmorType type) {
        return settings -> new ArmorItem(material, type, settings);
    }

    public static final RegistryObject<Item, ArmorItem> OLVITE_HELMET = add("olvite_helmet", armor(ParadiseLostArmorMaterials.OLVITE, ArmorType.HELMET), wearable());
    public static final RegistryObject<Item, ArmorItem> OLVITE_CHESTPLATE = add("olvite_chestplate", armor(ParadiseLostArmorMaterials.OLVITE, ArmorType.CHESTPLATE), wearable());
    public static final RegistryObject<Item, ArmorItem> OLVITE_LEGGINGS = add("olvite_leggings", armor(ParadiseLostArmorMaterials.OLVITE, ArmorType.LEGGINGS), wearable());
    public static final RegistryObject<Item, ArmorItem> OLVITE_BOOTS = add("olvite_boots", armor(ParadiseLostArmorMaterials.OLVITE, ArmorType.BOOTS), wearable());
    public static final RegistryObject<Item, ArmorItem> OLVITE_HELMET_ORNATE = add("ornate_olvite_helmet", armor(ParadiseLostArmorMaterials.ORNATE_OLVITE, ArmorType.HELMET), wearable());

    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_HELMET = add("glazed_gold_helmet", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorType.HELMET), wearable());
    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_CHESTPLATE = add("glazed_gold_chestplate", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorType.CHESTPLATE), wearable());
    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_LEGGINGS = add("glazed_gold_leggings", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorType.LEGGINGS), wearable());
    public static final RegistryObject<Item, ArmorItem> GLAZED_GOLD_BOOTS = add("glazed_gold_boots", armor(ParadiseLostArmorMaterials.GLAZED_GOLD, ArmorType.BOOTS), wearable());

    public static final RegistryObject<Item, ArmorItem> SURTRUM_HELMET = add("surtrum_helmet", armor(ParadiseLostArmorMaterials.SURTRUM, ArmorType.HELMET), wearable().fireResistant());
    public static final RegistryObject<Item, ArmorItem> SURTRUM_CHESTPLATE = add("surtrum_chestplate", armor(ParadiseLostArmorMaterials.SURTRUM, ArmorType.CHESTPLATE), wearable().fireResistant());
    public static final RegistryObject<Item, ArmorItem> SURTRUM_LEGGINGS = add("surtrum_leggings", armor(ParadiseLostArmorMaterials.SURTRUM, ArmorType.LEGGINGS), wearable().fireResistant());
    public static final RegistryObject<Item, ArmorItem> SURTRUM_BOOTS = add("surtrum_boots", armor(ParadiseLostArmorMaterials.SURTRUM, ArmorType.BOOTS), wearable().fireResistant());

    public static final RegistryObject<Item, XpCircletItem> XP_CIRCLET = add("xp_circlet", settings -> new XpCircletItem(ParadiseLostArmorMaterials.RELIC, ArmorType.HELMET, settings), wearable().rarity(RARE));
    public static final RegistryObject<Item, FloatyLeggingsItem> FLOATY_LEGGINGS = add("floaty_leggings",
            settings -> new FloatyLeggingsItem(ParadiseLostArmorMaterials.FLOATY, settings), wearable().rarity(UNCOMMON));
    public static final RegistryObject<Item, FloatyBootsItem> FLOATY_BOOTS = add("floaty_boots",
            settings -> new FloatyBootsItem(ParadiseLostArmorMaterials.FLOATY, settings), wearable().rarity(UNCOMMON));
    private static Properties food() {
        return new Properties();
    }

    private static Properties food(FoodProperties foodComponent) {
        return new Properties().food(foodComponent);
    }

    private static Properties food(FoodProperties foodComponent, Consumable consumable) {
        return new Properties().food(foodComponent, consumable);
    }

    private static Function<Properties, BlockItem> blockItem(Supplier<? extends Block> block) {
        return settings -> new BlockItem(block.get(), settings);
    }

    public static final RegistryObject<Item, BlockItem> BLACKCURRANT = add("blackcurrant", blockItem(BlockRegistry.BLACKCURRANT_BUSH), food(ParadiseLostFoodComponent.BLACKCURRANT, ParadiseLostFoodComponent.BLACKCURRANT_CONSUMABLE), compostable30);
    public static final RegistryObject<Item, BlockItem> AMADRYS_BUSHEL = add("amadrys_bushel", blockItem(BlockRegistry.AMADRYS), food(ParadiseLostFoodComponent.GENERIC_WORSE, ParadiseLostFoodComponent.GENERIC_WORSE_CONSUMABLE), compostable30);
    public static final RegistryObject<Item, BlockItem> NITRA_SEED = add("nitra", blockItem(BlockRegistry.NITRA), food(), compostable15);
    public static final RegistryObject<Item, Item> NITRA_BULB = add("nitra_bulb", NitraItem::new, food(), compostable50);
    public static final RegistryObject<Item, Item> AMADRYS_NOODLES = add("amadrys_noodles", Item::new, food(ParadiseLostFoodComponent.AMADRYS_NOODLES).usingConvertsTo(Items.BOWL));
    public static final RegistryObject<Item, Item> AMADRYS_BREAD = add("amadrys_bread", Item::new, food(ParadiseLostFoodComponent.AMADRYS_BREAD), compostable50);
    public static final RegistryObject<Item, Item> AMADRYS_BREAD_GLAZED = add("amadrys_bread_glazed", Item::new, food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED), compostable50);
    public static final RegistryObject<Item, Item> AMADRYS_BREAD_GLAZED_FILLED = add("amadrys_bread_glazed_filled", Item::new, food(ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED_FILLED, ParadiseLostFoodComponent.AMADRYS_BREAD_GLAZED_FILLED_CONSUMABLE), compostable50);
    public static final RegistryObject<Item, BlockItem> SWEDROOT = add("swedroot", blockItem(BlockRegistry.SWEDROOT), food(ParadiseLostFoodComponent.SWEDROOT), compostable30);
    public static final RegistryObject<Item, Item> BLACKCURRANT_PIE = add("blackcurrant_pie", Item::new, food(ParadiseLostFoodComponent.BLACKCURRANT_PIE), compostable100);
    public static final RegistryObject<Item, Item> BLACKCURRANT_COOKIE = add("blackcurrant_cookie", Item::new, food(ParadiseLostFoodComponent.BLACKCURRANT_COOKIE), compostable85);
    public static final RegistryObject<Item, OminousCookieItem> OMINOUS_COOKIE = add("ominous_cookie", OminousCookieItem::new, food(ParadiseLostFoodComponent.OMINOUS_COOKIE), compostable85);
    public static final RegistryObject<Item, Item> ROOT_STEW = add("root_stew", Item::new, food(ParadiseLostFoodComponent.ROOT_STEW).usingConvertsTo(Items.BOWL));
    public static final RegistryObject<Item, BlockItem> FLAXSEED = add("flaxseed", blockItem(BlockRegistry.FLAX), food(), compostable30);
    public static final RegistryObject<Item, Item> MOA_MEAT = add("moa_meat", Item::new, food(ParadiseLostFoodComponent.MOA_MEAT));
    public static final RegistryObject<Item, Item> COOKED_MOA_MEAT = add("moa_meat_cooked", Item::new, food(ParadiseLostFoodComponent.COOKED_MOA_MEAT));
    public static final RegistryObject<Item, Item> POPOM_JELLY = add("popom_jelly", Item::new, food(ParadiseLostFoodComponent.POPOM_JELLY, ParadiseLostFoodComponent.POPOM_JELLY_CONSUMABLE), compostable15);
    public static final RegistryObject<Item, ParadiseLostPortalItem> PARADISE_LOST_PORTAL = add("portal", ParadiseLostPortalItem::new, new Properties());
    public static final RegistryObject<Item, PalaceDoorPlacerItem> PALACE_DOOR_PLACER = add("palace_door_placer", PalaceDoorPlacerItem::new, new Properties());
    public static final RegistryObject<Item, MoaEggItem> MOA_EGG = add("moa_egg", MoaEggItem::new, new Properties().stacksTo(1));
    public static final RegistryObject<Item, AurelBucketItem> AUREL_BUCKET = add("aurel_bucket", AurelBucketItem::new, new Properties().stacksTo(16), fuel(200), emptyBucketBehavior);
    private static Properties aurelBucket() {
        return new Properties().stacksTo(1).craftRemainder(AUREL_BUCKET.get());
    }
    public static final RegistryObject<Item, AurelBucketItem> AUREL_WATER_BUCKET = add("aurel_water_bucket", settings -> new AurelBucketItem(Fluids.WATER, settings), ItemRegistry::aurelBucket, emptiableBucketBehavior);
    public static final RegistryObject<Item, AurelBucketItem> AUREL_POWDER_SNOW_BUCKET = add("aurel_powder_snow_bucket", settings -> new AurelBucketItem(Blocks.POWDER_SNOW, settings), ItemRegistry::aurelBucket, emptiableBucketBehavior);
    public static final RegistryObject<Item, AurelMilkBucketItem> AUREL_MILK_BUCKET = add("aurel_milk_bucket", AurelMilkBucketItem::new,
            () -> new Item.Properties().craftRemainder(AUREL_BUCKET.get()).component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).usingConvertsTo(AUREL_BUCKET.get()).stacksTo(1));
    public static final RegistryObject<Item, WardedJarItem> WARDED_JAR = add("warded_jar", WardedJarItem::new, new Properties());
    private static Properties wardedJar() {
        return new Properties().stacksTo(1).craftRemainder(WARDED_JAR.get());
    }
    public static final RegistryObject<Item, WardedJarItem> WARDED_JAR_ALLAY = add("warded_jar_allay", settings -> new WardedJarItem(EntityType.ALLAY, settings), ItemRegistry::wardedJar);
    public static final RegistryObject<Item, WardedJarItem> WARDED_JAR_QUINT = add("warded_jar_quint", settings -> new WardedJarItem(EntityRegistry.QUINT.get(), settings), ItemRegistry::wardedJar);
    public static final RegistryObject<Item, Item> PALACE_KEY = add("palace_key", Item::new, new Properties());

    public static final RegistryObject<Item, SpawnEggItem> ENVOY_SPAWN_EGG = add("envoy_spawn_egg", settings -> new SpawnEggItem(EntityRegistry.ENVOY.get(), 0xc5b1af, 0x993c3c, settings), new Properties(), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> SENTINEL_SPAWN_EGG = add("sentinel_spawn_egg", settings -> new SpawnEggItem(EntityRegistry.SENTINEL.get(), 0xf7eeec, 0xffc0bf, settings), new Properties(), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> MOA_SPAWN_EGG = add("moa_spawn_egg", settings -> new SpawnEggItem(EntityRegistry.MOA.get(), 0xC55C2E4, 0xB3A8BB, settings), new Properties(), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> POPOM_SPAWN_EGG = add("popom_spawn_egg", settings -> new SpawnEggItem(EntityRegistry.POPOM.get(), 0xd984e8, 0xd4d0cf, settings), new Properties(), spawnEggBehavior);
    public static final RegistryObject<Item, SpawnEggItem> QUINT_SPAWN_EGG = add("quint_spawn_egg", settings -> new SpawnEggItem(EntityRegistry.QUINT.get(), 0xd9d0d9, 0xeeebf0, settings), new Properties(), spawnEggBehavior);

    public static final RegistryObject<Item, StandingAndWallBlockItem> CHERINE_TORCH = add("cherine_torch", settings -> new StandingAndWallBlockItem(BlockRegistry.CHERINE_TORCH.get(), BlockRegistry.CHERINE_TORCH_WALL.get(), Direction.DOWN, settings), new Properties().useBlockDescriptionPrefix());

    private static Properties sign() {
        return new Properties().stacksTo(16).useBlockDescriptionPrefix();
    }
    public static final RegistryObject<Item, SignItem> AUREL_SIGN = add("aurel_sign", settings -> new SignItem(BlockRegistry.AUREL_SIGNS.sign().get(), BlockRegistry.AUREL_SIGNS.wallSign().get(), settings), sign(), fuel(200));
    public static final RegistryObject<Item, SignItem> AUREL_HANGING_SIGN = add("aurel_hanging_sign", settings -> new HangingSignItem(BlockRegistry.AUREL_SIGNS.hangingSign().get(), BlockRegistry.AUREL_SIGNS.wallHangingSign().get(), settings), sign(), fuel(200));
    public static final RegistryObject<Item, SignItem> MOTHER_AUREL_SIGN = add("mother_aurel_sign", settings -> new SignItem(BlockRegistry.MOTHER_AUREL_SIGNS.sign().get(), BlockRegistry.MOTHER_AUREL_SIGNS.wallSign().get(), settings), sign(), fuel(200));
    public static final RegistryObject<Item, SignItem> MOTHER_AUREL_HANGING_SIGN = add("mother_aurel_hanging_sign", settings -> new HangingSignItem(BlockRegistry.MOTHER_AUREL_SIGNS.hangingSign().get(), BlockRegistry.MOTHER_AUREL_SIGNS.wallHangingSign().get(), settings), sign(), fuel(200));
    public static final RegistryObject<Item, SignItem> MENTH_SIGN = add("menth_sign", settings -> new SignItem(BlockRegistry.MENTH_SIGNS.sign().get(), BlockRegistry.MENTH_SIGNS.wallSign().get(), settings), sign(), fuel(200));
    public static final RegistryObject<Item, SignItem> MENTH_HANGING_SIGN = add("menth_hanging_sign", settings -> new HangingSignItem(BlockRegistry.MENTH_SIGNS.hangingSign().get(), BlockRegistry.MENTH_SIGNS.wallHangingSign().get(), settings), sign(), fuel(200));
    public static final RegistryObject<Item, SignItem> WISTERIA_SIGN = add("wisteria_sign", settings -> new SignItem(BlockRegistry.WISTERIA_SIGNS.sign().get(), BlockRegistry.WISTERIA_SIGNS.wallSign().get(), settings), sign());
    public static final RegistryObject<Item, SignItem> WISTERIA_HANGING_SIGN = add("wisteria_hanging_sign", settings -> new HangingSignItem(BlockRegistry.WISTERIA_SIGNS.hangingSign().get(), BlockRegistry.WISTERIA_SIGNS.wallHangingSign().get(), settings), sign(), fuel(200));

    public static BoatSet AUREL_BOATS;
    public static BoatSet MOTHER_AUREL_BOATS;
    public static BoatSet MENTH_BOATS;
    public static BoatSet WISTERIA_BOATS;

    public static BoatSet[] BOAT_SETS;

    private static final RegistrationProvider<Potion> POTIONS =
            RegistrationProvider.get(Registries.POTION, ModConstants.MODID);

    public static final Holder<Potion> HEALTH_BOOST_POTION = registerPotion("health_boost", () -> new Potion("health_boost", new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 1)));
    public static final Holder<Potion> LONG_HEALTH_BOOST_POTION = registerPotion(
            "long_health_boost", () -> new Potion("health_boost", new MobEffectInstance(MobEffects.HEALTH_BOOST, 12000, 1))
    );
    public static final Holder<Potion> STRONG_HEALTH_BOOST_POTION = registerPotion(
            "strong_health_boost", () -> new Potion("health_boost", new MobEffectInstance(MobEffects.HEALTH_BOOST, 3000, 3))
    );

    public static void init() {
        AUREL_BOATS = addBoatItems("aurel", EntityRegistry.AUREL_BOAT, EntityRegistry.AUREL_CHEST_BOAT);
        MOTHER_AUREL_BOATS = addBoatItems("mother_aurel", EntityRegistry.MOTHER_AUREL_BOAT, EntityRegistry.MOTHER_AUREL_CHEST_BOAT);
        MENTH_BOATS = addBoatItems("menth", EntityRegistry.MENTH_BOAT, EntityRegistry.MENTH_CHEST_BOAT);
        WISTERIA_BOATS = addBoatItems("wisteria", EntityRegistry.WISTERIA_BOAT, EntityRegistry.WISTERIA_CHEST_BOAT);
        BOAT_SETS = new BoatSet[] {AUREL_BOATS, MOTHER_AUREL_BOATS, MENTH_BOATS, WISTERIA_BOATS};
    }

    @SafeVarargs
    private static <V extends Item> RegistryObject<Item, V> add(String id, Function<Properties, V> factory, Properties settings, Consumer<ItemLike>... additionalActions) {
        return add(id, factory, () -> settings, additionalActions);
    }

    @SafeVarargs
    private static <V extends Item> RegistryObject<Item, V> add(String id, Function<Properties, V> factory, Supplier<Properties> settings, Consumer<ItemLike>... additionalActions) {
        return ITEMS.register(id, () -> {
            V i = factory.apply(settings.get().setId(ResourceKey.create(Registries.ITEM, id(id))));
            for (var action : additionalActions) {
                action.accept(i);
            }
            return i;
        });
    }

    private static BoatSet addBoatItems(String woodId, Supplier<? extends EntityType<? extends AbstractBoat>> boatType, Supplier<? extends EntityType<? extends AbstractBoat>> chestBoatType) {
        RegistryObject<Item, BoatItem> boat =
                add(woodId + "_boat", settings -> new BoatItem(boatType.get(), settings), new Properties().stacksTo(1), fuel(1200));
        RegistryObject<Item, BoatItem> chestBoat =
                add(woodId + "_chest_boat", settings -> new BoatItem(chestBoatType.get(), settings), new Properties().stacksTo(1), fuel(1200));

        return new BoatSet(boat, chestBoat);
    }

    public record BoatSet(
            RegistryObject<Item, BoatItem> boat,
            RegistryObject<Item, BoatItem> chestBoat
    ) implements Iterable<Item> {
        public @NotNull Iterator<Item> iterator() {
            return Arrays.stream(new Item[]{boat.get(), chestBoat.get()}).iterator();
        }
    }

    private static Holder<Potion> registerPotion(String name, Supplier<Potion> potion) {
        return POTIONS.register(name, potion).asHolder();
    }
}
