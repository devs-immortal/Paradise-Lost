package net.id.paradiselost.items.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.api.MoaAPI;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.items.utils.ParadiseLostDataComponentTypes;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class MoaEggItem extends Item {
    public MoaEggItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext contextIn) {
        World world = contextIn.getWorld();
        PlayerEntity player = contextIn.getPlayer();
        ItemStack stack = contextIn.getStack();
        if (player != null && stack.getComponents().contains(ParadiseLostDataComponentTypes.MOA_GENES) && player.isCreative()) {
            MoaEntity moa = ParadiseLostEntityTypes.MOA.create(world);
            ParadiseLostDataComponentTypes.MoaGeneComponent geneTag = stack.get(ParadiseLostDataComponentTypes.MOA_GENES);
            moa.getGenes().fromComponent(geneTag);
            if (geneTag.isBaby()) {
                moa.setBreedingAge(-43200);
            }
            moa.refreshPositionAndAngles(contextIn.getBlockPos().up(), 0, 0);
            moa.setHealth(moa.getGenes().getAttribute(MoaAttributes.MAX_HEALTH));
            world.spawnEntity(moa);
            return ActionResult.success(world.isClient());
        }
        return super.useOnBlock(contextIn);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, net.minecraft.item.Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        var genes = stack.getOrDefault(ParadiseLostDataComponentTypes.MOA_GENES, null);
        if (genes != null) {
            Identifier raceId = genes.race();
            var race = MoaAPI.getRace(raceId);
            if (raceId != null) {
                tooltip.add(Text.translatable(race.getTranslationKey()).formatted(race.legendary() ? Formatting.LIGHT_PURPLE : Formatting.DARK_AQUA));
            }
            if (!genes.isBaby()) {
                tooltip.add(Text.translatable("moa.egg.adult").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
