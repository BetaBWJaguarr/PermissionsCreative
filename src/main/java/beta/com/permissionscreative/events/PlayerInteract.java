package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.enums.ThrowItems;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public PlayerInteract(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
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

        if (player.getGameMode() == GameMode.CREATIVE && ThrowItems.isThrowItem(player.getItemInHand().getType()) && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.throw"), "permissionscreative.throw.bypass", "events.throw","throw",player.getItemInHand().getType().toString())) {
            event.setCancelled(true);
            logger.log("discord.events.throw.actions", "discord.events.throw.message", player, discordLogAction);
        }
    }
}