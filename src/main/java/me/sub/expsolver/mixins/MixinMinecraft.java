package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.KeyEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = Minecraft.class, priority = 999)
@SuppressWarnings("UnstableApiUsage")
public class MixinMinecraft {

    @Shadow
    private Session session;//

    @Shadow
    public GuiScreen currentScreen;

    @Inject(method = "startGame", at = @At("TAIL"))
    private void startGame(CallbackInfo ci) {

    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    public void onKey(CallbackInfo ci) {
        if (Keyboard.getEventKeyState() && this.currentScreen == null)
            ExpSolver.INSTANCE.eventBus.post(new KeyEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));

    }


}
