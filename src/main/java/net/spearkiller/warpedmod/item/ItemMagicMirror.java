package net.spearkiller.warpedmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemMagicMirror extends AbstractMirror {

    public static final DustParticleOptions DUST_MAGIC_MIRROR = new DustParticleOptions(
            Vec3.fromRGB24(0xB3CFEC).toVector3f(), 1.0f
    );

    public ItemMagicMirror(Properties pProperties, int blocks, int max_cost, boolean xDimension){
        super(pProperties, blocks, max_cost, xDimension, DUST_MAGIC_MIRROR);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.magic_mirror.tooltip").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.magic_mirror.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
