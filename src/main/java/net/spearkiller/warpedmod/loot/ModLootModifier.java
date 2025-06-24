package net.spearkiller.warpedmod.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.spearkiller.warpedmod.WarpedMod;

public class ModLootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, WarpedMod.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ROLL_LOOT_TABLE = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ROLL_SUS_LOOT_TABLE = LOOT_MODIFIERS.register("add_sus_item", AddSusItemModifier.CODEC);
    public static void register(IEventBus eventBus){
        LOOT_MODIFIERS.register(eventBus);
    }
}
