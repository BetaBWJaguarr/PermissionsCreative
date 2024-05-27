package beta.com.permissionscreative.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class BlockPlace implements Listener {
    private final Config config;
    private final LangManager langManager;

    public BlockPlace(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
        if (player.getGameMode() == GameMode.CREATIVE && config.getConfig().getBoolean("permissions.build") && !player.hasPermission("permissionscreative.build.bypass")) {
            event.setCancelled(true);
            String message = langManager.getMessage("events.blockplace", config.getConfig().getString("lang"));
            player.sendMessage(prefix + " " + message);
        }
    }
}
