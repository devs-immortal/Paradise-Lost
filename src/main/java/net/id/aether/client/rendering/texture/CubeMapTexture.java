package net.id.aether.client.rendering.texture;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.util.Set;
import net.id.aether.mixin.client.render.NativeImageAccessor;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import static org.lwjgl.opengl.GL33.*;

public final class CubeMapTexture extends AbstractTexture{
    private static final Object2IntMap<Direction> DIRECTION_IDS;
    static{
        Object2IntMap<Direction> map = new Object2IntOpenHashMap<>(6);
        map.put(Direction.DOWN, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);
        map.put(Direction.UP, GL_TEXTURE_CUBE_MAP_POSITIVE_Y);
        map.put(Direction.NORTH, GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
        map.put(Direction.SOUTH, GL_TEXTURE_CUBE_MAP_POSITIVE_Z);
        map.put(Direction.WEST, GL_TEXTURE_CUBE_MAP_NEGATIVE_X);
        map.put(Direction.EAST, GL_TEXTURE_CUBE_MAP_POSITIVE_X);
        DIRECTION_IDS = Object2IntMaps.unmodifiable(map);
    }
    
    private final Identifier identifier;
    
    public CubeMapTexture(Identifier identifier){
        this.identifier = new Identifier(identifier.getNamespace(), "textures/cubemap/" + identifier.getPath());
    }
    
    @Override
    public void load(ResourceManager manager) throws IOException{
        Resource[] faceResources = new Resource[6];
        NativeImage[] faceImages = new NativeImage[6];
        Set<AutoCloseable[]> closeables = Set.of(
            faceResources, faceImages
        );
        
        try{
            for(Direction value : Direction.values()){
                var faceId = new Identifier(identifier.getNamespace(), identifier.getPath() + "/" + value.getName() + ".png");
                faceResources[value.ordinal()] = manager.getResource(faceId);
            }
            for(Direction value : Direction.values()){
                int ordinal = value.ordinal();
                faceImages[ordinal] = NativeImage.read(NativeImage.Format.RGBA, faceResources[ordinal].getInputStream());
            }
            
            int id = getGlId();
            glBindTexture(GL_TEXTURE_CUBE_MAP, id);
            for(Direction value : Direction.values()){
                var image = faceImages[value.ordinal()];
                var format = image.getFormat();
                //noinspection ConstantConditions
                glTexImage2D(
                    DIRECTION_IDS.getOrDefault(value, -1),
                    0, format.toGl(), image.getWidth(), image.getHeight(), 0, format.toGl(), GL_UNSIGNED_BYTE,
                    ((NativeImageAccessor)(Object)image).getPointer()
                );
            }
            
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        }finally{
            for(var array : closeables){
                for(var element : array){
                    if(element != null){
                        try{
                            element.close();
                        }catch(Exception ignored){}
                    }
                }
            }
        }
    }
}
