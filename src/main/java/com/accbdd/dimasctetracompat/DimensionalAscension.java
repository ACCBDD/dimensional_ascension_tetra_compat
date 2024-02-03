package com.accbdd.dimasctetracompat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DimensionalAscension.MODID)
public class DimensionalAscension
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dimasctetracompat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public DimensionalAscension()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //register config
        ModLoadingContext.get().registerConfig(Type.COMMON, DimensionalAscensionConfig.COMMON_CONFIG);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
