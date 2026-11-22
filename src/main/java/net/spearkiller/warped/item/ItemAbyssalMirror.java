package net.spearkiller.warped.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.spearkiller.warped.Warped;
import net.spearkiller.warped.effects.ModEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAbyssalMirror extends AbstractMirror {

    public static final DustParticleOptions DUST_ABYSSAL_MIRROR = new DustParticleOptions(
            Vec3.fromRGB24(0x034150).toVector3f(), 1.0f
    );

    public ItemAbyssalMirror(Properties pProperties, boolean xDimension){
        super(pProperties, xDimension, DUST_ABYSSAL_MIRROR, Warped.ABYSSAL_MIRROR_WARP_SICKNESS_DURATION, null, null);
    }


    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        int duration = context.level().getGameRules().getRule(Warped.ABYSSAL_MIRROR_WARP_SICKNESS_DURATION).get() * 20;
        if (duration > 0) PotionContents.addPotionTooltip(
                List.of(new MobEffectInstance(ModEffects.WARP_SICKESS, duration, 0)),
                pTooltipComponents::add,
                1.0f, 20.0f
        );
        if (Screen.hasShiftDown()){
            pTooltipComponents.add(Component.translatable("tooltip.warped.abyssal_mirror.tooltip"));
            pTooltipComponents.add(Component.translatable("tooltip.warped.abyssal_mirror.tooltip2"));
            pTooltipComponents.add(Component.translatable("tooltip.warped.abyssal_mirror.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
            super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        }
        else {
            pTooltipComponents.add(Component.translatable("tooltip.warped.generic_toolip.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        }
    }
}
