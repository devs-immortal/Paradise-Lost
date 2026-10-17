package net.id.paradise_lost.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class Plugin implements IMixinConfigPlugin {
    private static final String WILDER_WILD_SNOW_BLANKET = "net/frozenblock/wilderwild/worldgen/impl/feature/SnowBlanketFeature.class";

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !mixinClassName.startsWith("net.id.paradise_lost.mixin.devel.");
    }

    @Override
    public List<String> getMixins() {
        return isPresent(WILDER_WILD_SNOW_BLANKET) ? List.of("compat.wilderwild.SnowBlanketFeatureMixin") : List.of();
    }

    private static boolean isPresent(String classPath) {
        ClassLoader[] loaders = {Plugin.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        for (ClassLoader loader : loaders) {
            if (loader != null && loader.getResource(classPath) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
