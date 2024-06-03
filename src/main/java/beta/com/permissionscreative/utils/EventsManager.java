package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.object.EventsType;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.FileWriter;

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

    public void logEvent(String actionKey, String messageKey, Player player, DiscordLogAction discordLogAction) {
        String lang = config.getConfig().getString("lang");
        EventsType eventsType = new EventsType(langManager.getMessage(actionKey, lang), player.getName() + " " + langManager.getMessage(messageKey, lang));

        if (config.getConfig().getBoolean("logging.discordbot.enabled")) {
            discordbot(eventsType, discordLogAction);
        }
        if (config.getConfig().getBoolean("logging.gamechat.enabled")) {
            gamechat(actionKey, messageKey, player, lang);
        }
        if(config.getConfig().getBoolean("logging.console.enabled")){
            console(eventsType);
        }
        if(config.getConfig().getBoolean("logging.file.enabled")){
            file(eventsType);
        }
    }

    private void discordbot(EventsType eventsType,DiscordLogAction discordLogAction) {
        discordLogAction.logAction(eventsType);
    }

    private void gamechat(String actionKey, String messageKey, Player player, String lang) {
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("permissionscreative.admin.log")) {
                onlinePlayer.sendMessage((langManager.getMessage(actionKey, lang) + " " + onlinePlayer.getName() + " " + langManager.getMessage(messageKey, lang)));
            }
        }
    }
    private  void console(EventsType eventsType){
        String message = eventsType.getAction()+" : "+ eventsType.getMessage();
        System.out.println(message);
    }
    private  void file(EventsType eventsType){
        String filelocation = plugin.getDataFolder()+"/logs.txt";
        System.out.println(filelocation);
        String message = eventsType.getAction()+" : "+ eventsType.getMessage();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filelocation, true));
            writer.write(message);
            writer.newLine();
            writer.close();
        } catch(Exception e){}
    }
}
