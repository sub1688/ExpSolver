package me.sub.expsolver.ui.component.impl;

import me.sub.expsolver.font.TTFFontRenderer;
import me.sub.expsolver.ui.component.Component;
import me.sub.expsolver.ui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class Paragraph extends Component {

    private final float width;
    private final TTFFontRenderer fontRenderer;
    private final List<String> lines;

    public Paragraph(String paragraph, float width, TTFFontRenderer fontRenderer) {
        this.width = width;
        this.fontRenderer = fontRenderer;
        this.lines = wrapText(paragraph, width);
    }


    private List<String> wrapText(String text, float maxWidth) {
        List<String> wrappedLines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = (currentLine.length() == 0) ? word : currentLine + " " + word;
            if (fontRenderer.getWidth(testLine) <= maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                wrappedLines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            wrappedLines.add(currentLine.toString());
        }

        return wrappedLines;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        float currentY = y;
        for (String line : lines) {
            fontRenderer.drawString(line, x, currentY, Window.PROPERTIES_COLOR);
            currentY += fontRenderer.getHeight(line);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    public float getHeight() {
        float height = 0;
        for (String line : lines) {
            height += fontRenderer.getHeight(line);
        }

        return height;
    }
}
