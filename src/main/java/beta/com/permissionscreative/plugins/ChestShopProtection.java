package beta.com.permissionscreative.plugins;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import com.Acrobot.ChestShop.Events.*;
import com.Acrobot.ChestShop.Events.Protection.BuildPermissionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

    @EventHandler
    public void onTransaction(TransactionEvent event) {
        if (eventsManager.checkAndSendMessage(event.getClient(), event.getClient().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(true);
            logger.log(langManager.getMessage("discord.events.chestshop.actions",config.getConfig().getString("lang")), langManager.getMessage("discord.events.chestshop.message",config.getConfig().getString("lang")), event.getClient(), discordLogAction);
        }
    }

    @EventHandler
    public void onPreTransaction(PreTransactionEvent event) {
        if (eventsManager.checkAndSendMessage(event.getClient(), event.getClient().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(true);
            logger.log(langManager.getMessage("discord.events.chestshop.actions",config.getConfig().getString("lang")), langManager.getMessage("discord.events.chestshop.message",config.getConfig().getString("lang")), event.getClient(), discordLogAction);
        }
    }

    @EventHandler
    public void onBuildPermission(BuildPermissionEvent event) {
        if (eventsManager.checkAndSendMessage(event.getPlayer(), event.getPlayer().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(false);
            event.disallow();
            logger.log(langManager.getMessage("discord.events.chestshop.actions",config.getConfig().getString("lang")), langManager.getMessage("discord.events.chestshop.message",config.getConfig().getString("lang")), event.getPlayer(), discordLogAction);
        }
    }

    @EventHandler
    public void onShopInfo(ShopInfoEvent event) {
        if (eventsManager.checkAndSendMessage(event.getSender(), event.getSender().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(true);
            logger.log(langManager.getMessage("discord.events.chestshop.actions",config.getConfig().getString("lang")), langManager.getMessage("discord.events.chestshop.message",config.getConfig().getString("lang")), event.getSender(), discordLogAction);
        }
    }
}