package beta.com.permissionscreative;

import beta.com.permissionscreative.commands.ReloadCommands;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.RegisterListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Config config;

    private RegisterListener registerListener;

    private LangManager langManager;

    @Override
    public void onEnable() {
        config = new Config(this);
        langManager = new LangManager();
        getCommand("permissions-creative").setExecutor(new ReloadCommands(config,langManager));
        registerListener = new RegisterListener(this,config,langManager);
        registerListener.registerEvents();

    }

    @Override
    public void onDisable() {
    }
}
