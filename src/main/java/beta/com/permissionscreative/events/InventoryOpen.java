package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.worldmanagement.Regions;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.Logger;

public class InventoryOpen implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public InventoryOpen(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            World world = eventsManager.checkWorlds();
            boolean isPlayerInRegion = eventsManager.WorldguardCheck(player);

            if (!eventsManager.checkProtection(player, world, isPlayerInRegion)) {
                return;
            }

            boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.gui"), "permissionscreative.bypass.gui", "events.gui-disabled","","");
            if (cancel) {
                event.setCancelled(true);
                logger.log("discord.events.gui.actions", "discord.events.gui.message", player, discordLogAction);
            }
        }
    }
}