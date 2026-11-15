/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.network.NetworkEvent$Context
 *  net.minecraftforge.network.NetworkRegistry
 *  net.minecraftforge.network.simple.SimpleChannel
 */
package fr.uwar;

import fr.uwar.Aircraft;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Net {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CH = NetworkRegistry.newSimpleChannel((ResourceLocation)new ResourceLocation("uwar", "main"), () -> "1", "1"::equals, "1"::equals);
    public static final int C_WEAPON = 1;
    public static final int C_RELOAD = 2;
    public static final int C_WEAPON_PREV = 3;

    public static void register() {
        CH.registerMessage(0, InputMsg.class, InputMsg::encode, InputMsg::decode, InputMsg::handle);
        CH.registerMessage(1, CmdMsg.class, CmdMsg::encode, CmdMsg::decode, CmdMsg::handle);
        CH.registerMessage(2, LoadoutMsg.class, LoadoutMsg::encode, LoadoutMsg::decode, LoadoutMsg::handle);
    }

    static Aircraft piloted(ServerPlayer sp) {
        Aircraft a;
        Entity entity;
        if (sp != null && (entity = sp.getVehicle()) instanceof Aircraft && (a = (Aircraft)entity).getControllingPassenger() == sp) {
            return a;
        }
        return null;
    }

    public record InputMsg(float thr, boolean boost, boolean fire, boolean up, float yaw, float pitch, float impact) {
        static void encode(InputMsg m, FriendlyByteBuf b) {
            b.writeFloat(m.thr);
            b.writeBoolean(m.boost);
            b.writeBoolean(m.fire);
            b.writeBoolean(m.up);
            b.writeFloat(m.yaw);
            b.writeFloat(m.pitch);
            b.writeFloat(m.impact);
        }

        static InputMsg decode(FriendlyByteBuf b) {
            return new InputMsg(b.readFloat(), b.readBoolean(), b.readBoolean(), b.readBoolean(), b.readFloat(), b.readFloat(), b.readFloat());
        }

        static void handle(InputMsg m, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer sp = ctx.get().getSender();
            ctx.get().enqueueWork(() -> {
                Aircraft a = Net.piloted(sp);
                if (a != null) {
                    a.setInput(m.thr, m.boost, m.fire, m.up, m.yaw, m.pitch, m.impact);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public record CmdMsg(int cmd) {
        static void encode(CmdMsg m, FriendlyByteBuf b) {
            b.writeByte(m.cmd);
        }

        static CmdMsg decode(FriendlyByteBuf b) {
            return new CmdMsg(b.readByte());
        }

        static void handle(CmdMsg m, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer sp = ctx.get().getSender();
            ctx.get().enqueueWork(() -> {
                Aircraft a = Net.piloted(sp);
                if (a == null) {
                    return;
                }
                switch (m.cmd) {
                    case 1: {
                        a.nextWeapon(1);
                        break;
                    }
                    case 3: {
                        a.nextWeapon(-1);
                        break;
                    }
                    case 2: {
                        a.reloadFrom((Player)sp);
                        break;
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public record LoadoutMsg(int id, int mask) {
        static void encode(LoadoutMsg m, FriendlyByteBuf b) {
            b.writeVarInt(m.id);
            b.writeVarInt(m.mask);
        }

        static LoadoutMsg decode(FriendlyByteBuf b) {
            return new LoadoutMsg(b.readVarInt(), b.readVarInt());
        }

        static void handle(LoadoutMsg m, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer sp = ctx.get().getSender();
            ctx.get().enqueueWork(() -> {
                if (sp == null) {
                    return;
                }
                Entity patt3727$temp = sp.level().getEntity(m.id);
                if (patt3727$temp instanceof Aircraft) {
                    String err;
                    Aircraft a = (Aircraft)patt3727$temp;
                    if ((sp.getVehicle() == a || a.distanceTo((Entity)sp) < 12.0f) && (err = a.setLoadout((Player)sp, m.mask)) != null) {
                        sp.displayClientMessage((Component)Component.literal((String)err), true);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}

