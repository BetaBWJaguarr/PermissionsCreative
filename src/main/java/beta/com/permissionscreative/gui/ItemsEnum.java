package beta.com.permissionscreative.gui;

import org.bukkit.Material;

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
