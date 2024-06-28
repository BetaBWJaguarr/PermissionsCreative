package beta.com.permissionscreative.gui.listmode.editmenu.listmenu;

import beta.com.paginationapi.navigation.Navigation;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.PaginationManager;
import beta.com.permissionscreative.gui.listmode.listener.ListModeGUIListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EditListGUI {
    private Inventory inventory;
    private PaginationManager pagination;
    private ListModeGUIListener listModeGUIListener;
    private Config config;
    private Navigation navigation;

    public EditListGUI(PaginationManager pagination, ListModeGUIListener listModeGUIListener, Config config) {
        this.pagination = pagination;
        this.listModeGUIListener = listModeGUIListener;
        this.config = config;
        this.navigation = new Navigation(pagination.getPaginationService());
    }

    public void GUI(Player player) {
        inventory = Bukkit.createInventory(null, 18, "Edit List");

        inventory.clear();
        pagination.getPaginationService().getItemManager().clearItems();


        String playerSelection = listModeGUIListener.getPlayerSelection(player.getUniqueId());


        List<String> itemsList = config.getConfig().getStringList("list." + playerSelection);


        List<ItemStack> items;

        if (itemsList.contains("")) {
            items = new ArrayList<>();
        } else {
            items = itemsList.stream()
                    .map(itemName -> {
                        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta meta = (BookMeta) book.getItemMeta();
                        meta.setTitle(itemName);
                        book.setItemMeta(meta);
                        return book;
                    })
                    .collect(Collectors.toList());
        }


        for (ItemStack item : items) {
            pagination.getPaginationService().getItemManager().addItem(item);
        }

        UUID playerId = player.getUniqueId();

        adjustCurrentPageIfNecessary(playerId);

        inventory.clear();

        List<ItemStack> pageItems = pagination.getPaginationService().getCurrentPageItems(playerId);
        int itemCount = pageItems != null ? pageItems.size() : 0;

        if (itemCount == 0) {
            player.openInventory(inventory);
            return;
        }

        populateInventoryWithPageItems(playerId, pageItems);

        player.openInventory(inventory);
    }


    private void adjustCurrentPageIfNecessary(UUID playerId) {
        int currentPage = pagination.getPaginationService().getCurrentPageForPlayer(playerId);
        int totalItems = pagination.getPaginationService().getItemManager().getItems().size();
        int totalPages = (int) Math.ceil((double) totalItems / pagination.getPaginationService().getPageSize());

        if (currentPage >= totalPages) {
            currentPage = totalPages - 1;
            if (currentPage < 0) {
                currentPage = 0;
            }
            pagination.getPaginationService().setPageForPlayer(playerId, currentPage);
        }
    }


    private void populateInventoryWithPageItems(UUID playerId, List<ItemStack> pageItems) {
        int slot = 0;
        int maxItems = 8;

        if (pagination.getPaginationService().hasPreviousPage(playerId)) {
            inventory.setItem(slot++, navigation.createPreviousPageButton(playerId));
        }

        for (int i = 0; i < Math.min(pageItems.size(), maxItems); i++) {
            inventory.setItem(slot++, pageItems.get(i));
        }

        if (pagination.getPaginationService().hasNextPage(playerId)) {
            inventory.setItem(8, navigation.createNextPageButton(playerId));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PaginationManager getPagination() {
        return pagination;
    }

    public ListModeGUIListener getListModeGUIListener() {
        return listModeGUIListener;
    }

    public Config getConfig() {
        return config;
    }
}