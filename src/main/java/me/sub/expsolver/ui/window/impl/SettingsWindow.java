package me.sub.expsolver.ui.window.impl;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.module.impl.ExperimentationSolver;
import me.sub.expsolver.module.setting.*;
import me.sub.expsolver.ui.component.impl.Paragraph;
import me.sub.expsolver.ui.window.Window;
import me.sub.expsolver.ui.WindowGUI;
import me.sub.expsolver.ui.setting.GuiSetting;
import me.sub.expsolver.ui.setting.impl.*;
import me.sub.expsolver.util.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsWindow extends Window {

    public static final int SEPARATOR_COLOR = new Color(0x37FFFFFF, true).getRGB();
    private static final float SETTING_PADDING = 5.0F;
    private static final float SETTING_MARGIN = 7.0F;

    private final List<GuiSetting> settings;
    private final Paragraph paragraph;

    public SettingsWindow(WindowGUI windowGUI) {
        super("Experimentation Table Solver", 100.0F, 120.0F, 10.5F, windowGUI.windows::remove);
        this.settings = new ArrayList<>();
        this.paragraph = new Paragraph("Solves experiments on the experimentation table automatically", width - SETTING_PADDING * 2.0F, ClientFont.ROBOTO_SMALL.getFontRenderer());

        loadSettings();
    }

    @SuppressWarnings("all")
    private void loadSettings() {
        float paddedWidth = width - SETTING_PADDING * 2.0F;
        for (PrimitiveSetting<?> primitiveSetting : ExpSolver.INSTANCE.solverModule.getSettings()) {
            Object value = primitiveSetting.getValue();
            if (value instanceof Boolean) {
                settings.add(new BooleanSetting((PrimitiveSetting<Boolean>) primitiveSetting));
            } else if (value instanceof Double) {
                settings.add(new DoubleSliderSetting((DoubleSetting) primitiveSetting, paddedWidth));
            } else if (value instanceof Integer) {
                settings.add(new IntegerSliderSetting((IntSetting) primitiveSetting, paddedWidth));
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        RenderUtil.drawRect(x, y, x + width, y + height, Window.MAIN_COLOR);



        float paddedX = x + SETTING_PADDING;
        float paddedY = y + SETTING_PADDING;

        paragraph.setPosition(paddedX, paddedY);
        paragraph.draw(mouseX, mouseY);

        paddedY += paragraph.getHeight() + SETTING_MARGIN;
        drawSeparator(x, paddedY);

        for (GuiSetting guiSetting : settings) {
            guiSetting.setPosition(paddedX, paddedY);
            guiSetting.draw(mouseX, mouseY);
            paddedY += guiSetting.height + SETTING_MARGIN;
            drawSeparator(x, paddedY);
        }
        this.height = paddedY - y;

    }

    private void drawSeparator(float paddedX, float paddedY) {
        float paddedWidth = width;
        paddedY -= SETTING_PADDING / 2.0F + 0.5F;
        WindowGUI.drawSeparator(paddedX, paddedY, paddedWidth);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (GuiSetting guiSetting : settings) {
            if (guiSetting.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        settings.forEach(guiSetting -> guiSetting.mouseReleased(mouseX, mouseY, state));
        super.mouseReleased(mouseX, mouseY, state);
    }
}
