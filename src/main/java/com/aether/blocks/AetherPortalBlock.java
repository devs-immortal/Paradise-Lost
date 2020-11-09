package com.aether.blocks;

import com.aether.util.AetherEntity;
import com.aether.world.dimension.AetherDimension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.dimension.AreaHelper;

import java.util.Random;

public class AetherPortalBlock extends AbstractGlassBlock {
    public static final EnumProperty<Direction.Axis> AXIS;
    protected static final VoxelShape X_SHAPE;
    protected static final VoxelShape Z_SHAPE;

    static {
        AXIS = Properties.HORIZONTAL_AXIS;
        X_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
        Z_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    }

    public AetherPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(AXIS)) {
            case Z:
                return Z_SHAPE;
            case X:
            default:
                return X_SHAPE;
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis axis2 = state.get(AXIS);
        boolean bl = axis2 != axis && axis.isHorizontal();
        return !bl && !newState.isOf(this) && !(new AreaHelper(world, pos, axis2)).wasAlreadyValid() ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && !entity.hasVehicle() && !entity.hasPassengers() && ((AetherEntity) entity).canUseAetherPortal()) {
            BlockPos target;
            ServerWorld serverWorld = (ServerWorld) world;
            RegistryKey<World> targetRegKey = world.getRegistryKey() != AetherDimension.AETHER_WORLD_KEY ? AetherDimension.AETHER_WORLD_KEY : World.OVERWORLD;
            ServerWorld targetWorld = serverWorld.getServer().getWorld(targetRegKey);
            if(targetRegKey == AetherDimension.AETHER_WORLD_KEY)
                target = new BlockPos(entity.getX() / 8, entity.getY(), entity.getZ() / 8);
            else
                target = new BlockPos(entity.getX() * 8, entity.getY(), entity.getZ() * 8);
            int y = 64;
            BlockPos portalPos = tryFindPortal(targetWorld, target);
            if(portalPos != null) {
                target = portalPos;
                y = target.getY();
            }
            else {
                boolean floored = true;
                for(int i = targetWorld.getHeight(); i > 0; i--) {
                    final BlockPos probePos = new BlockPos(target.getX(), i, target.getZ());
                    final BlockState probeState = targetWorld.getBlockState(probePos);
                    if(!(probeState.isAir() || probeState.isTranslucent(targetWorld, probePos))) {
                        y = i + 1;
                        floored = false;
                        break;
                    }
                }
                buildPortal(targetWorld, new BlockPos(target.getX(), y, target.getZ()), floored);
            }
            ChunkPos chunkPos = new ChunkPos(target);
            if(entity instanceof ServerPlayerEntity) {
                serverWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, entity.getEntityId());
                ((ServerPlayerEntity) entity).teleport(targetWorld, target.getX(), y + 1, target.getZ(), entity.yaw, entity.pitch);
            }
            ((AetherEntity) entity).setPortalReady(false);
        }
    }

    private BlockPos tryFindPortal(ServerWorld world, BlockPos pos) {
        world.getBlockState(pos);
        ChunkSection chunk = world.getChunk(pos).getSectionArray()[pos.getY() >> 4];
        if(chunk != null && !chunk.isEmpty())
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    for (int k = 0; k < 16; k++) {
                        BlockState state = chunk.getBlockState(k, i, j);
                        if(state.isOf(AetherBlocks.BLUE_PORTAL)) {
                            BlockPos checkPos = new BlockPos(k, i, j);
                            while(state.isOf(AetherBlocks.BLUE_PORTAL)) {
                                checkPos.down();
                                state = world.getBlockState(checkPos);
                            }
                            return checkPos;
                        }
                    }
                }
            }
        return null;
    }

    private void buildPortal(ServerWorld world, BlockPos pos, boolean floored) {
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                world.setBlockState(pos.up(i).north(j), Blocks.GLOWSTONE.getDefaultState());
            }
        }
        pos = pos.up(1).north(1);
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                world.setBlockState(pos.up(i).north(j), AetherBlocks.BLUE_PORTAL.getDefaultState().with(AXIS, Direction.Axis.Z));
            }
        }
        if(floored) {
            pos = pos.down(2).south(5).west(4);
            for(int i = 0; i < 8; i++) {
                for (int j = 0; j < 11; j++) {
                    BlockPos placePos = pos.north(i).east(j);
                    int type = world.getRandom().nextInt(12);
                    if(type >= 9) {
                        world.setBlockState(placePos, AetherBlocks.MOSSY_HOLYSTONE.getDefaultState());
                    }
                    else
                        world.setBlockState(placePos, AetherBlocks.HOLYSTONE.getDefaultState());
                }
            }
        }
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
        if(world.getRandom().nextInt(6) != 0) {
            world.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, g, h, j);
        }
        else
            world.addParticle(ParticleTypes.CLOUD, d, e, f, 0, 0, 0);

    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.get(AXIS)) {
                    case Z:
                        return state.with(AXIS, Direction.Axis.X);
                    case X:
                        return state.with(AXIS, Direction.Axis.Z);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}