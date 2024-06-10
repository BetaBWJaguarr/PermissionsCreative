package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onGameModeChange implements Listener {

    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public onGameModeChange(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onGameModeChanges(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        if (!eventsManager.checkProtection(player, eventsManager.checkWorlds(), eventsManager.WorldguardCheck(player))) {
            return;
        }

        GameMode newGameMode = event.getNewGameMode();

        if (newGameMode != GameMode.CREATIVE && !player.hasPermission("permissionscreative.gamemode.bypass") && !config.getConfig().getBoolean("inventory." + newGameMode.name())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix")) + " " + config.getConfig().getString("events.gmdenied"));
            logger.log("discord.events.gamemode.actions", "discord.events.gamemode.message", player, discordLogAction);
        }
    }
}