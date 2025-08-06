package me.sub.expsolver.ui.component;

public abstract class Component {
    protected float x;
    protected float y;

    public abstract void draw(int mouseX, int mouseY);

    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract float getWidth();

    public abstract boolean keyTyped(char typedChar, int keyCode);


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
