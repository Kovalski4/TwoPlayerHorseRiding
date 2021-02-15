package org.github.kovalski.stand;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.github.kovalski.task.DonkeyStandMove;
import org.github.kovalski.task.HorseStandMove;
import org.github.kovalski.task.LlamaStandMove;
import org.github.kovalski.task.MuleStandMove;
import org.github.kovalski.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class StandMoveHandler extends BukkitRunnable {

    public static final List<StandMoveController> horseStandMoveList = new ArrayList<>();
    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private static final MessageUtil messageUtil = instance.getMessageUtil();

    @Override
    public void run() {
        for (StandMoveController horseStandMove : horseStandMoveList){
            horseStandMove.run();
        }
    }

    public static void addStandMove(Player rider, Entity horse, EntityType entityType) {
        StandMoveController standMoveController = null;
        ArmorStand armorStand = (ArmorStand) horse.getWorld().spawnEntity(horse.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);
        switch (entityType) {
            case HORSE:
                standMoveController = new HorseStandMove(rider, horse, armorStand);
                break;
            case DONKEY:
                standMoveController = new DonkeyStandMove(rider, horse, armorStand);
                break;
            case MULE:
                standMoveController = new MuleStandMove(rider, horse, armorStand);
                break;
            case LLAMA:
                standMoveController = new LlamaStandMove(rider, horse, armorStand);
                break;
        }
        if (standMoveController != null){
            StandMoveHandler.horseStandMoveList.add(standMoveController);
        }
    }

    public static void removeIfExist(Entity horse){
        for (StandMoveController horseStandMove : horseStandMoveList){
            if (horseStandMove.getHorse().equals(horse)){
                horseStandMove.stop();
                horseStandMoveList.remove(horseStandMove);
                break;
            }
        }
    }

    public static void teleportIfExist(Entity horse, Location location){
        for (StandMoveController horseStandMove : horseStandMoveList){
            if (horseStandMove.getHorse().equals(horse)){
                horseStandMove.teleport(location);
                break;
            }
        }
    }

    public static void sitBackSeat(Entity horse, Player passanger){
        for (StandMoveController horseStandMove : horseStandMoveList){
            if (horseStandMove.getHorse().equals(horse)){
                if (horseStandMove.isLocked()){
                    passanger.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_BACKSEAT_LOCKED));
                    return;
                }
                if (horseStandMove.getStand().getPassengers().isEmpty()){
                    horseStandMove.getStand().setPassenger(passanger);
                    return;
                }
            }
        }
    }

}
