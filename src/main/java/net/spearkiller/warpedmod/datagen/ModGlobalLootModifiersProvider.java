package net.spearkiller.warpedmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.spearkiller.warpedmod.WarpedMod;
import net.spearkiller.warpedmod.item.ModItems;
import net.spearkiller.warpedmod.loot.AddItemModifier;
import net.spearkiller.warpedmod.loot.AddSusItemModifier;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {


    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, WarpedMod.MOD_ID);
    }

    @Override
    protected void start() {
        add("withered_totem_from_wither_skeletons", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse("entities/wither_skeleton")).build(),
                LootItemRandomChanceCondition.randomChance(0.01f).build()
        }, ModItems.WITHERED_TOTEM.get()));


        add("withered_totem_from_nether_fortresses", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.25f).build()
        }, ModItems.WITHERED_TOTEM.get()));

        String[] recallLocations = {
                "abandoned_mineshaft",
                "simple_dungeon",
                "stronghold_corridor",
                "stronghold_crossing"
        };

        for (String l: recallLocations){
            add("recall_potion_from_" + l, new AddItemModifier(new LootItemCondition[]{
                    new LootTableIdCondition.Builder(ResourceLocation.parse("chests/"+l)).build(),
                    LootItemRandomChanceCondition.randomChance(0.25f).build()

            }, ModItems.POTION_RECALL_LESSER.get()));
        }

        add("tarnished_mirror_from_trail_ruins", new AddSusItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse("archaeology/trail_ruins_rare")).build()
        }, ModItems.TARNISHED_MIRROR.get()));

        add("tarnished_mirror_from_desert_pyramid", new AddSusItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse("archaeology/desert_pyramid")).build()
        }, ModItems.TARNISHED_MIRROR.get()));


    }
}
