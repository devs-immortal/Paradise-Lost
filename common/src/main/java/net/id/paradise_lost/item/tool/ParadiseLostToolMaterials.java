package net.id.paradise_lost.item.tool;

import com.google.common.base.Suppliers;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ParadiseLostToolMaterials {
    public static final Tier OLVITE = create(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 4.5f, 2f, 14, () -> Ingredient.of(ItemRegistry.OLVITE.get()));
    public static final Tier SURTRUM = create(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 827, 7.0f, 3f, 16, () -> Ingredient.of(ItemRegistry.REFINED_SURTRUM.get()));
    public static final Tier GLAZED_GOLD = create(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 12f, 2f, 22, () -> Ingredient.of(ItemRegistry.GOLDEN_AMBER.get()));
    public static final Tier SOUL_BLADE = create(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 636, 6.5f, 1.5f, 17, () -> Ingredient.of(BlockRegistry.BLOOMED_CALCITE.get()));

    public static Tier create(final TagKey<Block> incorrect, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        return new ParadiseToolMaterial(incorrect, itemDurability, miningSpeed, attackDamage, enchantability, repairIngredient);
    }

    static class ParadiseToolMaterial implements Tier {

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

        public int getUses() {
            return this.itemDurability;
        }

        public float getSpeed() {
            return this.miningSpeed;
        }

        public float getAttackDamageBonus() {
            return this.attackDamage;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return this.incorrectTag;
        }

        public int getEnchantmentValue() {
            return this.enchantability;
        }

        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }

}
