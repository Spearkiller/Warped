package net.spearkiller.warpedmod.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.spearkiller.warpedmod.WarpedMod;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WarpedMod.MOD_ID);

    public static final RegistryObject<MobEffect> FALL_BREAK = EFFECTS.register(
            "fall_break", () -> new EffectFallBreak(MobEffectCategory.BENEFICIAL, 0xFECBE6));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
