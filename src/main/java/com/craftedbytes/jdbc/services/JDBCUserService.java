package com.craftedbytes.jdbc.services;

import com.craftedbytes.domain.User;
import com.craftedbytes.jdbc.dao.UserDAO;
import com.craftedbytes.services.UserService;

import java.util.Collection;
import java.util.Map;

/**
 * The JDBCUserService provides a consistent means of accessing data with common views.
 */
public class JDBCUserService implements UserService {

    private UserDAO userDao = new UserDAO();

    public Collection<User> getUserByLocation(String location) {
        return userDao.getUserByLocation(location);
    }

    public User getUser(Integer userKey) {
        return userDao.getUser(userKey);
    }

    @Override
    public void removeUser(Integer userKey) {

    }

    @Override
    public void addUsers(Map<Integer, User> users) {
        for (User user : users.values()) {
            userDao.insertUser(user);
        }
    }

}
