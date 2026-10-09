package net.id.paradise_lost.clienttest;

import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.Optional;

import static net.id.paradise_lost.clienttest.TestHelpers.*;

final class TestWorld {
    private static final String NAME = "clienttest";

    private TestWorld() {
    }

    static void create() {
        try {
            FileUtils.deleteDirectory(client().getLevelSource().getBaseDir().resolve(NAME).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WorldDataConfiguration data = ModResourcePackUtil.createDefaultDataConfiguration();
        GameRules rules = new GameRules();

        rules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, null);
        rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, null);
        rules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
        LevelSettings settings = new LevelSettings(NAME, GameType.SURVIVAL, false, Difficulty.PEACEFUL, true, rules, data);

        WorldOptions options = WorldOptions.defaultWithRandomSeed();
        client().createWorldOpenFlows().createFreshLevel(NAME, settings, options, TestWorld::flatWithoutStructures, new TitleScreen());
    }

    private static WorldDimensions flatWithoutStructures(RegistryAccess registries) {
        WorldDimensions dimensions = registries.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(WorldPresets.FLAT).value().createWorldDimensions();
        FlatLevelGeneratorSettings flat = ((FlatLevelSource) dimensions.overworld()).settings();
        FlatLevelGeneratorSettings noStructures = flat.withBiomeAndLayers(flat.getLayersInfo(), Optional.of(HolderSet.direct()), flat.getBiome());
        return dimensions.replaceOverworldGenerator(registries, new FlatLevelSource(noStructures));
    }

    static boolean playerAlive() {
        Minecraft client = client();
        return client.player != null && client.player.isAlive() && fromServer(server -> player(server).isAlive());
    }

    static void resetPlayer(int slot) {
        client().options.keyUse.setDown(false);
        client().options.keyShift.setDown(false);
        client().player.getInventory().selected = 0;
        if (client().screen != null) client().player.closeContainer();
        onServer(server -> {
            server.setDifficulty(Difficulty.PEACEFUL, true);
            server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_VOID_KILLS).set(false, server);
            server.getGameRules().getRule(ParadiseLostGameRules.PARADISE_PORTAL_ENABLED).set(true, server);
            ServerPlayer player = player(server);
            player.stopRiding();
            player.setGameMode(GameType.SURVIVAL);
            player.removeAllEffects();
            player.clearFire();
            player.setHealth(player.getMaxHealth());
            player.getFoodData().setFoodLevel(20);
            player.getInventory().clearContent();
            player.setDeltaMovement(Vec3.ZERO);
            player.fallDistance = 0;
            teleport(player, Level.OVERWORLD, slot * 64 + 0.5, -60, 0.5, 0);
        });
        Recorder.reset();
    }
}
