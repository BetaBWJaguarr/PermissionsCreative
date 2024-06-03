package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.utils.EventsManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.event.player.PlayerInteractEvent;
import beta.com.permissionscreative.utils.Logger;

public class CreatureSpawn implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;

    public CreatureSpawn(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material itemInHand = player.getItemInHand().getType();

        if (itemInHand.name().endsWith("_SPAWN_EGG")) {
            boolean shouldCancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.spawnegg"), "permissionscreative.spawnegg.bypass", "events.spawnegg");
            if (shouldCancel) {
                event.setCancelled(true);
                Logger.log("discord.events.spawnegg.actions", "discord.events.spawnegg.message", player, discordLogAction);
            }
        }
    }
}