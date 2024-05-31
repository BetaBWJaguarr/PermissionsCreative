package beta.com.permissionscreative.databasemanager;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Base64;
import java.util.UUID;

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

    public ItemStack[] loadPlayerItems(UUID playerUUID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT inventory FROM player_data WHERE uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String serializedInventory = resultSet.getString("inventory");
                return deserializeInventory(serializedInventory);
            }
        }
        return null;
    }

    private ItemStack[] deserializeInventory(String serializedInventory) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(serializedInventory)));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ItemStack[] inventory = new ItemStack[36];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = config.getItemStack(String.valueOf(i), null);
        }
        return inventory;
    }
}
