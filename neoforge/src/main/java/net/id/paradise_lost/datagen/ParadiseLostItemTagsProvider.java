package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.id.paradise_lost.registry.BlockRegistry.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public class ParadiseLostItemTagsProvider extends ItemTagsProvider {
    public ParadiseLostItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                        CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);
        copy(Tags.Blocks.CHAINS, Tags.Items.CHAINS);
        copy(Tags.Blocks.COBBLESTONES, Tags.Items.COBBLESTONES);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(Tags.Blocks.STONES, Tags.Items.STONES);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        copy(Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS);
        copy(Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS);

        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(BlockTags.LOGS, ItemTags.LOGS);
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(BlockTags.STONE_BUTTONS, ItemTags.STONE_BUTTONS);
        copy(BlockTags.TALL_FLOWERS, ItemTags.TALL_FLOWERS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);

        copy(ParadiseLostBlockTags.AUREL_LOGS, ParadiseLostItemTags.AUREL_LOGS);
        copy(ParadiseLostBlockTags.CLOUDS, ParadiseLostItemTags.CLOUDS);
        copy(ParadiseLostBlockTags.HANGERS, ParadiseLostItemTags.HANGERS);
        copy(ParadiseLostBlockTags.HOLLOW_LOGS, ParadiseLostItemTags.HOLLOW_LOGS);
        copy(ParadiseLostBlockTags.MENTH_LOGS, ParadiseLostItemTags.MENTH_LOGS);
        copy(ParadiseLostBlockTags.MOTHER_AUREL_LOGS, ParadiseLostItemTags.MOTHER_AUREL_LOGS);
        copy(ParadiseLostBlockTags.WISTERIA_LOGS, ParadiseLostItemTags.WISTERIA_LOGS);

        tag(Tags.Items.BUCKETS_EMPTY).add(AUREL_BUCKET.get());
        tag(Tags.Items.BUCKETS_MILK).add(AUREL_MILK_BUCKET.get());
        tag(Tags.Items.BUCKETS_WATER).add(AUREL_WATER_BUCKET.get());
        tag(Tags.Items.FOODS).add(AMADRYS_NOODLES.get(), POPOM_JELLY.get());
        tag(Tags.Items.FOODS_BREAD).add(AMADRYS_BREAD.get());
        tag(Tags.Items.FOODS_COOKED_MEAT).add(COOKED_MOA_MEAT.get());
        tag(Tags.Items.FOODS_COOKIE).add(
                BLACKCURRANT_COOKIE.get(),
                AMADRYS_BREAD_GLAZED.get(), AMADRYS_BREAD_GLAZED_FILLED.get()
        );
        tag(Tags.Items.FOODS_FRUIT).add(BLACKCURRANT.get());
        tag(Tags.Items.FOODS_PIE).add(BLACKCURRANT_PIE.get());
        tag(Tags.Items.FOODS_RAW_MEAT).add(MOA_MEAT.get());
        tag(Tags.Items.FOODS_SOUP).add(ROOT_STEW.get());
        tag(Tags.Items.FOODS_VEGETABLE).add(NITRA_BULB.get());

        tag(ItemTags.ARROWS).add(LEVITA_ARROW.get());
        tag(ItemTags.AXES).add(SURTRUM_AXE.get(), GLAZED_GOLD_AXE.get(), OLVITE_AXE.get());
        tag(ItemTags.CHEST_ARMOR).add(GLAZED_GOLD_CHESTPLATE.get(), OLVITE_CHESTPLATE.get(), SURTRUM_CHESTPLATE.get());
        tag(ItemTags.DECORATED_POT_SHERDS).addTag(ParadiseLostItemTags.CALCITE_POT_SHERDS);
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(XP_CIRCLET.get());
        tag(ItemTags.FOOT_ARMOR).add(GLAZED_GOLD_BOOTS.get(), OLVITE_BOOTS.get(), SURTRUM_BOOTS.get(), FLOATY_BOOTS.get());
        tag(ItemTags.DAMPENS_VIBRATIONS).add(FLOATY_BOOTS.get());
        tag(ItemTags.HEAD_ARMOR).add(GLAZED_GOLD_HELMET.get(), OLVITE_HELMET.get(), OLVITE_HELMET_ORNATE.get(), SURTRUM_HELMET.get());
        tag(ItemTags.HOES).add(SURTRUM_HOE.get(), GLAZED_GOLD_HOE.get(), OLVITE_HOE.get());
        tag(ItemTags.LEG_ARMOR).add(GLAZED_GOLD_LEGGINGS.get(), OLVITE_LEGGINGS.get(), SURTRUM_LEGGINGS.get(), FLOATY_LEGGINGS.get());
        tag(ItemTags.PICKAXES).add(SURTRUM_PICKAXE.get(), GLAZED_GOLD_PICKAXE.get(), OLVITE_PICKAXE.get());
        tag(ItemTags.PIGLIN_LOVED).add(
                GLAZED_GOLD_HELMET.get(), GLAZED_GOLD_CHESTPLATE.get(), GLAZED_GOLD_LEGGINGS.get(), GLAZED_GOLD_BOOTS.get(),
                GLAZED_GOLD_SWORD.get(), GLAZED_GOLD_PICKAXE.get(), GLAZED_GOLD_SHOVEL.get(), GLAZED_GOLD_AXE.get(), GLAZED_GOLD_HOE.get()
        );
        tag(ItemTags.SHOVELS).add(SURTRUM_SHOVEL.get(), GLAZED_GOLD_SHOVEL.get(), OLVITE_SHOVEL.get());
        tag(ItemTags.STONE_CRAFTING_MATERIALS).add(COBBLED_FLOESTONE.get().asItem());
        tag(ItemTags.STONE_TOOL_MATERIALS).add(COBBLED_FLOESTONE.get().asItem());
        tag(ItemTags.SWORDS).add(SURTRUM_SWORD.get(), GLAZED_GOLD_SWORD.get(), OLVITE_SWORD.get(), SOUL_BLADE.get());
        tag(ItemTags.TRIMMABLE_ARMOR).remove(FLOATY_LEGGINGS.get(), FLOATY_BOOTS.get());

        tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
                SURTRUM_BOOTS.get(), SURTRUM_LEGGINGS.get(), SURTRUM_CHESTPLATE.get(), SURTRUM_HELMET.get(), FLOATY_BOOTS.get()
        );
        tag(ItemTags.HORSE_FOOD).add(AMADRYS_BUSHEL.get());
        tag(ItemTags.LLAMA_FOOD).add(AMADRYS_BUSHEL.get(), AMADRYS_BUNDLE.get().asItem());
        tag(ItemTags.LLAMA_TEMPT_ITEMS).add(AMADRYS_BUNDLE.get().asItem());
        tag(ItemTags.MEAT).add(MOA_MEAT.get(), COOKED_MOA_MEAT.get());
        tag(ItemTags.SHEEP_FOOD).add(AMADRYS_BUSHEL.get());

        addBoatTags();
        addSignTags();

        tag(ParadiseLostItemTags.CALCITE_DECORATED_POT_INGREDIENTS)
                .add(Items.CALCITE)
                .addTag(ItemTags.DECORATED_POT_SHERDS);
        tag(ParadiseLostItemTags.CALCITE_POT_SHERDS).add(SOL_POTTERY_SHERD.get(), COO_POTTERY_SHERD.get());
        tag(ParadiseLostItemTags.IGNITING_TOOLS).add(
                SURTRUM_SHOVEL.get(), SURTRUM_PICKAXE.get(), SURTRUM_AXE.get(), SURTRUM_SWORD.get(), SURTRUM_HOE.get()
        );
        tag(ParadiseLostItemTags.IRON_INTERCHANGABLE).add(Items.IRON_INGOT, OLVITE.get());
        tag(ParadiseLostItemTags.MOA_BREEDABLES).add(POPOM_JELLY.get(), ItemRegistry.SWEDROOT.get(), Items.SUGAR);
        tag(ParadiseLostItemTags.MOA_TEMPTABLES).addTag(Tags.Items.FOODS_RAW_MEAT);
        tag(ParadiseLostItemTags.MUSHROOMS).add(ROOTCAP.get().asItem(), BROWN_SPORECAP.get().asItem(), PINK_SPORECAP.get().asItem());
        tag(ParadiseLostItemTags.PARADISE_PLANKS).add(
                AUREL_WOODSTUFF.plank().get().asItem(),
                MOTHER_AUREL_WOODSTUFF.plank().get().asItem(),
                MENTH_WOODSTUFF.plank().get().asItem(),
                WISTERIA_WOODSTUFF.plank().get().asItem()
        );
        tag(ParadiseLostItemTags.RIGHTEOUS_WEAPONS).add(Items.GOLDEN_SWORD, Items.NETHERITE_SWORD, GLAZED_GOLD_SWORD.get());
        tag(ParadiseLostItemTags.SACRED_WEAPONS);
        tag(ParadiseLostItemTags.RENDING_ENCHANTABLE).add(ItemRegistry.SOUL_BLADE.get());
    }

    private void addBoatTags() {
        for (ItemRegistry.BoatSet boats : ItemRegistry.BOAT_SETS) {
            tag(ItemTags.BOATS).add(boats.boat().get());
            tag(ItemTags.CHEST_BOATS).add(boats.chestBoat().get());
        }
    }

    private void addSignTags() {
        tag(ItemTags.SIGNS).add(AUREL_SIGN.get(), MOTHER_AUREL_SIGN.get(), MENTH_SIGN.get(), WISTERIA_SIGN.get());
        tag(ItemTags.HANGING_SIGNS).add(
                AUREL_HANGING_SIGN.get(), MOTHER_AUREL_HANGING_SIGN.get(), MENTH_HANGING_SIGN.get(), WISTERIA_HANGING_SIGN.get()
        );
    }
}
