package net.spearkiller.warpedrelics.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.scores.Team;
import net.spearkiller.warpedrelics.WarpedRelics;
import net.spearkiller.warpedrelics.effects.ModEffects;

import java.util.*;

public class ItemPotionReunion extends Item{
    private final boolean ENHANCED_POTION;
    public static final String TAG_SELECTED_PLAYER = "warpedSelectedPlayer";

    public ItemPotionReunion(boolean enhanced){
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
        if (player.isShiftKeyDown()){
            ItemStack stack = player.getItemInHand(hand);

            if (!(level instanceof ServerLevel sLevel)) return InteractionResultHolder.success(stack);
            if (!(player instanceof ServerPlayer sPlayer)) return InteractionResultHolder.success(stack);

            //Get all players in the world/dimension
            List<ServerPlayer> players = findPlayers(sPlayer, (ItemPotionReunion)stack.getItem());
            if (players.isEmpty()){
                player.displayClientMessage(Component.translatable("info.warped.potion_reunion.forever_alone")
                        .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
                return InteractionResultHolder.success(stack);
            }

            //Get the current index so we can work out who the next one is.
            //If current index can't be found, then it'll just reset to zero.


            UUID current;
            if (!player.getPersistentData().contains(TAG_SELECTED_PLAYER) || player.getPersistentData().getUUID(TAG_SELECTED_PLAYER) == null) {
                current = players.getFirst().getUUID();
            }
            else current = player.getPersistentData().getUUID(TAG_SELECTED_PLAYER);

            int currentIndex = -1;
            for (int i = 0; i < players.size(); i++){
                if (players.get(i).getUUID().equals(current)) {
                    currentIndex = i;
                    break;
                }
            }

            //Get next player
            int nextIndex = (currentIndex + 1) % players.size();
            ServerPlayer nextPlayer = players.get(nextIndex);
            player.getPersistentData().putUUID(TAG_SELECTED_PLAYER, nextPlayer.getUUID());

            player.displayClientMessage(Component.translatable("info.warped.potion_reunion.next_player", nextPlayer.getName(), nextIndex+1, players.size())
                    .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            return InteractionResultHolder.success(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (!(entity instanceof ServerPlayer player)) return stack;

        //Rerun player check, to make sure player doesn't end up at an invalid player

        //Check if the player is drinking the potion before assigning anyone to it
        UUID targetUUID;

        if (!player.getPersistentData().contains(TAG_SELECTED_PLAYER) || player.getPersistentData().getUUID(TAG_SELECTED_PLAYER) == null) {
            player.displayClientMessage(Component.translatable("info.warped.potion_reunion.no_player")
                    .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
        }
        else {
            //Check assigned player is still in the sever
            targetUUID = player.getPersistentData().getUUID(TAG_SELECTED_PLAYER);
            ServerPlayer targetPlayer = Objects.requireNonNull(player.getServer()).getPlayerList().getPlayer(targetUUID);
            if (targetPlayer == null) {
                player.displayClientMessage(Component.translatable("info.warped.potion_reunion.missing_player")
                        .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            }
            else {
                //Player is still in the server and accessible
                if (checkPlayer(targetPlayer, player, (ItemPotionReunion) stack.getItem())){
                    ServerLevel targetDimension = targetPlayer.serverLevel();

                    level.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1.5f);
                    targetDimension.gameEvent(GameEvent.RESONATE_15, player.position(), GameEvent.Context.of(player));

                    player.teleportTo(targetDimension, targetPlayer.blockPosition().getX(), targetPlayer.blockPosition().getY() + 0.1, targetPlayer.blockPosition().getZ(), targetPlayer.getXRot(), targetPlayer.getYRot());

                    AbstractMirror.trySpawnParticles(level, entity, ParticleTypes.END_ROD, 30, true);
                    level.playSound(null, entity.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1.5f);

                    AbstractMirror.trySpawnParticles(targetDimension, targetPlayer, ParticleTypes.END_ROD, 30, true);
                    targetDimension.playSound(null, targetPlayer.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1.5f);
                    targetDimension.gameEvent(GameEvent.RESONATE_15, targetPlayer.position(), GameEvent.Context.of(player));
                }
                //Player has changed dimensions or teams
                else {
                    player.displayClientMessage(Component.translatable("info.warped.potion_reunion.missing_player")
                            .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
                }
            }
        }

        if (!player.gameMode.getGameModeForPlayer().equals(GameType.CREATIVE)) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if (context.level() == null) {
            super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
            return;
        }
        
        int duration = context.level().getGameRules().getRule(WarpedRelics.RECALL_POTION_WARP_SICKNESS_DURATION).get() * 20;
        if (duration > 0) PotionContents.addPotionTooltip(
                List.of(new MobEffectInstance(ModEffects.WARP_SICKESS, duration, 0)),
                pTooltipComponents::add,
                1.0f, 20.0f
        );

        if (Screen.hasShiftDown()){
            pTooltipComponents.add(Component.translatable("tooltip.warpedrelics.potion_reunion.tooltip_info").withStyle(ChatFormatting.ITALIC));
            if (!this.ENHANCED_POTION) {
                pTooltipComponents.add(Component.translatable("tooltip.warpedrelics.generic_toolip.no_interdimensional_travel").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
            }
            pTooltipComponents.add(Component.translatable("tooltip.warpedrelics.potion_reunion.tooltip_info2").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        }
        else {
            pTooltipComponents.add(Component.translatable("tooltip.warpedrelics.generic_toolip.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
    }

    // ------------------------------------------------------
    // Custom methods
    // ------------------------------------------------------

    private static List<ServerPlayer> findPlayers(ServerPlayer player, ItemPotionReunion i) {

        return player.server.getPlayerList().getPlayers().stream().filter(p -> checkPlayer(p, player, i)).toList();
    }

    private static boolean checkPlayer(ServerPlayer s, ServerPlayer p, ItemPotionReunion i){
        //Skip yourself
        if (s.getUUID().equals(p.getUUID())) return false;

        //Skip players in other dimensions if this is not a greater potion
        if (!i.isEnhanced()) {
            if (s.level().dimension() != p.level().dimension()) return false;
        }

        //If the potion should ignore teams, add players regardless of team
        if (p.level().getGameRules().getRule(WarpedRelics.REUNION_POTION_IGNORE_TEAMS).get()){
            return true;
        }

        //Otherwise, perform a team check
        else {
            Team otherTeam = s.getTeam();
            return (otherTeam == null && p.level().getGameRules().getRule(WarpedRelics.REUNION_POTION_FUZZY_TEAMS).get())
                    || otherTeam.isAlliedTo(p.getTeam());
        }
    }

    public boolean isEnhanced() { return this.ENHANCED_POTION; }
}
