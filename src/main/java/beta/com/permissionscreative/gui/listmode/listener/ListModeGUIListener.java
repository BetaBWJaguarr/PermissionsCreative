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
