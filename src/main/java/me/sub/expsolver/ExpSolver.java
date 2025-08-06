package me.sub.expsolver;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import me.sub.expsolver.command.GuiCommand;
import me.sub.expsolver.event.ExpSolverEventBus;
import me.sub.expsolver.event.impl.WorldTickEvent;
import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.manager.ClickManager;
import me.sub.expsolver.module.impl.ExperimentationSolver;
import me.sub.expsolver.notification.NotificationManager;
import me.sub.expsolver.ui.WindowGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("UnstableApiUsage")
public enum ExpSolver {

    INSTANCE;

    public final ScheduledExecutorService scheduler;
    public final NotificationManager notificationManager;
    public final ClickManager clickManager;
    public final EventBus eventBus;

    public boolean guiOpened;

    public final ExperimentationSolver solverModule;

    ExpSolver() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.eventBus = new ExpSolverEventBus();
        this.notificationManager = new NotificationManager();
        this.clickManager = new ClickManager();

        eventBus.register(this);
        eventBus.register(notificationManager);
        eventBus.register(clickManager);

        this.solverModule = new ExperimentationSolver();

        eventBus.register(this.solverModule);

        ClientCommandHandler.instance.registerCommand(new GuiCommand());

        Minecraft.getMinecraft().addScheduledTask(ClientFont::initializeFonts);
    }

    public void log(Object object) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(object.toString()));
        }
    }

    @Subscribe
    public void onKey(WorldTickEvent event) {
        if (this.guiOpened && Minecraft.getMinecraft().currentScreen == null) {
            Minecraft.getMinecraft().displayGuiScreen(new WindowGUI());
        }else if (!this.guiOpened && Minecraft.getMinecraft().currentScreen instanceof WindowGUI) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

}
