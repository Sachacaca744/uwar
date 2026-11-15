/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraftforge.event.BuildCreativeModeTabContentsEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package fr.uwar;

import fr.uwar.Aircraft;
import fr.uwar.Kind;
import fr.uwar.Reg;
import fr.uwar.W;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="uwar", bus=Mod.EventBusSubscriber.Bus.MOD)
public class TabHook {
    public static final ResourceKey<CreativeModeTab> TAB = ResourceKey.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (ResourceLocation)new ResourceLocation("utab", "main"));

    @SubscribeEvent
    public static void onTab(BuildCreativeModeTabContentsEvent e) {
        if (!e.getTabKey().equals(TAB)) {
            return;
        }
        for (Kind kind : Kind.values()) {
            e.accept(Aircraft.makeStack(kind, false));
            e.accept(Aircraft.makeStack(kind, true));
        }
        for (Enum enum_ : W.values()) {
            e.accept(new ItemStack((ItemLike)Reg.AMMO.get(enum_).get()));
        }
        e.accept(new ItemStack((ItemLike)Reg.FUEL_CAN.get()));
        e.accept(new ItemStack((ItemLike)Reg.REPAIR_KIT.get()));
    }
}

