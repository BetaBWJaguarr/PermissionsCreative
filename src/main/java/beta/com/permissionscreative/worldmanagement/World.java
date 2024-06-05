package beta.com.permissionscreative.worldmanagement;

import org.bukkit.Server;

import java.util.ArrayList;
import java.util.List;

public class World {
    private List<org.bukkit.World> allowedWorlds;

    public World(Server server, List<String> worldNames) {
        this.allowedWorlds = new ArrayList<>();
        for (String worldName : worldNames) {
            org.bukkit.World world = server.getWorld(worldName);
            if (world != null) {
                this.allowedWorlds.add(world);
            }
        }
    }

    public boolean isWorldAllowed(org.bukkit.World world) {
        return allowedWorlds.contains(world);
    }
}
