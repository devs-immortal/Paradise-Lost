package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.ParadiseSignBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ParadiseWallSignBlock extends WallSignBlock {
    public ParadiseWallSignBlock(Settings settings, WoodType woodType) {
        super(woodType, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        FIXME: check if this is actually necessary, and if so, find a better way to do it
//        Identifier identifier = Registries.BLOCK.getId(this.asBlock()); // this is stupid
//        this.lootTableKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, identifier.withPrefixedPath("blocks/"));
        return new ParadiseSignBlockEntity(pos, state);
    }
}

