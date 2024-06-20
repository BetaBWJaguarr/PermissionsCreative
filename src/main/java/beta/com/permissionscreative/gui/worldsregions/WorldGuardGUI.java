package beta.com.permissionscreative.gui.worldsregions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WorldGuardGUI {

    private Inventory inventory;

    public WorldGuardGUI() {
        inventory = Bukkit.createInventory(null, 9, "WorldGuard Menu");


        ItemStack worldItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta worldMeta = worldItem.getItemMeta();
        worldMeta.setDisplayName(ChatColor.AQUA + "Worlds");
        worldItem.setItemMeta(worldMeta);


        inventory.setItem(3, worldItem);


        ItemStack rItem = new ItemStack(Material.REDSTONE);
        ItemMeta rMeta = rItem.getItemMeta();
        rMeta.setDisplayName(ChatColor.BLUE + "Regions");
        rItem.setItemMeta(rMeta);


        inventory.setItem(5, rItem);


        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Main Menu");
        backItem.setItemMeta(backMeta);


        inventory.setItem(0, backItem);

    }


    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}