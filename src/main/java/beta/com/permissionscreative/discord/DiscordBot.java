package beta.com.permissionscreative.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;

public class DiscordBot {
    private JDA jda;

    public DiscordBot(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        try {
            jda = JDABuilder.createDefault(token).build();
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JDA getJDA() {
        return jda;
    }
}