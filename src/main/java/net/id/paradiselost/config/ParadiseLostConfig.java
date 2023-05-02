package net.id.paradiselost.config;

import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.annotation.*;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

@Config(name = "Paradise_Lost")
public class ParadiseLostConfig implements ConfigData {

	public static ParadiseLostConfig CONFIG;

	protected String ParadiseLostPortalBaseBlock = "minecraft:glowstone";


	public static void init() {
		AutoConfig.register(ParadiseLostConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(ParadiseLostConfig.class).getConfig();
	}

	public Block getPortalBaseBlock(Registry<Block> blockRegistry) {
		return blockRegistry.get(new Identifier(ParadiseLostPortalBaseBlock));
	}

}
