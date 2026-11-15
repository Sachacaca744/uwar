/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.Input
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers
 *  net.minecraftforge.client.event.InputEvent$InteractionKeyMappingTriggered
 *  net.minecraftforge.client.event.MovementInputUpdateEvent
 *  net.minecraftforge.client.event.RegisterGuiOverlaysEvent
 *  net.minecraftforge.client.event.RegisterKeyMappingsEvent
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Pre
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.client.event.ViewportEvent$ComputeCameraAngles
 *  net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.entity.EntityJoinLevelEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package fr.uwar.client;

import fr.uwar.Aircraft;
import fr.uwar.Kind;
import fr.uwar.Net;
import fr.uwar.Reg;
import fr.uwar.client.AircraftRenderer;
import fr.uwar.client.AircraftSound;
import fr.uwar.client.ClientHooks;
import fr.uwar.client.Hud;
import fr.uwar.client.LoadoutScreen;
import fr.uwar.client.ShellRenderer;
import java.lang.reflect.Method;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    public static final String CAT = "key.categories.uwar";
    public static final KeyMapping WEAPON = new KeyMapping("key.uwar.weapon", 71, "key.categories.uwar");
    public static final KeyMapping LOADOUT = new KeyMapping("key.uwar.loadout", 53, "key.categories.uwar");
    public static final KeyMapping RELOAD = new KeyMapping("key.uwar.reload", 82, "key.categories.uwar");

    @Mod.EventBusSubscriber(modid="uwar", value={Dist.CLIENT})
    public static class ForgeBus {
        static int lastSlot = -1;
        private static Method MOVE;
        private static boolean moveTried;

        @SubscribeEvent
        public static void tick(TickEvent.ClientTickEvent e) {
            Entity entity;
            if (e.phase != TickEvent.Phase.END) {
                return;
            }
            Minecraft mcc = Minecraft.getInstance();
            if (mcc.player != null && (entity = mcc.player.getVehicle()) instanceof Aircraft) {
                Aircraft ac = (Aircraft)entity;
                if (LOADOUT.consumeClick() && mcc.screen == null) {
                    if (lastSlot >= 0) {
                        mcc.player.getInventory().selected = lastSlot;
                    }
                    mcc.setScreen((Screen)new LoadoutScreen(ac));
                }
                lastSlot = mcc.player.getInventory().selected;
            } else {
                while (LOADOUT.consumeClick()) {
                }
                lastSlot = -1;
            }
            if (ClientHooks.mine() == null) {
                while (WEAPON.consumeClick() || RELOAD.consumeClick()) {
                }
                return;
            }
            while (WEAPON.consumeClick()) {
                Net.CH.sendToServer((Object)new Net.CmdMsg(1));
            }
            while (RELOAD.consumeClick()) {
                Net.CH.sendToServer((Object)new Net.CmdMsg(2));
            }
        }

        @SubscribeEvent
        public static void join(EntityJoinLevelEvent e) {
            Entity entity;
            if (!e.getLevel().isClientSide() || !((entity = e.getEntity()) instanceof Aircraft)) {
                return;
            }
            Aircraft a = (Aircraft)entity;
            Minecraft.getInstance().getSoundManager().play((SoundInstance)new AircraftSound(a, a.kind.heli() ? (SoundEvent)Reg.HELI_LOOP.get() : (SoundEvent)Reg.JET_LOOP.get()));
        }

        @SubscribeEvent
        public static void move(MovementInputUpdateEvent e) {
            if (ClientHooks.mine() == null) {
                return;
            }
            Input in = e.getInput();
            in.forwardImpulse = 0.0f;
            in.leftImpulse = 0.0f;
            in.right = false;
            in.left = false;
            in.down = false;
            in.up = false;
            in.jumping = false;
        }

        @SubscribeEvent
        public static void interact(InputEvent.InteractionKeyMappingTriggered e) {
            if (ClientHooks.mine() != null && (e.isAttack() || e.isUseItem())) {
                e.setCanceled(true);
                e.setSwingHand(false);
            }
        }

        @SubscribeEvent
        public static void camDist(ViewportEvent.ComputeCameraAngles e) {
            Entity vehicle;
            Camera cam = e.getCamera();
            if (!cam.isDetached() || cam.getEntity() == null || !((vehicle = cam.getEntity().getVehicle()) instanceof Aircraft)) {
                return;
            }
            Aircraft a = (Aircraft)vehicle;
            if (!moveTried) {
                moveTried = true;
                for (String n : new String[]{"move", "move"}) {
                    try {
                        MOVE = Camera.class.getDeclaredMethod(n, Double.TYPE, Double.TYPE, Double.TYPE);
                        MOVE.setAccessible(true);
                        break;
                    }
                    catch (Throwable t) {
                        MOVE = null;
                    }
                }
                System.out.println("UWAR: reglage camera 3e personne " + (MOVE != null ? "ok" : "indisponible"));
            }
            if (MOVE == null) {
                return;
            }
            try {
                MOVE.invoke(cam, a.kind.heli() ? -9.0 : -11.0, a.kind.heli() ? 1.5 : 1.8, 0.0);
            }
            catch (Throwable t) {
                MOVE = null;
            }
        }

        @SubscribeEvent
        public static void hand(RenderHandEvent e) {
            if (ClientHooks.mine() != null) {
                e.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void overlay(RenderGuiOverlayEvent.Pre e) {
            if (ClientHooks.mine() == null) {
                return;
            }
            ResourceLocation id = e.getOverlay().id();
            if (id.equals((Object)VanillaGuiOverlay.CROSSHAIR.id()) || id.equals((Object)VanillaGuiOverlay.MOUNT_HEALTH.id()) || id.equals((Object)VanillaGuiOverlay.JUMP_BAR.id())) {
                e.setCanceled(true);
            }
        }
    }

    @Mod.EventBusSubscriber(modid="uwar", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void renderers(EntityRenderersEvent.RegisterRenderers e) {
            for (Kind k : Kind.values()) {
                e.registerEntityRenderer((EntityType)Reg.AIRCRAFTS.get((Object)k).get(), AircraftRenderer::new);
            }
            e.registerEntityRenderer((EntityType)Reg.SHELL.get(), ShellRenderer::new);
        }

        @SubscribeEvent
        public static void keys(RegisterKeyMappingsEvent e) {
            e.register(WEAPON);
            e.register(RELOAD);
            e.register(LOADOUT);
        }

        @SubscribeEvent
        public static void overlays(RegisterGuiOverlaysEvent e) {
            e.registerAboveAll("cockpit", (gui, g, pt, w, h) -> {
                if (!Minecraft.getInstance().options.hideGui) {
                    Hud.render(g, w, h, pt);
                }
            });
        }
    }
}

