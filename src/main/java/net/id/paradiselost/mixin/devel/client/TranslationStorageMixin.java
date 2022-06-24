package net.id.paradiselost.mixin.devel.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.devel.ParadiseLostDevel;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(TranslationStorage.class)
public abstract class TranslationStorageMixin {
    @Shadow @Final private Map<String, String> translations;
    
    @Inject(
        method = "get",
        at = @At("HEAD"),
        cancellable = true
    )
    private void get(String key, CallbackInfoReturnable<String> cir){
        var translation = translations.get(key);
        if(translation == null){
            if(key != null && key.toLowerCase(Locale.ROOT).contains("paradise_lost")){
                ParadiseLostDevel.Client.logMissingLanguageKey(key);
            }
            cir.setReturnValue(key);
        }else{
            cir.setReturnValue(translation);
        }
    }
}
