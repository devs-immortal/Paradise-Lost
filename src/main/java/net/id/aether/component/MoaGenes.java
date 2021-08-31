package net.id.aether.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.id.aether.api.MoaAPI;
import net.id.aether.api.MoaAttributes;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.passive.MoaEntity;
import net.id.aether.items.AetherItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class MoaGenes implements AutoSyncedComponent {

    private final Object2FloatOpenHashMap<MoaAttributes> attributeMap = new Object2FloatOpenHashMap<>();
    private MoaAPI.Race race = MoaAPI.FALLBACK_MOA;
    private MoaAttributes affinity;
    private boolean legendary, initialized;
    private UUID owner;
    private float hunger = 100F;

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

    public ItemStack getEggForBreeding(MoaGenes otherParent, World world, BlockPos pos) {
        var childRace = MoaAPI.getMoaForBreeding(this, otherParent, world, pos);

        ItemStack stack = new ItemStack(AetherItems.MOA_EGG);
        NbtCompound nbt = stack.getOrCreateSubNbt("genes");
        Random random = world.getRandom();
        MoaGenes genes = new MoaGenes();

        float increaseChance = 1F;
        for (MoaAttributes attribute : MoaAttributes.values()) {
            boolean increase = random.nextFloat() <= increaseChance;
            genes.attributeMap.addTo(attribute, attribute.fromBreeding(this, otherParent, increase));
            if(increase) {
                increaseChance /= 2;
            }
        }
        genes.race = childRace;
        genes.affinity = random.nextBoolean() ? this.affinity : otherParent.affinity;
        genes.owner = random.nextBoolean() ? this.owner : otherParent.owner;
        genes.initialized = true;

        genes.writeToNbt(nbt);
        nbt.putBoolean("baby", true);
        return stack;
    }

    public static ItemStack getEggForCommand(MoaAPI.Race race, World world, boolean baby) {
        ItemStack stack = new ItemStack(AetherItems.MOA_EGG);
        NbtCompound nbt = stack.getOrCreateSubNbt("genes");
        Random random = world.getRandom();
        MoaGenes genes = new MoaGenes();

        for (MoaAttributes attribute : MoaAttributes.values()) {
            genes.attributeMap.addTo(attribute, race.statWeighting().configure(attribute, race, random));
        }
        genes.race = race;
        genes.affinity = race.defaultAffinity();
        genes.initialized = true;

        genes.writeToNbt(nbt);
        nbt.putBoolean("baby", baby);
        return stack;
    }

    public static MoaEntity getMoaFromEgg(World world, ItemStack stack) {
        MoaEntity moa = AetherEntityTypes.MOA.create(world);
        MoaGenes genes = moa.getGenes();
        if(stack.isOf(AetherItems.MOA_EGG)) {
            genes.readFromNbt(stack.getOrCreateSubNbt("genes"));
        }
        moa.setBreedingAge(-43200);
        return moa;
    }

    public float getAttribute(MoaAttributes attribute) {
        return attributeMap.getOrDefault(attribute, attribute.min);
    }

    public void setAttribute(MoaAttributes attribute, float value) {
        attributeMap.put(attribute, value);
    }

    public MoaAttributes getAffinity() {
        return affinity;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public MoaAPI.Race getRace() {
        return race;
    }

    public Identifier getTexture() {
        return race.texturePath();
    }

    public float getHunger() {
        return hunger;
    }

    public void setHunger(float hunger) {
        this.hunger = Math.max(Math.min(hunger, 100), 0);
    }

    public boolean isTamed() {
        return owner != null;
    }

    public void tame(UUID newOwner) {
        this.owner = newOwner;
    }

    public UUID getOwner() {
        return owner;
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
            hunger = tag.getFloat("hunger");
            if(tag.getBoolean("tamed")) {
                owner = tag.getUuid("owner");
            }
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
            tag.putFloat("hunger", hunger);
            tag.putBoolean("tamed", isTamed());
            if(isTamed()) {
                tag.putUuid("owner", owner);
            }
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> tag.putFloat(attribute.name(), attributeMap.getFloat(attribute)));
        }
    }
}
