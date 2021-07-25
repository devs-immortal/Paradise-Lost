package com.aether.util;

import net.minecraft.util.SignType;

public class AetherSignType extends SignType {
    public static final AetherSignType SKYROOT = registerSign(new AetherSignType("skyroot"));
    public static final AetherSignType ORANGE = registerSign(new AetherSignType("orange"));
    public static final AetherSignType WISTERIA = registerSign(new AetherSignType("wisteria"));
    public static final AetherSignType GOLDEN_OAK = registerSign(new AetherSignType("golden_oak"));
    public static final AetherSignType CRYSTAL = registerSign(new AetherSignType("crystal"));

    protected AetherSignType(String name) {
        super(name);
    }

    public static AetherSignType registerSign(AetherSignType type){
        VALUES.add(type);
        return type;
    }
}
