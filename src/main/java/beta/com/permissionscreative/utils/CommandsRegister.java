package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.commands.ReloadCommands;
import beta.com.permissionscreative.commands.ReloadItemCommands;
import beta.com.permissionscreative.commands.SettingsCommands;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

/**
 * The CommandsRegister class is responsible for registering and handling commands for the PermissionsCreative plugin.
 *
 *
 * This class holds references to the Config, LangManager, SettingsCommands, ReloadCommands, and Plugin instances.
 * It uses these references to execute the appropriate actions when a command is issued.
 *
 * @param config The Config instance used to access the plugin's configuration.
 * @param langManager The LangManager instance used to access the plugin's language settings.
 * @param plugin The Plugin instance representing the PermissionsCreative plugin.
 *
 * The constructor initializes the SettingsCommands and ReloadCommands with the provided Config, LangManager, and Plugin instances.
 *
 * The registerCommands method is used to register the "permissions-creative" command with the server.
 *
 * The handlePermissionsCreativeCommand method is the executor for the "permissions-creative" command. It checks the arguments of the command,
 * and based on the subcommand (either "settings" or "reload"), it calls the appropriate method from the SettingsCommands or ReloadCommands instance.
 * If no subcommand is provided, or if an invalid subcommand is provided, it sends an appropriate message to the sender.
 */


public class CommandsRegister {

    private Config config;
    private LangManager langManager;
    private SettingsCommands settingsCommands;
    private ReloadCommands reloadCommands;
    private ReloadItemCommands reloadItemCommands;
    private DatabaseManager databaseManager;
    private InventoryManager inventoryManager;
    private Plugin plugin;

    public CommandsRegister(Config config, LangManager langManager, Plugin plugin,DatabaseManager databaseManager,InventoryManager inventoryManager) {
        this.config = config;
        this.langManager = langManager;
        this.settingsCommands = new SettingsCommands(plugin, langManager, config);
        this.reloadCommands = new ReloadCommands(config, langManager);
        this.reloadItemCommands = new ReloadItemCommands(config, langManager,databaseManager,inventoryManager);
        this.plugin = plugin;
    }

    public void registerCommands() {
        PluginCommand permissionsCreativeCommand = plugin.getServer().getPluginCommand("permissions-creative");
        permissionsCreativeCommand.setExecutor(this::handlePermissionsCreativeCommand);
    }

    private boolean handlePermissionsCreativeCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));

        if (args.length == 0) {
            sender.sendMessage(prefix + langManager.getMessage("commands.permissions-creative.no_subcommand",config.getConfig().getString("lang")));
            return true;
        }

        String subCommand = args[0];
        switch (subCommand.toLowerCase()) {
            case "settings":
                settingsCommands.onCommand(sender, command, label, args);
                break;
            case "reload":
                reloadCommands.onCommand(sender, command, label, args);
                break;
            case "reload-items":
                reloadItemCommands.onCommand(sender, command, label, args);
                break;
            default:
                sender.sendMessage(prefix + langManager.getMessage("commands.permissions-creative.invalid_subcommand", config.getConfig().getString("lang")));
                break;
        }

        return true;
    }
}
