package beta.com.permissionscreative.enums;

import org.bukkit.Material;

public enum ThrowItems {
    ENDER_PEARL(Material.ENDER_PEARL),
    FIREBALL(Material.FIRE_CHARGE);

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
