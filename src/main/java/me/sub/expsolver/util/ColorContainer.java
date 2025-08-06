package me.sub.expsolver.util;

import java.awt.*;

public class ColorContainer {
    private float h;
    private float s;
    private float b;
    private float alpha;


    public ColorContainer(float h, float s, float b, float alpha) {
        this.h = Math.min(1.0F, Math.max(0.0F, h));
        this.s = Math.min(1.0F, Math.max(0.0F, s));
        this.b = Math.min(1.0F, Math.max(0.0F, b));
        this.alpha = Math.min(1.0F, Math.max(0.0F, alpha));
    }

    public ColorContainer(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.h = Math.min(1.0F, Math.max(0.0F, hsb[0]));
        this.s = Math.min(1.0F, Math.max(0.0F, hsb[1]));
        this.b = Math.min(1.0F, Math.max(0.0F, hsb[2]));
        this.alpha = (float) color.getAlpha() / 255.0F;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getS() {
        return s;
    }

    public void setS(float s) {
        this.s = s;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public void setHSB(float h, float s, float b) {
        this.h = Math.min(1.0F, Math.max(0.0F, h));
        this.s = Math.min(1.0F, Math.max(0.0F, s));
        this.b = Math.min(1.0F, Math.max(0.0F, b));
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Color getColor() {
        Color temp = Color.getHSBColor(h, s, b);
        return new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), (int) (alpha * 255));
    }

}
