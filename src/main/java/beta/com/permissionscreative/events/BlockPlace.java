package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public BlockPlace(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!eventsManager.checkProtection(event.getPlayer(), eventsManager.checkWorlds(), eventsManager.WorldguardCheck(event.getPlayer()))) {
            return;
        }

        if (eventsManager.checkAndSendMessage(event.getPlayer(), GameMode.CREATIVE, config.getConfig().getBoolean("permissions.build"), "permissionscreative.build.bypass", "events.blockplace", "build", event.getBlock().getType().toString())) {
            event.setCancelled(true);
            logger.log("discord.events.blockplace.actions", "discord.events.blockplace.message", event.getPlayer(), discordLogAction);
        }
    }
}