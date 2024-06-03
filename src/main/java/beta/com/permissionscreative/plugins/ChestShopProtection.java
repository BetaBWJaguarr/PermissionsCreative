package beta.com.permissionscreative.plugins;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.utils.EventsManager;
import com.Acrobot.ChestShop.Events.*;
import com.Acrobot.ChestShop.Events.Protection.BuildPermissionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChestShopProtection implements Listener {
    private final Config config;
    private final EventsManager eventsManager;

    public ChestShopProtection(Config config, EventsManager eventsManager) {
        this.config = config;
        this.eventsManager = eventsManager;
    }

    @EventHandler
    public void onTransaction(TransactionEvent event) {
        if (eventsManager.checkAndSendMessage(event.getClient(), event.getClient().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPreTransaction(PreTransactionEvent event) {
        if (eventsManager.checkAndSendMessage(event.getClient(), event.getClient().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBuildPermission(BuildPermissionEvent event) {
        if (eventsManager.checkAndSendMessage(event.getPlayer(), event.getPlayer().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(false);
            event.disallow();
        }
    }

    @EventHandler
    public void onShopInfo(ShopInfoEvent event) {
        if (eventsManager.checkAndSendMessage(event.getSender(), event.getSender().getGameMode(), config.getConfig().getBoolean("permissions.plugins.chestshop"), "permissionscreative.chestshop.bypass", "events.plugins.chestshop")) {
            event.setCancelled(true);
        }
    }
}