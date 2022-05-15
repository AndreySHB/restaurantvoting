package ru.restaurantvoting.web.user;

import ru.restaurantvoting.model.Role;
import ru.restaurantvoting.model.User;
import ru.restaurantvoting.util.JsonUtil;
import ru.restaurantvoting.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password", "votes");

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int NOT_FOUND = 100;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User", USER_MAIL, "user", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final User masha = new User(3, "Masha", "masha@gmail.com", "masha", Role.USER);
    public static final User dasha = new User(4, "Dasha", "dasha@gmail.com", "dasha", Role.USER);
    public static final User sasha = new User(5, "Sasha", "sasha@gmail.com", "sasha", Role.USER);
    public static final User pasha = new User(6, "Pasha", "pasha@gmail.com", "pasha", Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "newPass", false, new Date(), Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
