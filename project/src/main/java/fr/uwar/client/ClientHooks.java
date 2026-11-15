/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.Options
 *  net.minecraft.world.entity.Entity
 */
package fr.uwar.client;

import fr.uwar.Aircraft;
import fr.uwar.Net;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.Entity;

public class ClientHooks {
    public static void poll(Aircraft a) {
        boolean ok;
        Minecraft mc = Minecraft.getInstance();
        Options o = mc.options;
        if (mc.player == null) {
            return;
        }
        boolean bl = ok = mc.screen == null;
        float f = ok ? (float)((o.keyUp.isDown() ? 1 : 0) - (o.keyDown.isDown() ? 1 : 0)) : (a.inF = 0.0f);
        float f2 = ok ? (float)((o.keyLeft.isDown() ? 1 : 0) - (o.keyRight.isDown() ? 1 : 0)) : (a.inS = 0.0f);
        a.inU = ok ? (float)((o.keyJump.isDown() ? 1 : 0) - (o.keySprint.isDown() ? 1 : 0)) : 0.0f;
        a.inBoost = ok && o.keyJump.isDown();
        a.inBrake = ok && o.keySprint.isDown();
        a.inFire = ok && o.keyAttack.isDown();
        a.aimYaw = mc.player.getYRot();
        a.aimPitch = mc.player.getXRot();
        Net.CH.sendToServer((Object)new Net.InputMsg(a.thrust, a.inBoost, a.inFire, a.inU > 0.0f, a.aimYaw, a.aimPitch, a.pendingImpact));
        a.pendingImpact = 0.0f;
    }

    public static Aircraft mine() {
        Aircraft a;
        Entity entity;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && (entity = mc.player.getVehicle()) instanceof Aircraft && (a = (Aircraft)entity).getControllingPassenger() == mc.player) {
            return a;
        }
        return null;
    }
}

