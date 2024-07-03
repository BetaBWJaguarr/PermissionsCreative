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


/**
 * This class, {@code DeleteItemAllCommands}, implements the {@code CommandExecutor} interface
 * to handle the `/deleteitems` command in a Bukkit/Spigot plugin environment. The command allows
 * authorized users to delete player data from a database managed by {@link DatabaseManager}.
 *
 * <p>
 * The class is initialized with instances of {@link DatabaseManager}, {@link LangManager}, and
 * {@link Config} to facilitate database operations, language localization, and configuration
 * management, respectively. It retrieves the language preferences and message formatting from
 * the configuration file to ensure the command's response is localized and formatted correctly.
 *
 * <p>
 * Command execution begins by checking the sender's permissions to ensure they have the necessary
 * access (`permissionscreative.deleteitems`). If the sender lacks permission, an appropriate
 * message is sent using {@link LangManager} to inform them of their insufficient privileges.
 *
 * <p>
 * The command supports two modes of operation based on the number of arguments provided:
 * - If only one argument is provided, the command deletes all player data stored in the database
 *   using {@link DatabaseManager#deletePlayerData(String)}.
 * - If two arguments are provided, the command attempts to locate the player by name and delete
 *   their data using {@link DatabaseManager#deletePlayerData(String)}.
 *
 * <p>
 * Error handling is managed through {@link SQLException} handling, ensuring that any database
 * operation errors are properly logged and reported to the command sender through
 * {@link LangManager} messages.
 *
 * <p>
 * Overall, {@code DeleteItemAllCommands} provides robust functionality for managing player data
 * deletion with permissions control, error handling, and localized messaging, making it an essential
 * component for administrative tasks in Bukkit/Spigot plugin development.
 */

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