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


    public List<Follow> followsToId(FetchEvent<Follow> event) {
        final FollowList followList = twitchClient.getHelix().getFollowers(credential.getAccessToken(), null, user.getId(), null, 100).execute();
        final List<Follow> follows = followList.getFollows();
        final int total = followList.getTotal();
        final List<Follow> result = new ArrayList<>(follows);
        if(event != null){
            event.onFetch(follows, result.size(), total);
        }
        String cursor = followList.getPagination().getCursor();
        while(cursor != null){
            cursor = followsToId(event, result, user.getId(), cursor);
           if(result.size() > 500)
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
        if(fetchEvent != null){
            fetchEvent.onFetch(follows, result.size(), total);
        }
        if(fetchEvent != null){
            abort = fetchEvent.onFetch(follows, result.size(), total);
        }
        if(abort){
            return null;
        }else {
            return followList.getPagination().getCursor();
        }
    }






//    public FollowList fetchFollows() {
//        return twitchClient.getHelix().getFollowers(credential.getAccessToken(), null, user.getId(), null, 100).execute();
//    }
//
//    public FollowList fetchFollows(List) {
//        return twitchClient.getHelix().getFollowers(credential.getAccessToken(), null, user.getId(), null, 100).execute();
//    }

}
