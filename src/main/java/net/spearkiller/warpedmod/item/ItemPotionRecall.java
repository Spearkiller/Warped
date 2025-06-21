package net.spearkiller.warpedmod.item;

import net.minecraft.ChatFormatting;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemPotionRecall extends Item {

    private final boolean ENHANCED_POTION;

    public ItemPotionRecall(boolean enhanced) {
        super(new Properties()
                .stacksTo(16)
                .food(new FoodProperties.Builder()
                        .alwaysEat()
                .build()));
        ENHANCED_POTION = enhanced;
    }

    // ------------------------------------------------------
    // Methods from Item Class
    // ------------------------------------------------------

    @Override
    public boolean isFoil(ItemStack stack) {
        return ENHANCED_POTION;
    }

    @Override
    public int getUseDuration(ItemStack stack){
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
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

        if (homeCoords == null) return stack;

        ServerLevel targetDimension = player.getServer().getLevel(spawnDimension);
        player.teleportTo(targetDimension, homeCoords.x(), homeCoords.y() + 0.1, homeCoords.z(), spawnAngle, 0f);

        AbstractMirror.trySpawnParticles(level, entity, ParticleTypes.END_ROD, 30, true);
        level.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.25f, 1.5f);

        if (!player.gameMode.getGameModeForPlayer().equals(GameType.CREATIVE)) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.potion_recall.tooltip_info").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        if (!this.ENHANCED_POTION) {
            pTooltipComponents.add(Component.translatable("tooltip.warpedmod.potion_recall.tooltip_lesser_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        }
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.potion_recall.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
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
            player.displayClientMessage(Component.translatable("info.warpedmod.potion_recall.missing_or_invalid_spawn_point")
                    .withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            return null;
        }

        // If spawn is in a different dimension
        if (!spawnDimension.equals(sLevel.dimension()) && !ENHANCED_POTION){
            player.displayClientMessage(Component.translatable("info.warpedmod.potion_recall.wrong_dimension").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            return null;
        }

        ServerLevel targetDimension = player.getServer().getLevel(spawnDimension);

        // Check the home bed is valid and unobstructed (basically, do we have somewhere we can teleport to?) - Part II
        Optional<Vec3> safeSpawn = ServerPlayer.findRespawnPositionAndUseSpawnBlock(
                targetDimension,
                spawnPos,
                spawnAngle,
                false,
                true
        );
        if (safeSpawn.isEmpty()) {
            player.displayClientMessage(Component.translatable("info.warpedmod.potion_recall.missing_or_invalid_spawn_point").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC), true);
            return null;
        }

        return safeSpawn.get();
    }

}
