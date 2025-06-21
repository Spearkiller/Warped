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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export=true)
@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {


    @Inject(method = "tick", at = @At("TAIL"))
    private static void warpedmod$trackPlayersNearby(Level pLevel, BlockPos pPos, BlockState pState, BeaconBlockEntity pBlockEntity, CallbackInfo ci) {

        WarpedMod.getLogger().info("Ticking beacon " + pBlockEntity.toString());
        System.out.println("Ticking");

        int beaconLevel = warped$getBeaconLevel(pBlockEntity);
        if (beaconLevel <= 0) return;

        int range = 10 + (beaconLevel*10);
        for (Player player : pLevel.players()) {
            if (player.blockPosition().closerThan(pPos, range)) {

                //If player has a flight ring, grant them flight
                WarpedMod.BeaconFlightTracker.playersInBeaconRange.put(player.getUUID(), player.tickCount);
                WarpedMod.getLogger().info(player + " is in range.");
            }
            else {
                WarpedMod.getLogger().info("Checked if " + player + " was in range. Was not. Sucks to suck.");
            }
        }
    }

    @Unique
    private static int warped$getBeaconLevel(BeaconBlockEntity b){
        try{
            ContainerData cd = ((BeaconBlockEntityAccessor) b).warpedmod$getDataAccess();
            return cd.get(0);

        } catch (Exception e){
            WarpedMod.getLogger().error("An error has occurred when trying to access container data of beacon " + b.toString());
            WarpedMod.getLogger().error(e.getMessage());
            return -1;
        }
    }

}
