package net.id.paradise_lost.block.mechanical;

import com.mojang.serialization.MapCodec;
import net.id.paradise_lost.block.blockentity.FoodBowlBlockEntity;
import net.id.paradise_lost.networking.packet.FoodBowlCtrlC2SPacket;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.id.paradise_lost.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FoodBowlBlock extends ParadiseLostBlockWithEntity {

    public static final MapCodec<FoodBowlBlock> CODEC = simpleCodec(FoodBowlBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty FULL = BooleanProperty.create("full");
    private final VoxelShape shapeZ = Block.box(0, 0, 1, 16, 8, 15);
    private final VoxelShape shapeX = Block.box(1, 0, 0, 15, 8, 16);

    public FoodBowlBlock(Properties settings) {
        super(settings, false);
        registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.X).setValue(FULL, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Sneak is reserved by vanilla; Ctrl dumps the stack (synced via packet before this interact).
        if (world.isClientSide()) {
            PacketHandler.sendToServer(new FoodBowlCtrlC2SPacket(Services.CLIENT.isControlKeyDown()));
        }
        if (world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
            ItemStack handStack = player.getItemInHand(hand);
            boolean dumpStack = player instanceof ServerPlayer serverPlayer
                    && FoodBowlCtrlC2SPacket.consumeDumpRequest(serverPlayer);
            boolean used = world.isClientSide()
                    ? state.getValue(FULL) || handStack.is(ItemTags.MEAT) || Services.CLIENT.isControlKeyDown()
                    : foodBowl.handleUse(player, hand, handStack, dumpStack);
            if (used) {
                return ItemInteractionResult.sidedSuccess(world.isClientSide());
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        // Empty-hand take-out (useItemOn is not used when both hands are empty).
        if (world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
            boolean used = world.isClientSide()
                    ? state.getValue(FULL)
                    : foodBowl.handleUse(player, InteractionHand.MAIN_HAND, ItemStack.EMPTY, false);
            if (used) {
                return InteractionResult.sidedSuccess(world.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && world.getBlockEntity(pos) instanceof FoodBowlBlockEntity foodBowl) {
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), foodBowl.getContainedItem());
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(AXIS, ctx.getHorizontalDirection().getAxis());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> shapeX;
            case Z -> shapeZ;
            default -> throw new IllegalStateException("Unexpected value: " + state.getValue(AXIS));
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoodBowlBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS, FULL);
    }
}
