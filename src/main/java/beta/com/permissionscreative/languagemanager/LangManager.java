package beta.com.permissionscreative.languagemanager;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The LangManager class manages language files and translations in a Bukkit plugin.
 * It facilitates loading, copying, and reloading of language files, and provides methods
 * to retrieve messages in a specific language.
 *
 * <p>
 * The class maintains a map of languages, where each language is represented by a map of key-value pairs.
 * Each key in these maps represents a message identifier, and the corresponding value is the message text
 * in the respective language.
 *
 * <p>
 * Upon instantiation, LangManager loads language files based on provided language codes. It ensures the necessary
 * files exist by copying them from resources if absent. It specifically loads files for English ("en") and Turkish ("tr")
 * languages during initialization.
 *
 * <p>
 * The class supports reloading of language files through the {@link #reloadLanguageFiles()} method, which clears
 * the current language map and reloads all language files.
 *
 * <p>
 * Messages can be retrieved using {@link #getMessage(String, String)}. This method takes a message key and a language
 * code as parameters. It retrieves the message from the corresponding language map. If the requested language or message key
 * does not exist, it falls back to an error message in the requested language indicating either an unknown key or language.
 *
 * <p>
 * LangManager also supports translation of color codes using {@link TranslateColorCodes#translateHexColorCodes(String, String)}.
 * This method converts hexadecimal color codes found in messages into legacy Bukkit ChatColor codes for display.
 *
 * <p>
 * This class is designed for Bukkit plugins that need to provide multi-language support, ensuring user messages are
 * displayed in the preferred language of the player.
 */


public class LangManager {
    private final Map<String, Map<String, Object>> languages;
    private  ArrayList<String> langCodes = new ArrayList<>();
    private final Plugin plugin;

    public LangManager(ArrayList<String> langCodes, Plugin plugin) {
        languages = new HashMap<>();
        this.langCodes = langCodes;
        this.plugin = plugin;
        loadLanguageFiles();
    }

    private void loadLanguageFiles() {
        copyLanguageFiles(langCodes);
        loadLanguageFile("en");
        loadLanguageFile("tr");
    }

    private void loadLanguageFile(String langCode) {
        Path filePath = Paths.get(String.valueOf(plugin.getDataFolder()), "languages/" + langCode + ".yml");
        try (InputStream input = new FileInputStream(filePath.toFile())) {
            Yaml yaml = new Yaml();
            Map<String, Object> langData = yaml.load(input);
            languages.put(langCode, langData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void copyLanguageFiles(ArrayList<String> langCodes) {
        for (String langCode : langCodes) {
            InputStream sourceStream = getClass().getClassLoader().getResourceAsStream("languages/" + langCode + ".yml");
            Path targetDir = Paths.get(String.valueOf(plugin.getDataFolder()), "languages");
            Path targetPath = targetDir.resolve(langCode + ".yml");

            if (!Files.exists(targetPath)) {
                try {
                    Files.createDirectories(targetDir);

                    try (OutputStream outStream = new FileOutputStream(targetPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = sourceStream.read(buffer)) > 0) {
                            outStream.write(buffer, 0, length);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }




    public void reloadLanguageFiles() {
        languages.clear();
        loadLanguageFiles();
    }


    public String getMessage(String key, String language) {
        Map<String, Object> props = languages.get(language);
        if (props != null) {
            Object messageObj = getNestedProperty(props, key);
            if (messageObj != null) {
                String message = messageObj.toString();
                message = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
                return TranslateColorCodes.translateHexColorCodes("#", message);
            }
            return getMessage("language-error.unknown_key", language);
        } else {
            return getMessage("language-error.unknown_language", language);
        }
    }


    private Object getNestedProperty(Map<String, Object> map, String key) {
        String[] parts = key.split("\\.");
        Object current = map;
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                return null;
            }
        }
        return current;
    }
}
