package net.id.paradiselost.items.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostDataComponentTypes {

    // Registered Components

    public static final DataComponentType<MoaGeneComponent> MOA_GENES = register("moa_genes", (builder) -> builder.codec(MoaGeneComponent.CODEC).packetCodec(MoaGeneComponent.PACKET_CODEC).cache());



    // Util

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, locate(id), builderOperator.apply(DataComponentType.builder()).build());
    }

    // Component Source

    public record MoaGeneComponent(Identifier race, String affinity, boolean isBaby, float hunger, UUID ownerId, MoaAttributeComponent attributes) {

        public static final Codec<MoaGeneComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Identifier.CODEC.optionalFieldOf("race", ParadiseLost.locate("fallback")).forGetter(MoaGeneComponent::race),
                Codec.STRING.optionalFieldOf("affinity", "").forGetter(MoaGeneComponent::affinity),
                Codec.BOOL.optionalFieldOf("is_baby", true).forGetter(MoaGeneComponent::isBaby),
                Codec.FLOAT.optionalFieldOf("hunger", 0.0F).forGetter(MoaGeneComponent::hunger),
                Uuids.CODEC.optionalFieldOf("owner_id", null).forGetter(MoaGeneComponent::ownerId),
                MoaAttributeComponent.CODEC.optionalFieldOf("attributes", null).forGetter(MoaGeneComponent::attributes)
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
                Codec.FLOAT.fieldOf("value").forGetter(MoaAttributeComponent::groundSpeed),
                Codec.FLOAT.fieldOf("value").forGetter(MoaAttributeComponent::glidingSpeed),
                Codec.FLOAT.fieldOf("value").forGetter(MoaAttributeComponent::glidingDecay),
                Codec.FLOAT.fieldOf("value").forGetter(MoaAttributeComponent::jumpStrength),
                Codec.FLOAT.fieldOf("value").forGetter(MoaAttributeComponent::dropMultiplier),
                Codec.FLOAT.fieldOf("value").forGetter(MoaAttributeComponent::maxHealth)
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
}
