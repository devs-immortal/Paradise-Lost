package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.tag.ParadiseLostGameEventTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ParadiseLostGameEventTagsProvider extends GameEventTagsProvider {
    public ParadiseLostGameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
                                             @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, ModConstants.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ParadiseLostGameEventTags.FLOATY_BOOTS_DAMPENED)
                .add(GameEvent.STEP.key())
                .add(GameEvent.HIT_GROUND.key());
    }
}
