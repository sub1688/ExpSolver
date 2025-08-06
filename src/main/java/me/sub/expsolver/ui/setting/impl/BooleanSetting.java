package me.sub.expsolver.ui.setting.impl;

import me.sub.expsolver.module.setting.PrimitiveSetting;
import me.sub.expsolver.ui.component.impl.Checkmark;
import me.sub.expsolver.ui.setting.GuiSetting;
import me.sub.expsolver.ui.window.Window;

public class BooleanSetting extends GuiSetting {

    private final Checkmark checkmark;
    private final PrimitiveSetting<Boolean> setting;

    public BooleanSetting(PrimitiveSetting<Boolean> setting) {
        this.setting = setting;
        this.checkmark = new Checkmark(setting.getValue(), setting::setValue);
        this.height = Checkmark.HEIGHT;
    }


    @Override
    public void draw(int mouseX, int mouseY) {
        checkmark.draw(mouseX, mouseY);
        fontRenderer.drawString(setting.getName(), x + Checkmark.WIDTH + Window.CHECKMARK_SPACING, y + Checkmark.HEIGHT / 2.0F - 2.25F, Window.PROPERTIES_COLOR);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (checkmark.mouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        checkmark.setPosition(x, y);
    }
}
