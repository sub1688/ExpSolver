package me.sub.expsolver.ease;

public class EaseAnimation {

    private final Easing ease;
    public final long duration;
    public long progress;
    public long lastTime;

    public EaseAnimation(Easing ease, long duration) {
        this.ease = ease;
        this.duration = duration;
        this.progress = 0;
        this.lastTime = System.currentTimeMillis();
    }

    public EaseAnimation(Easing ease, long progress, long duration) {
        this.ease = ease;
        this.duration = duration;
        this.progress = progress;
        this.lastTime = System.currentTimeMillis();
    }

    public float getValueFloat(boolean increasing) {
        return (float) this.getValue(increasing);
    }

    public double getValue(boolean increasing) {
        long milis = increasing ? (System.currentTimeMillis() - lastTime) : -(System.currentTimeMillis() - lastTime);
        float value = (float) (progress = Math.max(Math.min(progress + milis, duration), 0)) / duration;
        this.lastTime = System.currentTimeMillis();
        return this.ease.ease(value);
    }

    public void reset() {
        this.lastTime = System.currentTimeMillis();
        this.progress = 0;
    }

    public boolean isAtStart() {
        return this.progress == 0;
    }

    public boolean isAtFinish() {
        return this.progress == duration;
    }

    public long getProgress() {
        return progress;
    }
}
