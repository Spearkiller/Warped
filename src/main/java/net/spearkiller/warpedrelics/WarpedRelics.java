package net.spearkiller.warpedrelics;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(WarpedRelics.MODID)
public class WarpedRelics {
    public static final String MODID = "warpedrelics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WarpedRelics(IEventBus modEventBus, ModContainer modContainer) {
    }
}
