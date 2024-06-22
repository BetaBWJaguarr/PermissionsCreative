package beta.com.permissionscreative.gui.listener;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.SettingsGUI;
import beta.com.permissionscreative.gui.worldsregions.WorldGuardGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.ChoicesGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.ListGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.PaginationManager;
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

import java.util.UUID;


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
                plugin.getServer().getPluginManager().registerEvents(new SettingsGUIListener(settingsGUI, config, plugin,langManager), plugin);
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


    @EventHandler
    public void onInventoryClick2(InventoryClickEvent event) {
        ListGUI listGUI = new ListGUI(config,paginationManager.getPaginationService());
        plugin.getServer().getPluginManager().registerEvents(new ListGUIListener(listGUI), plugin);


        if (event.getInventory().equals(choicesGUI.getInventory())) {
            Material type = event.getCurrentItem().getType();
            Player player = (Player) event.getWhoClicked();
            playerUUID = player.getUniqueId();
            if (type == Material.GREEN_WOOL) {
                isWaitingForInput = true;
                ((Player) event.getWhoClicked()).sendMessage(ChatColor.GREEN + "Please type your which region add");
                player.closeInventory();
            } else if (type == Material.RED_WOOL) {
                String lastSelection = choicesGUI.getWorldGuardGUI().getLastSelection(playerUUID);
                listGUI.createMenu(lastSelection);
                listGUI.openMenu((Player) event.getWhoClicked());
            }
        }
    }



    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (isWaitingForInput && event.getPlayer().getUniqueId().equals(playerUUID)) {
            isWaitingForInput = false;
            String regionToAdd = event.getMessage();
            event.setCancelled(true);
        }
    }
}