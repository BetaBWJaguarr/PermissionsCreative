package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EventsManager {

    private Config config;
    private LangManager langManager;
    private Plugin plugin;

    public EventsManager(Config config, LangManager langManager, Plugin plugin) {
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
    }

    public boolean checkAndSendMessage(Player player, GameMode gameMode, boolean permissionEnabled, String permission, String messageKey) {
        if (player.getGameMode() == gameMode && permissionEnabled && !player.hasPermission(permission)) {
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
            String message = langManager.getMessage(messageKey, config.getConfig().getString("lang"));
            player.sendMessage(prefix + " " + message);
            return true;
        }
        return false;
    }
}
