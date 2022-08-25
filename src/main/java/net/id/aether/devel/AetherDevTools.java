package net.id.aether.devel;

import com.mojang.logging.LogUtils;
import net.id.aether.Aether;
import net.id.aether.entities.block.SliderEntity;
import net.id.aether.items.AetherItemGroups;
import net.id.incubus_core.devel.Devel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;

/**
 * Tools for aether development, such as blocks and items
 */
// Package-private to avoid being called in production
public class AetherDevTools {
    /**
     * Spawns a slider
     */
    private static final Item SLIDER_TEST_ITEM = new Item(new Item.Settings().group(AetherItemGroups.AETHER_MISC)) {
        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            var slider = new SliderEntity(context.getWorld(), new BlockPos(context.getHitPos()));
            slider.moveTime = 1;
            context.getWorld().spawnEntity(slider);
            return ActionResult.SUCCESS;
        }
    };

    public static void init() {
        Registry.register(Registry.ITEM, Aether.locate("slider_test_item"), SLIDER_TEST_ITEM);
    }

    static {
        if (!Devel.isDevel()) {
            Aether.LOG.error(LogUtils.FATAL_MARKER, "!!\n!!\n!!\n!!AetherDevItems called in production environment! Please report this to Paradise Lost developers!");
            new RuntimeException("").printStackTrace();
        }
    }
}
