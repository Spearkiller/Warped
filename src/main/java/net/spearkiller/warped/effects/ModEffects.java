package net.spearkiller.warped.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spearkiller.warped.Warped;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Warped.MODID);

    public static final DeferredHolder<MobEffect, EffectFallBreak> FALL_BREAK = EFFECTS.register(
            "fall_break", () -> new EffectFallBreak(MobEffectCategory.BENEFICIAL, 0xFECBE6));

    public static final DeferredHolder<MobEffect, EffectFallBreak> WARP_SICKESS = EFFECTS.register(
            "warp_sickness", () -> new EffectFallBreak(MobEffectCategory.HARMFUL, 0x4E1382));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
