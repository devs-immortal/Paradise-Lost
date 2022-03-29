package net.id.aether.mixin.entity;

import net.id.aether.entities.hostile.swet.SwetEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.objectweb.asm.Opcodes.ISTORE;

/**
 * Current modifications:
 * - Swet flag
 * - Overrides vanilla particle generation if this is a swet
 */
@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntity implements Monster {
    @Unique private boolean aether_isSwet;
    
    @SuppressWarnings("ConstantConditions")
    private SlimeEntityMixin() {
        super(null, null);
    }
    
    @SuppressWarnings("ConstantConditions")
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void init(EntityType<? extends SlimeEntity> entityType, World world, CallbackInfo ci){
        // Only do this once
        aether_isSwet = ((Object)this) instanceof SwetEntity;
    }
    
    /**
     * Vanilla Minecraft uses a loop that will not iterate if passed 0 for particle generation, we abuse that to
     * use a hook that can co-exist with other mods doing the same thing.
     *
     * If this is a swet we replace the size with 0 and call the custom swet callback.
     *
     * @param original The size of the slime
     * @return The original value or 0 if a swet
     */
    @ModifyVariable(
        method = "tick",
        at = @At(
            value = "STORE",
            opcode = ISTORE
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/mob/MobEntity;tick()V"
            ),
            to = @At(
                value = "INVOKE",
                target = "Ljava/util/Random;nextFloat()F"
            )
        )
    )
    private int overrideSize(int original){
        if(aether_isSwet){
            //noinspection ConstantConditions
            ((SwetEntity)(Object)this).spawnSwetParticles();
            return 0;
        }else{
            return original;
        }
    }
}
