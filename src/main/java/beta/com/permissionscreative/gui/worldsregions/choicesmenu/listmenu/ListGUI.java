package beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.ItemsEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ListGUI {
    private Config config;
    private Inventory inventory;

    public ListGUI(Config config) {
        this.config = config;
    }

    public List<String> getRegionsBasedOnSelection(String selection) {
        List<String> regions = new ArrayList<>();
        if (selection.equals("worlds")) {
            regions = config.getConfig().getStringList("worlds.regions");
        } else if (selection.equals("world-guard")) {
            regions = config.getConfig().getStringList("world-guard.regions");
        }
        return regions;
    }

    public void createMenu(String selection) {
        List<String> regions = getRegionsBasedOnSelection(selection);
        inventory = Bukkit.createInventory(null, (int) Math.ceil(regions.size() / 9.0) * 9, "Regions Menu");

        for (String region : regions) {
            ItemStack paper = new ItemStack(ItemsEnum.BOOK.getMaterial());
            ItemMeta meta = paper.getItemMeta();
            meta.setDisplayName(region);
            paper.setItemMeta(meta);
            inventory.addItem(paper);
        }
    }

    public void openMenu(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}