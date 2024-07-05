package beta.com.permissionscreative.gui.listmode.editmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * The EditGUI class represents a graphical user interface (GUI) for editing items in a Bukkit/Spigot Minecraft plugin.
 *
 * <p>This GUI provides options for adding, removing, and changing items. These options are presented as clickable
 * items in an inventory menu, which can be accessed by players. Each item in the inventory is represented by a different
 * type of wool: green for "Add", red for "Remove", and yellow for "Change".</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Creates an inventory menu with specific items representing actions (Add, Remove, Change).</li>
 *     <li>Handles the logic for initializing these items in the inventory. This includes creating the ItemStacks, setting the display names, and placing the items in the correct slots.</li>
 *     <li>Provides methods to retrieve the inventory. This allows other parts of the plugin to interact with the GUI, such as opening it for a player.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * EditGUI editGUI = new EditGUI();
 * Player player = Bukkit.getPlayer("playerName");
 * player.openInventory(editGUI.getInventory());
 * }
 * </pre>
 * </p>
 *
 * <p>This class is a crucial part of the plugin's user interface. It allows players to easily manage
 * their items without needing to use complex commands or other interfaces. The simplicity and intuitiveness of the GUI make it a
 * user-friendly way for players to interact with the plugin.</p>
 */


public class EditGUI {
    private Inventory inventory;

    public EditGUI() {
        this.inventory = Bukkit.createInventory(null, 9, "Edit Mode GUI");
        initializeItems();
    }

    private void initializeItems() {
        addItem(Material.GREEN_WOOL, "Add", 2);
        addItem(Material.RED_WOOL, "Remove", 4);
        addItem(Material.YELLOW_WOOL, "Change", 6);
    }

    private void addItem(Material material, String name, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + name);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    public Inventory getInventory() {
        return inventory;
    }
}