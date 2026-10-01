package net.id.paradise_lost.item.misc;

import net.id.paradise_lost.api.MoaAPI;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.item.ParadiseLostDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import java.util.List;

public class MoaEggItem extends Item {
    public MoaEggItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext contextIn) {
        Level world = contextIn.getLevel();
        Player player = contextIn.getPlayer();
        ItemStack stack = contextIn.getItemInHand();
        if (player != null && stack.getComponents().has(ParadiseLostDataComponentTypes.MOA_GENES) && player.isCreative()) {
            MoaEntity moa = EntityRegistry.MOA.get().create(world);
            ParadiseLostDataComponentTypes.MoaGeneComponent geneTag = stack.get(ParadiseLostDataComponentTypes.MOA_GENES);
            moa.getGenes().fromComponent(geneTag);
            if (geneTag.isBaby()) {
                moa.setAge(-43200);
            }
            moa.moveTo(contextIn.getClickedPos().above(), 0, 0);
            moa.setHealth(moa.getGenes().getAttribute(MoaAttributes.MAX_HEALTH));
            moa.syncGenes();
            world.addFreshEntity(moa);
            return InteractionResult.sidedSuccess(world.isClientSide());
        }
        return super.useOn(contextIn);
    }

    @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        var genes = stack.getOrDefault(ParadiseLostDataComponentTypes.MOA_GENES, null);
        if (genes != null) {
            ResourceLocation raceId = genes.race();
            var race = MoaAPI.getRace(raceId);
            if (raceId != null) {
                tooltip.add(Component.translatable(race.getTranslationKey()).withStyle(race.legendary() ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_AQUA));
            }
            if (!genes.isBaby()) {
                tooltip.add(Component.translatable("moa.egg.adult").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            }
        }
        super.appendHoverText(stack, context, tooltip, type);
    }
}
