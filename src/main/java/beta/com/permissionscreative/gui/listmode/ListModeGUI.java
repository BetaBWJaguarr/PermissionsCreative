package beta.com.permissionscreative.gui.listmode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The ListModeGUI class represents a graphical user interface (GUI) for selecting different modes in a Bukkit/Spigot Minecraft plugin.
 * It creates and initializes an inventory with predefined items that players can interact with to select specific modes.
 *
 * <p>The GUI consists of an inventory with a fixed size of 9 slots, where each slot contains an item representing a mode.
 * The available modes are "Build Mode", "Throw Mode", "Drop Mode", and "Break Mode", each associated with a different material.</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Creates an inventory named "List Mode GUI" with 9 slots using the Bukkit API.</li>
 *     <li>Initializes the inventory with specific items in designated slots, each item representing a mode.</li>
 *     <li>Provides a method to retrieve the inventory for further interactions.</li>
 * </ul>
 * </p>
 *
 * <p>The following methods are used to achieve the functionality:
 * <ul>
 *     <li>{@link #initializeItems()} - Initializes the inventory items with specific materials and names.</li>
 *     <li>{@link #addItem(Material, String, int)} - Adds an item to the inventory at a specified slot with a given name and material.</li>
 *     <li>{@link #getInventory()} - Returns the initialized inventory for interaction.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * ListModeGUI listModeGUI = new ListModeGUI();
 * Inventory inventory = listModeGUI.getInventory();
 * // Further code to open the inventory for a player
 * }
 * </pre>
 * </p>
 */

public class ListModeGUI {
    private Inventory inventory;

    public ListModeGUI() {
        this.inventory = Bukkit.createInventory(null, 9, "List Mode GUI");
        initializeItems();
    }

    private void initializeItems() {
        addItem(Material.REDSTONE, "Build Mode", 0);
        addItem(Material.EMERALD, "Throw Mode", 1);
        addItem(Material.DIAMOND, "Drop Mode", 2);
        addItem(Material.GOLD_INGOT, "Break Mode", 3);
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