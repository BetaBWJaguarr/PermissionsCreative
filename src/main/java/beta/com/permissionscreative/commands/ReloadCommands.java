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

        if (!sender.hasPermission("permissionscreative.reload")) {
            sender.sendMessage(prefix + languageManager.getMessage("commands.reload_commands.no_permission",config.getConfig().getString("lang")));
            return true;
        }

        config.reloadConfig();
        sender.sendMessage(prefix  + languageManager.getMessage("commands.reload_commands.config_reloaded",config.getConfig().getString("lang")));
        return true;
    }

}
