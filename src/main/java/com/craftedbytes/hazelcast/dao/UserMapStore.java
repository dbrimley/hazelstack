package com.craftedbytes.hazelcast.dao;

import com.craftedbytes.domain.User;
import com.craftedbytes.jdbc.dao.UserDAO;
import com.hazelcast.core.MapStore;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserMapStore implements MapStore<Integer, User> {

    private UserDAO userDao = new UserDAO();

    private UserMapStore() {
    }

    @Override
    public void store(Integer userKey, User user) {
        userDao.insertUser(user);
    }

    @Override
    public User load(Integer userKey) {
        return userDao.getUser(userKey);
    }

    @Override
    public Map<Integer, User> loadAll(Collection<Integer> userKeys) {
        return userDao.loadAll(userKeys);
    }

    @Override
    public Set<Integer> loadAllKeys() {
        return userDao.getUserKeys();
    }

    @Override
    public void storeAll(Map<Integer, User> userKeyUserMap) {
        // Should consider using batching here rather than calling the single insert DAO.
        for (Map.Entry<Integer, User> mapEntry : userKeyUserMap.entrySet()) {
            store(mapEntry.getKey(), mapEntry.getValue());
        }
    }

    @Override
    public void delete(Integer userKey) {

    }

    @Override
    public void deleteAll(Collection<Integer> userKeys) {

    }

}
