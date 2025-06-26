package net.spearkiller.warpedmod.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.spearkiller.warpedmod.WarpedMod;
import net.spearkiller.warpedmod.item.ModItems;

import java.util.List;

@Mod.EventBusSubscriber(modid = WarpedMod.MOD_ID)
public class TradeEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){

        if (event.getType() == VillagerProfession.CLERIC) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(5).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4),
                    new ItemStack(ModItems.POTION_RECALL_LESSER.get()),
                    1, 8, 0.02f
            )));
        }

    }

    @SubscribeEvent
    public static void addCustomWanderingTrades(WandererTradesEvent event){
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        rareTrades.add(((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 4),
                new ItemStack(ModItems.POTION_RECALL_LESSER.get()),
                1, 8, 0.02f
        )));

    }

}
