package beta.com.permissionscreative.commands;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.SettingsGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import beta.com.permissionscreative.languagemanager.LangManager;

public class SettingsCommands implements CommandExecutor {
    private LangManager languageManager;
    private Plugin plugin;
    private Config config;

    public SettingsCommands(Plugin plugin, LangManager langManager, Config config) {
        this.languageManager = langManager;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));

        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + languageManager.getMessage("commands.setting_commands.not_player", config.getConfig().getString("lang")));
            return true;
        }

        if (!sender.hasPermission("permissionscreative.settings")) {
            sender.sendMessage(prefix + languageManager.getMessage("commands.settings_commands.no_permission", config.getConfig().getString("lang")));
            return true;
        }

        Player player = (Player) sender;
        SettingsGUI settingsGUI = new SettingsGUI(config,languageManager);
        settingsGUI.open(plugin, player);
        return true;
    }
}