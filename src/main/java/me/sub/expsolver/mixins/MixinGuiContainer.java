package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.InventoryRenderEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {

    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V", ordinal = 1))
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        InventoryRenderEvent inventoryRenderEvent = new InventoryRenderEvent();
        ExpSolver.INSTANCE.eventBus.post(inventoryRenderEvent);
    }
}