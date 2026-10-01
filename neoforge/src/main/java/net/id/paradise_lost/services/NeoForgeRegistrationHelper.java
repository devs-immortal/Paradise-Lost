package net.id.paradise_lost.services;

import net.id.paradise_lost.platform.services.IRegistrationHelper;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeRegistrationHelper implements IRegistrationHelper {
    private static final Map<Block, Block> STRIPPABLES = new ConcurrentHashMap<>();
    private static final Map<Block, Block> TILLABLES = new ConcurrentHashMap<>();
    private static final Map<Block, Block> FLATTENABLES = new ConcurrentHashMap<>();

    private static final Map<Item, Integer> FUELS = new ConcurrentHashMap<>();
    private static final Map<Item, Float> COMPOST = new ConcurrentHashMap<>();
    private static boolean toolListenersRegistered;

    @Override
    public void setFlammable(Object block, int encouragement, int flammability) {
        if (block instanceof Block b) {
            ((FireBlock) Blocks.FIRE).setFlammable(b, encouragement, flammability);
        }
    }

    @Override
    public void registerStrippable(Object log, Object stripped) {

        if (log instanceof Block logBlock && stripped instanceof Block strippedBlock) {
            STRIPPABLES.put(logBlock, strippedBlock);
        }
    }

    @Override
    public void registerTillable(Object block, Object farmlandResult) {
        ensureToolListeners();
        if (block instanceof Block b && farmlandResult instanceof Block result) {
            TILLABLES.put(b, result);
        }
    }

    @Override
    public void registerFlattenable(Object block, Object pathResult) {
        ensureToolListeners();
        if (block instanceof Block b && pathResult instanceof Block result) {
            FLATTENABLES.put(b, result);
        }
    }

    private static void ensureToolListeners() {
        if (toolListenersRegistered) {
            return;
        }
        toolListenersRegistered = true;
        NeoForge.EVENT_BUS.addListener(NeoForgeRegistrationHelper::onToolModification);
    }

    private static void onToolModification(BlockEvent.BlockToolModificationEvent event) {
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (event.getItemAbility() == ItemAbilities.HOE_TILL) {
            Block tilled = TILLABLES.get(block);
            if (tilled != null) {
                event.setFinalState(tilled.defaultBlockState());
            }
        } else if (event.getItemAbility() == ItemAbilities.SHOVEL_FLATTEN) {
            Block path = FLATTENABLES.get(block);
            if (path != null) {
                event.setFinalState(path.defaultBlockState());
            }
        }
    }

    @Override
    public void registerFuel(ItemLike item, int burnTime) {

        FUELS.put(item.asItem(), burnTime);
    }

    @Override
    public void registerCompostable(ItemLike item, float chance) {
        COMPOST.put(item.asItem(), chance);
    }

    public static Map<Item, Float> getCompostables() {
        return Map.copyOf(COMPOST);
    }

    public static Map<Item, Integer> getFuels() {
        return Map.copyOf(FUELS);
    }

    public static Map<Block, Block> getStrippables() {
        return Map.copyOf(STRIPPABLES);
    }

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
    }

    @Override
    public void registerCreativeTabEntries() {
        NeoForgePlatformHelper.modBus().addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS)) {
                event.accept(ItemRegistry.MOA_SPAWN_EGG.get());
                event.accept(ItemRegistry.POPOM_SPAWN_EGG.get());
                event.accept(ItemRegistry.QUINT_SPAWN_EGG.get());
                event.accept(ItemRegistry.ENVOY_SPAWN_EGG.get());
                event.accept(ItemRegistry.SENTINEL_SPAWN_EGG.get());
            }
        });
    }
}
