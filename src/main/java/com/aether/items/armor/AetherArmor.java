package com.aether.items.armor;

import com.aether.items.AetherItemGroups;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

import java.util.ArrayList;
import java.util.List;

public class AetherArmor extends GeoArmorItem implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final AetherArmorType typeIn;
    private String armorName = "iron";

    public AetherArmor(AetherArmorType typeIn, EquipmentSlot slotIn) {
        super(typeIn.getMaterial(), slotIn, new Settings().group(AetherItemGroups.Armor));
        this.typeIn = typeIn;
    }

    public AetherArmor(AetherArmorType typeIn, Rarity rarityIn, EquipmentSlot slotIn) {
        super(typeIn.getMaterial(), slotIn, new Settings().group(AetherItemGroups.Armor).rarity(rarityIn));
        this.typeIn = typeIn;
    }

    public AetherArmor(String nameIn, AetherArmorType typeIn, EquipmentSlot slotIn) {
        this(typeIn, slotIn);
        this.armorName = nameIn;
    }

    public AetherArmor(String nameIn, AetherArmorType typeIn, Rarity rarityIn, EquipmentSlot slotIn) {
        this(typeIn, rarityIn, slotIn);
        this.armorName = nameIn;
    }

    public AetherArmorType getType() {
        return this.typeIn;
    }

    public String getArmorName() {
        return this.armorName;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        //This is all the extradata this event carries. The livingentity is the entity that's wearing the armor. The itemstack and equipmentslottype are self explanatory.
        LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);

        //Always loop the animation but later on in this method we'll decide whether or not to actually play it
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.potato_armor.new", true));

        //If the living entity is an armorstand just play the animation nonstop
        if (livingEntity instanceof ArmorStandEntity) {
            return PlayState.CONTINUE;
        }

        //The entity is a player, so we want to only play if the player is wearing the full set of armor
        else if (livingEntity instanceof ClientPlayerEntity) {
            ClientPlayerEntity client = (ClientPlayerEntity) livingEntity;

            //Get all the equipment, aka the armor, currently held item, and offhand item
            List<Item> equipmentList = new ArrayList<>();
            client.getItemsEquipped().forEach((x) -> equipmentList.add(x.getItem()));

            //elements 2 to 6 are the armor so we take the sublist. Armorlist now only contains the 4 armor slots
            List<Item> armorList = equipmentList.subList(2, 6);
        }
        return PlayState.STOP;
    }
}