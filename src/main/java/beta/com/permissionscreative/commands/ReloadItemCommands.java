package beta.com.permissionscreative.commands;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.languagemanager.TranslateColorCodes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

/**
 * The ReloadItemCommands class implements the CommandExecutor interface.
 * It is responsible for reloading items from the database into a player's inventory.
 *
 * It has four private members:
 * - `databaseManager`: an instance of DatabaseManager to interact with the database.
 * - `inventoryManager`: an instance of InventoryManager to manage the player's inventory.
 * - `langManager`: an instance of LangManager to handle language-specific messages.
 * - `config`: an instance of Config to handle the plugin's configuration.
 *
 * The class constructor initializes these members.
 *
 * The `onCommand` method is overridden from the CommandExecutor interface. It handles the command execution.
 * It first checks if the sender has the required permission. If not, it sends a no_permission message.
 * Then, it checks if the correct number of arguments has been provided. If not, it sends a usage message.
 * It then attempts to get the target player. If the player is not found, it sends a player_not_found message.
 * If the player is found, it attempts to load the player's items from the database and set the player's inventory.
 * Finally, it sends an items_reloaded message to the sender.
 */

public class ReloadItemCommands implements CommandExecutor {
    private DatabaseManager databaseManager;
    private InventoryManager inventoryManager;
    private LangManager langManager;
    private Config config;

    public ReloadItemCommands(Config config, LangManager langManager, DatabaseManager databaseManager, InventoryManager inventoryManager) {
        this.config = config;
        this.langManager = langManager;
        this.databaseManager = databaseManager;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String language = config.getConfig().getString("lang");
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
        prefix = TranslateColorCodes.translateHexColorCodes("#", prefix);

        if (!sender.hasPermission("permissionscreative.reloaditems")) {
            sendFormattedMessage(sender, prefix, "commands.reload-items.no_permission", language);
            return true;
        }

        if (args.length < 2) {
            sendFormattedMessage(sender, prefix, "commands.reload-items.usage", language);
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sendFormattedMessage(sender, prefix, "commands.reload-items.player_not_found", language, args[1]);
            return false;
        }

        ItemStack[] items = loadPlayerItems(targetPlayer);
        if (items == null || items.length == 0) {
            sendFormattedMessage(sender, prefix, "commands.reload-items.inventory_not_found", language);
            return true;
        }

        inventoryManager.setPlayerInventory(targetPlayer, items);
        sendFormattedMessage(sender, prefix, "commands.reload-items.items_reloaded", language, targetPlayer.getName());

        return true;
    }

    private void sendFormattedMessage(CommandSender sender, String prefix, String messageKey, String language, String... args) {
        String message = langManager.getMessage(messageKey, language);
        if (args.length > 0) {
            message += args[0];
        }
        sender.sendMessage(prefix + message);
    }

    private ItemStack[] loadPlayerItems(Player targetPlayer) {
        try {
            return databaseManager.loadPlayerItems(targetPlayer.getUniqueId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}