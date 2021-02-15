package org.github.kovalski.task;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.kovalski.stand.StandMove;

public class HorseStandMove extends StandMove {

    public HorseStandMove(Player rider, Entity horse, ArmorStand stand) {
        super(rider, horse, stand);
    }

}
