package net.id.paradiselost.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.lore.LoreState;

public class ParadiseLostComponents implements EntityComponentInitializer {

    public static final ComponentKey<MoaGenes> MOA_GENETICS_KEY = ComponentRegistry.getOrCreate(ParadiseLost.locate("moa_genetics"), MoaGenes.class);
    public static final ComponentKey<LoreState> LORE_STATE = ComponentRegistry.getOrCreate(ParadiseLost.locate("lore_state"), LoreState.class);

    public static final ComponentKey<LUV> LUV = ComponentRegistry.getOrCreate(ParadiseLost.locate("luv"), net.id.paradiselost.component.LUV.class);
    
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(MoaEntity.class, MOA_GENETICS_KEY, moa -> new MoaGenes());
        registry.registerForPlayers(LORE_STATE, LoreState.Impl::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(LUV, net.id.paradiselost.component.LUV::new);
    }
}
