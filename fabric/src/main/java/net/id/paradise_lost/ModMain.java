package net.id.paradise_lost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.id.paradise_lost.commands.ParadiseLostCommands;
import net.id.paradise_lost.data.FabricDataEntries;
import net.id.paradise_lost.entity.ModEntities;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.id.paradise_lost.platform.Services;
import net.id.paradise_lost.registry.EntityRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.screen.FabricScreens;
import net.id.paradise_lost.util.ParadiseLostAliasFix;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Services.PLATFORM.getPlatformName();
        ModCommon.init();
        FabricDataEntries.apply();

        ParadiseLostAliasFix.init();
        CommonDataAttachments.init();
        EntityRegistry.registerSpawnPlacements();
        FabricScreens.register();
        ModEntities.registerEntityAttributes(FabricDefaultAttributeRegistry::register);

        PacketHandler.registerPackets();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                ParadiseLostCommands.registerAll(dispatcher));

        // Floaty boots already grant Feather Falling IV; ban the enchantment from stacking.
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, context) -> {
            if (target.is(ItemRegistry.FLOATY_BOOTS.get()) && enchantment.is(Enchantments.FEATHER_FALLING)) {
                return TriState.FALSE;
            }
            return TriState.DEFAULT;
        });
    }
}
