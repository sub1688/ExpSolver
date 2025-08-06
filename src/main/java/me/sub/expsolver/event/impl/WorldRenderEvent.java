package me.sub.expsolver.event.impl;

import me.sub.expsolver.event.Event;

public class WorldRenderEvent extends Event {

    private final float partialTicks;

    public WorldRenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
