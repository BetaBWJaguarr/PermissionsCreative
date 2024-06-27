package beta.com.permissionscreative.gui.listmode.editmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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