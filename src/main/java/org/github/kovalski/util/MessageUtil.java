package org.github.kovalski.util;
import org.bukkit.ChatColor;
import org.bukkit.util.CachedServerIcon;
import org.github.kovalski.data.YamlConfig;

public class MessageUtil {

    private final YamlConfig messages;

    public MessageUtil(YamlConfig messages) {
        this.messages = messages;
    }

    public enum Messages{
        ERROR_NO_PERM,
        CMD_RELOAD,
        ENABLED_WALK_MODE,
        DISABLED_WALK_MODE,
        ERROR_MAXIMUM_HEALTH_NOT_FOUND,
        ERROR_MOUNT_NOT_FOUND,
        ERROR_ONLY_PLAYERS,
        ERROR_ALREADY_SLOW,
        ERROR_CANNOT_LOCK,
        MOUNT_LOCKED,
        MOUNT_UNLOCKED,
        ERROR_NOT_ALLOWED_ENTITY,
        ERROR_BACKSEAT_LOCKED,
        ERROR_NOT_ALLOWED_IN_TOWNS
    }

    public String getMessage(Messages messageName){

        String string = " ";

        switch (messageName){
            case ERROR_NO_PERM:
                string = messages.getString("error_no_perm");
                break;
            case CMD_RELOAD:
                string = messages.getString("cmd_reload");
                break;
            case ENABLED_WALK_MODE:
                string = messages.getString("enabled_walk_mode");
                break;
            case DISABLED_WALK_MODE:
                string = messages.getString("disabled_walk_mode");
                break;
            case ERROR_MAXIMUM_HEALTH_NOT_FOUND:
                string = messages.getString("error_maximum_health_not_found");
                break;
            case ERROR_MOUNT_NOT_FOUND:
                string = messages.getString("error_mount_not_found");
                break;
            case ERROR_ONLY_PLAYERS:
                string = messages.getString("error_only_players");
                break;
            case ERROR_ALREADY_SLOW:
                string = messages.getString("error_already_slow");
                break;
            case ERROR_CANNOT_LOCK:
                string = messages.getString("error_cannot_lock");
                break;
            case MOUNT_LOCKED:
                string = messages.getString("mount_locked");
                break;
            case ERROR_NOT_ALLOWED_ENTITY:
                string = messages.getString("error_not_allowed_entity");
                break;
            case MOUNT_UNLOCKED:
                string = messages.getString("mount_unlocked");
                break;
            case ERROR_BACKSEAT_LOCKED:
                string = messages.getString("error_backseat_locked");
                break;
            case ERROR_NOT_ALLOWED_IN_TOWNS:
                string = messages.getString("error_not_allowed_in_towns");
                break;
        }

        return format(getPrefix()+" "+string);
    }

    public String getPrefix(){
        return messages.getString("prefix");
    }

    public String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public YamlConfig getMessages(){
        return messages;
    }

}
