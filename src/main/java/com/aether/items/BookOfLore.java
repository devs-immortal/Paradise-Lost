package com.aether.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BookOfLore extends Item {
    public BookOfLore(Properties settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.clear();
        tooltip.add((new TranslatableComponent("item.the_aether.lore_book")).withStyle(ChatFormatting.YELLOW));
        tooltip.add((new TranslatableComponent("book.edition", "1")).withStyle(ChatFormatting.GOLD));
        tooltip.add((new TranslatableComponent("book.byAuthor", "Immortal Devs")).withStyle(ChatFormatting.GRAY));
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        // TODO: Open the book

        return InteractionResultHolder.pass(itemStack);
    }
}
