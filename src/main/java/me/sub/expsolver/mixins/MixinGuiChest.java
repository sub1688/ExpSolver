package me.sub.expsolver.mixins;

import me.sub.expsolver.accessor.IGuiChestAccessor;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiChest.class)
public class MixinGuiChest implements IGuiChestAccessor {

    @Shadow
    private IInventory lowerChestInventory;

    @Override
    public IInventory getLowerChestInventory() {
        return lowerChestInventory;
    }
}
