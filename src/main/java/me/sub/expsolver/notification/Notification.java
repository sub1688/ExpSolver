package me.sub.expsolver.notification;

import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.font.TTFFontRenderer;
import me.sub.expsolver.ui.window.Window;
import me.sub.expsolver.ui.window.impl.SettingsWindow;
import me.sub.expsolver.ui.component.impl.Checkmark;
import me.sub.expsolver.ui.component.impl.Slider;
import me.sub.expsolver.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {

    private static final float WIDTH = 170.0F;
    private static final float HEIGHT = 30.0F;
    private static final float PADDING = 5.0F;
    private static final float MARGIN = 10.0F;

    private static final float TEXT_MARGIN = 3.0F;


    public enum NotificationType {
        INFO, ERROR
    }

    private final TTFFontRenderer titleFont;
    private final TTFFontRenderer descriptionFont;
    private final NotificationManager manager;
    private final NotificationType type;
    private final String title, text;
    private float smoothedY;
    private boolean killed;
    private long lastNano;

    public Notification(NotificationManager manager, NotificationType type, String title, String text) {
        this.titleFont = ClientFont.FIRA_HUGE.getFontRenderer();
        this.descriptionFont = ClientFont.SFPRO.getFontRenderer();
        this.manager = manager;
        this.type = type;
        this.title = title;
        this.text = text;
        this.lastNano = System.nanoTime();
    }

    public void render() {
        final float deltaTime = (System.nanoTime() - lastNano) / 1e6F;
        final float smoothingFactor = 64.F / deltaTime;

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final int displayWidth = sr.getScaledWidth();

        final int index = manager.notifications.indexOf(this);
        final float targetY = MARGIN + (MARGIN + HEIGHT) * index;
        this.lastNano = System.nanoTime();

        if (smoothedY == 0.F) {
            smoothedY = MARGIN + (MARGIN + HEIGHT) * (index - 1);
        }

        if (killed) {
            final float diff = (-(MARGIN + HEIGHT) - this.smoothedY);
            this.smoothedY += diff / smoothingFactor;
            if (Math.abs(diff) < 0.1F)
                manager.notifications.remove(index);
        } else {
            this.smoothedY += (targetY - smoothedY) / smoothingFactor;
        }

        float x = displayWidth / 2.0F - WIDTH / 2.0F;
        float y = smoothedY;
        RenderUtil.drawOutline(x, y, x + WIDTH, y + HEIGHT, 0.5F, Checkmark.OUTLINE_COLOR);
        RenderUtil.drawRect(x, y, x + WIDTH, y + HEIGHT, Window.MAIN_COLOR);
        renderText(x + PADDING, y + PADDING);


    }


    private void renderText(float x, float y) {
        titleFont.drawString(title, x, y, Slider.BAR_COLOR);

        float titleHeight = titleFont.getHeight(title);
        y += titleHeight + TEXT_MARGIN / 2.0F;


        RenderUtil.drawGradient(x, y, WIDTH, 0.5F, new int[] {
                SettingsWindow.SEPARATOR_COLOR,
                0x00000000,
                0x00000000,
                SettingsWindow.SEPARATOR_COLOR,
        });

        y += TEXT_MARGIN;

        descriptionFont.drawString(text, x, y, Slider.BAR_COLOR);
    }

    public void kill() {
        killed = true;
    }


}
