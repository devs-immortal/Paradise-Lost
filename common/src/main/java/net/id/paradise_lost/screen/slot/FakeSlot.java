package net.id.paradise_lost.screen.slot;

import net.id.paradise_lost.util.DummyInventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FakeSlot extends Slot {
    private final Supplier<ItemStack> getter;
    private final Consumer<ItemStack> setter;
    private final Predicate<ItemStack> filter;

    public FakeSlot(int x, int y, Supplier<ItemStack> getter, Consumer<ItemStack> setter, Predicate<ItemStack> filter) {
        super(new DummyInventory(), 0, x, y);
        this.getter = getter;
        this.setter = setter;
        this.filter = filter;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.isEmpty() || filter.test(stack);
    }

    @Override
    public ItemStack getItem() {
        return getter.get();
    }

    @Override
    public boolean hasItem() {
        return !getItem().isEmpty();
    }

    @Override
    public void setByPlayer(ItemStack stack, ItemStack previousStack) {
        if (stack.equals(previousStack)) {
            return;
        }
        setter.accept(stack);
        setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        var existing = getItem();
        var result = existing.split(amount);
        setByPlayer(existing);
        return result;
    }

    @Override
    public void setChanged() {
    }
}
