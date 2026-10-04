package net.id.paradise_lost.services;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.id.paradise_lost.platform.services.IRegistrationHelper;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

public class FabricRegistrationHelper implements IRegistrationHelper {
    @Override
    public void setFlammable(Object block, int encouragement, int flammability) {
        if (block instanceof Block b) {
            FlammableBlockRegistry.getDefaultInstance().add(b, encouragement, flammability);
        }
    }

    @Override
    public void registerTillable(Object block, Object farmlandResult) {
        if (block instanceof Block b && farmlandResult instanceof Block result) {
            TillableBlockRegistry.register(b, HoeItem::onlyIfAirAbove, result.defaultBlockState());
        }
    }

    @Override
    public void registerFlattenable(Object block, Object pathResult) {
        if (block instanceof Block b && pathResult instanceof Block result) {
            FlattenableBlockRegistry.register(b, result.defaultBlockState());
        }
    }

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRuleRegistry.register(
                name, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    @Override
    public void registerCreativeTabEntries() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(itemGroup -> {
            itemGroup.accept(ItemRegistry.MOA_SPAWN_EGG.get());
            itemGroup.accept(ItemRegistry.POPOM_SPAWN_EGG.get());
            itemGroup.accept(ItemRegistry.QUINT_SPAWN_EGG.get());
            itemGroup.accept(ItemRegistry.ENVOY_SPAWN_EGG.get());
            itemGroup.accept(ItemRegistry.SENTINEL_SPAWN_EGG.get());
        });
    }
}
