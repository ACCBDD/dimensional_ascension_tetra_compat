package com.accbdd.dimasctetracompat;

import com.accbdd.dimasctetracompat.effects.FairweatherEffect;
import com.accbdd.dimasctetracompat.effects.HorizonedEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //register effects
        MinecraftForge.EVENT_BUS.register(new HorizonedEffect());
        MinecraftForge.EVENT_BUS.register(new FairweatherEffect());
    }
}
