package net.spearkiller.warpedmod;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.spearkiller.warpedmod.item.AbstractMirror;
import net.spearkiller.warpedmod.item.ModCreativeTabs;
import net.spearkiller.warpedmod.item.ModItems;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WarpedMod.MOD_ID)
public class WarpedMod
{
    public static final String MOD_ID = "warpedmod";
    private static final Logger LOGGER = LogUtils.getLogger();


    //Add the gamerules here I guess?
    public static final  GameRules.Key<GameRules.IntegerValue> MAGIC_MIRROR_BLOCKS_PER_LEVEL_COST =
            GameRules.register("magicMirrorBlocksPerLevel", GameRules.Category.PLAYER, GameRules.IntegerValue.create(250));
    public static final  GameRules.Key<GameRules.IntegerValue> MAGIC_MIRROR_MAX_LEVEL_COST =
            GameRules.register("magicMirrorMaxLevelCost", GameRules.Category.PLAYER, GameRules.IntegerValue.create(5));
    public static final  GameRules.Key<GameRules.IntegerValue> CELESTIAL_MIRROR_BLOCKS_PER_LEVEL_COST =
            GameRules.register("celestialMirrorBlocksPerLevelCost", GameRules.Category.PLAYER, GameRules.IntegerValue.create(500));
    public static final  GameRules.Key<GameRules.IntegerValue> CELESTIAL_MIRROR_MAX_LEVEL_COST =
            GameRules.register("celestialMirrorMaxLevelCost", GameRules.Category.PLAYER, GameRules.IntegerValue.create(3));



    public WarpedMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeTabs.register(modEventBus);

        ModItems.register(modEventBus);



        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        WarpedMod.getLogger().info("WarpedMod loaded!");

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.MAGIC_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.CELESTIAL_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.ABYSSAL_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.FLIGHT_RING);

        addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_RECALL_LESSER);
        addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_RECALL_GREATER);
        //addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_DISCORD_LESSER);
        //addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_DISCORD_GREATER);

    }

    private void addItemToCreativeTab(BuildCreativeModeTabContentsEvent event, ResourceKey<CreativeModeTab> tab, RegistryObject<Item> item) {
        if(event.getTabKey() == tab) event.accept(item);
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerModEvents
    {
        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer player)) return;

            ItemStack itemInUse = player.getUseItem();
            if (!(itemInUse.getItem() instanceof AbstractMirror)) return;

            // Stop the mirror use
            player.stopUsingItem();
            player.displayClientMessage(Component.translatable("info.warpedmod.mirrors.use_cancelled_by_damage")
                    .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC), true);
        }
    }

    public static Logger getLogger(){
        return LOGGER;
    }

    public class BeaconFlightTracker {
        public static final Map<UUID, Integer> playersInBeaconRange = new HashMap<>();
        public static final int GRACE_PERIOD_TICKS = 100;

        public static boolean isPlayerInRange(ServerPlayer player) {
            Integer lastSeen = playersInBeaconRange.get(player.getUUID());
            return lastSeen != null && player.tickCount - lastSeen < GRACE_PERIOD_TICKS;
        }
    }

    public static String getPlural(int amount, String singular, String plural){
        if (amount == 1) return singular;
        return plural;
    }
}
