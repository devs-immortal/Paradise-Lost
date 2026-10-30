package net.id.paradise_lost.test.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.test.Step;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.id.paradise_lost.test.TestHelpers.*;

public final class RegistryTests {
    private static final Set<String> DROP_NOTHING = Set.of("blocks/surtrum_air", "blocks/blue_portal", "blocks/suspicious_dirt");

    private RegistryTests() {
    }

    @ServerTest("every block that names a loot table has one")
    private static Step lootTables() {
        return Step.run(0, () -> {
            MinecraftServer server = ServerTestRunner.server();
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.BLOCK, block -> {
                var key = block.getLootTable();
                if (key == BuiltInLootTables.EMPTY || DROP_NOTHING.contains(key.location().getPath())) return;
                if (server.reloadableRegistries().getLootTable(key) == LootTable.EMPTY)
                    wrong.add(key.location().toString());
            });
            check(wrong.isEmpty(), "missing loot tables " + wrong);
        });
    }

    @ServerTest("every recipe file decodes")
    private static Step recipeFormat() {
        return Step.run(0, () -> {
            MinecraftServer server = ServerTestRunner.server();
            var ops = server.registryAccess().createSerializationContext(JsonOps.INSTANCE);
            List<String> wrong = new ArrayList<>();
            Map<ResourceLocation, Resource> files = server.getResourceManager().listResources("recipe", path -> path.getPath().endsWith(".json"));
            int checked = 0;

            for (var file : files.entrySet()) {
                if (!file.getValue().sourcePackId().contains(ModConstants.MOD_ID))
                    continue;

                JsonObject json;
                try (Reader reader = file.getValue().openAsReader()) {
                    json = JsonParser.parseReader(reader).getAsJsonObject();
                } catch (Exception e) {
                    wrong.add(file.getKey() + ": " + e);
                    continue;
                }

                if (!loaded(json.get("type").getAsString()))
                    continue;

                json.remove("fabric:load_conditions");
                json.remove("neoforge:conditions");
                checked++;

                var result = Recipe.CODEC.parse(ops, withoutMissingMods(json));
                result.error().ifPresent(error -> wrong.add(file.getKey() + ": " + error.message()));
            }
            check(checked > 0, "found no recipe files from the mod");
            checkAll(wrong);
        });
    }

    private static boolean loaded(String id) {
        String namespace = ResourceLocation.parse(id.startsWith("#") ? id.substring(1) : id).getNamespace();
        return namespace.equals("minecraft") || namespace.equals("c") || Services.PLATFORM.isModLoaded(namespace);
    }

    private static JsonElement withoutMissingMods(JsonElement json) {
        if (json instanceof JsonObject object) {
            JsonObject copy = new JsonObject();
            object.entrySet().forEach(e -> copy.add(e.getKey(), withoutMissingMods(e.getValue())));
            return copy;
        }
        if (json instanceof JsonArray array) {
            JsonArray copy = new JsonArray();
            array.forEach(e -> copy.add(withoutMissingMods(e)));
            return copy;
        }
        if (json instanceof JsonPrimitive primitive && primitive.isString()) {
            String value = primitive.getAsString();
            if (value.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+") && !loaded(value)) {
                return new JsonPrimitive(value.startsWith("#") ? "#minecraft:planks" : "minecraft:stone");
            }
        }
        return json;
    }
}
