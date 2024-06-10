package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CreatureSpawn implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public CreatureSpawn(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!eventsManager.checkProtection(player, eventsManager.checkWorlds(), eventsManager.WorldguardCheck(player))) {
            return;
        }

        if (player.getItemInHand().getType().name().endsWith("_SPAWN_EGG")
                && eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.spawnegg"), "permissionscreative.spawnegg.bypass", "events.spawnegg","","")) {

            event.setCancelled(true);
            logger.log("discord.events.spawnegg.actions", "discord.events.spawnegg.message", player, discordLogAction);
        }
    }
}