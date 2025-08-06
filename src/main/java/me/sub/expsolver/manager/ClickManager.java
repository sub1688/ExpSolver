package me.sub.expsolver.manager;

import com.google.common.eventbus.Subscribe;
import me.sub.expsolver.event.impl.InventoryRenderEvent;
import me.sub.expsolver.event.impl.ScreenRenderEvent;
import me.sub.expsolver.util.ColorContainer;
import me.sub.expsolver.util.ContainerUtil;
import me.sub.expsolver.util.RenderUtil;
import me.sub.expsolver.util.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Mouse;

import java.util.LinkedList;

@SuppressWarnings("UnstableApiUsage unused")
public class ClickManager {

    private final Minecraft mc;
    private final LinkedList<Click> clicks;
    private final Stopwatch stopwatch;

    public ClickManager() {
        this.mc = Minecraft.getMinecraft();
        this.clicks = new LinkedList<>();
        this.stopwatch = new Stopwatch();
    }

    @Subscribe
    public void onRender(ScreenRenderEvent event) {
        Container container = mc.thePlayer.openContainer;
        IInventory inventory = ContainerUtil.getLowerChestInventory();
        clicks.removeIf(click -> click.windowId != mc.thePlayer.openContainer.windowId || !withinBounds(click, inventory));

        if (inventory == null || clicks.isEmpty()) {
            if (Mouse.isGrabbed() && inventory != null) {
                Mouse.setGrabbed(false);
            }

            clicks.clear();
            stopwatch.reset();
            return;
        }


        Click nextClick = clicks.peek();
        if (stopwatch.hasReached(nextClick.delay)) {
            clicks.removeFirst();
            mc.playerController.windowClick(container.windowId, nextClick.slot, nextClick.mouseButton, nextClick.clickType, mc.thePlayer);
            if (nextClick.runnable != null) {
                nextClick.runnable.run();
            }
            stopwatch.reset();
        }


    }
    @Subscribe
    public void onInventoryRender(InventoryRenderEvent event) {
        if (!clicks.isEmpty()) {
            Mouse.setGrabbed(true);
        }

        for (int index = 0; index < clicks.size(); index++) {
            Click click = clicks.get(index);
            Slot slot = mc.thePlayer.openContainer.inventorySlots.get(click.slot);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            float hue = 100.0F - (float) index / (float) clicks.size() * 100.0F;
            int color = new ColorContainer(hue / 360.0F,1.0F,1.0F, 0.3F).getColor().getRGB();

            RenderUtil.drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, color);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }

    public void scheduleClick(Click click) {
        clicks.add(click);
    }

    public void scheduleClick(int slot, int button, int mode, int windowId, long delay) {
        clicks.add(new Click(slot, button, mode, windowId, delay));
    }

    public void scheduleClick(int slot, int button, int mode, int windowId, long delay, Runnable postClickAction) {
        clicks.add(new Click(slot, button, mode, windowId, delay, postClickAction));
    }

    private boolean withinBounds(Click click, IInventory inventory) {
        return inventory == null || click.slot <= inventory.getSizeInventory() && click.slot >= 0;
    }

    public static class Click {
        private final int slot;
        private final int mouseButton;
        private final int clickType;
        private final int windowId;
        private final long delay;
        private final Runnable runnable;

        public Click(int slot, int mouseButton, int clickType, int windowId, long delay, Runnable runnable) {
            this.slot = slot;
            this.mouseButton = mouseButton;
            this.clickType = clickType;
            this.windowId = windowId;
            this.delay = delay + (long) (Math.random() * 50.0D);
            this.runnable = runnable;
        }

        public Click(int slot, int mouseButton, int clickType, int windowId, long delay) {
            this.slot = slot;
            this.mouseButton = mouseButton;
            this.clickType = clickType;
            this.windowId = windowId;
            this.delay = delay + (long) (Math.random() * 50.0D);
            this.runnable = null;
        }

    }
}
