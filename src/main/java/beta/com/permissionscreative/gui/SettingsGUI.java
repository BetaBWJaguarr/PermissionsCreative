package beta.com.permissionscreative.gui;

import beta.com.paginationapi.itemmanager.ItemManager;
import beta.com.paginationapi.navigation.Navigation;
import beta.com.paginationapi.page.Pagination;
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
 * for the game settings. It uses the Bukkit library to create an inventory interface, which represents the settings menu.
 *
 * Each setting is represented as an item in the inventory. The state of the setting (enabled or
 * disabled) is indicated by the color of the item's name (green for enabled, red for disabled).
 *
 * The items used to represent the settings are determined by the 'random_item' setting in the config.yml file.
 * If 'random_item' is true, the items are chosen randomly from the ItemsEnum enumeration.
 * If 'random_item' is false, the items are used in the order they are defined in the enumeration.
 *
 * When the GUI is opened, a SettingsGUIListener is registered to handle the player's interactions
 * with the GUI.
 *
 * This class uses instances of the Config and LangManager classes to access the game's configuration
 * settings and language-specific messages, respectively.
 *
 * The GUI method creates the inventory and populates it with items representing the settings.
 * The open method opens the GUI for a player and registers the SettingsGUIListener.
 */


public class SettingsGUI {

    private Inventory inventory;
    private Config config;
    private LangManager langManager;
    private Pagination pagination;
    private SettingsGUIListener settingsGUIListener;
    private Navigation navigation;

    public SettingsGUI(Config config, LangManager langManager, Plugin plugin,Pagination pagination) {
        this.pagination = pagination;
        this.navigation = new Navigation(pagination);
        this.settingsGUIListener = new SettingsGUIListener(this, config, plugin, langManager);
        this.config = config;
        this.langManager = langManager;
    }


    private ItemStack createSettingItem(String settingName, boolean settingEnabled, List<ItemsEnum> materials, Material defaultMaterial) {
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

        return item;
    }

    public void GUI(Player player) {
        List<ItemsEnum> materials = new ArrayList<>(Arrays.asList(ItemsEnum.values()));
        Material defaultMaterial = Material.getMaterial(config.getConfig().getString("gui.default_item"));

        Map<String, Object> permissionsSettings = config.getConfig().getConfigurationSection("permissions").getValues(false);
        Map<String, Object> inventorySettings = config.getConfig().getConfigurationSection("inventory").getValues(false);

        List<ItemStack> items = new ArrayList<>();
        for (Map.Entry<String, Object> entry : permissionsSettings.entrySet()) {
            String settingName = entry.getKey();
            if (settingName.equals("plugins")) {
                continue;
            }
            boolean settingEnabled = config.getConfig().getBoolean("permissions." + settingName);
            items.add(createSettingItem(settingName, settingEnabled, materials, defaultMaterial));
        }

        for (Map.Entry<String, Object> entry : inventorySettings.entrySet()) {
            String settingName = entry.getKey();
            boolean settingEnabled = config.getConfig().getBoolean("inventory." + settingName);
            items.add(createSettingItem(settingName, settingEnabled, materials, defaultMaterial));
        }



        inventory = Bukkit.createInventory(null, 9, "Settings");

        inventory.clear();
        pagination.getItemManager().clearItems();

        addItemsToInventory(items);

        UUID playerId = player.getUniqueId();

        int page = pagination.getCurrentPageForPlayer(playerId);
        pagination.setPageForPlayer(playerId, page);

        int slot = pagination.hasPreviousPage(playerId) ? 1 : 0;
        for (ItemStack item : pagination.getCurrentPageItems(playerId)) {
            inventory.setItem(slot, item);
            slot++;
        }

        if (pagination.hasNextPage(playerId)) {
            inventory.setItem(8, navigation.createNextPageButton(playerId));
        }

        if (pagination.hasPreviousPage(playerId)) {
            inventory.setItem(0, navigation.createPreviousPageButton(playerId));
        }

        player.openInventory(inventory);
    }

    public void addItemsToInventory(List<ItemStack> items) {
        for (ItemStack item : items) {
            pagination.getItemManager().addItem(item);
        }
    }

    public void open(Plugin plugin, Player player) {
        GUI(player);
        plugin.getServer().getPluginManager().registerEvents(settingsGUIListener, plugin);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public ItemManager getitemManager() {
        return pagination.getItemManager();
    }
}