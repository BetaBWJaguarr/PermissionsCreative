package beta.com.permissionscreative.databasemanager;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;
    private Plugin plugin;
    private Config config;

    public DatabaseManager(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/database.db");
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player_data (" +
                        "uuid TEXT PRIMARY KEY," +
                        "inventory TEXT" +
                        ")")) {
            statement.executeUpdate();
        }
    }

    public void savePlayerData(String uuid, String inventory) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT OR REPLACE INTO player_data (uuid, inventory) VALUES (?, ?)")) {
            statement.setString(1, uuid);
            statement.setString(2, inventory);
            statement.executeUpdate();
        }
    }

    public void startSavingTask(InventoryManager inventoryManager) {
        if (config.getConfig().getBoolean("inventory-settings.save-inventory")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventoryManager.saveAllPlayerInventories();
                }
            }.runTaskTimer(plugin, 0, config.getConfig().getLong("inventory-settings.save-interval") * 20);
            //config.getConfig().getLong("save-interval")
        }
    }
}
