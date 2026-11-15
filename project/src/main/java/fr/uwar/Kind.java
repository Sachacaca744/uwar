/*
 * Decompiled with CFR 0.152.
 */
package fr.uwar;

import fr.uwar.W;

public enum Kind {
    HELI("attack_heli", "Helicoptere d'attaque AH-64", 2.4f, 2.6f, 2, 180.0f, 1400.0f, 0.9f, 3, W.ROCKET.bit() | W.MISSILE.bit(), new int[]{600, 38, 8, 0, 4, 4, 0, 0, 4, 0}),
    JET("fighter_jet", "Avion de chasse F-16", 2.6f, 1.6f, 1, 120.0f, 1600.0f, 1.0f, 4, W.ROCKET.bit() | W.MISSILE.bit() | W.BOMB.bit(), new int[]{900, 14, 6, 4, 6, 6, 2, 2, 4, 12});

    public final String id;
    public final String name;
    public final float w;
    public final float h;
    public final float maxHp;
    public final float fuelCap;
    public final float fuelRate;
    public final int seats;
    public final int slots;
    public final int defaultLoadout;
    public final int[] cap;

    private Kind(String id, String name, float w, float h, int seats, float hp, float fuel, float rate, int slots, int def, int[] cap) {
        this.id = id;
        this.name = name;
        this.w = w;
        this.h = h;
        this.seats = seats;
        this.maxHp = hp;
        this.fuelCap = fuel;
        this.fuelRate = rate;
        this.slots = slots;
        this.defaultLoadout = def | W.CANNON.bit();
        this.cap = cap;
    }

    public boolean heli() {
        return this == HELI;
    }

    public boolean canCarry(W w) {
        return this.cap[w.ordinal()] > 0;
    }
}

