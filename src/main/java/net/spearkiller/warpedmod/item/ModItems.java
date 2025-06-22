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
            () -> new ItemMagicMirror(new Item.Properties().stacksTo(1), false));

    public static final RegistryObject<Item> CELESTIAL_MIRROR = ITEMS.register(
            "celestial_mirror",
            () -> new ItemCelestialMirror(new Item.Properties().stacksTo(1),  true));

    public static final RegistryObject<Item> ABYSSAL_MIRROR = ITEMS.register(
            "abyssal_mirror",
            () -> new ItemAbyssalMirror(new Item.Properties().stacksTo(1),  true));



    public static final RegistryObject<Item> POTION_RECALL_LESSER = ITEMS.register(
            "potion_recall_lesser",
            () -> new ItemPotionRecall(false));
    public static final RegistryObject<Item> POTION_RECALL_GREATER = ITEMS.register(
            "potion_recall_greater",
            () -> new ItemPotionRecall(true));
    public static final RegistryObject<Item> POTION_REUNION_LESSER = ITEMS.register(
            "potion_reunion_lesser",
            () -> new ItemPotionReunion(false));
    public static final RegistryObject<Item> POTION_REUNION_GREATER = ITEMS.register(
            "potion_reunion_greater",
            () -> new ItemPotionReunion(true));

    //Not adding these for now, because they're so inefficient and I don't know how to make them better.
    /*public static final RegistryObject<Item> POTION_DISCORD_LESSER = ITEMS.register(
            "potion_discord_lesser",
            () -> new ItemPotionDiscord(500,false));
    public static final RegistryObject<Item> POTION_DISCORD_GREATER = ITEMS.register(
            "potion_discord_greater",
            () -> new ItemPotionDiscord(1000,true));*/

    public static final RegistryObject<Item> WITHERED_TOTEM = ITEMS.register(
            "withered_totem",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOTEM_OF_ASCENSION = ITEMS.register(
            "totem_of_ascension",
            () -> new ItemTotemOfAscension(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
