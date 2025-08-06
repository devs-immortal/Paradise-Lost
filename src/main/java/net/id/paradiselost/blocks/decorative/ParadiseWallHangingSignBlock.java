package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.blocks.blockentity.ParadiseHangingSignBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ParadiseWallHangingSignBlock extends WallHangingSignBlock {

    public ParadiseWallHangingSignBlock(WoodType woodType, Settings settings) {
        super(woodType, settings);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        Identifier identifier = Registries.BLOCK.getId(this.asBlock()); // this is stupid
        this.lootTableKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, identifier.withPrefixedPath("blocks/"));
        return new HangingSignBlockEntity(pos, state);
    }

}
