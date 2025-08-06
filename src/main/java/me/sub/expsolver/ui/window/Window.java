package me.sub.expsolver.ui.window;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.ease.EaseAnimation;
import me.sub.expsolver.ease.Easing;
import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.font.TTFFontRenderer;
import me.sub.expsolver.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

// TODO: list of components
public class Window {

    public static final int BACKGROUND_COLOR = new Color(0x0A0A0A).getRGB();
    public static final float TITLE_OFFSET = 2.0F;
    private static final float DRAG_HEIGHT = 10.0F;
    public static final float CHECKMARK_SPACING = 3.0F;
    public static final int PROPERTIES_COLOR = new Color(0x646464).getRGB();
    public static final int MAIN_COLOR = new Color(0x0F0F0F).getRGB();
    public static final int OUTLINE_COLOR = new Color(0x1E1E1E).getRGB();
    public static final int TEXT_COLOR = new Color(0x7E7E7E).getRGB();

    private final String title;
    private final float titleBarHeight;

    protected float width;
    protected float height;
    public float x;
    public float y;

    private final Consumer<Window> closeAction;
    private final TTFFontRenderer titleFont;
    private float[] clickPos;
    private boolean dragging;
    public final EaseAnimation animation;
    public boolean expanding;
    private final CloseButton closeButton;

    public Window(String title, float width, float height, float titleBarHeight, @Nullable Consumer<Window> closeAction) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.titleBarHeight = titleBarHeight;
        this.closeAction = closeAction;
        this.titleFont = ClientFont.SFPRO.getFontRenderer();

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float randomOffset = (float) (Math.random() * 100.0F);
        this.x = scaledResolution.getScaledWidth() / 2.0F - width / 2.0F + randomOffset;
        this.y = scaledResolution.getScaledHeight() / 2.0F - height / 2.0F + randomOffset;

        this.animation = new EaseAnimation(Easing.EASE_OUT_BACK, 300L);
        this.expanding = true;

        this.closeButton = new CloseButton(5.5F);
    }

    public void draw(int mouseX, int mouseY) {
        float animationValue = animation.getValueFloat(expanding);

        GlStateManager.translate(x + width / 2f, y + height / 2f, 0);
        GlStateManager.scale(animationValue, animationValue, 0);
        GlStateManager.translate(-x + width / -2f, -y + height / -2f, 0);

        updatePos(mouseX, mouseY);
        drawOutline(x, y - titleBarHeight, width, height + titleBarHeight);
        RenderUtil.drawRect(x, y, x + width, y + height, BACKGROUND_COLOR);
        drawTitleBar(mouseX, mouseY, x, y - titleBarHeight);

        if (!expanding && animation.isAtStart()) {
            Minecraft.getMinecraft().setIngameFocus();

            if (ExpSolver.INSTANCE.guiOpened && closeAction != null) {
                closeAction.accept(this);
            }
        }

    }

    private void drawTitleBar(int mouseX, int mouseY, float x, float y) {
        if (titleBarHeight == 0) {
            return;
        }

        RenderUtil.drawRect(x, y, x + width, y + titleBarHeight, BACKGROUND_COLOR);
        titleFont.drawString(title.toLowerCase(), x + TITLE_OFFSET, y + TITLE_OFFSET, Window.TEXT_COLOR);
        RenderUtil.drawRect(x, y + titleBarHeight - 0.5F, x + width, y + titleBarHeight, Window.OUTLINE_COLOR);

        closeButton.setPosition(x + width - closeButton.size - TITLE_OFFSET, y + titleBarHeight / 2.0F - 0.5F - closeButton.size / 2.0F);
        closeButton.draw(mouseX, mouseY);

    }


    private void drawOutline(float x, float y, float width, float height) {
        RenderUtil.drawOutline(x, y, x + width, y + height, 3.0F, new Color(0x0D0D15).getRGB());
        RenderUtil.drawOutline(x, y, x + width, y + height, 2.5F, new Color(0x323232).getRGB());
        RenderUtil.drawOutline(x, y, x + width, y + height, 2.0F, new Color(0x24242C).getRGB());
        RenderUtil.drawOutline(x, y, x + width, y + height, 1.5F, new Color(0x1B1B1B).getRGB());
        RenderUtil.drawOutline(x, y, x + width, y + height, 1.0F, new Color(0x262626).getRGB());
        RenderUtil.drawOutline(x, y, x + width, y + height, 0.5F, new Color(0x0A0A0A).getRGB());
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (closeButton.click(mouseX, mouseY)) {
            return true;
        }

        if (mouseX > x && mouseX < x + width) {
            if (mouseY < y && mouseY > y - DRAG_HEIGHT) {
                this.clickPos = new float[]{mouseX - x, mouseY - y};
                this.dragging = true;
                return true;
            }
        }

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            return true;
        }

        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    protected void updatePos(int mouseX, int mouseY) {
        if (this.dragging) {
            this.x = mouseX - clickPos[0];
            this.y = mouseY - clickPos[1];
        }
    }

    public void close() {
        expanding = false;
    }

    private class CloseButton {
        private static final float CLICK_PADDING = 2.0f;
        private float x;
        private float y;
        private final float size;
        private final EaseAnimation hoverAnimation;

        private CloseButton(float size) {
            this.size = size;
            this.hoverAnimation = new EaseAnimation(Easing.LINEAR, 100L);
        }

        public void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
        }

        protected void draw(int mouseX, int mouseY) {
            boolean hovered = isHovered(mouseX, mouseY) || (ExpSolver.INSTANCE.guiOpened && !expanding);
            float hoverValue = hoverAnimation.getValueFloat(hovered);

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, Math.min(1.0F, 0.5F + hoverValue));
            GL11.glLineWidth(1.0F + hoverValue * 1.5F);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            worldrenderer.pos(x, y, 0).endVertex();
            worldrenderer.pos(x + size, y + size, 0).endVertex();
            tessellator.draw();

            worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            worldrenderer.pos(x, y + size, 0).endVertex();
            worldrenderer.pos(x + size, y, 0).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.popMatrix();
        }

        protected boolean click(int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                close();
                return true;
            }
            return false;
        }

        public boolean isHovered(int mouseX, int mouseY) {
            return mouseX >= x - CLICK_PADDING && mouseX <= x + size + CLICK_PADDING &&
                    mouseY >= y - CLICK_PADDING && mouseY <= y + size + CLICK_PADDING;
        }
    }

}
