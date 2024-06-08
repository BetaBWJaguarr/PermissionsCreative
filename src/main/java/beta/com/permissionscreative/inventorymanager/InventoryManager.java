package beta.com.permissionscreative.inventorymanager;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * The InventoryManager class is designed to handle the serialization, storage, and retrieval of player inventories within the game.
 * It provides methods to serialize the inventory into a string format, save it to a database, and restore it to the player as needed.
 * This class ensures that player inventories are preserved, especially during server stops or restarts, and can be a crucial part of
 * gameplay mechanics that require inventory manipulation. It works closely with the DatabaseManager to handle data persistence and
 * uses the server's configuration settings to determine when and how inventories should be saved or bypassed.
 */


public class InventoryManager {
    private final DatabaseManager databaseManager;
    private final Config config;

    public InventoryManager(DatabaseManager databaseManager, Config config) {
        this.databaseManager = databaseManager;
        this.config = config;
    }

    private String serializeInventory(ItemStack[] inventory) {
        YamlConfiguration yamlConfig = new YamlConfiguration();
        for (int i = 0; i < inventory.length; i++) {
            yamlConfig.set(String.valueOf(i), inventory[i]);
        }
        return Base64.getEncoder().encodeToString(yamlConfig.saveToString().getBytes());
    }

    private void saveInventoryToDatabase(Player player, String serializedInventory) {
        try {
            databaseManager.savePlayerData(player.getUniqueId().toString(), serializedInventory);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerInventory(Player player) {
        if (config.getConfig().getBoolean("inventory-settings.save-stop-inventory") && player.hasPermission("permissionscreative.inventory-save.bypass")) {
            return;
        }

        ItemStack[] inventory = player.getInventory().getContents();
        boolean isEmpty = Arrays.stream(inventory).allMatch(Objects::isNull);
        if (!isEmpty) {
            String serializedInventory = serializeInventory(inventory);
            saveInventoryToDatabase(player, serializedInventory);
        }
    }

    public void saveAllPlayerInventories() {
        Bukkit.getOnlinePlayers().forEach(this::savePlayerInventory);
    }

    public void setPlayerInventory(Player player, ItemStack[] items) {
        player.getInventory().setContents(items);
    }
}