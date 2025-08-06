package me.sub.expsolver.module.setting;

public class PrimitiveSetting<T> {

    private final String name, description;
    private PrimitiveSetting<Boolean> parentSetting;
    private T value;

    public PrimitiveSetting(String name, String description, T value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public PrimitiveSetting(String name, String description, T value, PrimitiveSetting<Boolean> parentSetting) {
        this(name, description, value);
        this.setParentSetting(parentSetting);
    }

    public boolean hasParent() {
        return this.parentSetting != null;
    }

    public PrimitiveSetting<Boolean> getParentSetting() {
        return parentSetting;
    }

    public void setParentSetting(PrimitiveSetting<Boolean> parentSetting) {
        this.parentSetting = parentSetting;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
