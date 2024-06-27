package beta.com.permissionscreative.commands;

import beta.com.paginationapi.page.service.PaginationService;
import beta.com.paginationapi.search.SearchFunction;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.SettingsGUI;
import beta.com.permissionscreative.gui.PaginationManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.languagemanager.TranslateColorCodes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * The SettingsCommands class implements the CommandExecutor interface and defines how a command is
 * processed.
 * This class allows the user to modify settings.
 *
 * Attributes:
 * - languageManager: Used to manage language files.
 * - plugin: The plugin for which this command executor is being created.
 * - config: The configuration for the plugin.
 *
 * The class constructor takes in a Plugin, LangManager, and Config object, and initializes the class
 * attributes.
 *
 * The onCommand method is overridden from the CommandExecutor interface. It processes the command
 * and returns a boolean.
 * If the command sender is not a player, it sends a message to the sender and returns true.
 * If the sender does not have the required permissions, it sends a message to the sender and returns
 * true.
 * If the sender is a player and has the required permissions, it opens the settings GUI for
 * the player and returns true.
 */


public class SettingsCommands implements CommandExecutor {
    private LangManager languageManager;
    private Plugin plugin;
    private Config config;
    private PaginationService pagination;
    private SearchFunction searchFunction;
    private PaginationManager paginationManager;
    private PaginationManager ListModeGUI;

    public SettingsCommands(Plugin plugin, LangManager langManager, Config config, PaginationService pagination, SearchFunction searchFunction, PaginationManager paginationManager, PaginationManager listModeGUI) {
        this.languageManager = langManager;
        this.plugin = plugin;
        this.config = config;
        this.pagination = pagination;
        this.searchFunction = searchFunction;
        this.paginationManager = paginationManager;
        this.ListModeGUI = listModeGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
        prefix = TranslateColorCodes.translateHexColorCodes("#", prefix);

        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + languageManager.getMessage("commands.setting_commands.not_player", config.getConfig().getString("lang")));
            return true;
        }

        if (!sender.hasPermission("permissionscreative.settings")) {
            sender.sendMessage(prefix + languageManager.getMessage("commands.setting_commands.no_permissions", config.getConfig().getString("lang")));
            return true;
        }

        Player player = (Player) sender;
        SettingsGUI settingsGUI = new SettingsGUI(config,languageManager,plugin,pagination,searchFunction,paginationManager,ListModeGUI);
        settingsGUI.open(plugin, player);
        return true;
    }
}