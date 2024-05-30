package beta.com.permissionscreative.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class EntityDamageByEntity implements Listener {
    private final Config config;
    private final LangManager langManager;

    public EntityDamageByEntity(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
            if (config.getConfig().getBoolean("permissions.pve") && player.getGameMode() == GameMode.CREATIVE) {
                if (!player.hasPermission("permissionscreative.pve.bypass")) {
                    event.setCancelled(true);
                    player.sendMessage(prefix +  " " + langManager.getMessage("events.pve", config.getConfig().getString("lang")));
                }
            }
        }
    }
}