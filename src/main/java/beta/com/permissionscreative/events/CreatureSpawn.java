package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CreatureSpawn implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public CreatureSpawn(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = eventsManager.checkWorlds();
        boolean isPlayerInRegion = eventsManager.WorldguardCheck(player);

        if (!eventsManager.checkProtection(player, world, isPlayerInRegion)) {
            return;
        }

        Material itemInHand = player.getItemInHand().getType();

        if (itemInHand.name().endsWith("_SPAWN_EGG")) {
            boolean shouldCancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.spawnegg"), "permissionscreative.spawnegg.bypass", "events.spawnegg","","");
            if (shouldCancel) {
                event.setCancelled(true);
                logger.log("discord.events.spawnegg.actions", "discord.events.spawnegg.message", player, discordLogAction);
            }
        }
    }
}