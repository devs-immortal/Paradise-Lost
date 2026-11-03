package net.id.paradise_lost.block.decorative;

import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CalciteDecoratedPotBlock extends DecoratedPotBlock {
    public CalciteDecoratedPotBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CalciteDecoratedPotBlockEntity(pos, state);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof DecoratedPotBlockEntity pot) {
            PotDecorations sherds = pot.getDecorations();
            builder.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, lootConsumer -> {
                for (Optional<Item> sherd : List.of(sherds.back(), sherds.left(), sherds.right(), sherds.front())) {
                    lootConsumer.accept(sherd.orElse(Items.CALCITE).getDefaultInstance());
                }
                lootConsumer.accept(Items.CALCITE.getDefaultInstance());
            });
        }

        LootParams params = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
        LootTable table = params.getLevel().getServer().reloadableRegistries().getLootTable(getLootTable());
        return table.getRandomItems(params);
    }
}
