package me.sub.expsolver.event;

import com.google.common.eventbus.EventBus;

@SuppressWarnings("UnstableApiUsage")
public class ExpSolverEventBus extends EventBus {
    public ExpSolverEventBus() {
        super((exception, context) -> {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        });
    }
}
