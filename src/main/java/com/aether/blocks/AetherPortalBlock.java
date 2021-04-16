package com.aether.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.portal.CustomAreaHelper;
import net.kyrptonaught.customportalapi.util.CustomTeleporter;
import net.kyrptonaught.customportalapi.util.EntityInCustomPortal;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Random;

public class AetherPortalBlock extends CustomPortalBlock {
    public AetherPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.getProfiler().push("portal");
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
            world.getProfiler().pop();
        }

        double d = (double) pos.getX() + random.nextDouble();
        double e = (double) pos.getY() + random.nextDouble();
        double f = (double) pos.getZ() + random.nextDouble();
        double g = ((double) random.nextFloat() - 0.5D) * 0.5D;
        double h = ((double) random.nextFloat() - 0.5D) * 0.5D;
        double j = ((double) random.nextFloat() - 0.5D) * 0.5D;
        int k = random.nextInt(2) * 2 - 1;
        if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
            d = (double) pos.getX() + 0.5D + 0.25D * (double) k;
            g = random.nextFloat() * 2.0F * (float) k;
        } else {
            f = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
            j = random.nextFloat() * 2.0F * (float) k;
        }
        if (world.getRandom().nextInt(6) != 0) world.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, g, h, j);
        else world.addParticle(ParticleTypes.CLOUD, d, e, f, 0, 0, 0);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis axis2 = state.get(AXIS);
        boolean bl = axis2 != axis && axis.isHorizontal();
        HashSet<Block> foundations = new HashSet<>();
        Block block = Blocks.GLOWSTONE;
        foundations.add(block);
        return !bl && !newState.isOf(this) && !(new CustomAreaHelper(world, pos, axis2, foundations)).wasAlreadyValid() ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        EntityInCustomPortal entityInPortal = (EntityInCustomPortal) entity;
        if (entity instanceof PlayerEntity) {
            if (!entityInPortal.didTeleport()) {
                entityInPortal.setInPortal(true);
                if (entityInPortal.getTimeInPortal() >= entity.getMaxNetherPortalTime()) {
                    entityInPortal.teleported();
                    if (!world.isClient)
                        CustomTeleporter.TPToDim(world, entity, Blocks.GLOWSTONE, pos);
                }
            } else entityInPortal.increaseCooldown();
        } else if (!world.isClient) {
            if (!entityInPortal.didTeleport()) {
                entityInPortal.teleported();
                CustomTeleporter.TPToDim(world, entity, Blocks.GLOWSTONE, pos);
            } else entityInPortal.increaseCooldown();

        }
    }
}
