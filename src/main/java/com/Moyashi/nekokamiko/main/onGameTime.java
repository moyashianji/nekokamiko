package com.Moyashi.nekokamiko.main;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = "nekokamiko")
public class onGameTime {
    private static boolean stopwatchRunning = false;
    private static long startTime = 0;

    private static long pausedTime = 0;

    private static long elapsedTime = 0;

    public static long decrementValue = 200;

    public static long pausedValue = 0;

    public static long displayedValue = 0;

    public static long decrementAmount = 0;

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        event.getServer().getCommands().getDispatcher().register(
                Commands.literal("neko")
                        .then(Commands.literal("time")
                                .then(Commands.literal("reset")
                                        .executes(context -> startStopwatch(context.getSource())
                                        )
                                )
                                .then(Commands.literal("stop")
                                        .executes(context -> pauseStopwatch(context.getSource())
                                        )
                                )
                                .then(Commands.literal("start")
                                        .executes(contect -> resumeStopwatch(contect.getSource())
                                        )
                                )
                                .then(Commands.literal("set")
                                        .then(Commands.argument("hours", IntegerArgumentType.integer())
                                                .then(Commands.argument("minutes",IntegerArgumentType.integer())
                                                        .then(Commands.argument("seconds", IntegerArgumentType.integer())
                                                                .executes(context -> setStartTime
                                                                        (context.getSource(), IntegerArgumentType.getInteger
                                                                                (context, "hours"), IntegerArgumentType.getInteger
                                                                                (context, "minutes"), IntegerArgumentType.getInteger
                                                                                (context, "seconds"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
        MinecraftForge.EVENT_BUS.register(onGameTime.class);
    }

    private static int startStopwatch(CommandSource source) {
        PlayerEntity player = source.getEntity() instanceof PlayerEntity ? (PlayerEntity) source.getEntity() : null;
        if (player != null) {
            if (!stopwatchRunning) {

                startTime = System.currentTimeMillis();
                stopwatchRunning = true;
                player.sendMessage(new StringTextComponent("Stopwatch started."), player.getUUID());
            } else {
                player.sendMessage(new StringTextComponent("Stopwatch is already running."), player.getUUID());
            }
        }
        return 0;
    }
    private static int pauseStopwatch(CommandSource source){
        //三項演算子　条件式 ? 真の場合の値　:　偽りの場合の値
        PlayerEntity player = source.getEntity() instanceof  PlayerEntity ? (PlayerEntity) source.getEntity() : null;
        if(player != null){
            if(stopwatchRunning){
                pausedTime = elapsedTime;
                pausedValue = displayedValue;
                stopwatchRunning = false;
                player.sendMessage(new StringTextComponent("Stopwatch paused"), player.getUUID());
            }else{
                player.sendMessage(new StringTextComponent("Already paused"), player.getUUID());
            }
        }
        return 0;
    }

    public static int resumeStopwatch(CommandSource source){

        PlayerEntity player = source.getEntity() instanceof PlayerEntity ? (PlayerEntity) source.getEntity() : null;
        if(player != null){
            if(!stopwatchRunning && pausedTime>0){

                startTime = System.currentTimeMillis() - pausedTime;
                pausedTime = 0;
                stopwatchRunning = true;
                player.sendMessage(new StringTextComponent("Stopwatch resume"), player.getUUID());
            }else{
                player.sendMessage(new StringTextComponent("/neko time stop してから実行してください"), player.getUUID());

            }
        }
        return 0;
    }

    private static int setStartTime(CommandSource source, int hours, int minutes, int seconds){
        PlayerEntity player = source.getEntity() instanceof PlayerEntity ? (PlayerEntity) source.getEntity():null;
        if(player != null){
            if(!stopwatchRunning){
                long currentTime = System.currentTimeMillis();
                long specifiedTime = currentTime - (hours * 3600000L) - (minutes * 60000L) - (seconds * 1000L);
                startTime = specifiedTime;
                stopwatchRunning = true;
                player.sendMessage(new StringTextComponent("Stopwatch start time set to: " + hours + ":" + minutes + ":" + seconds), player.getUUID());
            } else {
                player.sendMessage(new StringTextComponent("Stopwatch is already running."), player.getUUID());
            }
        }return 0;
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

            Minecraft mc = Minecraft.getInstance();
            FontRenderer fontRenderer = mc.font;

            if(stopwatchRunning){
                elapsedTime = System.currentTimeMillis() - startTime;

                 decrementAmount = (elapsedTime/1000) * decrementValue;
                 displayedValue = 0L + decrementAmount;
            }

            if (stopwatchRunning || pausedTime > 0) {
                long timeToShow = stopwatchRunning ? elapsedTime : pausedTime;
                long valueToShow = stopwatchRunning ? displayedValue : pausedValue;

                int seconds = (int) (timeToShow / 1000) % 60;
                int minutes = (int) ((timeToShow / (1000 * 60)) % 60);
                int hours = (int) ((timeToShow / (1000 * 60 * 60)) % 24);


                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                String valueString = String.format("%d",valueToShow);

                int scaledWidth = mc.getWindow().getGuiScaledWidth();
                int scaledHeight = mc.getWindow().getGuiScaledHeight();

                int textWidth = fontRenderer.width(timeString);
                int textHeight = fontRenderer.lineHeight;

                int x = scaledWidth - textWidth - 100;
                int y = scaledHeight - textHeight - 100;

                int valueWidth = fontRenderer.width(valueString);
                int valueHeigh = fontRenderer.lineHeight;

                int xv = scaledWidth - valueWidth - 50;
                int yv = scaledHeight - valueHeigh - 50;


                String all = timeString +"\u501f\u91d1" + valueString;
                MatrixStack matrixStack = event.getMatrixStack();
                matrixStack.pushPose();

                RenderSystem.enableBlend();

                matrixStack.translate(x, y, 0);

                fontRenderer.draw(matrixStack, all, 0, 0, -1);

                matrixStack.popPose();
            }
        }
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(onGameTime.class);
    }
}