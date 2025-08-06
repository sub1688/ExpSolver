package me.sub.expsolver.event.impl;

import me.sub.expsolver.event.Event;

public class ScreenRenderEvent extends Event {

    private final int displayWidth, displayHeight;

    public ScreenRenderEvent(int displayWidth, int displayHeight) {
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }
}
