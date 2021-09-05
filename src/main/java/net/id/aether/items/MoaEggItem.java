package net.id.aether.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.api.MoaAPI;
import net.id.aether.api.MoaAttributes;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.passive.MoaEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoaEggItem extends Item {
    public MoaEggItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext contextIn) {
        World world = contextIn.getWorld();
        PlayerEntity player = contextIn.getPlayer();
        ItemStack stack = contextIn.getStack();
        if (player != null && stack.getOrCreateNbt().contains("genes") && player.isCreative()) {
            MoaEntity moa = AetherEntityTypes.MOA.create(world);
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateNbt().contains("genes")) {
            NbtCompound geneTag = stack.getSubNbt("genes");
            Identifier raceId = Identifier.tryParse(geneTag.getString("raceId"));
            var race = MoaAPI.getRace(raceId);
            if (raceId != null) {
                tooltip.add(new TranslatableText(MoaAPI.formatForTranslation(raceId)).formatted(race.legendary() ? Formatting.LIGHT_PURPLE : Formatting.DARK_AQUA));
            }
            if (!geneTag.getBoolean("baby")) {
                tooltip.add(new TranslatableText("moa.egg.adult").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}