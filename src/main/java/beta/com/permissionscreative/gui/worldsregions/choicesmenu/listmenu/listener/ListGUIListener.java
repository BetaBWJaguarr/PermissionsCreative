package beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.listener;

import beta.com.paginationapi.listener.PaginationListener;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.worldsregions.WorldGuardGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.ListGUI;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.List;

/**
 * The ListGUIListener class handles interactions with the ListGUI in a Bukkit/Spigot Minecraft plugin, particularly focusing on
 * pagination and region management within the GUI.
 *
 * <p>This listener responds to inventory click events within the ListGUI, allowing players to navigate through pages or
 * manage regions. It leverages the PaginationListener to handle page transitions and interacts with the configuration
 * to update region data.</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Handles {@link InventoryClickEvent} to detect and process clicks within the ListGUI inventory.</li>
 *     <li>Manages pagination actions using {@link PaginationListener}, allowing players to navigate to the next or previous page.</li>
 *     <li>Interacts with {@link WorldGuardGUI} to retrieve the last region selection made by the player.</li>
 *     <li>Updates the configuration file to remove selected regions and provides feedback to the player via messages from {@link LangManager}.</li>
 * </ul>
 * </p>
 *
 * <p>This class utilizes the following dependencies:
 * <ul>
 *     <li>{@link ListGUI} - Represents the List Mode GUI inventory.</li>
 *     <li>{@link PaginationListener} - Manages pagination actions for the ListGUI.</li>
 *     <li>{@link WorldGuardGUI} - Handles region-related operations and selections.</li>
 *     <li>{@link Config} - Handles configuration settings for the plugin.</li>
 *     <li>{@link LangManager} - Manages language and localization for the plugin.</li>
 *     <li>{@link Plugin} - Represents the Bukkit plugin instance.</li>
 * </ul>
 * </p>
 *
 * <p>Methods Overview:
 * <ul>
 *     <li>{@link #onInventoryClick(InventoryClickEvent)} - The main event handler that processes inventory click events,
 *         manages pagination, and updates region configurations.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * ListGUIListener listener = new ListGUIListener(listGUI, worldGuardGUI, plugin, config, langManager);
 * plugin.getServer().getPluginManager().registerEvents(listener, plugin);
 * }
 * </pre>
 * </p>
 */


public class ListGUIListener implements Listener {
    private final ListGUI listGUI;
    private final PaginationListener paginationListener;
    private final WorldGuardGUI worldGuardGUI;
    private final Plugin plugin;
    private final Config config;
    private final LangManager langManager;

    public ListGUIListener(ListGUI listGUI, WorldGuardGUI worldGuardGUI, Plugin plugin, Config config, LangManager langManager) {
        this.listGUI = listGUI;
        this.paginationListener = new PaginationListener(listGUI.getPagination(), listGUI.getItemManager());
        this.worldGuardGUI = worldGuardGUI;
        this.plugin = plugin;
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if (!event.getInventory().equals(listGUI.getInventory())) {
            return;
        }
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();
        Player player = (Player) event.getWhoClicked();

        switch (itemName) {
            case "Next Page":
                paginationListener.onPageAction(player, true);
                listGUI.openMenu(player);
                break;
            case "Previous Page":
                paginationListener.onPageAction(player, false);
                listGUI.openMenu(player);
                break;
            default:
                handleRegionSelection(player, itemName);
                break;
        }
    }

    private void handleRegionSelection(Player player, String regionName) throws IOException {
        String lastSelection = worldGuardGUI.getLastSelection(player.getUniqueId());
        List<String> regions = config.getConfig().getStringList(lastSelection + ".regions");

        if (regions.remove(regionName)) {
            config.getConfig().set(lastSelection + ".regions", regions);
            config.getConfig().save(plugin.getDataFolder() + "/config.yml");

            String message = langManager.getMessage("gui.listgui.remove.success", config.getConfig().getString("lang"));
            player.sendMessage(message.replace("{region}", regionName));
        } else {
            String message = langManager.getMessage("gui.listgui.remove.failure", config.getConfig().getString("lang"));
            player.sendMessage(message.replace("{region}", regionName));
        }

        player.closeInventory();
    }
}