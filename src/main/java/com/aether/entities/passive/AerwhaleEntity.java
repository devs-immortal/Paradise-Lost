package com.aether.entities.passive;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class AerwhaleEntity extends FlyingEntity {
    public AerwhaleEntity(EntityType<? extends AerwhaleEntity> type, World worldIn) {
        super(type, worldIn);
        this.setYaw(360.0F * this.random.nextFloat());
        this.setPitch(90.0F * this.random.nextFloat() - 45.0F);
        //        this.ignoreCameraFrustum = true;
        this.moveControl = new AerwhaleEntity.AerwhaleMoveControl(this);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new AerwhaleEntity.FlyRandomlyGoal(this));
        //		this.goalSelector.add(7, new AerwhaleEntity.LookAroundGoal(this));
        //		this.goalSelector.add(1, new AerwhaleEntity.UnstuckGoal(this));
        //		this.goalSelector.add(5, new AerwhaleEntity.TravelCourseGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAerwhaleAttributes() {
        return createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0);
    }

    @Override
    public boolean canSpawn(WorldAccess worldIn, SpawnReason spawnReasonIn) {
        BlockPos pos = new BlockPos(MathHelper.floor(this.getX()), MathHelper.floor(this.getBoundingBox().minY), MathHelper.floor(this.getZ()));

        return this.random.nextInt(65) == 0 && !worldIn.getBlockCollisions(this, this.getBoundingBox()).findAny().isPresent()
                && !worldIn.containsFluid(this.getBoundingBox()) && worldIn.getLightLevel(pos) > 8
                && super.canSpawn(worldIn, spawnReasonIn);
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        this.extinguish();

        if (this.getY() < this.world.getBottomY()) {
            this.discard();
        }
    }

    @Override
    public void travel(Vec3d positionIn) {
        List<Entity> passengers = this.getPassengerList();
        if (!passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                this.setYaw(player.getYaw());
                this.prevYaw = this.getYaw();

                this.setPitch(player.getPitch());
                this.prevPitch = this.getPitch();

                this.headYaw = player.headYaw;

                positionIn = new Vec3d(player.sidewaysSpeed, 0.0, (player.forwardSpeed <= 0.0F) ? player.forwardSpeed * 0.25F : player.forwardSpeed);

                // TODO: Get replacement for isJumping
                boolean isJumping = false;

                if (isJumping) {
                    this.setVelocity(new Vec3d(0.0, 0.0, 0.0));
                } else {
                    double d0 = Math.toRadians(player.getYaw() - 90.0);
                    double d1 = Math.toRadians(-player.getPitch());
                    double d2 = Math.cos(d1);
                    this.setVelocity(
                            0.98 * (this.getVelocity().x + 0.05 * Math.cos(d0) * d2),
                            0.98 * (this.getVelocity().y + 0.02 * Math.sin(d1)),
                            0.98 * (this.getVelocity().z + 0.05 * Math.sin(d0) * d2));
                }

                this.stepHeight = 1.0F;

                if (!this.world.isClient()) {
                    this.flyingSpeed = this.getMovementSpeed() * 0.6F;
                    super.travel(positionIn);
                }

                this.lastLimbDistance = this.limbDistance;
                double d0 = this.getX() - this.prevX;
                double d1 = this.getZ() - this.prevZ;
                float f4 = 4.0F * MathHelper.sqrt((float) (d0 * d0 + d1 * d1));

                if (f4 > 1.0F) {
                    f4 = 1.0F;
                }

                this.limbDistance += 0.4F * (f4 - this.limbDistance);
                this.limbAngle += this.limbDistance;
            }
        } else {
            this.stepHeight = 0.5F;
            this.flyingSpeed = 0.02F;
            super.travel(positionIn);
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getUuid().getMostSignificantBits() == 220717875589366683L && player.getUuid().getLeastSignificantBits() == -7181826737698904209L) {
            player.startRiding(this);
            if (!this.world.isClient()) {
                BaseText msg = new LiteralText("Serenity is the queen of W(h)ales!!");
                player.world.getPlayers().forEach(p -> p.sendSystemMessage(msg, player.getUuid()));
            }
            return ActionResult.success(this.world.isClient());
        }
        return super.interactMob(player, hand);
    }

    // TODO: Create Sounds for Aerwhales
    //    @Override
    //    protected SoundEvent getAmbientSound() {
    //        return AetherSoundEvents.ENTITY_AERWHALE_AMBIENT;
    //    }
    //
    //    @Override
    //    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    //        return AetherSoundEvents.ENTITY_AERWHALE_DEATH;
    //    }
    //
    //    @Override
    //    protected SoundEvent getDeathSound() {
    //        return AetherSoundEvents.ENTITY_AERWHALE_DEATH;
    //    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    @SuppressWarnings("JavadocReference")
    static class AerwhaleMoveControl extends MoveControl {
        private final AerwhaleEntity aerwhale;
        private int collisionCheckCooldown;

        public AerwhaleMoveControl(AerwhaleEntity aerwhale) {
            super(aerwhale);
            this.aerwhale = aerwhale;
        }

        @Override
        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                double dz = this.targetZ - this.aerwhale.getZ();
                double dx = this.targetX - this.aerwhale.getX();
                
                // rotate whale in small steps
                float yawPlus90 = MathHelper.wrapDegrees(this.aerwhale.getYaw() + 90.0F);
                float nextYawPlus90 = MathHelper.wrapDegrees((float) MathHelper.atan2(dz, dx) * 57.295776F);
                this.aerwhale.setYaw(MathHelper.stepUnwrappedAngleTowards(yawPlus90, nextYawPlus90, 4.0F) - 90.0F);
                this.aerwhale.bodyYaw = this.aerwhale.getYaw();
                
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.aerwhale.getRandom().nextInt(5) + 2;

                    Vec3d vec3d = new Vec3d(dx, this.targetY - this.aerwhale.getY(), dz);

                    double length = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(length))) {
                        this.aerwhale.setVelocity(this.aerwhale.getVelocity().add(vec3d.multiply(0.05D)));
                    } else {
                        this.state = MoveControl.State.WAIT;
                    }
                }
            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.aerwhale.getBoundingBox();

            for (int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.aerwhale.world.isSpaceEmpty(this.aerwhale, box)) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * Copied from {@link net.minecraft.entity.mob.GhastEntity.FlyRandomlyGoal}
     */
    static class FlyRandomlyGoal extends Goal {
        private final AerwhaleEntity aerwhale;

        public FlyRandomlyGoal(AerwhaleEntity aerwhale) {
            this.aerwhale = aerwhale;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            MoveControl moveControl = this.aerwhale.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            }
            double dx = moveControl.getTargetX() - this.aerwhale.getX();
            double dy = moveControl.getTargetY() - this.aerwhale.getY();
            double dz = moveControl.getTargetZ() - this.aerwhale.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;
            return distSq < 1.0D || distSq > 3600.0D;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            // Move somewhere within a 16x16x16 box around the entity
            Random random = this.aerwhale.getRandom();
            double x = this.aerwhale.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double y = this.aerwhale.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            y = MathHelper.clamp(y, 20, 90);
            double z = this.aerwhale.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            this.aerwhale.getMoveControl().moveTo(x, y, z, 1.0D);
        }
    }

    /*
    public static class UnstuckGoal extends Goal {
    	private final AerwhaleEntity aerwhale;
    	private boolean stuckWarning = false;
    	private long checkTime;
    	private BlockPos checkPos;
    
    	public UnstuckGoal(AerwhaleEntity aerwhale) {
    		this.aerwhale = aerwhale;
    		this.setMutexFlags(EnumSet.of(Flag.MOVE));
    	}
    
    	@Override
    	public boolean shouldExecute() {
    		return this.aerwhale.isAlive() && isColliding();
    	}
    
    	@Override
    	public void tick() {
    		System.out.println("UnstuckGoal tick");
    		BlockPos pos = this.aerwhale.getPosition();
    		BlockPos posUp = pos.up(3);
    		BlockPos posDown = pos.down();
    		BlockPos posNorth = pos.north(2).up();
    		BlockPos posSouth = pos.south(2).up();
    		BlockPos posEast = pos.east(2).up();
    		BlockPos posWest = pos.west(2).up();
    
    		double addMotionX = 0.0;
    		double addMotionY = 0.0;
    		double addMotionZ = 0.0;
    
    		if (checkRegion(posUp.add(-1, 0, -1), posUp.add(1, 0, 1))) {
    			addMotionY += 0.002;
    		}
    		if (checkRegion(posDown.add(-1, 0, -1), posDown.add(1, 0, 1))) {
    			addMotionY -= 0.002;
    		}
    		if (checkRegion(posEast.add(0, -1, -1), posEast.add(0, 1, 1))) {
    			addMotionX += 0.002;
    			addMotionY += 0.002;
    		}
    		if (checkRegion(posWest.add(0, -1, -1), posWest.add(0, 1, 1))) {
    			addMotionX -= 0.002;
    			addMotionY += 0.002;
    		}
    		if (checkRegion(posSouth.add(-1, -1, 0), posSouth.add(1, 1, 0))) {
    			addMotionZ += 0.002;
    			addMotionY += 0.002;
    		}
    		if (checkRegion(posNorth.add(-1, -1, 0), posNorth.add(1, 1, 0))) {
    			addMotionZ -= 0.002;
    			addMotionY += 0.002;
    		}
    		this.aerwhale.setMotion(this.aerwhale.getMotion().add(addMotionX, addMotionY, addMotionZ));
    	}
    
    	public boolean checkRegion(BlockPos pos1, BlockPos pos2) {
    		boolean allAir = BlockPos.getAllInBox(pos1, pos2).allMatch(pos -> this.aerwhale.world.getBlockState(pos).isAir(this.aerwhale.world, pos));
    		return !allAir;
    	}
    
    	@Override
    	public void resetTask() {
    		this.stuckWarning = false;
    		this.checkPos = null;
    	}
    
    	public boolean isColliding() {
    		long curtime = System.currentTimeMillis();
    		if (curtime <= checkTime + 1000L) {
    			return false;
    		}
    
    		if (this.checkPos != null) {
    
    			double distanceTravelledSquared = this.aerwhale.getPosition().distanceSq(this.checkPos);
    
    			if (distanceTravelledSquared < 9) {
    				if (!stuckWarning) {
    					stuckWarning = true;
    				} else {
    					return true;
    				}
    			}
    
    		}
    
    		this.checkPos = this.aerwhale.getPosition();
    		this.checkTime = curtime;
    
    		return false;
    	}
    }
    
    public static class TravelCourseGoal extends Goal {
    	private final AerwhaleEntity aerwhale;
    	private double motionYaw, motionPitch;
    	private double originDir, westDir, eastDir, upDir, downDir;
    
    	public TravelCourseGoal(AerwhaleEntity aerwhale) {
    		this.aerwhale = aerwhale;
    		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    	}
    
    	@Override
    	public boolean shouldExecute() {
    		return this.aerwhale.isAlive();
    	}
    
    	@Override
    	public void tick() {
    		System.out.println("TravelCourseGoal tick");
    		if (this.aerwhale.isBeingRidden()) {
    			return;
    		}
    
    		this.originDir = this.checkForTravelableCourse(0, 0);
    		this.westDir = this.checkForTravelableCourse(45, 0);
    		this.upDir = this.checkForTravelableCourse(0, 45);
    		this.eastDir = this.checkForTravelableCourse(-45, 0);
    		this.downDir = this.checkForTravelableCourse(0, -45);
    
    		int course = this.getCorrectCourse();
    
    		if (course == 0) {
    			if (this.originDir == 50) {
    				this.motionYaw *= 0.9F;
    				this.motionPitch *= 0.9F;
    
    				if (this.aerwhale.getPosY() > 100) {
    					this.motionPitch -= 2.0F;
    				}
    				if (this.aerwhale.getPosY() < 20) {
    					this.motionPitch += 2.0F;
    				}
    			}
    			else {
    				this.aerwhale.rotationPitch = -this.aerwhale.rotationPitch;
    				this.aerwhale.rotationYaw = -this.aerwhale.rotationYaw;
    			}
    		}
    		else if (course == 1) {
    			this.motionYaw += 5.0F;
    		}
    		else if (course == 2) {
    			this.motionPitch -= 5.0F;
    		}
    		else if (course == 3) {
    			this.motionYaw -= 5.0F;
    		}
    		else {
    			this.motionPitch += 5.0F;
    		}
    
    		this.motionYaw += 2.0F * this.aerwhale.getRNG().nextFloat() - 1.0F;
    		this.motionPitch += 2.0F * this.aerwhale.getRNG().nextFloat() - 1.0F;
    
    		this.aerwhale.rotationPitch += 0.1F * this.motionPitch;
    		this.aerwhale.rotationYaw += 0.1F * this.motionYaw;
    
    		this.aerwhale.rotationPitch += 0.1F * this.motionPitch;
    		this.aerwhale.rotationYaw += 0.1F * this.motionYaw;
    
    		if (this.aerwhale.rotationPitch < -60) {
    			this.aerwhale.rotationPitch = -60;
    		}
    
    		if (this.aerwhale.rotationPitch < -60) {
    			this.aerwhale.rotationPitch = -60;
    		}
    
    		if (this.aerwhale.rotationPitch > 60) {
    			this.aerwhale.rotationPitch = 60;
    		}
    
    		if (this.aerwhale.rotationPitch > 60) {
    			this.aerwhale.rotationPitch = 60;
    		}
    
    		this.aerwhale.rotationPitch *= 0.99D;
    
    		double d0 = Math.toRadians(this.aerwhale.rotationYaw);
    		double d1 = Math.toRadians(this.aerwhale.rotationPitch);
    		double d2 = Math.cos(d1);
    
    		this.aerwhale.setMotion(this.aerwhale.getMotion().add(0.005 * Math.cos(d0) * d2, 0.005 * Math.sin(d1), 0.005 * Math.sin(d0) * d2).scale(0.98));
    
    		if (this.aerwhale.getMotion().x > 0 && !this.aerwhale.world.isAirBlock(this.aerwhale.getPosition().east())) {
    			this.aerwhale.setMotion(-this.aerwhale.getMotion().x, this.aerwhale.getMotion().y, this.aerwhale.getMotion().z);
    			this.motionYaw -= 10F;
    		}
    		else if (this.aerwhale.getMotion().x < 0 && !this.aerwhale.world.isAirBlock(this.aerwhale.getPosition().west())) {
    			this.aerwhale.setMotion(-this.aerwhale.getMotion().x, this.aerwhale.getMotion().y, this.aerwhale.getMotion().z);
    			this.motionYaw += 10F;
    		}
    		else if (this.aerwhale.getMotion().y > 0 && !this.aerwhale.world.isAirBlock(this.aerwhale.getPosition().up())) {
    			this.aerwhale.setMotion(this.aerwhale.getMotion().x, -this.aerwhale.getMotion().y, this.aerwhale.getMotion().z);
    			this.motionPitch -= 10F;
    		}
    		else if (this.aerwhale.getMotion().y < 0 && !this.aerwhale.world.isAirBlock(this.aerwhale.getPosition().down())) {
    			this.aerwhale.setMotion(this.aerwhale.getMotion().x, -this.aerwhale.getMotion().y, this.aerwhale.getMotion().z);
    			this.motionPitch += 10F;
    		}
    
    		if (this.aerwhale.getMotion().z > 0D && !this.aerwhale.world.isAirBlock(this.aerwhale.getPosition().south())) {
    			this.aerwhale.setMotion(this.aerwhale.getMotion().x, this.aerwhale.getMotion().y, -this.aerwhale.getMotion().z);
    			this.motionYaw -= 10F;
    		}
    		else if (this.aerwhale.getMotion().z < 0 && !this.aerwhale.world.isAirBlock(this.aerwhale.getPosition().north())) {
    			this.aerwhale.setMotion(this.aerwhale.getMotion().x, this.aerwhale.getMotion().y, -this.aerwhale.getMotion().z);
    			this.motionYaw += 10F;
    		}
    
    		this.aerwhale.move(MoverType.SELF, this.aerwhale.getMotion());
    	}
    
    	private double checkForTravelableCourse(float rotationYawOffset, float rotationPitchOffset) {
    		double standard = 50D;
    
    		float yaw = this.aerwhale.rotationYaw + rotationYawOffset;
    		float pitch = this.aerwhale.rotationPitch + rotationPitchOffset;
    
    		float f3 = MathHelper.cos(-yaw * 0.01745329F - (float)Math.PI);
    		float f4 = MathHelper.sin(-yaw * 0.01745329F - (float)Math.PI);
    		float f5 = MathHelper.cos(-pitch * 0.01745329F);
    		float f6 = MathHelper.sin(-pitch * 0.01745329F);
    
    		float f7 = f4 * f5;
    		float f8 = f6;
    		float f9 = f3 * f5;
    
    		Vector3d Vector3d = new Vector3d(this.aerwhale.getPosX(), this.aerwhale.getBoundingBox().minY, this.aerwhale.getPosZ());
    		Vector3d Vector3d1 = Vector3d.add(f7 * standard, f8 * standard, f9 * standard);
    
    		RayTraceResult movingobjectposition = this.aerwhale.world.rayTraceBlocks(new RayTraceContext(Vector3d, Vector3d1, BlockMode.COLLIDER, FluidMode.NONE, this.aerwhale));
    
    		if (movingobjectposition == null) {
    			return standard;
    		}
    
    		if (movingobjectposition.getType() == RayTraceResult.Type.BLOCK) {
    			double i = movingobjectposition.getHitVec().getX() - this.aerwhale.getPosX();
    			double j = movingobjectposition.getHitVec().getY() - this.aerwhale.getBoundingBox().minY;
    			double k = movingobjectposition.getHitVec().getZ() - this.aerwhale.getPosZ();
    			return Math.sqrt(i * i + j * j + k * k);
    		}
    
    		return standard;
    	}
    
    	private int getCorrectCourse() {
    		double[] distances = new double[] { originDir, westDir, upDir, eastDir, downDir };
    
    		int correctCourse = 0;
    
    		for (int i = 1; i < 5; i++) {
    			if (distances[i] > distances[correctCourse]) {
    				correctCourse = i;
    			}
    		}
    
    		return correctCourse;
    	}
    }
    /**/
}
