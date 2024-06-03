package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.Logger;

public class DropItem implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public DropItem(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.drop"), "permissionscreative.drop.bypass", "events.dropitem");
        if (cancel) {
            event.setCancelled(true);
            logger.log("discord.events.drop.actions", "discord.events.drop.message", player, discordLogAction);
        }
    }
}
