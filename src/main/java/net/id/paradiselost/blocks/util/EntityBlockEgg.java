package net.id.paradiselost.blocks.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.BiFunction;

public class EntityBlockEgg extends Block {
    private final BiFunction<World, BlockPos, ? extends LivingEntity> function;

    public EntityBlockEgg(Settings settings, BiFunction<World, BlockPos, ? extends LivingEntity> function) {
        super(settings);
        this.function = function;
    }

    public EntityBlockEgg(Settings settings, EntityType<? extends LivingEntity> type) {
        super(settings);
        this.function = (world, pos) -> {
            LivingEntity entity = type.create(world);
            if (entity != null) {
                entity.setPosition(Vec3d.of(pos));
            }
            return entity;
        };
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.isCreative()) {
            world.spawnEntity(function.apply(world, pos));
        }
        world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        return state;
    }
}
