package me.sub.expsolver.mixins;

import me.sub.expsolver.accessor.IKeybindingAccessor;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class MixinKeybinding implements IKeybindingAccessor {

    @Shadow
    private boolean pressed;


    @Override
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
