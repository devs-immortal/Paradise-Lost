package com.aether.entities.projectile;

import com.aether.Aether;
import com.aether.entities.AetherEntityTypes;
import com.aether.entities.util.AetherPoisonMovement;
import com.aether.items.AetherItems;
import io.netty.buffer.Unpooled;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PoisonDartEntity extends DartEntity {

    private LivingEntity victim;
    private AetherPoisonMovement poison;

    public PoisonDartEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level world) {
        super(entityType, x, y, z, world);
        this.setBaseDamage(0);
    }

    public PoisonDartEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level world) {
        super(entityType, owner, world);
        this.setBaseDamage(0);
    }

    public PoisonDartEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.setBaseDamage(0);
    }

    public PoisonDartEntity(double x, double y, double z, Level world) {
        this(AetherEntityTypes.POISON_DART, x, y, z, world);
    }

    public PoisonDartEntity(LivingEntity owner, Level world) {
        this(AetherEntityTypes.POISON_DART, owner, world);
    }

    public PoisonDartEntity(Level world) {
        this(AetherEntityTypes.POISON_DART, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.victim != null) {
            if (!this.victim.isAlive() || this.poison.ticks == 0) {
                this.outOfWorld();
                return;
            }

            if (this.getOwner() != null) if (this.getOwner().level instanceof ServerLevel)
                ((ServerLevel) this.getOwner().level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.RED_DYE)), this.victim.getX(), this.victim.getBoundingBox().minY + this.victim.getBbHeight() * 0.8D, this.victim.getZ(), 2, 0.0D, 0.0D, 0.0D, 0.0625D);

            this.unsetRemoved();
            this.poison.onUpdate();
            this.setInvisible(true);
            this.setPos(this.victim.getX(), this.victim.getY(), this.victim.getZ());
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entityIn) {
        super.doPostHurtEffects(entityIn);

        if (entityIn instanceof ServerPlayer) {
            //AetherAPI.get((PlayerEntity) entityIn).inflictPoison(500);

            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());

            byteBuf.writeInt(500);

            ((ServerPlayer) entityIn).connection.send(new ServerboundCustomPayloadPacket(Aether.locate("poison"), byteBuf));
        } else {
            this.victim = entityIn;
            this.poison = new AetherPoisonMovement(this.victim);
            this.poison.inflictPoison(500);
            this.unsetRemoved();
        }
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        return this.victim == null ? super.findHitEntity(start, end) : null;
    }

    @Override
    public void playerTouch(Player playerIn) {
        if (this.victim == null) super.playerTouch(playerIn);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(AetherItems.POISON_DART);
    }
}