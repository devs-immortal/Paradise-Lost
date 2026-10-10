package net.id.paradise_lost.clienttest.tests;

import net.id.paradise_lost.block.blockentity.ParadiseHangingSignBlockEntity;
import net.id.paradise_lost.clienttest.Step;
import net.id.paradise_lost.clienttest.Test;
import net.id.paradise_lost.clienttest.mixin.HangingSignEditScreenAccessor;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static net.id.paradise_lost.clienttest.TestHelpers.*;

public final class RegistryTests {
    private static final String GROUP = "Registry";
    private static final String MOD = "paradise_lost";
    private static final Set<String> DROP_NOTHING = Set.of("blocks/surtrum_air", "blocks/blue_portal", "blocks/suspicious_dirt");

    private RegistryTests() {
    }

    public static List<Test> all() {
        return List.of(
                blockModels(),
                itemModels(),
                signTextures(),
                hangingSignScreenTextures(),
                langEntries(),
                sounds(),
                particles(),
                lootTables()
        );
    }

    private static <T> void forEachOurs(Registry<T> registry, Consumer<T> action) {
        for (T entry : registry) {
            if (registry.getKey(entry).getNamespace().equals(MOD)) action.accept(entry);
        }
    }

    private static String missingTexture(BakedModel model, BlockState state) {
        if (isMissing(model.getParticleIcon().contents().name()))
            return "missing particle texture";

        List<BakedQuad> quads = new ArrayList<>(model.getQuads(state, null, RandomSource.create(42)));
        for (Direction direction : Direction.values()) {
            quads.addAll(model.getQuads(state, direction, RandomSource.create(42)));
        }

        long missing = quads.stream().filter(quad -> isMissing(quad.getSprite().contents().name())).count();
        return missing > 0 ? missing + " quads with the missing texture" : null;
    }

    private static boolean isMissing(ResourceLocation texture) {
        return texture.equals(MissingTextureAtlasSprite.getLocation());
    }

    private static Test blockModels() {
        return new Test(GROUP, "every block state has model and textures", Step.run(0, () -> {
            BakedModel missingModel = client().getModelManager().getMissingModel();
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.BLOCK, block -> {
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    if (state.getRenderShape() == RenderShape.INVISIBLE)
                        continue;

                    BakedModel model = client().getBlockRenderer().getBlockModel(state);
                    String problem = model == missingModel ? "missing model" : missingTexture(model, state);
                    if (problem != null) {
                        wrong.add(state + ": " + problem);
                        break;
                    }
                }
            });
            checkAll(wrong);
        }));
    }

    private static Test itemModels() {
        return new Test(GROUP, "every item has a model and textures", Step.run(0, () -> {
            BakedModel missingModel = client().getModelManager().getMissingModel();
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.ITEM, item -> {
                BakedModel model = client().getItemRenderer().getModel(new ItemStack(item), client().level, null, 0);
                String problem = model == missingModel
                        ? "missing model"
                        : model.isCustomRenderer() ? null : missingTexture(model, null);
                if (problem != null) wrong.add(BuiltInRegistries.ITEM.getKey(item) + ": " + problem);
            });

            ModelResourceLocation spyglass = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(MOD, "olvite_spyglass"));
            if (client().getModelManager().getModel(spyglass) == missingModel)
                wrong.add(spyglass + " extra model was never loaded");
            checkAll(wrong);
        }));
    }

    private static Test signTextures() {
        return new Test(GROUP, "every sign wood type has sign and hanging sign textures", Step.run(0, () -> {
            Set<WoodType> woodTypes = new LinkedHashSet<>();
            forEachOurs(BuiltInRegistries.BLOCK, block -> {
                if (block instanceof SignBlock) woodTypes.add(SignBlock.getWoodType(block));
            });
            check(!woodTypes.isEmpty(), "found no Paradise Lost signs");

            List<String> wrong = new ArrayList<>();
            for (WoodType woodType : woodTypes) {
                expect(wrong, !isMissing(Sheets.getSignMaterial(woodType).sprite().contents().name()), woodType.name() + " sign");
                expect(wrong, !isMissing(Sheets.getHangingSignMaterial(woodType).sprite().contents().name()), woodType.name() + " hanging sign");
            }
            check(wrong.isEmpty(), "missing texture for " + wrong);
        }));
    }

    private static Test hangingSignScreenTextures() {
        return new Test(GROUP, "every hanging sign edit screen has texture", Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.BLOCK, block -> {
                if (!(block instanceof CeilingHangingSignBlock) && !(block instanceof WallHangingSignBlock))
                    return;

                String name = BuiltInRegistries.BLOCK.getKey(block).toString();
                try {
                    var sign = new ParadiseHangingSignBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                    var screen = new HangingSignEditScreen(sign, true, false);
                    ResourceLocation texture = ((HangingSignEditScreenAccessor) screen).clienttest$texture();
                    expect(wrong, client().getResourceManager().getResource(texture).isPresent(), name + " (" + texture + ")");
                } catch (Exception e) {
                    wrong.add(name + " threw " + e);
                }
            });
            check(wrong.isEmpty(), "no edit screen texture for " + wrong);
        }));
    }

    private static Test langEntries() {
        return new Test(GROUP, "every block, item, entity and biome has a name", Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.BLOCK, block -> expect(wrong, I18n.exists(block.getDescriptionId()), block.getDescriptionId()));
            forEachOurs(BuiltInRegistries.ITEM, item -> expect(wrong, I18n.exists(item.getDescriptionId()), item.getDescriptionId()));
            forEachOurs(BuiltInRegistries.ENTITY_TYPE, type -> expect(wrong, I18n.exists(type.getDescriptionId()), type.getDescriptionId()));
            client().level.registryAccess().lookupOrThrow(Registries.BIOME).listElementIds()
                    .filter(key -> key.location().getNamespace().equals(MOD))
                    .map(key -> Util.makeDescriptionId("biome", key.location()))
                    .forEach(key -> expect(wrong, I18n.exists(key), key));
            check(wrong.isEmpty(), "no lang entry for " + wrong);
        }));
    }

    private static Test sounds() {
        return new Test(GROUP, "every sound event is in sounds.json", Step.run(0, () -> {
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.SOUND_EVENT, sound ->
                    expect(wrong, client().getSoundManager().getSoundEvent(sound.getLocation()) != null, sound.getLocation().toString()));
            check(wrong.isEmpty(), "no sounds.json entry for " + wrong);
        }));
    }

    private static Test particles() {
        return new Test(GROUP, "every simple particle type has a provider", Step.run(0, () -> {
            Vec3 at = client().player.position();
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.PARTICLE_TYPE, type -> {
                if (type instanceof SimpleParticleType simple) {
                    expect(wrong, client().particleEngine.createParticle(simple, at.x, at.y + 1, at.z, 0, 0, 0) != null,
                            BuiltInRegistries.PARTICLE_TYPE.getKey(type).toString());
                }
            });
            check(wrong.isEmpty(), "no particle provider for " + wrong);
        }));
    }

    private static Test lootTables() {
        return new Test(GROUP, "every block that names a loot table has one", Step.run(0, () -> onServer(server -> {
            List<String> wrong = new ArrayList<>();
            forEachOurs(BuiltInRegistries.BLOCK, block -> {
                var key = block.getLootTable();
                if (key == BuiltInLootTables.EMPTY || DROP_NOTHING.contains(key.location().getPath())) return;
                if (server.reloadableRegistries().getLootTable(key) == LootTable.EMPTY)
                    wrong.add(key.location().toString());
            });
            check(wrong.isEmpty(), "missing loot tables " + wrong);
        })));
    }
}
