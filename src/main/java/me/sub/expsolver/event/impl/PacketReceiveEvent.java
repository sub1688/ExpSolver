package me.sub.expsolver.event.impl;

import me.sub.expsolver.event.Event;
import me.sub.expsolver.event.Phase;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends Event {

    private final Packet<?> packet;
    private final Phase phase;

    public PacketReceiveEvent(Packet<?> packet, Phase phase) {
        this.packet = packet;
        this.phase = phase;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public Phase getPhase() {
        return phase;
    }
}
