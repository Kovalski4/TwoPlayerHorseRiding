package org.github.kovalski.task;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.github.kovalski.data.YamlConfig;
import org.github.kovalski.util.InventoryUtils;
import org.github.kovalski.util.MessageUtil;

public class HorseBreed {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final InventoryUtils inventoryUtils = instance.getInventoryUtils();
    private static final YamlConfig yamlConfig = instance.getYamlConfig();
    private final MessageUtil messageUtil = instance.getMessageUtil();

    Player rider;
    LivingEntity horse;
    ItemStack food;
    Double damage;

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

    public HorseBreed(Player rider, LivingEntity horse, ItemStack food, Double damage) {
        this.rider = rider;
        this.horse = horse;
        this.food = food;
        this.damage = damage;
        feed();
    }

    public void feed(){
        double heal = Foods.valueOf(food.getType().toString()).getValue();
        AttributeInstance genericMaxHealth = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (genericMaxHealth == null){
            throw new NullPointerException(messageUtil.getMessage(MessageUtil.Messages.ERROR_MAXIMUM_HEALTH_NOT_FOUND));
        }
        double remain = horse.getHealth()-damage;
        while (inventoryUtils.hasHayBale(rider)){
            inventoryUtils.delHayBale(rider);
            if (remain+heal >= genericMaxHealth.getValue()){
                horse.setHealth(Math.min(genericMaxHealth.getValue(), remain));
                return;
            }
            else {
                remain += heal;
            }
        }
        horse.setHealth(Math.min(genericMaxHealth.getValue(), remain));
    }

}
