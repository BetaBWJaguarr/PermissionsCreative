package beta.com.permissionscreative.events;

import org.bukkit.ChatColor;
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

public class PotionEvents implements Listener {
    private final Config config;
    private final LangManager langManager;

    public PotionEvents(Config config, LangManager langManager) {
        this.config = config;
        this.langManager = langManager;
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
                String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
                if (config.getConfig().getBoolean("permissions.remove_effects") && player.getGameMode() == GameMode.CREATIVE && !player.hasPermission("permissionscreative.removeeffects.bypass")) {
                    event.setCancelled(true);
                    player.sendMessage(prefix + " " + langManager.getMessage("events.remove-effects", config.getConfig().getString("lang")));
                }
            }
        }
    }
}