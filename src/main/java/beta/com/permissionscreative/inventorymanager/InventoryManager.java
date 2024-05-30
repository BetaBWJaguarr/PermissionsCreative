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

public class InventoryManager {
    private DatabaseManager databaseManager;
    private Config config;

    public InventoryManager(DatabaseManager databaseManager, Config config) {
        this.databaseManager = databaseManager;
        this.config = config;
    }

    public String serializeInventory(ItemStack[] inventory) {
        YamlConfiguration config = new YamlConfiguration();
        for (int i = 0; i < inventory.length; i++) {
            config.set(String.valueOf(i), inventory[i]);
        }
        return Base64.getEncoder().encodeToString(config.saveToString().getBytes());
    }

    public void savePlayerInventory(Player player) {
        if (config.getConfig().getBoolean("inventory-settings.save-stop-inventory") && player.hasPermission("permissionscreative.inventory-save.bypass")) {
            return;
        }

        ItemStack[] inventory = player.getInventory().getContents();
        boolean isEmpty = Arrays.stream(inventory).allMatch(Objects::isNull);
        if (!isEmpty) {
            String serializedInventory = serializeInventory(inventory);
            try {
                databaseManager.savePlayerData(player.getUniqueId().toString(), serializedInventory);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAllPlayerInventories() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerInventory(player);
        }
    }
}
