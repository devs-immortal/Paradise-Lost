package net.id.paradise_lost.mixin.world.gen.structure;

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
        int startHeightPos = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        var surfaceHeight = context.chunkGenerator().getBaseHeight(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), projectStartToHeightmap.orElse(Heightmap.Types.WORLD_SURFACE_WG), context.heightAccessor(), context.randomState());

        if (startHeightPos >= context.heightAccessor().getMinBuildHeight() && surfaceHeight <= context.heightAccessor().getMinBuildHeight()) {
            cir.setReturnValue(Optional.empty());
        }

    }

}
