package net.id.paradiselost.mixin.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.id.paradiselost.world.ExplosionExtensions;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Explosion.class)
public abstract class ExplosionMixin extends Object implements ExplosionExtensions {

    @Shadow
    @Final
    private boolean createFire;

    @Shadow
    @Final
    private double x;
    @Shadow
    @Final
    private double y;
    @Shadow
    @Final
    private double z;

    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private float power;

    @Shadow
    @Final
    private Random random;

    @Shadow
    @Final
    private World world;

    @Shadow
    @Final
    private Explosion.DestructionType destructionType;

    @Shadow
    @Final
    private ObjectArrayList<BlockPos> affectedBlocks;

    @Shadow
    public abstract LivingEntity getCausingEntity();

    public float getPower() {
        return this.power;
    }

    public void affectWorld(boolean particles, SoundEvent customSound) {
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, customSound, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean bl = this.destructionType != Explosion.DestructionType.KEEP;
        if (particles) {
            if (!(this.power < 2.0F) && bl) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }

        if (bl) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList();
            boolean bl2 = this.getCausingEntity() instanceof PlayerEntity;
            Util.shuffle(this.affectedBlocks, this.world.random);
            ObjectListIterator var5 = this.affectedBlocks.iterator();

            while (var5.hasNext()) {
                BlockPos blockPos = (BlockPos) var5.next();
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!blockState.isAir()) {
                    BlockPos blockPos2 = blockPos.toImmutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (block.shouldDropItemsOnExplosion((Explosion) (Object) this)) {
                        World var11 = this.world;
                        if (var11 instanceof ServerWorld) {
                            ServerWorld serverWorld = (ServerWorld) var11;
                            BlockEntity blockEntity = blockState.hasBlockEntity() ? this.world.getBlockEntity(blockPos) : null;
                            LootContext.Builder builder = (new LootContext.Builder(serverWorld)).random(this.world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, this.entity);
                            if (this.destructionType == Explosion.DestructionType.DESTROY) {
                                builder.parameter(LootContextParameters.EXPLOSION_RADIUS, this.power);
                            }

                            blockState.onStacksDropped(serverWorld, blockPos, ItemStack.EMPTY, bl2);
                            blockState.getDroppedStacks(builder).forEach((stack) -> {
                                tryMergeStack(objectArrayList, stack, blockPos2);
                            });
                        }
                    }

                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                    block.onDestroyedByExplosion(this.world, blockPos, (Explosion) (Object) this);
                    this.world.getProfiler().pop();
                }
            }

            var5 = objectArrayList.iterator();

            while (var5.hasNext()) {
                Pair<ItemStack, BlockPos> pair = (Pair) var5.next();
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
        }

        if (this.createFire) {

            for (BlockPos blockPos3 : this.affectedBlocks) {
                if (this.random.nextInt(3) == 0 && this.world.getBlockState(blockPos3).isAir() && this.world.getBlockState(blockPos3.down()).isOpaqueFullCube(this.world, blockPos3.down())) {
                    this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
                }
            }
        }

    }

    private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        int i = stacks.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = stacks.get(j);
            ItemStack itemStack = pair.getFirst();
            if (ItemEntity.canMerge(itemStack, stack)) {
                ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
                stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        stacks.add(Pair.of(stack, pos));
    }


}
