package org.github.kovalski.stand;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.github.kovalski.TwoPlayerHorseRiding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public abstract class StandMove implements StandMoveController {

    private static final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();
    private final ProtocolManager protocolManager = instance.getProtocolManager();

    Player rider;
    Entity horse;
    ArmorStand stand;
    boolean isLocked;
    int standCounter;
    Location oldLocation;

    public StandMove(Player rider, Entity horse, ArmorStand stand){
        this.rider = rider;
        this.horse = horse;
        this.stand = stand;
        oldLocation = stand.getLocation();
    }

    public void run() {
        Location seatLocation = getSeatLocation();

        try {
            methods[1].invoke(methods[0].invoke(stand), seatLocation.getX(), seatLocation.getY(), seatLocation.getZ(), seatLocation.getYaw(), seatLocation.getPitch());
        } catch (Exception ignored) {
        }

        if (stand.getPassenger() instanceof LivingEntity){

            double difference = oldLocation.toVector().subtract(seatLocation.toVector()).length();

            if (difference < 0.15){
                if (standCounter >= 5){
                    sendVisiblePacket();
                } else {
                    standCounter++;
                }
            }
            else {
                sendInvisiblePacket();
                standCounter = 0;
            }
        }

        oldLocation = seatLocation;

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

    public PacketContainer getInvisPacket(LivingEntity passanger, byte b){
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);

        int EntityID = passanger.getEntityId();

        WrappedDataWatcher watcher = new WrappedDataWatcher(passanger);
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        WrappedDataWatcher.WrappedDataWatcherObject test = new WrappedDataWatcher.WrappedDataWatcherObject(0, serializer);
        watcher.setObject(test, b);

        packetContainer.getIntegers()
                .write(0, EntityID);
        packetContainer.getWatchableCollectionModifier()
                .write(0, watcher.getWatchableObjects());

        return packetContainer;
    }

    public void sendInvisiblePacket(){
        try {
            protocolManager.sendServerPacket(rider, getInvisPacket((LivingEntity) stand.getPassenger(), (byte) 0x20));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void sendVisiblePacket(){
        try {
            protocolManager.sendServerPacket(rider, getInvisPacket((LivingEntity) stand.getPassenger(), (byte) 0x0));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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

    public StandMove getStandMove(){
        return this;
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
