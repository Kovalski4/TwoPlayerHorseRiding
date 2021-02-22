package org.github.kovalski.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HorseDismountEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Entity rider;
    private final LivingEntity horse;
    private final Location dismountLocation;
    private final DismountType dismountType;
    private boolean isCancelled = false;

    public enum DismountType {
        DISMOUNT,
        TELEPORT,
        DEATH
    }

    public HorseDismountEvent(Entity rider, LivingEntity horse, Location dismountLocation, DismountType dismountType){

        this.rider = rider;
        this.horse = horse;
        this.dismountLocation = dismountLocation;
        this.dismountType = dismountType;

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

    public Entity getRider() {
        return rider;
    }

    public LivingEntity getHorse() {
        return horse;
    }

    public Location getDismountLocation() {
        return dismountLocation;
    }

    public DismountType getDismountType() {
        return dismountType;
    }

}
