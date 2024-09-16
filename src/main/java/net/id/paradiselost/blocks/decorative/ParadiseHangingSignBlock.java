package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.ParadiseHangingSignBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ParadiseHangingSignBlock extends HangingSignBlock {
    public ParadiseHangingSignBlock(Settings settings, WoodType woodType) {
        super(woodType, settings);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        Identifier identifier = Registries.BLOCK.getId(this.asBlock()); // this is stupid
        this.lootTableId = identifier.withPrefixedPath("blocks/");
        return new ParadiseHangingSignBlockEntity(pos, state);
    }
}
