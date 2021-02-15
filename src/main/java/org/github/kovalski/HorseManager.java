package org.github.kovalski;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.github.kovalski.data.Database;
import org.github.kovalski.data.YamlConfig;
import org.github.kovalski.stand.StandMoveHandler;
import org.github.kovalski.stand.StandMoveController;
import org.github.kovalski.util.MessageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HorseManager {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final YamlConfig yamlConfig = instance.getYamlConfig();
    private final MessageUtil messageUtil = instance.getMessageUtil();
    private final Database database = instance.getDatabase();

    private List<EntityType> twoPlayerEntitys = new ArrayList<>();

    public boolean isTwoPlayerAllowedEntity(Entity entity){
        return twoPlayerEntitys.contains(entity.getType());
    }

    public HorseManager(){
        setTwoPlayerEntitys();
    }

    @Nullable
    public Player getRider(LivingEntity horse){
        Entity passanger = horse.getPassenger();
        if (passanger instanceof Player){
            return (Player) passanger;
        }
        return null;
    }

    public void toggleWalk(LivingEntity horse, Player rider){
        AttributeInstance GENERIC_MOVEMENT_SPEED = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        String UUID = horse.getUniqueId().toString();

        if (GENERIC_MOVEMENT_SPEED == null){
            rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_MAXIMUM_HEALTH_NOT_FOUND));
            return;
        }

        if (database.hasSpeedData(UUID)){

            if (GENERIC_MOVEMENT_SPEED.getBaseValue() == 0.1f){
                GENERIC_MOVEMENT_SPEED.setBaseValue(database.getSpeedFromDB(UUID));
                rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.DISABLED_WALK_MODE));
            }

            else {
                database.updateSpeedData(UUID, GENERIC_MOVEMENT_SPEED.getBaseValue());
                GENERIC_MOVEMENT_SPEED.setBaseValue(0.1f);
                rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ENABLED_WALK_MODE));
            }

        }

        else {

            if (GENERIC_MOVEMENT_SPEED.getBaseValue() <= 0.1f){
                rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_ALREADY_SLOW));
                return;
            }

            database.createSpeedData(UUID, GENERIC_MOVEMENT_SPEED.getBaseValue());
            GENERIC_MOVEMENT_SPEED.setBaseValue(0.1f);
            rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ENABLED_WALK_MODE));
        }

    }

    public void toggleLock(Entity horse, Player rider){
        for (StandMoveController standMoveController : StandMoveHandler.horseStandMoveList) {
            if (standMoveController.getHorse().equals(horse)) {
                if (standMoveController.isLocked()){
                    standMoveController.setLock(false);
                    rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.MOUNT_UNLOCKED));
                    return;
                }
                else {
                    standMoveController.setLock(true);
                    rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.MOUNT_LOCKED));
                    return;
                }
            }
        }
        rider.sendMessage(messageUtil.getMessage(MessageUtil.Messages.ERROR_MOUNT_NOT_FOUND));
    }

    public void setTwoPlayerEntitys(){
        List<EntityType> twoPlayerEntitys = new ArrayList<>();
        if (yamlConfig.getBoolean("HORSE")){
            twoPlayerEntitys.add(EntityType.HORSE);
        }
        if (yamlConfig.getBoolean("DONKEY")){
            twoPlayerEntitys.add(EntityType.DONKEY);
        }
        if (yamlConfig.getBoolean("MULE")){
            twoPlayerEntitys.add(EntityType.MULE);
        }
        if (yamlConfig.getBoolean("LLAMA")){
            twoPlayerEntitys.add(EntityType.LLAMA);
        }
        this.twoPlayerEntitys = twoPlayerEntitys;
    }

}
