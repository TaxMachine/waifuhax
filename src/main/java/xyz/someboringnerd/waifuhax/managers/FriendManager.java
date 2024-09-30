package xyz.someboringnerd.waifuhax.managers;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import xyz.someboringnerd.waifuhax.util.PathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static xyz.someboringnerd.waifuhax.util.SyncUtils.*;

public class FriendManager {

    public static HashSet<Friend> friends = new HashSet<>();

    private static final String friendsPath = PathUtils.join(".", "WaifuHax", "friends.json");

    public static void addFriend(Friend friend) {
        try {
            friends.add(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFriend(@NotNull String username, @NotNull UUID uuid) {
        addFriend(new Friend(username, uuid));
    }

    public static boolean isFriend(@NotNull String username) {
        for (Friend friend : friends) {
            if (friend.username.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void removeFriend(Friend friend) {
        try {
            friends.remove(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeFriend(String username) {
        for (Friend friend : friends) {
            if (friend.username.equals(username)) {
                removeFriend(friend);
                return;
            }
        }
    }

    public static void loadFriendList() {
        File file = new File(friendsPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String json = PathUtils.readFileToString(friendsPath);
        Gson gson = new Gson();
        Friend[] friends = gson.fromJson(json, Friend[].class);
        if (friends == null) return;
        for (Friend friend : friends) {
            addFriend(friend);
        }
    }

    public static void saveFriendList() {
        Gson gson = new Gson();
        String json = gson.toJson(friends);
        PathUtils.writeStringToFile(friendsPath, json);
    }

    /**
     * Import friends from another client
     *
     * @param clientID <br>0 : Meteor<br>1 : Rusherhack<br>2 : FutureClient
     */
    public static void importFriendList(int clientID) {
        List<String> friends = new ArrayList<>();
        switch (clientID) {
            case 0:
                friends.addAll(getMeteorFriends());
                break;
            case 1:
                friends.addAll(getRusherhackFriends());
                break;
            case 2:
                friends.addAll(getFutureFriends());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + clientID);
        }
        for (String friend : friends) {
            if (!friends.contains(friend))
                addFriend(new Friend(friend, null));
        }
    }

    public static class Friend {
        public String username;
        public UUID uuid;

        public Friend(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        @Override
        public int hashCode() {
            return this.username.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Friend)) return false;
            return this.username.equals(((Friend) obj).username);
        }

        @Override
        public String toString() {
            return this.username;
        }
    }
}