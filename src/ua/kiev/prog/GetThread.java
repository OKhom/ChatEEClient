package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n;
    private final User user;

    public GetThread(User user) {
        gson = new GsonBuilder().create();
        this.user = user;
    }

    @Override
    public void run() {
        try {
            while ( ! Thread.interrupted()) {
                String strBuf = Utils.request(Utils.getURL() + "/get?from=" + n);
                JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            if (m.getFrom().equals(user.getLogin()) ||
                                    m.getTo().equals(user.getLogin()) ||
                                    m.getTo().equals("all") ||
                                    Chatroom.getMemberChatroom(m.getTo(), user.getLogin())) {
                                System.out.println(m);
                            }
                            n++;
                        }
                    }
                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
