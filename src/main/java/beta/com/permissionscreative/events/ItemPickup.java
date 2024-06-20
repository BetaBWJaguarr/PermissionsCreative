package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.HashMap;
import java.util.Map;

public class ItemPickup implements Listener {

    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;
    private final Map<Player, Long> lastTriggerTimes = new HashMap<>();

    public ItemPickup(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        // Check if 30 seconds have passed since the last time the event was triggered for the player
        long currentTime = System.currentTimeMillis();
        Long lastTriggerTime = lastTriggerTimes.get(player);
        if (lastTriggerTime != null && currentTime - lastTriggerTime < 30 * 1000) {
            event.setCancelled(true);
            return;
        }

        lastTriggerTimes.put(player, currentTime);

        if (!eventsManager.checkProtection(player, eventsManager.checkWorlds(), eventsManager.WorldguardCheck(player))) {
            return;
        }

        if (eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.pickup"), "permissionscreative.pickup.bypass", "events.pickup", "", "")) {
            event.setCancelled(true);
            logger.log("discord.events.pickup.actions", "discord.events.pickup.message", player, discordLogAction);
        }
    }
}