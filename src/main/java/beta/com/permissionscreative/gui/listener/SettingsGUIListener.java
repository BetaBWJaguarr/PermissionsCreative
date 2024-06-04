package beta.com.permissionscreative.gui.listener;

import beta.com.paginationapi.listener.PaginationListener;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.SettingsGUI;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

/**
 * The SettingsGUIListener class implements the Listener interface and is responsible for handling user interactions with the SettingsGUI.
 *
 * This class has two main methods:
 *
 * 1. onInventoryClick(InventoryClickEvent event): This method is triggered when a player clicks on an item in the inventory.
 *    - It first checks if the clicked inventory is the "Settings" GUI.
 *    - If the clicked item is "Next Page" or "Previous Page", it navigates to the next or previous page of the settings GUI using the PaginationListener.
 *    - If the clicked item is a setting, it toggles the setting's state. It does this by getting the setting name and current state from the item's display name, then updating the corresponding setting in the config. It then saves the updated config and sends a success message to the player.
 *    - If the clicked item is not a setting or navigation item, it does nothing.
 *
 * 2. onInventoryClose(InventoryCloseEvent event): This method is triggered when a player closes the inventory.
 *    - It checks if the closed inventory is the "Settings" GUI.
 *    - If it is, it updates the GUI to reflect the current state of the settings. This is done by calling the GUI method of the SettingsGUI instance.
 *
 * The class also has a constructor, SettingsGUIListener(SettingsGUI settingsGUI, Config config, Plugin plugin, LangManager langManager), which initializes the SettingsGUI, Config, Plugin, and LangManager instances used by the class, and creates a new PaginationListener for the SettingsGUI's Pagination.
 *
 *  If the sender is a player and has the required permissions, it opens the settings GUI for
 * the player and returns true.
 *
 */


public class SettingsGUIListener implements Listener {
    private SettingsGUI settingsGUI;
    private Config config;
    private Plugin plugin;
    private LangManager langManager;
    private PaginationListener paginationListener;

    public SettingsGUIListener(SettingsGUI settingsGUI, Config config, Plugin plugin, LangManager langManager) {
        this.settingsGUI = settingsGUI;
        this.config = config;
        this.plugin = plugin;
        this.langManager = langManager;
        this.paginationListener = new PaginationListener(settingsGUI.getPagination(),settingsGUI.getitemManager());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {

        if (!event.getView().getTitle().equals("Settings")) {
            return;
        }

        if (!event.isLeftClick()) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();
        Player player = (Player) event.getWhoClicked();
        if (itemName.equals("Next Page")) {
            paginationListener.onNextPage(player);
            settingsGUI.GUI(player);
            return;
        } else if (itemName.equals("Previous Page")) {
            paginationListener.onPreviousPage(player);
            settingsGUI.GUI(player);
            return;
        }

        Inventory guiInventory = settingsGUI.getInventory();
        if (event.getView().getTopInventory().equals(guiInventory)) {
            int slot = event.getSlot();
            ItemStack clickedItems = guiInventory.getItem(slot);
            if (clickedItems != null) {

                String displayName = clickedItem.getItemMeta().getDisplayName();
                String[] parts = displayName.split(" ");
                String settingName = "";
                boolean settingEnabled = false;
                if (parts.length >= 2) {
                    settingName = ChatColor.stripColor(parts[0]);
                    String settingStatus = ChatColor.stripColor(parts[1]);
                    settingEnabled = settingStatus.equalsIgnoreCase("Enabled");
                }

                if (config.getConfig().getConfigurationSection("permissions").contains(settingName)) {
                    config.getConfig().set("permissions." + settingName, !settingEnabled);
                } else if (config.getConfig().getConfigurationSection("inventory").contains(settingName)) {
                    config.getConfig().set("inventory." + settingName, !settingEnabled);
                }

                config.getConfig().save(plugin.getDataFolder() + "/config.yml");

                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(langManager.getMessage("gui.settingsgui.success", config.getConfig().getString("lang")).replace("{settingName}", settingName).replace("{settingStatus}", (!settingEnabled) ? "Enabled" : "Disabled"));

            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(settingsGUI.getInventory())) {
            settingsGUI.GUI((Player) event.getPlayer()); // Update the inventory here
        }
    }
}