package net.spearkiller.warpedmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.spearkiller.warpedmod.WarpedMod;
import net.spearkiller.warpedmod.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WarpedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.TARNISHED_MIRROR);
        simpleItem(ModItems.MAGIC_MIRROR);
        simpleItem(ModItems.CELESTIAL_MIRROR);
        simpleItem(ModItems.ABYSSAL_MIRROR);

        simpleItem(ModItems.WITHERED_TOTEM);
        simpleItem(ModItems.TOTEM_OF_ASCENSION);

        simpleItem(ModItems.POTION_RECALL_GREATER);
        simpleItem(ModItems.POTION_RECALL_LESSER);
        simpleItem(ModItems.POTION_REUNION_GREATER);
        simpleItem(ModItems.POTION_REUNION_LESSER);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(WarpedMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}
