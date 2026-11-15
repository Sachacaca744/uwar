/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.Nullable
 */
package fr.uwar;

import fr.uwar.Aircraft;
import fr.uwar.W;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class SupplyItem
extends Item {
    public static final float FUEL_PER_CAN = 300.0f;
    public static final float REPAIR_HP = 45.0f;
    public final Type type;
    public final W weapon;

    public SupplyItem(Type t, W w, Item.Properties p) {
        super(p);
        this.type = t;
        this.weapon = w;
    }

    public void appendHoverText(ItemStack s, @Nullable Level l, List<Component> tip, TooltipFlag f) {
        switch (this.type) {
            case AMMO: {
                tip.add((Component)Component.literal((String)("+" + this.weapon.perItem + " " + this.weapon.label + " (clic droit sur l'appareil, ou touche R en vol)")));
                break;
            }
            case FUEL: {
                tip.add((Component)Component.literal((String)"+300 carburant (clic droit sur l'appareil)"));
                break;
            }
            case REPAIR: {
                tip.add((Component)Component.literal((String)"+45 points de coque (clic droit sur l'appareil)"));
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level lv, Player p, InteractionHand h) {
        ItemStack st = p.getItemInHand(h);
        Entity entity = p.getVehicle();
        if (entity instanceof Aircraft) {
            Aircraft a = (Aircraft)entity;
            if (!lv.isClientSide) {
                a.supply(p, st);
            }
            return InteractionResultHolder.sidedSuccess(st, (boolean)lv.isClientSide);
        }
        Aircraft best = null;
        double bd = 36.0;
        for (Aircraft a : lv.getEntitiesOfClass(Aircraft.class, new AABB(p.position(), p.position()).inflate(6.0))) {
            double d = a.distanceToSqr((Entity)p);
            if (!(d < bd)) continue;
            bd = d;
            best = a;
        }
        if (best != null) {
            if (!lv.isClientSide) {
                best.supply(p, st);
            }
            return InteractionResultHolder.sidedSuccess(st, (boolean)lv.isClientSide);
        }
        return InteractionResultHolder.pass(st);
    }

    public static enum Type {
        AMMO,
        FUEL,
        REPAIR;

    }
}

