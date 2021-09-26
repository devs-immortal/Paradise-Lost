package net.id.aether.entities.passive.moa;

import com.mojang.datafixers.util.Function4;
import net.id.aether.Aether;
import net.id.aether.api.MoaAPI;
import net.id.aether.api.MoaAPI.Race;
import net.id.aether.component.MoaGenes;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import static net.id.aether.api.MoaAPI.*;
import static net.id.aether.world.dimension.AetherDimension.*;

public class Moas {
    private static Action<Race> biome(RegistryKey<Biome> biome, int weight) { return (id, race) -> registerBiomeSpawnWeighting(biome, id, weight); }
    private static Action<Race> breeding(Race parentA, Race parentB, Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> predicate) { return (id, race) -> registerBreedingPredicate(id, parentA, parentB, predicate); }
    private static Action<Race> breeding(Race parentA, Race parentB, float chance){ return (id, race) -> registerBreedingChance(id, parentA, parentB, chance); }

    private static final Race HIGHLANDS_BLUE = addMoa("highlands_blue", MoaAttributes.GROUND_SPEED, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/blue.png", biome(HIGHLANDS_PLAINS, 50), biome(HIGHLANDS_FOREST, 50));
    private static final Race GOLDENROD = addMoa("goldenrod", MoaAttributes.JUMPING_STRENGTH, MoaAPI.SpawnStatWeighting.ENDURANCE, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/goldenrod.png", biome(HIGHLANDS_PLAINS, 10), biome(HIGHLANDS_FOREST, 35), biome(WISTERIA_WOODS, 49));
    private static final Race MINTGRASS = addMoa("mintgrass", MoaAttributes.GLIDING_SPEED, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/mintgrass.png", biome(HIGHLANDS_PLAINS, 40), biome(HIGHLANDS_THICKET, 45));
    private static final Race STRAWBERRY_WISTAR = addMoa("strawberry_wistar", MoaAttributes.GLIDING_DECAY, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/strawberry_wistar.png", biome(WISTERIA_WOODS, 49));
    private static final Race TANGERINE = addMoa("tangerine", MoaAttributes.JUMPING_STRENGTH, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/tangerine.png", biome(HIGHLANDS_FOREST, 15), biome(HIGHLANDS_THICKET, 50), breeding(GOLDENROD, STRAWBERRY_WISTAR, 0.5F));
    private static final Race FOXTROT = addMoa("foxtrot", MoaAttributes.DROP_MULTIPLIER, MoaAPI.SpawnStatWeighting.TANK, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/foxtrot.png", biome(HIGHLANDS_THICKET, 5), breeding(TANGERINE, GOLDENROD, 0.2F));
    private static final Race SCARLET = addMoa("scarlet", MoaAttributes.GLIDING_SPEED, MoaAPI.SpawnStatWeighting.ENDURANCE, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/scarlet.png", biome(WISTERIA_WOODS, 2), breeding(STRAWBERRY_WISTAR, HIGHLANDS_BLUE, 0.075F));
    private static final Race REDHOOD = addMoa("redhood", MoaAttributes.MAX_HEALTH, MoaAPI.SpawnStatWeighting.TANK, false, false, ParticleTypes.ENCHANT, "textures/entity/moas/highlands/redhood.png", breeding(FOXTROT, HIGHLANDS_BLUE, 0.1F));
    private static final Race MOONSTRUCK = addMoa("moonstruck", MoaAttributes.GLIDING_SPEED, MoaAPI.SpawnStatWeighting.SPEED, true, true, ParticleTypes.GLOW, "textures/entity/moas/highlands/moonstruck.png", breeding(REDHOOD, STRAWBERRY_WISTAR, ((moaGenes, moaGenes2, world, pos) -> world.isNight() && world.getRandom().nextFloat() <= 0.25F)));

    @SafeVarargs
    private static Race addMoa(String name, MoaAttributes affinity, MoaAPI.SpawnStatWeighting spawnStats, boolean glowing, boolean legendary, ParticleType<?> particles, String texturePath, Action<Race>... additionalActions){
        Race race = register(Aether.locate(name), affinity, spawnStats, glowing, legendary, particles, Aether.locate(texturePath));
        for(var action : additionalActions){
            action.accept(Aether.locate(name), race);
        }
        return race;
    }

    public static void init(){
        // empty method, just like my heart...
    }
}
