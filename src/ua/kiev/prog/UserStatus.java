package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

public class UserStatus {
    private final String login;
    private final String status;

    public UserStatus(String login, String status, Gson gson) {
        this.login = login;
        this.status = status;
    }

    public static List<UserStatus> request() throws IOException {
        Gson gson = new GsonBuilder().create();
        String strBuf = Utils.request(Utils.getURL() + "/userlist");
        return gson.fromJson(strBuf, JsonUsers.class).getList();
    }

    public static void print(User user) throws IOException {
        List<UserStatus> list = request();
        if (list != null) {
            System.out.println("All users status:");
            for (UserStatus us : list) {
                System.out.println("\t" + us.getLogin() + "\tstatus='" + us.getStatus() + "'");
            }
        }
        List<Chatroom> chatroomList = Chatroom.request();
        if (chatroomList != null) {
            System.out.println("You are member of chatrooms:");
            for (Chatroom cr : chatroomList) {
                if (cr.getMembers().contains(user.getLogin())) {
                    System.out.println("\t" + cr.getChatroom());
                }
            }
        }
        System.out.println();
    }

    public String getLogin() {
        return login;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "login='" + login + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
