package net.spearkiller.warpedrelics;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.spearkiller.warpedrelics.effects.ModEffects;
import net.spearkiller.warpedrelics.events.ModBrewingRecipes;
import net.spearkiller.warpedrelics.item.ModCreativeTabs;
import net.spearkiller.warpedrelics.item.ModItems;
import net.spearkiller.warpedrelics.loot.ModLootModifier;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import java.util.*;

@Mod(WarpedRelics.MODID)
public class WarpedRelics {
    public static final String MODID = "warpedrelics";
    public static final Logger LOGGER = LogUtils.getLogger();


    //Add the gamerules here I guess?
    public static final  GameRules.Key<GameRules.IntegerValue> MAGIC_MIRROR_BLOCKS_PER_LEVEL_COST =
            GameRules.register("warpedMagicMirrorBlocksPerLevel", GameRules.Category.PLAYER, GameRules.IntegerValue.create(500));
    public static final  GameRules.Key<GameRules.IntegerValue> MAGIC_MIRROR_MAX_LEVEL_COST =
            GameRules.register("warpedMagicMirrorMaxLevelCost", GameRules.Category.PLAYER, GameRules.IntegerValue.create(5));
    public static final  GameRules.Key<GameRules.IntegerValue> MAGIC_MIRROR_WARP_SICKNESS_DURATION =
            GameRules.register("warpedMagicMirrorWarpSicknessDuration", GameRules.Category.PLAYER, GameRules.IntegerValue.create(900));


    public static final  GameRules.Key<GameRules.IntegerValue> CELESTIAL_MIRROR_BLOCKS_PER_LEVEL_COST =
            GameRules.register("warpedCelestialMirrorBlocksPerLevelCost", GameRules.Category.PLAYER, GameRules.IntegerValue.create(1000));
    public static final  GameRules.Key<GameRules.IntegerValue> CELESTIAL_MIRROR_MAX_LEVEL_COST =
            GameRules.register("warpedCelestialMirrorMaxLevelCost", GameRules.Category.PLAYER, GameRules.IntegerValue.create(3));
    public static final  GameRules.Key<GameRules.IntegerValue> CELESTIAL_MIRROR_WARP_SICKNESS_DURATION =
            GameRules.register("warpedCelestialMirrorWarpSicknessDuration", GameRules.Category.PLAYER, GameRules.IntegerValue.create(600));

    public static final  GameRules.Key<GameRules.IntegerValue> ABYSSAL_MIRROR_WARP_SICKNESS_DURATION =
            GameRules.register("warpedAbyssalMirrorWarpSicknessDuration", GameRules.Category.PLAYER, GameRules.IntegerValue.create(300));

    public static final  GameRules.Key<GameRules.IntegerValue> RECALL_POTION_WARP_SICKNESS_DURATION =
            GameRules.register("warpedRecallPotionWarpSicknessDuration", GameRules.Category.PLAYER, GameRules.IntegerValue.create(900));

    public static final  GameRules.Key<GameRules.IntegerValue> FLIGHT_TOTEM_RANGE_MULT =
            GameRules.register("warpedFlightRingRangeMult", GameRules.Category.PLAYER, GameRules.IntegerValue.create(100));
    public static final  GameRules.Key<GameRules.BooleanValue> REUNION_POTION_FUZZY_TEAMS =
            GameRules.register("warpedReunionPotionIncludesNonTeamedPlayers", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
    public static final  GameRules.Key<GameRules.BooleanValue> REUNION_POTION_IGNORE_TEAMS =
            GameRules.register("warpedReunionPotionIgnoreTeams", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

    public WarpedRelics(IEventBus modEventBus, ModContainer modContainer) {

        ModCreativeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModEffects.register(modEventBus);

        ModLootModifier.register(modEventBus);

        ModBrewingRecipes.POTIONS.register(modEventBus);

        //modEventBus.addListener(this::commonSetup);

        //MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        //context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.MAGIC_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.CELESTIAL_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.ABYSSAL_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.TOOLS_AND_UTILITIES, ModItems.TOTEM_OF_ASCENSION);

        addItemToCreativeTab(event, CreativeModeTabs.INGREDIENTS, ModItems.TARNISHED_MIRROR);
        addItemToCreativeTab(event, CreativeModeTabs.INGREDIENTS, ModItems.WITHERED_TOTEM);

        addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_RECALL_LESSER);
        addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_RECALL_GREATER);
        addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_REUNION_LESSER);
        addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_REUNION_GREATER);
        //addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_DISCORD_LESSER);
        //addItemToCreativeTab(event, CreativeModeTabs.FOOD_AND_DRINKS, ModItems.POTION_DISCORD_GREATER);

    }

    private void addItemToCreativeTab(BuildCreativeModeTabContentsEvent event, ResourceKey<CreativeModeTab> tab, DeferredItem item) {
        if(event.getTabKey() == tab) event.accept(item);
    }

    @EventBusSubscriber(modid = WarpedRelics.MODID)
    public static class ServerModEvents{

        @SubscribeEvent
        public static void onIncomingDamage(LivingIncomingDamageEvent event){

            LivingEntity entity = event.getEntity();

            if (event.getEntity().level().isClientSide()) return;
            if (!event.getSource().is(DamageTypeTags.IS_FALL)) return;

            if (entity.hasEffect(ModEffects.FALL_BREAK)){

                entity.removeEffect(ModEffects.FALL_BREAK);
                event.setAmount(0);
                event.setCanceled(true);

            }
        }

        @SubscribeEvent
        public static void onEffectExpired(MobEffectEvent.Expired event) {
            LivingEntity entity = event.getEntity();
            if (event.getEffectInstance().is(ModEffects.ASCENSION)) {
                WarpedRelics.addFallBreak(entity, event.getEffectInstance());
            }
        }

        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {
            LivingEntity entity = event.getEntity();
            if (event.getEffectInstance().is(ModEffects.ASCENSION)) {
                WarpedRelics.addFallBreak(entity, event.getEffectInstance());
            }
        }
    }

    private static void addFallBreak(LivingEntity entity, MobEffectInstance instance){
        if (!entity.onGround()) {
            entity.addEffect(new MobEffectInstance(
                    ModEffects.FALL_BREAK, -1, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon()
            ));
        }
    }

    public static String getPlural(int amount, String singular, String plural){
        if (amount == 1) return singular;
        return plural;
    }

    public static class BeaconFlightTracker {
        public static final Map<UUID, Integer> playersInBeaconRange = new HashMap<>();
        public static final Set<UUID> flightEnabledPlayers = new HashSet<>();
        public static final int GRACE_PERIOD_TICKS = 85; //Beacons check every 4 seconds.

        public static boolean isPlayerInRange(ServerPlayer player) {
            Integer lastSeen = playersInBeaconRange.get(player.getUUID());
            return lastSeen != null && player.tickCount - lastSeen < GRACE_PERIOD_TICKS;
        }
    }
}
