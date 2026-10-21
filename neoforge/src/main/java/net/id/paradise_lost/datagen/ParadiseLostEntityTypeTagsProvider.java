package net.id.paradise_lost.datagen;

import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.tag.ParadiseLostEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ParadiseLostEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.ARROWS).add(EntityRegistry.LEVITA_ARROW.get());
        this.tag(ParadiseLostEntityTypeTags.CANNOT_LEVITATE)
                .add(EntityType.AREA_EFFECT_CLOUD)
                .add(EntityType.BLOCK_DISPLAY)
                .add(EntityType.DRAGON_FIREBALL)
                .add(EntityType.EGG)
                .add(EntityType.END_CRYSTAL)
                .add(EntityType.ENDER_PEARL)
                .add(EntityType.EVOKER_FANGS)
                .add(EntityType.EXPERIENCE_BOTTLE)
                .add(EntityType.FALLING_BLOCK)
                .add(EntityType.FIREWORK_ROCKET)
                .add(EntityType.GLOW_ITEM_FRAME)
                .add(EntityType.INTERACTION)
                .add(EntityType.ITEM_DISPLAY)
                .add(EntityType.ITEM_FRAME)
                .add(EntityType.OMINOUS_ITEM_SPAWNER)
                .add(EntityType.FIREBALL)
                .add(EntityType.LEASH_KNOT)
                .add(EntityType.LIGHTNING_BOLT)
                .add(EntityType.LLAMA_SPIT)
                .add(EntityType.MARKER)
                .add(EntityType.PAINTING)
                .add(EntityType.POTION)
                .add(EntityType.SHULKER_BULLET)
                .add(EntityType.SMALL_FIREBALL)
                .add(EntityType.SNOWBALL)
                .add(EntityType.TEXT_DISPLAY)
                .add(EntityType.WIND_CHARGE)
                .add(EntityType.WITHER_SKULL)
                .add(EntityRegistry.THROWN_NITRA.get());
    }
}
