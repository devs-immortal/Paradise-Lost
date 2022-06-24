package net.id.paradiselost.blocks.natural.plant;

import net.id.paradiselost.api.ConditionAPI;
import net.id.paradiselost.component.ConditionManager;
import net.id.paradiselost.effect.condition.Conditions;
import net.id.paradiselost.effect.condition.Persistence;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LichenBlock extends FallingBlock {

    public static final BooleanProperty ALLOW_FALL = BooleanProperty.of("allow_fall");
    public static final VoxelShape WALKING_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    private final boolean venomous;

    public LichenBlock(Settings settings, boolean venomous) {
        super(settings);
        this.venomous = venomous;
        setDefaultState(getDefaultState().with(ALLOW_FALL, false));
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0;
        if(venomous) {
            entity.slowMovement(state, new Vec3d(0.65D, 0.875D, 0.65D));
            if(entity instanceof LivingEntity livingEntity) {
                ConditionManager manager = ConditionAPI.getConditionManager(livingEntity);
                manager.add(Conditions.VENOM, Persistence.TEMPORARY, 1F);
            }
        }
        else {
            entity.slowMovement(state, new Vec3d(0.85D, 0.9D, 0.85D));
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        for(int i = random.nextInt(2); i < 3; i++) {
            BlockPos.streamOutwards(pos, 3, 2, 3)
                    .filter(temp -> world.getBlockState(temp).isIn(ParadiseLostBlockTags.LICHEN_SPREADABLES))
                    .filter(temp -> world.isAir(temp.up()) && world.getLightLevel(temp.up()) <= 9)
                    .filter(temp -> random.nextBoolean())
                    .findFirst()
                    .ifPresent(spreadPoint -> {
                        world.setBlockState(spreadPoint, getDefaultState());
                        world.playSound(null, spreadPoint, ParadiseLostSoundEvents.BLOCK_LICHEN_SPREADS, SoundCategory.BLOCKS, 1, 0.9F + random.nextFloat() / 10);
                    });
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return WALKING_SHAPE;
    }

    @Override
    public float getJumpVelocityMultiplier() {
        return 0.9F;
    }

    @Override
    public void onSteppedOn(World world, BlockPos topPos, BlockState state, Entity entity) {
        BlockPos pos = topPos.down(venomous ? 2 : 4);
        while(pos.getY() != topPos.getY() + 1) {
            tryFall(world, pos, state);
            pos = pos.up();
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(ALLOW_FALL)) {
            tryFall(world, pos, state);
        }
    }

    public void tryFall(World world, BlockPos pos, BlockState state) {
        if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()) {
            if (state.getPistonBehavior().equals(PistonBehavior.BLOCK) || state.getBlock().getHardness() == -1F){
                return;
            }
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, pos, state);
            this.configureFallingBlockEntity(fallingBlockEntity);

            BlockPos.iterateOutwards(pos, 1, 0, 1).forEach(checkPos -> {
                var checkState = world.getBlockState(checkPos);
                if(!checkPos.equals(pos) && checkState.isOf(this)) {
                    world.setBlockState(checkPos, checkState.with(ALLOW_FALL, true));
                }
            });
        }
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ALLOW_FALL);
    }
}
