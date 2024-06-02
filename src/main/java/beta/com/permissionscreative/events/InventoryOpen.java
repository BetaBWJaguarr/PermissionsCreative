package beta.com.permissionscreative.events;

import beta.com.permissionscreative.utils.EventsManager;
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
    private final EventsManager eventsManager;

    public InventoryOpen(Config config, LangManager langManager, EventsManager eventsManager) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.gui"), "permissionscreative.bypass.gui", "events.gui-disabled");
            if (cancel) {
                event.setCancelled(true);
            }
        }
    }
}