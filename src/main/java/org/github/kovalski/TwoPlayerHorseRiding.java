package org.github.kovalski;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.github.kovalski.cmd.MountCommand;
import org.github.kovalski.cmd.TwoPlayerHorseRidingCommand;
import org.github.kovalski.data.Database;
import org.github.kovalski.stand.StandMoveHandler;
import org.github.kovalski.listener.BukkitListener;
import org.github.kovalski.listener.HorseListener;
import org.github.kovalski.listener.PlayerListener;
import org.github.kovalski.stand.StandMoveController;
import org.github.kovalski.util.InventoryUtils;
import org.github.kovalski.data.YamlConfig;
import org.github.kovalski.util.MessageUtil;
import org.github.kovalski.util.UpdateChecker;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public final class TwoPlayerHorseRiding extends JavaPlugin {

    private static TwoPlayerHorseRiding instance;
    private Connection connection;
    private YamlConfig yamlConfig;
    private MessageUtil messageUtil;
    private Database database;
    private ProtocolManager protocolManager;
    private InventoryUtils inventoryUtils;
    private HorseManager horseManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        initConfig();
        setupSQLHikariPool();
        initManagers();
        registerEvents();
        loadCommand();
        initbStats();
        new StandMoveHandler().runTaskTimer(this, 1L, 0);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload(){
        for (StandMoveController standMoveController : StandMoveHandler.horseStandMoveList){
            standMoveController.stop();
        }
        yamlConfig.load(new File(this.getDataFolder(), "config.yml"));
        messageUtil.getMessages().load(new File(this.getDataFolder(), "messages.yml"));
        horseManager.setTwoPlayerEntitys();
    }

    private void checkUpdate(){
        Logger logger = this.getLogger();
        new UpdateChecker(this, 86444).getVersion(version -> {
            DefaultArtifactVersion spigotVer = new DefaultArtifactVersion(version);
            DefaultArtifactVersion currentVer = new DefaultArtifactVersion(this.getDescription().getVersion());
            if (currentVer.getIncrementalVersion() < spigotVer.getIncrementalVersion()) {
                logger.info("Checking for updates...");
                logger.info("You are running outdated version of TwoPlayerHorseRiding (v"+currentVer+")");
                logger.info("Get latest version at: https://www.spigotmc.org/resources/86444/");
            }else{
                logger.info("Checking for updates...");
                logger.info("You are running latest version of TwoPlayerHorseRiding (v"+currentVer+")");
            }
        });
    }

    private void setupSQLHikariPool() {

        HikariConfig config = new HikariConfig();

        try {
            String url = "jdbc:sqlite:./"+this.getDataFolder() + "/horse_data.db";

            config.setJdbcUrl(url);
            config.setUsername("admin");
            //config.setPassword(password);

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            HikariDataSource ds = new HikariDataSource(config);

            connection = ds.getConnection();
            Statement st = connection.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS horse_data (HORSE UUID, SPEED DOUBLE);");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerEvents(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new HorseListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new BukkitListener(), this);
    }

    @SuppressWarnings("ConstantConditions")
    public void loadCommand(){
        getCommand("mount").setExecutor(new MountCommand());
        getCommand("mount").setTabCompleter(new MountCommand());
        getCommand("twoplayerhorseriding").setExecutor(new TwoPlayerHorseRidingCommand());
        getCommand("twoplayerhorseriding").setTabCompleter(new TwoPlayerHorseRidingCommand());
    }

    public void initConfig(){
        yamlConfig = new YamlConfig("config.yml");
        messageUtil = new MessageUtil(new YamlConfig("messages.yml"));
    }

    public void initManagers(){
        database = new Database();
        horseManager = new HorseManager();
        inventoryUtils = new InventoryUtils();
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void initbStats(){
        int pluginId = 10315; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
    }

    public static TwoPlayerHorseRiding getInstance() {
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }

    public YamlConfig getYamlConfig() {
        return yamlConfig;
    }

    public Database getDatabase() {
        return database;
    }

    public HorseManager getHorseManager() {
        return horseManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public InventoryUtils getInventoryUtils() {
        return inventoryUtils;
    }

    public MessageUtil getMessageUtil() {
        return messageUtil;
    }
}
