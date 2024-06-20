package beta.com.permissionscreative.gui.listener;

import beta.com.permissionscreative.gui.SettingsGUI;
import beta.com.permissionscreative.gui.worldsregions.WorldGuardGUI;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.ChoicesGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;


public class WorldGuardGUIListener implements Listener {
    private final WorldGuardGUI worldGuardGUI;
    private final Plugin plugin;
    private final ChoicesGUI choicesGUI;
    private final SettingsGUI settingsGUI;

    public WorldGuardGUIListener(WorldGuardGUI worldGuardGUI, Plugin plugin, ChoicesGUI choicesGUI, SettingsGUI settingsGUI) {
        this.worldGuardGUI = worldGuardGUI;
        this.plugin = plugin;
        this.choicesGUI = choicesGUI;
        this.settingsGUI = settingsGUI;
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
            settingsGUI.GUI((Player) event.getWhoClicked());
        }

        if (event.getCurrentItem() != null &&
                (event.getCurrentItem().getType() == Material.REDSTONE ||
                        event.getCurrentItem().getType() == Material.GRASS_BLOCK)) {
            choicesGUI.open((Player) event.getWhoClicked());
        }
    }
}