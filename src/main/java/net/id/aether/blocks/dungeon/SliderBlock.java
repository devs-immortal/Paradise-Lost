package net.id.aether.blocks.dungeon;

import net.id.aether.entities.block.SliderEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SliderBlock extends Block {
    public static final EnumProperty<SliderEntity.State> STATE = EnumProperty.of("slider_state", SliderEntity.State.class);

    public SliderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        world.spawnEntity(new SliderEntity(world, pos));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }
}
