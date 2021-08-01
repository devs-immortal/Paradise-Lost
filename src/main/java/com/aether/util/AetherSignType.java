package com.aether.util;

import com.aether.mixin.util.SignTypeAccessor;
import net.minecraft.util.SignType;

public class AetherSignType extends SignType {
    public static final SignType SKYROOT = SignTypeAccessor.callRegister(new AetherSignType("aether_skyroot"));
    public static final SignType ORANGE = SignTypeAccessor.callRegister(new AetherSignType("aether_orange"));
    public static final SignType WISTERIA = SignTypeAccessor.callRegister(new AetherSignType("aether_wisteria"));
    public static final SignType GOLDEN_OAK = SignTypeAccessor.callRegister(new AetherSignType("aether_golden_oak"));
    public static final SignType CRYSTAL = SignTypeAccessor.callRegister(new AetherSignType("aether_crystal"));

    protected AetherSignType(String name) {
        super(name);
    }
}
