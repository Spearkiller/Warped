package net.spearkiller.warpedmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MirrorItem extends Item {

    public static final int USE_DURATION = 60;
    public static final float BLOCKS_PER_XP_LEVEL = 100;

    public static final DustParticleOptions MAGIC_MIRROR_DUST = new DustParticleOptions(
            Vec3.fromRGB24(0xB3CFEC).toVector3f(), 1.0f
    );

    public MirrorItem(Properties pProperties){
        super(pProperties);
    }

    public int getUseDuration(@NotNull ItemStack pStack){
        return USE_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) return stack;

        ServerLevel sLevel = (ServerLevel) player.level();

        BlockPos spawnPos = player.getRespawnPosition();
        float spawnAngle = player.getRespawnAngle();
        ResourceKey<Level> spawnDimension = player.getRespawnDimension();

        //Check that the player has set their spawnpoint at least once.
        if (spawnPos == null) {
            // Never set a spawn
            player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.missing_or_invalid_spawn_point")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
            return stack;
        }

        // If bed spawn is set, but is in a different dimension
        if (!spawnDimension.equals(sLevel.dimension())){
            player.displayClientMessage(Component.translatable( "info.warpedmod.mirrors.wrong_dimension").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
            return stack;
        }

        // Check the home bed is valid and unobstructed (basically, do we have somewhere we can teleport to?) - Part II
        Optional<Vec3> safeSpawn = ServerPlayer.findRespawnPositionAndUseSpawnBlock(
                sLevel,
                spawnPos,
                spawnAngle,
                false,
                true
        );
        if (safeSpawn.isEmpty()) {
            player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.missing_or_invalid_spawn_point").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
            return stack;
        }
        Vec3 trueSpawnPosition = safeSpawn.get();

        //Work out XP cost and check - Unless the player is in creative
        if (!player.gameMode.getGameModeForPlayer().equals(GameType.CREATIVE)) {
            double distanceFromSpawn = player.position().distanceToSqr(trueSpawnPosition);
            int levelCost = (int) Math.floor(distanceFromSpawn / Math.pow(BLOCKS_PER_XP_LEVEL, 2));
            if (player.experienceLevel < levelCost) {
                player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.insufficient_levels").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
                return stack;
            }
            player.giveExperienceLevels(-levelCost);
        }

        //Successful Teleport Time
        player.teleportTo(sLevel, trueSpawnPosition.x(), trueSpawnPosition.y() + 0.1, trueSpawnPosition.z(), spawnAngle, 0f);

        if (!level.isClientSide && player instanceof ServerPlayer) {
            ((ServerLevel) level).sendParticles( player, ParticleTypes.END_ROD, true, player.getX(), player.getY() + 1, player.getZ(),30, 0.5, 1, 0.5,0.1 );
            ((ServerLevel) level).sendParticles( player, MAGIC_MIRROR_DUST, true, player.getX(), player.getY() + 1, player.getZ(),60, 0.5, 1, 0.5,0.1 );
        }

        level.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.75f, 1.5f);

        player.getCooldowns().addCooldown(this, 40);
        return stack;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {

        //If this is running on the client, skip all this.
        if (!(entity instanceof ServerPlayer player) || !(level instanceof ServerLevel sLevel)) return;

        int elapsed = getUseDuration(stack) - remainingUseDuration;
        int particleCount = Math.min(elapsed / 5, 20);

        ((ServerLevel) level).sendParticles( MAGIC_MIRROR_DUST, player.getX(), player.getY() + 1, player.getZ(),particleCount, 0.5, 0.5, 0.5,0.1 );

        if (elapsed % 10 == 0 && remainingUseDuration > 0) {
            sLevel.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.75f, 2.0f);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.magic_mirror.tooltip").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.magic_mirror.tooltip_warnings").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
