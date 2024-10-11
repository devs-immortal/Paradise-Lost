package net.id.paradiselost.blocks.natural.tree;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class FruitingLeavesBlock extends ParadiseLostLeavesBlock {

    public static final IntProperty GROWTH = IntProperty.of("growth", 0, 2);
    public static final BooleanProperty CAPPED = BooleanProperty.of("capped");
    public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
    private final Supplier<Item> fruit;

    public FruitingLeavesBlock(Settings settings, Supplier<Item> fruit) {
        super(settings);
        this.fruit = fruit;
        setDefaultState(getDefaultState().with(GROWTH, 0).with(CAPPED, false).with(NATURAL, false));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(NATURAL)) {
            int growth = state.get(GROWTH);
            if (!state.get(CAPPED)) {
                if (random.nextInt(60) == 0 && growth < 2) {
                    if (growth == 1) {
                        if (world.isClient()) {
                            spawnPetalBurst(world, random, pos);
                        }
                    }
                    state = state.with(GROWTH, growth + 1).with(CAPPED, random.nextDouble() < 0.45 || growth + 1 == 2);
                    world.playSound(null, pos, SoundEvents.BLOCK_MOSS_BREAK, SoundCategory.BLOCKS, 1.25F, 1.5F);
                    world.setBlockState(pos, state);
                }
            } else {
                if (random.nextInt(240) == 0) {
                    if (growth == 1) {
                        if (world.isClient()) {
                            spawnPetalBurst(world, random, pos);
                        }
                    } else {
                        int dropBlocks = 0;
                        while (!world.isAir(pos.down(dropBlocks + 1)) && dropBlocks < 16 && world.getBlockState(pos.down(dropBlocks)).isOf(this)) {
                            dropBlocks++;
                        }
                        ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() - (dropBlocks + 0.25), pos.getZ() + 0.5, new ItemStack(fruit.get()));
                        world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_BREAK, SoundCategory.BLOCKS, 1F, 1F);
                    }
                    world.setBlockState(pos, getDefaultState().with(DISTANCE, state.get(DISTANCE)));
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Random random = world.getRandom();
        int growth = state.get(GROWTH);

        if (growth > 0) {
            world.setBlockState(pos, state.with(GROWTH, 0).with(CAPPED, false));
            world.playSound(null, pos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1F, 2F);

            if (growth == 1) {
                if (world.isClient()) {
                    spawnPetalBurst(world, random, pos);
                }
            } else {
                ItemStack drops = new ItemStack(fruit.get(), random.nextInt(1 + random.nextInt(1)) + 1);
                if (!player.giveItemStack(drops)) {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), drops);
                }
            }
            return ActionResult.success(world.isClient());
        }

        return super.onUse(state, world, pos, player, hit);
    }

    private void spawnPetalBurst(World world, Random random, BlockPos pos) {
        for (int i = 0; i < random.nextInt(9) + 5; i++) {
            world.addParticle(ParadiseLostParticles.FALLING_ORANGE_PETAL, (double) pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), (double) pos.getZ() + random.nextDouble(), speed, world.getRandom().nextDouble() / -20.0, 0);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(GROWTH) == 1 && random.nextInt(50) == 0) {
            Direction direction = Direction.DOWN;
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!(!blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite()) && !blockState.isTransparent(world, blockPos))) {

                if (speed == 0 || world.getTime() % 3000 == 0) {
                    speed = world.getRandom().nextInt(4);
                    if (world.isRaining()) {
                        speed += 1;
                    } else if (world.isThundering()) {
                        speed += 2;
                    }
                }

                for (int leaf = 0; leaf < random.nextInt(2) + 3; leaf++) {
                    if (world.random.nextInt(3) == 0) {
                        double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
                        double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
                        world.addParticle(ParadiseLostParticles.FALLING_ORANGE_PETAL, (double) pos.getX() + d, pos.getY(), (double) pos.getZ() + f, speed, world.getRandom().nextDouble() / -20.0, 0);
                    }
                }
            }
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(GROWTH, CAPPED, NATURAL);
    }
}
