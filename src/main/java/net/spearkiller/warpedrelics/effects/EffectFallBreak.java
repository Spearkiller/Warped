package net.spearkiller.warpedrelics.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectFallBreak extends MobEffect {

    protected EffectFallBreak(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        //Warped.LOGGER.debug("Ticking effect");

        if (entity.onGround()) {
            //Warped.LOGGER.debug("Removing effect");
            entity.removeEffect(ModEffects.FALL_BREAK);
        }

        if (entity.getRandom().nextInt(5) != 0) return true;

        if (entity.level().isClientSide()) return true;

        ServerLevel sLevel = (ServerLevel) entity.level();

        //Warped.LOGGER.debug("Sending particles");
        sLevel.sendParticles( ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 5, 0.05, 0.01, 0.05,0.05 );
        //warped.getLogger().debug("Ticking " + entity.getUUID());
        return true;
    }

}
