package org.github.kovalski.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.github.kovalski.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TwoPlayerHorseRidingCommand implements CommandExecutor, TabCompleter {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private MessageUtil messageUtil = instance.getMessageUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!sender.hasPermission("twoplayerhorseriding.admin")){
            sender.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_NO_PERM));
            return true;
        }

        if (args.length == 1 && args[0].equals("reload")){
            sender.sendMessage(messageUtil.getMessage(MessageUtil.Messages.CMD_RELOAD));
            instance.reload();
            return true;
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1){
            completions.add("reload");
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }
        return null;
    }
}
