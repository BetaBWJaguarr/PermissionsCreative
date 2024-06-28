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

public class EditGUIListener implements Listener {
    private final ListModeGUIListener listModeGUIListener;
    private final Config config;
    private final LangManager langManager;
    private final Plugin plugin;
    private final PaginationManager ListModeGUI;
    private final EditGUI editGUI;

    private HashMap<UUID, Boolean> addingItemFlags = new HashMap<>();

    public EditGUIListener(ListModeGUIListener listModeGUIListener, Config config, LangManager langManager, Plugin plugin, PaginationManager listModeGUI, EditGUI editGUI) {
        this.listModeGUIListener = listModeGUIListener;
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
        this.ListModeGUI = listModeGUI;
        this.editGUI = editGUI;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IOException {
        if (!event.getInventory().equals(editGUI.getInventory())) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if (itemName.equals("Change")) {
            String playerSelection = listModeGUIListener.getPlayerSelection(player.getUniqueId());
            String currentStatus = getModeStatus(playerSelection);
            String newStatus = currentStatus.equals("blacklist") ? "whitelist" : "blacklist";
            setModeStatus(playerSelection, newStatus);


            String message = langManager.getMessage("gui.editgui.modeChange", config.getConfig().getString("lang"))
                    .replace("{mode}", playerSelection)
                    .replace("{newStatus}", newStatus);
            player.sendMessage(message);
        } else if (itemName.equals("Add")) {
            setAddingItem(player.getUniqueId(), true);
            player.sendMessage("Please type the item you want to add in the chat.");
            player.closeInventory();
        } else if (itemName.equals("Remove")) {
            EditListGUI editListGUI = new EditListGUI(ListModeGUI,listModeGUIListener,config);
            EditListGUIListener editListGUIListener = new EditListGUIListener(editListGUI);
            plugin.getServer().getPluginManager().registerEvents(editListGUIListener, plugin);
            editListGUI.GUI(player);
        }


        event.setCancelled(true);
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

            player.sendMessage("Item " + itemToAdd + " has been added to " + playerSelection + ".");

        }
    }


    public void setAddingItem(UUID playerId, boolean isAdding) {
        addingItemFlags.put(playerId, isAdding);
    }

    public boolean isAddingItem(UUID playerId) {
        return addingItemFlags.getOrDefault(playerId, false);
    }

}
