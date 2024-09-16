package net.id.paradiselost.items.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.api.MoaAPI;
import net.id.paradiselost.api.MoaAPI.MoaRace;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
        if (player != null && stack.getOrCreateNbt().contains("genes") && player.isCreative()) {
            MoaEntity moa = ParadiseLostEntityTypes.MOA.create(world);
            NbtCompound geneTag = stack.getSubNbt("genes");
            boolean baby = geneTag.getBoolean("baby");
            moa.getGenes().readFromNbt(geneTag);
            if (baby) {
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, net.minecraft.item.Item.TooltipContext context) {
        if (stack.getOrCreateNbt().contains("genes")) {
            NbtCompound geneTag = stack.getSubNbt("genes");
            Identifier raceId = Identifier.tryParse(geneTag.getString("raceId"));
            var race = MoaAPI.getRace(raceId);
            if (raceId != null) {
                tooltip.add(Text.translatable(race.getTranslationKey()).formatted(race.legendary() ? Formatting.LIGHT_PURPLE : Formatting.DARK_AQUA));
            }
            if (!geneTag.getBoolean("baby")) {
                tooltip.add(Text.translatable("moa.egg.adult").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
