package net.id.paradiselost.util;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.resource.Resource;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.id.paradiselost.ParadiseLost.MOD_ID;
import static net.id.paradiselost.ParadiseLost.locate;

// TODO: Is this the best place for this?

/**
 * The home for all Paradise Lost sounds.
 */
public final class ParadiseLostSoundEvents {

    private ParadiseLostSoundEvents() {
        throw new RuntimeException();
    }
    
    /**
     * Used internally to track sounds, needs to be the first field.
     */
    private static final Set<AbstractSoundEvent> SOUNDS = new HashSet<>();

    public static final SoundEvent BLOCK_BLACKCURRANT_BUSH_PICK_BLUEBERRIES = childEvent("block.blackcurrant_bush.pick_blueberries", SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES);
    public static final SoundEvent BLOCK_PORTAL_AMBIENT = event("block.portal.ambient");
    public static final SoundEvent BLOCK_PORTAL_TRAVEL = event("block.portal.travel");
    public static final SoundEvent BLOCK_PORTAL_TRIGGER = event("block.portal.trigger");
    public static final SoundEvent BLOCK_SURTRUM_RUSH = childEvent("block.surtrum_air.rush", SoundEvents.BLOCK_FIRE_EXTINGUISH);
    public static final SoundEvent BLOCK_SURTRUM_CRACKLE = childEvent("block.surtrum_air.crackle", SoundEvents.BLOCK_FIRE_AMBIENT);

    public static final SoundEvent BLOCK_ORANGE_LEAVES_BREAK = childEvent("block.orange_leaves.break", SoundEvents.BLOCK_MOSS_BREAK);
    public static final SoundEvent BLOCK_ORANGE_LEAVES_DROP_FRUIT = childEvent("block.orange_leaves.drop_fruit", SoundEvents.BLOCK_CANDLE_BREAK);
    public static final SoundEvent BLOCK_ORANGE_LEAVES_BREAK_DIFFERENTLY = childEvent("block.orange_leaves.break_differently", SoundEvents.BLOCK_CROP_BREAK);
    public static final SoundEvent BLOCK_LICHEN_SPREADS = childEvent("block.lichen.spreads", SoundEvents.BLOCK_CHORUS_FLOWER_DEATH);

    public static final SoundEvent BLOCK_SPRING_WATER_AMBIENT = childEvent("block.spring_water.ambient", SoundEvents.BLOCK_WATER_AMBIENT);
    public static final SoundEvent BLOCK_SPRING_WATER_AMBIENT_2 = childEvent("block.spring_water.ambient.2", SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT);


    public static final SoundEvent MUSIC_PARADISE_LOST = music("cloud_ocean", "floating", "spirit_sunset", "constellation", "overcast", "sullen_lullaby");
    
    public static final SoundEvent MISC_SILENCE = event("misc.silence", false);


    public static final SoundEvent EFFECT_SIMMERING_SIMMER = childEvent("effect.simmering.simmer", SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT);


    public static final SoundEvent ENTITY_MOA_AMBIENT = childEvent("entity.moa.ambient", SoundEvents.ENTITY_PARROT_AMBIENT);
    public static final SoundEvent ENTITY_MOA_GLIDING = childEvent("entity.moa.gliding", SoundEvents.ENTITY_PHANTOM_FLAP);
    public static final SoundEvent ENTITY_MOA_DEATH = childEvent("entity.moa.death", SoundEvents.ENTITY_PARROT_DEATH);
    public static final SoundEvent ENTITY_MOA_HURT = childEvent("entity.moa.hurt", SoundEvents.ENTITY_BAT_DEATH);
    public static final SoundEvent ENTITY_MOA_EAT = childEvent("entity.moa.eat", SoundEvents.ENTITY_PARROT_EAT);
    public static final SoundEvent ENTITY_MOA_LAY_EGG = childEvent("entity.moa.lay_egg", SoundEvents.ENTITY_TURTLE_LAY_EGG);
    public static final SoundEvent ENTITY_MOA_EGG_HATCH = childEvent("entity.moa.egg_hatch", SoundEvents.ENTITY_TURTLE_EGG_HATCH);
    public static final SoundEvent ENTITY_MOA_STEP = childEvent("entity.moa.step", SoundEvents.ENTITY_PIG_STEP);

    public static final SoundEvent ENTITY_PARADISE_HARE_SNIFF = childEvent("entity.corsican_hare.sniff", SoundEvents.ENTITY_FOX_SNIFF);
    public static final SoundEvent ENTITY_PARADISE_HARE_JUMP = childEvent("entity.corsican_hare.jump", SoundEvents.ENTITY_RABBIT_JUMP);
    public static final SoundEvent ENTITY_PARADISE_HARE_HURT = childEvent("entity.corsican_hare.hurt", SoundEvents.ENTITY_RABBIT_HURT);
    public static final SoundEvent ENTITY_PARADISE_HARE_DEATH = childEvent("entity.corsican_hare.death", SoundEvents.ENTITY_RABBIT_DEATH);
    public static final SoundEvent ENTITY_PARADISE_HARE_EAT = childEvent("entity.corsican_hare.eat", SoundEvents.ENTITY_LLAMA_EAT);

    public static final SoundEvent ENTITY_NIGHTMARE_HURT = event("entity.nightmare.hurt");
    public static final SoundEvent ENTITY_NIGHTMARE_DEATH = event("entity.nightmare.death");
    public static final SoundEvent ENTITY_NIGHTMARE_AMBIENT = event("entity.nightmare.ambient");


    public static final SoundEvent ITEM_ARMOR_EQUIP_OLVITE = childEvent("item.armor.equip.olvite", SoundEvents.ITEM_ARMOR_EQUIP_IRON);
    public static final SoundEvent ITEM_ARMOR_EQUIP_GLAZED_GOLD = childEvent("item.armor.equip.glazed_gold", SoundEvents.ITEM_ARMOR_EQUIP_GOLD);
    public static final SoundEvent ITEM_ARMOR_EQUIP_SURTRUM = childEvent("item.armor.equip.surtrum", SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE);

    public static final SoundEvent ITEM_BLOODSTONE_PRICK = childEvent("item.bloodstone.prick", SoundEvents.ENTITY_BEE_STING);


    /**
     * Creates a new sound event with the sound and subtitle key based off of the name.
     *
     * @param name The name of the event
     * @return The new event
     */
    private static SoundEvent event(String name) {
        return event(name, true);
    }

    /**
     * Creates a new sound event with the sound and subtitle key based off of the name.
     *
     * @param event The sound event
     * @return The new event
     */
    private static SoundEvent event(SoundEvent event) {
        return event(event.getId().toString(), true);
    }
    
    /**
     * Creates a new sound event with the subtitle key based off of the name.
     *
     * @param name The name of the event
     * @param sounds The sounds to randomly pick
     * @return The new event
     */
    private static SoundEvent event(String name, String... sounds) {
        return event(name, true, sounds);
    }
    
    /**
     * Creates a new sound event with the sound based off of the name. The subtitle key will be based off of the name,
     * if enabled.
     *
     * @param name The name of the event
     * @param subtitles Does this event have a subtitle?
     * @return The new event
     */
    private static SoundEvent event(String name, boolean subtitles) {
        return event(name, subtitles, name.replaceAll("\\.", "/"));
    }
    
    /**
     * Creates a new sound event. The subtitle key will be based off of the name, if enabled.
     *
     * @param name The name of the event
     * @param subtitles Does this event have a subtitle?
     * @param sounds The sounds to randomly pick
     * @return The new event
     */
    private static SoundEvent event(String name, boolean subtitles, String... sounds) {
        var event = new ParadiseLostSoundEvent(
                locate(name),
                subtitles ? "subtitles." + MOD_ID + "." + name : null,
                Stream.of(sounds)
                    .map(ParadiseLost::locate)
                    .collect(Collectors.toUnmodifiableSet())
        );
        SOUNDS.add(event);
        return event;
    }
    
    /**
     * Creates a new sound event tuned for use with music.
     *
     * @param songs The tracks to play
     * @return The new sound event
     */
    private static SoundEvent music(String... songs) {
        var event = new MusicSoundEvent(Stream.of(songs).map((track) -> locate("music/paradise_lost/" + track)).collect(Collectors.toUnmodifiableSet()));
        SOUNDS.add(event);
        return event;
    }

    /**
     * Creates a new sound event that uses another as it's sound. The subtitle key is based off of the name.
     *
     * @param name The name of the event
     * @param parent The source of the sounds
     * @return The new sound event
     */
    private static SoundEvent childEvent(String name, String parent) {
        return childEvent(name, true, parent);
    }

    /**
     * Creates a new sound event that uses another as it's sound. The subtitle key is based off of the name.
     *
     * @param name The name of the event
     * @param parent The source of the sounds
     * @return The new sound event
     */
    private static SoundEvent childEvent(String name, SoundEvent parent) {
        return childEvent(name, true, parent.getId().toString());
    }
    
    /**
     * Creates a new sound event that uses another as it's sound. The subtitle key is based off of the name, if enabled.
     *
     * @param name The name of the event
     * @param subtitles Does this event have a subtitle?
     * @param parent The source of the sounds
     * @return The new sound event
     */
    private static SoundEvent childEvent(String name, boolean subtitles, String parent) {
        var event = new ChildSoundEvent(
                locate(name),
                subtitles ? "subtitles." + MOD_ID + "." + name : null,
                locate(parent)
        );
        SOUNDS.add(event);
        return event;
    }
    
    /**
     * Registers all the sounds.
     */
    public static void init() {
        SOUNDS.forEach(ParadiseLostSoundEvents::register);
        
        Music.init();
    }
    
    /**
     * Registers a sound event, an {@link Identifier} is not needed because the {@link SoundEvent} has its own ID;
     *
     * @param event The event to register
     */
    private static void register(SoundEvent event) {
        Registry.register(Registry.SOUND_EVENT, event.getId(), event);
    }
    
    /**
     * The identifier for our sounds.json resource.
     */
    private static final Identifier RESOURCE_SOUNDS = locate("sounds.json");
    
    /**
     * Creates a resource for the sound system to read, allowing for laziness and one less JSON file.
     *
     * @return The resource
     */
    public static Resource createResource() {
        StringBuilder builder = new StringBuilder("{");
    
        for (var sound : SOUNDS) {
            builder.append(sound.toJson()).append(',');
        }
        builder.setLength(builder.length() - 1);
        
        builder.append('}');

        var payload = builder.toString().getBytes(StandardCharsets.UTF_8);
        
        return new Resource(
                "Paradise Lost",
                () -> new ByteArrayInputStream(payload),
                () -> null
        );
    }

    public static final class Music {
        public static final MusicSound PARADISE_LOST = new MusicSound(MUSIC_PARADISE_LOST, 12000, 24000, false);

        //Triggers <clinit>()V
        private static void init() {
        }
    }

    private abstract static class AbstractSoundEvent extends SoundEvent {
        protected final String subtitle;
        
        private AbstractSoundEvent(Identifier id, String subtitle) {
            super(id);
            this.subtitle = subtitle;
        }
    
        public abstract String toJson();
    }
    
    private static final class ChildSoundEvent extends AbstractSoundEvent {
        private final Identifier parent;
        
        private ChildSoundEvent(Identifier id, String subtitle, Identifier parent) {
            super(id, subtitle);
            this.parent = parent;
        }
    
        @Override
        public String toJson() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\"%s\":{".formatted(getId().getPath()));
            if (subtitle != null) {
                stringBuilder.append("\"subtitle\":\"%s\",".formatted(subtitle));
            }
            stringBuilder.append("\"sounds\":[{\"name\":\"%s\",\"type\":\"event\"}]}".formatted(parent.toString()));
            return stringBuilder.toString();
        }
    }
    
    private static final class MusicSoundEvent extends AbstractSoundEvent {
        private final Set<Identifier> tracks;
        
        private MusicSoundEvent(Set<Identifier> tracks) {
            super(locate("music.paradise_lost"), null);
            this.tracks = tracks;
        }
        
        @Override
        public String toJson() {
            StringBuilder builder = new StringBuilder();
            builder.append("\"music.paradise_lost\":{\"sounds\":[");
            for (Identifier track : tracks) {
                builder.append("{\"name\":\"%s\",\"stream\":true},".formatted(track.toString()));
            }
            builder.setLength(builder.length() - 1);
            builder.append("]}");
            return builder.toString();
        }
    }
    
    private static final class ParadiseLostSoundEvent extends AbstractSoundEvent {
        private final Set<Identifier> sounds;
        
        private ParadiseLostSoundEvent(Identifier id, String subtitle, Set<Identifier> sounds) {
            super(id, subtitle);
            this.sounds = sounds;
        }
    
        @Override
        public String toJson() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\"%s\":{".formatted(getId().getPath()));
            if (subtitle != null) {
                stringBuilder.append("\"subtitle\":\"%s\",".formatted(subtitle));
            }
            stringBuilder.append("\"sounds\":[");
            for (Identifier sound : sounds) {
                stringBuilder.append("\"%s\"".formatted(sound.toString()));
            }
            stringBuilder.append("]}");
            return stringBuilder.toString();
        }
    }
}
