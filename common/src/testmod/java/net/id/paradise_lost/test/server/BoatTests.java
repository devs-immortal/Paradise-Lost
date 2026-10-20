package net.id.paradise_lost.test.server;

import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.test.Step;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.id.paradise_lost.registry.ItemRegistry.*;
import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.server.ServerTestHelpers.level;

public final class BoatTests {
    public static final List<Supplier<? extends EntityType<? extends Boat>>> BOATS = List.of(
            EntityRegistry.AUREL_BOAT, EntityRegistry.MOTHER_AUREL_BOAT, EntityRegistry.MENTH_BOAT, EntityRegistry.WISTERIA_BOAT,
            EntityRegistry.AUREL_CHEST_BOAT, EntityRegistry.MOTHER_AUREL_CHEST_BOAT, EntityRegistry.MENTH_CHEST_BOAT, EntityRegistry.WISTERIA_CHEST_BOAT
    );
    public static final List<String> WOODS = List.of("aurel", "mother_aurel", "menth", "wisteria");

    private BoatTests() {
    }

    public static EntityType<? extends Boat> boat(int index) {
        return BOATS.get(index).get();
    }

    public static Item boatItem(int index) {
        BoatSet set = BOAT_SETS[index % 4];
        return index < 4 ? set.boat().get() : set.chestBoat().get();
    }

    @ServerTest("boats keep type, item and chest contents after relog")
    private static Step boatSaveLoad() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            List<String> wrong = new ArrayList<>();
            for (int i = 0; i < BOATS.size(); i++) {
                Boat boat = boat(i).create(world);
                if (boat instanceof ChestBoat chest) chest.setItem(3, new ItemStack(Items.DIAMOND));
                CompoundTag nbt = new CompoundTag();
                boat.save(nbt);
                Entity loaded = EntityType.create(nbt, world).orElse(null);
                String id = EntityType.getKey(boat(i)).toString();
                if (loaded == null || loaded.getType() != boat(i)) {
                    wrong.add(id + " loaded as " + (loaded == null ? "nothing" : EntityType.getKey(loaded.getType())));
                } else if (!loaded.getPickResult().is(boatItem(i))) {
                    wrong.add(id + " item is " + loaded.getPickResult());
                } else if (loaded instanceof ChestBoat chest && !chest.getItem(3).is(Items.DIAMOND)) {
                    wrong.add(id + " lost its chest contents");
                }
            }
            checkAll(wrong);
        });
    }

    @ServerTest("2.4 custom boat Type migrates to dedicated entity types")
    private static Step legacyBoatTypeMigrates() {
        return Step.run(0, () -> {
            ServerLevel world = level();
            List<String> wrong = new ArrayList<>();
            for (int i = 0; i < WOODS.size(); i++) {
                String wood = WOODS.get(i);
                CompoundTag boatTag = new CompoundTag();
                boatTag.putString("id", "minecraft:boat");
                boatTag.putString("Type", "paradise_lost_" + wood);
                boatTag.put("Pos", listOfDoubles(0.5, 64, 0.5));
                Entity loadedBoat = EntityType.create(boatTag, world).orElse(null);
                EntityType<? extends Boat> expectedBoat = boat(i);
                if (loadedBoat == null || loadedBoat.getType() != expectedBoat) {
                    wrong.add("boat " + wood + " loaded as "
                            + (loadedBoat == null ? "nothing" : EntityType.getKey(loadedBoat.getType())));
                }

                CompoundTag chestTag = new CompoundTag();
                chestTag.putString("id", "minecraft:chest_boat");
                chestTag.putString("Type", "paradise_lost_" + wood);
                chestTag.put("Pos", listOfDoubles(1.5, 64, 0.5));
                Entity loadedChest = EntityType.create(chestTag, world).orElse(null);
                EntityType<? extends Boat> expectedChest = boat(i + 4);
                if (loadedChest == null || loadedChest.getType() != expectedChest) {
                    wrong.add("chest_boat " + wood + " loaded as "
                            + (loadedChest == null ? "nothing" : EntityType.getKey(loadedChest.getType())));
                }
            }
            checkAll(wrong);
        });
    }

    private static ListTag listOfDoubles(double x, double y, double z) {
        ListTag list = new ListTag();
        list.add(DoubleTag.valueOf(x));
        list.add(DoubleTag.valueOf(y));
        list.add(DoubleTag.valueOf(z));
        return list;
    }
}
