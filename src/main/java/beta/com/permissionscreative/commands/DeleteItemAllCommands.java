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
            sendFormattedMessage(sender, prefix, "commands.delete-items.no_permission", language);
            return true;
        }

        try {
            if (args.length > 1) {
                handlePlayerDataDeletion(sender, args[1], prefix, language);
            } else {
                handleAllDataDeletion(sender, prefix, language);
            }
        } catch (SQLException e) {
            sendFormattedMessage(sender, prefix, "commands.delete-items.error", language);
            e.printStackTrace();
        }
        return true;
    }

    private void sendFormattedMessage(CommandSender sender, String prefix, String messageKey, String language, String... args) {
        String message = langManager.getMessage(messageKey, language);
        if (args.length > 0) {
            message += " " + args[0];
        }
        sender.sendMessage(prefix + message);
    }

    private void handlePlayerDataDeletion(CommandSender sender, String playerName, String prefix, String language) throws SQLException {
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            sendFormattedMessage(sender, prefix, "commands.delete-items.player_not_found", language, playerName);
            return;
        }
        String playerUUID = targetPlayer.getUniqueId().toString();
        boolean result = databaseManager.deletePlayerData(playerUUID);
        String messageKey = result ? "commands.delete-items.player_data_deleted" : "commands.delete-items.not_found_data";
        sendFormattedMessage(sender, prefix, messageKey, language, playerName);
    }

    private void handleAllDataDeletion(CommandSender sender, String prefix, String language) throws SQLException {
        boolean result = databaseManager.deletePlayerData(null);
        String messageKey = result ? "commands.delete-items.all_data_deleted" : "commands.delete-items.not_found_data";
        sendFormattedMessage(sender, prefix, messageKey, language);
    }
}