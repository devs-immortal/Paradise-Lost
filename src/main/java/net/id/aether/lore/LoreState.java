package net.id.aether.lore;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.component.AetherComponents;
import net.id.aether.mixin.util.NbtCompoundAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import static net.id.aether.registry.AetherRegistries.LORE_REGISTRY;

public interface LoreState extends AutoSyncedComponent{
    @NotNull LoreStatus getLoreStatus(@NotNull Identifier identifier);
    
    void setLoreStatus(@NotNull Identifier identifier, @NotNull LoreStatus status);
    
    default @NotNull LoreStatus getLoreStatus(@NotNull LoreEntry<?> lore){
        Objects.requireNonNull(lore, "lore was null");
        Identifier id = LORE_REGISTRY.getId(lore);
        if(id == null){
            throw new RuntimeException("Unknown lore entry: " + lore);
        }
        return getLoreStatus(id);
    }
    
    default void setLoreStatus(@NotNull LoreEntry<?> lore, @NotNull LoreStatus status){
        Objects.requireNonNull(lore, "lore was null");
        Objects.requireNonNull(status, "status was null");
        Identifier id = LORE_REGISTRY.getId(lore);
        if(id == null){
            throw new RuntimeException("Unknown lore entry: " + lore);
        }
        setLoreStatus(id, status);
    }
    
    final class Impl implements LoreState{
        private final Map<Identifier, LoreStatus> loreStates = new Object2ObjectOpenHashMap<>();
        private final Set<Identifier> dirtyLore = new HashSet<>();
        private final PlayerEntity player;
        private final boolean isClient;
    
        public Impl(PlayerEntity player){
            this.player = player;
            isClient = player.world.isClient();
        }
        
        @Override
        public void readFromNbt(NbtCompound tag){
            var statuses = tag.getCompound("statuses");
            loreStates.clear();
            ((NbtCompoundAccessor)statuses).getEntries().forEach((key, value)->
                loreStates.put(new Identifier(key), LoreStatus.ofValue(value.asString()))
            );
            dirtyLore.addAll(loreStates.keySet());
            validate();
        }
    
        private void validate(){
            //TODO
        }
    
        @Override
        public void writeToNbt(NbtCompound tag){
            var statuses = new NbtCompound();
            loreStates.forEach((key, value)->
                statuses.putString(key.toString(), value.getName())
            );
        }
        
        @Override
        public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient){
            buf.writeVarInt(dirtyLore.size());
            for(Identifier identifier : dirtyLore){
                buf.writeIdentifier(identifier);
                buf.writeVarInt(loreStates.getOrDefault(identifier, LoreStatus.HIDDEN).ordinal());
            }
            dirtyLore.clear();
        }
        
        @Override
        public void applySyncPacket(PacketByteBuf buf){
            int count = buf.readVarInt();
            for(int i = 0; i < count; i++){
                var id = buf.readIdentifier();
                var status = LoreStatus.ofValue(buf.readVarInt());
                var oldStatus = loreStates.put(id, status);
                if(isClient && oldStatus != status && status == LoreStatus.COMPLETED){
                    // Fingers crossed this gets by the validator
                    triggerToast(id);
                }
            }
        }
        
        @Environment(EnvType.CLIENT)
        private void triggerToast(Identifier id){
            LORE_REGISTRY.getOrEmpty(id).ifPresent((lore)->{
                MinecraftClient.getInstance().getToastManager().add(new Toast(){
                    @Override
                    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime){
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderTexture(0, TEXTURE);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
                        List<OrderedText> list = manager.getGame().textRenderer.wrapLines(lore.title(), 125);
                        int color = 0xFFFF00;
                        if(list.size() == 1){
                            manager.getGame().textRenderer.draw(matrices, Text.of("TODO"), 30.0F, 7.0F, color | 0xFF000000);
                            manager.getGame().textRenderer.draw(matrices, list.get(0), 30.0F, 18.0F, -1);
                        }else{
                            int l;
                            if(startTime < 1500L){
                                l = MathHelper.floor(MathHelper.clamp((float)(1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                                manager.getGame().textRenderer.draw(matrices, Text.of("TODO"), 30.0F, 11.0F, color | l);
                            }else{
                                l = MathHelper.floor(MathHelper.clamp((float)(startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                                int var10000 = this.getHeight() / 2;
                                int var10001 = list.size();
                                Objects.requireNonNull(manager.getGame().textRenderer);
                                int m = var10000 - var10001 * 9 / 2;
                                
                                for(Iterator<OrderedText> var12 = list.iterator(); var12.hasNext(); m += 9){
                                    OrderedText orderedText = var12.next();
                                    manager.getGame().textRenderer.draw(matrices, orderedText, 30.0F, (float)m, 16777215 | l);
                                    Objects.requireNonNull(manager.getGame().textRenderer);
                                }
                            }
                        }
        
/*
                            if (!soundPlayed && startTime > 0L) {
                                soundPlayed = true;
                                if(advancementDisplay.getFrame() == AdvancementFrame.CHALLENGE) {
                                    manager.getGame().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
                                }
                            }
*/
    
                        manager.getGame().getItemRenderer().renderInGui(lore.stack(), 8, 8);
                        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
                    }
                });
            });
        }
        
        @Override
        public @NotNull LoreStatus getLoreStatus(@NotNull Identifier lore){
            if(lore.equals(AetherLore.ROOT_ID)){
                return LoreStatus.COMPLETED;
            }
            
            return loreStates.getOrDefault(lore, LoreStatus.HIDDEN);
        }
        
        @Override
        public void setLoreStatus(@NotNull Identifier identifier, @NotNull LoreStatus status){
            Objects.requireNonNull(identifier, "identifier was null");
            Objects.requireNonNull(status, "status was null");
            
            if(!isClient){
                throw new IllegalStateException("setLoreStatus was called on the client");
            }
            
            var existing = loreStates.put(identifier, status);
            if(existing != status){
                dirtyLore.add(identifier);
                
                if(status == LoreStatus.COMPLETED){
                    getChildren(identifier).forEach((child)->{
                        if(getLoreStatus(child) == LoreStatus.HIDDEN){
                            setLoreStatus(child, LoreStatus.LOCKED);
                        }
                    });
                }
                
                AetherComponents.LORE_STATE.sync(player);
            }
        }
    
        private static final Map<Identifier, Set<Identifier>> CHILDREN = new HashMap<>();
        public Set<Identifier> getChildren(Identifier identifier){
            var children = CHILDREN.get(identifier);
            if(children != null){
                return children;
            }
        
            Set<Identifier> newChildren = new HashSet<>();
        
            LORE_REGISTRY.forEach((entry)->{
                if(entry.prerequisites().contains(identifier)){
                    newChildren.add(LORE_REGISTRY.getId(entry));
                }
            });
    
            children = newChildren.isEmpty() ? Set.of() : Collections.unmodifiableSet(newChildren);
            CHILDREN.put(identifier, children);
            return children;
        }
    }
}
