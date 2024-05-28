package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.commands.ReloadCommands;
import beta.com.permissionscreative.commands.SettingsCommands;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

public class CommandsRegister {

    private Config config;
    private LangManager langManager;
    private SettingsCommands settingsCommands;
    private ReloadCommands reloadCommands;
    private Plugin plugin;

    public CommandsRegister(Config config, LangManager langManager, Plugin plugin) {
        this.config = config;
        this.langManager = langManager;
        this.settingsCommands = new SettingsCommands(plugin, langManager, config);
        this.reloadCommands = new ReloadCommands(config, langManager);
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
            default:
                sender.sendMessage(prefix + langManager.getMessage("commands.permissions-creative.invalid_subcommand", config.getConfig().getString("lang")));
                break;
        }

        return true;
    }
}
