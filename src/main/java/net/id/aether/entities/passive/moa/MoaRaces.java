package net.id.aether.entities.passive.moa;

import com.mojang.datafixers.util.Function4;
import net.id.aether.Aether;
import net.id.aether.api.MoaAPI;
import net.id.aether.api.MoaAPI.MoaRace;
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

public class MoaRaces {
    private static Action<MoaRace> biome(RegistryKey<Biome> biome, int weight) { return (id, race) -> registerBiomeSpawnWeighting(biome, id, weight); }
    private static Action<MoaRace> breeding(MoaRace parentA, MoaRace parentB, Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> predicate) { return (id, race) -> registerBreedingPredicate(id, parentA, parentB, predicate); }
    private static Action<MoaRace> breeding(MoaRace parentA, MoaRace parentB, float chance){ return (id, race) -> registerBreedingChance(id, parentA, parentB, chance); }

    public static final MoaRace HIGHLANDS_BLUE = addRace("highlands_blue", MoaAttributes.GROUND_SPEED, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/blue.png", biome(HIGHLANDS_PLAINS, 50), biome(HIGHLANDS_FOREST, 50));
    public static final MoaRace GOLDENROD = addRace("goldenrod", MoaAttributes.JUMPING_STRENGTH, MoaAPI.SpawnStatWeighting.ENDURANCE, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/goldenrod.png", biome(HIGHLANDS_PLAINS, 10), biome(HIGHLANDS_FOREST, 35), biome(WISTERIA_WOODS, 49));
    public static final MoaRace MINTGRASS = addRace("mintgrass", MoaAttributes.GLIDING_SPEED, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/mintgrass.png", biome(HIGHLANDS_PLAINS, 40), biome(HIGHLANDS_THICKET, 45));
    public static final MoaRace STRAWBERRY_WISTAR = addRace("strawberry_wistar", MoaAttributes.GLIDING_DECAY, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/strawberry_wistar.png", biome(WISTERIA_WOODS, 49));
    public static final MoaRace TANGERINE = addRace("tangerine", MoaAttributes.JUMPING_STRENGTH, MoaAPI.SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/tangerine.png", biome(HIGHLANDS_FOREST, 15), biome(HIGHLANDS_THICKET, 50), breeding(GOLDENROD, STRAWBERRY_WISTAR, 0.5F));
    public static final MoaRace FOXTROT = addRace("foxtrot", MoaAttributes.DROP_MULTIPLIER, MoaAPI.SpawnStatWeighting.TANK, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/foxtrot.png", biome(HIGHLANDS_THICKET, 5), breeding(TANGERINE, GOLDENROD, 0.2F));
    public static final MoaRace SCARLET = addRace("scarlet", MoaAttributes.GLIDING_SPEED, MoaAPI.SpawnStatWeighting.ENDURANCE, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/scarlet.png", biome(WISTERIA_WOODS, 2), breeding(STRAWBERRY_WISTAR, HIGHLANDS_BLUE, 0.075F));
    public static final MoaRace REDHOOD = addRace("redhood", MoaAttributes.MAX_HEALTH, MoaAPI.SpawnStatWeighting.TANK, false, false, ParticleTypes.ENCHANT, "textures/entity/moa/highlands/redhood.png", breeding(FOXTROT, HIGHLANDS_BLUE, 0.1F));
    public static final MoaRace MOONSTRUCK = addRace("moonstruck", MoaAttributes.GLIDING_SPEED, MoaAPI.SpawnStatWeighting.SPEED, true, true, ParticleTypes.GLOW, "textures/entity/moa/highlands/moonstruck.png", breeding(REDHOOD, STRAWBERRY_WISTAR, ((moaGenes, moaGenes2, world, pos) -> world.isNight() && world.getRandom().nextFloat() <= 0.25F)));

    @SafeVarargs
    private static MoaRace addRace(String name, MoaAttributes affinity, MoaAPI.SpawnStatWeighting spawnStats, boolean glowing, boolean legendary, ParticleType<?> particles, String texturePath, Action<MoaRace>... additionalActions){
        MoaRace race = register(Aether.locate(name), affinity, spawnStats, glowing, legendary, particles, Aether.locate(texturePath));
        for(var action : additionalActions){
            action.accept(Aether.locate(name), race);
        }
        return race;
    }

    public static void init(){
        // empty method, just like my heart...
    }
}
