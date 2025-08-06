package me.sub.expsolver.mixins;

import me.sub.expsolver.accessor.IEntityRendererAccessor;
import me.sub.expsolver.util.RenderUtil;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin implements IEntityRendererAccessor {
    @Inject(method = "renderWorldPass", at = @At("HEAD"))
    private void capturePartialTicks(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        RenderUtil.renderPartialTicks = partialTicks;
    }

    @Invoker("setupCameraTransform")
    @Override
    public abstract void invokeCameraTransform(float partialTicks, int pass);


}