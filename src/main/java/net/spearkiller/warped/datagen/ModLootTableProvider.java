package net.spearkiller.warped.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.spearkiller.warped.datagen.loot.ModBlockLootTables;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, Set<ResourceLocation> contentOwned) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(registries -> new ModBlockLootTables(registries, contentOwned), LootContextParamSets.BLOCK)
                //new SubProviderEntry(ForestryChestLootTables::new, LootContextParamSets.CHEST),
                //new SubProviderEntry(ForestryGiftLootTables::new, LootContextParamSets.GIFT)
        ), lookupProvider);
    }
}
