package beta.com.permissionscreative.languagemanager;

import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LangManager {
    private final Map<String, Map<String, Object>> languages;

    public LangManager() {
        languages = new HashMap<>();
        loadLanguageFiles();
    }

    private void loadLanguageFiles() {
        loadLanguageFile("en");
        loadLanguageFile("tr");
    }

    private void loadLanguageFile(String langCode) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("languages/" + langCode + ".yml")) {
            if (input != null) {
                Yaml yaml = new Yaml();
                Map<String, Object> langData = yaml.load(input);
                languages.put(langCode, langData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
