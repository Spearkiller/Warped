package net.spearkiller.warpedmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.spearkiller.warpedmod.WarpedMod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WarpedMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
        //        .add(ModBlocks.SAPPHIRE_ORE.get()).addTag(Tags.Blocks.ORES);

        //this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
        //        .add(ModBlocks.???.get(),
        //                ModBlocks.???.get(),


        //this.tag(BlockTags.NEEDS_IRON_TOOL)
        //        .add(ModBlocks.SAPPHIRE_BLOCK.get());


    }
}
