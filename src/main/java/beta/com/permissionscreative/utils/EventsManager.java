package beta.com.permissionscreative.utils;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.worldmanagement.Regions;
import beta.com.permissionscreative.worldmanagement.World;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

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

    public boolean checkAndSendMessage(Player player, GameMode gameMode, boolean permissionEnabled, String permission, String messageKey) {
        if (player.getGameMode() == gameMode && permissionEnabled && !player.hasPermission(permission)) {
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("prefix"));
            String message = langManager.getMessage(messageKey, config.getConfig().getString("lang"));
            player.sendMessage(prefix + " " + message);
            return true;
        }
        return false;
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

    public boolean WorldguardCheck(Player player) {
        Regions regions = new Regions(config, plugin.getServer());

        Location loc = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);

        for (ProtectedRegion region : set) {
            if (regions.areRegionsAllowed(Arrays.asList(region.getId()))) {
                return true;
            }
        }

        return false;
    }

    public boolean checkProtection(Player player, World world, boolean isPlayerInRegion) {
        boolean worldEnabled = config.getConfig().getBoolean("worlds.enabled");
        boolean worldGuardEnabled = config.getConfig().getBoolean("world-guard.enabled");

        if (worldEnabled) {
            if (world == null || !world.isWorldAllowed(player.getWorld())) {
                return false;
            }
        }

        if (worldGuardEnabled) {
            if (!isPlayerInRegion) {
                return false;
            }
        }

        return true;
    }
}
