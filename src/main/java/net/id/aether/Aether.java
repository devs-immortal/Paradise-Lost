package net.id.aether;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.client.model.AetherModelLayers;
import net.id.aether.client.model.AetherModelPredicates;
import net.id.aether.client.model.armor.AetherArmorModels;
import net.id.aether.client.model.block.HolidayBlockModel;
import net.id.aether.client.rendering.block.AetherBlockEntityRenderers;
import net.id.aether.client.rendering.entity.AetherEntityRenderers;
import net.id.aether.client.rendering.item.AetherItemRenderers;
import net.id.aether.client.rendering.particle.AetherParticles;
import net.id.aether.client.rendering.shader.AetherShaders;
import net.id.aether.client.rendering.texture.AetherTextures;
import net.id.aether.client.rendering.util.AetherColorProviders;
import net.id.aether.commands.AetherCommands;
import net.id.aether.devel.AetherDevel;
import net.id.aether.effect.AetherStatusEffects;
import net.id.aether.effect.condition.Conditions;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.passive.moa.MoaRaces;
import net.id.aether.fluids.AetherFluids;
import net.id.aether.items.AetherItems;
import net.id.aether.loot.AetherLootNumberProviderTypes;
import net.id.aether.lore.AetherLore;
import net.id.aether.registry.AetherRegistries;
import net.id.aether.screen.AetherScreens;
import net.id.aether.util.AetherSignType;
import net.id.aether.util.AetherSoundEvents;
import net.id.aether.world.AetherGameRules;
import net.id.aether.world.dimension.AetherBiomes;
import net.id.aether.world.dimension.AetherDimension;
import net.id.aether.world.feature.AetherFeatures;
import net.id.aether.world.gen.carver.AetherCarvers;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Docs for Paradise Lost are sometimes written long after
 * the code itself has been written, and oftentimes by different
 * authors than the author of the code itself.
 * If you have any questions or concerns regarding documentation,
 * please contact either the doc author or the code author, or
 * both, via our <a, href="https://discord.gg/eRsJ6F3Wng">Discord</a>.
 * <br><br>
 * The doc author can usually be found at the end of the first doc
 * of the class, next to a tilde.
 * <br><br>
 * The person(s) next to the @author tag are, as expected, the people
 * who have written the code.
 * <br><br>
 * ~ Jack
 * <br><br>
 * A list of developers can be found in {@code resources/fabric.mod.json}.
 */
public class Aether implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    public static final String MOD_ID = "the_aether";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    
    /**
     * Creates a new {@link Identifier} based on the passed location.
     * <p>
     * If the location contains a collin `:` it will be split and handled like normal, otherwise it will use the default
     * namespace contained in {@link #MOD_ID} instead of the default "minecraft" namespace.
     *
     * @param location The location to use
     * @return The new {@link Identifier} instance
     */
    public static Identifier locate(String location) {
        if (location.contains(":")) {
            return new Identifier(location);
        } else {
            return new Identifier(MOD_ID, location);
        }
    }

    @Override
    public void onInitialize() {
        AetherRegistries.init();
        AetherCarvers.init();
        AetherFeatures.init();
        AetherBiomes.init();
        AetherDimension.init();
        AetherStatusEffects.init();
        AetherBlocks.init();
        AetherFluids.init();
        AetherEntityTypes.init();
        AetherItems.init();
        AetherBlockEntityTypes.init();
        AetherCommands.init();
        AetherGameRules.init();
        AetherLootNumberProviderTypes.init();
        AetherSoundEvents.init();
        Conditions.init();
        MoaRaces.init();
        AetherScreens.init();
        AetherLore.init();
        AetherParticles.init();
        if(FabricLoader.getInstance().isDevelopmentEnvironment()){
            AetherDevel.init();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        CrowdinTranslate.downloadTranslations("aether", MOD_ID);
        AetherModelPredicates.initClient();
        AetherArmorModels.initClient();
        AetherModelLayers.initClient();
        AetherEntityRenderers.initClient();
        AetherColorProviders.initClient();
        AetherBlockEntityRenderers.initClient();
        AetherParticles.Client.init();
        AetherTextures.initClient();
        AetherItemRenderers.initClient();
        AetherScreens.initClient();
        Conditions.clientInit();
        AetherShaders.init();
        HolidayBlockModel.init();
        AetherSignType.clientInit();
        if(FabricLoader.getInstance().isDevelopmentEnvironment()){
            AetherDevel.Client.init();
        }
    }
    
    // FIXME This is really really really stupid.
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register((server)->{
            var world = server.getWorld(AetherDimension.AETHER_WORLD_KEY);
            if(world == null){
                var message = """
                    This crash is intentional. This is because of a bug in vanilla Minecraft that caused Paradise Lost
                    to be unable to add the Aether dimension.
                    
                    Please restart the server. This should solve this error.
                    
                    The related issue on Mojang's issue tracker is MC-195468 at https://bugs.mojang.com/browse/MC-195468
                    
                    You should only ever see this error message once per world.
                    If restarting the server doesn't solve the issue, then please contact us at https://discord.gg/eRsJ6F3Wng
                    """;
                
                Runtime.getRuntime().addShutdownHook(new Thread(()->{
                    System.err.println(
                        "\n".repeat(10) +
                        message +
                        "\n".repeat(10)
                    );
                }));
                throw new RuntimeException(message);
            }
        });
    }
}
