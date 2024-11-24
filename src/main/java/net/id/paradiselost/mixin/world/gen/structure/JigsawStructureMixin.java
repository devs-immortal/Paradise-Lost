package net.id.paradiselost.mixin.world.gen.structure;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/*
 * This mixin changes the behavior of jigsaw structures (used by all of PL's structures)
 * If a structure would generate at a position where the chunk generator says there is
 * no terrain (top y is bottom of the world) then we skip generating that structure
 *
 * This prevents surface structures from spawning at the bottom of the world and prevents
 * dungeons from generating in the air where it is unlikely to be a good location.
 */
@Mixin(JigsawStructure.class)
public class JigsawStructureMixin {

    @Shadow
    @Final
    private Optional<Heightmap.Type> projectStartToHeightmap;

    @Inject(
            method = "getStructurePosition",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getStructurePosition(Structure.Context context, CallbackInfoReturnable<Optional<Structure.StructurePosition>> cir) {
        ChunkPos chunkPos = context.chunkPos();
        var surfaceHeight = context.chunkGenerator().getHeight(chunkPos.getStartX(), chunkPos.getStartZ(), projectStartToHeightmap.orElse(Heightmap.Type.WORLD_SURFACE_WG), context.world(), context.noiseConfig());
        //System.out.println("Generating structure at height " + surfaceHeight + ", find at pos [" + chunkPos.getStartX() + ", " + chunkPos.getStartZ() + "]");
        //int i = this.startHeight.get(context.random(), new HeightContext(context.chunkGenerator(), context.world()));
        if (surfaceHeight <= context.world().getBottomY()) {
            cir.setReturnValue(Optional.empty());
        }

    }

}
