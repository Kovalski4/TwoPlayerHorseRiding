package org.github.kovalski.data;

import org.github.kovalski.TwoPlayerHorseRiding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final TwoPlayerHorseRiding instance = TwoPlayerHorseRiding.getInstance();

    public double getSpeedFromDB(String HorseUUID){
        String query = "SELECT SPEED FROM horse_data WHERE HORSE = '" + HorseUUID + "'";
        try (Statement stmt = instance.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean hasSpeedData(String HorseUUID){
        try (PreparedStatement prst = instance
                .getConnection()
                .prepareStatement("SELECT SPEED FROM horse_data WHERE HORSE = ?")){

            prst.setString(1, HorseUUID);

            try (ResultSet result = prst.executeQuery()) {
                return result.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createSpeedData(String HorseUUID, Double speed){
        try {
            PreparedStatement prst = this
                    .instance.getConnection()
                    .prepareStatement("INSERT INTO horse_data (HORSE,SPEED) VALUES (?,?)");
            prst.setString(1, HorseUUID);
            prst.setDouble(2, speed);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSpeedData(String HorseUUID, Double speed){
        try (PreparedStatement prst = instance
                .getConnection()
                .prepareStatement("UPDATE horse_data SET SPEED = ? WHERE HORSE = '" + HorseUUID + "'")){
            prst.setDouble(1, speed);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSpeedData(String HorseUUID){
        try (PreparedStatement prst = instance
                .getConnection()
                .prepareStatement("DELETE FROM horse_data WHERE HORSE = '" + HorseUUID + "'")){
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
