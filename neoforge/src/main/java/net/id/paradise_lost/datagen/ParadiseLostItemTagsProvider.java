package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostItemTagsProvider extends ItemTagsProvider {
    private static final String[] WOODS = {"aurel", "mother_aurel", "menth", "wisteria"};

    public ParadiseLostItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                        CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        entry(Tags.Items.BOOKSHELVES, "aurel_bookshelf");
        entry(Tags.Items.BUCKETS_EMPTY, "aurel_bucket");
        entry(Tags.Items.BUCKETS_MILK, "aurel_milk_bucket");
        entry(Tags.Items.BUCKETS_WATER, "aurel_water_bucket");
        entry(Tags.Items.CHAINS, "olvite_chain");
        entry(Tags.Items.COBBLESTONES, "cobbled_floestone");
        entry(Tags.Items.FOODS, "amadrys_noodles", "popom_jelly");
        entry(Tags.Items.FOODS_BREAD, "amadrys_bread");
        entry(Tags.Items.FOODS_COOKED_MEAT, "moa_meat_cooked");
        entry(Tags.Items.FOODS_COOKIE, "blackcurrant_cookie", "amadrys_bread_glazed", "amadrys_bread_glazed_filled");
        entry(Tags.Items.FOODS_FRUIT, "blackcurrant");
        entry(Tags.Items.FOODS_PIE, "blackcurrant_pie");
        entry(Tags.Items.FOODS_RAW_MEAT, "moa_meat");
        entry(Tags.Items.FOODS_SOUP, "root_stew");
        entry(Tags.Items.FOODS_VEGETABLE, "nitra_bulb");
        entry(Tags.Items.ORES, "cherine_ore", "olvite_ore", "levita_ore");
        entry(Tags.Items.STONES, "floestone");
        entry(Tags.Items.STORAGE_BLOCKS, "flaxweave_cushion", "amadrys_bundle", "cherine_block", "olvite_block", "refined_surtrum_block", "nitra_bunch");
        woods(Tags.Items.STRIPPED_LOGS, "stripped_%s_log");
        woods(Tags.Items.STRIPPED_WOODS, "stripped_%s_wood");
        entry(ItemTags.ARROWS, "levita_arrow");
        entry(ItemTags.AXES, "surtrum_axe", "glazed_gold_axe", "olvite_axe");
        woods(ItemTags.BOATS, "%s_boat");
        entry(ItemTags.CHEST_ARMOR, "glazed_gold_chestplate", "olvite_chestplate", "surtrum_chestplate");
        woods(ItemTags.CHEST_BOATS, "%s_chest_boat");
        entry(ItemTags.DECORATED_POT_SHERDS, ParadiseLostItemTags.CALCITE_POT_SHERDS);
        entry(ItemTags.DURABILITY_ENCHANTABLE, "xp_circlet");
        entry(ItemTags.FOOT_ARMOR, "glazed_gold_boots", "olvite_boots", "surtrum_boots", "floaty_boots");
        entry(mc("freeze_immune_wearables"), "surtrum_boots", "surtrum_leggings", "surtrum_chestplate", "surtrum_helmet", "floaty_boots");
        entry(ItemTags.DAMPENS_VIBRATIONS, "floaty_boots");
        woods(ItemTags.HANGING_SIGNS, "%s_hanging_sign");
        entry(ItemTags.HEAD_ARMOR, "glazed_gold_helmet", "olvite_helmet", "ornate_olvite_helmet", "surtrum_helmet");
        entry(ItemTags.HOES, "surtrum_hoe", "glazed_gold_hoe", "olvite_hoe");
        entry(mc("horse_food"), "amadrys_bushel");
        entry(ItemTags.LEAVES, "aurel_leaves", "mother_aurel_leaves", "rose_wisteria_leaves", "frost_wisteria_leaves", "lavender_wisteria_leaves", "menth_leaves");
        entry(ItemTags.LEG_ARMOR, "glazed_gold_leggings", "olvite_leggings", "surtrum_leggings", "floaty_leggings");
        entry(mc("llama_food"), "amadrys_bushel", "amadrys_bundle");
        entry(mc("llama_tempt_items"), "amadrys_bundle");
        entry(ItemTags.LOGS,
                ParadiseLostItemTags.AUREL_LOGS, ParadiseLostItemTags.MOTHER_AUREL_LOGS,
                ParadiseLostItemTags.WISTERIA_LOGS, ParadiseLostItemTags.MENTH_LOGS, ParadiseLostItemTags.HOLLOW_LOGS);
        entry(ItemTags.LOGS_THAT_BURN,
                ParadiseLostItemTags.AUREL_LOGS, ParadiseLostItemTags.MOTHER_AUREL_LOGS,
                ParadiseLostItemTags.WISTERIA_LOGS, ParadiseLostItemTags.MENTH_LOGS, ParadiseLostItemTags.HOLLOW_LOGS);
        entry(mc("meat"), "moa_meat", "moa_meat_cooked");
        entry(ItemTags.PICKAXES, "surtrum_pickaxe", "glazed_gold_pickaxe", "olvite_pickaxe");
        entry(ItemTags.PIGLIN_LOVED, "glazed_gold_helmet", "glazed_gold_chestplate", "glazed_gold_leggings", "glazed_gold_boots",
                "glazed_gold_sword", "glazed_gold_pickaxe", "glazed_gold_shovel", "glazed_gold_axe", "glazed_gold_hoe");
        woods(ItemTags.PLANKS, "%s_planks");
        entry(ItemTags.SAPLINGS, "aurel_sapling", "mother_aurel_sapling", "rose_wisteria_sapling", "frost_wisteria_sapling", "lavender_wisteria_sapling", "menth_sapling");
        entry(mc("sheep_food"), "amadrys_bushel");
        entry(ItemTags.SHOVELS, "surtrum_shovel", "glazed_gold_shovel", "olvite_shovel");
        woods(ItemTags.SIGNS, "%s_sign");
        entry(mc("slabs"), "levita_brick_slab", "floestone_brick_slab", "mossy_floestone_brick_slab", "floestone_slab", "mossy_floestone_slab", "cobbled_floestone_slab");
        entry(mc("small_flowers"), "ancient_flower", "ataraxia", "cloudsbluff", "drigean", "luminar");
        entry(mc("stairs"), "levita_brick_stairs", "floestone_brick_stairs", "mossy_floestone_brick_stairs", "floestone_stairs", "mossy_floestone_stairs", "cobbled_floestone_stairs");
        entry(mc("stone_buttons"), "floestone_button");
        entry(ItemTags.STONE_CRAFTING_MATERIALS, "cobbled_floestone");
        entry(ItemTags.STONE_TOOL_MATERIALS, "cobbled_floestone");
        entry(ItemTags.SWORDS, "surtrum_sword", "glazed_gold_sword", "olvite_sword", "soul_blade");
        entry(mc("tall_flowers"), "wild_flax");
        entry(mc("walls"), "floestone_brick_wall", "mossy_floestone_brick_wall", "floestone_wall", "mossy_floestone_wall", "cobbled_floestone_wall");
        woods(ItemTags.WOODEN_BUTTONS, "%s_button");
        woods(ItemTags.WOODEN_DOORS, "%s_door");
        woods(ItemTags.WOODEN_FENCES, "%s_fence");
        woods(ItemTags.WOODEN_PRESSURE_PLATES, "%s_pressure_plate");
        woods(ItemTags.WOODEN_SLABS, "%s_slab");
        woods(ItemTags.WOODEN_STAIRS, "%s_stairs");
        woods(ItemTags.WOODEN_TRAPDOORS, "%s_trapdoor");

        entry(ParadiseLostItemTags.AUREL_LOGS,
                "aurel_log", "mottled_aurel_log", "mottled_aurel_wood", "aurel_wood", "stripped_aurel_log", "stripped_aurel_wood");
        entry(ParadiseLostItemTags.CALCITE_DECORATED_POT_INGREDIENTS, "minecraft:calcite", ItemTags.DECORATED_POT_SHERDS);
        entry(ParadiseLostItemTags.CALCITE_POT_SHERDS, "sol_pottery_sherd", "coo_pottery_sherd");
        entry(ParadiseLostItemTags.CLOUDS, "cold_cloud", "blue_cloud", "golden_cloud", "green_cloud");
        entry(ParadiseLostItemTags.HANGERS, "frost_wisteria_hanger", "rose_wisteria_hanger", "lavender_wisteria_hanger");
        entry(ParadiseLostItemTags.HOLLOW_LOGS, "mottled_aurel_fallen_log");
        entry(ParadiseLostItemTags.IGNITING_TOOLS, "surtrum_shovel", "surtrum_pickaxe", "surtrum_axe", "surtrum_sword", "surtrum_hoe");
        entry(ParadiseLostItemTags.IRON_INTERCHANGABLE, "minecraft:iron_ingot", "olvite");
        entry(ParadiseLostItemTags.MENTH_LOGS, "menth_log", "menth_wood", "stripped_menth_log", "stripped_menth_wood");
        entry(ParadiseLostItemTags.MOA_BREEDABLES, "popom_jelly", "swedroot", "minecraft:sugar");
        entry(ParadiseLostItemTags.MOA_TEMPTABLES, TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/raw_meat")));
        entry(ParadiseLostItemTags.MOTHER_AUREL_LOGS, "mother_aurel_log", "mother_aurel_wood", "stripped_mother_aurel_log", "stripped_mother_aurel_wood");
        entry(ParadiseLostItemTags.MUSHROOMS, "rootcap", "brown_sporecap", "pink_sporecap");
        entry(ParadiseLostItemTags.PARADISE_PLANKS, "aurel_planks", "mother_aurel_planks", "menth_planks", "wisteria_planks");
        entry(ParadiseLostItemTags.RIGHTEOUS_WEAPONS, "minecraft:golden_sword", "minecraft:netherite_sword", "glazed_gold_sword");
        this.tag(ParadiseLostItemTags.SACRED_WEAPONS);
        tag(ParadiseLostItemTags.RENDING_ENCHANTABLE)
                .add(ItemRegistry.SOUL_BLADE.get());
        entry(ParadiseLostItemTags.WISTERIA_LOGS, "wisteria_log", "wisteria_wood", "stripped_wisteria_log", "stripped_wisteria_wood");
    }

    private void woods(TagKey<Item> tag, String pattern) {
        Object[] ids = new Object[WOODS.length];
        for (int i = 0; i < WOODS.length; i++) {
            ids[i] = pattern.formatted(WOODS[i]);
        }
        entry(tag, ids);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void entry(TagKey<Item> tag, Object... parts) {
        TagAppender appender = this.tag(tag);
        for (Object part : parts) {
            if (part instanceof TagKey<?> ref) {
                appender.addTag((TagKey) ref);
            } else {
                String id = (String) part;
                appender.addOptional(id.indexOf(':') >= 0 ? ResourceLocation.parse(id) : ModConstants.id(id));
            }
        }
    }

    private static TagKey<Item> mc(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(path));
    }
}
