package me.sub.expsolver.util;

import me.sub.expsolver.accessor.IGuiChestAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public final class ContainerUtil {

    public static IInventory getLowerChestInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiChest)) {
            return null;
        }
        IGuiChestAccessor guiChestAccessor = (IGuiChestAccessor) mc.currentScreen;
        return guiChestAccessor.getLowerChestInventory();
    }

    public static int getSlotIndex(Predicate<ItemStack> predicate) {
        IInventory container = getLowerChestInventory();
        if (container == null) {
            return -1;
        }

        for (int slot = 0; slot < container.getSizeInventory(); slot++) {
            ItemStack stack = container.getStackInSlot(slot);
            if (stack != null && predicate.test(stack)) {
                return slot;
            }
        }
        return -1;
    }
}
