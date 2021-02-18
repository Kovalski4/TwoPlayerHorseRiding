package org.github.kovalski.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.github.kovalski.event.HorseDismountEvent;
import org.github.kovalski.event.HorseMountEvent;

public class BukkitListener implements Listener {

    @EventHandler
    public void onMount(VehicleEnterEvent event){
        if (event.getVehicle() instanceof LivingEntity){
            LivingEntity horse = (LivingEntity) event.getVehicle();
            Bukkit.getPluginManager().callEvent(new HorseMountEvent(event.getEntered(), horse, horse.getLocation()));
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getVehicle() instanceof LivingEntity){
            LivingEntity horse = (LivingEntity) event.getVehicle();
            Bukkit.getPluginManager().callEvent(new HorseMountEvent(event.getExited(), horse, horse.getLocation()));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event){
        LivingEntity horse = event.getEntity();
        Bukkit.getPluginManager().callEvent(new HorseDismountEvent(horse, horse.getLocation(), HorseDismountEvent.DismountType.DEATH));
    }

}
