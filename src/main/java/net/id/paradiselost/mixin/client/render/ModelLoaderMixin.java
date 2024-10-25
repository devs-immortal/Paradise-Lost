package net.id.paradiselost.mixin.client.render;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.id.paradiselost.ParadiseLost.locate;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

    @Unique
    private static final ModelIdentifier OLVITE_SPYGLASS_IN_HAND = ModelIdentifier.ofInventoryVariant(locate("olvite_spyglass_in_hand"));

    @Shadow
    protected abstract void loadItemModel(ModelIdentifier id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;loadItemModel(Lnet/minecraft/client/util/ModelIdentifier;)V", ordinal = 0))
    public void init(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        this.loadItemModel(OLVITE_SPYGLASS_IN_HAND);
    }
}
