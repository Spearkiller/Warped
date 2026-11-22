package net.spearkiller.warped.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.spearkiller.warped.Warped;
import net.spearkiller.warped.effects.ModEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemPotionRecall extends Item {

    private final boolean ENHANCED_POTION;

    public ItemPotionRecall(boolean enhanced) {
        super(new Properties()
                .stacksTo(1)
                .food(new FoodProperties.Builder()
                        .alwaysEdible()
                        .build()));
        this.ENHANCED_POTION = enhanced;
    }

    // ------------------------------------------------------
    // Methods from Item Class
    // ------------------------------------------------------

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.ENHANCED_POTION;
    }

    //@Override
    //public int getUseDuration(ItemStack stack){ return 32; }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.hasEffect(ModEffects.WARP_SICKESS)){
            player.displayClientMessage(Component.translatable("info.warped.potions.use_cancelled_by_warp_sickness")
                    .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC), true);
            player.getCooldowns().addCooldown(this, 20);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (!(entity instanceof ServerPlayer player)) return stack;

        float spawnAngle = player.getRespawnAngle();
        ResourceKey<Level> spawnDimension = player.getRespawnDimension();

        ServerLevel sLevel = (ServerLevel) player.level();
        //Logic borrowed from Abstract Mirror thanks me

        Vec3 homeCoords = tryUse(sLevel, player);

        if (homeCoords == null) {
            player.getCooldowns().addCooldown(this, 20);
            return stack;
        }

        ServerLevel targetDimension = player.getServer().getLevel(spawnDimension);

        AbstractMirror.trySpawnParticles(level, entity, ParticleTypes.END_ROD, 30, true);

        //BlockPos oldCoords = new BlockPos((int)homeCoords.x, (int)homeCoords.y, (int)homeCoords.z);
        level.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1.5f);
        sLevel.gameEvent(GameEvent.RESONATE_15, player.blockPosition(), GameEvent.Context.of(player));

        player.teleportTo(targetDimension, homeCoords.x(), homeCoords.y() + 0.1, homeCoords.z(), spawnAngle, 0f);

        BlockPos newCoords = new BlockPos((int)homeCoords.x, (int)homeCoords.y, (int)homeCoords.z);
        level.playSound(null, newCoords, SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1.5f);
        sLevel.gameEvent(GameEvent.RESONATE_15, newCoords, GameEvent.Context.of(player));
        AbstractMirror.trySpawnParticles(level, entity, newCoords.getCenter(), ParticleTypes.END_ROD, 30, true);

        if (!player.gameMode.getGameModeForPlayer().equals(GameType.CREATIVE)) {
            player.addEffect(new MobEffectInstance(
                ModEffects.WARP_SICKESS,
                player.level().getGameRules().getRule(Warped.RECALL_POTION_WARP_SICKNESS_DURATION).get() * 20,
                0,
                false,
                false)
            );
            player.getCooldowns().addCooldown(this, 20);
            return new ItemStack(Items.GLASS_BOTTLE);
        }

        player.getCooldowns().addCooldown(this, 20);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        int duration = context.level().getGameRules().getRule(Warped.RECALL_POTION_WARP_SICKNESS_DURATION).get() * 20;
        PotionContents.addPotionTooltip(
                List.of(new MobEffectInstance(ModEffects.WARP_SICKESS, duration, 0)),
                pTooltipComponents::add,
                1.0f, 20.0f
        );

        if (Screen.hasShiftDown()){
            pTooltipComponents.add(Component.translatable("tooltip.warped.potion_recall.tooltip_info"));
            if (!this.ENHANCED_POTION) {
                pTooltipComponents.add(Component.translatable("tooltip.warped.generic_toolip.no_interdimensional_travel").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
            }
            pTooltipComponents.add(Component.translatable("tooltip.warped.potion_recall.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        }
        else {
            pTooltipComponents.add(Component.translatable("tooltip.warped.generic_toolip.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        }

    }

    // ------------------------------------------------------
    // Custom methods
    // ------------------------------------------------------

    /**
     * Checks if the player meets all the criteria to teleport to their home spawn.
     *
     * @param sLevel
     * @param player
     * @return
     */
    protected Vec3 tryUse(ServerLevel sLevel, ServerPlayer player){

        BlockPos spawnPos = player.getRespawnPosition();
        float spawnAngle = player.getRespawnAngle();
        ResourceKey<Level> spawnDimension = player.getRespawnDimension();

        //Check that the player has set their spawnpoint at least once.
        if (spawnPos == null) {
            // Never set a spawn
            player.displayClientMessage(Component.translatable("info.warped.potion_recall.missing_or_invalid_spawn_point")
                    .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            player.getCooldowns().addCooldown(this, 20);
            return null;
        }

        // If spawn is in a different dimension
        if (!spawnDimension.equals(sLevel.dimension()) && !ENHANCED_POTION){
            player.displayClientMessage(Component.translatable("info.warped.potion_recall.wrong_dimension").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            player.getCooldowns().addCooldown(this, 20);
            return null;
        }

        ServerLevel targetDimension = player.getServer().getLevel(spawnDimension);

        DimensionTransition ds = player.findRespawnPositionAndUseSpawnBlock(false, DimensionTransition.DO_NOTHING);

        if (ds.missingRespawnBlock()) {
            player.displayClientMessage(Component.translatable("info.warped.potion_recall.missing_or_invalid_spawn_point").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            player.getCooldowns().addCooldown(this, 20);
            return null;
        }
        return ds.pos();
    }

}
