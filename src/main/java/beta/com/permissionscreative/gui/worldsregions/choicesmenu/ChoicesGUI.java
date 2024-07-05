package beta.com.permissionscreative.gui.worldsregions.choicesmenu;

import beta.com.permissionscreative.gui.worldsregions.WorldGuardGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * The ChoicesGUI class represents a graphical user interface (GUI) for selecting options related to
 * WorldGuard regions in a Bukkit/Spigot Minecraft plugin.
 *
 * <p>This GUI provides options for adding, removing, enabling/disabling, and navigating back to the main menu.
 * The choices are presented as clickable items in an inventory menu, which can be opened by a player.</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Creates an inventory menu with specific items representing actions (Add, Remove, Enable/Disable, Back to Main Menu).</li>
 *     <li>Handles the interaction logic to display this inventory to players.</li>
 *     <li>Provides methods to access the WorldGuardGUI instance and the created inventory.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * ChoicesGUI choicesGUI = new ChoicesGUI(worldGuardGUI);
 * choicesGUI.open(player);
 * }
 * </pre>
 * </p>
 */

public class ChoicesGUI {

    private Inventory inventory;

    private WorldGuardGUI worldGuardGUI;

    public ChoicesGUI(WorldGuardGUI worldGuardGUI) {
        this.worldGuardGUI = worldGuardGUI;
        inventory = Bukkit.createInventory(null, 9, "Choices Menu");

        ItemStack addItem = new ItemStack(Material.GREEN_WOOL);
        ItemMeta addMeta = addItem.getItemMeta();
        addMeta.setDisplayName(ChatColor.GREEN + "Add");
        addItem.setItemMeta(addMeta);

        inventory.setItem(3, addItem);

        ItemStack removeItem = new ItemStack(Material.RED_WOOL);
        ItemMeta removeMeta = removeItem.getItemMeta();
        removeMeta.setDisplayName(ChatColor.RED + "Remove");
        removeItem.setItemMeta(removeMeta);



        inventory.setItem(5, removeItem);

        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "Back to Main Menu");
        backItem.setItemMeta(backMeta);


        inventory.setItem(0, backItem);

        ItemStack toggleItem = new ItemStack(Material.LEVER);
        ItemMeta toggleMeta = toggleItem.getItemMeta();
        toggleMeta.setDisplayName(ChatColor.YELLOW + "Enable/Disable");
        toggleItem.setItemMeta(toggleMeta);

        inventory.setItem(7, toggleItem);

    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public WorldGuardGUI getWorldGuardGUI() {
        return worldGuardGUI;
    }

    public Inventory getInventory() {
        return inventory;
    }
}