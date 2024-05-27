package beta.com.permissionscreative.events;

import beta.com.permissionscreative.enums.ThrowItems;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class PlayerInteract implements Listener {
    private final Config config;
    private final LangManager langManager;

    public PlayerInteract(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material itemInHand = player.getItemInHand().getType();

        if (player.getGameMode() == GameMode.CREATIVE && ThrowItems.isThrowItem(itemInHand)) {
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
            if (config.getConfig().getBoolean("permissions.throw") && !player.hasPermission("permissionscreative.throw.bypass")) {
                event.setCancelled(true);
                String message = langManager.getMessage("events.throw", config.getConfig().getString("lang"));
                player.sendMessage(prefix + " "  + message);
            }
        }
    }
}
