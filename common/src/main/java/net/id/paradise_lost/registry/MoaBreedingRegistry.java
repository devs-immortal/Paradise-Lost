package net.id.paradise_lost.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.api.MoaAPI.MoaBreedingContext;
import net.id.paradise_lost.api.MoaAPI.MoaRace;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.registration.registries.DatapackRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class MoaBreedingRegistry {

    public static final DatapackRegistry<BreedingRecipes> REGISTRY =
            DatapackRegistry.<BreedingRecipes>builder(ResourceLocation.fromNamespaceAndPath(ModConstants.MODID, "moa_breeding"))
                    .withElementCodec(BreedingRecipes.CODEC)
                    .withNetworkCodec(BreedingRecipes.CODEC)
                    .withBootstrap(MoaBreedingRegistry::bootstrap)
                    .build();

    private static final Set<ResourceLocation> WARNED_MISSING_RACES = ConcurrentHashMap.newKeySet();

    public static void init() {
    }

    private static void bootstrap(BootstrapContext<BreedingRecipes> context) {
        table(context, MoaRaceRegistry.TANGERINE,
                pair(MoaRaceRegistry.GOLDENROD, MoaRaceRegistry.STRAWBERRY_WISTAR, 0.5F));
        table(context, MoaRaceRegistry.FOXTROT,
                pair(MoaRaceRegistry.TANGERINE, MoaRaceRegistry.GOLDENROD, 0.2F));
        table(context, MoaRaceRegistry.SCARLET,
                pair(MoaRaceRegistry.STRAWBERRY_WISTAR, MoaRaceRegistry.HIGHLANDS_BLUE, 0.15F));
        table(context, MoaRaceRegistry.REDHOOD,
                pair(MoaRaceRegistry.FOXTROT, MoaRaceRegistry.HIGHLANDS_BLUE, 0.2F));
        table(context, MoaRaceRegistry.GREENSEED,
                pair(MoaRaceRegistry.SCARLET, MoaRaceRegistry.MINTGRASS, 0.33F));
        table(context, MoaRaceRegistry.AQUILAN,
                pair(MoaRaceRegistry.GREYHOUND, MoaRaceRegistry.FROSTGRASS, 0.2F));
        table(context, MoaRaceRegistry.MOONSTRUCK,
                pair(MoaRaceRegistry.REDHOOD, MoaRaceRegistry.STRAWBERRY_WISTAR, 0.25F, night()));
    }

    private static void table(BootstrapContext<BreedingRecipes> context,
                              RegistryObject<MoaRace, MoaRace> child,
                              BreedingEntry... recipes) {
        context.register(ResourceKey.create(REGISTRY.key(), child.getId()),
                new BreedingRecipes(List.of(recipes)));
    }

    private static BreedingEntry pair(RegistryObject<MoaRace, MoaRace> first,
                                      RegistryObject<MoaRace, MoaRace> second,
                                      float chance, LootItemCondition... conditions) {
        return new BreedingEntry(first.getId(), second.getId(), chance, List.of(conditions));
    }

    private static LootItemCondition night() {
        return new TimeCheck.Builder(IntRange.range(13_000, 23_000))
                .setPeriod(24_000L)
                .build();
    }

    public static MoaRace getMoaFromBreeding(MoaBreedingContext ctx) {
        Registry<BreedingRecipes> rules = REGISTRY.get(ctx.world().registryAccess());
        MoaRace parentA = ctx.parentA().getRace();
        MoaRace parentB = ctx.parentB().getRace();
        ResourceLocation idA = parentA.getId();
        ResourceLocation idB = parentB.getId();

        LootContext lootContext = null;
        for (IndexedRecipe recipe : index(rules)) {
            if (!recipe.matches(idA, idB)) {
                continue;
            }
            if (!recipe.entry().conditions().isEmpty()) {
                if (lootContext == null) {
                    lootContext = newLootContext(ctx);
                }
                if (lootContext == null || !recipe.entry().conditionsHold(lootContext)) {
                    continue;
                }
            }
            if (ctx.world().getRandom().nextFloat() >= recipe.entry().chance()) {
                continue;
            }
            return recipe.child();
        }

        if (parentA == MoaRaceRegistry.FALLBACK_MOA.get()) {
            return parentB;
        }
        if (parentB == MoaRaceRegistry.FALLBACK_MOA.get()) {
            return parentA;
        }
        return ctx.world().getRandom().nextBoolean() ? parentA : parentB;
    }

    private static List<IndexedRecipe> index(Registry<BreedingRecipes> rules) {
        List<IndexedRecipe> indexed = new ArrayList<>();
        for (var entry : rules.entrySet()) {
            MoaRace child = resolve(entry.getKey().location());
            if (child == null) {
                continue;
            }
            for (BreedingEntry recipe : entry.getValue().recipes()) {
                indexed.add(new IndexedRecipe(child, recipe));
            }
        }
        return indexed;
    }

    private static LootContext newLootContext(MoaBreedingContext ctx) {
        if (!(ctx.world() instanceof ServerLevel serverLevel)) {
            return null;
        }
        LootParams params = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(ctx.pos()))
                .create(LootContextParamSets.EMPTY);
        return new LootContext.Builder(params).create(Optional.empty());
    }

    private static MoaRace resolve(ResourceLocation raceId) {
        var race = MoaRaceRegistry.MOA_RACES.getOptional(raceId);
        if (race.isEmpty()) {
            if (WARNED_MISSING_RACES.add(raceId)) {
                ParadiseLost.LOG.error("moa_breeding refers to {} which is not a registered moa race; ignoring it", raceId);
            }
            return null;
        }
        return race.get();
    }

    private record IndexedRecipe(MoaRace child, BreedingEntry entry) {
        boolean matches(ResourceLocation parentA, ResourceLocation parentB) {
            return (entry.parent1().equals(parentA) && entry.parent2().equals(parentB))
                    || (entry.parent1().equals(parentB) && entry.parent2().equals(parentA));
        }
    }

    public record BreedingRecipes(List<BreedingEntry> recipes) {
        public static final Codec<BreedingRecipes> CODEC =
                BreedingEntry.CODEC.listOf().xmap(BreedingRecipes::new, BreedingRecipes::recipes);
    }

    public record BreedingEntry(ResourceLocation parent1, ResourceLocation parent2,
                                float chance, List<LootItemCondition> conditions) {

        public static final Codec<BreedingEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("parent1").forGetter(BreedingEntry::parent1),
                ResourceLocation.CODEC.fieldOf("parent2").forGetter(BreedingEntry::parent2),
                Codec.floatRange(0F, 1F).fieldOf("chance").forGetter(BreedingEntry::chance),
                LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of())
                        .forGetter(BreedingEntry::conditions)
        ).apply(instance, BreedingEntry::new));

        boolean conditionsHold(LootContext context) {
            for (LootItemCondition condition : conditions) {
                if (!condition.test(context)) {
                    return false;
                }
            }
            return true;
        }
    }
}
