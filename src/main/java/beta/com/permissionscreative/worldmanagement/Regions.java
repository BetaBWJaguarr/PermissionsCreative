package beta.com.permissionscreative.worldmanagement;

import beta.com.permissionscreative.configuration.Config;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * The `Regions` class is a part of the permissionscreative world management package, designed to work with the
 * WorldGuard plugin to manage region-based permissions in a Minecraft server.
 * It provides methods to check if certain regions are allowed by consulting the server's configuration and the WorldGuard API.
 * This class ensures that only permitted regions are accessible for gameplay and administrative actions, according to the
 * server's rules.
 *
 * @param config The Config object that holds the settings and configurations for the world-guard, including enabled status
 * and a list of blocked regions.
 * @param server The server instance where the regions are managed and checked against the WorldGuard plugin.
 *
 * The class contains two main methods:
 * - areRegionsAllowed: Public method that takes a list of region names and returns a boolean indicating
 * if those regions are allowed according to the world-guard settings in the config.
 * - checkRegions: Private method that performs the actual check against the WorldGuard API to determine
 * if the regions are blocked or not.
 */


public class Regions {
    private Config config;
    private Server server;

    public Regions(Config config, Server server) {
        this.config = config;
        this.server = server;
    }

    public boolean areRegionsAllowed(List<String> regionNames) {
        if (!config.getConfig().getBoolean("world-guard.enabled")) {
            return false;
        }

        Plugin plugin = server.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return false;
        }

        return checkRegions(regionNames);
    }

    private boolean checkRegions(List<String> regionNames) {
        List<String> blockedRegions = config.getConfig().getStringList("world-guard.regions");

        for (String regionName : regionNames) {
            if (regionName == null) {
                System.out.println("Warning: Encountered null region ID");
                continue;
            }
            for (org.bukkit.World world : server.getWorlds()) {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                World wgWorld = BukkitAdapter.adapt(world);
                RegionManager regionManager = container.get(wgWorld);
                ProtectedRegion region = regionManager.getRegion(regionName);
                if (region != null && blockedRegions.contains(region.getId())) {
                    return true;
                }
            }
        }

        return false;
    }
}
