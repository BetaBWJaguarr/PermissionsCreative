package beta.com.permissionscreative.events;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.worldmanagement.Regions;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.GameMode;
import beta.com.permissionscreative.utils.Logger;

public class onGameModeChange implements Listener {

    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public onGameModeChange(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }


    @EventHandler
    public void onGameModeChanges(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        World world = eventsManager.checkWorlds();
        int regions = eventsManager.WorldguardCheck(player);

        if (world != null && !world.isWorldAllowed(player.getWorld())) {
            return;
        }

        if (regions == 0) {
            return;
        }
        GameMode newGameMode = event.getNewGameMode();

        if (newGameMode != GameMode.CREATIVE && !player.hasPermission("permissionscreative.gamemode.bypass")) {
            String gameModeName = newGameMode.name();
            boolean isGameModeAllowed = config.getConfig().getBoolean("inventory." + gameModeName);

            if (!isGameModeAllowed) {
                String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
                String message = langManager.getMessage("events.gmdenied", config.getConfig().getString("lang"));

                event.setCancelled(true);
                player.sendMessage(prefix + " " + message);
                logger.log("discord.events.gamemode.actions", "discord.events.gamemode.message", player, discordLogAction);
            }
        }
    }
}