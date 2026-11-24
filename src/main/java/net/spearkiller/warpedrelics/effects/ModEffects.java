package net.spearkiller.warpedrelics.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.spearkiller.warpedrelics.WarpedRelics;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WarpedRelics.MODID);

    public static final DeferredHolder<MobEffect, EffectFallBreak> FALL_BREAK = EFFECTS.register(
            "fall_break", () -> new EffectFallBreak(MobEffectCategory.BENEFICIAL, 0xFECBE6));

    public static final DeferredHolder<MobEffect, EffectWarpSickness> WARP_SICKESS = EFFECTS.register(
            "warp_sickness", () -> new EffectWarpSickness(MobEffectCategory.HARMFUL, 0x4E1382));

    public static final DeferredHolder<MobEffect, EffectAscension> ASCENSION = EFFECTS.register(
            "ascension", () -> new EffectAscension(MobEffectCategory.BENEFICIAL, 0xFDFFA8));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
