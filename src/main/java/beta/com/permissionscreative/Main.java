package beta.com.permissionscreative;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.discord.DiscordBot;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.inventorymanager.InventoryManager;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.utils.CommandsRegister;
import beta.com.permissionscreative.utils.EventsManager;
import beta.com.permissionscreative.utils.Logger;
import beta.com.permissionscreative.utils.RegisterListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {

    private Config config;

    private RegisterListener registerListener;

    private LangManager langManager;

    private CommandsRegister commandsRegister;

    private EventsManager eventsManager;

    private DiscordBot discordBot;
    private DiscordLogAction discordLogAction;

    private Logger logger;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PaginationAPI") == null) {
            getLogger().severe("PaginationAPI not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        config = new Config(this);
        ArrayList<String> langCodes = new ArrayList<>();
        langCodes.add("en");
        langCodes.add("tr");
        langManager = new LangManager(langCodes,this);
        eventsManager = new EventsManager(config,langManager,this);

        if (config.getConfig().getBoolean("logging.discordbot.enabled")) {
            discordBot = new DiscordBot(config.getConfig().getString("logging.discordbot.token"));
            discordLogAction = new DiscordLogAction(discordBot,config);
        }

        logger = new Logger(config,langManager,this);

        registerListener = new RegisterListener(this,config,langManager,eventsManager,discordLogAction,logger);
        registerListener.registerEvents();

        DatabaseManager databaseManager = new DatabaseManager(this,config);
        InventoryManager inventoryManager = new InventoryManager(databaseManager,config);

        commandsRegister = new CommandsRegister(config,langManager,this,databaseManager,inventoryManager);
        commandsRegister.registerCommands();

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
