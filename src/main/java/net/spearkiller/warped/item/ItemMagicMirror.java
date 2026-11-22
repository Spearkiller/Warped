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

public class ItemMagicMirror extends AbstractMirror {

    public static final DustParticleOptions DUST_MAGIC_MIRROR = new DustParticleOptions(
            Vec3.fromRGB24(0xB3CFEC).toVector3f(), 1.0f
    );

    public ItemMagicMirror(Properties pProperties, boolean xDimension){
        super(pProperties, xDimension, DUST_MAGIC_MIRROR, Warped.MAGIC_MIRROR_WARP_SICKNESS_DURATION, Warped.MAGIC_MIRROR_BLOCKS_PER_LEVEL_COST, Warped.MAGIC_MIRROR_MAX_LEVEL_COST);
    }


    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (context.level() != null) {
            int duration = context.level().getGameRules().getRule(Warped.MAGIC_MIRROR_WARP_SICKNESS_DURATION).get() * 20;
            if (duration > 0) PotionContents.addPotionTooltip(
                    List.of(new MobEffectInstance(ModEffects.WARP_SICKESS, duration, 0)),
                    pTooltipComponents::add,
                    1.0f, 20.0f
            );

            if (Screen.hasShiftDown()){
                int blocksPerLevel = Math.max(context.level().getGameRules().getRule(COST_RULE).get(), 0);
                int maxCost = Math.max(context.level().getGameRules().getRule(MAX_COST_RULE).get(), 0);

                String pB = Warped.getPlural(blocksPerLevel, "block", "blocks");
                String mC = Warped.getPlural(maxCost, "level", "levels");

                pTooltipComponents.add(Component.translatable("tooltip.warped.magic_mirror.tooltip", blocksPerLevel, pB, maxCost, mC));
                pTooltipComponents.add(Component.translatable("tooltip.warped.magic_mirror.tooltip2", blocksPerLevel, pB, maxCost, mC));
                pTooltipComponents.add(Component.translatable("tooltip.warped.generic_toolip.no_interdimensional_travel").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
                pTooltipComponents.add(Component.translatable("tooltip.warped.magic_mirror.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
            }
            else {
                pTooltipComponents.add(Component.translatable("tooltip.warped.generic_toolip.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
            }
        }
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
    }
}
