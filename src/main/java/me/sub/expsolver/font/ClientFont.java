package me.sub.expsolver.font;

import me.sub.expsolver.ExpSolver;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public enum ClientFont {
    SFPRO("sfpro.ttf", 14),
    SANS("sans.ttf", 14),
    ROBOTO("roboto.ttf", 14),
    ROBOTO_SMALL("roboto.ttf", 12),
    FIRA_BIG("fira_bold.ttf", 13),
    FIRA_MEDIUM("fira_regular.ttf", 12),
    FIRA_SMALL("fira_regular.ttf", 10),
    FIRA_HUGE("fira_bold.ttf", 18);

    private final String fontName;
    private final int size;

    private TTFFontRenderer fontRenderer;
    public boolean initialized;


    ClientFont(String fontName, int size) {
        this.fontName = fontName;
        this.size = size;
    }


    public TTFFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public static void initializeFonts() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();

        for (ClientFont clientFont : ClientFont.values()) {
            try {
                InputStream is = ExpSolver.class.getResourceAsStream("/" + clientFont.fontName);
                Font myFont = Font.createFont(Font.PLAIN, is);
                myFont = myFont.deriveFont(Font.PLAIN, clientFont.size);
                clientFont.fontRenderer =  new TTFFontRenderer(executorService, textureQueue, myFont);
                clientFont.initialized = true;

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
            }
        }

    }
}
