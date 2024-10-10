package net.id.paradiselost.items.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;
import java.util.function.UnaryOperator;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostDataComponentTypes {

    // Registered Components

    public static final ComponentType<MoaGeneComponent> MOA_GENES = register("moa_genes", (builder) -> builder.codec(MoaGeneComponent.CODEC).packetCodec(MoaGeneComponent.PACKET_CODEC).cache());
    public static final ComponentType<BloodstoneComponent> BLOODSTONE = register("bloodstone", (builder) -> builder.codec(BloodstoneComponent.CODEC).packetCodec(BloodstoneComponent.PACKET_CODEC));


    // Util

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, locate(id), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void init() {
    }

    // Component Source

    public record MoaGeneComponent(Identifier race, String affinity, boolean isBaby, float hunger, UUID ownerId, MoaAttributeComponent attributes) {

        public static final Codec<MoaGeneComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Identifier.CODEC.optionalFieldOf("race", ParadiseLost.locate("fallback")).forGetter(MoaGeneComponent::race),
                Codec.STRING.optionalFieldOf("affinity", "").forGetter(MoaGeneComponent::affinity),
                Codec.BOOL.optionalFieldOf("is_baby", true).forGetter(MoaGeneComponent::isBaby),
                Codec.FLOAT.optionalFieldOf("hunger", 0.0F).forGetter(MoaGeneComponent::hunger),
                Uuids.CODEC.optionalFieldOf("owner_id", UUID.fromString("00000000-0000-0000-0000-000000000000")).forGetter(MoaGeneComponent::ownerId),
                MoaAttributeComponent.CODEC.optionalFieldOf("attributes", new MoaAttributeComponent(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)).forGetter(MoaGeneComponent::attributes)
        ).apply(instance, MoaGeneComponent::new));
        public static final PacketCodec<RegistryByteBuf, MoaGeneComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = PacketCodec.tuple(
                    Identifier.PACKET_CODEC, MoaGeneComponent::race,
                    PacketCodecs.STRING, MoaGeneComponent::affinity,
                    PacketCodecs.BOOL, MoaGeneComponent::isBaby,
                    PacketCodecs.FLOAT, MoaGeneComponent::hunger,
                    Uuids.PACKET_CODEC, MoaGeneComponent::ownerId,
                    MoaAttributeComponent.PACKET_CODEC, MoaGeneComponent::attributes,
                    MoaGeneComponent::new
            );
        }

        public Identifier race() {
            return this.race;
        }

        public String affinity() {
            return this.affinity;
        }

        public boolean isBaby() {
            return this.isBaby;
        }

        public float hunger() {
            return this.hunger;
        }

        public UUID ownerId() {
            return this.ownerId;
        }

        public MoaAttributeComponent attributes() {
            return this.attributes;
        }

    }

    public record MoaAttributeComponent(float groundSpeed, float glidingSpeed, float glidingDecay, float jumpStrength, float dropMultiplier, float maxHealth) {

        public static final Codec<MoaAttributeComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.FLOAT.fieldOf("ground_speed").forGetter(MoaAttributeComponent::groundSpeed),
                Codec.FLOAT.fieldOf("gliding_speed").forGetter(MoaAttributeComponent::glidingSpeed),
                Codec.FLOAT.fieldOf("gliding_decay").forGetter(MoaAttributeComponent::glidingDecay),
                Codec.FLOAT.fieldOf("jump_strength").forGetter(MoaAttributeComponent::jumpStrength),
                Codec.FLOAT.fieldOf("drop_multiplier").forGetter(MoaAttributeComponent::dropMultiplier),
                Codec.FLOAT.fieldOf("max_health").forGetter(MoaAttributeComponent::maxHealth)
        ).apply(instance, MoaAttributeComponent::new));
        public static final PacketCodec<RegistryByteBuf, MoaAttributeComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = PacketCodec.tuple(
                    PacketCodecs.FLOAT, MoaAttributeComponent::groundSpeed,
                    PacketCodecs.FLOAT, MoaAttributeComponent::glidingSpeed,
                    PacketCodecs.FLOAT, MoaAttributeComponent::glidingDecay,
                    PacketCodecs.FLOAT, MoaAttributeComponent::jumpStrength,
                    PacketCodecs.FLOAT, MoaAttributeComponent::dropMultiplier,
                    PacketCodecs.FLOAT, MoaAttributeComponent::maxHealth,
                    MoaAttributeComponent::new
            );
        }

        public float groundSpeed() {
            return this.groundSpeed;
        }

        public float glidingSpeed() {
            return this.glidingSpeed;
        }

        public float glidingDecay() {
            return this.glidingDecay;
        }

        public float jumpStrength() {
            return this.jumpStrength;
        }

        public float dropMultiplier() {
            return this.dropMultiplier;
        }

        public float maxHealth() {
            return this.maxHealth;
        }

    }

    public record BloodstoneComponent(UUID uuid, Text name, String health, String defense, String toughness, String owner) {

        public static final Codec<BloodstoneComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Uuids.CODEC.fieldOf("uuid").forGetter(BloodstoneComponent::uuid),
                TextCodecs.CODEC.fieldOf("name").forGetter(BloodstoneComponent::name),
                Codec.STRING.fieldOf("health").forGetter(BloodstoneComponent::health),
                Codec.STRING.fieldOf("defense").forGetter(BloodstoneComponent::defense),
                Codec.STRING.fieldOf("toughness").forGetter(BloodstoneComponent::toughness),
                Codec.STRING.fieldOf("owner").forGetter(BloodstoneComponent::owner)
        ).apply(instance, BloodstoneComponent::new));
        public static final PacketCodec<RegistryByteBuf, BloodstoneComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = PacketCodec.tuple(
                    Uuids.PACKET_CODEC, BloodstoneComponent::uuid,
                    TextCodecs.PACKET_CODEC, BloodstoneComponent::name,
                    PacketCodecs.STRING, BloodstoneComponent::health,
                    PacketCodecs.STRING, BloodstoneComponent::defense,
                    PacketCodecs.STRING, BloodstoneComponent::toughness,
                    PacketCodecs.STRING, BloodstoneComponent::owner,
                    BloodstoneComponent::new
            );
        }

        public UUID uuid() {
            return this.uuid;
        }

        public Text name() {
            return this.name;
        }

        public String health() {
            return this.health;
        }

        public String defense() {
            return this.defense;
        }

        public String toughness() {
            return this.toughness;
        }

        public String owner() {
            return this.owner;
        }

    }
}
