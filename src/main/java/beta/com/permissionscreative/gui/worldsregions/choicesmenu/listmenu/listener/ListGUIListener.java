package beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.listener;

import beta.com.paginationapi.listener.PaginationListener;
import beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu.ListGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class ListGUIListener implements Listener {
    private final ListGUI listGUI;
    private PaginationListener paginationListener;

    public ListGUIListener(ListGUI listGUI) {
        this.listGUI = listGUI;
        this.paginationListener = new PaginationListener(listGUI.getPagination(), listGUI.getItemManager());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(listGUI.getInventory())) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                if (itemName.equals("Next Page")) {
                    paginationListener.onPageAction(player,true);
                    listGUI.openMenu(player);
                } else if (itemName.equals("Previous Page")) {
                    paginationListener.onPageAction(player,false);
                    listGUI.openMenu(player);
                }
            }
        }
    }
}