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
 * The LangManager class is responsible for managing language files and translations in a Bukkit
 * plugin.
 * It allows for the loading, copying, and reloading of language files, as well as retrieving
 * messages in a specific language.
 *
 * The class maintains a map of languages, where each language is represented by a map of key-value
 * pairs.
 * Each key represents a message identifier, and the corresponding value is the message text in the
 * respective language.
 *
 * The class also provides a method to retrieve a message in a specific language. If the requested
 * language or message key does not exist,
 * it will return an error message in the requested language.
 *
 * This class is particularly useful for plugins that need to support multiple languages and provide
 * user messages in the user's preferred language.
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
                return ChatColor.translateAlternateColorCodes('&', message);
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
