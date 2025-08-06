package me.sub.expsolver.module.setting;

import net.minecraft.util.MathHelper;

public class DoubleSetting extends PrimitiveSetting<Double> {

    private final double min, max;
    private final double round;

    public DoubleSetting(String name, String description, double value, double min, double max, double round) {
        super(name, description, value);

        this.min = min;
        this.max = max;
        this.round = round;
    }

    public DoubleSetting(String name, String description, double value, double min, double max, double round, PrimitiveSetting<Boolean> parentSetting) {
        super(name, description, value, parentSetting);

        this.min = min;
        this.max = max;
        this.round = round;
    }

    @Override
    public Double getValue() {
        return Math.round(MathHelper.clamp_double(super.getValue(), min, max) / round) * round;
    }

    public float getValueFloat() {
        return (float) (Math.round(MathHelper.clamp_double(super.getValue(), min, max) / round) * round);
    }


    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getRound() {
        return round;
    }

    @Override
    public void setValue(Double value) {
        super.setValue(Math.round(MathHelper.clamp_double(value, min, max) / round) * round);
    }
}
