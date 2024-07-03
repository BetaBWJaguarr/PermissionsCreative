package beta.com.permissionscreative.enums;

import org.bukkit.Material;

/**
 * The ThrowItems enum lists various throwable items in Minecraft.
 * Each enum constant corresponds to a specific Material representing a throwable item.
 *
 * <p>
 * The enum includes constants for:
 * - ENDER_PEARL
 * - EYE_OF_ENDER
 * - SNOWBALL
 * - EGG
 * - POTION
 * - SPLASH_POTION
 * - LINGERING_POTION
 * - EXPERIENCE_BOTTLE
 * - FIRE_CHARGE
 *
 * <p>
 * Each enum constant has a corresponding Material field that represents the Minecraft material type
 * of the throwable item.
 *
 * <p>
 * The enum provides a method {@code getMaterial()} to retrieve the Material associated with each
 * enum constant.
 *
 * <p>
 * Additionally, the enum includes a static method {@code isThrowItem(Material material)} that checks
 * if a given Material is a throwable item listed in the enum. It iterates through all enum constants
 * and returns true if the provided Material matches any of the throwable items.
 */

public enum ThrowItems {
    ENDER_PEARL(Material.ENDER_PEARL),
    EYE_OF_ENDER(Material.ENDER_EYE),
    SNOWBALL(Material.SNOWBALL),
    EGG(Material.EGG),
    POTION(Material.POTION),
    SPLASH_POTION(Material.SPLASH_POTION),
    LINGERING_POTION(Material.LINGERING_POTION),
    EXPERIENCE_BOTTLE(Material.EXPERIENCE_BOTTLE),
    FIRE_CHARGE(Material.FIRE_CHARGE);

    private final Material material;

    ThrowItems(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public static boolean isThrowItem(Material material) {
        for (ThrowItems item : ThrowItems.values()) {
            if (item.getMaterial() == material) {
                return true;
            }
        }
        return false;
    }
}