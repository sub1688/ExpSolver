package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.PlayerPostUpdateEvent;
import me.sub.expsolver.event.impl.PlayerUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
@SuppressWarnings("UnstableApiUsage")
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onUpdateWalkingPlayer(CallbackInfo ci) {
        final PlayerUpdateEvent event = new PlayerUpdateEvent(0, 0, 0, 0, 0, false);
        ExpSolver.INSTANCE.eventBus.post(event);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("TAIL"))
    public void onUpdateWalkingPlayerPost(CallbackInfo ci) {
        final PlayerPostUpdateEvent event = new PlayerPostUpdateEvent();
        ExpSolver.INSTANCE.eventBus.post(event);
    }



}
