package me.sub.expsolver.ui.component.impl;

import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.font.TTFFontRenderer;
import me.sub.expsolver.ui.component.Component;
import me.sub.expsolver.ui.window.Window;
import me.sub.expsolver.util.RenderUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.function.Consumer;

public class Slider extends Component {

    public static final int BAR_COLOR = new Color(0xFFFFFF).getRGB();
    private static final float SNAP_RANGE = 1.0F;
    private static final float CLICK_PADDING = 2.0F;
    private static final float LINE_HEIGHT = 4.0F;
    private static final float PADDING = 4.5F;

    private final TTFFontRenderer fontRenderer;

    private double value;
    private final double round;
    private final double minValue;
    private final double maxValue;
    private final float width;
    private final float height;
    private boolean sliding;
    private final Consumer<Double> changeAction;

    public Slider(double value, double round, double minValue, double maxValue, float width, float height, Consumer<Double> changeAction) {
        this.fontRenderer = ClientFont.FIRA_SMALL.getFontRenderer();
        this.value = value;
        this.round = round;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.width = width - PADDING * 2.0F;
        this.height = height;
        this.changeAction = changeAction;
    }


    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x + PADDING, y);
    }

    public void draw(int mouseX, int mouseY) {
        if (sliding) {
            updateValue(mouseX, true);
        }

        double difference = maxValue - minValue;
        double diffFromCurrent = Math.round(value / round) * round - minValue;
        float percentage =   (float) (diffFromCurrent / difference);

        RenderUtil.drawOutline(x, y, x + width, y + height, 0.5F, Checkmark.OUTLINE_COLOR);
        RenderUtil.drawRect(x, y, x + width, y + height, Window.MAIN_COLOR);
        RenderUtil.drawGradient(x, y, width * percentage, height, new int[] {

                new Color(0x939393).getRGB(),
                new Color(0x939393).getRGB(),
                BAR_COLOR,
                BAR_COLOR
        });

        RenderUtil.drawSelector(x + width * percentage - 3F, y - height, 6.5F, new Color(0x000000).getRGB());
        RenderUtil.drawSelector(x + width * percentage - 2F, y - height + 0.5F, 4.5F, BAR_COLOR);

        String valueText = String.valueOf(value);
        float valueWidth = fontRenderer.getWidth(valueText);
        float valueX = x + width * percentage - valueWidth / 2.0F + 0.5F;
        drawLines(valueX);
        fontRenderer.drawStringWithOutline(valueText, valueX, y + height, BAR_COLOR);
    }

    private void drawLines(float valueX) {
        double difference = maxValue - minValue;

        for (double i = minValue; i <= maxValue; i += round) {
            float lineX = (float) ((i - minValue) / difference * width + x);
            if (i == maxValue) {
                lineX -= 0.5F;
            }

            RenderUtil.drawGradient(lineX, y + height + 1.0F, 0.5F, LINE_HEIGHT, new int[]{
                    0x00000000,
                    0x00000000,
                    new Color(0x757575).getRGB(),
                    new Color(0x757575).getRGB(),
            });
        }


        String minValueText = String.valueOf(minValue);
        float minValueWidth = fontRenderer.getWidth(minValueText);
        float minValueX = x - minValueWidth / 2.0F;
        float minValueDiff = Math.min(1.0F, Math.abs(minValueX - valueX) / 20.0F);

        int minValueColor = new Color(1.0F, 1.0F, 1.0F, minValueDiff).getRGB();
        int minValueShadowColor = new Color(0.0F, 0.0F, 0.0F, minValueDiff).getRGB();
        fontRenderer.drawStringWithOutline(minValueText, minValueX, y + height, minValueColor, minValueShadowColor);


        String maxValueText = String.valueOf(maxValue);
        float maxValueWidth = fontRenderer.getWidth(maxValueText);
        float maxValueX = x + width - maxValueWidth / 2.0F;
        float maxValueDiff = Math.min(1.0F, Math.abs(maxValueX - valueX) / 20.0F);

        int maxValueColor = new Color(1.0F, 1.0F, 1.0F, maxValueDiff).getRGB();
        int maxValueShadowColor = new Color(0.0F, 0.0F, 0.0F, maxValueDiff).getRGB();
        fontRenderer.drawStringWithOutline(maxValueText, maxValueX, y + height, maxValueColor, maxValueShadowColor);
    }


    private void updateValue(int mouseX, boolean snap) {
        double difference = maxValue - minValue;
        double percentage = MathHelper.clamp_double((mouseX - x) / width, 0.0D, 1.0D);
        double closestValue = Math.round((percentage * difference + minValue) / round) * round;
        float closestX = (float) ((closestValue - minValue) / difference * width + x);

        if (Math.abs(mouseX - closestX) <= SNAP_RANGE || !snap) {
            value = closestValue;
            changeAction.accept(value);  // Notify any change listeners
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            updateValue(mouseX, false);
            sliding = true;
        } return false;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    public void mouseReleased(int mouseX) {
        if (sliding) {
            updateValue(mouseX, false);
            sliding = false;
        }
    }


    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y - CLICK_PADDING && mouseY <= y + height + CLICK_PADDING;
    }

    public float getHeight() {
        String maxValueText = String.valueOf(maxValue);
        return (y + height + fontRenderer.getHeight(maxValueText)) - y;
    }
}
