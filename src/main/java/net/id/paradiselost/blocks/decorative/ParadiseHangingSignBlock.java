package net.id.paradiselost.blocks.decorative;

import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseHangingSignBlock extends HangingSignBlock {
    public ParadiseHangingSignBlock(WoodType woodType, Settings settings) {
        super(woodType, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        FIXME: check if this is actually necessary, and if so, find a better way to do it
//        Identifier identifier = Registries.BLOCK.getId(this.asBlock()); // this is stupid
//        this.lootTableKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, identifier.withPrefixedPath("blocks/"));
        return new HangingSignBlockEntity(pos, state);
    }

}
