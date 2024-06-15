package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.languagemanager.TranslateColorCodes;
import beta.com.permissionscreative.worldmanagement.Regions;
import beta.com.permissionscreative.worldmanagement.World;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

/**
 * The EventsManager class, part of the beta.com.permissionscreative.utils package, is a comprehensive utility class that manages various events and checks within the plugin. It is designed to handle a variety of tasks related to permissions, world checks, and region checks.
 *
 * The class is initialized with instances of Config, LangManager, and Plugin, which are used throughout its methods.
 * The Config instance provides access to the plugin’s configuration settings, the LangManager instance handles language-specific messages, and the Plugin instance provides access to the server and plugin data.
 * One of the key methods in this class is checkAndSendMessage. This method checks the player’s game mode and permissions,
 * and sends a message to the player if certain conditions are met. This is particularly useful for managing player interactions
 * and ensuring they have the correct permissions for certain actions.The checkWorlds method is another important feature of this class.
 * It checks if worlds are enabled in the configuration and, if so, creates a new World instance with the server and world names
 * from the configuration. This allows the plugin to interact with specific worlds on the server.The WorldguardCheck
 * method integrates with the WorldGuard plugin to check if a player is in a region that allows certain actions. It does this by
 * querying the applicable regions at the player’s location and checking if any of these regions are allowed in the configuration.
 * Finally, the checkProtection method checks if world protection is enabled in the configuration and, if so, checks if the player’s world is allowed and if the player is in an allowed region. This method is crucial for managing world and region protections and ensuring players can only perform actions in allowed areas.
 *
 * Overall, the EventsManager class is a robust utility class that handles a variety of checks and actions related to permissions, worlds, and regions. It is a key component of the plugin’s functionality and plays a crucial role in managing player interactions and protections. Its methods are designed to work together to provide a comprehensive system for managing events and checks within the plugin. The use of the Config, LangManager, and Plugin instances ensures that the class can interact effectively with the server, the plugin’s configuration, and language-specific messages. This makes the EventsManager class a versatile and valuable part of the plugin’s architecture.
 */

public class EventsManager {

    private Config config;
    private LangManager langManager;
    private Plugin plugin;
    private World world;

    public EventsManager(Config config, LangManager langManager, Plugin plugin) {
        this.config = config;
        this.langManager = langManager;
        this.plugin = plugin;
    }

    public boolean checkAndSendMessage(Player player, GameMode gameMode, boolean permissionEnabled, String permission, String messageKey, String action, String item) {
        if (player.getGameMode() != gameMode || !permissionEnabled || player.hasPermission(permission)) {
            return false;
        }

        String mode = config.getConfig().getString("list.mode." + action);
        List<String> items = config.getConfig().getStringList("list." + action);

        boolean result = false;
        if (items.isEmpty() || items.contains("")) {
            result = true;
        } else {
            for (String listItem : items) {
                if (listItem.contains("*_")) {
                    String listItemSuffix = listItem.substring(listItem.lastIndexOf("_") + 1);
                    String itemSuffix = item.contains("_") ? item.substring(item.lastIndexOf("_") + 1) : "";

                    if (mode.equals("whitelist")) {
                        if (listItemSuffix.equals(itemSuffix)) {
                            result = false;
                            break;
                        } else {
                            result = true;
                        }
                    } else if (mode.equals("blacklist")) {
                        if (listItemSuffix.equals(itemSuffix)) {
                            result = true;
                            break;
                        }
                    }
                } else {
                    if (mode.equals("whitelist")) {
                        if (listItem.equals(item)) {
                            result = false;
                            break;
                        } else {
                            result = true;
                        }
                    } else if (mode.equals("blacklist")) {
                        if (listItem.equals(item)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }

        if (result) {
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
            prefix = TranslateColorCodes.translateHexColorCodes("#", prefix);
            String message = langManager.getMessage(messageKey, config.getConfig().getString("lang"));
            player.sendMessage(prefix + " " + message);
        }

        return result;
    }

    public World checkWorlds() {
        List<String> worldNames = config.getConfig().getStringList("worlds.regions");
        boolean worldsEnabled = config.getConfig().getBoolean("worlds.enabled");
        if (!worldsEnabled) {
            return null;
        }
        world = new World(plugin.getServer(), worldNames);
        return world;
    }

    //WorldGuard Region Check

    public boolean WorldguardCheck(Player player) {
        Regions regions = new Regions(config, plugin.getServer());

        Location loc = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);

        return set.getRegions().stream().anyMatch(region -> regions.areRegionsAllowed(Arrays.asList(region.getId())));
    }

    //Worlds and Worldguard Region protection status

    public boolean checkProtection(Player player, World world, boolean isPlayerInRegion) {
        boolean worldEnabled = config.getConfig().getBoolean("worlds.enabled");
        boolean worldGuardEnabled = config.getConfig().getBoolean("world-guard.enabled");

        return (!worldEnabled || world != null && world.isWorldAllowed(player.getWorld())) && (!worldGuardEnabled || isPlayerInRegion);
    }
}
