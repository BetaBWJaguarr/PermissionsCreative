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

import java.util.*;

/**
 * The SettingsGUI class is responsible for creating and managing the graphical user interface (GUI)
 * for the settings in the game.
 * It uses the Bukkit library to create an inventory interface, which represents the settings menu.
 *
 * Each setting is represented as an item in the inventory, and the state of the setting (enabled or
 * disabled) is indicated by the color of the item's name (green for enabled, red for disabled).
 *
 * The items used to represent the settings can either be chosen randomly from the ItemsEnum
 * enumeration, or they can be used in the order they are defined in the enumeration, depending on the
 * value of the 'random_item' setting in the config.yml file.
 *
 * When the GUI is opened, a SettingsGUIListener is registered to handle the player's interactions
 * with the GUI.
 *
 * This class uses instances of the Config and LangManager classes to access the game's configuration
 * settings and language-specific messages, respectively.
 */

public class SettingsGUI {

    private Inventory inventory;
    private Config config;
    private LangManager langManager;

    public SettingsGUI(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }


    private void GUI() {
        List<ItemsEnum> materials = new ArrayList<>(Arrays.asList(ItemsEnum.values()));
        Material defaultMaterial = Material.getMaterial(config.getConfig().getString("gui.default_material"));
        inventory = Bukkit.createInventory(null, 9, "Settings");

        Map<String, Object> settings = config.getConfig().getConfigurationSection("permissions").getValues(false);
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            String settingName = entry.getKey();
            boolean settingEnabled = config.getConfig().getBoolean("permissions." + settingName);

            Material material;
            if (config.getConfig().getBoolean("gui.random_item")) {
                material = ItemsEnum.values()[new Random().nextInt(ItemsEnum.values().length)].getMaterial();
            } else {
                if (materials.isEmpty()) {
                    material = defaultMaterial;
                } else {
                    material = materials.get(0).getMaterial();
                    materials.remove(0);
                }
            }

            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.BLUE + settingName + " " + (settingEnabled ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            item.setItemMeta(itemMeta);

            inventory.addItem(item);
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