package com.Moyashi.nekokamiko.main;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.Moyashi.nekokamiko.config.ConfigFile.generateConfigFile;
import static com.Moyashi.nekokamiko.main.Nekokamiko.MOD_ID;
import static com.Moyashi.nekokamiko.time.inGameTime.printTime;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class Nekokamiko {
    // Directly reference a log4j logger.

    public static final String MOD_ID = "nekokamiko";
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String COLOR_BACKGROUND = "\u001B[40m";

    public Nekokamiko() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);


        onRenderText.register();
        onPlayerJoin.register();
        onGameTime.register();

        generateConfigFile();
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("siuroooo");

    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event){
        printTime();
        System.out.println("[NEKORUN]プレイヤーがワールドに入りました");

    }
    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event){

        System.out.println("[NEKORUN]サーバーが起動しました");
        generateConfigFile();
        printTime();
    }
    private void setup(final FMLCommonSetupEvent event) {
    }



}
