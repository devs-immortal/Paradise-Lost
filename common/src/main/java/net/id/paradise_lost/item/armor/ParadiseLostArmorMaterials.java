package net.id.paradise_lost.item.armor;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ParadiseLostArmorMaterials {
    private static final RegistrationProvider<ArmorMaterial> ARMOR_MATERIALS =
            RegistrationProvider.get(Registries.ARMOR_MATERIAL, ModConstants.MODID);

    public static final Holder<ArmorMaterial> OLVITE;
    public static final Holder<ArmorMaterial> ORNATE_OLVITE;
    public static final Holder<ArmorMaterial> GLAZED_GOLD;
    public static final Holder<ArmorMaterial> SURTRUM;
    public static final Holder<ArmorMaterial> RELIC;
    public static final Holder<ArmorMaterial> FLOATY;

    static {
        OLVITE = register("olvite", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 4);
            map.put(ArmorItem.Type.CHESTPLATE, 6);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 4);
        }), 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE, 0.0F, 0.0F, () -> Ingredient.of(ItemRegistry.OLVITE.get()));

        ORNATE_OLVITE = register("ornate_olvite", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 4);
            map.put(ArmorItem.Type.CHESTPLATE, 6);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 4);
        }), 9, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE, 0.0F, 0.0F, () -> Ingredient.of(ItemRegistry.OLVITE.get()));
        GLAZED_GOLD = register("glazed_gold", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, 1);
            map.put(ArmorItem.Type.LEGGINGS, 3);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 3);
        }), 25, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_GLAZED_GOLD, 0.0F, 0.0F, () -> Ingredient.of(ItemRegistry.GOLDEN_AMBER.get()));
        SURTRUM = register("surtrum", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 5);
            map.put(ArmorItem.Type.CHESTPLATE, 6);
            map.put(ArmorItem.Type.HELMET, 3);
            map.put(ArmorItem.Type.BODY, 5);
        }), 15, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_SURTRUM, 0.0F, 0.0F, () -> Ingredient.of(ItemRegistry.REFINED_SURTRUM.get()));
        RELIC = register("relic", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, 1);
            map.put(ArmorItem.Type.LEGGINGS, 4);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 4);
        }), 12, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_RELIC, 0.5F, 0.0F, () -> Ingredient.of(BlockRegistry.FLOESTONE.get()));
        FLOATY = register("floaty", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, 1);
            map.put(ArmorItem.Type.LEGGINGS, 2);
            map.put(ArmorItem.Type.CHESTPLATE, 3);
            map.put(ArmorItem.Type.HELMET, 1);
            map.put(ArmorItem.Type.BODY, 2);
        }), 10, ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_RELIC, 0.0F, 0.0F, () -> Ingredient.of(ItemRegistry.LEVITA_GEM.get()));
    }

    private static Holder<ArmorMaterial> register(String id, EnumMap<ArmorItem.Type, Integer> defense, int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ModConstants.id(id)));
        return register(id, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(String id, EnumMap<ArmorItem.Type, Integer> defense, int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient, List<ArmorMaterial.Layer> layers) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enumMap.put(type, defense.get(type));
        }

        ArmorMaterial material = new ArmorMaterial(enumMap, enchantability, equipSound, repairIngredient, layers, toughness, knockbackResistance);
        ARMOR_MATERIALS.register(id, () -> material);
        return Holder.direct(material);
    }
}
