package net.spearkiller.warpedmod.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.spearkiller.warpedmod.WarpedMod;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WarpedMod.MOD_ID);

    public static final RegistryObject<Item> MAGIC_MIRROR = ITEMS.register(
            "magic_mirror",
            () -> new ItemMagicMirror(new Item.Properties(), 250, 5, false));

    public static final RegistryObject<Item> CELESTIAL_MIRROR = ITEMS.register(
            "celestial_mirror",
            () -> new ItemCelestialMirror(new Item.Properties(), 500, 3, true));

    public static final RegistryObject<Item> ABYSSAL_MIRROR = ITEMS.register(
            "abyssal_mirror",
            () -> new ItemAbyssalMirror(new Item.Properties(), 0, 0, true));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
