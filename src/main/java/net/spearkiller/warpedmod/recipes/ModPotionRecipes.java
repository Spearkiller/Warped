package net.spearkiller.warpedmod.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.spearkiller.warpedmod.item.ModItems;

public class ModPotionRecipes {
    public static void register(FMLCommonSetupEvent event) {

        //TODO: Add a way to disable recipes. Maybe via config? I'm too lazy for that rn.
        //Chances are if people want the mod then they also want the potions.
        //They're supposed to be common enough (though still valuable) until you can
        //find a tarnished mirror.
        
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    new BrewingRecipe(
                            Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                            Ingredient.of(Items.PRISMARINE_CRYSTALS),
                            new ItemStack(ModItems.POTION_RECALL_LESSER.get())
                    )
            );
        });

        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    new BrewingRecipe(
                            Ingredient.of(ModItems.POTION_RECALL_LESSER.get()),
                            Ingredient.of(Items.GLOWSTONE_DUST),
                            new ItemStack(ModItems.POTION_RECALL_GREATER.get())
                    )
            );
        });

        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    new BrewingRecipe(
                            Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                            Ingredient.of(Items.ENDER_PEARL),
                            new ItemStack(ModItems.POTION_REUNION_LESSER.get())
                    )
            );
        });

        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    new BrewingRecipe(
                            Ingredient.of(ModItems.POTION_REUNION_LESSER.get()),
                            Ingredient.of(Items.GLOWSTONE_DUST),
                            new ItemStack(ModItems.POTION_REUNION_GREATER.get())
                    )
            );
        });


    }
}
