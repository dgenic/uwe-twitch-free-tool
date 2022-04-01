package de.uwe;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.BitsLeaderboard;
import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     *
     * Fetch User by login
     *
     */
    public User userByName(String login) {
        return twitchClient.getHelix().getUsers(credential.getAccessToken(), null, Collections.singletonList(login)).execute().getUsers().stream().findFirst().orElse(null);
    }


    /**
     *
     * Fetch Follow to userId
     *
     */
    public List<Follow> followsToId(FetchEvent<Follow> event, String userId, int limit) {

        final List<Follow> result = new ArrayList<>();

        String cursor = null;
        boolean firstFetch = true;
        while(cursor != null || firstFetch){
            firstFetch = false;
            cursor = followsToId(event, result, userId, cursor);
            if(result.size() >= limit && limit != 0)
                cursor = null;
        }
        return result;
    }

    private String followsToId(FetchEvent<Follow> fetchEvent, List<Follow> result, String toId, String cursor) {
        boolean abort = false;
        final FollowList followList = twitchClient.getHelix().getFollowers(credential.getAccessToken(), null, toId, cursor, 100).execute();
        final List<Follow> follows = followList.getFollows();
        final int total = followList.getTotal();
        result.addAll(follows);

        if(fetchEvent != null)
            abort = fetchEvent.onFetch(follows, result.size(), total);

        if(abort)
            return null;

        return followList.getPagination().getCursor();

    }




}
