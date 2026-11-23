package net.spearkiller.warped.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spearkiller.warped.Warped;
import net.spearkiller.warped.events.ModBrewingRecipes;

import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Warped.MODID);

    public static final Supplier<CreativeModeTab> WARPED_ITEMS_TAB = CREATIVE_MODE_TABS.register("warped_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MAGIC_MIRROR.get()))
                    .title(Component.translatable("creativetab.warped_items"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModItems.TARNISHED_MIRROR.get());
                        pOutput.accept(ModItems.MAGIC_MIRROR.get());
                        pOutput.accept(ModItems.CELESTIAL_MIRROR.get());
                        pOutput.accept(ModItems.ABYSSAL_MIRROR.get());
                        pOutput.accept(ModItems.WITHERED_TOTEM.get());
                        pOutput.accept(ModItems.TOTEM_OF_ASCENSION.get());
                        pOutput.accept(ModItems.POTION_RECALL_LESSER.get());
                        pOutput.accept(ModItems.POTION_RECALL_GREATER.get());
                        pOutput.accept(ModItems.POTION_REUNION_LESSER.get());
                        pOutput.accept(ModItems.POTION_REUNION_GREATER.get());
                        //pOutput.accept(ModItems.POTION_DISCORD_LESSER.get());
                        //pOutput.accept(ModItems.POTION_DISCORD_GREATER.get());
                        pOutput.accept(PotionContents.createItemStack(Items.POTION, ModBrewingRecipes.ASCENSION));
                        pOutput.accept(PotionContents.createItemStack(Items.POTION, ModBrewingRecipes.LONG_ASCENSION));

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
