package net.id.paradiselost.entities.passive.moa;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.api.MoaAPI;
import net.minecraft.particle.ParticleTypes;

import java.util.function.Predicate;

import static net.id.paradiselost.api.MoaAPI.*;
import static net.id.paradiselost.api.MoaAPI.SpawnStatWeighting.*;
import static net.id.paradiselost.entities.passive.moa.MoaAttributes.*;
import static net.id.paradiselost.world.dimension.ParadiseLostBiomes.*;

/**
 * An example class demonstrating how to use the Moa API.
 * I say demonstration, but this is actually how we use it. It's more like a lead-by-example.
 * <br> If you're viewing this in the javadocs preview, you're going to want to pull up
 * the actual code to get a look at things.
 * @author Jack Papel
 */
public class MoaRaces {
    public static final MoaRace HIGHLANDS_BLUE = register("highlands_blue", new MoaRace(GROUND_SPEED, SPEED));
    public static final MoaRace GOLDENROD = register("goldenrod", new MoaRace(JUMPING_STRENGTH, ENDURANCE));
    public static final MoaRace MINTGRASS = register("mintgrass", new MoaRace(GLIDING_SPEED, SPEED));
    public static final MoaRace STRAWBERRY_WISTAR = register("strawberry_wistar", new MoaRace(GLIDING_DECAY, SPEED));
    public static final MoaRace TANGERINE = register("tangerine", new MoaRace(JUMPING_STRENGTH, SPEED));
    public static final MoaRace FOXTROT = register("foxtrot", new MoaRace(DROP_MULTIPLIER, TANK));
    public static final MoaRace SCARLET = register("scarlet", new MoaRace(GLIDING_SPEED, ENDURANCE));
    public static final MoaRace REDHOOD = register("redhood", new MoaRace(MAX_HEALTH, TANK));
    public static final MoaRace MOONSTRUCK = register("moonstruck", new MoaRace(GLIDING_SPEED, SPEED, true, true, ParticleTypes.GLOW));
    
    private static MoaRace register(String name, MoaRace race){
        return MoaAPI.register(ParadiseLost.locate(name), race);
    }
    
    // Register breeding and spawning.
    public static void init(){
        registerSpawning(HIGHLANDS_BLUE, 50, HIGHLANDS_PLAINS_KEY);
        registerSpawning(HIGHLANDS_BLUE, 50, HIGHLANDS_FOREST_KEY);
    
        registerSpawning(GOLDENROD, 10, HIGHLANDS_PLAINS_KEY);
        registerSpawning(GOLDENROD, 25, HIGHLANDS_FOREST_KEY);
        registerSpawning(GOLDENROD, 49, WISTERIA_WOODS_KEY);
    
        registerSpawning(MINTGRASS, 40, HIGHLANDS_PLAINS_KEY);
        registerSpawning(MINTGRASS, 45, HIGHLANDS_THICKET_KEY);
    
        registerSpawning(STRAWBERRY_WISTAR, 49, WISTERIA_WOODS_KEY);
    
        registerSpawning(TANGERINE, 15, HIGHLANDS_FOREST_KEY);
        registerSpawning(TANGERINE, 50, HIGHLANDS_THICKET_KEY);
        registerBreeding(TANGERINE, 0.5F, GOLDENROD, STRAWBERRY_WISTAR);
    
        registerSpawning(FOXTROT, 5, HIGHLANDS_THICKET_KEY);
        registerBreeding(FOXTROT, 0.2F, TANGERINE, GOLDENROD);
    
        registerSpawning(SCARLET, 2, WISTERIA_WOODS_KEY);
        registerBreeding(SCARLET, 0.075F, STRAWBERRY_WISTAR, HIGHLANDS_BLUE);
    
        registerSpawning(REDHOOD, 5, HIGHLANDS_THICKET_KEY);
        registerBreeding(REDHOOD, 0.1F, FOXTROT, HIGHLANDS_BLUE);
    
        Predicate<MoaBreedingContext> moonstruckRequirements = ctx -> ctx.world().isNight() && ctx.world().getRandom().nextFloat() <= 0.25F;
    
        registerSpawning(MOONSTRUCK, 5, HIGHLANDS_THICKET_KEY);
        registerBreeding(MOONSTRUCK, moonstruckRequirements, REDHOOD, STRAWBERRY_WISTAR);
    }
}
