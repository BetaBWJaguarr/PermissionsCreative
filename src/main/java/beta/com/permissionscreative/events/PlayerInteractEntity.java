package beta.com.permissionscreative.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class PlayerInteractEntity implements Listener {
    private final Config config;
    private final LangManager langManager;

    public PlayerInteractEntity(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        handleInteractEvent(event);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        handleInteractEvent(event);
    }

    private void handleInteractEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
        if (config.getConfig().getBoolean("permissions.entity") && player.getGameMode() == GameMode.CREATIVE) {
            if (!player.hasPermission("permissionscreative.entity.bypass")) {
                event.setCancelled(true);
                player.sendMessage(prefix +  " " + langManager.getMessage("events.entity", config.getConfig().getString("lang")));
            }
        }
    }
}