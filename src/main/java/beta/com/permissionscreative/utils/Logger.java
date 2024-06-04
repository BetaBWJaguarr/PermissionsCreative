package beta.com.permissionscreative.utils;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.filemanager.LogsFile;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.object.EventsType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Logger {
    private final Config config;
     private final LangManager langManager;
     private final Plugin plugin;

    public Logger(Config config, LangManager langManager, Plugin plugin) {
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
    }


    public void log(String actionKey, String messageKey, Player player, DiscordLogAction discordLogAction) {
        String lang = config.getConfig().getString("lang");
        String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
        EventsType eventsType = new EventsType(langManager.getMessage(actionKey, lang), player.getName() + " " + langManager.getMessage(messageKey, lang));

        if (config.getConfig().getBoolean("logging.discordbot.enabled")) {
            discordbot(eventsType, discordLogAction);
        }
        if (config.getConfig().getBoolean("logging.gamechat.enabled")) {
            gamechat(actionKey, messageKey, player, lang,prefix);
        }
        if (config.getConfig().getBoolean("logging.console.enabled")) {
            console(eventsType);
        }
        if (config.getConfig().getBoolean("logging.file.enabled")) {
            file(eventsType);
        }
    }

    public void discordbot(EventsType eventsType, DiscordLogAction discordLogAction) {
        discordLogAction.logAction(eventsType);
    }

    public void gamechat(String actionKey, String messageKey, Player player, String lang,String prefix) {
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("permissionscreative.admin.log")) {
                onlinePlayer.sendMessage((prefix + " " + ChatColor.RED + langManager.getMessage(actionKey, lang) + " " + ChatColor.DARK_RED +  onlinePlayer.getName() + " " + langManager.getMessage(messageKey, lang)));
            }
        }
    }

    public void console(EventsType eventsType) {
        String message = eventsType.getAction() + " : " + eventsType.getMessage();
        try {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void file(EventsType eventsType) {
        String message = eventsType.getAction() + " : " + eventsType.getMessage();
        LogsFile logsFile = new LogsFile(plugin);
        logsFile.write(message);
    }
}