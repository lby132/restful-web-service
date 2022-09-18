package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<UserInfo> users = new ArrayList<>();

    private static int userCount = 3;

    static {
        users.add(new UserInfo(1, "Kenneth", new Date(), "pass1", "701010-1111111"));
        users.add(new UserInfo(2, "Alice", new Date(), "pass2", "801010-2222222"));
        users.add(new UserInfo(3, "Elena", new Date(), "pass3", "901010-1111111"));
    }

    public List<UserInfo> findAll() {
        return users;
    }

    public UserInfo save(UserInfo user) {
        if (user.getId() == null) {
            user.setId(++userCount);
        }

        users.add(user);
        return user;
    }

    public UserInfo findOne(int id) {
        for (UserInfo user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public UserInfo deleteById(int id) {
        Iterator<UserInfo> iterator = users.iterator();

        while (iterator.hasNext()) {
            final UserInfo user = iterator.next();

            if (user.getId() == id) {
                iterator.remove();
                return user;
            }
        }

        return null;
    }
}
