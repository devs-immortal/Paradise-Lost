package net.id.paradise_lost.platform;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.platform.services.IAttachmentHelper;
import net.id.paradise_lost.platform.services.IClientHelper;
import net.id.paradise_lost.platform.services.ICompatHelper;
import net.id.paradise_lost.platform.services.IMiscHelper;
import net.id.paradise_lost.platform.services.INetworkHelper;
import net.id.paradise_lost.platform.services.IPlatformHelper;
import net.id.paradise_lost.platform.services.IRegistrationHelper;

import java.util.ServiceLoader;

public final class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static final IAttachmentHelper ATTACHMENTS = load(IAttachmentHelper.class);

    public static final INetworkHelper NETWORK = load(INetworkHelper.class);

    public static final IClientHelper CLIENT = load(IClientHelper.class);

    public static final IRegistrationHelper REGISTRATION = load(IRegistrationHelper.class);

    public static final IMiscHelper MISC = load(IMiscHelper.class);

    public static final ICompatHelper COMPAT = load(ICompatHelper.class);

    private Services() {}

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ModConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
