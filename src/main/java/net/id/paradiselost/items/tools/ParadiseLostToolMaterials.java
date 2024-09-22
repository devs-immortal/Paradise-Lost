package net.id.paradiselost.items.tools;

import net.fabricmc.yarn.constants.MiningLevels;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.IngredientUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

import static net.id.paradiselost.items.tools.ParadiseLostToolMaterials.ParadiseToolMaterial.create;

@SuppressWarnings("unused")
public class ParadiseLostToolMaterials {
    public static ToolMaterial OLVITE = create(BlockTags.INCORRECT_FOR_IRON_TOOL, MiningLevels.IRON, 250, 4.5f, 2f, 14, () -> IngredientUtil.itemIngredient(ParadiseLostItems.OLVITE));
    public static ToolMaterial SURTRUM = create(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, MiningLevels.DIAMOND, 827, 7.0f, 3f, 16, () -> IngredientUtil.itemIngredient(ParadiseLostItems.REFINED_SURTRUM));
    public static ToolMaterial GLAZED_GOLD = create(BlockTags.INCORRECT_FOR_IRON_TOOL, MiningLevels.IRON, 131, 12f, 2f, 22, () -> IngredientUtil.itemIngredient(ParadiseLostItems.GOLDEN_AMBER));



    static class ParadiseToolMaterial implements ToolMaterial {

        private final int miningLevel;
        private final int itemDurability;
        private final float miningSpeed;
        private final float attackDamage;
        private final int enchantability;
        private final Lazy<Ingredient> repairIngredient;

        ParadiseToolMaterial(TagKey incorrect, int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier repairIngredient) {
            this.miningLevel = miningLevel;
            this.itemDurability = itemDurability;
            this.miningSpeed = miningSpeed;
            this.attackDamage = attackDamage;
            this.enchantability = enchantability;
            this.repairIngredient = new Lazy(repairIngredient);
        }

        public static ParadiseToolMaterial create(TagKey incorrect, int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier repairIngredient) {
            return new ParadiseToolMaterial(incorrect, miningLevel, itemDurability, miningSpeed, attackDamage, enchantability, repairIngredient);
        }

        public int getDurability() {
            return this.itemDurability;
        }

        public float getMiningSpeedMultiplier() {
            return this.miningSpeed;
        }

        public float getAttackDamage() {
            return this.attackDamage;
        }

        @Override
        public TagKey<Block> getInverseTag() {
            return null;
        }

        public int getMiningLevel() {
            return this.miningLevel;
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }

}
