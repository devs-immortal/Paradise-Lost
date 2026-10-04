package net.id.paradise_lost.datagen;

import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.loot.ParadiseLostLootTables;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.ParadiseLostCriteria;
import net.id.paradise_lost.world.dimension.ParadiseLostDimension;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.LootTableTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.ChatFormatting;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.BlockPredicate;

public class ParadiseLostAdvancementProvider extends AdvancementProvider {

    public ParadiseLostAdvancementProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    private static final class Generator implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(
                            BlockRegistry.CHISELED_FLOESTONE.get(),
                            Component.translatable("advancements.paradise_lost.root.title").withStyle(ChatFormatting.AQUA),
                            Component.translatable("advancements.paradise_lost.root.description"),
                            ModConstants.id("textures/block/floestone.png"),
                            AdvancementType.TASK,
                            false,
                            false,
                            false
                    )
                    .addCriterion("entered_paradise", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY))
                    .requirements(AdvancementRequirements.allOf(List.of("entered_paradise")))
                    .save(saver, ModConstants.id("root"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ItemRegistry.SOUL_BLADE.get(),
                            Component.translatable("advancements.paradise_lost.bloomed_blade_goal.title"),
                            Component.translatable("advancements.paradise_lost.bloomed_blade_goal.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            true
                    )
                    .addCriterion("goal", emptyLocationCriterion(ParadiseLostCriteria.BLOOMED_BLADE_GOAL))
                    .requirements(AdvancementRequirements.allOf(List.of("goal")))
                    .sendsTelemetryEvent()
                    .save(saver, ModConstants.id("bloomed_blade_goal"), existingFileHelper);

            AdvancementHolder brewPotion = AdvancementSubProvider.createPlaceholder("minecraft:nether/brew_potion");
            AdvancementHolder bloomedCalciteCraft = Advancement.Builder.advancement()
                    .parent(brewPotion)
                    .display(
                            BlockRegistry.BLOOMED_CALCITE.get(),
                            Component.translatable("advancements.paradise_lost.bloomed_calcite_craft.title"),
                            Component.translatable("advancements.paradise_lost.bloomed_calcite_craft.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("douse", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                            LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registries.lookupOrThrow(Registries.BLOCK), Blocks.CALCITE)),
                            ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)
                    ))
                    .addCriterion("hold_bc", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.BLOOMED_CALCITE.get()))
                    .requirements(AdvancementRequirements.anyOf(List.of("douse", "hold_bc")))
                    .save(saver, ModConstants.id("bloomed_calcite_craft"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            BlockRegistry.CALCITE_DECORATED_POT.get(),
                            Component.translatable("advancements.paradise_lost.calcite_sherd.title"),
                            Component.translatable("advancements.paradise_lost.calcite_sherd.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_sherd", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ParadiseLostItemTags.CALCITE_POT_SHERDS)
                    ))
                    .addCriterion("remains", LootTableTrigger.TriggerInstance.lootTableUsed(ParadiseLostLootTables.REMAINS_COMMON))
                    .requirements(AdvancementRequirements.allOf(List.of("remains", "has_sherd")))
                    .sendsTelemetryEvent()
                    .save(saver, ModConstants.id("calcite_sherd"), existingFileHelper);

            AdvancementHolder circletStored = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ItemRegistry.XP_CIRCLET.get(),
                            Component.translatable("advancements.paradise_lost.circlet_stored.title"),
                            Component.translatable("advancements.paradise_lost.circlet_stored.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("charged", emptyLocationCriterion(ParadiseLostCriteria.XP_CIRCLET_CHARGED))
                    .requirements(AdvancementRequirements.allOf(List.of("charged")))
                    .sendsTelemetryEvent()
                    .save(saver, ModConstants.id("circlet_stored"), existingFileHelper);

            ItemStack circletIcon = new ItemStack(ItemRegistry.XP_CIRCLET.get());
            circletIcon.set(ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE, new ParadiseLostDataComponentTypes.XpCircletChargeComponent(100));
            Advancement.Builder.advancement()
                    .parent(circletStored)
                    .display(
                            circletIcon,
                            Component.translatable("advancements.paradise_lost.circlet_stored_a_lot.title"),
                            Component.translatable("advancements.paradise_lost.circlet_stored_a_lot.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            true
                    )
                    .addCriterion("charged", emptyLocationCriterion(ParadiseLostCriteria.XP_CIRCLET_CHARGED_30))
                    .requirements(AdvancementRequirements.allOf(List.of("charged")))
                    .sendsTelemetryEvent()
                    .save(saver, ModConstants.id("circlet_stored_a_lot"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(bloomedCalciteCraft)
                    .display(
                            BlockRegistry.BLOOMED_CALCITE.get(),
                            Component.translatable("advancements.paradise_lost.enter_paradise_lost.title"),
                            Component.translatable("advancements.paradise_lost.enter_paradise_lost.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY))
                    .save(saver, ModConstants.id("enter_paradise_lost"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            BlockRegistry.LEVITATOR.get(),
                            Component.translatable("advancements.paradise_lost.float_up_paradise_lost.title"),
                            Component.translatable("advancements.paradise_lost.float_up_paradise_lost.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            true
                    )
                    .addCriterion("worldtop", PlayerTrigger.TriggerInstance.located(
                            LocationPredicate.Builder.location()
                                    .setDimension(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY)
                                    .setY(MinMaxBounds.Doubles.between(420.0, 450.0))
                    ))
                    .requirements(AdvancementRequirements.allOf(List.of("worldtop")))
                    .save(saver, ModConstants.id("float_up_paradise_lost"), existingFileHelper);

            AdvancementHolder tameMoa = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            Items.SADDLE,
                            Component.translatable("advancements.paradise_lost.tame_moa.title"),
                            Component.translatable("advancements.paradise_lost.tame_moa.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(
                            EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), EntityRegistry.MOA.get())
                    ))
                    .requirements(AdvancementRequirements.allOf(List.of("tame")))
                    .save(saver, ModConstants.id("tame_moa"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(tameMoa)
                    .display(
                            BlockRegistry.FOOD_BOWL.get(),
                            Component.translatable("advancements.paradise_lost.food_bowl_fill.title"),
                            Component.translatable("advancements.paradise_lost.food_bowl_fill.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("fill", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                            LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registries.lookupOrThrow(Registries.BLOCK), BlockRegistry.FOOD_BOWL.get())),
                            ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), Tags.Items.FOODS_RAW_MEAT)
                    ))
                    .requirements(AdvancementRequirements.allOf(List.of("fill")))
                    .save(saver, ModConstants.id("food_bowl_fill"), existingFileHelper);

            AdvancementHolder motherAurel = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get(),
                            Component.translatable("advancements.paradise_lost.mother_aurel.title"),
                            Component.translatable("advancements.paradise_lost.mother_aurel.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("leaves", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.MOTHER_AUREL_WOODSTUFF.leaves().get()))
                    .addCriterion("log", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get()))
                    .addCriterion("sapling", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.MOTHER_AUREL_WOODSTUFF.sapling().get()))
                    .addCriterion("stripped_log", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.MOTHER_AUREL_WOODSTUFF.strippedLog().get()))
                    .requirements(AdvancementRequirements.anyOf(List.of("log", "stripped_log", "leaves", "sapling")))
                    .save(saver, ModConstants.id("mother_aurel"), existingFileHelper);

            AdvancementHolder motherAurelStrip = Advancement.Builder.advancement()
                    .parent(motherAurel)
                    .display(
                            ItemRegistry.GOLDEN_AMBER.get(),
                            Component.translatable("advancements.paradise_lost.mother_aurel_strip.title"),
                            Component.translatable("advancements.paradise_lost.mother_aurel_strip.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("hold_amber", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GOLDEN_AMBER.get()))
                    .addCriterion("strip", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                            LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registries.lookupOrThrow(Registries.BLOCK), BlockRegistry.MOTHER_AUREL_WOODSTUFF.log().get())),
                            ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ItemTags.AXES)
                    ))
                    .requirements(AdvancementRequirements.anyOf(List.of("strip", "hold_amber")))
                    .save(saver, ModConstants.id("mother_aurel_strip"), existingFileHelper);

            AdvancementHolder glazedUpgradeFind = Advancement.Builder.advancement()
                    .parent(motherAurelStrip)
                    .display(
                            ItemRegistry.GLAZED_GOLD_UPGRADE.get(),
                            Component.translatable("advancements.paradise_lost.glazed_upgrade_find.title"),
                            Component.translatable("advancements.paradise_lost.glazed_upgrade_find.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("upgrade", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_UPGRADE.get()))
                    .requirements(AdvancementRequirements.allOf(List.of("upgrade")))
                    .save(saver, ModConstants.id("glazed_upgrade_find"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(glazedUpgradeFind)
                    .display(
                            ItemRegistry.GLAZED_GOLD_PICKAXE.get(),
                            Component.translatable("advancements.paradise_lost.glazed_upgrade_use.title"),
                            Component.translatable("advancements.paradise_lost.glazed_upgrade_use.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("axe", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_AXE.get()))
                    .addCriterion("boots", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_BOOTS.get()))
                    .addCriterion("chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_CHESTPLATE.get()))
                    .addCriterion("helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_HELMET.get()))
                    .addCriterion("hoe", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_HOE.get()))
                    .addCriterion("leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_LEGGINGS.get()))
                    .addCriterion("pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_PICKAXE.get()))
                    .addCriterion("shovel", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_SHOVEL.get()))
                    .addCriterion("sword", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.GLAZED_GOLD_SWORD.get()))
                    .requirements(AdvancementRequirements.anyOf(List.of(
                            "shovel", "pickaxe", "axe", "sword", "hoe", "helmet", "chestplate", "leggings", "boots"
                    )))
                    .save(saver, ModConstants.id("glazed_upgrade_use"), existingFileHelper);

            AdvancementHolder nitraFind = Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ItemRegistry.NITRA_SEED.get(),
                            Component.translatable("advancements.paradise_lost.nitra_find.title"),
                            Component.translatable("advancements.paradise_lost.nitra_find.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("find", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.NITRA_SEED.get()))
                    .requirements(AdvancementRequirements.allOf(List.of("find")))
                    .save(saver, ModConstants.id("nitra_find"), existingFileHelper);

            AdvancementHolder nitraBunch = Advancement.Builder.advancement()
                    .parent(nitraFind)
                    .display(
                            BlockRegistry.NITRA_BUNCH.get(),
                            Component.translatable("advancements.paradise_lost.nitra_bunch.title"),
                            Component.translatable("advancements.paradise_lost.nitra_bunch.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("find", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.NITRA_BUNCH.get()))
                    .requirements(AdvancementRequirements.allOf(List.of("find")))
                    .save(saver, ModConstants.id("nitra_bunch"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ItemRegistry.POPOM_JELLY.get(),
                            Component.translatable("advancements.paradise_lost.squeeze_popom.title"),
                            Component.translatable("advancements.paradise_lost.squeeze_popom.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("squeeze", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                            ItemPredicate.Builder.item(),
                            Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(registries.lookupOrThrow(Registries.ENTITY_TYPE), EntityRegistry.POPOM.get())))
                    ))
                    .requirements(AdvancementRequirements.allOf(List.of("squeeze")))
                    .save(saver, ModConstants.id("squeeze_popom"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(nitraBunch)
                    .display(
                            BlockRegistry.SURTRUM.get(),
                            Component.translatable("advancements.paradise_lost.surtrum_blast.title"),
                            Component.translatable("advancements.paradise_lost.surtrum_blast.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("block", InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.SURTRUM.get()))
                    .addCriterion("raw", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.RAW_SURTRUM.get()))
                    .requirements(AdvancementRequirements.anyOf(List.of("raw", "block")))
                    .save(saver, ModConstants.id("surtrum_blast"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            ItemRegistry.TOTEM_OF_LEVITATION.get(),
                            Component.translatable("advancements.paradise_lost.use_levitation_totem.title"),
                            Component.translatable("advancements.paradise_lost.use_levitation_totem.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion("used_totem", UsedTotemTrigger.TriggerInstance.usedTotem(registries.lookupOrThrow(Registries.ITEM), ItemRegistry.TOTEM_OF_LEVITATION.get()))
                    .requirements(AdvancementRequirements.allOf(List.of("used_totem")))
                    .sendsTelemetryEvent()
                    .save(saver, ModConstants.id("use_levitation_totem"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(
                            BlockRegistry.COLD_CLOUD.get(),
                            Component.translatable("advancements.paradise_lost.void_paradise_lost.title"),
                            Component.translatable("advancements.paradise_lost.void_paradise_lost.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            true
                    )
                    .addCriterion("invoid", PlayerTrigger.TriggerInstance.located(
                            LocationPredicate.Builder.location()
                                    .setDimension(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY)
                                    .setY(MinMaxBounds.Doubles.between(-100.0, -7.0))
                    ))
                    .requirements(AdvancementRequirements.allOf(List.of("invoid")))
                    .save(saver, ModConstants.id("void_paradise_lost"), existingFileHelper);
        }

        private static Criterion<?> emptyLocationCriterion(ItemUsedOnLocationTrigger trigger) {
            return trigger.createCriterion(new ItemUsedOnLocationTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
        }
    }
}
