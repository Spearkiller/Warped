package net.spearkiller.warped;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(Warped.MODID)
public class Warped {
    public static final String MODID = "warped";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Warped(IEventBus modEventBus, ModContainer modContainer) {
    }
}
