package net.id.paradise_lost.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ParadiseLostDataComponentTypes {
    private static final RegistrationProvider<DataComponentType<?>> DATA_COMPONENT_TYPES =
            RegistrationProvider.get(Registries.DATA_COMPONENT_TYPE, ModConstants.MODID);

    public static final DataComponentType<MoaGeneComponent> MOA_GENES = register("moa_genes", (builder) -> builder.persistent(MoaGeneComponent.CODEC).networkSynchronized(MoaGeneComponent.PACKET_CODEC).cacheEncoding());
    public static final DataComponentType<BloodstoneComponent> BLOODSTONE = register("bloodstone", (builder) -> builder.persistent(BloodstoneComponent.CODEC).networkSynchronized(BloodstoneComponent.PACKET_CODEC));
    public static final DataComponentType<XpCircletChargeComponent> XP_CIRCLET_CHARGE = register("xp_circlet", (builder) -> builder.persistent(XpCircletChargeComponent.CODEC).networkSynchronized(XpCircletChargeComponent.PACKET_CODEC));
    // Not vanilla's ominous_bottle_amplifier: that component applies its own 100 minute Bad Omen when the item is eaten.
    public static final DataComponentType<Integer> OMINOUS_COOKIE_AMPLIFIER = register("ominous_cookie_amplifier", (builder) -> builder.persistent(ExtraCodecs.intRange(0, 4)).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<CollectedSoulsComponent> COLLECTED_SOULS = register("collected_souls", (builder) -> builder.persistent(CollectedSoulsComponent.CODEC).networkSynchronized(CollectedSoulsComponent.PACKET_CODEC));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        DataComponentType<T> type = builderOperator.apply(DataComponentType.builder()).build();
        DATA_COMPONENT_TYPES.register(id, () -> type);
        return type;
    }

    public static void init() {
    }

    public record MoaGeneComponent(ResourceLocation race, String affinity, boolean isBaby, float hunger, UUID ownerId, MoaAttributeComponent attributes) {

        public static final Codec<MoaGeneComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("race", ModConstants.id("fallback")).forGetter(MoaGeneComponent::race),
                Codec.STRING.optionalFieldOf("affinity", "").forGetter(MoaGeneComponent::affinity),
                Codec.BOOL.optionalFieldOf("is_baby", true).forGetter(MoaGeneComponent::isBaby),
                Codec.FLOAT.optionalFieldOf("hunger", 0.0F).forGetter(MoaGeneComponent::hunger),
                UUIDUtil.AUTHLIB_CODEC.optionalFieldOf("owner_id", UUID.fromString("00000000-0000-0000-0000-000000000000")).forGetter(MoaGeneComponent::ownerId),
                MoaAttributeComponent.CODEC.optionalFieldOf("attributes", new MoaAttributeComponent(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)).forGetter(MoaGeneComponent::attributes)
        ).apply(instance, MoaGeneComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, MoaGeneComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, MoaGeneComponent::race,
                    ByteBufCodecs.STRING_UTF8, MoaGeneComponent::affinity,
                    ByteBufCodecs.BOOL, MoaGeneComponent::isBaby,
                    ByteBufCodecs.FLOAT, MoaGeneComponent::hunger,
                    UUIDUtil.STREAM_CODEC, MoaGeneComponent::ownerId,
                    MoaAttributeComponent.PACKET_CODEC, MoaGeneComponent::attributes,
                    MoaGeneComponent::new
            );
        }

        public ResourceLocation race() {
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
        public static final StreamCodec<RegistryFriendlyByteBuf, MoaAttributeComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = StreamCodec.composite(
                    ByteBufCodecs.FLOAT, MoaAttributeComponent::groundSpeed,
                    ByteBufCodecs.FLOAT, MoaAttributeComponent::glidingSpeed,
                    ByteBufCodecs.FLOAT, MoaAttributeComponent::glidingDecay,
                    ByteBufCodecs.FLOAT, MoaAttributeComponent::jumpStrength,
                    ByteBufCodecs.FLOAT, MoaAttributeComponent::dropMultiplier,
                    ByteBufCodecs.FLOAT, MoaAttributeComponent::maxHealth,
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

    public record BloodstoneComponent(UUID uuid, Component name, String health, String defense, String toughness, String owner) {

        public static final Codec<BloodstoneComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                UUIDUtil.AUTHLIB_CODEC.fieldOf("uuid").forGetter(BloodstoneComponent::uuid),
                ComponentSerialization.CODEC.fieldOf("name").forGetter(BloodstoneComponent::name),
                Codec.STRING.fieldOf("health").forGetter(BloodstoneComponent::health),
                Codec.STRING.fieldOf("defense").forGetter(BloodstoneComponent::defense),
                Codec.STRING.fieldOf("toughness").forGetter(BloodstoneComponent::toughness),
                Codec.STRING.fieldOf("owner").forGetter(BloodstoneComponent::owner)
        ).apply(instance, BloodstoneComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, BloodstoneComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC, BloodstoneComponent::uuid,
                    ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, BloodstoneComponent::name,
                    ByteBufCodecs.STRING_UTF8, BloodstoneComponent::health,
                    ByteBufCodecs.STRING_UTF8, BloodstoneComponent::defense,
                    ByteBufCodecs.STRING_UTF8, BloodstoneComponent::toughness,
                    ByteBufCodecs.STRING_UTF8, BloodstoneComponent::owner,
                    BloodstoneComponent::new
            );
        }

        public UUID uuid() {
            return this.uuid;
        }

        public Component name() {
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

    public record XpCircletChargeComponent(int storedXp) {

        public static final Codec<XpCircletChargeComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.INT.fieldOf("xp_content").forGetter(XpCircletChargeComponent::storedXp)
        ).apply(instance, XpCircletChargeComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, XpCircletChargeComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = StreamCodec.composite(
                    ByteBufCodecs.INT, XpCircletChargeComponent::storedXp,
                    XpCircletChargeComponent::new
            );
        }

        public int storedXp() {
            return this.storedXp;
        }

        public boolean charged() {
            return this.storedXp > 0;
        }

    }

    public record CollectedSoulsComponent(List<String> soulIds) {

        public static final Codec<CollectedSoulsComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.STRING.listOf().fieldOf("souls").forGetter(CollectedSoulsComponent::soulIds)
        ).apply(instance, CollectedSoulsComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, CollectedSoulsComponent> PACKET_CODEC;

        static {
            PACKET_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), CollectedSoulsComponent::soulIds,
                    CollectedSoulsComponent::new
            );
        }

        public List<String> soulIds() {
            return this.soulIds;
        }

        public int soulCount() {
            return this.soulIds.size();
        }

    }
}
