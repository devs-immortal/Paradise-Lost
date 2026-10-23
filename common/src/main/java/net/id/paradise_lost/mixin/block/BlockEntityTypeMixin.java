package net.id.paradise_lost.mixin.block;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.compat.ParadiseLostFarmersDelightCompat;
import net.id.paradise_lost.platform.Services;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    public void isValid(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (Services.PLATFORM.isModLoaded("farmersdelight")) {
            try {
                Class<?> farmersDelightBlockEntityTypes = Class.forName("vectorwing.farmersdelight.common.registry.ModBlockEntityTypes");
                Supplier<BlockEntityType<?>> cabinetBlockEntityType = (Supplier<BlockEntityType<?>>) farmersDelightBlockEntityTypes.getField("CABINET").get(null);
                BlockEntityType<?> type = (BlockEntityType<?>) (Object) this;
                if (type == cabinetBlockEntityType.get() && (
                        state.is(ParadiseLostFarmersDelightCompat.AUREL_CABINET.get()) ||
                        state.is(ParadiseLostFarmersDelightCompat.MOTHER_AUREL_CABINET.get()) ||
                        state.is(ParadiseLostFarmersDelightCompat.MENTH_CABINET.get()) ||
                        state.is(ParadiseLostFarmersDelightCompat.WISTERIA_CABINET.get())
                )) {
                    cir.setReturnValue(true);
                }
            }
            catch (Exception e) {
                ModConstants.LOGGER.warn("Failed to load Farmer's Delight Compatability Mixin!", e);
            }
        }
    }

}
