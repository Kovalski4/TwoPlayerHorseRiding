package org.github.kovalski.task;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.github.kovalski.stand.StandMove;

public class ExampleStandMove extends StandMove {

    public ExampleStandMove(Player rider, Entity horse, ArmorStand stand) {
        super(rider, horse, stand);
    }

    //Override seatLocation and setup new location
    @Override
    public Location getSeatLocation(){
        return null;
    }

    //Override getFutureLocation and setup new location
    @Override
    public Location getFutureLocation(){
        return null;
    }

}
