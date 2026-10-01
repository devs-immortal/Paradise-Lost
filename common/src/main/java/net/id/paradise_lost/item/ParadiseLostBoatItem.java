package net.id.paradise_lost.item;

import net.id.paradise_lost.entity.vehicle.ParadiseLostBoatEntity;
import net.id.paradise_lost.entity.vehicle.ParadiseLostBoatType;
import net.id.paradise_lost.entity.vehicle.ParadiseLostChestBoatEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;
import net.id.paradise_lost.registry.EntityRegistry;

public class ParadiseLostBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    private final ParadiseLostBoatType wood;
    private final boolean hasChest;

    public ParadiseLostBoatItem(ParadiseLostBoatType wood, boolean hasChest, Item.Properties properties) {
        super(properties);
        this.wood = wood;
        this.hasChest = hasChest;
    }

    public ParadiseLostBoatType getWood() {
        return wood;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }
        Vec3 view = player.getViewVector(1.0F);
        List<Entity> nearby = level.getEntities(player, player.getBoundingBox().expandTowards(view.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
        if (!nearby.isEmpty()) {
            Vec3 eyes = player.getEyePosition();
            for (Entity entity : nearby) {
                AABB box = entity.getBoundingBox().inflate((double) entity.getPickRadius());
                if (box.contains(eyes)) {
                    return InteractionResultHolder.pass(stack);
                }
            }
        }
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        Boat boat = createBoat(level, hit.getLocation());
        boat.setYRot(player.getYRot());
        if (!level.noCollision(boat, boat.getBoundingBox())) {
            return InteractionResultHolder.fail(stack);
        }
        if (!level.isClientSide) {
            level.addFreshEntity(boat);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, hit.getLocation());
            stack.consume(1, player);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private Boat createBoat(Level level, Vec3 pos) {
        return hasChest
                ? new ParadiseLostChestBoatEntity(EntityRegistry.chestBoatType(wood), level, wood, pos.x, pos.y, pos.z)
                : new ParadiseLostBoatEntity(EntityRegistry.boatType(wood), level, wood, pos.x, pos.y, pos.z);
    }
}
