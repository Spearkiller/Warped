package net.spearkiller.warped.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.spearkiller.warped.Warped;
import net.spearkiller.warped.item.ModItems;
import net.spearkiller.warped.loot.AddItemModifier;
import net.spearkiller.warped.loot.AddSusItemModifier;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Warped.MODID);
    }

    @Override
    protected void start() {
        this.add("withered_totem_from_wither_skeletons",
                new AddItemModifier(new LootItemCondition[]{
                    new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/wither_skeleton")).build(),
                LootItemRandomChanceCondition.randomChance(0.01f).build()
        }, ModItems.WITHERED_TOTEM.get()));


        this.add("withered_totem_from_nether_fortresses", new AddItemModifier(
                new LootItemCondition[]{
                    new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.25f).build()
        }, ModItems.WITHERED_TOTEM.get()));

        String[] recallLocations = {
                "abandoned_mineshaft",
                "simple_dungeon",
                "stronghold_corridor",
                "stronghold_crossing"
        };

        for (String l: recallLocations){
            this.add("recall_potion_from_" + l,
                    new AddItemModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/"+l)).build(),
                    LootItemRandomChanceCondition.randomChance(0.25f).build()

            }, ModItems.POTION_RECALL_LESSER.get()));
        }

        this.add("tarnished_mirror_from_trail_ruins", new AddSusItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("archaeology/trail_ruins_rare")).build()
        }, ModItems.TARNISHED_MIRROR.get()));

        this.add("tarnished_mirror_from_ocean_ruin", new AddSusItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("archaeology/ocean_ruin_cold")).build()
        }, ModItems.TARNISHED_MIRROR.get()));

    }
}
