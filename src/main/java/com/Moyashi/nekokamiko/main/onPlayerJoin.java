package com.Moyashi.nekokamiko.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.Moyashi.nekokamiko.main.Nekokamiko.COLOR_BACKGROUND;

@Mod(Nekokamiko.MOD_ID)
public class onPlayerJoin {

    private static final int TICKS_PER_SECOND = 20;
    private static final int TICKS_PER_UPDATE = 2; // 0.5秒ごとに更新
    private static final double TICKS_PER_DAY = 24000.0;
    private static final double TICKS_PER_SECOND_CUSTOM = TICKS_PER_DAY / (24.0 * 60.0 * 60.0);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
    private static final DecimalFormat DF = new DecimalFormat("00");

    public static String formattedTime = "";
    private int tickCounter = 0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player == Minecraft.getInstance().player) {
            tickCounter++;
            if (tickCounter >= TICKS_PER_UPDATE) {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if (server != null) {
                    for (ServerWorld world : server.getAllLevels()) {
                        long worldTime = world.getDayTime();
                        long customTime = (long) ((worldTime % TICKS_PER_DAY) / TICKS_PER_SECOND_CUSTOM);
                         formattedTime = formatTime(customTime);
                        System.out.println(COLOR_BACKGROUND + "NowTime: " + formattedTime);
                    }
                }
                tickCounter = 0;
            }
        }
    }

    private String formatTime(long customTime) {
        long hours = (customTime / 3600) % 24; // 時間
        long minutes = (customTime % 3600) / 60; // 分
        long seconds = customTime % 60; // 秒

        DecimalFormat df = new DecimalFormat("00");

        if (hours == 0 && minutes == 0 && seconds == 0) {
            return "24/00/00";
        } else {
            return df.format(hours) + "/" + df.format(minutes) + "/" + df.format(seconds);
        }
    }

    private void updateConsoleDisplay() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            for (ServerWorld world : server.getAllLevels()) {
                long worldTime = world.getDayTime();
                long customTime = (long) ((worldTime % TICKS_PER_DAY) / TICKS_PER_SECOND_CUSTOM);
                 formattedTime = formatTime(customTime);
                System.out.println(COLOR_BACKGROUND + "NowTime: " + formattedTime);
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fontRenderer = mc.font;
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        int x = width / 2 - fontRenderer.width(formattedTime) / 2;
        int y = height / 2 - fontRenderer.lineHeight / 2;
        fontRenderer.drawShadow(event.getMatrixStack(), formattedTime, x, y, -1);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // プレイヤーがログインした際に疑似時刻を更新する
        formattedTime = SDF.format(new Date());
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new onPlayerJoin());
    }
}