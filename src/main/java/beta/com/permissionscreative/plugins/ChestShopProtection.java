package beta.com.permissionscreative.plugins;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import beta.com.permissionscreative.worldmanagement.World;
import com.Acrobot.ChestShop.Events.*;
import com.Acrobot.ChestShop.Events.Protection.BuildPermissionEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This class provides protection mechanisms for ChestShop transactions within the game.
 * It utilizes event handlers to intercept various ChestShop events such as transactions,
 * pre-transactions, build permissions, and shop information requests. The class ensures
 * that these events are processed according to the server's configurations and permissions,
 * logging actions to Discord if necessary. It leverages an integrated language manager for
 * localized messages, a configuration manager to handle server-specific settings, and an
 * events manager to check world-specific conditions and permissions. The goal is to provide
 * a seamless and secure shopping experience for players, preventing unauthorized actions and
 * ensuring compliance with the server's rules.
 */


public class ChestShopProtection implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final Logger logger;
    private final DiscordLogAction discordLogAction;
    private final LangManager langManager;

    public ChestShopProtection(Config config, EventsManager eventsManager, Logger logger, DiscordLogAction discordLogAction, LangManager langManager) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.logger = logger;
        this.discordLogAction = discordLogAction;
        this.langManager = langManager;
    }

    private void handleEvent(Player player, GameMode gameMode, String permission, String messageKey, String logAction, String logMessage) {
        World world = eventsManager.checkWorlds();
        if (world == null || !world.isWorldAllowed(player.getWorld())) {
            return;
        }

        if (eventsManager.checkAndSendMessage(player, gameMode, config.getConfig().getBoolean(permission), "permissionscreative.chestshop.bypass", messageKey)) {
            logger.log(logAction, logMessage, player, discordLogAction);
        }
    }

    @EventHandler
    public void onTransaction(TransactionEvent event) {
        handleEvent(event.getClient(), event.getClient().getGameMode(), "permissions.plugins.chestshop", "events.plugins.chestshop", "discord.events.chestshop.actions", "discord.events.chestshop.message");
        event.setCancelled(true);
    }

    @EventHandler
    public void onPreTransaction(PreTransactionEvent event) {
        handleEvent(event.getClient(), event.getClient().getGameMode(), "permissions.plugins.chestshop", "events.plugins.chestshop", "discord.events.chestshop.actions", "discord.events.chestshop.message");
        event.setCancelled(true);
    }

    @EventHandler
    public void onBuildPermission(BuildPermissionEvent event) {
        handleEvent(event.getPlayer(), event.getPlayer().getGameMode(), "permissions.plugins.chestshop", "events.plugins.chestshop", "discord.events.chestshop.actions", "discord.events.chestshop.message");
        event.setCancelled(false);
        event.disallow();
    }

    @EventHandler
    public void onShopInfo(ShopInfoEvent event) {
        handleEvent(event.getSender(), event.getSender().getGameMode(), "permissions.plugins.chestshop", "events.plugins.chestshop", "discord.events.chestshop.actions", "discord.events.chestshop.message");
        event.setCancelled(true);
    }
}