package me.sub.expsolver.command;

import me.sub.expsolver.ExpSolver;
import me.sub.expsolver.ui.WindowGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.Collections;
import java.util.List;

public class GuiCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "expsolver";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/expsolver";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ExpSolver.INSTANCE.guiOpened = true;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return this.getCommandName().compareTo(o.getCommandName());
    }
}
