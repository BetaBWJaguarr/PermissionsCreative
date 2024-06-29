package beta.com.permissionscreative.gui.listmode.editmenu.listmenu.listener;

import beta.com.paginationapi.listener.PaginationListener;
import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.gui.listmode.editmenu.listmenu.EditListGUI;
import beta.com.permissionscreative.languagemanager.LangManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class EditListGUIListener implements Listener {
    private EditListGUI editListGUI;
    private LangManager langManager;
    private PaginationListener paginationListener;
    private Config config;

    public EditListGUIListener(EditListGUI editListGUI, LangManager langManager, Config config) {
        this.editListGUI = editListGUI;
        this.paginationListener = new PaginationListener(editListGUI.getPagination().getPaginationService(),editListGUI.getPagination().getPaginationService().getItemManager());

        this.langManager = langManager;
        this.config = config;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !editListGUI.getInventory().equals(event.getClickedInventory())) {
            return;
        }

        event.setCancelled(true);

        String title = "";
        String displayName = "";
        if (clickedItem.getType() == Material.WRITTEN_BOOK) {
            BookMeta meta = (BookMeta) clickedItem.getItemMeta();
            title = meta.getTitle();
        } else {
            displayName = clickedItem.getItemMeta().getDisplayName();
        }

        if (displayName.equals("Next Page")) {
            paginationListener.onPageAction(player,true);
            editListGUI.GUI(player);
        } else if (displayName.equals("Previous Page")) {
            paginationListener.onPageAction(player,false);
            editListGUI.GUI(player);
        } else {
            String playerSelection = editListGUI.getListModeGUIListener().getPlayerSelection(player.getUniqueId());
            List<String> itemsList = editListGUI.getConfig().getConfig().getStringList("list." + playerSelection);
            itemsList.remove(title);
            editListGUI.getConfig().getConfig().set("list." + playerSelection, itemsList);
            editListGUI.getConfig().saveConfig();
            String message = langManager.getMessage("gui.editgui.removed", config.getConfig().getString("lang"))
                    .replace("{item}", title)
                    .replace("{selection}", playerSelection);
            player.sendMessage(message);

            player.closeInventory();
        }
    }
}