package beta.com.permissionscreative.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class CommandsEvent implements Listener {
    private final Config config;
    private final LangManager langManager;

    public CommandsEvent(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));

        if (player.getGameMode() == GameMode.CREATIVE) {
            if (config.getConfig().getBoolean("permissions.commands") && !player.hasPermission("permissionscreative.commands.bypass")) {
                event.setCancelled(true);
                String message = langManager.getMessage("events.commands", config.getConfig().getString("lang"));
                player.sendMessage(prefix + " " + message);
            }
        }
    }
}
