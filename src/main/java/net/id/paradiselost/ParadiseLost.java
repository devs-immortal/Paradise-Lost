package net.id.paradiselost;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.blocks.ParadiseLostBlockSets;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.ParadiseLostWoodTypes;
import net.id.paradiselost.blocks.blockentity.ParadiseLostBlockEntityTypes;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.ParadiseLostModelPredicates;
import net.id.paradiselost.client.model.armor.ParadiseLostArmorModels;
import net.id.paradiselost.client.rendering.block.ParadiseLostBlockEntityRenderers;
import net.id.paradiselost.client.rendering.entity.ParadiseLostEntityRenderers;
import net.id.paradiselost.client.rendering.item.ParadiseLostItemRenderers;
import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.client.rendering.texture.ParadiseLostTextures;
import net.id.paradiselost.client.rendering.util.ParadiseLostColorProviders;
import net.id.paradiselost.commands.ParadiseLostCommands;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.id.paradiselost.entities.passive.moa.MoaRaces;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.lore.ParadiseLostLore;
import net.id.paradiselost.recipe.ParadiseLostRecipeTypes;
import net.id.paradiselost.registry.ParadiseLostRegistries;
import net.id.paradiselost.screen.ParadiseLostScreens;
import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.id.paradiselost.world.ParadiseLostGameRules;
import net.id.paradiselost.world.dimension.ParadiseLostBiomes;
import net.id.paradiselost.world.dimension.ParadiseLostDimension;
import net.id.paradiselost.world.feature.ParadiseLostFeatures;
import net.id.paradiselost.world.gen.carver.ParadiseLostCarvers;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Docs for Paradise Lost are sometimes written long after the code itself has been written, and oftentimes by different
 * authors than the author of the code itself. If you have any questions or concerns regarding documentation, please
 * contact either the doc author or the code author, or both, via our <a
 * href="https://discord.gg/eRsJ6F3Wng">Discord</a>.
 * <br><br>
 * The doc author can usually be found at the end of the first doc of the class, next to a tilde.
 * <br><br>
 * The person(s) next to the @author tag are, as expected, the people who have written the code.
 * <br><br>
 * ~ Jack
 * <br><br>
 * A list of developers can be found in {@code resources/fabric.mod.json}.
 */
public class ParadiseLost implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    public static final String MOD_ID = "paradise_lost";
    public static final Logger LOG = LogUtils.getLogger();

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
        ParadiseLostRegistries.init();
        ParadiseLostCarvers.init();
        ParadiseLostFeatures.init();
        ParadiseLostBiomes.init();
        ParadiseLostDimension.init();
        ParadiseLostBlockSets.init();
        ParadiseLostWoodTypes.init();
        ParadiseLostBlocks.init();
        ParadiseLostEntityTypes.init();
        ParadiseLostItems.init();
        ParadiseLostBlockEntityTypes.init();
        ParadiseLostRecipeTypes.init();
        ParadiseLostCommands.init();
        ParadiseLostGameRules.init();
        ParadiseLostSoundEvents.init();
        MoaRaces.init();
        ParadiseLostScreens.init();
        ParadiseLostLore.init();
        ParadiseLostParticles.init();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			// since the portal can be configured to open with other mods blocks
			// we have to wait until all mods have loaded
			// and therefore have to register the portal late
			ParadiseLostDimension.initPortal();
		});
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        initializeCrowdin();
        ParadiseLostModelPredicates.initClient();
        ParadiseLostArmorModels.initClient();
        ParadiseLostModelLayers.initClient();
        ParadiseLostEntityRenderers.initClient();
        ParadiseLostColorProviders.initClient();
        ParadiseLostBlockEntityRenderers.initClient();
        ParadiseLostParticles.Client.init();
        ParadiseLostTextures.initClient();
        ParadiseLostItemRenderers.initClient();
        ParadiseLostScreens.initClient();
        ParadiseLostScreens.clientInit();
    }

    @Environment(EnvType.CLIENT)
    private void initializeCrowdin() {
        // No code changes for when the mod isn't present. :-)
        if (FabricLoader.getInstance().isModLoaded("crowdin-translate")) {
            try {
                var CrowdinTranslate = Class.forName("de.guntram.mcmod.crowdintranslate.CrowdinTranslate");
                var lookup = MethodHandles.lookup();
                var downloadTranslations = lookup.findStatic(CrowdinTranslate, "downloadTranslations", MethodType.methodType(void.class, String.class, String.class));
                downloadTranslations.invokeExact("paradise_lost", MOD_ID);
            } catch (Throwable e) {
                LOG.warn("Failed to setup Crowdin Translate", e);
            }
        }
    }

    // FIXME This is really really really stupid.
    @Environment(EnvType.SERVER)
    private static final String DISABLE_WORLD_CHECK = "PARADISE_LOST_DISABLE_WORLD_CHECK";

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            if (System.getProperty(DISABLE_WORLD_CHECK) != null) {
                return;
            }

            var world = server.getWorld(ParadiseLostDimension.PARADISE_LOST_WORLD_KEY);
            if (world == null) {
                var message = """
                        This crash is intentional. This is because of a bug in vanilla Minecraft that caused Paradise Lost
                        to be unable to add the Paradise Lost dimension.
                                            
                        Please restart the server. This should solve this error.
                                            
                        The related issue on Mojang's issue tracker is MC-195468 at https://bugs.mojang.com/browse/MC-195468
                                            
                        You should only ever see this error message once per world.
                        If restarting the server doesn't solve the issue, then please contact us at https://discord.gg/eRsJ6F3Wng
                                            
                        If you would like to suppress this crash add -D%s to your arguments.
                        For example, if you have:
                        java -jar fabric-server.jar nogui
                        you would want to add -D%s after the `java` part, like so:
                        java -D%s -jar fabric-server.jar nogui
                        """
                        .formatted(
                                DISABLE_WORLD_CHECK,
                                DISABLE_WORLD_CHECK,
                                DISABLE_WORLD_CHECK
                        );

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    // To people who might want to change this to use the Logger class, don't.
                    // It will not print the message when you do that. I tried.
                    System.err.println(
                            "\n".repeat(10)
                                    + message
                                    + "\n".repeat(10)
                    );
                }));
                throw new RuntimeException(message);
            }
        });
    }
}
