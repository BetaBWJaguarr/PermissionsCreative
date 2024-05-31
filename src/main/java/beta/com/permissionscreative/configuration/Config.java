package beta.com.permissionscreative.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The Config class is a utility class that manages the configuration file of a JavaPlugin.
 * It provides methods to reload, save, and fetch the configuration. This class plays a crucial role in managing plugin settings and preferences that are stored in the configuration file.
 *
 * Fields:
 * - plugin: An instance of the JavaPlugin that this configuration is associated with. This association allows the configuration to interact directly with the plugin's data folder.
 * - config: The FileConfiguration object that contains the loaded configuration. This object is central to reading and writing data to and from the configuration file.
 * - configFile: A File object that represents the physical configuration file. This file is where the configuration data is stored on disk.
 *
 * Constructor:
 * - Config(JavaPlugin plugin): Constructs a new Config object for the given plugin and saves the default configuration if it doesn't exist. This constructor ensures that a configuration file is always available for the plugin.
 *
 * Methods:
 * - reloadConfig(): Reloads the configuration from the file and sets the defaults from the default configuration file if it exists. This method is useful when changes to the configuration file are made while the plugin is running.
 * - getConfig(): Returns the FileConfiguration object. If the configuration has not been loaded, it reloads it first. This method provides a way for other parts of the plugin to interact with the configuration data.
 * - saveConfig(): Saves the current configuration to the file. If the configuration or the file is null, it does nothing. This method ensures that changes made to the configuration during the plugin's operation are persisted to disk.
 * - saveDefaultConfig(): Saves the default configuration if it doesn't exist yet. This method ensures that a base configuration is always available, even if a custom configuration file has not been created yet.
 */

public class Config {
    private final JavaPlugin plugin;
    private FileConfiguration config = null;
    private File configFile = null;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        File defaultConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (defaultConfigFile.exists()) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigFile);
            config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }
}
