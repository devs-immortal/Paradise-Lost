package net.id.aether.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.id.aether.Aether;
import net.id.aether.entities.passive.MoaEntity;

public class AetherComponents implements EntityComponentInitializer {

    public static final ComponentKey<MoaGenes> MOA_GENETICS_KEY = ComponentRegistry.getOrCreate(Aether.locate("moa_genetics"), MoaGenes.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(MoaEntity.class, MOA_GENETICS_KEY, moa -> new MoaGenes());
    }
}
