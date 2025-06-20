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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import java.util.logging.Logger;

public abstract class AbstractMirror extends Item {

    public static final int USE_DURATION = 60;
    public final float BLOCKS_PER_XP_LEVEL;
    public final boolean ALLOW_CROSS_DIMENSIONAL_TRAVEL;
    public final ParticleOptions DUST_TYPE;
    public final int MAX_COST;

    public AbstractMirror(Properties pProperties, int blocksPerLevel, int max_cost, boolean allowCrossDimensions, ParticleOptions dust){
        super(pProperties);
        BLOCKS_PER_XP_LEVEL = blocksPerLevel;
        ALLOW_CROSS_DIMENSIONAL_TRAVEL = allowCrossDimensions;
        DUST_TYPE = dust;
        MAX_COST = max_cost;
    }

    // ------------------------------------------------------
    // Methods from Item Class
    // ------------------------------------------------------

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

        float spawnAngle = player.getRespawnAngle();
        ResourceKey<Level> spawnDimension = player.getRespawnDimension();

        //Check the player can travel cross-dimensionally
        Vec3 trueSpawnPosition = tryUse(sLevel, player);
        if (trueSpawnPosition == null) {
            return stack;
        }

        //Successful Teleport Time :>

        // Get the player's spawn dimension. Yes we have it already if their bed is in the same dimension as them but still
        ServerLevel targetDimension = player.getServer().getLevel(spawnDimension);
        player.teleportTo(targetDimension, trueSpawnPosition.x(), trueSpawnPosition.y() + 0.1, trueSpawnPosition.z(), spawnAngle, 0f);

        trySpawnParticles(level, entity, DUST_TYPE, 60, true);
        trySpawnParticles(level, entity, ParticleTypes.END_ROD, 30, true);

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

        trySpawnParticles(level, entity, DUST_TYPE, particleCount, false);

        //this could probably be changed to be client-side only. Whaddaya gonna do?
        if (elapsed % 10 == 0 && remainingUseDuration > 0) {
            sLevel.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.75f, 2.0f);
        }
    }

    // ------------------------------------------------------
    // Methods unique to AbstractMirror
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

        Logger.getLogger("warpedmod").info("Player spawn pos: " + spawnPos);
        Logger.getLogger("warpedmod").info("Spawn dimension: " + spawnDimension);
        Logger.getLogger("warpedmod").info("Current dimension: " + sLevel.dimension());

        //Check that the player has set their spawnpoint at least once.
        if (spawnPos == null) {
            // Never set a spawn
            player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.missing_or_invalid_spawn_point")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
            return null;
        }

        // If spawn is in a different dimension
        if (!spawnDimension.equals(sLevel.dimension()) && !ALLOW_CROSS_DIMENSIONAL_TRAVEL){
            player.displayClientMessage(Component.translatable( "info.warpedmod.mirrors.wrong_dimension").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
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
            player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.missing_or_invalid_spawn_point").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
            return null;
        }
        Vec3 trueSpawnPosition = safeSpawn.get();

        //Work out XP cost and check - Unless the player is in creative
        if (!player.gameMode.getGameModeForPlayer().equals(GameType.CREATIVE) && BLOCKS_PER_XP_LEVEL > 0) {
            int levelCost = 0;

            //Check for cross-dimensional travel
            if (!spawnDimension.equals(sLevel.dimension()) && ALLOW_CROSS_DIMENSIONAL_TRAVEL) {
                levelCost = MAX_COST;
            }

            else {
                double distanceFromSpawn = player.position().distanceToSqr(trueSpawnPosition);
                levelCost = (int) Math.min(Math.floor(distanceFromSpawn / Math.pow(BLOCKS_PER_XP_LEVEL, 2)), MAX_COST);
                if (player.experienceLevel < levelCost) {
                    player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.insufficient_levels").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
                    return null;
                }
            }

            player.giveExperienceLevels(-levelCost);
        }

        return trueSpawnPosition;
    }

    protected final void trySpawnParticles(Level level, LivingEntity entity, ParticleOptions particles, int particleCount, boolean force){
        if (!(entity instanceof ServerPlayer player) || !(level instanceof ServerLevel sLevel)) return;

        sLevel.sendParticles( player, particles, force,player.getX(), player.getY() + 1, player.getZ(),particleCount, 0.5, 0.5, 0.5,0.1 );
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    };
}
