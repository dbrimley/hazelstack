package com.craftedbytes.services;

import com.craftedbytes.domain.User;

import java.util.Collection;
import java.util.Map;

public interface UserService {

    Collection<User> getUserByLocation(String location);

    User getUser(Integer userKey);

    void removeUser(Integer userKey);

    void addUsers(Map<Integer, User> users);

}
