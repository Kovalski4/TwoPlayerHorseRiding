package org.github.kovalski.stand;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import org.bukkit.World;
import org.github.kovalski.TwoPlayerHorseRiding;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public abstract class StandMove implements StandMoveController {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final ProtocolManager protocolManager = instance.getProtocolManager();

    Player rider;
    Entity horse;
    ArmorStand stand;
    Location clientSideLocation;
    boolean isLocked;

    public StandMove(Player rider, Entity horse, ArmorStand stand){
        this.rider = rider;
        this.horse = horse;
        this.stand = stand;
        this.clientSideLocation = getSeatLocation();
    }

    public void run() {
        Location getSeatLocation = getSeatLocation();

        try {
            protocolManager.sendServerPacket(rider, getEntityTeleportPacket(getFutureLocation()));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            methods[1].invoke(methods[0].invoke(stand), getSeatLocation.getX(), getSeatLocation.getY(), getSeatLocation.getZ(), getSeatLocation.getYaw(), getSeatLocation.getPitch());
        } catch (Exception ignored) {
        }

    }

    public void setLock(Boolean b){
        isLocked = b;
    }

    public Boolean isLocked(){
        return isLocked;
    }

    public void teleport(Location location){
        //to-do
    }

    public void stop(){
        stand.eject();
        stand.remove();
    }

    public Location getSeatLocation() {
        Vector backSeatVector = horse.getLocation().getDirection().multiply(0.6).setY(0.3);
        Location location = horse.getLocation().subtract(backSeatVector);
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        if (!stand.getPassengers().isEmpty()){
            Location passangerLoc = stand.getPassenger().getLocation();
            yaw = passangerLoc.getYaw();
            pitch = passangerLoc.getPitch();
        }
        return new Location(location.getWorld(), x, y ,z, yaw, pitch);
    }

    public Location getFutureLocation(){
        Location location = getSeatLocation();
        Vector difference = clientSideLocation.toVector().subtract(location.toVector());
        World world = location.getWorld();
        double x = location.getX() - difference.getX()*4;
        double y = location.getY() - difference.getY()*4;
        double z = location.getZ() - difference.getZ()*4;
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        clientSideLocation = location;
        return new Location(world, x, y, z, yaw, pitch);
    }

    public PacketContainer getEntityTeleportPacket(Location futureLocation){
        PacketContainer movePacket = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        int entityID = stand.getEntityId();
        movePacket.getIntegers()
                .write(0, entityID);
        movePacket.getEntityModifier(stand.getWorld())
                .write(0, stand);
        movePacket.getDoubles()
                .write(0, futureLocation.getX())
                .write(1, futureLocation.getY())
                .write(2, futureLocation.getZ());
        movePacket.getBooleans()
                .write(0, true);
        return movePacket;
    }

    public Player getRider(){
        return rider;
    }

    public Entity getHorse(){
        return horse;
    }

    public ArmorStand getStand(){
        return stand;
    }

    public final Method[] methods = ((Supplier<Method[]>) () -> {
        try {
            Method getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity").getDeclaredMethod("getHandle");
            return new Method[] {
                    getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class)
            };
        } catch (Exception ex) {
            return null;
        }
    }).get();

}
