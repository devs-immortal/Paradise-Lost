package net.id.paradise_lost.datagen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ParadiseLostSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ParadiseLostSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ModConstants.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(ParadiseLostSoundEvents.BLOCK_BLACKCURRANT_BUSH_PICK_BLUEBERRIES, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.blackcurrant_bush.pick_blueberries")
                .with(event("block.sweet_berry_bush.pick_berries")));

        add(ParadiseLostSoundEvents.BLOCK_PORTAL_AMBIENT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.portal.ambient")
                .with(sound("paradise_lost:block/portal/ambient")));
        add(ParadiseLostSoundEvents.BLOCK_PORTAL_TRAVEL, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.portal.travel")
                .with(sound("paradise_lost:block/portal/travel")));
        add(ParadiseLostSoundEvents.BLOCK_PORTAL_TRIGGER, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.portal.trigger")
                .with(sound("paradise_lost:block/portal/trigger")));

        add(ParadiseLostSoundEvents.BLOCK_PALACE_DOOR_OPEN, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.palace_door.open")
                .with(sound("paradise_lost:block/palace_door/click_open")));
        add(ParadiseLostSoundEvents.BLOCK_PALACE_DOOR_UNLOCK, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.palace_door.unlock")
                .with(sound("paradise_lost:block/palace_door/open")));

        add(ParadiseLostSoundEvents.BLOCK_SURTRUM_RUSH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.surtrum_air.rush")
                .with(event("block.fire.extinguish")));
        add(ParadiseLostSoundEvents.BLOCK_SURTRUM_CRACKLE, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.surtrum_air.crackle")
                .with(event("block.fire.ambient")));

        add(ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_OLVITE.value(), SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.armor.equip.olvite")
                .with(event("item.armor.equip_iron")));
        add(ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_GLAZED_GOLD.value(), SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.armor.equip.glazed_gold")
                .with(event("item.armor.equip_gold")));
        add(ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_SURTRUM.value(), SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.armor.equip.surtrum")
                .with(event("item.armor.equip_netherite")));
        add(ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_RELIC.value(), SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.armor.equip.relic")
                .with(event("item.armor.equip_chain")));
        add(ParadiseLostSoundEvents.ITEM_ARMOR_EQUIP_FLAXWEAVE.value(), SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.armor.equip.flaxweave")
                .with(event("item.armor.equip_leather")));

        add(ParadiseLostSoundEvents.ITEM_BLOODSTONE_PRICK, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.bloodstone.prick")
                .with(event("entity.bee.sting")));
        add(ParadiseLostSoundEvents.SOUL_BLADE_HARVEST, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.item.soul_blade.harvest")
                .with(event("entity.allay.item_taken")));

        add(ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.ambient")
                .with(
                        sound("paradise_lost:entity/passive/moa/moachirp1"),
                        sound("paradise_lost:entity/passive/moa/moachirp2"),
                        sound("paradise_lost:entity/passive/moa/moachirp3"),
                        sound("paradise_lost:entity/passive/moa/moachirp4")
                ));
        add(ParadiseLostSoundEvents.ENTITY_MOA_AMBIENT_SING, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.ambient")
                .with(
                        sound("paradise_lost:entity/passive/moa/moaambient3"),
                        sound("paradise_lost:entity/passive/moa/moaambient4"),
                        sound("paradise_lost:entity/passive/moa/moaambient5"),
                        sound("paradise_lost:entity/passive/moa/moaambient6")
                ));
        add(ParadiseLostSoundEvents.ENTITY_MOA_GLIDING, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.gliding")
                .with(
                        sound("paradise_lost:entity/passive/moa/moaflap1"),
                        sound("paradise_lost:entity/passive/moa/moaflap3"),
                        sound("paradise_lost:entity/passive/moa/moaflap4"),
                        sound("paradise_lost:entity/passive/moa/moaflap5")
                ));
        add(ParadiseLostSoundEvents.ENTITY_MOA_DEATH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.death")
                .with(event("entity.parrot.death")));
        add(ParadiseLostSoundEvents.ENTITY_MOA_HURT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.hurt")
                .with(event("entity.bat.death")));
        add(ParadiseLostSoundEvents.ENTITY_MOA_EAT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.eat")
                .with(event("entity.parrot.eat")));
        add(ParadiseLostSoundEvents.ENTITY_MOA_LAY_EGG, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.lay_egg")
                .with(event("entity.turtle.lay_egg")));
        add(ParadiseLostSoundEvents.ENTITY_MOA_EGG_HATCH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.egg_hatch")
                .with(event("entity.turtle.egg_hatch")));
        add(ParadiseLostSoundEvents.ENTITY_MOA_STEP, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.moa.step")
                .with(event("entity.pig.step")));

        add(ParadiseLostSoundEvents.ENTITY_POPOM_AMBIENT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.popom.ambient")
                .with(
                        sound("paradise_lost:entity/passive/popom/popom_ambient_1"),
                        sound("paradise_lost:entity/passive/popom/popom_ambient_2"),
                        sound("paradise_lost:entity/passive/popom/popom_ambient_3"),
                        sound("paradise_lost:entity/passive/popom/popom_ambient_4"),
                        sound("paradise_lost:entity/passive/popom/popom_ambient_5"),
                        sound("paradise_lost:entity/passive/popom/popom_ambient_6")
                ));
        add(ParadiseLostSoundEvents.ENTITY_POPOM_HURT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.popom.hurt")
                .with(
                        sound("paradise_lost:entity/passive/popom/popom_death_1"),
                        sound("paradise_lost:entity/passive/popom/popom_death_2")
                ));
        add(ParadiseLostSoundEvents.ENTITY_POPOM_DEATH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.popom.death")
                .with(
                        sound("paradise_lost:entity/passive/popom/popom_death_1"),
                        sound("paradise_lost:entity/passive/popom/popom_death_2")
                ));
        add(ParadiseLostSoundEvents.ENTITY_POPOM_HARVEST, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.popom.harvest")
                .with(event("block.honey_block.fall")));

        add(ParadiseLostSoundEvents.ENTITY_NITRA_THROW, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.nitra.throw")
                .with(event("entity.snowball.throw")));
        add(ParadiseLostSoundEvents.ENTITY_NITRA_EXPLODE, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.nitra.explode")
                .with(
                        sound("paradise_lost:entity/nitra/nitra_1"),
                        sound("paradise_lost:entity/nitra/nitra_2"),
                        sound("paradise_lost:entity/nitra/nitra_3"),
                        sound("paradise_lost:entity/nitra/nitra_4")
                ));

        add(ParadiseLostSoundEvents.ENTITY_ENVOY_AMBIENT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.ambient")
                .with(event("entity.skeleton.ambient")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_STEP, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.step")
                .with(event("entity.skeleton.step")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_HURT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.hurt")
                .with(event("entity.skeleton.hurt")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_DEATH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.death")
                .with(event("entity.wither_skeleton.death")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_GETS_ENLIGHTENED, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.enlightened.ambient")
                .with(event("entity.zombie_villager.converted")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_AMBIENT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.enlightened.ambient")
                .with(event("block.amethyst_block.chime")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_STEP, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.enlightened.step")
                .with(event("block.amethyst_block.step")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_HURT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.enlightened.hurt")
                .with(event("block.amethyst_block.hit")));
        add(ParadiseLostSoundEvents.ENTITY_ENVOY_ENLIGHTENED_DEATH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.envoy.enlightened.death")
                .with(event("block.amethyst_block.break")));

        add(ParadiseLostSoundEvents.ENTITY_SENTINEL_STEP, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.sentinel.step")
                .with(event("block.calcite.step")));
        add(ParadiseLostSoundEvents.ENTITY_SENTINEL_HURT, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.sentinel.hurt")
                .with(event("block.basalt.place")));
        add(ParadiseLostSoundEvents.ENTITY_SENTINEL_DEATH, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.sentinel.death")
                .with(event("entity.snow_golem.death")));

        add(ParadiseLostSoundEvents.ENTITY_MINECART_INSIDE_LEVITATING, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.minecart.inside_levitating")
                .with(sound("paradise_lost:entity/minecart/inside_levitating")));
        add(ParadiseLostSoundEvents.ENTITY_MINECART_ROLLING_LEVITATING, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.entity.minecart.rolling_levitating")
                .with(sound("paradise_lost:entity/minecart/rolling_levitating")));

        add(ParadiseLostSoundEvents.MUSIC_PARADISE_LOST, SoundDefinition.definition()
                .subtitle("subtitles.paradise_lost.block.portal.ambient")
                .with(
                        sound("paradise_lost:music/paradise_lost/cloud_ocean").stream(),
                        sound("paradise_lost:music/paradise_lost/constellation").stream(),
                        sound("paradise_lost:music/paradise_lost/floating").stream(),
                        sound("paradise_lost:music/paradise_lost/overcast").stream(),
                        sound("paradise_lost:music/paradise_lost/spirit_sunset").stream(),
                        sound("paradise_lost:music/paradise_lost/sullen_lullaby").stream()
                ));
    }

    private static SoundDefinition.Sound event(String vanillaEvent) {
        return sound(ResourceLocation.withDefaultNamespace(vanillaEvent), SoundDefinition.SoundType.EVENT);
    }
}
