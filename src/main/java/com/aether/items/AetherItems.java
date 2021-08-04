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

import java.util.function.Consumer;

import static com.aether.Aether.locate;
import static com.aether.entities.AetherEntityTypes.*;
import static com.aether.items.AetherItemGroups.*;
import static com.aether.registry.RegistryQueue.onClient;
import static com.aether.util.item.AetherRarity.*;
import static net.minecraft.entity.EquipmentSlot.*;
import static net.minecraft.util.Rarity.*;

@SuppressWarnings("unused")
public class AetherItems {
    private static Consumer<Item> compostable(float chance) { return item -> ComposterBlock.registerCompostableItem(chance, item); }
    private static final Consumer<Item> compostable30 = compostable(0.3f);
    private static final Consumer<Item> compostable50 = compostable(0.5f);
    private static final Consumer<Item> compostable65 = compostable(0.65f);
    private static final Consumer<Item> swetColor = onClient(new StackableVariantColorizer(0xDADADA, 0xDADADA, 0xDADADA)::register);



    /*
    Begin items
     */

    private static final Settings resource = new Settings().group(AETHER_RESOURCES);
    public static final Item ZANITE_GEM = add("zanite_gemstone", new Item(resource));
    public static final Item ZANITE_FRAGMENT = add("zanite_fragment", new Item(resource));
    public static final Item GRAVITITE_GEM = add("gravitite_gemstone", new Item(resource));
    public static final Item AMBROSIUM_SHARD = add("ambrosium_shard", new AmbrosiumShardItem(resource));
    public static final Item GOLDEN_AMBER = add("golden_amber", new Item(resource));
    public static final Item AECHOR_PETAL = add("aechor_petal", new Item(resource), compostable65);
    public static final Item SWET_BALL = add("swet_ball", new Item(resource), swetColor);
    public static final Item GOLD_AERDUST = add("gold_aerdust", new Item(resource));
    public static final Item FROZEN_AERDUST = add("frozen_aerdust", new Item(resource));


    private static Settings tool() { return new Settings().group(AETHER_TOOLS); }
    private static final Settings tool = tool();
    private static final Settings rareTool = tool().rarity(RARE);
    private static final Settings aetherLootTool = tool().rarity(AETHER_LOOT);
    private static final Settings unstaclableTool = tool().maxCount(1);
    private static final Settings unstaclableRareTool = tool().maxCount(1).rarity(RARE);
    public static final Item ZANITE_SHOVEL = add("zanite_shovel", new ZaniteShovelItem(AetherToolMaterials.ZANITE, 1.5f, -3f, tool));
    public static final Item ZANITE_PICKAXE = add("zanite_pickaxe", new ZanitePickaxeItem(AetherToolMaterials.ZANITE, 1, -2.8f, tool));
    public static final Item ZANITE_AXE = add("zanite_axe", new ZaniteAxeItem(AetherToolMaterials.ZANITE, 6f, -3.1f, tool));
    public static final Item ZANITE_SWORD = add("zanite_sword", new ZaniteSwordItem(AetherToolMaterials.ZANITE, 3, -2.4f, tool));
    public static final Item ZANITE_HOE = add("zanite_hoe", new ZaniteHoeItem(AetherToolMaterials.ZANITE, 1, 3f, tool));

    public static final Item GRAVITITE_SHOVEL = add("gravitite_shovel", new GravititeShovelItem(AetherToolMaterials.GRAVITITE, 1.5f, -3f, rareTool));
    public static final Item GRAVITITE_PICKAXE = add("gravitite_pickaxe", new GravititePickaxeItem(AetherToolMaterials.GRAVITITE, 1, -2.8f, rareTool));
    public static final Item GRAVITITE_AXE = add("gravitite_axe", new GravititeAxeItem(AetherToolMaterials.GRAVITITE, 5f, -3f, rareTool));
    public static final Item GRAVITITE_SWORD = add("gravitite_sword", new SwordItem(AetherToolMaterials.GRAVITITE, 3, -2.4f, rareTool));
    public static final Item GRAVITITE_HOE = add("gravitite_hoe", new GravititeHoeItem(AetherToolMaterials.GRAVITITE, 1,4f, rareTool));

    public static final Item VALKYRIE_SHOVEL = add("valkyrie_shovel", new ShovelItem(AetherToolMaterials.VALKYRIE, 1.5f, -3f,  aetherLootTool));
    public static final Item VALKYRIE_PICKAXE = add("valkyrie_pickaxe", new AetherPickaxeItem(AetherToolMaterials.VALKYRIE, 1, -2.8f, aetherLootTool));
    public static final Item VALKYRIE_AXE = add("valkyrie_axe", new AetherAxeItem(AetherToolMaterials.VALKYRIE, 4f, -2.9f, aetherLootTool));
    public static final Item VALKYRIE_LANCE = add("valkyrie_lance", new ValkyrieLanceItem(AetherToolMaterials.VALKYRIE, 10, -3f, 6f, 4f, aetherLootTool));
    public static final Item VALKYRIE_HOE = add("valkyrie_hoe", new AetherHoeItem(AetherToolMaterials.VALKYRIE, 1, 5f, aetherLootTool));

    public static final Item GOLDEN_DART = add("golden_dart", new DartItem(tool));
    public static final Item ENCHANTED_DART = add("enchanted_dart", new DartItem(rareTool));
    public static final Item POISON_DART = add("poison_dart", new DartItem(tool));
    public static final Item GOLDEN_DART_SHOOTER = add("golden_dart_shooter", new DartShooterItem((DartItem) GOLDEN_DART, unstaclableTool));
    public static final Item ENCHANTED_DART_SHOOTER = add("enchanted_dart_shooter", new DartShooterItem((DartItem) ENCHANTED_DART, unstaclableRareTool));
    public static final Item POISON_DART_SHOOTER = add("poison_dart_shooter", new DartShooterItem((DartItem) POISON_DART, unstaclableTool));

    public static final Item PHOENIX_BOW = add("phoenix_bow", new BowItem(tool().maxDamage(384)));
    public static final Item FLAMING_SWORD = add("flaming_sword", new FlamingSwordItem(AetherToolMaterials.LEGENDARY, 4, -2.4f, aetherLootTool));
    public static final Item LIGHTNING_SWORD = add("lightning_sword", new LightningSwordItem(AetherToolMaterials.LEGENDARY, 4, -2.4f, aetherLootTool));
    public static final Item HOLY_SWORD = add("holy_sword", new HolySwordItem(AetherToolMaterials.LEGENDARY, 4, -2.4f, aetherLootTool));
    public static final Item VAMPIRE_BLADE = add("vampire_blade", new VampireBladeItem(AetherToolMaterials.LEGENDARY, 3, -2.4f, aetherLootTool));
    public static final Item PIG_SLAYER = add("pig_slayer", new PigSlayerItem(AetherToolMaterials.LEGENDARY, 3, -2.4f, aetherLootTool));
    public static final Item CANDY_CANE_SWORD = add("candy_cane_sword", new CandyCaneSwordItem(AetherToolMaterials.CANDY, 3, -2f, aetherLootTool));

    public static final Item CLOUD_PARACHUTE = add("cold_parachute", new ParachuteItem(unstaclableTool));
    public static final Item GOLDEN_CLOUD_PARACHUTE = add("golden_parachute", new ParachuteItem(tool().maxCount(1).maxDamage(20)));

    public static final Item AMBROSIUM_BLOODSTONE = add("ambrosium_bloodstone", new AmbrosiumBloodstoneItem(unstaclableTool));
    public static final Item ZANITE_BLOODSTONE = add("zanite_bloodstone", new ZaniteBloodstoneItem(unstaclableTool));
    public static final Item GRAVITITE_BLOODSTONE = add("gravitite_bloodstone", new GravititeBloodstoneItem(unstaclableTool));
    public static final Item ABSTENTINE_BLOODSTONE = add("abstentine_bloodstone", new AbstentineBloodstoneItem(unstaclableTool));


    private static Settings wearable() { return new Settings().group(AETHER_WEARABLES); }
    private static final Settings wearable = wearable();
    private static final Settings rareWearable = wearable().rarity(RARE);
    private static final Settings aetherLootWearable = wearable().rarity(AETHER_LOOT);
    public static final Item ZANITE_HELMET = add("zanite_helmet", new ArmorItem(AetherArmorMaterials.ZANITE, HEAD, wearable));
    public static final Item ZANITE_CHESTPLATE = add("zanite_chestplate", new ArmorItem(AetherArmorMaterials.ZANITE, CHEST, wearable));
    public static final Item ZANITE_LEGGINGS = add("zanite_leggings", new ArmorItem(AetherArmorMaterials.ZANITE, LEGS, wearable));
    public static final Item ZANITE_BOOTS = add("zanite_boots", new ArmorItem(AetherArmorMaterials.ZANITE, FEET, wearable));

    public static final Item GRAVITITE_HELMET = add("gravitite_helmet", new ArmorItem(AetherArmorMaterials.GRAVITITE, HEAD, rareWearable));
    public static final Item GRAVITITE_CHESTPLATE = add("gravitite_chestplate", new ArmorItem(AetherArmorMaterials.GRAVITITE, CHEST, rareWearable));
    public static final Item GRAVITITE_LEGGINGS = add("gravitite_leggings", new ArmorItem(AetherArmorMaterials.GRAVITITE, LEGS, rareWearable));
    public static final Item GRAVITITE_BOOTS = add("gravitite_boots", new ArmorItem(AetherArmorMaterials.GRAVITITE, FEET, rareWearable));

    public static final Item NEPTUNE_HELMET = add("neptune_helmet", new ArmorItem(AetherArmorMaterials.NEPTUNE, HEAD, aetherLootWearable));
    public static final Item NEPTUNE_CHESTPLATE = add("neptune_chestplate", new ArmorItem(AetherArmorMaterials.NEPTUNE, CHEST, aetherLootWearable));
    public static final Item NEPTUNE_LEGGINGS = add("neptune_leggings", new ArmorItem(AetherArmorMaterials.NEPTUNE, LEGS, aetherLootWearable));
    public static final Item NEPTUNE_BOOTS = add("neptune_boots", new ArmorItem(AetherArmorMaterials.NEPTUNE, FEET, aetherLootWearable));

    public static final Item PHOENIX_HELMET = add("phoenix_helmet", new ArmorItem(AetherArmorMaterials.PHOENIX, HEAD, aetherLootWearable));
    public static final Item PHOENIX_CHESTPLATE = add("phoenix_chestplate", new ArmorItem(AetherArmorMaterials.PHOENIX, CHEST, aetherLootWearable));
    public static final Item PHOENIX_LEGGINGS = add("phoenix_leggings", new ArmorItem(AetherArmorMaterials.PHOENIX, LEGS, aetherLootWearable));
    public static final Item PHOENIX_BOOTS = add("phoenix_boots", new ArmorItem(AetherArmorMaterials.PHOENIX, FEET, aetherLootWearable));

    public static final Item OBSIDIAN_HELMET = add("obsidian_helmet", new ArmorItem(AetherArmorMaterials.OBSIDIAN, HEAD, aetherLootWearable));
    public static final Item OBSIDIAN_CHESTPLATE = add("obsidian_chestplate", new ArmorItem(AetherArmorMaterials.OBSIDIAN, CHEST, aetherLootWearable));
    public static final Item OBSIDIAN_LEGGINGS = add("obsidian_leggings", new ArmorItem(AetherArmorMaterials.OBSIDIAN, LEGS, aetherLootWearable));
    public static final Item OBSIDIAN_BOOTS = add("obsidian_boots", new ArmorItem(AetherArmorMaterials.OBSIDIAN, FEET, aetherLootWearable));

    public static final Item VALKYRIE_HELMET = add("valkyrie_helmet", new ArmorItem(AetherArmorMaterials.VALKYRIE, HEAD, aetherLootWearable));
    public static final Item VALKYRIE_CHESTPLATE = add("valkyrie_chestplate", new ArmorItem(AetherArmorMaterials.VALKYRIE, CHEST, aetherLootWearable));
    public static final Item VALKYRIE_LEGGINGS = add("valkyrie_leggings", new ArmorItem(AetherArmorMaterials.VALKYRIE, LEGS, aetherLootWearable));
    public static final Item VALKYRIE_BOOTS = add("valkyrie_boots", new ArmorItem(AetherArmorMaterials.VALKYRIE, FEET, aetherLootWearable));

    public static final Item SENTRY_BOOTS = add("sentry_boots", new ArmorItem(AetherArmorMaterials.SENTRY, FEET, aetherLootWearable));

    public static final Item LEATHER_GLOVES = add("leather_gloves", new AccessoryItem(AccessoryType.GLOVES, 1.5f, wearable));
    public static final Item IRON_GLOVES = add("iron_gloves", new AccessoryItem(AccessoryType.GLOVES, 2.5f, wearable));
    public static final Item GOLDEN_GLOVES = add("golden_gloves", new AccessoryItem(AccessoryType.GLOVES, 2f, wearable));
    public static final Item CHAIN_GLOVES = add("chain_gloves", new AccessoryItem(AccessoryType.GLOVES, "chain", 2f, wearable));
    public static final Item DIAMOND_GLOVES = add("diamond_gloves", new AccessoryItem(AccessoryType.GLOVES, 4.5f, wearable));
    public static final Item ZANITE_GLOVES = add("zanite_gloves", new AccessoryItem(AccessoryType.GLOVES, 3.0f, wearable));
    public static final Item GRAVITITE_GLOVES = add("gravitite_gloves", new AccessoryItem(AccessoryType.GLOVES, 4f, rareWearable));
    public static final Item NEPTUNE_GLOVES = add("neptune_gloves", new AccessoryItem(AccessoryType.GLOVES, 4.5f, aetherLootWearable));
    public static final Item PHOENIX_GLOVES = add("phoenix_gloves", new AccessoryItem(AccessoryType.GLOVES, "phoenix", 4f, aetherLootWearable));
    public static final Item OBSIDIAN_GLOVES = add("obsidian_gloves", new AccessoryItem(AccessoryType.GLOVES, 5f, aetherLootWearable));
    public static final Item VALKYRIE_GLOVES = add("valkyrie_gloves", new AccessoryItem(AccessoryType.GLOVES, "valkyrie", 5f, aetherLootWearable));

    public static final Item IRON_RING = add("iron_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final Item GOLDEN_RING = add("golden_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final Item ZANITE_RING = add("zanite_ring", new AccessoryItem(AccessoryType.RING, wearable));
    public static final Item ICE_RING = add("ice_ring", new AccessoryItem(AccessoryType.RING, rareWearable));

    public static final Item GOLDEN_PENDANT = add("golden_pendant", new AccessoryItem(AccessoryType.PENDANT, wearable));
    public static final Item ZANITE_PENDANT = add("zanite_pendant", new AccessoryItem(AccessoryType.PENDANT, wearable));
    public static final Item ICE_PENDANT = add("ice_pendant", new AccessoryItem(AccessoryType.PENDANT, rareWearable));

    public static final Item WHITE_CAPE = add("white_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final Item RED_CAPE = add("red_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final Item BLUE_CAPE = add("blue_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final Item YELLOW_CAPE = add("yellow_cape", new AccessoryItem(AccessoryType.CAPE, wearable));
    public static final Item SWET_CAPE = add("swet_cape", new AccessoryItem(AccessoryType.CAPE, "swet", aetherLootWearable));
    public static final Item AGILITY_CAPE = add("agility_cape", new AccessoryItem(AccessoryType.CAPE, "agility", aetherLootWearable));
    public static final Item INVISIBILITY_CAPE = add("invisibility_cape", new AccessoryItem(AccessoryType.CAPE, aetherLootWearable));

    public static final Item GOLDEN_FEATHER = add("golden_feather", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    public static final Item REGENERATION_STONE = add("regeneration_stone", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));
    public static final Item IRON_BUBBLE = add("iron_bubble", new AccessoryItem(AccessoryType.MISC, aetherLootWearable));


    private static Settings food(FoodComponent foodComponent) { return new Settings().group(AETHER_FOOD).food(foodComponent); }
    private static Settings food(FoodComponent foodComponent, Rarity rarity) { return food(foodComponent).rarity(rarity); }
    public static final Item BLUEBERRY = add("blue_berry", new AliasedBlockItem(AetherBlocks.BLUEBERRY_BUSH, food(AetherFoodComponent.BLUEBERRY)), compostable30);
    public static final Item ENCHANTED_BLUEBERRY = add("enchanted_blueberry", new Item(food(AetherFoodComponent.ENCHANTED_BLUEBERRY, RARE)), compostable50);
    public static final Item ORANGE = add("orange", new Item(food(AetherFoodComponent.ORANGE)), compostable65);
    public static final Item WHITE_APPLE = add("white_apple", new WhiteApple(food(AetherFoodComponent.WHITE_APPLE)), compostable(0f));
    public static final Item BLUE_GUMMY_SWET = add("blue_gummy_swet", new Item(food(AetherFoodComponent.GUMMY_SWET, AETHER_LOOT)));
    public static final Item GOLDEN_GUMMY_SWET = add("golden_gummy_swet", new Item(food(AetherFoodComponent.GUMMY_SWET, AETHER_LOOT)));
    public static final Item VALKYRIE_MILK = add("valkyrie_milk", new ValkyrieMilkItem(food(AetherFoodComponent.VALKYRIE_MILK, EPIC).maxCount(1)));
    public static final Item HEALING_STONE = add("healing_stone", new HealingStone(food(AetherFoodComponent.HEALING_STONE, RARE)));
    public static final Item CANDY_CANE = add("candy_cane", new Item(food(AetherFoodComponent.GENERIC)), compostable30);
    public static final Item GINGERBREAD_MAN = add("ginger_bread_man", new Item(food(AetherFoodComponent.GENERIC)), compostable30);
    public static final Item MOA_MEAT = add("moa_meat", new Item(food(AetherFoodComponent.MOA_MEAT)));
    public static final Item COOKED_MOA_MEAT = add("moa_meat_cooked", new Item(food(AetherFoodComponent.COOKED_MOA_MEAT)));


    private static Settings misc() { return new Settings().group(AETHER_MISC); }
    private static final Settings misc = misc();
    private static final Settings unstackableMisc = misc().maxCount(1);
    public static final Item LIFE_SHARD = add("life_shard", new LifeShard(misc().rarity(AETHER_LOOT).maxCount(1)));
    public static final Item CLOUD_STAFF = add("cloud_staff", new CloudStaffItem(misc().maxCount(1).maxDamage(60)));
    public static final Item NATURE_STAFF = add("nature_staff", new NatureStaffItem(misc().maxCount(1).maxDamage(100)));
    public static final Item MOA_EGG = add("moa_egg", new MoaEggItem(unstackableMisc));
    public static final Item LORE_BOOK = add("lore_book", new BookOfLoreItem(unstackableMisc));

    public static final Item SKYROOT_BOAT = add("skyroot_boat", new BoatItem(AetherBoatTypes.SKYROOT, unstackableMisc));
    public static final Item GOLDEN_OAK_BOAT = add("golden_oak_boat", new BoatItem(AetherBoatTypes.GOLDEN_OAK, unstackableMisc));
    public static final Item ORANGE_BOAT = add("orange_boat", new BoatItem(AetherBoatTypes.ORANGE, unstackableMisc));
    public static final Item CRYSTAL_BOAT = add("crystal_boat", new BoatItem(AetherBoatTypes.CRYSTAL, unstackableMisc));
    public static final Item WISTERIA_BOAT = add("wisteria_boat", new BoatItem(AetherBoatTypes.WISTERIA, unstackableMisc));

    public static final Item SKYROOT_BUCKET = add("skyroot_bucket", new SkyrootBucketItem(misc().maxCount(16)));

    private static final Settings skyrootBucket = misc().maxCount(1).recipeRemainder(SKYROOT_BUCKET);
    public static final Item SKYROOT_WATER_BUCKET = add("skyroot_water_bucket", new SkyrootBucketItem(Fluids.WATER, skyrootBucket));
    public static final Item SKYROOT_MILK_BUCKET = add("skyroot_milk_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final Item SKYROOT_POISON_BUCKET = add("skyroot_poison_bucket", new SkyrootBucketItem(skyrootBucket));
    public static final Item SKYROOT_REMEDY_BUCKET = add("skyroot_remedy_bucket", new SkyrootBucketItem(skyrootBucket));

    public static final Item QUICKSOIL_VIAL = add("quicksoil_vial", new VialItem(Fluids.EMPTY, misc().maxCount(32)));
    public static final Item AERCLOUD_VIAL = add("aercloud_vial", new VialItem(AetherFluids.DENSE_AERCLOUD, misc().maxCount(32)));

    public static final Item BRONZE_KEY = add("bronze_key", new Item(misc().rarity(AETHER_LOOT)));
    public static final Item SILVER_KEY = add("silver_key", new Item(misc().rarity(AETHER_LOOT)));
    public static final Item GOLDEN_KEY = add("golden_key", new Item(misc().rarity(AETHER_LOOT)));

    public static final Item AETHER_PORTAL = add("aether_portal", new AetherPortalItem(misc));

    public static final Item AECHOR_PLANT_SPAWN_EGG = add("aechor_plant_spawn_egg", new SpawnEggItem(AECHOR_PLANT, 0x97DED4, 0x31897D, misc));
    public static final Item CHEST_MIMIC_SPAWN_EGG = null;
    public static final Item COCKATRICE_SPAWN_EGG = add("cockatrice_spawn_egg", new SpawnEggItem(COCKATRICE, 0x9FC3F7, 0x3D2338, misc));
    public static final Item AERBUNNY_SPAWN_EGG = add("aerbunny_spawn_egg", new SpawnEggItem(AERBUNNY, 0xC5D6ED, 0x82A6D9, misc));
    public static final Item AERWHALE_SPAWN_EGG = add("aerwhale_spawn_egg", new SpawnEggItem(AERWHALE, 0x5C6D91, 0xDEDBCE, misc));
    public static final Item FLYING_COW_SPAWN_EGG = null;
    public static final Item MOA_SPAWN_EGG = add("moa_spawn_egg", new SpawnEggItem(MOA, 0xC55C2E4, 0xB3A8BB, misc));
    public static final Item SWET_SPAWN_EGG = add("swet_spawn_egg", new SpawnEggItem(WHITE_SWET, 0x8F9294, 0xE6EAEB, misc));
    public static final Item BLUE_SWET_SPAWN_EGG = add("blue_swet_spawn_egg", new SpawnEggItem(BLUE_SWET, 0x46699E, 0xE6EAEB, misc));
    public static final Item PURPLE_SWET_SPAWN_EGG = add("purple_swet_spawn_egg", new SpawnEggItem(PURPLE_SWET, 0x5D548C, 0xE6EAEB, misc));
    public static final Item GOLDEN_SWET_SPAWN_EGG = add("golden_swet_spawn_egg", new SpawnEggItem(GOLDEN_SWET, 0xC99D36, 0xE6EAEB, misc));
    public static final Item PHYG_SPAWN_EGG = null;
    public static final Item SHEEPUFF_SPAWN_EGG = null;



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
        RegistryQueue.ITEM.register();
    }

    @SafeVarargs
    public static Item add(String id, Item item, Consumer<Item>... additionalActions) {
        return RegistryQueue.ITEM.add(locate(id), item, additionalActions);
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
