package org.github.kovalski.listener;

import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.event.PlayerLeaveTownEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.kovalski.HorseManager;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.github.kovalski.data.YamlConfig;

public class TownyListener implements Listener {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final HorseManager horseManager = instance.getHorseManager();
    private final YamlConfig yamlConfig = instance.getYamlConfig();

    @EventHandler
    public void onEnterTown(PlayerEnterTownEvent event){
        if (yamlConfig.getBoolean("force_walk_in_towns")){
            Player rider = event.getPlayer();
            if (rider.getVehicle() != null){
                if (horseManager.isTwoPlayerAllowedEntity(rider.getVehicle())){
                    LivingEntity horse = (LivingEntity) rider.getVehicle();
                    AttributeInstance GENERIC_MOVEMENT_SPEED = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                    if (GENERIC_MOVEMENT_SPEED == null){
                        return;
                    }

                    if (GENERIC_MOVEMENT_SPEED.getBaseValue() != 0.1f){
                        horseManager.toggleWalk(rider, horse);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeaveTown(PlayerLeaveTownEvent event){
        if (yamlConfig.getBoolean("force_walk_in_towns")){
            Player rider = event.getPlayer();
            if (rider.getVehicle() != null){
                if (horseManager.isTwoPlayerAllowedEntity(rider.getVehicle())){
                    LivingEntity horse = (LivingEntity) rider.getVehicle();
                    AttributeInstance GENERIC_MOVEMENT_SPEED = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                    if (GENERIC_MOVEMENT_SPEED == null){
                        return;
                    }

                    if (GENERIC_MOVEMENT_SPEED.getBaseValue() == 0.1f){
                        horseManager.toggleWalk(rider, horse);
                    }
                }
            }
        }
    }

}
