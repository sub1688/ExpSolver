package me.sub.expsolver.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.Phase;
import me.sub.expsolver.event.impl.PacketReceiveEvent;
import me.sub.expsolver.event.impl.PacketSendEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onChannelRead0(ChannelHandlerContext context, Packet packet, CallbackInfo ci) {
        PacketReceiveEvent event = new PacketReceiveEvent(packet, Phase.PRE);
        ExpSolver.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet packetIn, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(packetIn, Phase.PRE);
        ExpSolver.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
