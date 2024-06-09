package beta.com.permissionscreative.events;

import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.enums.ThrowItems;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.worldmanagement.Regions;
import beta.com.permissionscreative.worldmanagement.World;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.Logger;

public class PlayerInteract implements Listener {
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public PlayerInteract(Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = eventsManager.checkWorlds();
        boolean isPlayerInRegion = eventsManager.WorldguardCheck(player);

        if (!eventsManager.checkProtection(player, world, isPlayerInRegion)) {
            return;
        }

        Material itemInHand = player.getItemInHand().getType();
        String items = itemInHand.toString();

        if (player.getGameMode() == GameMode.CREATIVE && ThrowItems.isThrowItem(itemInHand)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                boolean shouldcancel = eventsManager.checkAndSendMessage(player, GameMode.CREATIVE, config.getConfig().getBoolean("permissions.throw"), "permissionscreative.throw.bypass", "events.throw","throw",items);
                if (shouldcancel) {
                    event.setCancelled(true);
                    logger.log("discord.events.throw.actions", "discord.events.throw.message", player, discordLogAction);
                }
            }
        }
    }
}
