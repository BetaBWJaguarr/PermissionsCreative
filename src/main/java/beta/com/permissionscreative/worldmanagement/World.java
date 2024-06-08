package beta.com.permissionscreative.worldmanagement;

import org.bukkit.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * The `World` class is part of the permissionscreative world management package, designed to manage access to worlds within
 * a Minecraft server. It maintains a list of allowed worlds and provides a method to check if a world is permitted for activities.
 * This class is initialized with a list of world names, which it uses to populate the list of allowed worlds by verifying their
 * existence on the server. The primary functionality is to serve as a gatekeeper, ensuring that only approved worlds are accessible
 * for gameplay and administrative actions.
 * The class includes:
 * - A constructor that takes the server instance and a list of world names, populating the allowedWorlds list with valid world objects.
 * - The isWorldAllowed method, which checks if a given world object is in the list of allowed worlds, returning true if it is,
 * and false otherwise.
 */


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
