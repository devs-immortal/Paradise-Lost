package net.id.paradiselost.mixin.world.gen.structure;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.HeightContext;
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

@Mixin(JigsawStructure.class)
public class JigsawStructureMixin {

    @Shadow
    @Final
    private HeightProvider startHeight;

    @Inject(
            method = "getStructurePosition",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getStructurePosition(Structure.Context context, CallbackInfoReturnable<Optional<Structure.StructurePosition>> cir) {
        ChunkPos chunkPos = context.chunkPos();
        int i = this.startHeight.get(context.random(), new HeightContext(context.chunkGenerator(), context.world()));
        if (i <= context.world().getBottomY()) {
            cir.setReturnValue(Optional.empty());
        }

    }

}
