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
