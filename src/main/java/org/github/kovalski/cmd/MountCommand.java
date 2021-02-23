package org.github.kovalski.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.github.kovalski.HorseManager;
import org.github.kovalski.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MountCommand implements CommandExecutor, TabCompleter {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final HorseManager horseManager = instance.getHorseManager();
    private final MessageUtil messageUtil = instance.getMessageUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_ONLY_PLAYERS));
            return true;
        }

        Player rider = (Player) sender;
        LivingEntity horse = (LivingEntity) rider.getVehicle();

        if (horse == null){
            rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_MOUNT_NOT_FOUND));
            return true;
        }

        if (args.length == 1){

            if (args[0].equals("walk")){

                if (horseManager.canWalk(rider, horse)){
                    horseManager.toggleWalk(rider, horse);
                }

                return true;

            }

            else if (args[0].equals("lock")){

                if (horseManager.canLock(rider, horse)){
                    horseManager.toggleLock(rider, horse);
                }

                return true;

            }

        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1){
            completions.add("walk");
            completions.add("lock");
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }
        return null;
    }

}
