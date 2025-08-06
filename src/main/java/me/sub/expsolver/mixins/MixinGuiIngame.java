package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.ScreenRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Shadow @Final protected Minecraft mc;

    @Inject(method = "renderTooltip", at=@At("TAIL"))
    public void onScreenRender(CallbackInfo ci) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final ScreenRenderEvent event = new ScreenRenderEvent(sr.getScaledWidth(), sr.getScaledHeight());
        ExpSolver.INSTANCE.eventBus.post(event);
    }

}
