package com.aether.mixin.entity;

import com.aether.entities.vehicle.AetherBoatEntityTypeExtensions;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Arrays;

@Mixin(BoatEntity.Type.class)
public class BoatEntityTypeMixin implements AetherBoatEntityTypeExtensions {
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Mutable
    @Final
    private static BoatEntity.Type[] field_7724;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static BoatEntity.Type callInit(String string, int i, Block baseBlock, String name) {
        throw new AssertionError();
    }

    @Override
    public BoatEntity.Type addBoat(String id, String name, Block baseBlock) {
        BoatEntity.Type newBoat = callInit(id, field_7724.length, baseBlock, name);
        field_7724 = Arrays.copyOf(field_7724, field_7724.length + 1);
        field_7724[field_7724.length - 1] = newBoat;
        return newBoat;
    }
}