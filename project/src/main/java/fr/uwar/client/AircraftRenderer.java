/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 */
package fr.uwar.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.uwar.Aircraft;
import fr.uwar.W;
import fr.uwar.client.Mesh;
import fr.uwar.client.Models;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AircraftRenderer
extends EntityRenderer<Aircraft> {
    public AircraftRenderer(EntityRendererProvider.Context c) {
        super(c);
        this.shadowRadius = 1.2f;
    }

    public ResourceLocation getTextureLocation(Aircraft a) {
        return new ResourceLocation("uwar", "textures/entity/" + a.kind.id + ".png");
    }

    public boolean shouldRender(Aircraft a, Frustum f, double x, double y, double z) {
        return true;
    }

    public static void draw(Mesh m, PoseStack.Pose pose, VertexConsumer vc, int light, float alpha) {
        for (float[] q : m.q) {
            if (q.length < 24) continue;
            int mat = (int)q[23];
            int l = mat == 7 || mat == 14 || mat == 15 ? 0xF000F0 : light;
            for (int i = 0; i < 4; ++i) {
                vc.vertex(pose.pose(), q[i * 3], q[i * 3 + 1], q[i * 3 + 2]).color(1.0f, 1.0f, 1.0f, alpha).uv(q[12 + i * 2], q[13 + i * 2]).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(l).normal(pose.normal(), q[20], q[21], q[22]).endVertex();
            }
        }
    }

    private static void station(PoseStack ps, float[] p, float sc, Mesh mesh, VertexConsumer vc, int light) {
        ps.pushPose();
        ps.translate(p[0], p[1], p[2]);
        ps.scale(sc, sc, sc);
        AircraftRenderer.draw(mesh, ps.last(), vc, light, 1.0f);
        ps.popPose();
    }

    public void render(Aircraft a, float yaw, float pt, PoseStack ps, MultiBufferSource buf, int light) {
        Models.Model m = Models.get(a.kind);
        ResourceLocation tex = this.getTextureLocation(a);
        VertexConsumer vc = buf.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)tex));
        Minecraft mc = Minecraft.getInstance();
        boolean inside = mc.options.getCameraType().isFirstPerson() && mc.player != null && a.hasPassenger((Entity)mc.player);
        float bodyYaw = Mth.rotLerp((float)pt, (float)a.yRotO, (float)a.getYRot());
        ps.pushPose();
        ps.mulPose(Axis.YP.rotationDegrees(-bodyYaw));
        ps.translate(0.0f, m.pivotY, 0.0f);
        ps.mulPose(Axis.XP.rotationDegrees(Mth.lerp((float)pt, (float)a.xRotO, (float)a.getXRot())));
        ps.mulPose(Axis.ZP.rotationDegrees(Mth.lerp((float)pt, (float)a.rollO, (float)a.roll)));
        ps.translate(0.0f, -m.pivotY, 0.0f);
        if (!inside) {
            AircraftRenderer.draw(m.body, ps.last(), vc, light, 1.0f);
            if (a.grounded() || a.kind.heli() && a.getDeltaMovement().lengthSqr() < 0.02) {
                AircraftRenderer.draw(m.gear, ps.last(), vc, light, 1.0f);
            }
            for (Models.Ord o : m.ord) {
                if (a.ammo(W.values()[o.weapon]) <= o.idx) continue;
                AircraftRenderer.draw(o.m, ps.last(), vc, light, 1.0f);
            }
            int ms = 0;
            int bs = 0;
            for (W w : a.loaded()) {
                int k;
                int n = Math.min(2, a.ammo(w));
                if (w.missileLike()) {
                    for (k = 0; k < n && ms < m.missSt.size(); ++k) {
                        AircraftRenderer.station(ps, m.missSt.get(ms++), w.visScale(), Models.missile, vc, light);
                    }
                    continue;
                }
                if (!w.bombLike()) continue;
                for (k = 0; k < n && bs < m.bombSt.size(); ++k) {
                    AircraftRenderer.station(ps, m.bombSt.get(bs++), w.visScale() * 1.15f, Models.bomb, vc, light);
                }
            }
        }
        float rot = Mth.lerp((float)pt, (float)a.rotorO, (float)a.rotor);
        if (a.kind.heli() && !inside) {
            VertexConsumer vt = null;
            ps.pushPose();
            ps.translate(m.rx, m.ry, m.rz);
            ps.mulPose(Axis.YP.rotationDegrees(-rot));
            AircraftRenderer.draw(m.rotor, ps.last(), vc, light, 1.0f);
            if (a.engineOn()) {
                vt = buf.getBuffer(RenderType.entityTranslucent((ResourceLocation)tex));
                AircraftRenderer.draw(m.rotorDisc, ps.last(), vt, light, 0.28f);
            }
            ps.popPose();
            if (!inside) {
                ps.pushPose();
                ps.translate(m.tx, m.ty, m.tz);
                ps.mulPose(Axis.ZP.rotationDegrees(90.0f));
                ps.mulPose(Axis.YP.rotationDegrees(rot * 1.7f));
                AircraftRenderer.draw(m.tail, ps.last(), vc, light, 1.0f);
                if (a.engineOn()) {
                    if (vt == null) {
                        vt = buf.getBuffer(RenderType.entityTranslucent((ResourceLocation)tex));
                    }
                    AircraftRenderer.draw(m.tailDisc, ps.last(), vt, light, 0.25f);
                }
                ps.popPose();
            }
        } else if (!a.kind.heli() && a.engineOn() && (a.thr() > 0.35f || a.boosting())) {
            float k = a.boosting() ? 1.25f : 0.5f + a.thr() * 0.6f;
            float pul = 1.0f + 0.08f * (float)Math.sin(((float)a.tickCount + pt) * 2.7f);
            ps.pushPose();
            ps.translate(0.0f, 1.0f, -6.0f);
            ps.scale(0.8f, 0.8f, k * pul);
            ps.translate(0.0f, -1.0f, 6.0f);
            AircraftRenderer.draw(m.flame, ps.last(), vc, light, 1.0f);
            ps.popPose();
        }
        ps.popPose();
    }
}

