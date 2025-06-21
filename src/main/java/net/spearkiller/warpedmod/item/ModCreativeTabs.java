package net.spearkiller.warpedmod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.spearkiller.warpedmod.WarpedMod;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WarpedMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> WARPED_ITEMS_TAB = CREATIVE_MODE_TABS.register("tutorial_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MAGIC_MIRROR.get()))
                    .title(Component.translatable("creativetab.warpedmod_items"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModItems.MAGIC_MIRROR.get());
                        pOutput.accept(ModItems.CELESTIAL_MIRROR.get());
                        pOutput.accept(ModItems.ABYSSAL_MIRROR.get());
                        pOutput.accept(ModItems.TOTEM_OF_ASCENSION.get());
                        pOutput.accept(ModItems.POTION_RECALL_LESSER.get());
                        pOutput.accept(ModItems.POTION_RECALL_GREATER.get());
                        //pOutput.accept(ModItems.POTION_DISCORD_LESSER.get());
                        //pOutput.accept(ModItems.POTION_DISCORD_GREATER.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
