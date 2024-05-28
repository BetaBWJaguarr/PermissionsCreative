package beta.com.permissionscreative.gui.listener;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.SettingsGUI;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class SettingsGUIListener implements Listener {
    private SettingsGUI settingsGUI;
    private Config config;
    private Plugin plugin;
    private LangManager langManager;

    public SettingsGUIListener(SettingsGUI settingsGUI, Config config, Plugin plugin, LangManager langManager) {
        this.settingsGUI = settingsGUI;
        this.config = config;
        this.plugin = plugin;
        this.langManager = langManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {

        if (!event.getView().getTitle().equals("Settings")) {
            return;
        }

        Inventory guiInventory = settingsGUI.getInventory();
        if (event.getView().getTopInventory().equals(guiInventory)) {
            int slot = event.getSlot();
            ItemStack clickedItem = guiInventory.getItem(slot);
            if (clickedItem != null) {

                String displayName = clickedItem.getItemMeta().getDisplayName();
                String[] parts = displayName.split(" ");
                String settingName = "";
                boolean permissionEnabled = false;
                if (parts.length >= 2) {
                    settingName = ChatColor.stripColor(parts[0]);
                    String settingStatus = ChatColor.stripColor(parts[1]);
                    permissionEnabled = settingStatus.equalsIgnoreCase("Enabled");
                }

                config.getConfig().set("permissions." + settingName, !permissionEnabled);
                config.getConfig().save(plugin.getDataFolder() + "/config.yml");

                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(langManager.getMessage("gui.settingsgui.success", config.getConfig().getString("lang")).replace("{settingName}", settingName).replace("{settingStatus}", (!permissionEnabled) ? "Enabled" : "Disabled"));

            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(settingsGUI.getInventory())) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }
}