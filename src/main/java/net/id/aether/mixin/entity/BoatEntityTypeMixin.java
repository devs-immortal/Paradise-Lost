package net.id.aether.mixin.entity;

import net.id.aether.util.EnumExtender;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;

@Mixin(BoatEntity.Type.class)
public class BoatEntityTypeMixin {
    @Shadow(aliases = "field_7724") @Mutable @Final private static BoatEntity.Type[] VALUES;

    static {
        EnumExtender.register(BoatEntity.Type.class, (name, args) -> {
            BoatEntity.Type entry = (BoatEntity.Type) (Object) new BoatEntityTypeMixin(name, VALUES.length, (Block) args[0], (String) args[1]);
            VALUES = Arrays.copyOf(VALUES, VALUES.length + 1);
            return VALUES[VALUES.length - 1] = entry;
        });
    }

    private BoatEntityTypeMixin(String valueName, int ordinal, Block baseBlock, String name) {
        throw new AssertionError();
    }
}
