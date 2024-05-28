package beta.com.permissionscreative.gui;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.listener.SettingsGUIListener;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Random;

public class SettingsGUI {

    private Inventory inventory;
    private Config config;
    private LangManager langManager;

    public SettingsGUI(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }


    private void GUI() {
        inventory = Bukkit.createInventory(null, 9, "Settings");


        Map<String, Object> settings = config.getConfig().getConfigurationSection("permissions").getValues(false);
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            String settingName = entry.getKey();
            boolean settingEnabled = config.getConfig().getBoolean("permissions." + settingName);

            ItemStack item = new ItemStack(ItemsEnum.values()[new Random().nextInt(ItemsEnum.values().length)].getMaterial());
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.BLUE + settingName + " " + (settingEnabled ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            item.setItemMeta(itemMeta);

            inventory.addItem(item);

            int slot = inventory.first(item);
            inventory.setItem(slot, item);
        }
    }

    public void open(Plugin plugin, Player player) {
        GUI();
        plugin.getServer().getPluginManager().registerEvents(new SettingsGUIListener(this, config, plugin,langManager), plugin);
        player.openInventory(inventory);
    }



    public Inventory getInventory() {
        return inventory;
    }

}