package beta.com.permissionscreative.filemanager;

import org.bukkit.plugin.Plugin;

import java.io.*;

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