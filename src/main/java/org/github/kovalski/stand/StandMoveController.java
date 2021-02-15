package org.github.kovalski.stand;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface StandMoveController {

    void run();

    void stop();

    void setLock(Boolean b);

    void teleport(Location location);

    Player getRider();

    Entity getHorse();
    Boolean isLocked();

    ArmorStand getStand();

}
