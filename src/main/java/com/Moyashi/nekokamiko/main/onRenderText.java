package com.Moyashi.nekokamiko.main;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mod("nekokamiko")
public class onRenderText {

    // 疑似時間のフォーマット
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    // 疑似時間を保持する変数
    private static String formattedTime = "";

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;

            int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
            int scaledHeight = minecraft.getWindow().getGuiScaledHeight();

            int textWidth = fontRenderer.width(formattedTime);
            int textHeight = fontRenderer.lineHeight;

            int x = scaledWidth - textWidth - 20;
            int y = scaledHeight - textHeight - 20;

            MatrixStack matrixStack = event.getMatrixStack();
            matrixStack.pushPose();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);

            // 画像の表示コードを追加
            ResourceLocation imageTexture = new ResourceLocation("nekokamiko", "textures/gui/touka.png");
            int imageWidth = 115;
            int imageHeight = 55;
            int imageX = scaledWidth - imageWidth - 5; // 右端から画像の幅とオフセット分だけ左にずらす
            int imageY = 5; // 画面の上端からのオフセット

            minecraft.getTextureManager().bind(imageTexture);
            AbstractGui.blit(matrixStack, imageX, imageY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
            RenderSystem.disableBlend();

            // テキストの描画
            matrixStack.translate(x, y, 0);
            fontRenderer.draw(matrixStack, formattedTime, 0, 0, -1);

            matrixStack.popPose();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // プレイヤーがログインした際に疑似時刻を更新する
        formattedTime = sdf.format(new Date());
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(onRenderText.class);
    }
}
