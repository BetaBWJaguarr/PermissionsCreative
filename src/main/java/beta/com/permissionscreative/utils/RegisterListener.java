package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.events.BlockPlace;
import beta.com.permissionscreative.events.DropItem;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RegisterListener {
    private final JavaPlugin plugin;
    private final Config config;
    private final LangManager langManager;

    public RegisterListener(JavaPlugin plugin, Config config, LangManager langManager) {
        this.plugin = plugin;
        this.config = config;
        this.langManager = langManager;
    }

    public void registerEvents() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new BlockPlace(config,langManager), plugin);
        pm.registerEvents(new DropItem(config,langManager), plugin);
    }
}
