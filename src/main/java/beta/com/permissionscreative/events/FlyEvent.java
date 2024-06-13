package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyEvent implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public FlyEvent(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().isOnGround()) {
            handleFlightEvent(event.getPlayer(), event.getPlayer().isFlying());
        }
    }

    private void handleFlightEvent(Player player, boolean isFlying) {
        if (!eventsManager.checkProtection(player, eventsManager.checkWorlds(), eventsManager.WorldguardCheck(player))) {
            return;
        }

        if (isFlying && eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.fly"), "permissionscreative.fly.bypass", "events.fly", "", "")) {
            player.setFlying(false);
            logger.log("discord.events.fly.actions", "discord.events.fly.message", player, discordLogAction);
        }
    }
}