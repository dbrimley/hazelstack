package com.craftedbytes.benchmarks;

import com.craftedbytes.domain.User;
import com.craftedbytes.services.ServiceFactory;
import com.craftedbytes.services.UserService;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.craftedbytes.services.ServiceFactory.UserServices.hazelcast;
import static com.craftedbytes.services.ServiceFactory.UserServices.postgresql;

public class TimedBenchmarks {

    public static final int INSERT_COUNT = 1000;
    public static final int PARTIAL_QUERY_COUNT = 25;

    // Talip Ozturk : Founder and CEO of Hazelcast !
    public static final int USER_KEY = 62638;

    public static void main(String args[]) {

        UserService postgresqlService = ServiceFactory.getUserService(postgresql);
        UserService hazelcastService = ServiceFactory.getUserService(hazelcast);

        try {

            if (args.length > 0) {

                String command = args[0];

                if ("partialKeyQuery".equals(command)) {
                    partialKeyQuery(postgresqlService, "India");
                    partialKeyQuery(hazelcastService, "India");
                } else if ("readThrough".equals(command)) {
                    readThrough(hazelcastService);
                    readThrough(postgresqlService);
                } else if ("writeThrough".equals(command)) {
                    int startingID = new Integer(args[1]);
                    writeThrough(postgresqlService, startingID);
                    writeThrough(hazelcastService, startingID + INSERT_COUNT + 1);
                }

            } else {
                System.out.println("Commands are :- ");
                System.out.println("partialKeyQuery");
                System.out.println("readThrough");
                System.out.println("writeThrough");
            }

        } finally {
            postgresqlService.close();
            hazelcastService.close();
            System.exit(0);
        }


    }

    /**
     * Get Users based on location.
     *
     * @param userService
     */
    private static void partialKeyQuery(UserService userService, String location) {

        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < PARTIAL_QUERY_COUNT; i++) {

            stopWatch.start();

            userService.getUserByLocation(location);

            stopWatch.stop();
        }

        printTimes(userService, stopWatch);
    }

    /**
     * User is not in the Hazelcast Cache, we will read into the Database and place into the Cache.
     * <p/>
     * 1st Query will be slower and subsequent queries will be coming from cache (when using HazelcastUserService)
     */
    private static void readThrough(UserService userService) {

        userService.removeUser(USER_KEY);

        StopWatch stopWatch = new StopWatch();

        for (int i = 0; i < READ_THROUGH_COUNT(); i++) {

            stopWatch.start();

            User user = userService.getUser(USER_KEY);

            stopWatch.stop();

        }

        printTimes(userService, stopWatch);

    }

    private static int READ_THROUGH_COUNT() {
        return 10;
    }

    /**
     * Create new users, writes go to Cache and then can be sync or async write to the Database.
     */
    private static void writeThrough(UserService userService, int startingID) {

        Map<Integer, User> users = createUsersMap(startingID);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        userService.addUsers(users);

        stopWatch.stop();

        printTimes(userService, stopWatch);
    }

    private static Map<Integer, User> createUsersMap(int startingID) {
        // Create Update Map of Users
        Map<Integer, User> users = new HashMap<Integer, User>();
        for (int i = 0; i < INSERT_COUNT; i++) {
            User user = new User(startingID + i, "hazelcast_" + i, 100, new Date(), new Date(), "http://www.hazelcast.com", "Palo Alto", 30, "aboutMe", 100, 23, 0);
            users.put(i, user);
        }
        return users;
    }

    private static void printTimes(UserService userService, StopWatch stopWatch) {
        System.out.println(userService.getClass().getCanonicalName() + " total time = " + stopWatch.getTotalTimeSeconds() + " (secs)");
        System.out.println(userService.getClass().getCanonicalName() + " avg time = " + stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount() + " (ms)");
    }

}
