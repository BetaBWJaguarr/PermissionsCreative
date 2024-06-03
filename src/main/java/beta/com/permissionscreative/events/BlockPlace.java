package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.object.EventsType;
import beta.com.permissionscreative.utils.EventsManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;

public class BlockPlace implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;

    public BlockPlace(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        boolean shouldCancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.build"), "permissionscreative.build.bypass", "events.blockplace");
        if (shouldCancel) {
            event.setCancelled(true);
            eventsManager.logEvent("discord.events.blockplace.actions", "discord.events.blockplace.message", player, discordLogAction);
        }
    }
}
