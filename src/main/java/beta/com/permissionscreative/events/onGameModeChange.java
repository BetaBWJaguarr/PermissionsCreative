package beta.com.permissionscreative.events;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
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

    public onGameModeChange(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
    }


    @EventHandler
    public void onGameModeChanges(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GameMode newGameMode = event.getNewGameMode();

        if (newGameMode != GameMode.CREATIVE && !player.hasPermission("permissionscreative.gamemode.bypass")) {
            String gameModeName = newGameMode.name();
            boolean isGameModeAllowed = config.getConfig().getBoolean("inventory." + gameModeName);

            if (!isGameModeAllowed) {
                String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
                String message = langManager.getMessage("events.gmdenied", config.getConfig().getString("lang"));

                event.setCancelled(true);
                player.sendMessage(prefix + " " + message);
                Logger.log("discord.events.gamemode.actions", "discord.events.gamemode.message", player, discordLogAction);
            }
        }
    }
}