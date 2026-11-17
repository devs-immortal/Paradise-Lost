package net.id.paradise_lost.client.rendering;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.CloudStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class CloudMeshCache {
    @Nullable
    public VertexBuffer buffer;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public int z = Integer.MIN_VALUE;
    public Vec3 color = Vec3.ZERO;
    @Nullable
    public CloudStatus type;
    public boolean dirty = true;
}
