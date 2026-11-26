package net.id.paradise_lost.block.decorative;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public enum PotMaterial implements StringRepresentable {
    TERRACOTTA("terracotta"),
    CALCITE("calcite");

    public static final EnumProperty<PotMaterial> PROPERTY = EnumProperty.create("paradise_lost_pot", PotMaterial.class);

    private final String name;

    PotMaterial(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}