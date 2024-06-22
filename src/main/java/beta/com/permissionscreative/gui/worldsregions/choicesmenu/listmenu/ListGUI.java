package beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu;

import beta.com.paginationapi.itemmanager.service.ItemManagerService;
import beta.com.paginationapi.navigation.Navigation;
import beta.com.paginationapi.page.service.PaginationService;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.ItemsEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListGUI {
    private Config config;
    private Inventory inventory;
    private Navigation navigation;
    private PaginationService paginationService;

    public ListGUI(Config config, PaginationService paginationService) {
        this.config = config;
        this.navigation = new Navigation(paginationService);
        this.paginationService = paginationService;
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
        inventory = Bukkit.createInventory(null, 9, "Regions Menu");

        List<ItemStack> items = new ArrayList<>();
        for (String region : regions) {
            ItemStack paper = new ItemStack(ItemsEnum.BOOK.getMaterial());
            ItemMeta meta = paper.getItemMeta();
            meta.setDisplayName(region);
            paper.setItemMeta(meta);
            items.add(paper);
        }

        inventory.clear();
        paginationService.getItemManager().clearItems();

        addItemsToInventory(items);
    }

    public void addItemsToInventory(List<ItemStack> items) {
        for (ItemStack item : items) {
            paginationService.getItemManager().addItem(item);
        }
    }

    public void openMenu(Player player) {
        UUID playerId = player.getUniqueId();

        inventory.clear();

        List<ItemStack> pageItems = paginationService.getCurrentPageItems(playerId);
        int itemCount = pageItems.size();

        int slot = paginationService.hasPreviousPage(playerId) ? 1 : 0;
        for (ItemStack item : pageItems) {
            if (slot >= 8) break;
            inventory.setItem(slot, item);
            slot++;
        }

        if (paginationService.hasNextPage(playerId)) {
            inventory.setItem(8, navigation.createNextPageButton(playerId));
        }

        if (paginationService.hasPreviousPage(playerId)) {
            inventory.setItem(0, navigation.createPreviousPageButton(playerId));
        }

        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PaginationService getPagination() {
        return paginationService;
    }

    public ItemManagerService getItemManager() {
        return paginationService.getItemManager();
    }
}
