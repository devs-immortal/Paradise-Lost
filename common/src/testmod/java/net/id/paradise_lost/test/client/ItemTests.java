package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Step;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.id.paradise_lost.item.armor.XpCircletItem;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.id.paradise_lost.test.TestHelpers.*;
import static net.id.paradise_lost.test.client.ClientTestHelpers.*;
import static net.id.paradise_lost.registry.ItemRegistry.*;

public final class ItemTests {
    private static BlockPos pos;
    private static final List<Integer> entityIds = new ArrayList<>();

    private ItemTests() {
    }

    @ClientTest(value = "names use item lang keys", group = "Items")
    private static Step itemNames() {
        return Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            for (Item item : List.<Item>of(BLACKCURRANT.get(), AMADRYS_BUSHEL.get(), NITRA_SEED.get(), SWEDROOT.get(), FLAXSEED.get())) {
                String key = Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(item));
                String expected = I18n.get(key);
                String actual = new ItemStack(item).getHoverName().getString();

                if (!I18n.exists(key)) {
                    wrong.add("no lang entry for " + key);
                } else if (!actual.equals(expected)) {
                    wrong.add(BuiltInRegistries.ITEM.getKey(item) + " shows '" + actual + "', expected '" + expected + "'");
                }
            }

            checkAll(wrong);
        });
    }

    @ClientTest(value = "igniting tools smelt mob drops", group = "Items")
    private static Step ignitingToolSmeltsDrops() {
        return Step.run(0, () -> onServer(server -> {
            ServerPlayer player = player(server);
            ServerLevel world = player.serverLevel();
            hold(player, SURTRUM_SWORD.get());

            BlockPos at = ahead(player, 3, 0);
            Cow cow = EntityType.COW.create(world);
            cow.moveTo(at.getX() + 0.5, at.getY(), at.getZ() + 0.5, 0, 0);
            world.addFreshEntity(cow);
            cow.hurt(world.damageSources().playerAttack(player), 100);

            Map<Item, Integer> loot = drops(world, at);
            check(loot.containsKey(Items.COOKED_BEEF) && !loot.containsKey(Items.BEEF), "cow dropped " + describe(loot));
        }));
    }

    @ClientTest(value = "olvite spyglass zooms like the vanilla spyglass", group = "Items")
    private static List<Step> olviteSpyglassScopes() {
        return List.of(
                Step.run(0, () -> onServer(server -> hold(player(server), OLVITE_SPYGLASS.get()))),
                Step.run(10, () -> client().options.keyUse.setDown(true)),
                Step.until("client player to scope", 20, () -> client().player.isScoping()),
                Step.run(0, () -> client().options.keyUse.setDown(false))
        );
    }

    @ClientTest(value = "collects one soul per mob type and hits harder", group = "Soul blade")
    private static List<Step> soulBladeCollectsSouls() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    hold(player, SOUL_BLADE.get());

                    entityIds.clear();
                    for (EntityType<? extends Mob> type : List.of(EntityType.COW, EntityType.PIG, EntityType.COW)) {
                        int id = spawnNoAi(player, type, ahead(player, 2, entityIds.size() - 1));
                        ((LivingEntity) world(server).getEntity(id)).setHealth(1);
                        entityIds.add(id);
                    }
                })),
                Step.run(10, () -> client().gameMode.attack(client().player, client().level.getEntity(entityIds.get(0)))),
                Step.run(15, () -> client().gameMode.attack(client().player, client().level.getEntity(entityIds.get(1)))),
                Step.run(15, () -> client().gameMode.attack(client().player, client().level.getEntity(entityIds.get(2)))),
                Step.run(10, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    ItemStack blade = player.getMainHandItem();

                    var souls = blade.get(ParadiseLostDataComponentTypes.COLLECTED_SOULS);
                    check(souls != null && souls.soulIds().size() == 2, "souls after killing cow, pig, cow: " + (souls == null ? "none" : souls.soulIds()));

                    float bonus = blade.getItem().getAttackDamageBonus(null, 0, player.serverLevel().damageSources().playerAttack(player));
                    check(bonus == 0.5F, "attack bonus " + bonus + ", expected 2 * 0.25");
                }))
        );
    }


    @ClientTest(value = "stores XP on death and gives it back when worn", group = "XP circlet")
    private static List<Step> xpCircletStoresXpOnDeath() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    pos = player.blockPosition();

                    player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(XP_CIRCLET.get()));
                    player.setExperienceLevels(10);
                    player.setExperiencePoints(0);
                    player.setRespawnPosition(Level.OVERWORLD, pos, 0, true, false);

                    player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
                })),
                Step.until("respawn", 100, () -> client().player.isAlive() && fromServer(server -> player(server).isAlive())),
                Step.run(5, () -> onServer(server -> {
                    player(server).setRespawnPosition(Level.OVERWORLD, null, 0, false, false);

                    ServerLevel world = server.overworld();
                    List<ItemEntity> circlets = world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(6), e -> e.getItem().is(XP_CIRCLET.get()));
                    if (circlets.size() != 1) {
                        List<String> nearby = world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(16), e -> true).stream()
                                .map(e -> e.getItem() + " at " + e.blockPosition().toShortString()).toList();
                        throw new AssertionError(circlets.size() + " circlets dropped where the player died at " + pos.toShortString()
                                + " items nearby: " + nearby + " respawned player wears " + player(server).getItemBySlot(EquipmentSlot.HEAD));
                    }

                    ItemStack circlet = circlets.getFirst().getItem().copy();
                    circlets.getFirst().discard();
                    var charge = circlet.get(ParadiseLostDataComponentTypes.XP_CIRCLET_CHARGE);
                    check(XpCircletItem.isCharged(circlet) && charge.storedXp() == 160, "circlet holds " + charge);

                    ServerPlayer respawned = player(server);
                    respawned.setExperienceLevels(0);
                    respawned.setItemSlot(EquipmentSlot.HEAD, circlet);
                    check(respawned.experienceLevel == 10, "player is level " + respawned.experienceLevel + " after wearing the charged circlet");
                }))
        );
    }

    @ClientTest(value = "captures a mob's stats", group = "Bloodstone")
    private static List<Step> bloodstoneCaptures() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityIds.clear();
                    entityIds.add(spawnNoAi(player, EntityType.COW, ahead(player, 2, 0)));
                    hold(player, CHERINE_BLOODSTONE.get());
                })),
                Step.run(10, () -> interact(entityIds.getFirst())),
                Step.until("bloodstone data", 20, () -> fromServer(server -> player(server).getMainHandItem().has(ParadiseLostDataComponentTypes.BLOODSTONE))),
                Step.run(0, () -> client().player.setXRot(25)),
                Step.run(10, () -> screenshot("bloodstone_hud.png", true))
        );
    }

    private static void lookDownAt(net.minecraft.world.level.block.state.BlockState block, Item hand) {
        onServer(server -> {
            ServerPlayer player = player(server);
            pos = ahead(player, 1, 0);
            player.serverLevel().setBlockAndUpdate(pos, block);
            hold(player, hand);
        });
        client().player.setXRot(60);
    }

    private static void useItem() {
        client().gameMode.useItem(client().player, InteractionHand.MAIN_HAND);
    }

    private static boolean holding(Item item) {
        return fromServer(server -> player(server).getMainHandItem().is(item));
    }

    @ClientTest(value = "picks up and places water", group = "Aurel bucket")
    private static List<Step> aurelBucketWater() {
        return List.of(
                Step.run(0, () -> lookDownAt(Blocks.WATER.defaultBlockState(), AUREL_BUCKET.get())),
                Step.run(10, ItemTests::useItem),
                Step.until("aurel water bucket", 20, () -> holding(AUREL_WATER_BUCKET.get())),
                Step.run(0, () -> check(fromServer(server -> world(server).getBlockState(pos).isAir()), "water source was not taken")),
                Step.run(10, ItemTests::useItem),
                Step.until("water placed back", 20, () -> fromServer(server -> world(server).getBlockState(pos).is(Blocks.WATER))),
                Step.run(0, () -> check(holding(AUREL_BUCKET.get()), "hand does not hold an empty aurel bucket"))
        );
    }

    @ClientTest(value = "milks a cow", group = "Aurel bucket")
    private static List<Step> aurelBucketMilk() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    entityIds.clear();
                    entityIds.add(spawnNoAi(player, EntityType.COW, ahead(player, 2, 0)));
                    hold(player, AUREL_BUCKET.get());
                })),
                Step.run(10, () -> interact(entityIds.getFirst())),
                Step.until("aurel milk bucket", 20, () -> holding(AUREL_MILK_BUCKET.get()))
        );
    }

    @ClientTest(value = "picks up powder snow", group = "Aurel bucket")
    private static List<Step> aurelBucketPowderSnow() {
        return List.of(
                Step.run(0, () -> lookDownAt(Blocks.POWDER_SNOW.defaultBlockState(), AUREL_BUCKET.get())),
                Step.run(10, ItemTests::useItem),
                Step.until("aurel powder snow bucket", 20, () -> holding(AUREL_POWDER_SNOW_BUCKET.get()))
        );
    }

    @ClientTest(value = "levitation totem stops a big fall and shows on the client", group = "Totem")
    private static List<Step> levitationTotemStopsFall() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    hold(player, TOTEM_OF_LEVITATION.get());
                    player.teleportTo(player.getX(), player.getY() + 18, player.getZ());
                })),
                Step.run(5, () -> {}),
                Step.until("totem used", 100, () -> fromServer(server -> player(server).getMainHandItem().isEmpty())),
                Step.until("totem animation on the client", 20, () -> Recorder.itemActivations.stream().anyMatch(s -> s.is(TOTEM_OF_LEVITATION.get()))),
                Step.run(0, () -> check(!Recorder.playerHurtBy(DamageTypes.FALL), "took fall damage from the fall the totem stopped"))
        );
    }

    @ClientTest(value = "boots soften a 14 block fall", group = "Floaty")
    private static List<Step> floatyBootsFall() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setItemSlot(EquipmentSlot.FEET, new ItemStack(FLOATY_BOOTS.get()));
                    player.teleportTo(player.getX(), player.getY() + 14, player.getZ());
                })),
                Step.run(5, () -> {}),
                Step.until("landing", 100, () -> fromServer(server -> player(server).onGround())),
                Step.run(5, () -> check(!Recorder.playerHurtBy(DamageTypes.FALL), "took fall damage"))
        );
    }

    @ClientTest(value = "anchor state syncs to the client", group = "Floaty")
    private static List<Step> floatyAnchorSync() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(FLOATY_LEGGINGS.get()));
                    player.teleportTo(player.getX(), player.getY() + 10, player.getZ());
                })),
                Step.run(5, () -> onServer(server -> ((ParadiseLostEntityExtensions) player(server)).setFloatyAnchored(true))),
                Step.until("client to see the anchor", 20, () -> ((ParadiseLostEntityExtensions) client().player).isFloatyAnchored()),
                Step.run(20, () -> onServer(server -> check(player(server).getY() > -55, "anchored player fell to y=" + player(server).getY()))),
                Step.run(0, () -> onServer(server -> ((ParadiseLostEntityExtensions) player(server)).setFloatyAnchored(false))),
                Step.until("client to see the release", 20, () -> !((ParadiseLostEntityExtensions) client().player).isFloatyAnchored())
        );
    }

    @ClientTest(value = "double jump in the air toggles the anchor", group = "Floaty")
    private static List<Step> floatyDoubleJumpAnchors() {
        return List.of(
                Step.run(0, () -> onServer(server -> {
                    ServerPlayer player = player(server);
                    player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(FLOATY_LEGGINGS.get()));
                    player.teleportTo(player.getX(), player.getY() + 20, player.getZ());
                })),
                Step.run(10, () -> client().options.keyJump.setDown(true)),
                Step.run(2, () -> client().options.keyJump.setDown(false)),
                Step.run(2, () -> client().options.keyJump.setDown(true)),
                Step.run(2, () -> client().options.keyJump.setDown(false)),
                Step.until("server to see the anchor", 20, () -> fromServer(server -> ((ParadiseLostEntityExtensions) player(server)).isFloatyAnchored())),
                Step.run(0, () -> onServer(server -> ((ParadiseLostEntityExtensions) player(server)).setFloatyAnchored(false)))
        );
    }
}
