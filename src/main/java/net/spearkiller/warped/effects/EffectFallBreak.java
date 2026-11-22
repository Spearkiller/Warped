package net.spearkiller.warped.effects;

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
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.onGround()) {
            entity.removeEffect(ModEffects.FALL_BREAK);
        }

        if (entity.getRandom().nextInt(5) != 0) return false;

        if (entity.level().isClientSide()) return false;

        ServerLevel sLevel = (ServerLevel) entity.level();

        sLevel.sendParticles( ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 5, 0.05, 0.01, 0.05,0.05 );
        //WarpedMod.getLogger().debug("Ticking " + entity.getUUID());
        return true;
    }

}
