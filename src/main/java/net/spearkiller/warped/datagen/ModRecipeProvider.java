package net.spearkiller.warped.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.spearkiller.warped.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        //Recipe for Magic Mirror
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.DIAMOND),
                        Ingredient.of(ModItems.TARNISHED_MIRROR.get()),
                        Ingredient.of(Items.GHAST_TEAR),
                        RecipeCategory.TOOLS,
                        ModItems.MAGIC_MIRROR.get())
                .unlocks("has_tarnished_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TARNISHED_MIRROR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("warped", "magic_mirror_smithing"));

        //Recipe for Celestial Mirror
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ModItems.MAGIC_MIRROR.get()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.TOOLS,
                        ModItems.CELESTIAL_MIRROR.get())
                .unlocks("has_magic_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MAGIC_MIRROR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("warped", "celestial_mirror_smithing"));

        //Recipe for Abyssal Mirror
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ModItems.CELESTIAL_MIRROR.get()),
                        Ingredient.of(Items.ECHO_SHARD),
                        RecipeCategory.TOOLS,
                        ModItems.ABYSSAL_MIRROR.get())
                .unlocks("has_celestial_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CELESTIAL_MIRROR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("warped", "abyssal_mirror_smithing"));

        //Recipe for Totem of Ascension
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(ModItems.WITHERED_TOTEM.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                ModItems.TOTEM_OF_ASCENSION.get())
                .unlocks("has_withered_totem", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WITHERED_TOTEM.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("warped", "totem_of_ascension_smithing"));
    }
}
