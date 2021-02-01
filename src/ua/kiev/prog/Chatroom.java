package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chatroom {
    private final String chatroom;
    private final List<String> members;

    public Chatroom(String chatroom, List<String> members) {
        this.chatroom = chatroom;
        this.members = members;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static void create(User user, Scanner scanner) throws IOException {
        System.out.println("Creating new chatroom");
        System.out.println("Enter name your chatroom:");
        String chatroomName = scanner.nextLine().strip();
        if (chatroomName.isEmpty()) {
            System.out.println("You don't input chatroom name. Create of chatroom is false. Try again...");
            return;
        }
        System.out.println("Enter members of chatroom: ");
        List<String> members = new ArrayList<>();
        boolean listFull = false;
        do {
            String member = scanner.nextLine().strip();
            if (member.isEmpty()) break;
            List<UserStatus> list = UserStatus.request();
            if (list == null || list.stream().noneMatch(us -> us.getLogin().equals(member))) {
                System.out.println("User does not exist. Try again...");
            } else {
                members.add(member);
            }
        } while (!listFull);
        if (members.size() == 0) {
            System.out.println("You don't input chatroom members. Create of chatroom is false. Try again...");
            return;
        }
        members.add(user.getLogin());
        Chatroom chatroom = new Chatroom(chatroomName, members);

        int response = chatroom.send(Utils.getURL() + "/chatroom");

        if (response != 200) { // 200 OK
            System.out.println("HTTP error occurred: " + response);
        } else {
            System.out.println("New chatroom '" + chatroomName + "' created. Members of chatroom are:");
            for (String member : members) {
                System.out.println("\t" + member);
            }
        }
    }

    private int send(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        try {
            String json = toJSON();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            return conn.getResponseCode();
        } finally {
            os.close();
        }
    }

    public static List<Chatroom> request() throws IOException {
        Gson gson = new GsonBuilder().create();
        String strBuf = Utils.request(Utils.getURL() + "/chatroomlist");
        return gson.fromJson(strBuf, JsonChatroom.class).getList();
    }

    public String getChatroom() {
        return chatroom;
    }

    public List<String> getMembers() {
        return members;
    }

    public static boolean getMemberChatroom(String chatroom, String member) throws IOException {
        List<Chatroom> chatroomList = Chatroom.request();
        if (chatroomList != null) {
            for (Chatroom cr : chatroomList) {
                if (cr.getChatroom().equals(chatroom) && cr.getMembers().contains(member)) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "chatroom='" + chatroom + '\'' +
                ", members=" + members +
                '}';
    }
}
