package beta.com.permissionscreative.events;

import beta.com.permissionscreative.utils.EventsManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class PlayerDamage implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;

    public PlayerDamage(Config config, LangManager langManager, EventsManager eventsManager) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getDamager();
            boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.pvp"), "permissionscreative.pvp.bypass", "events.pvp");
            if (cancel) {
                event.setCancelled(true);
            }
        }
    }
}
