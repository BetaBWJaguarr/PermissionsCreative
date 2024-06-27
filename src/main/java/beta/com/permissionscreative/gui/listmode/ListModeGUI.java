package beta.com.permissionscreative.gui.listmode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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