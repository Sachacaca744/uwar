/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.MobCategory
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.RegistryObject
 */
package fr.uwar;

import fr.uwar.Aircraft;
import fr.uwar.AircraftItem;
import fr.uwar.Kind;
import fr.uwar.Shell;
import fr.uwar.SupplyItem;
import fr.uwar.W;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class Reg {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create((IForgeRegistry)ForgeRegistries.ITEMS, (String)"uwar");
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create((IForgeRegistry)ForgeRegistries.ENTITY_TYPES, (String)"uwar");
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create((ResourceKey)Registries.SOUND_EVENT, (String)"uwar");
    public static final RegistryObject<SoundEvent> GUN = Reg.snd("gun");
    public static final RegistryObject<SoundEvent> BOOM = Reg.snd("boom");
    public static final RegistryObject<SoundEvent> LAUNCH = Reg.snd("launch");
    public static final RegistryObject<SoundEvent> BEEP = Reg.snd("beep");
    public static final RegistryObject<SoundEvent> WARN = Reg.snd("warn");
    public static final RegistryObject<SoundEvent> HELI_LOOP = Reg.snd("heli_loop");
    public static final RegistryObject<SoundEvent> JET_LOOP = Reg.snd("jet_loop");
    public static final Map<Kind, RegistryObject<EntityType<Aircraft>>> AIRCRAFTS = new EnumMap<Kind, RegistryObject<EntityType<Aircraft>>>(Kind.class);
    public static final Map<Kind, RegistryObject<Item>> AIRCRAFT_ITEMS = new EnumMap<Kind, RegistryObject<Item>>(Kind.class);
    public static final Map<W, RegistryObject<Item>> AMMO = new EnumMap<W, RegistryObject<Item>>(W.class);
    public static final RegistryObject<Item> FUEL_CAN;
    public static final RegistryObject<Item> REPAIR_KIT;
    public static final RegistryObject<EntityType<Shell>> SHELL;

    private static RegistryObject<SoundEvent> snd(String n) {
        return SOUNDS.register(n, () -> SoundEvent.createVariableRangeEvent((ResourceLocation)new ResourceLocation("uwar", n)));
    }

    private static Item ammoItem(W w) {
        return new SupplyItem(SupplyItem.Type.AMMO, w, new Item.Properties().stacksTo(w == W.CANNON ? 64 : 16));
    }

    static {
        for (final Kind k : Kind.values()) {
            RegistryObject<EntityType<?>> eo = ENTITIES.register(k.id, () -> EntityType.Builder.<Aircraft>of((t, l) -> new Aircraft(t, l, k), MobCategory.MISC).sized(k.w, k.h).clientTrackingRange(16).updateInterval(1).fireImmune().build(k.id));
            @SuppressWarnings({"unchecked", "rawtypes"})
            RegistryObject<EntityType<Aircraft>> typed = (RegistryObject) eo;
            AIRCRAFTS.put(k, typed);
            RegistryObject<Item> io = ITEMS.register(k.id, () -> new AircraftItem(k, new Item.Properties().stacksTo(1)));
            AIRCRAFT_ITEMS.put(k, io);
        }
        for (final W w : W.values()) {
            RegistryObject<Item> ao = ITEMS.register(w.item, () -> Reg.ammoItem(w));
            AMMO.put(w, ao);
        }
        FUEL_CAN = ITEMS.register("fuel_can", () -> new SupplyItem(SupplyItem.Type.FUEL, null, new Item.Properties().stacksTo(16)));
        REPAIR_KIT = ITEMS.register("repair_kit", () -> new SupplyItem(SupplyItem.Type.REPAIR, null, new Item.Properties().stacksTo(16)));
        RegistryObject<EntityType<?>> so = ENTITIES.register("shell", () -> EntityType.Builder.<Shell>of(Shell::new, MobCategory.MISC).sized(0.3f, 0.3f).clientTrackingRange(10).updateInterval(1).build("shell"));
        @SuppressWarnings({"unchecked", "rawtypes"})
        RegistryObject<EntityType<Shell>> shellTyped = (RegistryObject) so;
        SHELL = shellTyped;
    }
}

