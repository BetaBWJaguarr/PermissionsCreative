package beta.com.permissionscreative.gui.worldsregions.choicesmenu.listmenu;

import beta.com.paginationapi.itemmanager.ItemManager;
import beta.com.paginationapi.itemmanager.service.ItemManagerService;
import beta.com.paginationapi.itemmanager.service.impl.ItemManagerServiceImpl;
import beta.com.paginationapi.page.Pagination;
import beta.com.paginationapi.page.service.PaginationService;
import beta.com.paginationapi.page.service.impl.PaginationServiceImpl;

public class PaginationManager {
    private final ItemManagerService itemManagerService;
    private final PaginationService paginationService;

    public PaginationManager() {
        this.itemManagerService = new ItemManagerServiceImpl();
        this.paginationService = new PaginationServiceImpl(8, itemManagerService);
    }

    public Pagination createPagination() {
        ItemManager itemManager = itemManagerService.createItemManager();
        return paginationService.createPagination();
    }

    public PaginationService getPaginationService() {
        return paginationService;
    }
}