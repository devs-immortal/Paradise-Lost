package net.id.paradiselost.items.tools;

import com.google.common.base.Suppliers;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ParadiseLostToolMaterials {
    public static final ToolMaterial OLVITE = create(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 4.5f, 2f, 14, () -> Ingredient.ofItems(ParadiseLostItems.OLVITE));
    public static final ToolMaterial SURTRUM = create(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 827, 7.0f, 3f, 16, () -> Ingredient.ofItems(ParadiseLostItems.REFINED_SURTRUM));
    public static final ToolMaterial GLAZED_GOLD = create(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 12f, 2f, 22, () -> Ingredient.ofItems(ParadiseLostItems.GOLDEN_AMBER));


    public static ToolMaterial create(final TagKey<Block> incorrect, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        return new ParadiseToolMaterial(incorrect, itemDurability, miningSpeed, attackDamage, enchantability, repairIngredient);
    }

    static class ParadiseToolMaterial implements ToolMaterial {

        private final TagKey<Block> incorrectTag;
        private final int itemDurability;
        private final float miningSpeed;
        private final float attackDamage;
        private final int enchantability;
        private final Supplier<Ingredient> repairIngredient;

        ParadiseToolMaterial(final TagKey<Block> incorrect, int itemDurability, float miningSpeed, float attackDamage, int enchantability, final Supplier<Ingredient> repairIngredient) {
            this.incorrectTag = incorrect;
            this.itemDurability = itemDurability;
            this.miningSpeed = miningSpeed;
            this.attackDamage = attackDamage;
            this.enchantability = enchantability;
            this.repairIngredient = Suppliers.memoize(repairIngredient::get);
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
            return this.incorrectTag;
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }

}
