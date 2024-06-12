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

/**
 * The DatabaseManager class is responsible for managing the database operations related to player data.
 *
 * It has three private members:
 * - `connection`: a Connection object to interact with the SQLite database.
 * - `plugin`: an instance of the Plugin class, used to get the plugin's data folder.
 * - `config`: an instance of the Config class, used to get configuration details.
 *
 * The DatabaseManager class, upon instantiation, initializes its members which include a connection to the SQLite database,
 * a plugin instance, and a configuration instance. The connect method is then invoked to establish a connection with the
 * SQLite database and create a table if it doesn’t already exist. The savePlayerData method is designed to insert or replace
 * a player’s UUID and serialized inventory into the database, ensuring that the player’s data is always up-to-date. To automate
 * this process, the startSavingTask method initiates a BukkitRunnable task that periodically saves all player inventories at a
 * specified interval, provided that the configuration allows it. When it comes to retrieving player data, the loadPlayerItems
 * method comes into play. It fetches a player’s serialized inventory from the database using their UUID and then deserializes it
 * into an ItemStack array, effectively loading the player’s items. The deserialization process is handled by the deserializeInventory
 * method, a private helper method that takes a Base64 encoded string and converts it into an ItemStack array. This comprehensive
 * system ensures efficient and reliable management of player data in the database.
 */

public class DatabaseManager {
    private Connection connection;
    private Plugin plugin;
    private Config config;

    public DatabaseManager(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void connect() throws SQLException {
        String type = config.getConfig().getString("DataSource.type");
        if (type.equalsIgnoreCase("MYSQL")) {
            String host = config.getConfig().getString("DataSource.host");
            String port = config.getConfig().getString("DataSource.port");
            String database = config.getConfig().getString("DataSource.database");
            String username = config.getConfig().getString("DataSource.username");
            String password = config.getConfig().getString("DataSource.password");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_data (uuid VARCHAR(36) UNIQUE, inventory TEXT, PRIMARY KEY(uuid))").executeUpdate();
        } else {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/database.db");
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_data (uuid TEXT PRIMARY KEY, inventory TEXT)").executeUpdate();
        }
    }

    public void savePlayerData(String uuid, String inventory) throws SQLException {
        String type = config.getConfig().getString("DataSource.type");
        PreparedStatement statement;
        if (type.equalsIgnoreCase("MYSQL")) {
            statement = connection.prepareStatement(
                    "INSERT INTO player_data (uuid, inventory) VALUES (?, ?) ON DUPLICATE KEY UPDATE inventory = ?"
            );
            statement.setString(1, uuid);
            statement.setString(2, inventory);
            statement.setString(3, inventory); // for the UPDATE part
        } else {
            statement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO player_data (uuid, inventory) VALUES (?, ?)"
            );
            statement.setString(1, uuid);
            statement.setString(2, inventory);
        }
        statement.executeUpdate();
    }

    public boolean deletePlayerData(String uuid) throws SQLException {
        String query;
        if (uuid == null) {
            query = "SELECT COUNT(*) AS total FROM player_data";
        } else {
            query = "SELECT COUNT(*) AS total FROM player_data WHERE uuid = ?";
        }

        PreparedStatement countStatement = connection.prepareStatement(query);
        if (uuid != null) {
            countStatement.setString(1, uuid);
        }
        ResultSet resultSet = countStatement.executeQuery();

        if (resultSet.next() && resultSet.getInt("total") > 0) {
            if (uuid == null) {
                query = "DELETE FROM player_data";
            } else {
                query = "DELETE FROM player_data WHERE uuid = ?";
            }

            PreparedStatement deleteStatement = connection.prepareStatement(query);
            if (uuid != null) {
                deleteStatement.setString(1, uuid);
            }
            deleteStatement.executeUpdate();
            return true;
        }
        return false;
    }

    public void startSavingTask(InventoryManager inventoryManager) {
        if (config.getConfig().getBoolean("inventory-settings.save-inventory")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventoryManager.saveAllPlayerInventories();
                }
            }.runTaskTimer(plugin, 0, config.getConfig().getLong("inventory-settings.save-interval") * 20);
        }
    }

    public ItemStack[] loadPlayerItems(UUID playerUUID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT inventory FROM player_data WHERE uuid = ?");
        statement.setString(1, playerUUID.toString());
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ? deserializeInventory(resultSet.getString("inventory")) : null;
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
