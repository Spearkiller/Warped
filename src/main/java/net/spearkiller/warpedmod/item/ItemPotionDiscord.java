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
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemPotionDiscord extends Item {

    private final int TELEPORT_RADIUS;
    private final boolean ENHANCED_POTION;

    public ItemPotionDiscord(int tpRadius, boolean enhanced) {
        super(new Properties()
                .stacksTo(16)
                .food(new FoodProperties.Builder()
                        .alwaysEat()
                .build()));
        TELEPORT_RADIUS = tpRadius;
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

        BlockPos newCoords = findSafeTeleportPosition(player, level, TELEPORT_RADIUS, 64);
        if (newCoords == null) {
            player.displayClientMessage(Component.translatable("info.warpedmod.potion_discord.failed_tp")
                    .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC), true);
            return stack;
        }


        player.teleportTo((ServerLevel) player.level(), newCoords.getX(), newCoords.getY() + 0.1, newCoords.getZ(), player.getXRot(), player.getYRot());

        AbstractMirror.trySpawnParticles(level, entity, ParticleTypes.END_ROD, 30, true);
        level.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.7f, 1.5f);

        if (!player.gameMode.getGameModeForPlayer().equals(GameType.CREATIVE)) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    // ------------------------------------------------------
    // Custom methods
    // ------------------------------------------------------

    public BlockPos findSafeTeleportPosition(ServerPlayer player, Level level, int radius, int attempts){

        RandomSource rand = player.getRandom();
        BlockPos playerPos = player.blockPosition();

        int minY;
        int maxY;

        ResourceKey<Level> d = level.dimension();
        if (d == Level.OVERWORLD) {
            minY = -60;
            maxY = 128;
        }
        else if (d == Level.NETHER) {
            minY = 8;
            maxY = 120;
        }
        else if (d == Level.END) {
            minY = 48;
            maxY = 80;
        }
        else {
            minY = 8;
            maxY = 80;
        }

        for (int i = 0; i < attempts; i++){
            int newX = playerPos.getX() + (rand.nextIntBetweenInclusive(-radius, radius));
            int newZ = playerPos.getZ() + (rand.nextIntBetweenInclusive(-radius, radius));

            int newY = playerPos.getY() + (rand.nextIntBetweenInclusive(-minY, maxY));

            BlockPos footPos = new BlockPos(newX, newY, newZ);

            BlockState below = level.getBlockState(footPos.below());
            BlockState foot = level.getBlockState(footPos);
            BlockState head = level.getBlockState(footPos.above());

            if (!head.isAir() || !foot.isAir()) continue;
            if (below.getFluidState() != null || !below.isSolid() ) continue;

            return footPos;

        }

        return null;

    }


}
