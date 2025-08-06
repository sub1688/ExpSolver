package me.sub.expsolver.module.impl;

import com.google.common.eventbus.Subscribe;
import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.event.impl.InventoryRenderEvent;
import me.sub.expsolver.event.impl.PacketReceiveEvent;
import me.sub.expsolver.event.impl.PlayerUpdateEvent;
import me.sub.expsolver.font.ClientFont;
import me.sub.expsolver.manager.ClickManager;
import me.sub.expsolver.module.setting.IntSetting;
import me.sub.expsolver.module.setting.PrimitiveSetting;
import me.sub.expsolver.notification.Notification;
import me.sub.expsolver.ui.setting.impl.BooleanSetting;
import me.sub.expsolver.util.ContainerUtil;
import me.sub.expsolver.util.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2FPacketSetSlot;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class ExperimentationSolver {

    private enum GameMode {
        CHRONOMATRON,
        ULTRASEQUENCER,
        SUPERPAIRS,
        OTHER
    }

    private final Minecraft mc = Minecraft.getMinecraft();

    private final IntSetting clickDelay = new IntSetting("Click Delay", "The delay between clicks", 150, 150, 1000, 50);
    private final IntSetting chronomatronRounds = new IntSetting("Chronomatron rounds", "The max rounds when doing chronomatron", 12, 1, 24, 1);
    private final IntSetting ultraSequencerRounds = new IntSetting("Ultrasequencer rounds", "The max rounds when doing ultrasequencer", 12, 1, 36, 1);
    public final PrimitiveSetting<Boolean> toggled = new PrimitiveSetting<Boolean>("Enabled", "Enabled", true) {
        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (value) {
                ExpSolver.INSTANCE.eventBus.register(ExperimentationSolver.this);
            }else {
                ExpSolver.INSTANCE.eventBus.unregister(ExperimentationSolver.this);
            }
        }
    };

    private final List<ClickManager.Click> pendingClicks;
    private final Stopwatch listenStopwatch;
    private final ClientFont font;
    private int round;

    private final List<PrimitiveSetting<?>> settings = new ArrayList<>();


    public ExperimentationSolver() {
        this.pendingClicks = new ArrayList<>();
        this.listenStopwatch = new Stopwatch();
        this.font = ClientFont.ROBOTO;
        this.round = 0;

        this.settings.add(clickDelay);
        this.settings.add(chronomatronRounds);
        this.settings.add(ultraSequencerRounds);
        this.settings.add(toggled);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onInventoryRender(InventoryRenderEvent event) {
        int maxRounds = getMaxRounds();
        if (maxRounds == 0) {
            return;
        }

        String gameMode = String.format("Solving %s (max rounds - %d)", getCurrentGameMode().name().toLowerCase(), maxRounds);
        float textHeight = font.getFontRenderer().getHeight(gameMode);
        GlStateManager.disableLighting();
        font.getFontRenderer().drawStringWithOutline(gameMode, 0, -textHeight, new Color(0xFFFFFF).getRGB());
        GlStateManager.enableLighting();
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onPacketReceive(PacketReceiveEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S2FPacketSetSlot) {
            S2FPacketSetSlot setSlot = (S2FPacketSetSlot) packet;
            ItemStack stack = setSlot.func_149174_e();
            if (stack == null)
                return;
            Item item = stack.getItem();
            int slot = setSlot.func_149173_d();
            int windowId = setSlot.func_149175_c();
            int glowstoneIndex = ContainerUtil.getSlotIndex(itemStack -> itemStack.getItem() == Item.getItemFromBlock(Blocks.glowstone));
            GameMode gameMode = getCurrentGameMode();

            if ((glowstoneIndex == -1 && gameMode != GameMode.SUPERPAIRS) || item == null) {
                return;
            }


            if (item == Items.clock) {
                if (glowstoneIndex == slot) {
                    pendingClicks.forEach(ExpSolver.INSTANCE.clickManager::scheduleClick);
                    pendingClicks.clear();
                    return;
                }
            }

            switch (gameMode) {
                case CHRONOMATRON:
                    if (item == Item.getItemFromBlock(Blocks.stained_hardened_clay) && listenStopwatch.hasReached(100L)) {
                        pendingClicks.add(new ClickManager.Click(slot, 2, 3, mc.thePlayer.openContainer.windowId, clickDelay.getValue()));
                        listenStopwatch.reset();
                    }
                    break;

                case ULTRASEQUENCER:
                    List<ClickManager.Click> clicks = getUltraSequencerClicks();
                    if (pendingClicks.size() < clicks.size()) {
                        pendingClicks.clear();
                        pendingClicks.addAll(clicks);
                    }
                    break;
            }
        } else if (packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;
            if (soundEffect.getSoundName().equalsIgnoreCase("random.levelup") && soundEffect.getPitch() == 1.7619047F) {
                int maxRounds = getMaxRounds();
                if (maxRounds != 0 && ++round >= maxRounds) {
                    String gamemode = getCurrentGameMode().name();
                    gamemode = gamemode.substring(0, 1).toUpperCase() + gamemode.substring(1).toLowerCase();
                    ExpSolver.INSTANCE.notificationManager.addNotification(Notification.NotificationType.INFO, gamemode, String.format("Successfully finished %s on round %d!", gamemode.toLowerCase(), round));

                    mc.thePlayer.closeScreen();
                    round = 0;
                }
            }
        }
    }

    private int getMaxRounds() {
        switch (getCurrentGameMode()) {
            case CHRONOMATRON:
                return chronomatronRounds.getValue();
            case ULTRASEQUENCER:
                return ultraSequencerRounds.getValue();
        }
        return 0;
    }

    private List<ClickManager.Click> getUltraSequencerClicks() {
        IInventory inventory = ContainerUtil.getLowerChestInventory();
        if (inventory == null) {
            return Collections.emptyList();
        }

        Map<Integer, Integer> clickMap = new HashMap<>();
        for (int index = 9; index < inventory.getSizeInventory() - 9; index++) {
            ItemStack itemStack = inventory.getStackInSlot(index);
            if (itemStack == null) {
                continue;
            }

            if (itemStack.getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                continue;
            }

            clickMap.put(itemStack.stackSize, index);

        }

        return clickMap.keySet()
                .stream()
                .sorted(Comparator.comparingInt(order -> order))
                .map(key -> new ClickManager.Click(clickMap.get(key), 2, 3, mc.thePlayer.openContainer.windowId, clickDelay.getValue()))
                .collect(Collectors.toList());

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onUpdate(PlayerUpdateEvent event) {

        if (getCurrentGameMode() == GameMode.OTHER) {
            pendingClicks.clear();
            round = 0;
        }
    }

    private GameMode getCurrentGameMode() {
        IInventory inventory = ContainerUtil.getLowerChestInventory();
        if (inventory == null) {
            return GameMode.OTHER;
        }

        String name = inventory.getDisplayName().getUnformattedText();
        if (!name.endsWith(")")) {
            return GameMode.OTHER;
        }


        switch (name.split(" ")[0].toLowerCase()) {
            case "chronomatron":
                return GameMode.CHRONOMATRON;
            case "ultrasequencer":
                return GameMode.ULTRASEQUENCER;
            case "superpairs":
                return GameMode.SUPERPAIRS;
        }


        return GameMode.OTHER;
    }

    public List<PrimitiveSetting<?>> getSettings() {
        return this.settings;
    }

}
