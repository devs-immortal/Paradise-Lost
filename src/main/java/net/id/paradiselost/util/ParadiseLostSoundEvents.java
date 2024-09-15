package net.id.paradiselost.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static net.id.paradiselost.ParadiseLost.locate;

/**
 * The home for all Paradise Lost sounds.
 */
public final class ParadiseLostSoundEvents {

    public static final SoundEvent BLOCK_BLACKCURRANT_BUSH_PICK_BLUEBERRIES = register("block.blackcurrant_bush.pick_blueberries");
    public static final SoundEvent BLOCK_PORTAL_AMBIENT = register("block.portal.ambient");
    public static final SoundEvent BLOCK_PORTAL_TRAVEL = register("block.portal.travel");
    public static final SoundEvent BLOCK_PORTAL_TRIGGER = register("block.portal.trigger");
    public static final SoundEvent BLOCK_SURTRUM_RUSH = register("block.surtrum_air.rush");
    public static final SoundEvent BLOCK_SURTRUM_CRACKLE = register("block.surtrum_air.crackle");

    public static final SoundEvent ITEM_ARMOR_EQUIP_OLVITE = register("item.armor.equip.olvite");
    public static final SoundEvent ITEM_ARMOR_EQUIP_GLAZED_GOLD = register("item.armor.equip.glazed_gold");
    public static final SoundEvent ITEM_ARMOR_EQUIP_SURTRUM = register("item.armor.equip.surtrum");
    public static final SoundEvent ITEM_BLOODSTONE_PRICK = register("item.bloodstone.prick");


    public static final SoundEvent ENTITY_MOA_AMBIENT = register("entity.moa.ambient");
    public static final SoundEvent ENTITY_MOA_GLIDING = register("entity.moa.gliding");
    public static final SoundEvent ENTITY_MOA_DEATH = register("entity.moa.death");
    public static final SoundEvent ENTITY_MOA_HURT = register("entity.moa.hurt");
    public static final SoundEvent ENTITY_MOA_EAT = register("entity.moa.eat");
    public static final SoundEvent ENTITY_MOA_LAY_EGG = register("entity.moa.lay_egg");
    public static final SoundEvent ENTITY_MOA_EGG_HATCH = register("entity.moa.egg_hatch");
    public static final SoundEvent ENTITY_MOA_STEP = register("entity.moa.step");
    public static final SoundEvent ENTITY_NITRA_THROW = register("entity.nitra.throw");
    public static final SoundEvent ENTITY_ENVOY_DAMAGE = register("entity.envoy.damage");

    public static final SoundEvent MUSIC_PARADISE_LOST = register("music.paradise");

    public static final MusicSound PARADISE_MUSIC_SOUND = new MusicSound(Registries.SOUND_EVENT.getEntry(MUSIC_PARADISE_LOST), 6000, 24000, true);

    public static void init() {
    }

    private static SoundEvent register(String location) {
        Identifier id = locate(location);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

}
