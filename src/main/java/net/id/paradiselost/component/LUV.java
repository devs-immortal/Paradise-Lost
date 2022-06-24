package net.id.paradiselost.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * What are you doing here?
 * Dawn will soon break...
 * Child of light, this is not for you to see.
 *
 *
 * @author why, that is not for you to know, is it?
 */
public class LUV implements AutoSyncedComponent, CommonTickingComponent, PlayerComponent<LUV> {

    private PlayerEntity player;
    private byte value;

    public LUV(PlayerEntity player) {
        value = (byte) player.getRandom().nextInt(128);
        this.player = player;
    }

    public static LUV getLUV(PlayerEntity player) {
        return ParadiseLostComponents.LUV.get(player);
    }

    @Override
    public void tick() {
        var playerPos = player.getBlockPos();
        var world = player.getEntityWorld();
        var random = player.getRandom();

        if(value >= 36 || world.getMoonPhase() == 0) {
            handleRookSpawning(world, playerPos, random);
        }
    }

    public void handleRookSpawning(World world, BlockPos pos, Random random) {
//        var dayTime = world.getTimeOfDay();
//
//        var rookCount = (long) world.getEntitiesByClass(RookEntity.class, Box.of(Vec3d.ofCenter(pos), 64, 64, 64), entity -> true).size();
//        var rookCap = (value == 48 || value == 100 || value >= 126) ? 64 : 16;
//
//        var scaling = 0.5 + Math.sqrt(rookCount);
//        var luvModifier = 1.0;
//
//        if(world.getRegistryKey() == ParadiseLostDimension.PARADISE_LOST_WORLD_KEY) {
//            if(value == 48 || (value >= 0 && value < 30))
//                luvModifier = 2;
//            else if(value > 100 && value < 110)
//                luvModifier = 0.5;
//            else if(value < 0)
//                luvModifier = 0.25;
//        }
//        else {
//            luvModifier = 4;
//        }
//
//        if(rookCount < rookCap && world.getTime() % 20 == 0 && (dayTime > 14000 && dayTime < 22000)) {
//            var posStream = BlockPos.streamOutwards(pos, 32, 32, 32);
//            double finalLuvModifier = luvModifier;
//            posStream
//                    .filter(blockPos -> blockPos.getManhattanDistance(pos) > 22)
//                    .forEach(blockPos -> {
//                        var upPos = blockPos.up();
//                        var floorPos = blockPos.down();
//                        var state = world.getBlockState(blockPos);
//                        var upState = world.getBlockState(upPos);
//                        var floorState = blockPos.down();
//
//                        if(world.getLightLevel(LightType.BLOCK, blockPos) > 4 || state.isFullCube(world, blockPos) || upState.isFullCube(world, upPos) || world.isAir(floorPos))
//                            return;
//
//                        var roofed = 3;
//
//                        for (int i = 1; i <= 4; i++) {
//                            var checkPos = upPos.up(i);
//                            if(!world.getBlockState(checkPos).isTranslucent(world, checkPos)) {
//                                roofed = 1;
//                                break;
//                            }
//                        }
//
//                        if(random.nextInt((int) (30000 * roofed * scaling * finalLuvModifier)) == 0) {
//                            var rook = new RookEntity(ParadiseLostEntityTypes.ROOK, world);
//                            rook.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.1, blockPos.getZ() + 0.5);
//                            world.spawnEntity(rook);
//                        }
//                    });
//        }
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        value = tag.getByte("LUV");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putByte("LUV", value);
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
        return sameCharacter;
    }
}
