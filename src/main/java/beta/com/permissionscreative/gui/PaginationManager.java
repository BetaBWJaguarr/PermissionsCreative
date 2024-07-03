package beta.com.permissionscreative.gui;

import beta.com.paginationapi.itemmanager.ItemManager;
import beta.com.paginationapi.itemmanager.service.ItemManagerService;
import beta.com.paginationapi.itemmanager.service.impl.ItemManagerServiceImpl;
import beta.com.paginationapi.page.Pagination;
import beta.com.paginationapi.page.service.PaginationService;
import beta.com.paginationapi.page.service.impl.PaginationServiceImpl;

/**
 * The PaginationManager class manages pagination functionality using PaginationAPI.
 * It initializes services for item management and pagination configuration.
 *
 * <p>
 * The class includes:
 * - {@code itemManagerService}: An instance of {@link ItemManagerService} for item management.
 * - {@code paginationService}: An instance of {@link PaginationService} for pagination configuration.
 *
 * <p>
 * Upon instantiation, the PaginationManager initializes {@code itemManagerService} with
 * {@link ItemManagerServiceImpl}, and {@code paginationService} with {@link PaginationServiceImpl}.
 * The pagination service is configured with a page size of 8 and the item manager service instance.
 *
 * <p>
 * The {@code createPagination()} method creates a new pagination instance using the configured
 * {@code itemManagerService} and {@code paginationService}. It returns a {@link Pagination} object
 * that manages the paginated display of items.
 *
 * <p>
 * The {@code getPaginationService()} method provides access to the {@code paginationService}
 * instance, allowing external components to interact with pagination configurations and services.
 */

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