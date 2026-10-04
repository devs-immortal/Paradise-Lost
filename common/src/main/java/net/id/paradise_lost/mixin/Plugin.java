package net.id.paradise_lost.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class Plugin implements IMixinConfigPlugin {
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
        if (classPresent("net.frozenblock.wilderwild.worldgen.impl.feature.SnowBlanketFeature")
                || classPresent("net.frozenblock.wilderwild.levelgen.feature.SnowBlanketFeature")) {
            return List.of("compat.wilderwild.SnowBlanketFeatureMixin");
        }
        return List.of();
    }

    private static boolean classPresent(String name) {
        String path = name.replace('.', '/') + ".class";
        try {
            ClassLoader cl = Plugin.class.getClassLoader();
            if (cl != null && cl.getResource(path) != null) {
                return true;
            }
            ClassLoader ctx = Thread.currentThread().getContextClassLoader();
            return ctx != null && ctx.getResource(path) != null;
        } catch (Throwable ignored) {
            return false;
        }
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
