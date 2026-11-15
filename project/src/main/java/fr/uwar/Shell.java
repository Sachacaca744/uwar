/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.network.NetworkHooks
 */
package fr.uwar;

import fr.uwar.Aircraft;
import fr.uwar.Blast;
import fr.uwar.W;
import java.util.UUID;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class Shell
extends Projectile {
    static final EntityDataAccessor<Integer> KIND = SynchedEntityData.defineId(Shell.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private int life;
    private UUID target;
    private UUID srcAircraft;
    private Vec3 point;
    private Vec3 launchPos;
    private double travelled;
    private double cruiseY;
    private boolean diving;
    public static final double BALLISTIC_G = 0.04;

    public Shell(EntityType<? extends Shell> t, Level l) {
        super(t, l);
        this.noCulling = true;
    }

    public W weapon() {
        return W.values()[Math.max(0, Math.min(W.values().length - 1, (Integer)this.entityData.get(KIND)))];
    }

    public void setup(W w, Entity owner, Aircraft from, Vec3 vel, UUID target) {
        this.entityData.set(KIND, (Object)w.ordinal());
        this.setOwner(owner);
        this.setDeltaMovement(vel);
        this.target = target;
        this.srcAircraft = from == null ? null : from.getUUID();
    }

    public void setPoint(Vec3 p) {
        this.point = p;
        this.cruiseY = Math.max(this.getY(), p.y) + 28.0;
    }

    protected void defineSynchedData() {
        this.entityData.define(KIND, (Object)W.ROCKET.ordinal());
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket((Entity)this);
    }

    protected boolean canHitEntity(Entity e) {
        if (this.srcAircraft != null && e.getUUID().equals(this.srcAircraft) && this.life < 60) {
            return false;
        }
        if (e == this.getOwner() || this.getOwner() != null && e == this.getOwner().getVehicle()) {
            return false;
        }
        if (e instanceof Aircraft) {
            Aircraft a = (Aircraft)e;
            if (this.getOwner() != null && a.hasPassenger(this.getOwner())) {
                return false;
            }
        }
        return super.canHitEntity(e);
    }

    public void tick() {
        super.tick();
        ++this.life;
        W w = this.weapon();
        Vec3 v = this.getDeltaMovement();
        if (!this.level().isClientSide) {
            if (this.launchPos == null) {
                this.launchPos = this.position();
            }
            switch (w) {
                case MISSILE: 
                case FLAK: 
                case BUSTER: {
                    Entity t2;
                    Level level;
                    double gain;
                    Vec3 dir;
                    double sp = Math.min((double)w.speed, v.length() + 0.18);
                    Vec3 vec3 = dir = v.lengthSqr() > 1.0E-4 ? v.normalize() : Vec3.ZERO;
                    double d = w == W.FLAK ? 0.26 : (gain = w == W.BUSTER ? 0.12 : 0.2);
                    if (this.target != null && (level = this.level()) instanceof ServerLevel) {
                        ServerLevel sl = (ServerLevel)level;
                        v2 = sl.getEntity(this.target);
                    } else {
                        v2 = t2 = null;
                    }
                    if (t2 != null && t2.isAlive() && this.life > 4) {
                        Vec3 want = t2.getBoundingBox().getCenter().subtract(this.position()).normalize();
                        dir = dir.scale(1.0 - gain).add(want.scale(gain)).normalize();
                        if (w == W.FLAK && t2.getBoundingBox().getCenter().distanceTo(this.position()) < 7.5) {
                            this.explode();
                            return;
                        }
                    }
                    v = dir.scale(sp);
                    break;
                }
                case IR: {
                    Entity t;
                    Level t2;
                    Vec3 dir;
                    double sp = Math.min((double)w.speed, v.length() + 0.2);
                    Vec3 vec3 = dir = v.lengthSqr() > 1.0E-4 ? v.normalize() : Vec3.ZERO;
                    if (this.target != null && (t2 = this.level()) instanceof ServerLevel) {
                        ServerLevel sl = (ServerLevel)t2;
                        v4 = sl.getEntity(this.target);
                    } else {
                        v4 = t = null;
                    }
                    if (!(t != null && t.isAlive() || this.life <= 6 || this.life % 3 != 0)) {
                        Entity best = null;
                        double bs = 0.0;
                        for (Entity e : this.level().getEntities((Entity)this, this.getBoundingBox().inflate(70.0), x -> !(!x.isAlive() || x.isSpectator() || !(x instanceof LivingEntity) && !(x instanceof Aircraft) || x == this.getOwner() || this.getOwner() != null && x == this.getOwner().getVehicle()))) {
                            Vec3 to = e.getBoundingBox().getCenter().subtract(this.position());
                            double cos = dir.dot(to.normalize());
                            if (cos < 0.5) continue;
                            double sc = cos + Aircraft.heat(e) * 2.0 - to.length() / 200.0;
                            if (best != null && !(sc > bs)) continue;
                            best = e;
                            bs = sc;
                        }
                        if (best != null) {
                            this.target = best.getUUID();
                        }
                        t = best;
                    }
                    if (t != null && t.isAlive() && this.life > 4) {
                        Vec3 want = t.getBoundingBox().getCenter().subtract(this.position()).normalize();
                        dir = dir.scale(0.72).add(want.scale(0.28)).normalize();
                    }
                    v = dir.scale(sp);
                    break;
                }
                case CRUISE: {
                    Vec3 want;
                    double hz;
                    double hx;
                    double hd;
                    ServerLevel sl;
                    Entity t;
                    Level dir;
                    Vec3 tp = this.point;
                    if (this.target != null && (dir = this.level()) instanceof ServerLevel && (t = (sl = (ServerLevel)dir).getEntity(this.target)) != null && t.isAlive()) {
                        tp = t.getBoundingBox().getCenter();
                    }
                    if ((hd = Math.hypot(hx = tp.x - this.getX(), hz = tp.z - this.getZ())) < 38.0) {
                        this.diving = true;
                    }
                    if (this.diving) {
                        want = tp.subtract(this.position()).normalize().scale(1.9);
                    } else {
                        double vy = Mth.clamp((double)((this.cruiseY - this.getY()) * 0.15), (double)-0.5, (double)0.7);
                        Vec3 hv = new Vec3(hx / Math.max(1.0E-4, hd), 0.0, hz / Math.max(1.0E-4, hd)).scale((double)w.speed);
                        want = new Vec3(hv.x, vy, hv.z);
                    }
                    v = v.scale(0.8).add(want.scale(0.2));
                    break;
                }
                case BALLISTIC: {
                    v = v.add(0.0, -0.04, 0.0);
                    break;
                }
                case ROCKET: {
                    v = v.add(0.0, -0.012, 0.0);
                    break;
                }
                default: {
                    v = v.add(0.0, -0.05, 0.0).scale(0.995);
                }
            }
            this.setDeltaMovement(v);
            HitResult hit = ProjectileUtil.getHitResultOnMoveVector((Entity)this, this::canHitEntity);
            if (hit.getType() != HitResult.Type.MISS) {
                Vec3 p = hit.getLocation();
                this.setPos(p.x, p.y, p.z);
                this.explode();
                return;
            }
            this.setPos(this.getX() + v.x, this.getY() + v.y, this.getZ() + v.z);
            this.travelled += v.length();
            if (w == W.FLAK && this.travelled >= 50.0) {
                this.explode();
                return;
            }
            if (this.life > 600 || this.getY() < (double)(this.level().getMinBuildHeight() - 10)) {
                this.explode();
            }
        } else {
            if (w == W.ROCKET) {
                v = v.add(0.0, -0.012, 0.0);
            } else if (w == W.BALLISTIC) {
                v = v.add(0.0, -0.04, 0.0);
            } else if (w.bombLike()) {
                v = v.add(0.0, -0.05, 0.0).scale(0.995);
            }
            this.setDeltaMovement(v);
            this.setPos(this.getX() + v.x, this.getY() + v.y, this.getZ() + v.z);
            if (w.bombLike()) {
                if (this.life % 3 == 0) {
                    this.level().addParticle((ParticleOptions)ParticleTypes.CLOUD, this.getX(), this.getY() + 0.2, this.getZ(), 0.0, 0.0, 0.0);
                }
            } else if (w != W.CANNON) {
                this.level().addParticle((ParticleOptions)ParticleTypes.FLAME, this.getX() - v.x * 0.6, this.getY() - v.y * 0.6, this.getZ() - v.z * 0.6, 0.0, 0.0, 0.0);
                this.level().addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, this.getX() - v.x, this.getY() - v.y, this.getZ() - v.z, 0.0, 0.01, 0.0);
                this.level().addParticle((ParticleOptions)ParticleTypes.SMOKE, this.getX() - v.x * 1.5, this.getY() - v.y * 1.5, this.getZ() - v.z * 1.5, 0.0, 0.01, 0.0);
            }
        }
    }

    private void explode() {
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel)level;
            W w = this.weapon();
            Blast.boom(sl, this.position(), w.radius, w.dmg, (Entity)this, this.getOwner(), w.heavy);
        }
        this.discard();
    }

    protected void onHitEntity(EntityHitResult r) {
    }

    protected void onHitBlock(BlockHitResult r) {
    }

    protected void readAdditionalSaveData(CompoundTag t) {
        this.entityData.set(KIND, (Object)t.getInt("K"));
    }

    protected void addAdditionalSaveData(CompoundTag t) {
        t.putInt("K", ((Integer)this.entityData.get(KIND)).intValue());
    }
}

