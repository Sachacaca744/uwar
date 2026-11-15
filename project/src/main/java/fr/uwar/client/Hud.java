/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package fr.uwar.client;

import fr.uwar.Aircraft;
import fr.uwar.W;
import fr.uwar.client.ClientHooks;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class Hud {
    static final int GREEN = -10944646;
    static final int AMBER = -16320;
    static final int RED = -47803;
    static final int DIM = -1722220678;
    static final int BG = 0x66000000;
    private static final String[] CARD = new String[]{"N", "NE", "E", "SE", "S", "SO", "O", "NO"};

    static int[] project(Vec3 p, int w, int h) {
        Minecraft mc = Minecraft.getInstance();
        Camera cam = mc.gameRenderer.getMainCamera();
        Vec3 rel = p.subtract(cam.getPosition());
        Vector3f look = cam.getLookVector();
        Vector3f up = cam.getUpVector();
        Vector3f left = cam.getLeftVector();
        double z = rel.x * (double)look.x + rel.y * (double)look.y + rel.z * (double)look.z;
        if (z < 0.5) {
            return null;
        }
        double x = rel.x * (double)left.x + rel.y * (double)left.y + rel.z * (double)left.z;
        double y = rel.x * (double)up.x + rel.y * (double)up.y + rel.z * (double)up.z;
        double th = Math.tan(Math.toRadians(((Integer)mc.options.fov().get()).intValue()) / 2.0);
        double asp = (double)w / (double)h;
        int sx = (int)((double)w / 2.0 - x / (z * th * asp) * ((double)w / 2.0));
        int sy = (int)((double)h / 2.0 - y / (z * th) * ((double)h / 2.0));
        return new int[]{sx, sy};
    }

    static void bar(GuiGraphics g, int x, int y, int w, int h, float k, int col) {
        g.fill(x - 1, y - 1, x + w + 1, y + h + 1, -1442840576);
        g.fill(x, y, x + w, y + h, 0x55000000);
        g.fill(x, y, x + (int)((float)w * Mth.clamp((float)k, (float)0.0f, (float)1.0f)), y + h, col);
    }

    static void box(GuiGraphics g, int cx, int cy, int r, int c) {
        int l = r / 2;
        for (int sx = -1; sx <= 1; sx += 2) {
            for (int sy = -1; sy <= 1; sy += 2) {
                g.fill(cx + sx * r - (sx > 0 ? l : 0), cy + sy * r, cx + sx * r + (sx > 0 ? 1 : l), cy + sy * r + 1, c);
                g.fill(cx + sx * r, cy + sy * r - (sy > 0 ? l : 0), cx + sx * r + 1, cy + sy * r + (sy > 0 ? 1 : l), c);
            }
        }
    }

    public static void render(GuiGraphics g, int w, int h, float pt) {
        Vec3 apt;
        int[] s;
        float yaw;
        int[] n;
        Minecraft mc = Minecraft.getInstance();
        Aircraft a = ClientHooks.mine();
        if (a == null || mc.level == null || mc.player == null) {
            return;
        }
        Font f = mc.font;
        long t = mc.level.getGameTime();
        boolean blink = t / 8L % 2L == 0L;
        int cx = w / 2;
        int cy = h / 2;
        g.fill(cx - 12, cy, cx - 4, cy + 1, -10944646);
        g.fill(cx + 4, cy, cx + 12, cy + 1, -10944646);
        g.fill(cx, cy - 12, cx + 1, cy - 4, -10944646);
        g.fill(cx, cy + 4, cx + 1, cy + 12, -10944646);
        g.fill(cx - 1, cy - 1, cx + 2, cy + 2, -10944646);
        Vec3 eye = a.position().add(0.0, (double)a.kind.h * 0.6, 0.0);
        if (!a.kind.heli() && (n = Hud.project(eye.add(a.aimDir().scale(120.0)), w, h)) != null) {
            g.fill(n[0] - 6, n[1], n[0] - 2, n[1] + 1, -16320);
            g.fill(n[0] + 3, n[1], n[0] + 7, n[1] + 1, -16320);
            g.fill(n[0], n[1] - 6, n[0] + 1, n[1] - 2, -16320);
            g.fill(n[0] - 1, n[1] - 1, n[0] + 2, n[1] + 2, -16320);
        }
        if ((yaw = Mth.wrapDegrees((float)(a.getYRot() + 180.0f))) < 0.0f) {
            yaw += 360.0f;
        }
        String hdg = String.format("%03d %s", Math.round(yaw) % 360, CARD[Math.floorMod(Math.round(yaw / 45.0f), 8)]);
        g.drawCenteredString(f, hdg, cx, 14, -10944646);
        g.fill(cx - 40, 24, cx + 41, 25, -1722220678);
        g.fill(cx, 24, cx + 1, 29, -10944646);
        Vec3 v = a.getDeltaMovement();
        double kmh = a.vel.length() * 72.0;
        if (a.isControlledByLocalInstance()) {
            kmh = v.length() * 72.0;
        }
        int x0 = 16;
        int y0 = h / 2 - 34;
        g.fill(x0 - 4, y0 - 4, x0 + 104, y0 + 70, 0x66000000);
        g.drawString(f, "VIT  " + Math.round(kmh) + " km/h", x0, y0, -10944646, false);
        double agl = Hud.agl(a);
        g.drawString(f, "ALT  " + Math.round(a.getY()), x0, y0 + 12, -10944646, false);
        g.drawString(f, "SOL  " + String.valueOf(agl < 0.0 ? "--" : Long.valueOf(Math.round(agl))), x0, y0 + 24, agl >= 0.0 && agl < 12.0 && !a.grounded() && v.y < -0.2 ? -47803 : -10944646, false);
        g.drawString(f, a.kind.heli() ? "COLL " : "GAZ  ", x0, y0 + 36, -10944646, false);
        Hud.bar(g, x0 + 36, y0 + 37, 62, 6, a.kind.heli() ? a.thr() : a.thrust, -10944646);
        float fk = a.fuel() / a.kind.fuelCap;
        float hk = a.hp() / a.kind.maxHp;
        g.drawString(f, "CARB", x0, y0 + 48, fk < 0.2f ? -47803 : -10944646, false);
        Hud.bar(g, x0 + 36, y0 + 49, 62, 6, fk, fk < 0.2f ? -47803 : (fk < 0.4f ? -16320 : -10944646));
        g.drawString(f, "COQUE", x0, y0 + 60, hk < 0.3f ? -47803 : -10944646, false);
        Hud.bar(g, x0 + 36, y0 + 61, 62, 6, hk, hk < 0.3f ? -47803 : (hk < 0.6f ? -16320 : -10944646));
        List<W> ld = a.loaded();
        int ax = w - 178;
        int ay = h / 2 - 34;
        g.fill(ax - 6, ay - 4, w - 12, ay + 14 + ld.size() * 12, 0x66000000);
        g.drawString(f, "ARMEMENT  (5 : config)", ax, ay, -1722220678, false);
        for (int i = 0; i < ld.size(); ++i) {
            W wp = ld.get(i);
            boolean sel = wp == a.selected();
            int am = a.ammo(wp);
            int col = sel ? (am > 0 ? -10944646 : -47803) : -1722220678;
            g.drawString(f, (sel ? "> " : "  ") + wp.shortName, ax, ay + 12 + i * 12, col, false);
            String s2 = am + "/" + a.cap(wp);
            g.drawString(f, s2, w - 18 - f.width(s2), ay + 12 + i * 12, col, false);
        }
        if ((a.selected() == W.CRUISE || a.selected() == W.BALLISTIC) && (s = Hud.project(apt = a.aimPoint((Level)mc.level, eye, a.aimDir()), w, h)) != null) {
            Hud.box(g, s[0], s[1], 10, -16320);
            g.drawString(f, "POINT VISE " + Math.round(apt.distanceTo(eye)) + " m", s[0] + 14, s[1] - 4, -16320, false);
        }
        if (Aircraft.lockWeapon(a.selected())) {
            Entity tg = mc.level.getEntity(a.lockId());
            if (tg != null) {
                s = Hud.project(tg.getBoundingBox().getCenter(), w, h);
                if (s != null) {
                    int c = blink ? -47803 : -16320;
                    Hud.box(g, s[0], s[1], 14, c);
                    g.drawString(f, "VERROUILLE " + Math.round(tg.distanceTo((Entity)a)) + " m", s[0] + 18, s[1] - 4, c, false);
                }
                g.drawCenteredString(f, "CIBLE VERROUILLEE", cx, h - 74, blink ? -47803 : -16320);
            } else {
                g.drawCenteredString(f, "RECHERCHE DE CIBLE...", cx, h - 74, -1722220678);
            }
        }
        int wy = 40;
        if (!a.kind.heli() && !a.grounded() && a.speedF < 0.45f && a.engineOn() == a.engineOn() && a.fuel() > 0.0f) {
            if (blink) {
                g.drawCenteredString(f, "DECROCHAGE", cx, wy, -47803);
            }
            wy += 12;
        }
        if (a.fuel() <= 0.0f) {
            g.drawCenteredString(f, "PLUS DE CARBURANT", cx, wy, -47803);
            wy += 12;
        } else if (fk < 0.2f) {
            if (blink) {
                g.drawCenteredString(f, "CARBURANT FAIBLE", cx, wy, -16320);
            }
            wy += 12;
        }
        if (hk < 0.3f) {
            if (blink) {
                g.drawCenteredString(f, "COQUE CRITIQUE", cx, wy, -47803);
            }
            wy += 12;
        }
        if (a.ammo(a.selected()) <= 0) {
            g.drawCenteredString(f, "MUNITIONS VIDES - R : RECHARGER", cx, wy, -16320);
        }
        String h1 = a.kind.heli() ? "ZQSD/WASD : vol   Espace : monter   Ctrl : descendre   Souris : cap" : "W/S : gaz   Souris : cap et assiette   Espace : postcombustion   Ctrl : frein";
        g.drawCenteredString(f, h1, cx, h - 58, -2007433350);
        g.drawCenteredString(f, "Clic gauche : tirer   G : arme suivante   R : recharger   Maj : sortir", cx, h - 48, -2007433350);
    }

    static double agl(Aircraft a) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 p = a.position();
        BlockHitResult r = mc.level.clip(new ClipContext(p, p.add(0.0, -400.0, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)a));
        return r.getType() == HitResult.Type.MISS ? -1.0 : p.y - r.getLocation().y;
    }
}

