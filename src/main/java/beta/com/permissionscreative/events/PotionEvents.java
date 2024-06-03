package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.event.entity.ProjectileHitEvent;
import beta.com.permissionscreative.utils.Logger;

public class PotionEvents implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;

    public PotionEvents(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
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
                boolean cancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.remove_effects"), "permissionscreative.removeeffects.bypass", "events.remove-effects");
                if (cancel) {
                    event.setCancelled(true);
                    Logger.log("discord.events.remove_effects.actions", "discord.events.remove_effects.message", player, discordLogAction);
                }
            }
        }
    }
}