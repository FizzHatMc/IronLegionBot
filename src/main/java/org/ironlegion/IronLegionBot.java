package org.ironlegion;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.ironlegion.Listener.DiscordEventListener;

import javax.security.auth.login.LoginException;

public class IronLegionBot {
    // We reference the class here, also known as the instance.
    protected static IronLegionBot selfBot;
    private ShardManager shardManager = null;

    // We will parse the token through and cli argument, this is a protection against leaking the token by accident. (By uploading it to git for example)
    public IronLegionBot(String token) {
        try {
            shardManager = buildShardManager(token);
        } catch (LoginException e) {
            System.out.println("Failed to start bot! Please check the console for any errors.");
            System.exit(0);
        }

    }

    // The JDA Shardmanager instance, this is the brains of the entire bot. Without this, the bot doesn't boot.
    private ShardManager buildShardManager(String token) throws LoginException {
        // It is often better to load your token in from an external file or environment variable, especially if you plan on publishing the source code.
        DefaultShardManagerBuilder builder =
                DefaultShardManagerBuilder.createDefault(token)
                        .addEventListeners(new DiscordEventListener(this));     // <------- STARTS LISTENER CLASS


        return builder.build();
    }

    public ShardManager getJDA() {
        return shardManager;
    }


}
