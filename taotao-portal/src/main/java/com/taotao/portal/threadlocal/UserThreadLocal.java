package com.taotao.portal.threadlocal;

import com.taotao.portal.bean.User;

public class UserThreadLocal {

    private static final ThreadLocal<User> USER = new ThreadLocal<User>();

    public static void set(User user) {
        USER.set(user);
    }

    public static User get() {
        return USER.get();
    }
    
    public static void clear(){
        set(null);
    }

}
