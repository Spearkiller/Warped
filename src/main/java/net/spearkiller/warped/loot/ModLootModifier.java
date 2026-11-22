package net.spearkiller.warped.loot;


import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.spearkiller.warped.Warped;

import java.util.function.Supplier;

public class ModLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Warped.MODID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ROLL_LOOT_TABLE =
            LOOT_MODIFIERS.register("add_item", () -> AddItemModifier.CODEC);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ROLL_SUS_LOOT_TABLE =
            LOOT_MODIFIERS.register("add_sus_item", () -> AddSusItemModifier.CODEC);

    public static void register(IEventBus eventBus){
        LOOT_MODIFIERS.register(eventBus);
    }
}
