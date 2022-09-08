package net.id.aether.entities.passive.ambyst;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class AmbystBrain {

    protected static Brain<?> create(Brain<AmbystEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addHideActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.HIDE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<AmbystEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new LookAroundTask(45, 90), new WanderAroundTask(), new TemptationCooldownTask(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
    }

    private static void addIdleActivities(Brain<AmbystEntity> brain) {
        brain.setTaskList(Activity.IDLE, ImmutableList.of(Pair.of(0, new TimeLimitedTask(new FollowMobTask(EntityType.PLAYER, 6.0F), UniformIntProvider.create(30, 60))), Pair.of(1, new BreedTask(EntityType.AXOLOTL, 0.2F)), Pair.of(3, new SeekWaterTask(6, 0.15F)), Pair.of(4, new CompositeTask(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT), ImmutableSet.of(), CompositeTask.Order.ORDERED, CompositeTask.RunMode.TRY_ALL, ImmutableList.of(Pair.of(new AquaticStrollTask(0.5F), 2), Pair.of(new StrollTask(0.15F, false), 2))))));
    }

    private static void addHideActivities(Brain<AmbystEntity> brain) {
        //Run and hide when not raining
        brain.setTaskList(Activity.HIDE, 0, ImmutableList.of(new RunToLogTask()));

        //StayInLogTask will control the ambyst moving around inside the log while it's hiding there. Maybe cowering away when the player gets close
        brain.setTaskList(AetherEntityTypes.HIDEINLOG, 0, ImmutableList.of(new StayInLogTask()));
    }
    /*
        public static void updateActivities(AxolotlEntity axolotl) {
            Brain<AxolotlEntity> brain = axolotl.getBrain();
            Activity activity = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Object)null);
            if (activity != Activity.PLAY_DEAD) {
                brain.resetPossibleActivities((List)ImmutableList.of(Activity.PLAY_DEAD, Activity.FIGHT, Activity.IDLE));
                if (activity == Activity.FIGHT && brain.getFirstPossibleNonCoreActivity().orElse((Object)null) != Activity.FIGHT) {
                    brain.remember(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
                }
            }

        }

        public static Ingredient getTemptItems() {
            return Ingredient.fromTag(ItemTags.AXOLOTL_TEMPT_ITEMS);
        }

     */
}