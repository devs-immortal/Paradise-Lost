package net.id.paradiselost.component;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class ParadiseLostComponents implements EntityComponentInitializer {

    public static final ComponentKey<MoaGenes> MOA_GENETICS_KEY = ComponentRegistry.getOrCreate(ParadiseLost.locate("moa_genetics"), MoaGenes.class);
    
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(MoaEntity.class, MOA_GENETICS_KEY, moa -> new MoaGenes());
    }
}
