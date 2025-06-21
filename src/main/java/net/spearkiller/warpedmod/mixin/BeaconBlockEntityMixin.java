package net.spearkiller.warpedmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.spearkiller.warpedmod.WarpedMod;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export=true)
@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {


    @Inject(method = "tick", at = @At("TAIL"))
    private static void warpedmod$trackPlayersNearby(Level pLevel, BlockPos pPos, BlockState pState, BeaconBlockEntity pBlockEntity, CallbackInfo ci) {

        if (pLevel.isClientSide()) return;

        int beaconLevel = 0;
        //WarpedMod.getLogger().info("Ticking beacon " + pBlockEntity.toString());
        if (pBlockEntity instanceof BeaconBlockEntityAccessor accessor) {
            ContainerData data = accessor.warpedmod$getDataAccess();
            beaconLevel = data.get(0);
        }

        if (beaconLevel <= 0) return;

        double rangeMod = (float)Math.max(pLevel.getGameRules().getRule(WarpedMod.FLIGHT_TOTEM_RANGE_MULT).get(), 0) /100;

        int range = (int)((10 + (beaconLevel*10)) * rangeMod);
        //WarpedMod.getLogger().debug("Flight range: " + range);

        for (Player player : pLevel.players()) {

            //Check for distance on the flat.
            float xDist = player.blockPosition().getX() - pPos.getX();
            float zDist = player.blockPosition().getZ() - pPos.getZ();

            float dist = (xDist * xDist) + (zDist * zDist);

            if (dist <= (range*range)) {

                //If player has a flight ring, grant them flight
                WarpedMod.BeaconFlightTracker.playersInBeaconRange.put(player.getUUID(), player.tickCount);


                //WarpedMod.getLogger().info(player + " is in range.");
            }
            else {
                //WarpedMod.getLogger().info("Checked if " + player + " was in range. Was not. Sucks to suck.");
            }
        }
    }
}
