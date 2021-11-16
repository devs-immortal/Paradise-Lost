package net.id.aether.lore;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record LoreEntry<T>(
    int x,
    int y,
    @NotNull ItemStack stack,
    @NotNull Text title,
    @NotNull Text description,
    @NotNull LoreTriggerType triggerType,
    @NotNull Predicate<T> trigger,
    @NotNull Set<@NotNull Identifier> prerequisites
){
    public LoreEntry{
        Objects.requireNonNull(stack, "stack was null");
        Objects.requireNonNull(title, "title was null");
        Objects.requireNonNull(description, "description was null");
        Objects.requireNonNull(triggerType, "triggerType was null");
        Objects.requireNonNull(trigger, "trigger was null");
        Objects.requireNonNull(prerequisites, "prerequisites was null");
    }
    
    public LoreEntry(int x, int y, @NotNull ItemConvertible item, @NotNull String title, @NotNull String description, @NotNull LoreEntry<?> @NotNull ... prerequisites){
        this(
            x, y,
            new ItemStack(Objects.requireNonNull(item, "item was null")),
            new TranslatableText(Objects.requireNonNull(title, "title was null")),
            new TranslatableText(Objects.requireNonNull(description, "description was null")),
            LoreTriggerType.NONE, (ignored)->false,
            Stream.of(Objects.requireNonNull(prerequisites, "prerequisites was null"))
                .map(AetherRegistries.LORE_REGISTRY::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet())
        );
    }
    
    public LoreEntry(int x, int y, @NotNull ItemConvertible item, @NotNull String title, @NotNull String description, @NotNull LoreTriggerType triggerType, @NotNull Predicate<T> trigger, @NotNull LoreEntry<?> @NotNull ... prerequisites){
        this(
            x, y,
            new ItemStack(Objects.requireNonNull(item, "item was null")),
            new TranslatableText(Objects.requireNonNull(title, "title was null")),
            new TranslatableText(Objects.requireNonNull(description, "description was null")),
            triggerType, trigger,
            Stream.of(Objects.requireNonNull(prerequisites, "prerequisites was null"))
                .map(AetherRegistries.LORE_REGISTRY::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet())
        );
    }
    
}
