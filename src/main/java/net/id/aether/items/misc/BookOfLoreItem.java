package net.id.aether.items.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.screen.handler.LoreHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BookOfLoreItem extends Item {
    public BookOfLoreItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add((Text.translatable("item.the_aether.lore_book")).formatted(Formatting.YELLOW));
        tooltip.add((Text.translatable("book.edition", "1")).formatted(Formatting.GOLD));
        tooltip.add((Text.translatable("book.byAuthor", "Immortal Devs")).formatted(Formatting.GRAY));
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.openHandledScreen(new NamedScreenHandlerFactory(){
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player){
                return new LoreHandler(syncId, inv);
            }
    
            @Override
            public Text getDisplayName(){
                return Text.of("asdf");
            }
        });
        return TypedActionResult.pass(itemStack);
    }
}
