package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.events.*;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
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

    public RegisterListener(JavaPlugin plugin, Config config, LangManager langManager) {
        this.plugin = plugin;
        this.config = config;
        this.langManager = langManager;
    }

    public void registerEvents() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new BlockPlace(config,langManager), plugin);
        pm.registerEvents(new DropItem(config,langManager), plugin);
        pm.registerEvents(new CommandsEvent(config,langManager),plugin);
        pm.registerEvents(new PlayerDamage(config,langManager),plugin);
        pm.registerEvents(new PlayerInteract(config,langManager),plugin);
        pm.registerEvents(new onGameModeChange(config,langManager),plugin);
        pm.registerEvents(new InventoryOpen(config,langManager),plugin);
        pm.registerEvents(new EntityDamageByEntity(config,langManager),plugin);
        pm.registerEvents(new PotionEvents(config,langManager),plugin);
        pm.registerEvents(new PlayerInteractEntity(config,langManager),plugin);
    }
}
