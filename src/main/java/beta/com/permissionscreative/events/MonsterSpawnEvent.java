package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Objects;

public class MonsterSpawnEvent implements Listener {

    private final Config config;
    private final EventsManager eventsManager;
    private final Logger logger;
    private final DiscordLogAction discordLogAction;

    public MonsterSpawnEvent(Config config, EventsManager eventsManager, Logger logger, DiscordLogAction discordLogAction) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.logger = logger;
        this.discordLogAction = discordLogAction;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent e) {
        CreatureSpawnEvent.SpawnReason spawnReason = e.getSpawnReason();

        if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) ||
                spawnReason.equals(CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM) ||
                spawnReason.equals(CreatureSpawnEvent.SpawnReason.BUILD_WITHER)) {

            Player player = getNearestPlayer(e.getLocation(), 10);

            if (!eventsManager.checkProtection(player, eventsManager.checkWorlds(), eventsManager.WorldguardCheck(player))) {
                return;
            }

            if (eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.buildable_mob"), "permissionscreative.buildable_mob.bypass", "events.buildable_mob", "", "")) {
                e.setCancelled(true);
                logger.log("discord.events.buildable_mob.actions", "discord.events.buildable_mob.message", player, discordLogAction);
            }
        }
    }


    private Player getNearestPlayer(Location location, double radius) {
        double closestDistance = radius;
        Player closestPlayer = null;

        for (Player player : Objects.requireNonNull(location.getWorld()).getPlayers()) {
            double distance = player.getLocation().distance(location);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }
}