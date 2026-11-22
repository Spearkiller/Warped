package net.spearkiller.warped.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.spearkiller.warped.Warped;
import net.spearkiller.warped.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Warped.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.TARNISHED_MIRROR.get());
        basicItem(ModItems.MAGIC_MIRROR.get());
        basicItem(ModItems.CELESTIAL_MIRROR.get());
        basicItem(ModItems.ABYSSAL_MIRROR.get());

        basicItem(ModItems.WITHERED_TOTEM.get());
        basicItem(ModItems.TOTEM_OF_ASCENSION.get());

        basicItem(ModItems.POTION_RECALL_GREATER.get());
        basicItem(ModItems.POTION_RECALL_LESSER.get());
        basicItem(ModItems.POTION_REUNION_GREATER.get());
        basicItem(ModItems.POTION_REUNION_LESSER.get());
    }
}
