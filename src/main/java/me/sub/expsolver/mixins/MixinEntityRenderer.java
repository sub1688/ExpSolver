package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.WorldRenderEvent;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "renderHand", at=@At("HEAD"))
    public void onRenderHand(float partialTicks, int xOffset, CallbackInfo ci) {
        final WorldRenderEvent worldRenderEvent = new WorldRenderEvent(partialTicks);
        ExpSolver.INSTANCE.eventBus.post(worldRenderEvent);
    }

}
