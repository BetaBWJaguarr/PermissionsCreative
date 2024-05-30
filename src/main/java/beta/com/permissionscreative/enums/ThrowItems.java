package beta.com.permissionscreative.enums;

import org.bukkit.Material;

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