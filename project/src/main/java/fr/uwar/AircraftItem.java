/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package fr.uwar;

import fr.uwar.Aircraft;
import fr.uwar.Kind;
import fr.uwar.Reg;
import fr.uwar.W;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AircraftItem
extends Item {
    private final Kind kind;

    public AircraftItem(Kind k, Item.Properties p) {
        super(p);
        this.kind = k;
    }

    public InteractionResult useOn(UseOnContext c) {
        Level lv = c.getLevel();
        if (lv.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockPos pos = c.getClickedPos().relative(c.getClickedFace());
        if (c.getClickedFace() != Direction.UP && c.getClickedFace() != Direction.DOWN) {
            pos = c.getClickedPos().relative(c.getClickedFace());
        }
        Aircraft a = new Aircraft((EntityType)Reg.AIRCRAFTS.get((Object)this.kind).get(), lv, this.kind);
        double y = c.getClickedFace() == Direction.UP ? (double)c.getClickedPos().getY() + 1.0 : (double)pos.getY();
        a.moveTo((double)pos.getX() + 0.5, y, (double)pos.getZ() + 0.5, c.getRotation() + 180.0f, 0.0f);
        a.applyStack(c.getItemInHand());
        if (!lv.noCollision((Entity)a, a.getBoundingBox())) {
            a.moveTo(a.getX(), a.getY() + 1.0, a.getZ());
            if (!lv.noCollision((Entity)a, a.getBoundingBox())) {
                return InteractionResult.FAIL;
            }
        }
        lv.addFreshEntity((Entity)a);
        if (c.getPlayer() == null || !c.getPlayer().getAbilities().instabuild) {
            c.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    public void appendHoverText(ItemStack s, @Nullable Level l, List<Component> tip, TooltipFlag f) {
        tip.add((Component)Component.literal((String)(this.kind.heli() ? "Clic droit sur le sol pour le poser, puis clic droit dessus pour monter" : "Clic droit sur une piste/le sol pour le poser, puis clic droit dessus pour monter")));
        CompoundTag t = s.getTagElement("ac");
        if (t != null) {
            tip.add((Component)Component.literal((String)("Coque " + Math.round(t.getFloat("Hp") * 100.0f / this.kind.maxHp) + " %  |  Carburant " + Math.round(t.getFloat("Fuel") * 100.0f / this.kind.fuelCap) + " %")));
            int[] a = t.getIntArray("Am");
            StringBuilder sb = new StringBuilder();
            for (W w : W.values()) {
                if (a.length <= w.ordinal() || a[w.ordinal()] <= 0) continue;
                sb.append(w.shortName).append(' ').append(a[w.ordinal()]).append("  ");
            }
            tip.add((Component)Component.literal((String)sb.toString()));
        } else {
            tip.add((Component)Component.literal((String)"Livre avec 40 % de carburant et 150 obus de canon"));
        }
    }
}

