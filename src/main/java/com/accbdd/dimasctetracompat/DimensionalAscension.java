package com.accbdd.dimasctetracompat;

import com.accbdd.dimasctetracompat.loot.HasBlockEntity;
import com.accbdd.dimasctetracompat.loot.HasScorching;
import com.accbdd.dimasctetracompat.loot.modifier.ScorchingLootModifier;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DimensionalAscension.MODID)
public class DimensionalAscension
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dimasctetracompat";
    
    //loot modifier for autosmelt
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SCORCHING_MODIFIER = LOOT_MODIFIERS.register("scorching", () -> ScorchingLootModifier.CODEC);

    //loot condition for safer autosmelt (don't smelt block entities)
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registry.LOOT_CONDITION_TYPE.key(), MODID);
    public static final RegistryObject<LootItemConditionType> HAS_BLOCK_ENTITY = LOOT_CONDITION_TYPES.register("has_block_entity", () -> new LootItemConditionType(new HasBlockEntity.Serializer()));
    public static final RegistryObject<LootItemConditionType> HAS_SCORCHING = LOOT_CONDITION_TYPES.register("has_scorching", () -> new LootItemConditionType(new HasScorching.Serializer()));

    public DimensionalAscension()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //register config
        ModLoadingContext.get().registerConfig(Type.COMMON, DimensionalAscensionConfig.COMMON_CONFIG);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //register loot modifier
        LOOT_MODIFIERS.register(modEventBus);

        //register loot conditions
        LOOT_CONDITION_TYPES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
        }
    }
}
