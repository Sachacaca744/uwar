/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 */
package fr.uwar.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.uwar.Shell;
import fr.uwar.W;
import fr.uwar.client.AircraftRenderer;
import fr.uwar.client.Mesh;
import fr.uwar.client.Models;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ShellRenderer
extends EntityRenderer<Shell> {
    private static final ResourceLocation TEX = new ResourceLocation("uwar", "textures/entity/weapons.png");

    public ShellRenderer(EntityRendererProvider.Context c) {
        super(c);
    }

    public ResourceLocation getTextureLocation(Shell s) {
        return TEX;
    }

    public void render(Shell s, float yaw, float pt, PoseStack ps, MultiBufferSource buf, int light) {
        Mesh m;
        W w = s.weapon();
        Vec3 v = s.getDeltaMovement();
        float yy = (float)(Mth.atan2((double)(-v.x), (double)v.z) * 57.2957763671875);
        float pp = (float)(-Math.toDegrees(Math.atan2(v.y, Math.hypot(v.x, v.z))));
        if (v.lengthSqr() < 1.0E-4) {
            yy = 0.0f;
            pp = 90.0f;
        }
        ps.pushPose();
        ps.mulPose(Axis.YP.rotationDegrees(-yy));
        ps.mulPose(Axis.XP.rotationDegrees(pp));
        m = w.missileLike() ? Models.missile : (w.bombLike() ? Models.bomb : Models.rocket);
        if (w.missileLike() || w.bombLike()) {
            ps.scale(w.visScale(), w.visScale(), w.visScale());
        }
        AircraftRenderer.draw(m, ps.last(), buf.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)TEX)), light, 1.0f);
        ps.popPose();
    }
}

