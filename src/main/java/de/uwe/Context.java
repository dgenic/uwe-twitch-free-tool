package de.uwe;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.BitsLeaderboard;
import com.github.twitch4j.helix.domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class Context {

    public User user;

    @Produces
    public OAuth2Credential credential;

    @Produces
    public TwitchClient twitchClient;


    public void init(){


        credential = new OAuth2Credential("twitch", Config.getValue(Config.O_AUTH));

        twitchClient = TwitchClientBuilder.builder()
                .withClientId(Config.getValue(Config.CLIENT_ID))
                .withEnableHelix(true)
                .build();

        user = twitchClient.getHelix().getUsers(credential.getAccessToken(),null, null).execute()
                .getUsers().stream().findFirst().orElse(null);

        System.out.println(user.getDisplayName());

    }


    public BitsLeaderboard getBitsLeaderboard() {
        return twitchClient.getHelix().getBitsLeaderboard(credential.getAccessToken(), 100, "all", null, null).execute();
    }
}
