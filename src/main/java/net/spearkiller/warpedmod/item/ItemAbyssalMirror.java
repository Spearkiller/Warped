package net.spearkiller.warpedmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.spearkiller.warpedmod.WarpedMod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAbyssalMirror extends AbstractMirror {

    public static final DustParticleOptions DUST_ABYSSAL_MIRROR = new DustParticleOptions(
            Vec3.fromRGB24(0x1D1A1C).toVector3f(), 1.0f
    );

    public ItemAbyssalMirror(Properties pProperties, boolean xDimension){
        super(pProperties, xDimension, DUST_ABYSSAL_MIRROR, null, null);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.abyssal_mirror.tooltip").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.abyssal_mirror.tooltip2").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.abyssal_mirror.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
