package beta.com.permissionscreative.commands;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class ReloadItemCommands implements CommandExecutor {
    private DatabaseManager databaseManager;
    private InventoryManager inventoryManager;
    private LangManager langManager;
    private Config config;

    public ReloadItemCommands(Config config,LangManager langManager,DatabaseManager databaseManager, InventoryManager inventoryManager) {
        this.config = config;
        this.langManager = langManager;
        this.databaseManager = databaseManager;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String language = config.getConfig().getString("lang");
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));

        if (!sender.hasPermission("permissionscreative.reloaditems")) {
            sender.sendMessage(prefix + langManager.getMessage("commands.reload-items.no_permission", language));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(prefix + langManager.getMessage("commands.reload-items.usage", language));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(prefix + langManager.getMessage("commands.reload-items.player_not_found", language) + args[1]);
            return false;
        }

        ItemStack[] items = null;
        try {
            items = databaseManager.loadPlayerItems(targetPlayer.getUniqueId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        inventoryManager.setPlayerInventory(targetPlayer, items);

        sender.sendMessage(prefix + langManager.getMessage("commands.reload-items.items_reloaded", language) + targetPlayer.getName());
        return true;
    }
}