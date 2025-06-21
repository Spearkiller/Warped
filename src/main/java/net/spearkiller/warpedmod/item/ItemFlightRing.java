package net.spearkiller.warpedmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.spearkiller.warpedmod.WarpedMod;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Optional;

public class ItemFlightRing extends Item implements ICurioItem {

    public ItemFlightRing(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("ring");
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.item_flight_ring.info").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.item_flight_ring.info2").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }



    public static void updateFlightStatus(ServerPlayer player){

        //WarpedMod.getLogger().debug("Checking " + player + " for beacon flight");

        boolean hasRing = isEquipped(player);
        //WarpedMod.getLogger().debug("Equipped ring: " + hasRing);
        boolean inBeaconRange = WarpedMod.BeaconFlightTracker.isPlayerInRange(player);
        //WarpedMod.getLogger().debug("In range: " + inBeaconRange);

        boolean shouldHaveFlight = hasRing && inBeaconRange;

        if (shouldHaveFlight) {
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                //WarpedMod.getLogger().debug(player + " is permitted for takeoff");
                player.onUpdateAbilities();
            }
            WarpedMod.BeaconFlightTracker.flightEnabledPlayers.add(player.getUUID());

        } else {
            //WarpedMod.getLogger().debug(player + " should be prevented from flying.");

            //If they were removed from the set then at some point we gave them flight
            if (WarpedMod.BeaconFlightTracker.flightEnabledPlayers.remove(player.getUUID())) {

                if (!(player.isCreative() || player.isSpectator())) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }
        }
    }

    public static boolean isEquipped(ServerPlayer player){
        Optional<ICuriosItemHandler> curios = CuriosApi.getCuriosInventory(player).resolve();
        //Yes this can be simplified but the simplified version of the code is scawy
        if (curios.isEmpty()) return false;

        return curios.get().findFirstCurio(stack -> stack.getItem() instanceof ItemFlightRing).isPresent();
    }
}
