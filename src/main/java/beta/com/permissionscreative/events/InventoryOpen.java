package beta.com.permissionscreative.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class InventoryOpen implements Listener {
    private final Config config;
    private final LangManager langManager;

    public InventoryOpen(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
            if (config.getConfig().getBoolean("permissions.gui")) {
                if (!player.hasPermission("permissionscreative.bypass.gui") && player.getGameMode() == GameMode.CREATIVE) {
                    event.setCancelled(true);
                    player.sendMessage(prefix +  " " + langManager.getMessage("events.gui-disabled", config.getConfig().getString("lang")));
                }
            }
        }
    }
}