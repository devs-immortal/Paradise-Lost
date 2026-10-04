package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.tag.ParadiseLostItemTags;
import net.id.paradise_lost.util.ParadiseLostEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ParadiseLostEnchantmentProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ParadiseLostEnchantmentProvider::bootstrap);

    public ParadiseLostEnchantmentProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ModConstants.MODID));
    }

    private static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        context.register(
                ParadiseLostEnchantments.RENDING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ParadiseLostItemTags.RENDING_ENCHANTABLE),
                                1,
                                3,
                                Enchantment.dynamicCost(15, 15),
                                Enchantment.dynamicCost(45, 15),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).build(ParadiseLostEnchantments.RENDING.location())
        );
    }

    @Override
    public String getName() {
        return "Paradise Lost Enchantments";
    }
}
