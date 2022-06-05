package net.id.aether.lore;

import net.id.aether.registry.AetherRegistries;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record LoreEntry<T>(
    int x,
    int y,
    @NotNull ItemStack stack,
    @NotNull LoreTriggerType triggerType,
    @NotNull Predicate<T> trigger,
    @NotNull Set<@NotNull LoreEntry<?>> prerequisites
){
    public LoreEntry{
        Objects.requireNonNull(stack, "stack was null");
        Objects.requireNonNull(triggerType, "triggerType was null");
        Objects.requireNonNull(trigger, "trigger was null");
        Objects.requireNonNull(prerequisites, "prerequisites was null");
    }
    
    public LoreEntry(int x, int y, @NotNull ItemConvertible item, @NotNull LoreEntry<?> @NotNull ... prerequisites){
        this(
            x, y,
            new ItemStack(Objects.requireNonNull(item, "item was null")),
            LoreTriggerType.NONE, (ignored)->false,
            Stream.of(Objects.requireNonNull(prerequisites, "prerequisites was null"))
                .collect(Collectors.toUnmodifiableSet())
        );
    }
    
    public LoreEntry(int x, int y, @NotNull ItemConvertible item, @NotNull LoreTriggerType triggerType, @NotNull Predicate<T> trigger, @NotNull LoreEntry<?> @NotNull ... prerequisites){
        this(
            x, y,
            new ItemStack(Objects.requireNonNull(item, "item was null")),
            triggerType, trigger,
            Stream.of(Objects.requireNonNull(prerequisites, "prerequisites was null"))
                .collect(Collectors.toUnmodifiableSet())
        );
    }

    public Text getTitleText(){
        Identifier id = AetherRegistries.LORE_REGISTRY.getId(this);
        return Text.translatable("lore." + id.getNamespace() + "." + id.getPath() + ".title");
    }

    public Text getDescriptionText(){
        Identifier id = AetherRegistries.LORE_REGISTRY.getId(this);
        return Text.translatable("lore." + id.getNamespace() + "." + id.getPath() + ".description");
    }

    public Identifier getId(){
        return AetherRegistries.LORE_REGISTRY.getId(this);
    }
}
