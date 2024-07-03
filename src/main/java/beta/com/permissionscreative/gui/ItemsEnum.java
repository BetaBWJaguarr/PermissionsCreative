package beta.com.permissionscreative.gui;

import org.bukkit.Material;

/**
 * The ItemsEnum enum lists various Minecraft materials as items.
 * Each enum constant corresponds to a specific Material representing an item.
 *
 * <p>
 * The enum includes constants for:
 * - BOOK
 * - GLOWING_ITEM_FRAME
 * - DIAMOND
 * - IRON_INGOT
 * - GOLD_INGOT
 * - STICK
 * - BONE
 * - COAL
 * - REDSTONE
 *
 * <p>
 * Each enum constant has a corresponding Material field that represents the Minecraft material type
 * of the item.
 *
 * <p>
 * The enum provides a method {@code getMaterial()} to retrieve the Material associated with each
 * enum constant.
 */

public enum ItemsEnum {
    BOOK(Material.BOOK),
    GLOWING_ITEM_FRAME(Material.GLOW_ITEM_FRAME),
    DIAMOND(Material.DIAMOND),
    IRON_INGOT(Material.IRON_INGOT),
    GOLD_INGOT(Material.GOLD_INGOT),
    STICK(Material.STICK),
    BONE(Material.BONE),
    COAL(Material.COAL),
    REDSTONE(Material.REDSTONE);

    private final Material material;

    ItemsEnum(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
