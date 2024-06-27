package beta.com.permissionscreative.gui.listmode.editmenu.listener;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.listmode.editmenu.EditGUI;
import beta.com.permissionscreative.gui.listmode.listener.ListModeGUIListener;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class EditGUIListener implements Listener {
    private final ListModeGUIListener listModeGUIListener;
    private final Config config;
    private final LangManager langManager;
    private final Plugin plugin;
    private final EditGUI editGUI;

    public EditGUIListener(ListModeGUIListener listModeGUIListener, Config config, LangManager langManager, Plugin plugin, EditGUI editGUI) {
        this.listModeGUIListener = listModeGUIListener;
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
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
        }

        player.closeInventory();

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
}
