package net.spearkiller.warpedmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.spearkiller.warpedmod.WarpedMod;
import net.spearkiller.warpedmod.effects.ModEffects;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;

public class ItemTotemOfAscension extends Item implements ICurioItem {

    public ItemTotemOfAscension(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("charm");
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.item_totem_of_ascension.info").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        pTooltipComponents.add(Component.translatable("tooltip.warpedmod.item_totem_of_ascension.info2").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }



    public static void updateFlightStatus(ServerPlayer player){

        //WarpedMod.getLogger().debug("Checking " + player + " for beacon flight");

        boolean hasTotem = isEquipped(player);
        //WarpedMod.getLogger().debug("Equipped ring: " + hasRing);
        boolean inBeaconRange = WarpedMod.BeaconFlightTracker.isPlayerInRange(player);
        //WarpedMod.getLogger().debug("In range: " + inBeaconRange);

        boolean shouldHaveFlight = hasTotem && inBeaconRange;

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
                    //WarpedMod.getLogger().debug("Removing flight from " + player.getUUID());
                    player.addEffect(new MobEffectInstance(ModEffects.FALL_BREAK.get(), -1));
                }
            }
        }
    }

    public static boolean isEquipped(ServerPlayer player){
        Optional<ICuriosItemHandler> curios = CuriosApi.getCuriosInventory(player).resolve();

        if (curios.isEmpty()) return false;

        if (curios.get().findFirstCurio(stack -> stack.getItem() instanceof ItemTotemOfAscension).isPresent())
            return true;

        if (player.getOffhandItem().getItem() instanceof  ItemTotemOfAscension)
            return true;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ItemTotemOfAscension)
                return true;
        }

        return false;
    }
}
