package me.sub.expsolver.notification;

import com.google.common.eventbus.Subscribe;
import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.ScreenRenderEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage unused")
public class NotificationManager {

    public final LinkedList<Notification> notifications;

    public NotificationManager() {
        this.notifications = new LinkedList<>();
    }

    @Subscribe
    public void onRender(ScreenRenderEvent event) {
        LinkedList<Notification> l = new LinkedList<>(notifications);
        Collections.reverse(l);
        for (Notification n : l) {
            n.render();
        }
    }

    // TODO: Configurable delay
    public void addNotification(Notification.NotificationType type, String title, String text) {
        final Notification notification = new Notification(this, type, title, text);
        this.notifications.add(notification);
        ExpSolver.INSTANCE.scheduler.schedule(() -> {
            notification.kill();
            notifications.add(notifications.poll());
        }, 5000, TimeUnit.MILLISECONDS);
    }




}
