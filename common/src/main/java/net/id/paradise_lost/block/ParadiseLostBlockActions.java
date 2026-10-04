package net.id.paradise_lost.block;

import net.id.paradise_lost.data.ParadiseLostDataEntries;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.util.RenderUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParadiseLostBlockActions {
    public static final BlockBehaviour.StatePredicate never = (state, view, pos) -> false;
    public static final BlockBehaviour.StatePredicate always = (state, view, pos) -> true;

    public static Consumer<Block> flammable(int spread, int burn) {
        return (block) -> Services.REGISTRATION.setFlammable(block, spread, burn);
    }

    public static final Consumer<Block> flammableLog = flammable(5, 5);
    public static final Consumer<Block> flammablePlanks = flammable(20, 5);
    public static final Consumer<Block> flammableLeaves = flammable(60, 30);
    public static final Consumer<Block> flammablePlant = flammable(60, 100);

    public static final Consumer<Block> translucentRenderLayer = clientOnly(RenderUtils::transparentRenderLayer);
    public static final Consumer<Block> cutoutRenderLayer = clientOnly(RenderUtils::cutoutRenderLayer);
    public static final Consumer<Block> cutoutMippedRenderLayer = clientOnly(RenderUtils::cutoutMippedRenderLayer);

    public static Consumer<Block> stripsTo(Supplier<? extends Block> stripped) {
        return (original) -> ParadiseLostDataEntries.registerStrippable(original, stripped.get());
    }

    public static Consumer<Block> stripsTo(RegistryObject<Block, ? extends Block> stripped) {
        return stripsTo((Supplier<? extends Block>) stripped);
    }

    public static Consumer<Block> tillable() {
        return (block) -> Services.REGISTRATION.registerTillable(block, BlockRegistry.FARMLAND.get());
    }

    public static Consumer<Block> coarseTillable() {
        return (block) -> Services.REGISTRATION.registerTillable(block, BlockRegistry.DIRT.get());
    }

    public static Consumer<Block> flattenable(Supplier<? extends Block> turnInto) {
        return (block) -> Services.REGISTRATION.registerFlattenable(block, turnInto.get());
    }

    public static Consumer<Block> flattenable(RegistryObject<Block, ? extends Block> turnInto) {
        return flattenable((Supplier<? extends Block>) turnInto);
    }

    public static Consumer<Block> clientOnly(Consumer<Block> func) {

        return (block) -> {
            if (Services.PLATFORM.isPhysicalClient()) {
                func.accept(block);
            }
        };
    }
}
