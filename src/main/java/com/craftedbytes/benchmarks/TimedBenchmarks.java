package com.craftedbytes.benchmarks;

import com.craftedbytes.domain.User;
import com.craftedbytes.services.ServiceFactory;
import com.craftedbytes.services.UserService;
import org.springframework.util.StopWatch;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.craftedbytes.services.ServiceFactory.UserServices.hazelcast;
import static com.craftedbytes.services.ServiceFactory.UserServices.postgresql;

public class TimedBenchmarks {

    public static void main(String args[]) {

        UserService postgresqlService = ServiceFactory.getUserService(postgresql);
        UserService hazelcastService = ServiceFactory.getUserService(hazelcast);

        if (args.length > 0) {

            String command = args[0];

            if ("partialKeyQuery".equals(command)) {
                partialKeyQuery(postgresqlService, "India");
                partialKeyQuery(hazelcastService, "India");
            } else if ("readThrough".equals(command)) {
                readThrough(hazelcastService);
                readThrough(postgresqlService);
            } else if ("writeThrough".equals(command)) {
                writeThrough(postgresqlService, 8400000);
                writeThrough(hazelcastService, 8500000);
            }

        } else {
            System.out.println("Commands are :- ");
            System.out.println("partialKeyQuery");
            System.out.println("readThrough");
            System.out.println("writeThrough");
        }

    }

    /**
     * Get Users based on location.
     *
     * @param userService
     */
    private static void partialKeyQuery(UserService userService, String location) {

        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < 25; i++) {

            stopWatch.start();

            Collection<User> users = null;
            users = userService.getUserByLocation(location);

            //System.out.println(users.size());

            stopWatch.stop();
        }

        System.out.println(userService.getClass().getCanonicalName() + " total time = " + stopWatch.getTotalTimeSeconds() + " (secs)");
        System.out.println(userService.getClass().getCanonicalName() + " avg query time = " + stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount() + " (ms)");
    }

    /**
     * User is not in the Hazelcast Cache, we will read into the Database and place into the Cache.
     * <p/>
     * 1st Query will be slower and subsequent queries will be coming from cache (when using HazelcastUserService)
     */
    private static void readThrough(UserService userService) {

        userService.removeUser(62638);

        StopWatch stopWatch = new StopWatch();

        for (int i = 0; i < 10; i++) {

            stopWatch.start();

            // Talip Ozturk : Founder and CEO of Hazelcast !
            User user = userService.getUser(62638);

            stopWatch.stop();

        }

        System.out.println(userService.getClass().getCanonicalName() + " total time = " + stopWatch.getTotalTimeMillis() + " (ms)");

    }

    /**
     * Create new users, writes go to Cache and then can be sync or async write to the Database.
     */
    private static void writeThrough(UserService userService, int startingID) {

        // Create Update Map of Users
        Map<Integer, User> users = new HashMap<Integer, User>();
        for (int i = 0; i < 1000; i++) {
            User user = new User(startingID + i, "hazelcast_" + i, 100, new Date(), new Date(), "http://www.hazelcast.com", "Palo Alto", 30, "aboutMe", 100, 23, 0);
            users.put(i, user);
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        userService.addUsers(users);
        stopWatch.stop();

        System.out.println(userService.getClass().getCanonicalName() + " took " + stopWatch.getTotalTimeSeconds() + " seconds @ " + new Date().toString());

    }


}
