package net.spearkiller.warpedmod.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.spearkiller.warpedmod.WarpedMod;

public class EffectFallBreak extends MobEffect {

    protected EffectFallBreak(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.onGround()) {
            entity.removeEffect(ModEffects.FALL_BREAK.get());
        }

        if (entity.getRandom().nextInt(5) != 0) return;

        if (entity.level().isClientSide()) return;

        ServerLevel sLevel = (ServerLevel) entity.level();

        sLevel.sendParticles( ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 5, 0.05, 0.01, 0.05,0.05 );
        //WarpedMod.getLogger().debug("Ticking " + entity.getUUID());
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
