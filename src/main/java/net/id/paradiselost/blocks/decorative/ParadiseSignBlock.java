package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.ParadiseSignBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ParadiseSignBlock extends SignBlock {

    public ParadiseSignBlock(Settings settings, WoodType woodType) {
        super(settings, woodType);
        this.lootTableId = null;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        Identifier identifier = Registries.BLOCK.getId(this.asBlock()); // this is stupid
        this.lootTableId = identifier.withPrefixedPath("blocks/");
        return new ParadiseSignBlockEntity(pos, state);
    }

}
