package net.spearkiller.warped.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.spearkiller.warped.Warped;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@EventBusSubscriber(modid = Warped.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModAdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of(new ModAdvancementProvider.Advancements())));
        //generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModItemModelProvider(packOutput, existingFileHelper) );
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));

    }
}
