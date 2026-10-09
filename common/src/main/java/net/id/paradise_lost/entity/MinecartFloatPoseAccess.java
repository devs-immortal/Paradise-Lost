package net.id.paradise_lost.entity;

public interface MinecartFloatPoseAccess {
    void paradiseLost$setRailRenderPose(float yaw, float pitch);

    float paradiseLost$getRailRenderYaw();

    float paradiseLost$getRailRenderPitch();

    boolean paradiseLost$hasRailRenderPose();

    void paradiseLost$clearRailRenderPose();
}
