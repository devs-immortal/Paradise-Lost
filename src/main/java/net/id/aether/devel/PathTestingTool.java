package net.id.aether.devel;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.util.AStarManager;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PathTestingTool extends Item {

    public PathTestingTool(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient) {
            var world = context.getWorld();
            var random = world.getRandom();
            var starBuilder = AStarManager.createBuilder();

            var start = context.getBlockPos();
            var end = start.add(random.nextInt(20) + 3, random.nextInt(20) + 3, random.nextInt(20) + 3);

            starBuilder.start(AStarManager.BlockPosProvider.simple(start));
            starBuilder.goal(AStarManager.BlockPosProvider.simple(end));
            starBuilder.costMapper(AStarManager.Builder.c_favorReplaceable);
            starBuilder.expectedLength(100);
            //starBuilder.heuristic(1.1);
            var pather = starBuilder.build(world);

            pather.compute();

            pather.getLastOutput().ifPresent(pathingOutput -> {

                pathingOutput.checkedBlocks().forEach(blockPos -> {
                    if(world.getBlockState(blockPos).getMaterial().isReplaceable()) {
                        world.setBlockState(blockPos, Blocks.GLASS.getDefaultState());
                    }
                    else {
                        world.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState());
                    }
                });

                var node = pathingOutput.path().get(0);
                while(node.parent() != null) {
                    world.setBlockState(node.pos(), world.getBlockState(node.pos()).getMaterial().isReplaceable() || world.getBlockState(node.pos()).isOf(Blocks.GLASS) ? AetherBlocks.AEROGEL.getDefaultState() : AetherBlocks.QUICKSOIL_GLASS.getDefaultState());
                    node = node.parent();
                }

                world.setBlockState(node.pos(), Blocks.GLOWSTONE.getDefaultState());
                world.setBlockState(pathingOutput.path().get(0).pos(), Blocks.SEA_LANTERN.getDefaultState());
            });
        }
        return super.useOnBlock(context);
    }
}
