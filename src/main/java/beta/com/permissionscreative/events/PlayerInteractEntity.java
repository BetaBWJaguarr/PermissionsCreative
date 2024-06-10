package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractEntity implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;
    private final Map<UUID, Long> recentInteractions = new HashMap<>();

    public PlayerInteractEntity(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        UUID playerID = event.getPlayer().getUniqueId();
        long currentTime = System.currentTimeMillis();
        if (!recentInteractions.containsKey(playerID) || currentTime - recentInteractions.get(playerID) > 1000) {
            recentInteractions.put(playerID, currentTime);
            handleInteractEvent(event);
        }
    }

    private void handleInteractEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        World world = eventsManager.checkWorlds();
        boolean isPlayerInRegion = eventsManager.WorldguardCheck(player);

        if (!eventsManager.checkProtection(player, world, isPlayerInRegion)) {
            return;
        }

        boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.entity"), "permissionscreative.entity.bypass", "events.entity","","");
        if (cancel) {
            event.setCancelled(true);
            logger.log("discord.events.entity.actions", "discord.events.entity.message", player, discordLogAction);
        }
    }
}
