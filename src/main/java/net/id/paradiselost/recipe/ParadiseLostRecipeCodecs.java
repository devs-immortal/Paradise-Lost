package net.id.paradiselost.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;

public class ParadiseLostRecipeCodecs {

    private static final Codec<Item> CRAFTING_RESULT_ITEM;
    public static final Codec<ItemStack> NBT_CRAFTING_RESULT;

    public ParadiseLostRecipeCodecs() {
    }

    static {
        CRAFTING_RESULT_ITEM = Codecs.validate(Registries.ITEM.getCodec(), (item) -> {
            return item == Items.AIR ? DataResult.error(() -> {
                return "Crafting result must not be minecraft:air";
            }) : DataResult.success(item);
        });
        NBT_CRAFTING_RESULT = RecordCodecBuilder.create((instance) -> {
            return instance.group(
                    CRAFTING_RESULT_ITEM.fieldOf("item").forGetter(ItemStack::getItem),
                    Codecs.createStrictOptionalFieldCodec(Codecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount),
                    Codecs.createStrictOptionalFieldCodec(NbtCompound.CODEC, "nbt", new NbtCompound()).forGetter(ItemStack::getNbt)
            ).apply(instance, ParadiseLostRecipeCodecs::nbtItem);
        });


    }

    private static ItemStack nbtItem(ItemConvertible item, int count, NbtCompound nbt) {
        var stack = new ItemStack(item, count);
        stack.setNbt(nbt);
        return stack;
    }

}
