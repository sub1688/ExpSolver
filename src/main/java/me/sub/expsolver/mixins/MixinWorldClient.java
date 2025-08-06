package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.WorldTickEvent;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        ExpSolver.INSTANCE.eventBus.post(new WorldTickEvent());
    }

}
