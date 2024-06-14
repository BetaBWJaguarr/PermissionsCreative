package beta.com.permissionscreative.languagemanager;

import org.bukkit.ChatColor;

public class TranslateColorCodes {

    public static String translateHexColorCodes(String startTag, String message) {
        int startIndex = message.indexOf(startTag);
        while (startIndex != -1) {
            int endIndex = startIndex + 7;
            if (endIndex > message.length()) {
                break;
            }
            String colorCode = message.substring(startIndex, endIndex);
            try {
                net.md_5.bungee.api.ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(colorCode);
                String legacyColor = ChatColor.getLastColors(hexColor.toString());
                message = message.replace(colorCode, legacyColor);
            } catch (Exception ignored) {
            }
            startIndex = message.indexOf(startTag, endIndex);
        }
        return message;
    }
}
