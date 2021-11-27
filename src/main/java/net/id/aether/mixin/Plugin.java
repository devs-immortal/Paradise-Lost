package net.id.aether.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class Plugin implements IMixinConfigPlugin{
    private static final boolean isDevel = FabricLoader.getInstance().isDevelopmentEnvironment();
    
    private static final class Compat{
        private static final boolean satin = isLoaded("satin");
        
        private static boolean isLoaded(String id){
            return FabricLoader.getInstance().isModLoaded(id);
        }
    }
    
    @Override
    public void onLoad(String mixinPackage){}
    
    @Override
    public String getRefMapperConfig(){
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName){
        if(Compat.satin && mixinClassName.equals("net.id.aether.mixin.client.render.ShaderIdFixMixin")){
            return false;
        }
        
        if(isDevel){
            return true;
        }
        return !mixinClassName.startsWith("net.id.aether.mixin.devel.");
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets){}
    
    @Override
    public List<String> getMixins(){
        return List.of();
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo){}
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo){}
}
