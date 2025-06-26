package net.spearkiller.warpedmod.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.spearkiller.warpedmod.item.ModItems;

import java.util.function.Consumer;

public class ModAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> writer, ExistingFileHelper existingFileHelper)  {

        //Mirror, Mirror
        Advancement tarnishedMirror = Advancement.Builder.advancement()
                .parent(ResourceLocation.fromNamespaceAndPath("minecraft", "adventure/salvage_sherd"))
                .display(
                        ModItems.TARNISHED_MIRROR.get(),
                        Component.translatable("advancement.warpedmod.tarnished_mirror"),
                        Component.translatable("advancement.warpedmod.tarnished_mirror.desc"),
                        null,
                        FrameType.TASK,
                        true, true, false
                )
                .addCriterion("warpedmod:has_tarnished_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TARNISHED_MIRROR.get()))
                .requirements(new String[][] { { "warpedmod:has_tarnished_mirror" } })
                .save(writer, ResourceLocation.fromNamespaceAndPath("warpedmod", "get_tarnished_mirror"), existingFileHelper);

        //Upon reflection...
        Advancement magicMirror = Advancement.Builder.advancement()
                .parent(tarnishedMirror)
                .display(
                        ModItems.MAGIC_MIRROR.get(),
                        Component.translatable("advancement.warpedmod.magic_mirror"),
                        Component.translatable("advancement.warpedmod.magic_mirror.desc"),
                        null,
                        FrameType.TASK,
                        true, true, false
                )
                .addCriterion("warpedmod:has_magic_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MAGIC_MIRROR.get()))
                .requirements(new String[][] { { "warpedmod:has_magic_mirror" } })
                .save(writer,  ResourceLocation.fromNamespaceAndPath("warpedmod", "get_magic_mirror"),existingFileHelper);

        //Seeing stars
        Advancement celestialMirror = Advancement.Builder.advancement()
                .parent(magicMirror)
                .display(
                        ModItems.CELESTIAL_MIRROR.get(),
                        Component.translatable("advancement.warpedmod.celestial_mirror"),
                        Component.translatable("advancement.warpedmod.celestial_mirror.desc"),
                        null,
                        FrameType.TASK,
                        true, true, false
                )
                .addCriterion("warpedmod:has_celestial_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CELESTIAL_MIRROR.get()))
                .requirements(new String[][] { { "warpedmod:has_celestial_mirror" } })
                .save(writer, ResourceLocation.fromNamespaceAndPath("warpedmod", "get_celestial_mirror"),existingFileHelper);

        //It stares back
        Advancement abyssalMirror = Advancement.Builder.advancement()
                .parent(celestialMirror)
                .display(
                        ModItems.ABYSSAL_MIRROR.get(),
                        Component.translatable("advancement.warpedmod.abyssal_mirror"),
                        Component.translatable("advancement.warpedmod.abyssal_mirror.desc"),
                        null,
                        FrameType.GOAL,
                        true, true, false
                )
                .addCriterion("warpedmod:has_abyssal_mirror", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ABYSSAL_MIRROR.get()))
                .requirements(new String[][] { { "warpedmod:has_abyssal_mirror" } })
                .save(writer, ResourceLocation.fromNamespaceAndPath("warpedmod", "get_abyssal_mirror"), existingFileHelper);


        //Withered Totem
        Advancement witheredTotem = Advancement.Builder.advancement()
                .parent(ResourceLocation.fromNamespaceAndPath("minecraft", "nether/find_fortress"))
                .display(
                        ModItems.WITHERED_TOTEM.get(),
                        Component.translatable("advancement.warpedmod.withered_totem"),
                        Component.translatable("advancement.warpedmod.withered_totem.desc"),
                        null,
                        FrameType.TASK,
                        true, true, false
                )
                .addCriterion("warpedmod:has_withered_totem", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WITHERED_TOTEM.get()))
                .requirements(new String[][] { { "warpedmod:has_withered_totem" } })
                .save(writer, ResourceLocation.fromNamespaceAndPath("warpedmod", "get_withered_totem"), existingFileHelper);

        //Totem of Ascension
        Advancement totemOfAscension = Advancement.Builder.advancement()
                .parent(witheredTotem)
                .display(
                        ModItems.TOTEM_OF_ASCENSION.get(),
                        Component.translatable("advancement.warpedmod.totem_of_ascension"),
                        Component.translatable("advancement.warpedmod.totem_of_ascension.desc"),
                        null,
                        FrameType.GOAL,
                        true, true, false
                )
                .addCriterion("warpedmod:has_totem_of_ascension", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOTEM_OF_ASCENSION.get()))
                .requirements(new String[][] { { "warpedmod:has_totem_of_ascension" } })
                .save(writer,  ResourceLocation.fromNamespaceAndPath("warpedmod", "get_totem_of_ascension"),existingFileHelper);


        //
        //Advancement usePotionOfRecall;

        //Reunited
        //Advancement usePotionOfReunion;

        //Guess who's back?
        //Advancement usePotionOfReturn;
    }
}
