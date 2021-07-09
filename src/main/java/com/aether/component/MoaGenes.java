package com.aether.component;

import com.aether.api.MoaAPI;
import com.aether.api.MoaAttributes;
import com.aether.entities.passive.MoaEntity;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class MoaGenes implements AutoSyncedComponent {

    private final Object2FloatOpenHashMap<MoaAttributes> attributeMap = new Object2FloatOpenHashMap<>();
    private MoaAPI.Race race = MoaAPI.FALLBACK_MOA;
    private MoaAttributes affinity;
    private boolean legendary, initialized;

    public MoaGenes() {}

    public void initMoa(@NotNull MoaEntity moa) {
        World world = moa.world;
        Random random = moa.getRandom();
        race = MoaAPI.getMoaForBiome(world.getBiomeKey(moa.getBlockPos()).get(), random);
        affinity = race.defaultAffinity();

        for (MoaAttributes attribute : MoaAttributes.values()) {
            attributeMap.addTo(attribute, race.statWeighting().configure(attribute, race, random));
        }
        initialized = true;
    }

    public float getAttribute(MoaAttributes attribute) {
        return attributeMap.getOrDefault(attribute, attribute.min);
    }

    public Identifier getTexture() {
        return race.texturePath();
    }

    public static MoaGenes get(@NotNull MoaEntity moa) {
        return AetherComponents.MOA_GENETICS_KEY.get(moa);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        initialized = tag.getBoolean("initialized");
        if(initialized) {
            race = MoaAPI.getRace(Identifier.tryParse(tag.getString("raceId")));
            affinity = MoaAttributes.valueOf(tag.getString("affinity"));
            legendary = tag.getBoolean("legendary");
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> attributeMap.put(attribute, tag.getFloat(attribute.name())));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("initialized", initialized);
        if(initialized) {
            tag.putString("raceId", race.id().toString());
            tag.putString("affinity", affinity.name());
            tag.putBoolean("legendary", legendary);
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> tag.putFloat(attribute.name(), attributeMap.getFloat(attribute)));
        }
    }
}
