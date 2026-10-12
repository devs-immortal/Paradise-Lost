package net.id.paradise_lost.networking.packet;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.blockentity.FoodBowlBlockEntity;
import net.id.paradise_lost.block.mechanical.FoodBowlBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public record FoodBowlInteractC2SPacket(BlockPos pos, boolean mainHand, boolean dumpStack)
        implements C2SModPacket<RegistryFriendlyByteBuf> {
    public static final Type<FoodBowlInteractC2SPacket> TYPE = new Type<>(ModConstants.id("food_bowl_interact"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodBowlInteractC2SPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, FoodBowlInteractC2SPacket::pos,
            ByteBufCodecs.BOOL, FoodBowlInteractC2SPacket::mainHand,
            ByteBufCodecs.BOOL, FoodBowlInteractC2SPacket::dumpStack,
            FoodBowlInteractC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (!player.level().isLoaded(pos) || !player.canInteractWithBlock(pos, 1.0)) {
            return;
        }
        BlockState state = player.level().getBlockState(pos);
        if (!(state.getBlock() instanceof FoodBowlBlock)) {
            return;
        }
        BlockEntity be = player.level().getBlockEntity(pos);
        if (!(be instanceof FoodBowlBlockEntity foodBowl)) {
            return;
        }
        InteractionHand hand = mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        ItemStack handStack = player.getItemInHand(hand);
        foodBowl.handleUse(player, hand, handStack, dumpStack);
    }
}
