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


public class ListGUIListener implements Listener {
    private final ListGUI listGUI;
    private PaginationListener paginationListener;
    private WorldGuardGUI worldGuardGUI;
    private Plugin plugin;
    private Config config;
    private LangManager langManager;

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
        if (event.getInventory().equals(listGUI.getInventory())) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                if (itemName.equals("Next Page")) {
                    paginationListener.onPageAction(player, true);
                    listGUI.openMenu(player);
                } else if (itemName.equals("Previous Page")) {
                    paginationListener.onPageAction(player, false);
                    listGUI.openMenu(player);
                } else {
                    String lastSelection = worldGuardGUI.getLastSelection(player.getUniqueId());

                    List<String> regions = config.getConfig().getStringList(lastSelection + ".regions");

                    if (regions.remove(itemName)) {
                        config.getConfig().set(lastSelection + ".regions", regions);
                        config.getConfig().save(plugin.getDataFolder() + "/config.yml");

                        String message = langManager.getMessage("gui.listgui.remove.success", config.getConfig().getString("lang"));
                        message = message.replace("{region}", itemName);
                        player.sendMessage(message);
                    } else {
                        String message = langManager.getMessage("gui.listgui.remove.failure", config.getConfig().getString("lang"));
                        message = message.replace("{region}", itemName);
                        player.sendMessage(message);
                    }

                    player.closeInventory();
                }
            }
        }
    }
}