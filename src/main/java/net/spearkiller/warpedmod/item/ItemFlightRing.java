package net.spearkiller.warpedmod.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.spearkiller.warpedmod.WarpedMod;

public class ItemFlightRing extends Item {

    public ItemFlightRing(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pSlotId < Inventory.getSelectionSize() && pEntity instanceof Player player) {
            if (!(player instanceof ServerPlayer sPlayer)) return;

            //If player is outside of a beacon range (use isPlayerInRange), remove flight here (probably?)
            //Maybe change this to a normal player tick.
            if (WarpedMod.BeaconFlightTracker.isPlayerInRange(sPlayer))
                player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }
}
