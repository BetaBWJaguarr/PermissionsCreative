package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandsEvent implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public CommandsEvent(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().toLowerCase().startsWith("/permissions-creative")
                && eventsManager.checkProtection(event.getPlayer(), eventsManager.checkWorlds(), eventsManager.WorldguardCheck(event.getPlayer()))
                && eventsManager.checkAndSendMessage(event.getPlayer(), GameMode.CREATIVE, config.getConfig().getBoolean("permissions.commands"), "permissionscreative.commands.bypass", "events.commands","","")) {

            event.setCancelled(true);
            logger.log("discord.events.commands.actions", "discord.events.commands.message", event.getPlayer(), discordLogAction);
        }
    }
}