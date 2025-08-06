package me.sub.expsolver.ui.setting;

import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.font.TTFFontRenderer;

public abstract class GuiSetting {

    protected final TTFFontRenderer fontRenderer = ClientFont.ROBOTO_SMALL.getFontRenderer();
    public float x;
    public float y;
    public float height;

    public abstract void draw(int mouseX, int mouseY);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);
    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
