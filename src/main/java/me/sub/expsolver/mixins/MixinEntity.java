package me.sub.expsolver.mixins;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.MoveFlyingEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnstableApiUsage"})
@Mixin(Entity.class)
public class MixinEntity {


    @Inject(method = "moveFlying", at = @At("HEAD"), cancellable = true)
    private void onMoveFlying(float strafe, float forward, float friction, CallbackInfo ci) {
        Entity entity = (Entity)(Object) this;

        MoveFlyingEvent event = new MoveFlyingEvent(strafe, forward, friction, entity.rotationYaw);
        ExpSolver.INSTANCE.eventBus.post(event);

        // Store modified values in local variables
        float modifiedStrafe = event.getStrafe();
        float modifiedForward = event.getForward();
        float modifiedFriction = event.getFriction();
        entity.rotationYaw = event.getRotationYaw();

        // Calculate movement using modified values
        float f = modifiedStrafe * modifiedStrafe + modifiedForward * modifiedForward;

        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0F) {
                f = 1.0F;
            }

            f = modifiedFriction / f;
            modifiedStrafe *= f;
            modifiedForward *= f;

            float f1 = MathHelper.sin(entity.rotationYaw * (float)Math.PI / 180.0F);
            float f2 = MathHelper.cos(entity.rotationYaw * (float)Math.PI / 180.0F);

            entity.motionX += (double)(modifiedStrafe * f2 - modifiedForward * f1);
            entity.motionZ += (double)(modifiedForward * f2 + modifiedStrafe * f1);
        }

        // Cancel the original method since we're handling the movement
        ci.cancel();
    }



}
