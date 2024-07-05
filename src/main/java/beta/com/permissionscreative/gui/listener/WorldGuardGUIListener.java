package beta.com.permissionscreative.gui.listener;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.SettingsGUI;
import beta.com.permissionscreative.gui.worldsregions.WorldGuardGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.ChoicesGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.ListGUI;
import beta.com.permissionscreative.gui.PaginationManager;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.listener.ListGUIListener;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * The WorldGuardGUIListener class manages interactions with GUIs related to WorldGuard settings and regions
 * within a Bukkit/Spigot Minecraft plugin. It handles inventory clicks, player chat events for input,
 * and updates to configuration settings.
 *
 * <p>This listener responds to inventory click events within the WorldGuardGUI and ChoicesGUI,
 * facilitating navigation, toggling settings, and adding regions. It utilizes a PaginationManager
 * for GUI navigation and interacts with configuration settings and language messages using Config and LangManager.</p>
 *
 * <p><strong>Key Responsibilities:</strong>
 * <ul>
 *     <li>Responds to {@link InventoryClickEvent}s in the WorldGuardGUI and ChoicesGUI to manage interaction.</li>
 *     <li>Manages settings toggles and region additions using {@link ListGUI} and {@link ListGUIListener}.</li>
 *     <li>Updates configuration settings for WorldGuard regions and toggles, saving changes to config.yml.</li>
 *     <li>Utilizes {@link LangManager} to fetch and display localized messages for player feedback.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * WorldGuardGUIListener listener = new WorldGuardGUIListener(worldGuardGUI, plugin, settingsGUI, config, paginationManager, langManager);
 * plugin.getServer().getPluginManager().registerEvents(listener, plugin);
 * }
 * </pre>
 * </p>
 *
 * <p><strong>Interaction Details:</strong>
 * <ul>
 *     <li>Handles clicking on specific items in the GUI to trigger actions like opening menus or toggling settings.</li>
 *     <li>Manages chat input from players to add new regions when prompted by ChoicesGUI actions.</li>
 *     <li>Updates and saves configuration changes to reflect updated WorldGuard settings and region lists.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Note:</strong> This class assumes the presence of related GUIs (WorldGuardGUI, ChoicesGUI),
 * PaginationManager for navigation, and properly configured instances of Config and LangManager for
 * configuration and language handling respectively.</p>
 */

public class WorldGuardGUIListener implements Listener {
    private final WorldGuardGUI worldGuardGUI;
    private final Plugin plugin;
    private final ChoicesGUI choicesGUI;
    private final SettingsGUI settingsGUI;
    private final Config config;
    private final PaginationManager paginationManager;
    private final LangManager langManager;
    private UUID playerUUID;
    private boolean isWaitingForInput = false;


    public WorldGuardGUIListener(WorldGuardGUI worldGuardGUI, Plugin plugin, SettingsGUI settingsGUI, Config config, PaginationManager paginationManager, LangManager langManager) {
        this.worldGuardGUI = worldGuardGUI;
        this.plugin = plugin;
        this.choicesGUI = new ChoicesGUI(worldGuardGUI);
        this.settingsGUI = settingsGUI;
        this.config = config;
        this.paginationManager = paginationManager;
        this.langManager = langManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    //ChoicesGUI and WorldGuardGUI Listener
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(worldGuardGUI.getInventory()) || event.getInventory().equals(choicesGUI.getInventory())) {
            event.setCancelled(true);
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().getType() == Material.ARROW) {
            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta.hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equals("Back to Main Menu")) {
                settingsGUI.GUI((Player) event.getWhoClicked());
                plugin.getServer().getPluginManager().registerEvents(new SettingsGUIListener(settingsGUI, config, plugin, langManager), plugin);
            }
        }

        if (event.getCurrentItem() != null) {
            Material type = event.getCurrentItem().getType();
            UUID playerUUID = event.getWhoClicked().getUniqueId();
            if (type == Material.REDSTONE) {
                worldGuardGUI.setLastSelection(playerUUID, "world-guard");
                choicesGUI.open((Player) event.getWhoClicked());
            } else if (type == Material.GRASS_BLOCK) {
                worldGuardGUI.setLastSelection(playerUUID, "worlds");
                choicesGUI.open((Player) event.getWhoClicked());
            }
        }
    }


    //Choices GUI Listener

    @EventHandler
    public void onInventoryClick2(InventoryClickEvent event) throws IOException {
        ListGUI listGUI = new ListGUI(config, paginationManager.getPaginationService());
        plugin.getServer().getPluginManager().registerEvents(new ListGUIListener(listGUI, worldGuardGUI, plugin, config,langManager), plugin);

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getInventory().equals(choicesGUI.getInventory())) {
            Material type = event.getCurrentItem().getType();
            Player player = (Player) event.getWhoClicked();
            playerUUID = player.getUniqueId();
            if (type == Material.GREEN_WOOL) {
                isWaitingForInput = true;
                ((Player) event.getWhoClicked()).sendMessage(langManager.getMessage("gui.choicesgui.add", config.getConfig().getString("lang")));
                player.closeInventory();
            } else if (type == Material.RED_WOOL) {
                String lastSelection = choicesGUI.getWorldGuardGUI().getLastSelection(playerUUID);
                listGUI.createMenu(lastSelection);
                listGUI.openMenu((Player) event.getWhoClicked());
            } else if (type == Material.LEVER) {
                String lastSelection = choicesGUI.getWorldGuardGUI().getLastSelection(playerUUID);

                boolean currentValue = config.getConfig().getBoolean(lastSelection + ".enabled");
                config.getConfig().set(lastSelection + ".enabled", !currentValue);
                config.getConfig().save(plugin.getDataFolder() + "/config.yml");


                String message = langManager.getMessage("gui.listgui.toggle.success", config.getConfig().getString("lang"));
                message = message.replace("{selection}", lastSelection).replace("{status}", (!currentValue) ? "Enabled" : "Disabled");
                player.sendMessage(message);

                player.closeInventory();
            }
        }
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws IOException {
        if (isWaitingForInput && event.getPlayer().getUniqueId().equals(playerUUID)) {
            isWaitingForInput = false;
            String regionToAdd = event.getMessage();
            event.setCancelled(true);

            String lastSelection = choicesGUI.getWorldGuardGUI().getLastSelection(playerUUID);

            List<String> regions = config.getConfig().getStringList(lastSelection + ".regions");

            if (!regions.contains(regionToAdd)) {
                regions.add(regionToAdd);

                config.getConfig().set(lastSelection + ".regions", regions);

                config.getConfig().save(plugin.getDataFolder() + "/config.yml");

                String message = langManager.getMessage("gui.listgui.add.success", config.getConfig().getString("lang"));
                message = message.replace("{region}", regionToAdd);
                event.getPlayer().sendMessage(message);
            } else {
                String message = langManager.getMessage("gui.listgui.add.failure", config.getConfig().getString("lang"));
                message = message.replace("{region}", regionToAdd);
                event.getPlayer().sendMessage(message);
            }
        }
    }
}