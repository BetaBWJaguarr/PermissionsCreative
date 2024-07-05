package beta.com.permissionscreative.gui.listmode.editmenu.listener;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.PaginationManager;
import beta.com.permissionscreative.gui.listmode.editmenu.EditGUI;
import beta.com.permissionscreative.gui.listmode.editmenu.listmenu.EditListGUI;
import beta.com.permissionscreative.gui.listmode.editmenu.listmenu.listener.EditListGUIListener;
import beta.com.permissionscreative.gui.listmode.listener.ListModeGUIListener;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * The EditGUIListener class handles interactions with the EditGUI in a Bukkit/Spigot Minecraft plugin,
 * specifically managing inventory clicks and player chat events related to editing modes and lists.
 *
 * <p>This listener responds to inventory click events within the EditGUI, allowing players to change mode status,
 * add items to lists, and remove items via interactions with the EditListGUI. It utilizes a PaginationManager
 * for GUI navigation and interacts with configuration settings and language messages using Config and LangManager.</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Handles {@link InventoryClickEvent} to detect and process clicks within the EditGUI inventory.</li>
 *     <li>Manages mode change actions, list item addition, and removal using {@link EditListGUI} and {@link EditListGUIListener}.</li>
 *     <li>Interacts with {@link ListModeGUIListener} to retrieve player selections and {@link PaginationManager} for GUI navigation.</li>
 *     <li>Updates configuration settings for mode statuses and list items, saving changes to config.yml.</li>
 *     <li>Uses {@link LangManager} to retrieve and display localized messages to players.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * EditGUIListener listener = new EditGUIListener(listModeGUIListener, config, langManager, plugin, listModeGUI, editGUI);
 * plugin.getServer().getPluginManager().registerEvents(listener, plugin);
 * }
 * </pre>
 * </p>
 */

public class EditGUIListener implements Listener {
    private final ListModeGUIListener listModeGUIListener;
    private final Config config;
    private final LangManager langManager;
    private final Plugin plugin;
    private final PaginationManager listModeGUI;
    private final EditGUI editGUI;
    private final HashMap<UUID, Boolean> addingItemFlags = new HashMap<>();

    public EditGUIListener(ListModeGUIListener listModeGUIListener, Config config, LangManager langManager, Plugin plugin, PaginationManager listModeGUI, EditGUI editGUI) {
        this.listModeGUIListener = listModeGUIListener;
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
        this.listModeGUI = listModeGUI;
        this.editGUI = editGUI;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if (!event.getInventory().equals(editGUI.getInventory())) {
            return;
        }

        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        switch (itemName) {
            case "Change":
                handleChangeClick(player);
                break;
            case "Add":
                handleAddClick(player);
                break;
            case "Remove":
                handleRemoveClick(player);
                break;
            default:
                break;
        }
    }

    private void handleChangeClick(Player player) throws IOException {
        String playerSelection = listModeGUIListener.getPlayerSelection(player.getUniqueId());
        String currentStatus = getModeStatus(playerSelection);
        String newStatus = currentStatus.equals("blacklist") ? "whitelist" : "blacklist";
        setModeStatus(playerSelection, newStatus);

        String message = langManager.getMessage("gui.editgui.modechange", config.getConfig().getString("lang"))
                .replace("{mode}", playerSelection)
                .replace("{newStatus}", newStatus);
        player.sendMessage(message);
        player.closeInventory();
    }

    private void handleAddClick(Player player) {
        setAddingItem(player.getUniqueId(), true);
        String message = langManager.getMessage("gui.editgui.type", config.getConfig().getString("lang"));
        player.sendMessage(message);
        player.closeInventory();
    }

    private void handleRemoveClick(Player player) {
        EditListGUI editListGUI = new EditListGUI(listModeGUI, listModeGUIListener, config);
        EditListGUIListener editListGUIListener = new EditListGUIListener(editListGUI, langManager, config);
        plugin.getServer().getPluginManager().registerEvents(editListGUIListener, plugin);
        editListGUI.GUI(player);
    }

    private String getModeStatus(String mode) {
        String status = config.getConfig().getString("list.mode." + mode);
        return (status == null || status.isEmpty()) ? "blacklist" : status;
    }

    private void setModeStatus(String mode, String status) throws IOException {
        config.getConfig().set("list.mode." + mode, status);
        config.getConfig().save(plugin.getDataFolder() + "/config.yml");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws IOException {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (isAddingItem(playerId)) {
            event.setCancelled(true);
            setAddingItem(playerId, false);

            String playerSelection = listModeGUIListener.getPlayerSelection(playerId);
            String itemToAdd = event.getMessage();

            List<String> items = config.getConfig().getStringList("list." + playerSelection);
            if (items.contains("")) {
                items.remove("");
            }
            items.add(itemToAdd);
            config.getConfig().set("list." + playerSelection, items);
            config.getConfig().save(plugin.getDataFolder() + "/config.yml");

            String message = langManager.getMessage("gui.editgui.added", config.getConfig().getString("lang"))
                    .replace("{item}", itemToAdd)
                    .replace("{selection}", playerSelection);
            player.sendMessage(message);
        }
    }

    public void setAddingItem(UUID playerId, boolean isAdding) {
        addingItemFlags.put(playerId, isAdding);
    }

    public boolean isAddingItem(UUID playerId) {
        return addingItemFlags.getOrDefault(playerId, false);
    }
}
