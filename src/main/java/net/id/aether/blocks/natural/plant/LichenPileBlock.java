package net.id.aether.blocks.natural.plant;

import net.id.aether.api.ConditionAPI;
import net.id.aether.component.ConditionManager;
import net.id.aether.effect.condition.Conditions;
import net.id.aether.effect.condition.Persistence;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LichenPileBlock extends FallingBlock implements Fertilizable {

    public static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final VoxelShape WALKING_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    private final boolean venomous;

    public LichenPileBlock(Settings settings, boolean venomous) {
        super(settings);
        this.venomous = venomous;
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(venomous) {
            entity.slowMovement(state, new Vec3d(0.925D, 1D, 0.925D));
            if(entity instanceof LivingEntity livingEntity) {
                ConditionManager manager = ConditionAPI.getConditionManager(livingEntity);
                manager.add(Conditions.VENOM, Persistence.TEMPORARY, 0.6F);
            }
        }
        else {
            entity.slowMovement(state, new Vec3d(0.975D, 1D, 0.975D));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return WALKING_SHAPE;
    }

    @Override
    protected int getFallDelay() {
        return 3;
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return venomous ? 0x4d333c : 0x503639;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos.streamOutwards(pos, 3, 1, 3)
                .filter(world::isAir)
                .filter(temp -> {
                    var floor = world.getBlockState(temp);
                    return floor.getBlock() instanceof LichenBlock || floor.isIn(AetherBlockTags.LICHEN_SPREADABLES);
                })
                .filter(temp -> random.nextInt(60) == 0)
                .forEach(spreadPoint -> world.setBlockState(spreadPoint, getDefaultState()));
    }
}
