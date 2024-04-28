package net.id.paradiselost.config;

import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.annotation.*;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Config(name = "Paradise_Lost")
public class ParadiseLostConfig implements ConfigData {

	public static ParadiseLostConfig CONFIG;

	protected String ParadiseLostPortalBaseBlock = "paradise_lost:bloomed_calcite";


	public static void init() {
		AutoConfig.register(ParadiseLostConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(ParadiseLostConfig.class).getConfig();
	}

	public Block getPortalBaseBlock(Registry<Block> blockRegistry) {
		return blockRegistry.get(new Identifier(ParadiseLostPortalBaseBlock));
	}

}
