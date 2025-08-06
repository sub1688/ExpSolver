package me.sub.expsolver.ui.setting.impl;

import me.sub.expsolver.module.setting.IntSetting;
import me.sub.expsolver.ui.component.impl.Slider;
import me.sub.expsolver.ui.setting.GuiSetting;
import me.sub.expsolver.ui.window.Window;

public class IntegerSliderSetting  extends GuiSetting {

    private final IntSetting doubleSetting;
    private final Slider slider;

    public IntegerSliderSetting(IntSetting integerSetting, float width) {
        this.doubleSetting = integerSetting;
        this.slider = new Slider(integerSetting.getValue(), integerSetting.getRound(), integerSetting.getMin(), integerSetting.getMax(), width, 2.0F, value -> integerSetting.setValue(value.intValue()));
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        String settingName = doubleSetting.getName();
        slider.setPosition(x, y + fontRenderer.getHeight(settingName) + DoubleSliderSetting.TEXT_OFFSET);
        height = fontRenderer.getHeight(settingName) + DoubleSliderSetting.TEXT_OFFSET + slider.getHeight();
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
