package net.id.aether.entities.passive.moa;

import com.mojang.datafixers.util.Function4;
import net.id.aether.Aether;
import net.id.aether.api.MoaAPI.MoaRace;
import net.id.aether.component.MoaGenes;
import net.id.incubus_core.util.RegistryQueue.Action;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import static net.id.aether.api.MoaAPI.*;
import static net.id.aether.api.MoaAPI.SpawnStatWeighting.*;
import static net.id.aether.entities.passive.moa.MoaAttributes.*;
import static net.id.aether.world.dimension.AetherDimension.*;

public class MoaRaces {
    private static Action<MoaRace> biome(RegistryKey<Biome> biome, int weight) { return (id, race) -> registerBiomeSpawnWeighting(biome, race, weight); }
    private static Action<MoaRace> breeding(MoaRace parentA, MoaRace parentB, Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> predicate) { return (id, race) -> registerBreedingPredicate(race, parentA, parentB, predicate); }
    private static Action<MoaRace> breeding(MoaRace parentA, MoaRace parentB, float chance){ return (id, race) -> registerBreedingChance(race, parentA, parentB, chance); }

    public static final MoaRace HIGHLANDS_BLUE = addRace("highlands_blue", new MoaRace(GROUND_SPEED, SPEED), biome(HIGHLANDS_PLAINS, 50), biome(HIGHLANDS_FOREST, 50));
    public static final MoaRace GOLDENROD = addRace("goldenrod", new MoaRace(JUMPING_STRENGTH, ENDURANCE), biome(HIGHLANDS_PLAINS, 10), biome(HIGHLANDS_FOREST, 35), biome(WISTERIA_WOODS, 49));
    public static final MoaRace MINTGRASS = addRace("mintgrass", new MoaRace(GLIDING_SPEED, SPEED), biome(HIGHLANDS_PLAINS, 40), biome(HIGHLANDS_THICKET, 45));
    public static final MoaRace STRAWBERRY_WISTAR = addRace("strawberry_wistar", new MoaRace(GLIDING_DECAY, SPEED), biome(WISTERIA_WOODS, 49));
    public static final MoaRace TANGERINE = addRace("tangerine", new MoaRace(JUMPING_STRENGTH, SPEED), biome(HIGHLANDS_FOREST, 15), biome(HIGHLANDS_THICKET, 50), breeding(GOLDENROD, STRAWBERRY_WISTAR, 0.5F));
    public static final MoaRace FOXTROT = addRace("foxtrot", new MoaRace(DROP_MULTIPLIER, TANK), biome(HIGHLANDS_THICKET, 5), breeding(TANGERINE, GOLDENROD, 0.2F));
    public static final MoaRace SCARLET = addRace("scarlet", new MoaRace(GLIDING_SPEED, ENDURANCE), biome(WISTERIA_WOODS, 2), breeding(STRAWBERRY_WISTAR, HIGHLANDS_BLUE, 0.075F));
    public static final MoaRace REDHOOD = addRace("redhood", new MoaRace(MAX_HEALTH, TANK), breeding(FOXTROT, HIGHLANDS_BLUE, 0.1F));
    public static final MoaRace MOONSTRUCK = addRace("moonstruck", new MoaRace(GLIDING_SPEED, SPEED, true, true, ParticleTypes.GLOW), breeding(REDHOOD, STRAWBERRY_WISTAR, ((moaGenes, moaGenes2, world, pos) -> world.isNight() && world.getRandom().nextFloat() <= 0.25F)));

    @SafeVarargs
    private static MoaRace addRace(String name, MoaRace race, Action<MoaRace>... additionalActions){
        for(var action : additionalActions){
            action.accept(Aether.locate(name), race);
        }
        return register(Aether.locate(name), race);
    }

    public static void init(){
        // empty method, just like my heart...
    }
}
