package net.spearkiller.warped.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    private final Set<ResourceLocation> contentOwned;

    public ModBlockLootTables(HolderLookup.Provider registries, Set<ResourceLocation> contentOwned) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        this.contentOwned = contentOwned;
    }

    @Override
    protected void generate() {

    }
}
