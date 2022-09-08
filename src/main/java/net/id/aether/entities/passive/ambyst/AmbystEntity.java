package net.id.aether.entities.passive.ambyst;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.passive.AetherAnimalEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AmbystEntity extends AetherAnimalEntity implements AngledModelEntity {
    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super AmbystEntity>>> SENSORS;
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_MODULES;
    private final Map<String, Vec3f> modelAngles = Maps.newHashMap();

    public AmbystEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.moveControl = new AquaticMoveControl(this, 20, 20, 1, 1, false);
        //this.lookControl = new AquaticLookControl(this, 180);
        //this.stepHeight = 1;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.655F;
    }

    public static DefaultAttributeContainer.Builder createAmbystAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected Brain.Profile<AmbystEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return AmbystBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    public Brain<AmbystEntity> getBrain() {
        return (Brain<AmbystEntity>) super.getBrain();
    }

    @Override
    protected void mobTick() {
        this.world.getProfiler().push("ambystBrain");
        this.getBrain().tick((ServerWorld) this.world, this);
        this.world.getProfiler().pop();
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        //this.navigation.startMovingTo(player, .5);
        //this.setBodyYaw(this.bodyYaw +10);
        //this.setHeadYaw(this.getHeadYaw() + 10);
        //this.setYaw(this.getYaw() +10);
        this.lookControl.lookAt(player);
        return ActionResult.SUCCESS;
    }

    public EntityGroup getGroup() {
        return EntityGroup.AQUATIC;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public Map<String, Vec3f> getModelAngles() {
        return this.modelAngles;
    }

    static {
        SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, AetherEntityTypes.WEATHER_SENSOR, AetherEntityTypes.FINDLOG_SENSOR);
        MEMORY_MODULES = ImmutableList.of(AetherEntityTypes.IS_RAINING_MEMORY, AetherEntityTypes.LOG_OPENING_MEMORY, AetherEntityTypes.LOG_MEMORY, MemoryModuleType.BREED_TARGET, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.PLAY_DEAD_TICKS, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN);
    }
}