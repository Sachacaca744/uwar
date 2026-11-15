/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 */
package fr.uwar.client;

import fr.uwar.Aircraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class AircraftSound
extends AbstractTickableSoundInstance {
    private final Aircraft a;

    public AircraftSound(Aircraft a, SoundEvent ev) {
        super(ev, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.a = a;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0E-4f;
        this.x = a.getX();
        this.y = a.getY();
        this.z = a.getZ();
    }

    public void tick() {
        if (this.a.isRemoved()) {
            this.stop();
            return;
        }
        this.x = this.a.getX();
        this.y = this.a.getY();
        this.z = this.a.getZ();
        if (!this.a.engineOn()) {
            this.volume = 1.0E-4f;
            return;
        }
        float t = this.a.thr();
        if (this.a.kind.heli()) {
            this.volume = 3.0f;
            this.pitch = 0.85f + t * 0.3f;
        } else {
            this.volume = 3.2f * (0.4f + 0.6f * Math.min(1.0f, t + (this.a.boosting() ? 0.4f : 0.0f)));
            this.pitch = 0.7f + t * 0.45f + (this.a.boosting() ? 0.15f : 0.0f);
        }
    }
}

