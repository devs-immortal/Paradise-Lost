package net.id.paradise_lost.component;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import net.id.paradise_lost.api.MoaAPI;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.nbt.Tag;

public class MoaGenes {
    private final Object2FloatOpenHashMap<MoaAttributes> attributeMap = new Object2FloatOpenHashMap<>();
    private MoaAPI.MoaRace race = MoaAPI.getRace(null);
    private MoaAttributes affinity;
    private boolean legendary, initialized;
    private UUID owner;
    private float hunger = 100F;

    public MoaGenes() {
    }

    public static ItemStack getEggForCommand(MoaAPI.MoaRace race, Level world, boolean baby) {
        RandomSource random = world.getRandom();
        ItemStack stack = new ItemStack(ItemRegistry.MOA_EGG.get());

        float attr1 = race.statWeighting().configure(MoaAttributes.GROUND_SPEED, race, random);
        float attr2 = race.statWeighting().configure(MoaAttributes.GLIDING_SPEED, race, random);
        float attr3 = race.statWeighting().configure(MoaAttributes.GLIDING_DECAY, race, random);
        float attr4 = race.statWeighting().configure(MoaAttributes.JUMPING_STRENGTH, race, random);
        float attr5 = race.statWeighting().configure(MoaAttributes.DROP_MULTIPLIER, race, random);
        float attr6 = race.statWeighting().configure(MoaAttributes.MAX_HEALTH, race, random);

        var attributes = new ParadiseLostDataComponentTypes.MoaAttributeComponent(attr1, attr2, attr3, attr4, attr5, attr6);
        var genes = new ParadiseLostDataComponentTypes.MoaGeneComponent(race.getId(), race.defaultAffinity().name(), baby, 100.0F, UUID.fromString("00000000-0000-0000-0000-000000000000"), attributes);

        stack.set(ParadiseLostDataComponentTypes.MOA_GENES, genes);
        return stack;
    }

    public static MoaEntity getMoaFromEgg(Level world, ItemStack stack, UUID owner, BlockPos pos) {
        MoaEntity moa = EntityRegistry.MOA.get().create(world);
        moa.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, moa.getYRot(), moa.getXRot());
        MoaGenes genes = moa.getGenes();
        if (stack.is(ItemRegistry.MOA_EGG.get())) {
            var component = stack.get(ParadiseLostDataComponentTypes.MOA_GENES);
            if (component != null) {
                genes.fromComponent(component);
            } else {

                genes.initMoa(moa);
            }
            genes.owner = owner == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : owner;
        }
        moa.setAge(-43200);

        moa.setHealth(genes.getAttribute(MoaAttributes.MAX_HEALTH));

        moa.syncGenes();
        return moa;
    }

    public static MoaGenes get(@NotNull MoaEntity moa) {
        return moa.getGenes();
    }

    public void initMoa(@NotNull MoaEntity moa) {
        Level world = moa.level();
        RandomSource random = moa.getRandom();
        race = MoaAPI.getMoaFromSpawning(world, world.getBiome(moa.blockPosition()).unwrapKey().get(), random);
        affinity = race.defaultAffinity();

        for (MoaAttributes attribute : MoaAttributes.values()) {
            attributeMap.addTo(attribute, race.statWeighting().configure(attribute, race, random));
        }
        initialized = true;
    }

    public ItemStack getEggForBreeding(MoaGenes otherParent, Level world, BlockPos pos) {
        var childRace = MoaAPI.getMoaFromBreeding(this, otherParent, world, pos);

        ItemStack stack = new ItemStack(ItemRegistry.MOA_EGG.get());
        RandomSource random = world.getRandom();
        MoaGenes genes = new MoaGenes();

        float increaseChance = 1F;
        for (MoaAttributes attribute : MoaAttributes.values()) {
            boolean increase = random.nextFloat() <= increaseChance;
            genes.attributeMap.addTo(attribute, attribute.fromBreeding(this, otherParent, increase));
            if (increase) {
                increaseChance /= 2;
            }
        }
        genes.race = childRace;
        genes.affinity = random.nextBoolean() ? this.affinity : otherParent.affinity;
        genes.owner = random.nextBoolean() ? this.owner : otherParent.owner;
        genes.initialized = true;

        var com = genes.intoComponent();
        stack.set(ParadiseLostDataComponentTypes.MOA_GENES, com);
        return stack;
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

    public MoaAPI.MoaRace getRace() {
        return race;
    }

    public ResourceLocation getTexture() {
        ResourceLocation id = this.race.getId();
        String name = id.getPath();
        String namespace = id.getNamespace();
        return ResourceLocation.fromNamespaceAndPath(namespace, "textures/entity/moa/" + name + ".png");
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

    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        initialized = tag.getBoolean("initialized");
        if (initialized) {
            race = MoaAPI.getRace(ResourceLocation.tryParse(tag.getString("raceId")));
            String affinityStr = tag.getString("affinity");
            affinity = affinityStr.isEmpty() ? race.defaultAffinity() : MoaAttributes.valueOf(affinityStr);
            legendary = tag.getBoolean("legendary");
            if (tag.contains("hunger", Tag.TAG_FLOAT)) {
                hunger = tag.getFloat("hunger");
            }
            if (tag.getBoolean("tamed")) {
                owner = tag.getUUID("owner");
            }
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> {
                if (tag.contains(attribute.name(), Tag.TAG_FLOAT)) {
                    attributeMap.put(attribute, tag.getFloat(attribute.name()));
                } else {

                    if (attribute.equals(MoaAttributes.JUMPING_STRENGTH)) {
                        attributeMap.put(attribute, attribute.max);
                    } else {
                        attributeMap.put(attribute, attribute.min);
                    }
                }
            });
        }
    }

    public void fromComponent(ParadiseLostDataComponentTypes.MoaGeneComponent com) {
        initialized = true;
        race = MoaAPI.getRace(com.race());
        affinity = com.affinity().isEmpty() ? race.defaultAffinity() : MoaAttributes.valueOf(com.affinity());
        legendary = race.legendary();
        hunger = com.hunger();
        if (!com.ownerId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) owner = com.ownerId();
        attributeMap.put(MoaAttributes.GROUND_SPEED, com.attributes().groundSpeed());
        attributeMap.put(MoaAttributes.GLIDING_SPEED, com.attributes().glidingSpeed());
        attributeMap.put(MoaAttributes.GLIDING_DECAY, com.attributes().glidingDecay());
        attributeMap.put(MoaAttributes.JUMPING_STRENGTH, com.attributes().jumpStrength());
        attributeMap.put(MoaAttributes.DROP_MULTIPLIER, com.attributes().dropMultiplier());
        attributeMap.put(MoaAttributes.MAX_HEALTH, com.attributes().maxHealth());
    }

    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        tag.putBoolean("initialized", initialized);
        if (initialized) {
            tag.putString("raceId", race.getId().toString());
            tag.putString("affinity", affinity.name());
            tag.putBoolean("legendary", legendary);
            tag.putFloat("hunger", hunger);
            tag.putBoolean("tamed", isTamed());
            if (isTamed()) {
                tag.putUUID("owner", owner);
            }
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> tag.putFloat(attribute.name(), attributeMap.getFloat(attribute)));
        }
    }

    public ParadiseLostDataComponentTypes.MoaGeneComponent intoComponent() {
        var attributes = new ParadiseLostDataComponentTypes.MoaAttributeComponent(
                attributeMap.getFloat(MoaAttributes.GROUND_SPEED),
                attributeMap.getFloat(MoaAttributes.GLIDING_SPEED),
                attributeMap.getFloat(MoaAttributes.GLIDING_DECAY),
                attributeMap.getFloat(MoaAttributes.JUMPING_STRENGTH),
                attributeMap.getFloat(MoaAttributes.DROP_MULTIPLIER),
                attributeMap.getFloat(MoaAttributes.MAX_HEALTH)
        );
        return new ParadiseLostDataComponentTypes.MoaGeneComponent(race.getId(), affinity.name(), true, hunger, owner == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : owner, attributes);
    }
}
