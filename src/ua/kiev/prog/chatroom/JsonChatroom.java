package ua.kiev.prog.chatroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonChatroom {
    private final List<Chatroom> list = new ArrayList<>();

    public JsonChatroom(List<Chatroom> sourceList) {
        list.addAll(sourceList);
    }

    public List<Chatroom> getList() {
        return Collections.unmodifiableList(list);
    }
}
