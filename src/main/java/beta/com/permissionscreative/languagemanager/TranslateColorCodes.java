package beta.com.permissionscreative.languagemanager;

import org.bukkit.ChatColor;

public class TranslateColorCodes {

    public static String translateHexColorCodes(String startTag, String message) {
        int startIndex = message.indexOf(startTag);
        while (startIndex != -1) {
            int endIndex = calculateEndIndex(startIndex);
            if (endIndex > message.length()) {
                break;
            }
            String colorCode = extractColorCode(message, startIndex, endIndex);
            message = replaceColorCodeInMessage(message, colorCode);
            startIndex = findNextColorCode(message, startTag, endIndex);
        }
        return message;
    }

    private static int calculateEndIndex(int startIndex) {
        return startIndex + 7;
    }

    private static String extractColorCode(String message, int startIndex, int endIndex) {
        return message.substring(startIndex, endIndex);
    }

    private static String replaceColorCodeInMessage(String message, String colorCode) {
        try {
            net.md_5.bungee.api.ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(colorCode);
            String legacyColor = ChatColor.getLastColors(hexColor.toString());
            message = message.replace(colorCode, legacyColor);
        } catch (Exception ignored) {
        }
        return message;
    }

    private static int findNextColorCode(String message, String startTag, int fromIndex) {
        return message.indexOf(startTag, fromIndex);
    }
}