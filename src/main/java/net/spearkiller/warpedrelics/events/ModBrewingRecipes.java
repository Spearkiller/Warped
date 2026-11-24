package net.spearkiller.warpedrelics.events;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spearkiller.warpedrelics.WarpedRelics;
import net.spearkiller.warpedrelics.effects.ModEffects;
import net.spearkiller.warpedrelics.item.ModItems;

@EventBusSubscriber(modid = WarpedRelics.MODID)
public class ModBrewingRecipes {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, WarpedRelics.MODID );

    //The more and more I use these the more I regret not just making a different class but I'm too LAZY!!!
    public static final DeferredHolder<Potion, Potion> ASCENSION =
            POTIONS.register("ascension",
                    () -> new Potion(
                            "ascension",
                            new MobEffectInstance(
                                    ModEffects.ASCENSION,
                                    3600,
                                    0
                            )
                    )
            );

    public static final DeferredHolder<Potion, Potion> LONG_ASCENSION =
            POTIONS.register("long_ascension",
                    () -> new Potion(
                            "long_ascension",
                            new MobEffectInstance(
                                    ModEffects.ASCENSION,
                                    6000,
                                    0
                            )
                    )
            );

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.AWKWARD)),
                Ingredient.of(Items.PRISMARINE_CRYSTALS),
                ModItems.POTION_RECALL_LESSER.toStack()
        ));

        builder.addRecipe(new BrewingRecipe(
                Ingredient.of(ModItems.POTION_RECALL_LESSER),
                Ingredient.of(Items.GLOWSTONE_DUST),
                ModItems.POTION_RECALL_GREATER.toStack()
        ));

        builder.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.AWKWARD)),
                Ingredient.of(Items.ENDER_PEARL),
                ModItems.POTION_REUNION_LESSER.toStack()
        ));

        builder.addRecipe(new BrewingRecipe(
                Ingredient.of(ModItems.POTION_REUNION_LESSER),
                Ingredient.of(Items.GLOWSTONE_DUST),
                ModItems.POTION_REUNION_GREATER.toStack()
        ));

        builder.addMix(
                ASCENSION,
                Items.REDSTONE,
                LONG_ASCENSION
        );
    }
}
