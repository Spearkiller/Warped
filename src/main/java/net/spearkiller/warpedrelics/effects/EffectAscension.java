package net.spearkiller.warpedrelics.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.spearkiller.warpedrelics.WarpedRelics;

public class EffectAscension extends MobEffect {

    protected EffectAscension(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(
                NeoForgeMod.CREATIVE_FLIGHT,
                ResourceLocation.fromNamespaceAndPath(WarpedRelics.MODID, "ascension_effect_flight"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
                );
    }
}
