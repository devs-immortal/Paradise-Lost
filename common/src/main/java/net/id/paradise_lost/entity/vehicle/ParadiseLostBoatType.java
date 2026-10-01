package net.id.paradise_lost.entity.vehicle;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registration.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;
import net.id.paradise_lost.registry.ItemRegistry;

public enum ParadiseLostBoatType {
    AUREL("aurel", BlockRegistry.AUREL_WOODSTUFF::plank),
    MOTHER_AUREL("mother_aurel", BlockRegistry.MOTHER_AUREL_WOODSTUFF::plank),
    MENTH("menth", BlockRegistry.MENTH_WOODSTUFF::plank),
    WISTERIA("wisteria", BlockRegistry.WISTERIA_WOODSTUFF::plank);

    private final String name;
    private final Supplier<? extends RegistryObject<Block, ? extends Block>> planks;

    ParadiseLostBoatType(String name, Supplier<? extends RegistryObject<Block, ? extends Block>> planks) {
        this.name = name;
        this.planks = planks;
    }

    public String getName() {
        return name;
    }

    public Block getPlanks() {
        return planks.get().get();
    }

    public ResourceLocation boatTexture() {
        return ResourceLocation.fromNamespaceAndPath("paradise_lost", "textures/entity/boat/" + name + ".png");
    }

    public ResourceLocation chestBoatTexture() {
        return ResourceLocation.fromNamespaceAndPath("paradise_lost", "textures/entity/chest_boat/" + name + ".png");
    }

    public Item boatItem() {
        return switch (this) {
            case AUREL -> ItemRegistry.AUREL_BOATS.boat().get();
            case MOTHER_AUREL -> ItemRegistry.MOTHER_AUREL_BOATS.boat().get();
            case MENTH -> ItemRegistry.MENTH_BOATS.boat().get();
            case WISTERIA -> ItemRegistry.WISTERIA_BOATS.boat().get();
        };
    }

    public Item chestBoatItem() {
        return switch (this) {
            case AUREL -> ItemRegistry.AUREL_BOATS.chestBoat().get();
            case MOTHER_AUREL -> ItemRegistry.MOTHER_AUREL_BOATS.chestBoat().get();
            case MENTH -> ItemRegistry.MENTH_BOATS.chestBoat().get();
            case WISTERIA -> ItemRegistry.WISTERIA_BOATS.chestBoat().get();
        };
    }

    public static ParadiseLostBoatType byName(String name) {
        for (ParadiseLostBoatType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return AUREL;
    }
}
