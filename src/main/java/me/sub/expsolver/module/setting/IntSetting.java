package me.sub.expsolver.module.setting;


import net.minecraft.util.MathHelper;

public class IntSetting extends PrimitiveSetting<Integer> {

    private final int min, max;
    private final int round;

    public IntSetting(String name, String description, int value, int min, int max, int round) {
        super(name, description, value);

        this.min = min;
        this.max = max;
        this.round = round;
    }

    public IntSetting(String name, String description, int value, int min, int max, int round, PrimitiveSetting<Boolean> parentSetting) {
        super(name, description, value, parentSetting);

        this.min = min;
        this.max = max;
        this.round = round;
    }

    @Override
    public Integer getValue() {
        int rounded = Math.round((float) super.getValue() / round) * round;
        return MathHelper.clamp_int(rounded, min, max);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getRound() {
        return round;
    }

    @Override
    public void setValue(Integer value) {
        int rounded = Math.round((float) value / round) * round;
        super.setValue(MathHelper.clamp_int(rounded, min, max));
    }
}
