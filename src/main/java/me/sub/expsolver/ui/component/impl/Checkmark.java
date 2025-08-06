package me.sub.expsolver.ui.component.impl;

import me.sub.expsolver.ui.component.Component;
import me.sub.expsolver.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.function.Consumer;

public class Checkmark extends Component {

    public static final int BACKGROUND_COLOR = new Color(0x0C0C0C).getRGB();

    private static final ResourceLocation CHECKMARK = new ResourceLocation( "textures/dot.png");
    public static final int OUTLINE_COLOR = new Color(0x262626).getRGB();
    private static final int HOVER_OVERLAY = new Color(0x1A707070, true).getRGB();
    public static final float WIDTH = 5.5F;
    public static final float HEIGHT = 5.5F;

    private boolean toggle;
    private final Consumer<Boolean> toggleAction;


    public Checkmark(boolean toggle, Consumer<Boolean> toggleAction) {
        this.toggle = toggle;
        this.toggleAction = toggleAction;
    }

    public void draw(int mouseX, int mouseY) {
        RenderUtil.drawOutline(x, y, x + WIDTH, y + HEIGHT, 0.5F, OUTLINE_COLOR);
        RenderUtil.drawRect(x, y, x + WIDTH, y + HEIGHT, BACKGROUND_COLOR);
        RenderUtil.drawGradient(x, y, WIDTH, HEIGHT, new int[] {
                new Color(0x191919).getRGB(),
                new Color(0x191919).getRGB(),
                0x00000000,
                new Color(0x111111).getRGB()
        });



        if (isHovered(mouseX,mouseY)) {
            RenderUtil.drawOutline(x, y, x + WIDTH, y + HEIGHT, 0.5F, HOVER_OVERLAY);
            if (Mouse.isButtonDown(0)) {
                RenderUtil.drawOutline(x, y, x + WIDTH, y + HEIGHT, 0.5F, HOVER_OVERLAY);
            }
        }



         if (toggle) {
             GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
             RenderUtil.drawTexture(CHECKMARK, x, y, WIDTH, HEIGHT);
         }
    }


    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            this.toggle = !toggle;
            toggleAction.accept(toggle);
            return true;
        }

        return false;
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + WIDTH &&
                mouseY >= y && mouseY <= y + HEIGHT;
    }

}
