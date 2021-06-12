package com.aether.api.moa;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class MoaType {

    private final MoaProperties properties;
    public final int hexColor;
    private ResourceLocation registryName;
    private CreativeModeTab group;

    public MoaType(int hexColor, MoaProperties properties) {
        this.hexColor = hexColor;
        this.properties = properties;
    }

    public MoaType(int hexColor, CreativeModeTab group, MoaProperties properties) {
        this(hexColor, properties);

        this.group = group;
    }

    public ResourceLocation getTexture(boolean saddled) {
        return this.properties.getCustomTexture(saddled);
    }

    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    public void setRegistryName(ResourceLocation location) {
        this.registryName = location;
    }

    public MoaProperties getMoaProperties() {
        return this.properties;
    }

    public CreativeModeTab getItemGroup() {
        return this.group;
    }

    public int getMoaEggColor() {
        return this.hexColor;
    }

}