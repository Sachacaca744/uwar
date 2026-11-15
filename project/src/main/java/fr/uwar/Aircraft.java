/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.DustParticleOptions
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.decoration.ArmorStand
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Blaze
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.MagmaCube
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.network.NetworkHooks
 *  org.joml.Vector3f
 */
package fr.uwar;

import fr.uwar.Blast;
import fr.uwar.Cfg;
import fr.uwar.Kind;
import fr.uwar.Reg;
import fr.uwar.Shell;
import fr.uwar.SupplyItem;
import fr.uwar.W;
import fr.uwar.client.ClientHooks;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Vector3f;

public class Aircraft
extends Entity {
    static final EntityDataAccessor<Float> HP = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    static final EntityDataAccessor<Float> FUEL = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    static final EntityDataAccessor<Float> THR = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    static final EntityDataAccessor<Integer> SEL = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.INT);
    static final EntityDataAccessor<Integer> LOCK = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.INT);
    static final EntityDataAccessor<Boolean> ENGINE = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    static final EntityDataAccessor<Integer> LOADOUT = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.INT);
    static final EntityDataAccessor<Integer>[] AM = new EntityDataAccessor[W.values().length];
    static final EntityDataAccessor<Boolean> BOOST;
    public final Kind kind;
    public float inF;
    public float inS;
    public float inU;
    public float aimYaw;
    public float aimPitch;
    public boolean inBoost;
    public boolean inFire;
    public boolean inBrake;
    private int lastInputTick;
    public float speedF;
    public float thrust;
    public float roll;
    public float rollO;
    public float rotor;
    public float rotorO;
    public float flapO;
    public float pendingImpact;
    private int cooldown;
    private int crashCd;
    private int lockTick;
    private int clickCd;
    private boolean alt;
    private Vec3 lastPos;
    public Vec3 vel = Vec3.ZERO;
    public int shots;
    private int lerpSteps;
    private double lx;
    private double ly;
    private double lz;
    private float lyr;
    private float lxr;
    public Player testPilot;
    private boolean dying;

    public Aircraft(EntityType<?> t, Level l, Kind k) {
        super(t, l);
        this.kind = k;
        this.noCulling = true;
        this.blocksBuilding = true;
    }

    protected void defineSynchedData() {
        this.entityData.define(HP, Float.valueOf(100.0f));
        this.entityData.define(FUEL, Float.valueOf(0.0f));
        this.entityData.define(THR, Float.valueOf(0.0f));
        this.entityData.define(SEL, 0);
        this.entityData.define(LOCK, -1);
        this.entityData.define(ENGINE, false);
        this.entityData.define(LOADOUT, W.CANNON.bit());
        for (EntityDataAccessor<Integer> a : AM) {
            this.entityData.define(a, 0);
        }
        this.entityData.define(BOOST, false);
    }

    public float hp() {
        return ((Float)this.entityData.get(HP)).floatValue();
    }

    public float fuel() {
        return ((Float)this.entityData.get(FUEL)).floatValue();
    }

    public float thr() {
        return ((Float)this.entityData.get(THR)).floatValue();
    }

    public boolean engineOn() {
        return (Boolean)this.entityData.get(ENGINE);
    }

    public boolean boosting() {
        return (Boolean)this.entityData.get(BOOST);
    }

    public int lockId() {
        return (Integer)this.entityData.get(LOCK);
    }

    public int loadout() {
        return (Integer)this.entityData.get(LOADOUT);
    }

    public boolean has(W w) {
        return (this.loadout() & w.bit()) != 0;
    }

    public int slotsUsed() {
        return Integer.bitCount(this.loadout() & ~W.CANNON.bit());
    }

    public W selected() {
        W w = W.values()[Mth.clamp((int)((Integer)this.entityData.get(SEL)), (int)0, (int)(W.values().length - 1))];
        return this.has(w) ? w : W.CANNON;
    }

    public List<W> loaded() {
        ArrayList<W> l = new ArrayList<W>();
        for (W w : W.values()) {
            if (!this.has(w)) continue;
            l.add(w);
        }
        return l;
    }

    public int ammo(W w) {
        return (Integer)this.entityData.get(AM[w.ordinal()]);
    }

    public int cap(W w) {
        return this.has(w) ? this.kind.cap[w.ordinal()] : 0;
    }

    public void setAmmo(W w, int n) {
        this.entityData.set(AM[w.ordinal()], Mth.clamp((int)n, (int)0, (int)this.kind.cap[w.ordinal()]));
    }

    public void setFuel(float f) {
        this.entityData.set(FUEL, Float.valueOf(Mth.clamp((float)f, (float)0.0f, (float)this.kind.fuelCap)));
    }

    public void setHp(float h) {
        this.entityData.set(HP, Float.valueOf(Mth.clamp((float)h, (float)0.0f, (float)this.kind.maxHp)));
    }

    public void nextWeapon(int d) {
        int n = W.values().length;
        int idx = this.selected().ordinal();
        for (int k = 0; k < n; ++k) {
            idx = ((idx + d) % n + n) % n;
            if (this.has(W.values()[idx])) break;
        }
        this.entityData.set(SEL, idx);
        this.entityData.set(LOCK, -1);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)Reg.BEEP.get(), SoundSource.NEUTRAL, 0.5f, 1.6f);
    }

    public String setLoadout(Player p, int mask) {
        mask |= W.CANNON.bit();
        for (W w : W.values()) {
            if ((mask & w.bit()) == 0 || this.kind.canCarry(w)) continue;
            return w.label + " : impossible sur cet appareil";
        }
        if (Integer.bitCount(mask & ~W.CANNON.bit()) > this.kind.slots) {
            return "Trop d'armes : " + this.kind.slots + " emplacements maximum";
        }
        if (!this.grounded() && !p.getAbilities().instabuild) {
            return "Pose l'appareil au sol pour changer l'armement";
        }
        int old = this.loadout();
        if (mask == old) {
            return null;
        }
        for (W w : W.values()) {
            if ((old & w.bit()) == 0 || (mask & w.bit()) != 0) continue;
            int items = this.ammo(w) / w.perItem;
            this.setAmmo(w, 0);
            if (items <= 0 || w == W.CANNON) continue;
            ItemStack st = new ItemStack((ItemLike)Reg.AMMO.get((Object)w).get(), items);
            if (p.getInventory().add(st)) continue;
            p.drop(st, false);
        }
        this.entityData.set(LOADOUT, mask);
        this.entityData.set(LOCK, -1);
        if (!this.has(W.values()[(Integer)this.entityData.get(SEL)])) {
            this.entityData.set(SEL, W.CANNON.ordinal());
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_USE, SoundSource.NEUTRAL, 0.5f, 1.2f);
        return null;
    }

    public void setInput(float thr, boolean boost, boolean fire, boolean up, float yaw, float pitch, float impact) {
        this.lastInputTick = this.tickCount;
        if (impact > this.pendingImpact) {
            this.pendingImpact = Math.min(impact, 4.0f);
        }
        this.inFire = fire;
        this.inBoost = boost;
        this.aimYaw = yaw;
        this.aimPitch = pitch;
        if (!this.level().isClientSide) {
            this.entityData.set(THR, Float.valueOf(Mth.clamp((float)thr, (float)0.0f, (float)1.0f)));
            this.entityData.set(BOOST, boost && this.kind == Kind.JET && this.fuel() > 20.0f);
        }
    }

    public static ItemStack makeStack(Kind k, boolean full) {
        ItemStack s = new ItemStack((ItemLike)Reg.AIRCRAFT_ITEMS.get((Object)k).get());
        if (full) {
            CompoundTag t = s.getOrCreateTagElement("ac");
            t.putFloat("Hp", k.maxHp);
            t.putFloat("Fuel", k.fuelCap);
            t.putInt("Lo", k.defaultLoadout);
            int[] a = new int[W.values().length];
            for (W w : W.values()) {
                if ((k.defaultLoadout & w.bit()) == 0) continue;
                a[w.ordinal()] = k.cap[w.ordinal()];
            }
            t.putIntArray("Am", a);
            s.setHoverName((Component)Component.literal((String)(k.name + " (equipe)")).withStyle(st -> st.withItalic(Boolean.valueOf(false))));
        }
        return s;
    }

    public void applyStack(ItemStack s) {
        CompoundTag t = s.getTagElement("ac");
        if (t == null) {
            this.entityData.set(LOADOUT, this.kind.defaultLoadout);
            this.setHp(this.kind.maxHp);
            this.setFuel(this.kind.fuelCap * 0.4f);
            this.setAmmo(W.CANNON, 150);
            return;
        }
        this.entityData.set(LOADOUT, (t.contains("Lo") ? t.getInt("Lo") | W.CANNON.bit() : this.kind.defaultLoadout));
        this.setHp(t.getFloat("Hp"));
        this.setFuel(t.getFloat("Fuel"));
        int[] a = t.getIntArray("Am");
        for (W w : W.values()) {
            if (a.length <= w.ordinal()) continue;
            this.setAmmo(w, a[w.ordinal()]);
        }
    }

    public ItemStack toStack() {
        ItemStack s = new ItemStack((ItemLike)Reg.AIRCRAFT_ITEMS.get((Object)this.kind).get());
        CompoundTag t = s.getOrCreateTagElement("ac");
        t.putFloat("Hp", this.hp());
        t.putFloat("Fuel", this.fuel());
        t.putInt("Lo", this.loadout());
        int[] a = new int[W.values().length];
        for (W w : W.values()) {
            a[w.ordinal()] = this.ammo(w);
        }
        t.putIntArray("Am", a);
        return s;
    }

    public boolean supply(Player p, ItemStack st) {
        Item item = st.getItem();
        if (item instanceof SupplyItem) {
            SupplyItem si = (SupplyItem)item;
            Object msg = null;
            boolean used = false;
            switch (si.type) {
                case AMMO: {
                    W w = si.weapon;
                    if (this.cap(w) <= 0) {
                        msg = w.label + " : non installe (touche 5 pour configurer l'armement)";
                        break;
                    }
                    if (this.ammo(w) >= this.cap(w)) {
                        msg = w.label + " : deja plein (" + this.ammo(w) + ")";
                        break;
                    }
                    this.setAmmo(w, this.ammo(w) + w.perItem);
                    used = true;
                    msg = w.label + " : " + this.ammo(w) + " / " + this.cap(w);
                    break;
                }
                case FUEL: {
                    if (this.fuel() >= this.kind.fuelCap - 1.0f) {
                        msg = "Reservoir deja plein";
                        break;
                    }
                    this.setFuel(this.fuel() + 300.0f);
                    used = true;
                    msg = "Carburant : " + Math.round(this.fuel() * 100.0f / this.kind.fuelCap) + " %";
                    break;
                }
                case REPAIR: {
                    if (this.hp() >= this.kind.maxHp - 0.5f) {
                        msg = "Appareil deja en parfait etat";
                        break;
                    }
                    this.setHp(this.hp() + 45.0f);
                    used = true;
                    msg = "Etat : " + Math.round(this.hp() * 100.0f / this.kind.maxHp) + " %";
                }
            }
            if (!p.level().isClientSide) {
                if (used && !p.getAbilities().instabuild) {
                    st.shrink(1);
                }
                if (used) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_USE, SoundSource.NEUTRAL, 0.4f, 1.5f);
                }
                if (msg != null) {
                    p.displayClientMessage((Component)Component.literal((String)msg), true);
                }
            }
            return true;
        }
        return false;
    }

    public void reloadFrom(Player p) {
        int used = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.getInventory().getContainerSize(); ++i) {
            SupplyItem si;
            Item item;
            ItemStack st = p.getInventory().getItem(i);
            while (!st.isEmpty() && (item = st.getItem()) instanceof SupplyItem && this.canTake(si = (SupplyItem)item) && this.supply(p, st)) {
                ++used;
                if (!p.getAbilities().instabuild) continue;
            }
        }
        for (W w : this.loaded()) {
            sb.append(w.shortName).append(' ').append(this.ammo(w)).append('/').append(this.cap(w)).append("  ");
        }
        p.displayClientMessage((Component)Component.literal((String)(used == 0 ? "Rien a charger dans l'inventaire | " + String.valueOf(sb) : "Rechargement termine | " + String.valueOf(sb) + "Carburant " + Math.round(this.fuel() * 100.0f / this.kind.fuelCap) + " %")), true);
    }

    private boolean canTake(SupplyItem si) {
        return switch (si.type) {
            case AMMO -> {
                if (this.cap(si.weapon) > 0 && this.ammo(si.weapon) < this.cap(si.weapon)) {
                    yield true;
                }
                yield false;
            }
            case FUEL -> {
                if (this.fuel() < this.kind.fuelCap - 1.0f) {
                    yield true;
                }
                yield false;
            }
            case REPAIR -> this.hp() < this.kind.maxHp - 0.5f;
        };
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public boolean isPushable() {
        return false;
    }

    public boolean fireImmune() {
        return true;
    }

    public ItemStack getPickResult() {
        return this.toStack();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket((Entity)this);
    }

    public InteractionResult interact(Player p, InteractionHand hand) {
        ItemStack st = p.getItemInHand(hand);
        if (st.getItem() instanceof SupplyItem) {
            this.supply(p, st);
            return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
        }
        if (p.isSecondaryUseActive()) {
            if (!this.getPassengers().isEmpty()) {
                return InteractionResult.PASS;
            }
            if (!this.level().isClientSide) {
                ItemEntity ie = new ItemEntity(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), this.toStack());
                this.level().addFreshEntity((Entity)ie);
                this.discard();
            }
            return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
        }
        if (!this.level().isClientSide) {
            if (this.hp() <= 0.0f) {
                return InteractionResult.PASS;
            }
            return p.startRiding((Entity)this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    protected boolean canAddPassenger(Entity e) {
        return this.getPassengers().size() < this.kind.seats;
    }

    public LivingEntity getControllingPassenger() {
        LivingEntity le;
        Entity entity = this.getFirstPassenger();
        return entity instanceof LivingEntity ? (le = (LivingEntity)entity) : null;
    }

    private double[] seat(int i) {
        if (this.kind == Kind.HELI) {
            double[] dArray;
            if (i == 0) {
                double[] dArray2 = new double[3];
                dArray2[0] = 0.0;
                dArray2[1] = 0.45;
                dArray = dArray2;
                dArray2[2] = 1.25;
            } else {
                double[] dArray3 = new double[3];
                dArray3[0] = 0.0;
                dArray3[1] = 0.3;
                dArray = dArray3;
                dArray3[2] = 2.55;
            }
            return dArray;
        }
        return new double[]{0.0, 0.42, 1.7};
    }

    protected void positionRider(Entity p, Entity.MoveFunction cb) {
        if (!this.hasPassenger(p)) {
            return;
        }
        double[] s = this.seat(Math.max(0, this.getPassengers().indexOf(p)));
        double a = this.getYRot() * ((float)Math.PI / 180);
        double b = this.getXRot() * ((float)Math.PI / 180);
        double fx = -Math.sin(a);
        double fz = Math.cos(a);
        cb.accept(p, this.getX() + fx * s[2] * Math.cos(b), this.getY() + s[1] - Math.sin(b) * s[2], this.getZ() + fz * s[2] * Math.cos(b));
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity p) {
        double[][] c;
        double a = this.getYRot() * ((float)Math.PI / 180);
        double lxv = Math.cos(a);
        double lzv = Math.sin(a);
        double d = (double)this.kind.w * 0.5 + 1.0;
        for (double[] o : c = new double[][]{{lxv * d, lzv * d}, {-lxv * d, -lzv * d}, {-Math.sin(a) * -3.0, Math.cos(a) * -3.0}}) {
            Vec3 pos = new Vec3(this.getX() + o[0], this.getY(), this.getZ() + o[1]);
            AABB bb = p.getBoundingBox().move(pos.subtract(p.position()));
            if (!this.level().noCollision((Entity)p, bb)) continue;
            return pos;
        }
        return new Vec3(this.getX(), this.getBoundingBox().maxY + 0.2, this.getZ());
    }

    public boolean hurt(DamageSource src, float amount) {
        if (this.level().isClientSide || this.isRemoved()) {
            return false;
        }
        if (src.getEntity() != null && this.hasPassenger(src.getEntity())) {
            return false;
        }
        if (src.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        Entity entity = src.getEntity();
        if (entity instanceof Player) {
            Player p = (Player)entity;
            if (p.getAbilities().instabuild && p.isShiftKeyDown()) {
                this.ejectPassengers();
                this.discard();
                return true;
            }
        }
        if (!src.is(DamageTypeTags.IS_EXPLOSION) && !src.is(DamageTypeTags.IS_PROJECTILE)) {
            amount = Math.max(0.0f, amount - 1.0f);
        }
        float h = this.hp() - amount;
        this.entityData.set(HP, Float.valueOf(Math.max(0.0f, h)));
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 0.25f, 1.7f);
        if (h <= 0.0f) {
            this.destroy(src.getEntity());
        }
        return true;
    }

    private void destroy(Entity killer) {
        if (this.dying) {
            return;
        }
        this.dying = true;
        List<Entity> pass = List.copyOf(this.getPassengers());
        this.ejectPassengers();
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel)level;
            Blast.boom(sl, this.position().add(0.0, (double)this.kind.h * 0.5, 0.0), 4.5f, 18.0f, this, killer);
            for (Entity e : pass) {
                e.hurt(sl.damageSources().explosion((Entity)this, killer), 14.0f);
            }
        }
        this.discard();
    }

    public boolean isControlledByLocalInstance() {
        LivingEntity c = this.getControllingPassenger();
        if (c instanceof Player) {
            Player p = (Player)c;
            return p.isLocalPlayer() || this.testPilot != null && p == this.testPilot;
        }
        return !this.level().isClientSide;
    }

    public void tick() {
        super.tick();
        this.tickLerp();
        if (!this.level().isClientSide) {
            this.serverTick();
        }
        LivingEntity c = this.getControllingPassenger();
        if (this.isControlledByLocalInstance()) {
            boolean piloted = c instanceof Player;
            if (this.level().isClientSide) {
                this.inU = 0.0f;
                this.inS = 0.0f;
                this.inF = 0.0f;
                this.inBrake = false;
                if (piloted) {
                    DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> ClientHooks.poll(this));
                } else {
                    this.inBoost = false;
                    this.inFire = false;
                }
            } else if (this.testPilot == null) {
                this.inU = 0.0f;
                this.inS = 0.0f;
                this.inF = 0.0f;
            }
            this.fly(piloted);
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }
        if (this.level().isClientSide) {
            this.animate();
        }
    }

    private void animate() {
        this.rotorO = this.rotor;
        this.rollO += 0.0f;
        float t = this.thr();
        this.rotor = this.engineOn() ? (this.rotor += this.kind.heli() ? 58.0f : 30.0f + t * 60.0f) : (this.rotor += this.kind.heli() ? Math.max(0.0f, 18.0f - (float)(this.tickCount % 1000) * 0.1f) * 0.0f : 0.0f);
        if (this.rotor > 3600.0f) {
            this.rotor -= 3600.0f;
            this.rotorO -= 3600.0f;
        }
        if (this.engineOn() && this.kind == Kind.JET && this.boosting() && this.tickCount % 1 == 0) {
            double a = this.getYRot() * ((float)Math.PI / 180);
            this.level().addParticle((ParticleOptions)ParticleTypes.FLAME, this.getX() + Math.sin(a) * 6.0, this.getY() + 1.0, this.getZ() - Math.cos(a) * 6.0, 0.0, 0.0, 0.0);
        }
        if (this.engineOn() && this.kind.heli() && this.grounded()) {
            double ang = this.random.nextDouble() * Math.PI * 2.0;
            this.level().addParticle((ParticleOptions)ParticleTypes.CLOUD, this.getX() + Math.cos(ang) * 4.5, this.getY() + 0.2, this.getZ() + Math.sin(ang) * 4.5, Math.cos(ang) * 0.12, 0.01, Math.sin(ang) * 0.12);
        }
    }

    public boolean grounded() {
        return this.onGround() || !this.level().noCollision((Entity)this, this.getBoundingBox().move(0.0, -0.2, 0.0));
    }

    private void fly(boolean piloted) {
        boolean on;
        boolean bl = on = piloted && this.engineOn() && this.fuel() > 0.0f;
        if (!piloted) {
            this.inU = 0.0f;
            this.inS = 0.0f;
            this.inF = 0.0f;
            this.inBoost = false;
        }
        this.rollO = this.roll;
        if (this.kind.heli()) {
            this.flyHeli(on);
        } else {
            this.flyJet(on);
        }
    }

    private void flyHeli(boolean on) {
        double vy;
        double vz;
        double vx;
        float dyaw = Mth.wrapDegrees((float)(this.aimYaw - this.getYRot()));
        if (on) {
            this.setYRot(this.getYRot() + Mth.clamp((float)dyaw, (float)-4.5f, (float)4.5f));
        }
        double a = this.getYRot() * ((float)Math.PI / 180);
        double fx = -Math.sin(a);
        double fz = Math.cos(a);
        double lxv = Math.cos(a);
        double lzv = Math.sin(a);
        Vec3 v = this.getDeltaMovement();
        if (on) {
            double tx = fx * (double)this.inF * 0.62 + lxv * (double)this.inS * 0.36;
            double tz = fz * (double)this.inF * 0.62 + lzv * (double)this.inS * 0.36;
            double ty = (double)this.inU * 0.34;
            vx = v.x + (tx - v.x) * 0.045;
            vz = v.z + (tz - v.z) * 0.045;
            vy = v.y + (ty - v.y) * 0.12;
        } else {
            vx = v.x * 0.96;
            vz = v.z * 0.96;
            vy = Math.max(v.y - 0.035, -0.32);
        }
        this.setDeltaMovement(vx, vy, vz);
        double hb = Math.hypot(vx, vz);
        this.move(MoverType.SELF, this.getDeltaMovement());
        double hbAfter = Math.hypot(this.getDeltaMovement().x, this.getDeltaMovement().z);
        float imp = 0.0f;
        if (this.horizontalCollision) {
            imp = (float)(hb - hbAfter);
        }
        if (this.verticalCollision && vy < -0.45) {
            imp = Math.max(imp, (float)(-vy));
        }
        if (imp > 0.4f) {
            this.pendingImpact = Math.max(this.pendingImpact, imp);
        }
        if (this.grounded() && this.getDeltaMovement().y < 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 0.0, 0.8));
        }
        this.setXRot(Mth.lerp((float)0.08f, (float)this.getXRot(), (float)(on ? this.inF * 13.0f : 0.0f)));
        float rt = on ? -this.inS * 10.0f + Mth.clamp((float)dyaw, (float)-20.0f, (float)20.0f) * 0.4f : 0.0f;
        this.roll = Mth.lerp((float)0.1f, (float)this.roll, (float)rt);
        this.speedF = (float)this.getDeltaMovement().length();
        this.thrust = on ? Math.max(Math.abs(this.inF), Math.max(Math.abs(this.inS), Math.abs(this.inU))) : 0.0f;
    }

    private void flyJet(boolean on) {
        double tgt;
        boolean boost;
        boolean ground = this.grounded();
        if (on) {
            this.thrust += this.inF * 0.02f;
            if (this.inBrake) {
                this.thrust -= 0.05f;
            }
            this.thrust = Mth.clamp((float)this.thrust, (float)0.0f, (float)1.0f);
        } else {
            this.thrust = Math.max(0.0f, this.thrust - 0.01f);
        }
        boolean bl = boost = on && this.inBoost && this.fuel() > 20.0f;
        tgt = on ? (0.04 + (double)this.thrust * 1.5) * (boost ? 1.5 : 1.0) : 0.0;
        this.speedF = tgt > (double)this.speedF ? (this.speedF += (float)((tgt - (double)this.speedF) * 0.03 + 0.002)) : (this.speedF += (float)((tgt - (double)this.speedF) * 0.015));
        if (ground) {
            this.speedF *= this.inBrake || this.thrust < 0.02f ? 0.96f : 0.997f;
        } else if (!on) {
            this.speedF -= 0.004f;
        }
        this.speedF = Math.max(0.0f, this.speedF);
        float dyaw = Mth.wrapDegrees((float)(this.aimYaw - this.getYRot()));
        if (on || this.speedF > 0.3f) {
            float yr = ground ? (this.speedF > 0.03f ? 2.0f : 0.0f) : 3.2f;
            this.setYRot(this.getYRot() + Mth.clamp((float)dyaw, (float)(-yr), (float)yr));
            float pt = Mth.clamp((float)this.aimPitch, (float)-70.0f, (float)70.0f);
            if (ground && this.speedF < 0.6f) {
                pt = 0.0f;
            }
            this.setXRot(this.getXRot() + Mth.clamp((float)(pt - this.getXRot()), (float)-2.6f, (float)2.6f));
        }
        float lift = Mth.clamp((float)((this.speedF - 0.25f) / 0.55f), (float)0.0f, (float)1.0f);
        if (!ground) {
            this.setXRot(Math.min(85.0f, this.getXRot() + (1.0f - lift) * 1.6f));
        }
        double yr = this.getYRot() * ((float)Math.PI / 180);
        double xr = this.getXRot() * ((float)Math.PI / 180);
        double dx = -Math.sin(yr) * Math.cos(xr);
        double dy = -Math.sin(xr);
        double dz = Math.cos(yr) * Math.cos(xr);
        double vy = dy * (double)this.speedF - (double)(1.0f - lift) * 0.12;
        this.setDeltaMovement(dx * (double)this.speedF, vy, dz * (double)this.speedF);
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.horizontalCollision && this.speedF > 0.3f) {
            this.pendingImpact = Math.max(this.pendingImpact, this.speedF * 0.8f);
            this.speedF *= 0.3f;
        } else if (this.verticalCollision && dy * (double)this.speedF < -0.35) {
            this.pendingImpact = Math.max(this.pendingImpact, (float)(-dy * (double)this.speedF));
            this.speedF *= 0.3f;
        }
        float rt = on ? Mth.clamp((float)dyaw, (float)-30.0f, (float)30.0f) * 1.6f : 0.0f;
        this.roll = Mth.lerp((float)0.12f, (float)this.roll, (float)rt);
    }

    private void serverTick() {
        Level level;
        boolean on;
        LivingEntity c = this.getControllingPassenger();
        boolean bl = on = c != null && this.fuel() > 0.0f && this.hp() > 0.0f;
        if (on != this.engineOn()) {
            this.entityData.set(ENGINE, on);
        }
        if (this.testPilot == null && c != null && this.tickCount - this.lastInputTick > 6) {
            this.inFire = false;
            this.inBoost = false;
        }
        if (c == null) {
            if (this.thr() != 0.0f) {
                this.entityData.set(THR, Float.valueOf(0.0f));
            }
            if (this.boosting()) {
                this.entityData.set(BOOST, false);
            }
            this.inFire = false;
        }
        if (this.isInLava()) {
            this.hurt(this.level().damageSources().inFire(), 1.5f);
        }
        if (this.getY() < (double)(this.level().getMinBuildHeight() - 20)) {
            this.discard();
        }
        Vec3 p = this.position();
        this.vel = this.lastPos == null ? Vec3.ZERO : p.subtract(this.lastPos);
        this.lastPos = p;
        if (this.crashCd > 0) {
            --this.crashCd;
        }
        float imp = this.pendingImpact;
        this.pendingImpact = 0.0f;
        if (imp > 0.35f && this.crashCd == 0 && !this.isRemoved() && c != null) {
            this.crashCd = 10;
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.8f, 1.4f);
            level = this.level();
            if (level instanceof ServerLevel) {
                ServerLevel sl = (ServerLevel)level;
                sl.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.5, this.getZ(), 2, 1.0, 0.5, 1.0, 0.0);
            }
            if ((double)imp > 0.8) {
                for (Entity e : this.getPassengers()) {
                    e.hurt(this.level().damageSources().flyIntoWall(), (imp - 0.7f) * 30.0f);
                }
            }
            this.hurt(this.level().damageSources().generic(), imp * 80.0f);
            if (this.isRemoved()) {
                return;
            }
        }
        if (on) {
            float use = this.kind.fuelRate * (0.15f + 0.45f * this.thr() + (this.boosting() ? 1.1f : 0.0f)) * ((Double)Cfg.FUEL_USE.get()).floatValue();
            this.setFuel(this.fuel() - use);
        }
        if (this.hp() < this.kind.maxHp * 0.35f && (level = this.level()) instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel)level;
            if (this.tickCount % 3 == 0) {
                sl.sendParticles((ParticleOptions)ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1.2, this.getZ(), 2, 0.3, 0.2, 0.3, 0.02);
                if (this.hp() < this.kind.maxHp * 0.15f) {
                    sl.sendParticles((ParticleOptions)ParticleTypes.FLAME, this.getX(), this.getY() + 1.2, this.getZ(), 2, 0.3, 0.2, 0.3, 0.02);
                }
            }
        }
        if (this.cooldown > 0) {
            --this.cooldown;
        }
        if (this.clickCd > 0) {
            --this.clickCd;
        }
        if (on) {
            if (Aircraft.lockWeapon(this.selected()) && this.tickCount % 4 == 0) {
                this.updateLock(this.selected());
            }
            if (this.inFire && this.cooldown <= 0) {
                this.fire(c);
            }
        } else if (this.lockId() != -1) {
            this.entityData.set(LOCK, -1);
        }
    }

    public Vec3 aimDir() {
        float x;
        float y = this.kind.heli() ? this.aimYaw : this.getYRot();
        float f = x = this.kind.heli() ? this.aimPitch : this.getXRot();
        if (this.kind.heli()) {
            x = Mth.clamp((float)x, (float)-25.0f, (float)70.0f);
        }
        double yr = y * ((float)Math.PI / 180);
        double xr = x * ((float)Math.PI / 180);
        return new Vec3(-Math.sin(yr) * Math.cos(xr), -Math.sin(xr), Math.cos(yr) * Math.cos(xr));
    }

    private Vec3 leftVec() {
        double a = this.getYRot() * ((float)Math.PI / 180);
        return new Vec3(Math.cos(a), 0.0, Math.sin(a));
    }

    public static boolean lockWeapon(W w) {
        return w == W.MISSILE || w == W.IR || w == W.FLAK || w == W.BUSTER || w == W.CRUISE;
    }

    private void updateLock(W w) {
        int id;
        double minCos = switch (w) {
            case IR -> 0.78;
            case FLAK -> 0.9;
            case BUSTER -> 0.965;
            case CRUISE -> 0.96;
            default -> 0.94;
        };
        double range = switch (w) {
            case IR -> 180.0;
            case CRUISE -> 170.0;
            case FLAK -> 150.0;
            case BUSTER -> 100.0;
            default -> 135.0;
        };
        Vec3 o = this.position().add(0.0, (double)this.kind.h * 0.5, 0.0);
        Vec3 d = this.aimDir();
        Entity best = null;
        double bs = -1.0;
        AABB box = new AABB(o, o).inflate(range);
        for (Entity e : this.level().getEntities((Entity)this, box, this::lockable)) {
            BlockHitResult br;
            double cos;
            Vec3 to = e.getBoundingBox().getCenter().subtract(o);
            double dist = to.length();
            if (dist < 8.0 || dist > range || (cos = to.normalize().dot(d)) < minCos || (br = this.level().clip(new ClipContext(o, e.getBoundingBox().getCenter(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this))).getType() != HitResult.Type.MISS) continue;
            double score = cos + (e instanceof Enemy ? 0.02 : 0.0);
            if (e instanceof Aircraft) {
                Aircraft ac = (Aircraft)e;
                score += w == W.FLAK ? 0.3 : 0.01;
            }
            if (w == W.IR) {
                score += Aircraft.heat(e);
            }
            if (!(score > bs)) continue;
            bs = score;
            best = e;
        }
        int n = id = best == null ? -1 : best.getId();
        if (id != this.lockId()) {
            this.entityData.set(LOCK, id);
            if (id != -1) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)Reg.BEEP.get(), SoundSource.NEUTRAL, 0.6f, 1.2f);
            }
        }
    }

    public static double heat(Entity e) {
        Aircraft ac;
        double h = 0.0;
        if (e instanceof Aircraft && (ac = (Aircraft)e).engineOn()) {
            h += 0.5 + (ac.boosting() ? 0.3 : 0.0) + (double)ac.thr() * 0.2;
        }
        if (e.isOnFire()) {
            h += 0.25;
        }
        if (e instanceof Blaze || e instanceof MagmaCube) {
            h += 0.3;
        }
        return h;
    }

    private boolean lockable(Entity e) {
        if (!e.isAlive() || e.isSpectator() || this.hasPassenger(e) || e.getVehicle() == this) {
            return false;
        }
        if (e instanceof Aircraft) {
            Aircraft a = (Aircraft)e;
            return a != this;
        }
        if (e instanceof Player && !((Boolean)Cfg.LOCK_PLAYERS.get()).booleanValue()) {
            return false;
        }
        if (e instanceof ArmorStand) {
            return false;
        }
        return e instanceof LivingEntity;
    }

    private UUID lockedUuid(ServerLevel sl) {
        if (this.lockId() == -1) {
            return null;
        }
        Entity t = sl.getEntity(this.lockId());
        return t == null ? null : t.getUUID();
    }

    private Shell launch(ServerLevel sl, W w, LivingEntity pilot, Vec3 o, Vec3 v, UUID target, Vec3 point) {
        Shell s = new Shell((EntityType<? extends Shell>)((EntityType)Reg.SHELL.get()), (Level)sl);
        s.setPos(o.x, o.y, o.z);
        s.setup(w, (Entity)pilot, this, v, target);
        if (point != null) {
            s.setPoint(point);
        }
        sl.addFreshEntity((Entity)s);
        return s;
    }

    public Vec3 aimPoint(Level lv, Vec3 from, Vec3 dir) {
        double range = 170.0;
        Vec3 end = from.add(dir.scale(range));
        BlockHitResult br = lv.clip(new ClipContext(from, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
        if (br.getType() != HitResult.Type.MISS) {
            return br.getLocation();
        }
        if (!lv.isClientSide) {
            BlockPos bp = lv.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing((Position)end));
            return new Vec3(end.x, (double)bp.getY(), end.z);
        }
        return end;
    }

    private void fire(LivingEntity pilot) {
        W w = this.selected();
        Level level = this.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel sl = (ServerLevel)level;
        boolean unl = (Boolean)Cfg.UNLIMITED_AMMO.get();
        if (!unl && this.ammo(w) <= 0) {
            this.cooldown = 12;
            if (this.clickCd == 0) {
                this.clickCd = 20;
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)Reg.BEEP.get(), SoundSource.NEUTRAL, 0.6f, 0.6f);
                if (pilot instanceof Player) {
                    Player p = (Player)pilot;
                    p.displayClientMessage((Component)Component.literal((String)("Plus de munitions : " + w.label + " (R pour recharger)")), true);
                }
            }
            return;
        }
        Vec3 dir = this.aimDir();
        Vec3 left = this.leftVec();
        this.alt = !this.alt;
        double side = this.alt ? 1.0 : -1.0;
        Vec3 base = this.position().add(0.0, (double)this.kind.h * 0.55, 0.0);
        switch (w) {
            case CANNON: {
                this.fireCannon(sl, pilot, base.add(dir.scale(2.6)).add(0.0, this.kind.heli() ? -0.7 : -0.1, 0.0), dir);
                break;
            }
            case ROCKET: {
                Vec3 o = base.add(left.scale(side * (this.kind.heli() ? 1.7 : 3.6))).add(dir.scale(2.2)).add(0.0, -0.4, 0.0);
                this.launch(sl, w, pilot, o, dir.scale((double)w.speed).add(this.vel.scale(0.4)), null, null);
                sl.playSound(null, o.x, o.y, o.z, (SoundEvent)Reg.LAUNCH.get(), SoundSource.NEUTRAL, 1.6f, 1.2f);
                break;
            }
            case IR: 
            case FLAK: 
            case BUSTER: 
            case MISSILE: {
                Vec3 o = base.add(left.scale(side * (this.kind.heli() ? 2.5 : 5.0))).add(dir.scale(1.5)).add(0.0, -0.6, 0.0);
                this.launch(sl, w, pilot, o, dir.scale(0.9).add(this.vel.scale(0.5)), this.lockedUuid(sl), null);
                sl.playSound(null, o.x, o.y, o.z, (SoundEvent)Reg.LAUNCH.get(), SoundSource.NEUTRAL, w == W.BUSTER ? 2.6f : 2.0f, w == W.BUSTER ? 0.6f : 0.9f);
                break;
            }
            case CRUISE: {
                Vec3 o = base.add(0.0, 0.2, 0.0).add(dir.scale(2.0));
                Vec3 pt = this.aimPoint((Level)sl, base, dir);
                Vec3 h = new Vec3(dir.x, 0.0, dir.z).normalize();
                this.launch(sl, w, pilot, o, h.scale(0.5).add(0.0, 0.9, 0.0), this.lockedUuid(sl), pt);
                sl.playSound(null, o.x, o.y, o.z, (SoundEvent)Reg.LAUNCH.get(), SoundSource.NEUTRAL, 3.0f, 0.5f);
                break;
            }
            case BALLISTIC: {
                Vec3 o = base.add(0.0, 1.2, 0.0).add(dir.scale(1.0));
                Vec3 pt = this.aimPoint((Level)sl, base, dir);
                double dx = pt.x - o.x;
                double dz = pt.z - o.z;
                double d = Math.max(25.0, Math.hypot(dx, dz));
                double th = Math.toRadians(60.0);
                double g = 0.04;
                double den = Math.max(2.0, d * Math.tan(th) - (pt.y - o.y));
                double v = Math.sqrt(g * d * d / (2.0 * Math.cos(th) * Math.cos(th) * den));
                double hl = Math.max(1.0E-4, Math.hypot(dx, dz));
                Vec3 vv = new Vec3(dx / hl * v * Math.cos(th), v * Math.sin(th), dz / hl * v * Math.cos(th));
                this.launch(sl, w, pilot, o, vv, null, pt);
                sl.playSound(null, o.x, o.y, o.z, (SoundEvent)Reg.LAUNCH.get(), SoundSource.NEUTRAL, 3.2f, 0.45f);
                break;
            }
            case BOMB: 
            case BAY: {
                Vec3 o = base.add(left.scale(side * (w == W.BAY ? 0.5 : 0.8))).add(0.0, -1.1, 0.0);
                this.launch(sl, w, pilot, o, this.vel, null, null);
                sl.playSound(null, o.x, o.y, o.z, SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 1.0f, w == W.BAY ? 0.6f : 0.8f);
            }
        }
        ++this.shots;
        if (!unl) {
            this.setAmmo(w, this.ammo(w) - 1);
        }
        this.cooldown = w.cooldown;
    }

    private void fireCannon(ServerLevel sl, LivingEntity pilot, Vec3 from, Vec3 dir) {
        EntityHitResult er;
        Vec3 end = from.add(dir.scale(110.0));
        BlockHitResult br = sl.clip(new ClipContext(from, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
        if (br.getType() != HitResult.Type.MISS) {
            end = br.getLocation();
        }
        if ((er = ProjectileUtil.getEntityHitResult((Level)sl, (Entity)this, (Vec3)from, (Vec3)end, (AABB)new AABB(from, end).inflate(1.0), e -> e.isAlive() && !e.isSpectator() && e != pilot && !this.hasPassenger((Entity)e) && e != this && (e instanceof LivingEntity || e instanceof Aircraft) && !(e instanceof ArmorStand))) != null) {
            DamageSource damageSource;
            end = er.getLocation();
            Entity t = er.getEntity();
            t.invulnerableTime = 0;
            if (pilot instanceof Player) {
                Player pp = (Player)pilot;
                damageSource = sl.damageSources().playerAttack(pp);
            } else {
                damageSource = sl.damageSources().mobAttack(pilot);
            }
            DamageSource ds = damageSource;
            float dmg = (float)((double)W.CANNON.dmg * (Double)Cfg.DAMAGE_MULT.get());
            if (t instanceof LivingEntity) {
                t.hurt(sl.damageSources().thrown((Entity)this, (Entity)pilot), dmg);
            } else {
                t.hurt(ds, dmg);
            }
            sl.sendParticles((ParticleOptions)ParticleTypes.CRIT, end.x, end.y, end.z, 4, 0.2, 0.2, 0.2, 0.2);
        } else if (br.getType() != HitResult.Type.MISS) {
            sl.sendParticles((ParticleOptions)ParticleTypes.SMOKE, end.x, end.y, end.z, 3, 0.1, 0.1, 0.1, 0.02);
            sl.sendParticles((ParticleOptions)ParticleTypes.CRIT, end.x, end.y, end.z, 3, 0.1, 0.1, 0.1, 0.15);
        }
        double len = end.subtract(from).length();
        DustParticleOptions dust = new DustParticleOptions(new Vector3f(1.0f, 0.82f, 0.25f), 1.1f);
        for (double d = 2.0; d < len; d += 3.5) {
            Vec3 q = from.add(dir.scale(d));
            sl.sendParticles((ParticleOptions)dust, q.x, q.y, q.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        sl.playSound(null, from.x, from.y, from.z, (SoundEvent)Reg.GUN.get(), SoundSource.NEUTRAL, 1.4f, 0.9f + this.random.nextFloat() * 0.2f);
    }

    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean teleport) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = yr;
        this.lxr = xr;
        this.lerpSteps = 4;
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lx - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.ly - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lz - this.getZ()) / (double)this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot() + Mth.wrapDegrees((float)(this.lyr - this.getYRot())) / (float)this.lerpSteps, this.getXRot() + (this.lxr - this.getXRot()) / (float)this.lerpSteps);
            --this.lerpSteps;
        }
    }

    protected void readAdditionalSaveData(CompoundTag t) {
        this.setHp(t.getFloat("Hp"));
        this.setFuel(t.getFloat("Fuel"));
        this.entityData.set(LOADOUT, (t.contains("Lo") ? t.getInt("Lo") | W.CANNON.bit() : this.kind.defaultLoadout));
        for (W w : W.values()) {
            this.setAmmo(w, t.getInt("A" + w.ordinal()));
        }
        this.entityData.set(SEL, t.getInt("Sel"));
    }

    protected void addAdditionalSaveData(CompoundTag t) {
        t.putFloat("Hp", this.hp());
        t.putFloat("Fuel", this.fuel());
        for (W w : W.values()) {
            t.putInt("A" + w.ordinal(), this.ammo(w));
        }
        t.putInt("Sel", ((Integer)this.entityData.get(SEL)).intValue());
        t.putInt("Lo", this.loadout());
    }

    public void remove(Entity.RemovalReason r) {
        super.remove(r);
    }

    public UUID uuidForTest() {
        return this.getUUID();
    }

    static {
        for (int i = 0; i < AM.length; ++i) {
            Aircraft.AM[i] = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.INT);
        }
        BOOST = SynchedEntityData.defineId(Aircraft.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    }
}

