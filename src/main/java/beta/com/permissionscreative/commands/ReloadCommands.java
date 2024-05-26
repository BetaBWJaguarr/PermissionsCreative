package beta.com.permissionscreative.commands;

import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import beta.com.permissionscreative.configuration.Config;

public class ReloadCommands implements CommandExecutor {
    private final Config config;
    private final LangManager languageManager;

    public ReloadCommands(Config config, LangManager languageManager) {
        this.config = config;
        this.languageManager = languageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                config.reloadConfig();
                sender.sendMessage(prefix + ChatColor.GREEN + languageManager.getMessage("commands.reload_commands.config_reloaded",config.getConfig().getString("lang")));
                return true;
            } else {
                sender.sendMessage(prefix + ChatColor.RED + languageManager.getMessage("commands.reload_commands.invalid_argument", config.getConfig().getString("lang")));
                return false;
            }
        } else {
            sender.sendMessage(prefix + ChatColor.RED + languageManager.getMessage("commands.reload_commands.provide_argument", config.getConfig().getString("lang")));
            return false;
        }
    }
}
