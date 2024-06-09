package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.Logger;

public class BlockPlace implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public BlockPlace(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = eventsManager.checkWorlds();
        boolean isPlayerInRegion = eventsManager.WorldguardCheck(player);

        if (!eventsManager.checkProtection(player, world, isPlayerInRegion)) {
            return;
        }

        String item = event.getBlock().getType().toString();
        boolean shouldCancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.build"), "permissionscreative.build.bypass", "events.blockplace", "build", item);
        if (shouldCancel) {
            event.setCancelled(true);
            logger.log("discord.events.blockplace.actions", "discord.events.blockplace.message", player, discordLogAction);
        }
    }
}