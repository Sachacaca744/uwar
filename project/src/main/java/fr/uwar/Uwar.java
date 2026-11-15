/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.ModLoadingContext
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.config.IConfigSpec
 *  net.minecraftforge.fml.config.ModConfig$Type
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 */
package fr.uwar;

import fr.uwar.Cfg;
import fr.uwar.Net;
import fr.uwar.Reg;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value="uwar")
public class Uwar {
    public static final String MODID = "uwar";

    public Uwar() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Reg.ITEMS.register(bus);
        Reg.ENTITIES.register(bus);
        Reg.SOUNDS.register(bus);
        Net.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, (IConfigSpec)Cfg.SPEC);
        if (System.getProperty("uwar.selftest") != null) {
            try {
                Class.forName("fr.uwar.SelfTest").getMethod("init", new Class[0]).invoke(null, new Object[0]);
            }
            catch (Throwable t) {
                System.out.println("SELFTEST: init impossible " + String.valueOf(t));
            }
        }
    }
}

