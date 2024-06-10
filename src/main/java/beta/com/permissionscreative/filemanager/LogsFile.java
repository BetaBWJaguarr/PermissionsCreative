package beta.com.permissionscreative.filemanager;

import org.bukkit.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The LogsFile class is part of the `beta.com.permissionscreative.filemanager` package.
 * It is responsible for managing a log file within the plugin's data folder.
 * The class provides functionality to write messages to this log file.
 */

public class LogsFile {
    private final File file;

    public LogsFile(Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), "logs.txt");
    }

    public void write(String message) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}