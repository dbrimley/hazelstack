package com.craftedbytes.hazelcast.services;

import com.craftedbytes.domain.User;
import com.craftedbytes.services.UserService;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;

import java.util.Collection;
import java.util.Map;

/**
 * The HazelcastUserService provides a consistent means of accessing data with common views.
 */
public class HazelcastUserService implements UserService {

    private IMap<Integer, User> userMap;

    public HazelcastUserService() {

        // Create a client connection to the Hazelcast Cluster
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("127.0.0.1");
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        userMap = hazelcastInstance.getMap("user");

        // hit the size method to block on intial MapStore load.
        System.out.println("Loaded map size is = " + userMap.size());
    }

    @Override
    public Collection<User> getUserByLocation(String location) {
        return userMap.values(Predicates.equal("location", location));
    }

    @Override
    public User getUser(Integer userKey) {
        return userMap.get(userKey);
    }

    @Override
    public void removeUser(Integer userKey) {
        // evict removes the User from the Hazelcast Map but does not call onto MapStore to delete.
        userMap.evict(userKey);
    }

    @Override
    public void addUsers(Map<Integer, User> users) {
        for (User user : users.values()) {
            // set does not return the replaced value over the wire to client, so can be faster than put.
            userMap.set(user.getId(), user);
        }
    }

}
