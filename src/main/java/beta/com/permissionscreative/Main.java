package beta.com.permissionscreative;

import beta.com.permissionscreative.commands.ReloadCommands;
import beta.com.permissionscreative.commands.SettingsCommands;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.CommandsRegister;
import beta.com.permissionscreative.utils.RegisterListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Config config;

    private RegisterListener registerListener;

    private LangManager langManager;

    private CommandsRegister commandsRegister;

    @Override
    public void onEnable() {
        config = new Config(this);
        langManager = new LangManager();
        registerListener = new RegisterListener(this,config,langManager);
        registerListener.registerEvents();

        commandsRegister = new CommandsRegister(config,langManager,this);
        commandsRegister.registerCommands();

    }

    @Override
    public void onDisable() {
    }
}
