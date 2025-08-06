package me.sub.expsolver.util;

import java.awt.*;

public final class ColorUtil {

    /**
     * Override opacity of color
     * @param opacity Range: 0.0 - 1.0
     */
    public static int overrideOpacity(int color, float opacity) {
        ColorContainer colorContainer = new ColorContainer(new Color(color));
        colorContainer.setAlpha(opacity);
        return colorContainer.getColor().getRGB();
    }

}
