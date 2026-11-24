package net.spearkiller.warpedrelics.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import net.spearkiller.warpedrelics.item.ItemTotemOfAscension;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Debug(export=true)
@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    //We have to 'borrow' this code to keep the effect area the same.
    //At some point, I might make it so that this is instead done by it's own block, a la Conduits.
    @Inject(method = "applyEffects", at = @At("TAIL"))
    private static void applyEffects(Level level, BlockPos pos, int beaconLevel, Holder<MobEffect> primaryEffect, Holder<MobEffect> secondaryEffect, CallbackInfo ci) {
        if (!level.isClientSide) {
            double d0 = (double)(beaconLevel * 10 + 10);

            int j = (9 + beaconLevel * 2) * 20;
            AABB aabb = (new AABB(pos)).inflate(d0).expandTowards((double)0.0F, (double)level.getHeight(), (double)0.0F);
            List<Player> playersInRange = level.getEntitiesOfClass(Player.class, aabb);

            for (Player p: playersInRange){
                //Warped.BeaconFlightTracker.playersInBeaconRange.put(p.getUUID(), p.tickCount);
                if (p instanceof ServerPlayer sp) ItemTotemOfAscension.updateFlightStatus(sp, j);
            }
        }
    }
}
