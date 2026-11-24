package net.spearkiller.warpedrelics.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.spearkiller.warpedrelics.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

//This code is based off of my work from Forestry. And by that I mean it's mostly based off TheDarkColour's work.
public class ModAdvancementProvider extends AdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
        super(output, registries, existingFileHelper, subProviders);
    }

    public static class Advancements implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer, ExistingFileHelper existingFileHelper) {

            //Mirror, Mirror
            AdvancementHolder tarnishedMirror = Advancement.Builder.advancement()
                    .parent(ResourceLocation.fromNamespaceAndPath("minecraft", "adventure/salvage_sherd"))
                    .display(
                            ModItems.TARNISHED_MIRROR.get(),
                            Component.translatable("advancement.warpedrelics.tarnished_mirror"),
                            Component.translatable("advancement.warpedrelics.tarnished_mirror.desc"),
                            null,
                            AdvancementType.TASK,
                            true, true, false
                    )
                    .addCriterion("warpedrelics:has_tarnished_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TARNISHED_MIRROR.get()))
                    .save(writer, ResourceLocation.fromNamespaceAndPath("warpedrelics", "get_tarnished_mirror"), existingFileHelper);

            //Upon reflection...
            AdvancementHolder magicMirror = Advancement.Builder.advancement()
                    .parent(tarnishedMirror)
                    .display(
                            ModItems.MAGIC_MIRROR.get(),
                            Component.translatable("advancement.warpedrelics.magic_mirror"),
                            Component.translatable("advancement.warpedrelics.magic_mirror.desc"),
                            null,
                            AdvancementType.TASK,
                            true, true, false
                    )
                    .addCriterion("warpedrelics:has_magic_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MAGIC_MIRROR.get()))
                    .save(writer, ResourceLocation.fromNamespaceAndPath("warpedrelics", "get_magic_mirror"), existingFileHelper);

            //Seeing stars
            AdvancementHolder celestialMirror = Advancement.Builder.advancement()
                    .parent(magicMirror)
                    .display(
                            ModItems.CELESTIAL_MIRROR.get(),
                            Component.translatable("advancement.warpedrelics.celestial_mirror"),
                            Component.translatable("advancement.warpedrelics.celestial_mirror.desc"),
                            null,
                            AdvancementType.TASK,
                            true, true, false
                    )
                    .addCriterion("warpedrelics:has_celestial_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CELESTIAL_MIRROR.get()))
                    .save(writer, ResourceLocation.fromNamespaceAndPath("warpedrelics", "get_celestial_mirror"), existingFileHelper);

            //It stares back
            AdvancementHolder abyssalMirror = Advancement.Builder.advancement()
                    .parent(celestialMirror)
                    .display(
                            ModItems.ABYSSAL_MIRROR.get(),
                            Component.translatable("advancement.warpedrelics.abyssal_mirror"),
                            Component.translatable("advancement.warpedrelics.abyssal_mirror.desc"),
                            null,
                            AdvancementType.GOAL,
                            true, true, false
                    )
                    .addCriterion("warpedrelics:has_abyssal_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ABYSSAL_MIRROR.get()))
                    .save(writer, ResourceLocation.fromNamespaceAndPath("warpedrelics", "get_abyssal_mirror"), existingFileHelper);

            //Withered Totem
            AdvancementHolder witheredTotem = Advancement.Builder.advancement()
                    .parent(ResourceLocation.fromNamespaceAndPath("minecraft", "nether/find_fortress"))
                    .display(
                            ModItems.WITHERED_TOTEM.get(),
                            Component.translatable("advancement.warpedrelics.withered_totem"),
                            Component.translatable("advancement.warpedrelics.withered_totem.desc"),
                            null,
                            AdvancementType.TASK,
                            true, true, false
                    )
                    .addCriterion("warpedrelics:has_withered_totem", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WITHERED_TOTEM.get()))
                    .save(writer, ResourceLocation.fromNamespaceAndPath("warpedrelics", "get_withered_totem"), existingFileHelper);

            //Totem of Ascension
            AdvancementHolder totemOfAscension = Advancement.Builder.advancement()
                    .parent(witheredTotem)
                    .display(
                            ModItems.TOTEM_OF_ASCENSION.get(),
                            Component.translatable("advancement.warpedrelics.totem_of_ascension"),
                            Component.translatable("advancement.warpedrelics.totem_of_ascension.desc"),
                            null,
                            AdvancementType.GOAL,
                            true, true, false
                    )
                    .addCriterion("warpedrelics:has_totem_of_ascension", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOTEM_OF_ASCENSION.get()))
                    .save(writer, ResourceLocation.fromNamespaceAndPath("warpedrelics", "get_totem_of_ascension"), existingFileHelper);


            //Homecoming - Use a magic mirror or potion of recall.
            //Far From Home - Use a Potion of Discord
            //No Way Home - try to use a magic mirror or potion of recall with Warp Sickness

            //
            //Advancement usePotionOfRecall;

            //Reunited
            //Advancement usePotionOfReunion;

            //Guess who's back?
            //Advancement usePotionOfReturn;
        }
    }
}
