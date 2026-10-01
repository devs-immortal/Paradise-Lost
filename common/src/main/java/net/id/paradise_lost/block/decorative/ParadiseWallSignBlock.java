package net.id.paradise_lost.block.decorative;

import net.id.paradise_lost.block.blockentity.ParadiseSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ParadiseWallSignBlock extends WallSignBlock {
    public ParadiseWallSignBlock(Properties settings, WoodType woodType) {
        super(woodType, settings);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(this.asBlock());
        this.drops = ResourceKey.create(Registries.LOOT_TABLE, identifier.withPrefix("blocks/"));
        return new ParadiseSignBlockEntity(pos, state);
    }
}
