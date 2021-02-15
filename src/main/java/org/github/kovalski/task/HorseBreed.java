package org.github.kovalski.task;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.github.kovalski.data.YamlConfig;
import org.github.kovalski.util.MessageUtil;

public class HorseBreed {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private static final YamlConfig yamlConfig = instance.getYamlConfig();
    private final MessageUtil messageUtil = instance.getMessageUtil();

    LivingEntity horse;
    ItemStack food;

    enum Foods{

        HAY_BLOCK(yamlConfig.getDouble("hay_bale"));

        private final Double value;

        Foods(Double value) {
            this.value = value;
        }

        public Double getValue() {
            return value;
        }

    }

    public HorseBreed(LivingEntity horse, ItemStack food) {
        this.horse = horse;
        this.food = food;
        feed();
    }

    public void feed(){
        double heal = Foods.valueOf(food.getType().toString()).getValue();
        AttributeInstance genericMaxHealth = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (genericMaxHealth == null){
            throw new NullPointerException(messageUtil.getMessage(MessageUtil.Messages.ERROR_MAXIMUM_HEALTH_NOT_FOUND));
        }
        horse.setHealth(Math.min(genericMaxHealth.getValue(), heal + horse.getHealth()));
    }

}
