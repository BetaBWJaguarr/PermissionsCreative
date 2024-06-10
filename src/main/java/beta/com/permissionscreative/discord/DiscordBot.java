package beta.com.permissionscreative.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * The DiscordBot class is responsible for interacting with the Discord API.
 *
 * It has one private member:
 * - `jda`: an instance of the JDA (Java Discord API) class.
 *
 * The class constructor initializes the `jda` member using the provided token. If the token is null or empty, the constructor returns early.
 *
 * The `getJDA` method returns the `jda` instance.
 */

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