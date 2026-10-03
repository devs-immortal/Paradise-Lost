package net.id.paradise_lost.mixin.world.gen.structure;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.world.feature.RiverField;
import net.id.paradise_lost.world.feature.configs.RiverConfiguration;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

@Mixin(JigsawStructure.class)
public class JigsawStructureMixin {

    @Shadow
    @Final
    private HeightProvider startHeight;

    @Shadow
    @Final
    private Optional<Heightmap.Types> projectStartToHeightmap;

    @Inject(
            method = "findGenerationPoint",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getStructurePosition(Structure.GenerationContext context, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir) {
        ChunkPos chunkPos = context.chunkPos();
        if (this.projectStartToHeightmap.isPresent()
                && anchoredInRiver(context, chunkPos.getMinBlockX(), chunkPos.getMinBlockZ())) {
            cir.setReturnValue(Optional.empty());
            return;
        }
        int startHeightPos = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        var surfaceHeight = context.chunkGenerator().getBaseHeight(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), projectStartToHeightmap.orElse(Heightmap.Types.WORLD_SURFACE_WG), context.heightAccessor(), context.randomState());

        if (startHeightPos >= context.heightAccessor().getMinBuildHeight() && surfaceHeight <= context.heightAccessor().getMinBuildHeight()) {
            cir.setReturnValue(Optional.empty());
        }

    }

    /** Structure starts use noise height, so they ignore the carved channel unless rejected here. */
    private static boolean anchoredInRiver(Structure.GenerationContext context, int x, int z) {
        Holder<Biome> biome = context.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(x), 0, QuartPos.fromBlock(z), context.randomState().sampler());
        boolean paradise = biome.unwrapKey()
                .map(key -> ModConstants.MODID.equals(key.location().getNamespace()))
                .orElse(false);
        if (!paradise) {
            return false;
        }
        RiverConfiguration river = RiverConfiguration.noiseDefaults();
        return RiverField.covers(RiverField.noise(context.seed(), river.salt()), river, x, z, 2.0);
    }

}
