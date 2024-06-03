package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.Logger;

public class CommandsEvent implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public CommandsEvent(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("/permissions-creative")) {
            return;
        }

        Player player = event.getPlayer();
        boolean shouldCancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.commands"), "permissionscreative.commands.bypass", "events.commands");
        if (shouldCancel) {
            event.setCancelled(true);
            logger.log("discord.events.commands.actions", "discord.events.commands.message", player, discordLogAction);
        }
    }
}
