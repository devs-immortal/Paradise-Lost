package net.id.paradiselost.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.util.CompatConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class Plugin implements IMixinConfigPlugin {
    private static final boolean isDevel = FabricLoader.getInstance().isDevelopmentEnvironment();
    
    @Override
    public void onLoad(String mixinPackage) {
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("net.id.paradiselost.mixin.client.ClientPlayerEntityMixin")) {
            return !CompatConfig.SPECTRUM_WORKAROUND;
        }
        if (isDevel) {
            return true;
        }
        return !mixinClassName.startsWith("net.id.paradiselost.mixin.devel.");
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }
    
    @Override
    public List<String> getMixins() {
        return List.of();
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
