package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.tag.ParadiseLostItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ParadiseLostEntityExtensions {
    private boolean flipped = false;
    private int gravFlipTime;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean getFlipped() {
        return flipped;
    }

    @Override
    public void setFlipped() {
        flipped = true;
        gravFlipTime = 0;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (flipped) {
            gravFlipTime++;
            if (gravFlipTime > 20) {
                flipped = false;
                this.fallDistance = 0;
            }
            if (!this.hasNoGravity()) {
                Vec3d antiGravity = new Vec3d(0, 0.12D, 0);
                this.setVelocity(this.getVelocity().add(antiGravity));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    private void getMoaMaxHealth(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof MoaEntity moa) {
            var genes = moa.getGenes();
            cir.setReturnValue(genes.isInitialized() ? genes.getAttribute(MoaAttributes.MAX_HEALTH) : 40F);
            cir.cancel();
        }
    }

    @ModifyArgs(method = "dropLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;JLjava/util/function/Consumer;)V"))
    private void dropSmeltedLoot(Args args) {
        LootContextParameterSet lootContextParameterSet = args.get(0);
        var weapon = lootContextParameterSet.get(LootContextParameters.DAMAGE_SOURCE).getWeaponStack();
        if (weapon != null && weapon.isIn(ParadiseLostItemTags.IGNITING_TOOLS)) {
            args.set(2, (Consumer<ItemStack>) this::dropStackInternal);
        }
    }

    @Nullable
    private ItemEntity dropStackInternal(ItemStack stack) {
        Optional<RecipeEntry<SmeltingRecipe>> optional = this.getWorld().getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(stack), getWorld());
        if (optional.isPresent()) {
            ItemStack itemStack = (((RecipeEntry)optional.get()).value()).getResult(getWorld().getRegistryManager());
            if (!itemStack.isEmpty()) {
                return this.dropStack(itemStack.copyWithCount(stack.getCount()), 0.0F);
            }
        }
        return this.dropStack(stack);
    }

}
