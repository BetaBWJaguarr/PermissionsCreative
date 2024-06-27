package beta.com.permissionscreative;

import beta.com.paginationapi.itemmanager.ItemManager;
import beta.com.paginationapi.itemmanager.service.ItemManagerService;
import beta.com.paginationapi.itemmanager.service.impl.ItemManagerServiceImpl;
import beta.com.paginationapi.page.Pagination;
import beta.com.paginationapi.page.service.PaginationService;
import beta.com.paginationapi.page.service.impl.PaginationServiceImpl;
import beta.com.paginationapi.search.SearchFunction;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.databasemanager.DatabaseManager;
import beta.com.permissionscreative.discord.DiscordBot;
import beta.com.permissionscreative.discord.actions.DiscordLogAction;
import beta.com.permissionscreative.gui.PaginationManager;
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

    private PaginationManager paginationManager;

    private PaginationManager ListModeGUI;

    private EventsManager eventsManager;

    private DiscordBot discordBot;

    private ItemManagerService itemManagerService;

    private PaginationService paginationService;

    private DiscordLogAction discordLogAction;

    private Logger logger;

    public Main getInstance() {
        return Main.this;
    }

    @Override
    public void onEnable() {
        checkDependencies();
        config = new Config(this);
        ArrayList<String> langCodes = new ArrayList<>();
        langCodes.add("en");
        langCodes.add("tr");
        langManager = new LangManager(langCodes,this);
        eventsManager = new EventsManager(config,langManager,this);


        logger = new Logger(config,langManager,this);

        reloadConfig();

        paginationManager = new PaginationManager();
        paginationManager.createPagination();

        ListModeGUI = new PaginationManager();
        ListModeGUI.createPagination();


        registerListener = new RegisterListener(this,config,langManager,eventsManager,discordLogAction,logger);
        registerListener.registerEvents();

        DatabaseManager databaseManager = new DatabaseManager(this,config);
        InventoryManager inventoryManager = new InventoryManager(databaseManager,config);



        itemManagerService = new ItemManagerServiceImpl();
        paginationService = new PaginationServiceImpl(8, itemManagerService);

        Pagination pagination = createPagination();

        SearchFunction searchFunction = new SearchFunction(paginationService,itemManagerService);
        getServer().getPluginManager().registerEvents(searchFunction, this);


        commandsRegister = new CommandsRegister(config,langManager,this,databaseManager,inventoryManager,paginationService,searchFunction,paginationManager,ListModeGUI);
        commandsRegister.registerCommands();

        try {
            databaseManager.connect();
            databaseManager.startSavingTask(inventoryManager);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void reloadConfig() {
        if (config.getConfig().getBoolean("logging.discordbot.enabled")) {
            discordBot = new DiscordBot(config.getConfig().getString("logging.discordbot.token"));
            discordLogAction = new DiscordLogAction(discordBot, config, langManager);
        }
    }

    private void checkDependencies() {
        String[] plugins = {"PaginationAPI", "WorldGuard", "Vault"};

        for (String plugin : plugins) {
            if (getServer().getPluginManager().getPlugin(plugin) == null) {
                getLogger().severe(plugin + " not found! Disabling plugin...");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
    }


    private Pagination createPagination() {
        ItemManager itemManager = itemManagerService.createItemManager();
        return paginationService.createPagination();
    }

    @Override
    public void onDisable() {
    }
}
