package ru.javaops.bootjava;

import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;

import java.util.Set;

public class UserTestUtil {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final User user = new User("user@gmail.com", "User_First", "User_Last", "password", Set.of(Role.USER));
    public static final User admin = new User("admin@gmail.com", "Admin_First", "Admin_Last", "admin", Set.of(Role.USER, Role.ADMIN));
}
