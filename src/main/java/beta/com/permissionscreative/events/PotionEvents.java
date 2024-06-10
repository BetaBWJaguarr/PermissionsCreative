package beta.com.permissionscreative.events;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class PotionEvents implements Listener {
    private final Config config;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public PotionEvents(Config config, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        handlePotionEvent(event);
    }

    @EventHandler
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {
        handlePotionEvent(event);
    }

    private void handlePotionEvent(ProjectileHitEvent event) {
        if (event.getEntity() instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) event.getEntity();
            if (potion.getShooter() instanceof Player) {
                Player player = (Player) potion.getShooter();
                World world = eventsManager.checkWorlds();
                boolean isPlayerInRegion = eventsManager.WorldguardCheck(player);

                if (!eventsManager.checkProtection(player, world, isPlayerInRegion)) {
                    return;
                }

                boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.remove_effects"), "permissionscreative.removeeffects.bypass", "events.remove-effects","","");
                if (cancel) {
                    event.setCancelled(true);
                    logger.log("discord.events.remove_effects.actions", "discord.events.remove_effects.message", player, discordLogAction);
                }
            }
        }
    }
}