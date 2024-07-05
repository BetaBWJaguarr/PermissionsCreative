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



/**
 * The ListGUI class provides a graphical user interface (GUI) for managing and displaying regions within a Minecraft plugin.
 * This class handles the creation, pagination, and interaction of region lists, allowing players to navigate through regions
 * based on their selections.
 *
 * <p>This GUI is created using the Bukkit API and includes pagination support through the PaginationService and Navigation classes.</p>
 *
 * <p>Class Functionality:
 * <ul>
 *     <li>Manages configuration settings using the {@link Config} class to retrieve region data based on player selections.</li>
 *     <li>Creates an inventory named "Regions Menu" to display region items, with pagination support.</li>
 *     <li>Handles the initialization and updating of inventory items, ensuring proper pagination and navigation between pages.</li>
 *     <li>Provides methods to open the GUI for players, manage current pages, and handle item interactions.</li>
 * </ul>
 * </p>
 *
 * <p>This class utilizes the following dependencies:
 * <ul>
 *     <li>{@link Config} - Manages configuration settings for the plugin.</li>
 *     <li>{@link PaginationService} - Handles pagination logic for the GUI items.</li>
 *     <li>{@link Navigation} - Manages navigation buttons for pagination.</li>
 *     <li>{@link ItemsEnum} - Enum to define and manage item materials for the GUI.</li>
 * </ul>
 * </p>
 *
 * <p>Methods Overview:
 * <ul>
 *     <li>{@link #getRegionsBasedOnSelection(String)} - Retrieves a list of regions based on the player's selection from the configuration.</li>
 *     <li>{@link #createMenu(String)} - Creates and initializes the region menu inventory based on the player's selection.</li>
 *     <li>{@link #addItemsToInventory(List)} - Adds a list of items to the inventory and handles pagination setup.</li>
 *     <li>{@link #openMenu(Player)} - Opens the region menu for a player, managing pagination and navigation buttons.</li>
 *     <li>{@link #getInventory()} - Returns the current inventory.</li>
 *     <li>{@link #getPagination()} - Returns the pagination service instance.</li>
 *     <li>{@link #getItemManager()} - Returns the item manager service instance.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * ListGUI listGUI = new ListGUI(config, paginationService);
 * listGUI.createMenu("worlds");
 * Player player = // get player instance;
 * listGUI.openMenu(player);
 * }
 * </pre>
 * </p>
 */

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

        int currentPage = paginationService.getCurrentPageForPlayer(playerId);
        int totalItems = paginationService.getItemManager().getItems().size();
        int totalPages = (int) Math.ceil((double) totalItems / paginationService.getPageSize());

        if (currentPage >= totalPages) {
            currentPage = totalPages - 1;
            if (currentPage < 0) {
                currentPage = 0;
            }
            paginationService.setPageForPlayer(playerId, currentPage);
        }

        inventory.clear();

        List<ItemStack> pageItems = paginationService.getCurrentPageItems(playerId);
        int itemCount = pageItems != null ? pageItems.size() : 0;

        if (itemCount == 0) {
            player.openInventory(inventory);
            return;
        }

        int slot = 0;
        int maxItems = 8;

        if (paginationService.hasPreviousPage(playerId)) {
            inventory.setItem(slot++, navigation.createPreviousPageButton(playerId));
        }

        for (int i = 0; i < Math.min(itemCount, maxItems); i++) {
            inventory.setItem(slot++, pageItems.get(i));
        }

        if (paginationService.hasNextPage(playerId)) {
            inventory.setItem(8, navigation.createNextPageButton(playerId));
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
