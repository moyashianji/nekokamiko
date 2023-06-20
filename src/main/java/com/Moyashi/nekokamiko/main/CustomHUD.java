package com.Moyashi.nekokamiko.main;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.java.games.input.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class CustomHUD {

    private static final ResourceLocation IMAGE_TEXTURE = new ResourceLocation("nekokamiko", "textures/gui/realms/test.png");
    private static final int IMAGE_WIDTH = 64;
    private static final int IMAGE_HEIGHT = 64;

    public void render(MatrixStack matrixStack, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        minecraft.getTextureManager().bind(IMAGE_TEXTURE);

        int screenWidth = minecraft.getWindow().getWidth();
        int screenHeight = minecraft.getWindow().getHeight();

        int imageX = (screenWidth - IMAGE_WIDTH) / 2; // 画像のX座標を中央に配置
        int imageY = (screenHeight - IMAGE_HEIGHT) / 2; // 画像のY座標を中央に配置

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); // カラーモードをリセット

        AbstractGui.blit(matrixStack, imageX, imageY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        RenderSystem.disableBlend();
    }
}