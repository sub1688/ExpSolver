package me.sub.expsolver.ui.setting.impl;

import me.sub.expsolver.module.setting.DoubleSetting;
import me.sub.expsolver.ui.component.impl.Slider;
import me.sub.expsolver.ui.setting.GuiSetting;
import me.sub.expsolver.ui.window.Window;

public class DoubleSliderSetting extends GuiSetting {

    public static final float TEXT_OFFSET = 3.0F;
    private final DoubleSetting doubleSetting;
    private final Slider slider;

    public DoubleSliderSetting(DoubleSetting doubleSetting, float width) {
        this.doubleSetting = doubleSetting;
        this.slider = new Slider(doubleSetting.getValue(), doubleSetting.getRound(), doubleSetting.getMin(), doubleSetting.getMax(), width, 2.0F, doubleSetting::setValue);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        String settingName = doubleSetting.getName();
        slider.setPosition(x, y + fontRenderer.getHeight(settingName) + TEXT_OFFSET);
        height = fontRenderer.getHeight(settingName) + TEXT_OFFSET + slider.getHeight();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        fontRenderer.drawString(doubleSetting.getName(), x, y, Window.PROPERTIES_COLOR);
        slider.draw(mouseX, mouseY);

    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return slider.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        slider.mouseReleased(mouseX);
    }

}
