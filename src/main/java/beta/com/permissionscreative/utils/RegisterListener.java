package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.events.*;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.plugins.ChestShopProtection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The RegisterListener class is responsible for registering the various event listeners used in the
 * plugin.
 * It is part of the 'beta.com.permissionscreative.utils' package.
 *
 * This class has three private final members:
 * - A JavaPlugin instance 'plugin' which represents the current plugin instance.
 * - A Config instance 'config' which holds the configuration settings for the plugin.
 * - A LangManager instance 'langManager' which manages the language settings for the plugin.
 *
 * The constructor for this class takes a JavaPlugin, Config, and LangManager instance, and
 * initializes the private members.
 *
 * The 'registerEvents' method is used to register the event listeners for the plugin. It gets the
 * PluginManager for the server,
 * and then registers each of the event listeners (BlockPlace, DropItem, CommandsEvent, PlayerDamage,
 * PlayerInteract, onGameModeChange)
 * with the PluginManager. Each event listener is initialized with the Config and LangManager
 * instances, and the plugin instance is passed
 * to the registerEvents method of the PluginManager.
 */

public class RegisterListener {
    private final JavaPlugin plugin;
    private final Config config;
    private final LangManager langManager;
    private final EventsManager eventsManager;
    private final DiscordLogAction discordLogAction;
    private final Logger logger;

    public RegisterListener(JavaPlugin plugin, Config config, LangManager langManager, EventsManager eventsManager, DiscordLogAction discordLogAction, Logger logger) {
        this.plugin = plugin;
        this.config = config;
        this.langManager = langManager;
        this.eventsManager = eventsManager;
        this.discordLogAction = discordLogAction;
        this.logger = logger;
    }

    public void registerEvents() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new BlockPlace(config,langManager,eventsManager,discordLogAction,logger), plugin);
        pm.registerEvents(new DropItem(config,eventsManager,discordLogAction,logger), plugin);
        pm.registerEvents(new CommandsEvent(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new PlayerDamage(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new PlayerInteract(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new onGameModeChange(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new InventoryOpen(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new EntityDamageByEntity(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new PotionEvents(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new PlayerInteractEntity(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new CreatureSpawn(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new ChestShopProtection(config,eventsManager,logger,discordLogAction),plugin);
        pm.registerEvents(new FlyEvent(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new BlockBreak(config,eventsManager,discordLogAction,logger),plugin);
        pm.registerEvents(new MonsterSpawnEvent(config,eventsManager,logger,discordLogAction),plugin);
        pm.registerEvents(new ItemPickup(config,eventsManager,discordLogAction,logger),plugin);
    }
}
