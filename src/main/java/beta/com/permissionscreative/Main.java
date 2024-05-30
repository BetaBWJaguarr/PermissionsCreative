package beta.com.permissionscreative;

import beta.com.permissionscreative.commands.ReloadCommands;
import beta.com.permissionscreative.commands.SettingsCommands;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.CommandsRegister;
import beta.com.permissionscreative.utils.RegisterListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {

    private Config config;

    private RegisterListener registerListener;

    private LangManager langManager;

    private CommandsRegister commandsRegister;

    @Override
    public void onEnable() {
        config = new Config(this);
        ArrayList<String> langCodes = new ArrayList<>();
        langCodes.add("en");
        langCodes.add("tr");
        langManager = new LangManager(langCodes,this);
        registerListener = new RegisterListener(this,config,langManager);
        registerListener.registerEvents();

        commandsRegister = new CommandsRegister(config,langManager,this);
        commandsRegister.registerCommands();

        DatabaseManager databaseManager = new DatabaseManager(this,config);
        InventoryManager inventoryManager = new InventoryManager(databaseManager,config);
        try {
            databaseManager.connect();
            databaseManager.startSavingTask(inventoryManager);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
    }
}
