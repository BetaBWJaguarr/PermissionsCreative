package beta.com.permissionscreative.gui.listmode.listener;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.PaginationManager;
import beta.com.permissionscreative.gui.listmode.ListModeGUI;
import beta.com.permissionscreative.gui.listmode.editmenu.EditGUI;
import beta.com.permissionscreative.gui.listmode.editmenu.listener.EditGUIListener;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The ListModeGUIListener class is responsible for handling interactions with the List Mode GUI in a Bukkit/Spigot Minecraft plugin.
 * It listens for inventory click events within the List Mode GUI and performs actions based on the player's selection.
 *
 * <p>When a player clicks on an item in the List Mode GUI, the listener checks if the item corresponds to one of the predefined modes.
 * If a valid mode is selected, the listener stores the player's selection, initializes an EditGUI, and registers an EditGUIListener
 * to handle interactions within the edit menu.</p>
 *
 * <p>This class utilizes the following dependencies:
 * <ul>
 *     <li>{@link ListModeGUI} - Represents the List Mode GUI inventory.</li>
 *     <li>{@link PaginationManager} - Manages pagination for the List Mode GUI.</li>
 *     <li>{@link Config} - Handles configuration settings for the plugin.</li>
 *     <li>{@link LangManager} - Manages language and localization for the plugin.</li>
 *     <li>{@link Plugin} - Represents the Bukkit plugin instance.</li>
 * </ul>
 * </p>
 *
 * <p>The listener also maintains a mapping of display names to mode keys and stores player selections in a HashMap.</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Handles {@link InventoryClickEvent} to detect and process clicks within the List Mode GUI.</li>
 *     <li>Maps clicked items to specific modes using the {@link #modeMapping}.</li>
 *     <li>Registers the {@link EditGUIListener} for further interaction handling within the edit menu.</li>
 *     <li>Stores and retrieves player mode selections using a {@link HashMap} with UUID keys.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * ListModeGUIListener listener = new ListModeGUIListener(listModeGUI, paginationManager, config, langManager, plugin);
 * plugin.getServer().getPluginManager().registerEvents(listener, plugin);
 * }
 * </pre>
 * </p>
 */


public class ListModeGUIListener implements Listener {
    private final ListModeGUI listModeGUI;
    private final HashMap<UUID, String> playerSelections;
    private final PaginationManager listModeGUIS;

    private final Config config;
    private final LangManager langManager;
    private final Plugin plugin;

    private final Map<String, String> modeMapping;

    public ListModeGUIListener(ListModeGUI listModeGUI1, PaginationManager listModeGUIS, Config config, LangManager langManager, Plugin plugin) {
        this.listModeGUI = listModeGUI1;
        this.listModeGUIS = listModeGUIS;
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
        this.playerSelections = new HashMap<>();

        modeMapping = new HashMap<>();
        modeMapping.put("Build Mode", "build");
        modeMapping.put("Throw Mode", "throw");
        modeMapping.put("Drop Mode", "drop");
        modeMapping.put("Break Mode", "break");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(listModeGUI.getInventory())) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if (modeMapping.containsKey(itemName)) {
            String modeKey = modeMapping.get(itemName);
            playerSelections.put(player.getUniqueId(), modeKey);
            EditGUI editGUI = new EditGUI();
            plugin.getServer().getPluginManager().registerEvents(new EditGUIListener(this, config, langManager, plugin, listModeGUIS,editGUI), plugin);
            player.openInventory(editGUI.getInventory());
        }

        event.setCancelled(true);
    }

    public String getPlayerSelection(UUID playerId) {
        return playerSelections.get(playerId);
    }
}
