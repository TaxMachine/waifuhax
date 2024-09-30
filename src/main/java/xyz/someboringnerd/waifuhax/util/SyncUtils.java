package xyz.someboringnerd.waifuhax.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class SyncUtils {
    private static final String HomeFolder = System.getProperty("user.home");
    private static final String FuturePath = PathUtils.join(HomeFolder, "Future");
    private static final String RusherHackPath = PathUtils.join("./", "rusherhack");
    private static final String MeteorPath = PathUtils.join("./", "meteor-client");

    public static Set<String> getFutureFriends() {
        String path = PathUtils.join(FuturePath, "friends.json");
        Set<String> friends = new HashSet<>();
        try {
            JSONArray json = new JSONArray(PathUtils.readFileToString(path));
            for (int i = 0; i < json.length(); i++) {
                JSONObject friend = json.getJSONObject(i);
                friends.add(friend.getString("friend-label"));
            }
        } catch (RuntimeException ignore) {
        }
        return friends;
    }

    public static Set<String> getRusherhackFriends() {
        String path = PathUtils.join(RusherHackPath, "config", "relations.json");
        Set<String> friends = new HashSet<>();
        try {
            JSONArray json = new JSONArray(PathUtils.readFileToString(path));
            for (int i = 0; i < json.length(); i++) {
                JSONObject friend = json.getJSONObject(i);
                if (friend.getString("state").equalsIgnoreCase("friend"))
                    friends.add(friend.getString("username"));
            }
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        }
        return friends;
    }

    /**
     * &#064;fixme  : currently Crash for no apparent reason
     */
    public static Set<String> getMeteorFriends() {
        return null;
    }
}
