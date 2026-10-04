package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.util.ParadiseLostDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;

public class ParadiseLostDamageTypeTagsProvider extends TagsProvider<DamageType> {
    public ParadiseLostDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, ModConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
        this.tag(DamageTypeTags.NO_IMPACT).add(ParadiseLostDamageTypes.FALL_FROM_PARADISE);
    }
}
