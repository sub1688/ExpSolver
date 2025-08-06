package me.sub.expsolver.util;

public class Stopwatch {

    private long startTime;

    public Stopwatch() {
        this.reset();
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean hasReached(long time) {
        return System.currentTimeMillis() - startTime > time;
    }

}
