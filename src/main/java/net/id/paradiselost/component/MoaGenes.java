package net.id.paradiselost.component;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.fabricmc.fabric.api.util.NbtType;
import net.id.paradiselost.api.MoaAPI;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.Arrays;
import java.util.UUID;

import static net.id.paradiselost.api.MoaAPI.FALLBACK_MOA;

public class MoaGenes implements AutoSyncedComponent {

    private final Object2FloatOpenHashMap<MoaAttributes> attributeMap = new Object2FloatOpenHashMap<>();
    private MoaAPI.MoaRace race = FALLBACK_MOA;
    private MoaAttributes affinity;
    private boolean legendary, initialized;
    private UUID owner;
    private float hunger = 100F;

    public MoaGenes() {
    }

    public static ItemStack getEggForCommand(MoaAPI.MoaRace race, World world, boolean baby) {
        Random random = world.getRandom();
        ItemStack stack = new ItemStack(ParadiseLostItems.MOA_EGG);

        float attr1 = race.statWeighting().configure(MoaAttributes.GROUND_SPEED, race, random);
        float attr2 = race.statWeighting().configure(MoaAttributes.GLIDING_SPEED, race, random);
        float attr3 = race.statWeighting().configure(MoaAttributes.GLIDING_DECAY, race, random);
        float attr4 = race.statWeighting().configure(MoaAttributes.JUMPING_STRENGTH, race, random);
        float attr5 = race.statWeighting().configure(MoaAttributes.DROP_MULTIPLIER, race, random);
        float attr6 = race.statWeighting().configure(MoaAttributes.MAX_HEALTH, race, random);

        var attributes = new ParadiseLostDataComponentTypes.MoaAttributeComponent(attr1, attr2, attr3, attr4, attr5, attr6);
        var genes = new ParadiseLostDataComponentTypes.MoaGeneComponent(race.getId(), race.defaultAffinity().name(), baby, 0.0F, null, attributes);

        stack.set(ParadiseLostDataComponentTypes.MOA_GENES, genes);
        return stack;
    }

    public static MoaEntity getMoaFromEgg(World world, ItemStack stack, UUID owner) {
        MoaEntity moa = ParadiseLostEntityTypes.MOA.create(world);
        MoaGenes genes = moa.getGenes();
        if (stack.isOf(ParadiseLostItems.MOA_EGG)) {
            genes.fromComponent(stack.get(ParadiseLostDataComponentTypes.MOA_GENES));
            genes.owner = owner == null ? UUID.randomUUID() : owner;
        }
        moa.setBreedingAge(-43200);
        return moa;
    }

    public static MoaGenes get(@NotNull MoaEntity moa) {
        return ParadiseLostComponents.MOA_GENETICS_KEY.get(moa);
    }

    public void initMoa(@NotNull MoaEntity moa) {
        World world = moa.getWorld();
        Random random = moa.getRandom();
        race = MoaAPI.getMoaFromSpawning(world.getBiome(moa.getBlockPos()).getKey().get(), random);
        affinity = race.defaultAffinity();

        for (MoaAttributes attribute : MoaAttributes.values()) {
            attributeMap.addTo(attribute, race.statWeighting().configure(attribute, race, random));
        }
        initialized = true;
    }

    public ItemStack getEggForBreeding(MoaGenes otherParent, World world, BlockPos pos) {
        var childRace = MoaAPI.getMoaFromBreeding(this, otherParent, world, pos);

        ItemStack stack = new ItemStack(ParadiseLostItems.MOA_EGG);
        Random random = world.getRandom();
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

    public Identifier getTexture() {
        Identifier id = this.race.getId();
        String name = id.getPath();
        String namespace = id.getNamespace();
        return new Identifier(namespace, "textures/entity/moa/" + name + ".png");
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

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        initialized = tag.getBoolean("initialized");
        if (initialized) {
            race = MoaAPI.getRace(Identifier.tryParse(tag.getString("raceId")));
            String affinityStr = tag.getString("affinity");
            affinity = affinityStr.isEmpty() ? race.defaultAffinity() : MoaAttributes.valueOf(affinityStr);
            legendary = tag.getBoolean("legendary");
            if (tag.contains("hunger", NbtType.FLOAT)) {
                hunger = tag.getFloat("hunger");
            }
            if (tag.getBoolean("tamed")) {
                owner = tag.getUuid("owner");
            }
            Arrays.stream(MoaAttributes.values()).forEach(attribute -> {
                if (tag.contains(attribute.name(), NbtType.FLOAT)) {
                    attributeMap.put(attribute, tag.getFloat(attribute.name()));
                } else {
                    // AZZY THIS IS SO CURSED WHY IS JUMPING BACKWARDSSSSSSS
                    // TODO b1.7 make this not be so weird
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
        if (com.ownerId() != UUID.fromString("00000000-0000-0000-0000-000000000000")) owner = com.ownerId();
        attributeMap.put(MoaAttributes.GROUND_SPEED, com.attributes().groundSpeed());
        attributeMap.put(MoaAttributes.GLIDING_SPEED, com.attributes().glidingSpeed());
        attributeMap.put(MoaAttributes.GLIDING_DECAY, com.attributes().glidingDecay());
        attributeMap.put(MoaAttributes.JUMPING_STRENGTH, com.attributes().jumpStrength());
        attributeMap.put(MoaAttributes.DROP_MULTIPLIER, com.attributes().dropMultiplier());
        attributeMap.put(MoaAttributes.MAX_HEALTH, com.attributes().maxHealth());
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("initialized", initialized);
        if (initialized) {
            tag.putString("raceId", race.getId().toString());
            tag.putString("affinity", affinity.name());
            tag.putBoolean("legendary", legendary);
            tag.putFloat("hunger", hunger);
            tag.putBoolean("tamed", isTamed());
            if (isTamed()) {
                tag.putUuid("owner", owner);
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
        return new ParadiseLostDataComponentTypes.MoaGeneComponent(race.getId(), affinity.name(), true, hunger, owner, attributes);
    }
}
