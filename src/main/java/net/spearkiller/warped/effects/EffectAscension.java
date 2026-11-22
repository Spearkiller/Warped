package net.spearkiller.warped.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.spearkiller.warped.Warped;

import java.util.Set;

public class EffectAscension extends MobEffect {

    protected EffectAscension(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(
                NeoForgeMod.CREATIVE_FLIGHT,
                ResourceLocation.fromNamespaceAndPath(Warped.MODID, "ascension_effect_flight"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
                );
    }


    @Override
    public void onMobRemoved(LivingEntity livingEntity, int amplifier, Entity.RemovalReason reason) {
        super.onMobRemoved(livingEntity, amplifier, reason);

        if (!(livingEntity instanceof ServerPlayer player)) return;

        livingEntity.addEffect(new MobEffectInstance(
                ModEffects.FALL_BREAK, -1, 0, false, false
        ));

    }
}
