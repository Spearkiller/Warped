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

public class ItemCelestialMirror extends AbstractMirror {

    public static final DustParticleOptions DUST_CELESTIAL_MIRROR = new DustParticleOptions(
            Vec3.fromRGB24(0xB9C9C9).toVector3f(), 1.0f
    );

    public ItemCelestialMirror(Properties pProperties, boolean xDimension){
        super(pProperties, xDimension, DUST_CELESTIAL_MIRROR, WarpedMod.CELESTIAL_MIRROR_BLOCKS_PER_LEVEL_COST, WarpedMod.CELESTIAL_MIRROR_MAX_LEVEL_COST);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pLevel != null) {
            int blocksPerLevel = Math.max(pLevel.getGameRules().getRule(COST_RULE).get(), 0);
            int maxCost = Math.max(pLevel.getGameRules().getRule(MAX_COST_RULE).get(), 0);

            String pB = WarpedMod.getPlural(blocksPerLevel, "block", "blocks");
            String mC = WarpedMod.getPlural(maxCost, "level", "levels");

            pTooltipComponents.add(Component.translatable("tooltip.warpedmod.celestial_mirror.tooltip").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
            pTooltipComponents.add(Component.translatable("tooltip.warpedmod.celestial_mirror.tooltip2", blocksPerLevel, pB, maxCost, mC).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
            pTooltipComponents.add(Component.translatable("tooltip.warpedmod.celestial_mirror.tooltip3", maxCost, mC).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
            pTooltipComponents.add(Component.translatable("tooltip.warpedmod.celestial_mirror.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
