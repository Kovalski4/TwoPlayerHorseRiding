package org.github.kovalski.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public boolean hasHayBale(Player player) {
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.getInventory().getItem(i);
            if (is != null){
                if (is.getType() == Material.HAY_BLOCK) {
                    return true;
                }
            }
        }
        return false;
    }

    public void delHayBale(Player player) {
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.getInventory().getItem(i);
            if (is != null) {
                if (is.getType() == Material.HAY_BLOCK) {
                    is.setAmount(is.getAmount() - 1);
                    return;
                }
            }
        }
    }
}
