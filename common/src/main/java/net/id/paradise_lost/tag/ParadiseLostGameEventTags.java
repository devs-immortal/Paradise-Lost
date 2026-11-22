package net.id.paradise_lost.tag;

import net.id.paradise_lost.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.gameevent.GameEvent;

public class ParadiseLostGameEventTags {
    public static final TagKey<GameEvent> FLOATY_BOOTS_DAMPENED = register("floaty_boots_dampened");

    private static TagKey<GameEvent> register(String id) {
        return TagKey.create(Registries.GAME_EVENT, ModConstants.locate(id));
    }
}
