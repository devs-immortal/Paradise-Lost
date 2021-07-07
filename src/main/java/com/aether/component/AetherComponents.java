package com.aether.component;

import com.aether.Aether;
import com.aether.entities.passive.MoaEntity;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;

public class AetherComponents implements EntityComponentInitializer {

    public static final ComponentKey<MoaGenetics> MOA_GENETICS_KEY = ComponentRegistry.getOrCreate(Aether.locate("moa_genetics"), MoaGenetics.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(MoaEntity.class, MOA_GENETICS_KEY, (moa) -> {
            if(moa != null) {
                return MoaGenetics.initMoa(moa);
            }
            return new MoaGenetics(moa);
        });
    }
}
