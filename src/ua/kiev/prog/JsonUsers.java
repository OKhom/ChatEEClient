package ua.kiev.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUsers {
    private final List<UserStatus> list = new ArrayList<>();

    public JsonUsers(List<UserStatus> sourceList) {
        list.addAll(sourceList);
    }

    public List<UserStatus> getList() {
        return Collections.unmodifiableList(list);
    }
}
