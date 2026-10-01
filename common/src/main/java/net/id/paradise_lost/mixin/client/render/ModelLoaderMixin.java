package net.id.paradise_lost.mixin.client.render;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.id.paradise_lost.ModConstants.id;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {

    @Unique
    private static final ModelResourceLocation OLVITE_SPYGLASS_IN_HAND = ModelResourceLocation.inventory(id("olvite_spyglass_in_hand"));

    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", ordinal = 0))
    public void init(BlockColors blockColors, ProfilerFiller profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        this.loadSpecialItemModelAndDependencies(OLVITE_SPYGLASS_IN_HAND);
    }
}
