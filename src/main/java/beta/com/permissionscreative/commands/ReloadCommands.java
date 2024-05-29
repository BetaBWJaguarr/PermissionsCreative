package beta.com.permissionscreative.commands;

import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import beta.com.permissionscreative.configuration.Config;

/**
 * The ReloadCommands class implements the CommandExecutor interface and is responsible for handling
 * the 'reload' command in the game.
 *
 * This command allows a player with the 'permissionscreative.reload' permission to reload the game's
 * configuration settings from the config.yml file. If the player does not have this permission,they
 * are sent a message indicating that they do not have permission to use this command.
 *
 * The class uses instances of the Config and LangManager classes to access the game's configuration
 * settings and language-specific messages, respectively.
 *
 * When the 'reload' command is executed, the configuration settings are reloaded from the config.yml
 * file, and a message is sent to the player to confirm that the configuration has been reloaded.
 */

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
        languageManager.reloadLanguageFiles();
        sender.sendMessage(prefix  + languageManager.getMessage("commands.reload_commands.config_reloaded",config.getConfig().getString("lang")));
        return true;
    }

}
