package com.craftedbytes.services;

import com.craftedbytes.hazelcast.services.HazelcastUserService;
import com.craftedbytes.jdbc.services.JDBCUserService;

public class ServiceFactory {

    public enum UserServices {postgresql, hazelcast}

    ;

    public static UserService getUserService(UserServices userService) {

        UserService userServiceToReturn = null;

        switch (userService) {

            case postgresql:
                userServiceToReturn = new JDBCUserService();
                break;
            case hazelcast:
                userServiceToReturn = new HazelcastUserService();
                break;

        }

        return userServiceToReturn;
    }

}
