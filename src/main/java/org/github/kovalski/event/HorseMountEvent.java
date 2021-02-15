package org.github.kovalski.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HorseMountEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Entity rider;
    private final LivingEntity horse;
    private final Location mountLocation;
    private boolean isCancelled = false;


    public HorseMountEvent(Entity rider, LivingEntity horse, Location dismountLocation){
        this.rider = rider;
        this.horse = horse;
        this.mountLocation = dismountLocation;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LivingEntity getHorse() {
        return horse;
    }

    public Location getMountLocation() {
        return mountLocation;
    }

    public Entity getRider() {
        return rider;
    }
}
