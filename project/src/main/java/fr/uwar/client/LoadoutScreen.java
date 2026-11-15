/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Tooltip
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 */
package fr.uwar.client;

import fr.uwar.Aircraft;
import fr.uwar.Net;
import fr.uwar.W;
import fr.uwar.client.ClientEvents;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class LoadoutScreen
extends Screen {
    private final Aircraft a;
    private final List<W> list = new ArrayList<W>();
    private final List<Button> buttons = new ArrayList<Button>();
    private int x0;
    private int y0;
    private static final int ROW = 22;
    private static final int WIDTH = 400;
    private static final int COLW = 195;

    public LoadoutScreen(Aircraft a) {
        super((Component)Component.literal((String)"Armement"));
        this.a = a;
        for (W w : W.values()) {
            if (w == W.CANNON || !a.kind.canCarry(w)) continue;
            this.list.add(w);
        }
    }

    protected void init() {
        this.buttons.clear();
        this.x0 = (this.width - 400) / 2;
        this.y0 = 36;
        for (int i = 0; i < this.list.size(); ++i) {
            W w = this.list.get(i);
            Button b = Button.builder((Component)Component.empty(), btn -> this.toggle(w)).bounds(this.x0 + i % 2 * 205, this.y0 + i / 2 * 22, 195, 20).tooltip(Tooltip.create((Component)Component.literal((String)w.desc))).build();
            this.buttons.add(this.addRenderableWidget(b));
        }
        int by = this.y0 + (this.list.size() + 1) / 2 * 22 + 30;
        this.addRenderableWidget(Button.builder((Component)Component.literal((String)"Recharger depuis l'inventaire"), btn -> Net.CH.sendToServer((Object)new Net.CmdMsg(2))).bounds(this.x0, by, 195, 20).build());
        this.addRenderableWidget(Button.builder((Component)Component.literal((String)"Fermer"), btn -> this.onClose()).bounds(this.x0 + 195 + 10, by, 195, 20).build());
        this.refresh();
    }

    private void toggle(W w) {
        Net.CH.sendToServer((Object)new Net.LoadoutMsg(this.a.getId(), this.a.loadout() ^ w.bit()));
    }

    private void refresh() {
        for (int i = 0; i < this.list.size(); ++i) {
            W w;
            boolean on;
            this.buttons.get(i).setMessage((Component)Component.literal((String)(((on = this.a.has(w = this.list.get(i))) ? "[X] " : "[  ] ") + w.shortName + (String)(on ? "  " + this.a.ammo(w) + "/" + this.a.cap(w) : ""))));
        }
    }

    public void tick() {
        if (this.a.isRemoved() || this.a.getControllingPassenger() == null && !this.a.hasPassenger((Entity)this.minecraft.player) && this.a.distanceTo((Entity)this.minecraft.player) > 12.0f) {
            this.onClose();
        }
        this.refresh();
    }

    public boolean keyPressed(int key, int scan, int mod) {
        if (ClientEvents.LOADOUT.matches(key, scan)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(key, scan, mod);
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void render(GuiGraphics g, int mx, int my, float pt) {
        this.renderBackground(g);
        g.drawCenteredString(this.font, this.a.kind.name + " - ARMEMENT", this.width / 2, 8, -10944646);
        String sl = "Emplacements : " + this.a.slotsUsed() + " / " + this.a.kind.slots + "   (le canon 20 mm est toujours monte)";
        g.drawCenteredString(this.font, sl, this.width / 2, 21, this.a.slotsUsed() >= this.a.kind.slots ? -16320 : -3355444);
        super.render(g, mx, my, pt);
        int ty = this.y0 + (this.list.size() + 1) / 2 * 22 + 4;
        boolean ground = this.a.grounded() || this.minecraft.player.getAbilities().instabuild;
        g.drawCenteredString(this.font, ground ? "Clic : installer / retirer (munitions rendues). Survole = description." : "Pose l'appareil au sol pour modifier l'armement.", this.width / 2, ty, ground ? -5592406 : -40864);
        g.drawCenteredString(this.font, "Munitions : clic droit avec l'objet, bouton ci-dessous ou touche R.", this.width / 2, ty + 12, -7829368);
    }
}

