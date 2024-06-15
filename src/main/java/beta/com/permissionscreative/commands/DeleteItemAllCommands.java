package beta.com.permissionscreative.commands;

import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.TranslateColorCodes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class DeleteItemAllCommands implements CommandExecutor {
    private DatabaseManager databaseManager;
    private LangManager langManager;
    private Config config;

    public DeleteItemAllCommands(DatabaseManager databaseManager, LangManager langManager, Config config) {
        this.databaseManager = databaseManager;
        this.langManager = langManager;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String language = config.getConfig().getString("lang");
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
        prefix = TranslateColorCodes.translateHexColorCodes("#", prefix);

        if (!sender.hasPermission("permissionscreative.deleteitems")) {
            sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.no_permission", language));
            return true;
        }

        try {
            if (args.length > 1) {
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.player_not_found", language) + args[1]);
                    return true;
                }
                String playerUUID = targetPlayer.getUniqueId().toString();
                boolean result = databaseManager.deletePlayerData(playerUUID);
                if (result) {
                    sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.player_data_deleted", language) + args[1]);
                } else {
                    sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.not_found_data", language));
                }
            } else {
                boolean result = databaseManager.deletePlayerData(null);
                if (result) {
                    sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.all_data_deleted", language));
                } else {
                    sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.not_found_data", language));
                }
            }
        } catch (SQLException e) {
            sender.sendMessage(prefix + langManager.getMessage("commands.delete-items.error", language));
            e.printStackTrace();
        }
        return true;
    }
}