package me.sub.expsolver.ui;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.ease.EaseAnimation;
import me.sub.expsolver.ease.Easing;
import me.sub.expsolver.module.setting.PrimitiveSetting;
import me.sub.expsolver.ui.window.Window;
import me.sub.expsolver.ui.window.impl.SettingsWindow;
import me.sub.expsolver.util.ColorContainer;
import me.sub.expsolver.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WindowGUI extends GuiScreen {

    private static final ResourceLocation CURSOR = new ResourceLocation("textures/cursor.png");
    public final CopyOnWriteArrayList<Window> windows;
    private final EaseAnimation backgroundAnimation;

    public WindowGUI() {
        this.windows = new CopyOnWriteArrayList<>();
        this.backgroundAnimation = new EaseAnimation(Easing.EASE_OUT_CUBIC, 300L);
        windows.add(new SettingsWindow(this));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        boolean increasing = ExpSolver.INSTANCE.guiOpened;
        ColorContainer colorContainer = new ColorContainer(0, 0, 0, backgroundAnimation.getValueFloat(increasing) * 0.3F);
        RenderUtil.drawRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), colorContainer.getColor().getRGB());


        if (mc.gameSettings.guiScale != 2) {
            GlStateManager.scale(2.0F / scaledResolution.getScaleFactor(), 2.0F / scaledResolution.getScaleFactor(), 0);
        }

        for (Window window : this.windows) {
            GlStateManager.pushMatrix();
            window.draw(mouseX, mouseY);
            GlStateManager.popMatrix();
        }


        drawCursor(scaledResolution);

        if (windows.isEmpty()) {
            ExpSolver.INSTANCE.guiOpened = false;
        }

    }

    private void drawCursor(ScaledResolution scaledResolution) {
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1.0F, -1000000.0F);
        float x = (float) Mouse.getX() / scaledResolution.getScaleFactor();
        float y = scaledResolution.getScaledHeight() - (float) Mouse.getY() / scaledResolution.getScaleFactor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtil.drawTexture(CURSOR, x, y, 42 / 4.0F, 42 / 4.0F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GlStateManager.popMatrix();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        List<Window> reversed = new ArrayList<>(windows);
        Collections.reverse(reversed);

        for (Window window : reversed) {
            if (window.mouseClicked(mouseX, mouseY, mouseButton)) {
                Window removedWindow = windows.remove(windows.indexOf(window));
                windows.add(removedWindow);
                return;
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        List<Window> reversed = new ArrayList<>(windows);
        Collections.reverse(reversed);

        for (Window window : reversed) {
            window.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (Window window : this.windows) {
            if (window.keyTyped(typedChar, keyCode)) {
                return;
            }
        }

        if (keyCode == 1) {
            windows.forEach(Window::close);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        Mouse.setGrabbed(true);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindForward));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindLeft));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindRight));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindBack));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindJump));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindSneak));
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), GameSettings.isKeyDown(mc.gameSettings.keyBindSprint));
    }

    public static void drawSeparator(float x, float y, float width) {
        RenderUtil.drawGradient(x, y, width / 2.0F, 0.5F, new int[]{
                0x00000000,
                SettingsWindow.SEPARATOR_COLOR,
                SettingsWindow.SEPARATOR_COLOR,
                0x00000000,
        });

        RenderUtil.drawGradient(x + width / 2.0F, y, width / 2.0F, 0.5F, new int[]{
                SettingsWindow.SEPARATOR_COLOR,
                0x00000000,
                0x00000000,
                SettingsWindow.SEPARATOR_COLOR,
        });

    }
}
