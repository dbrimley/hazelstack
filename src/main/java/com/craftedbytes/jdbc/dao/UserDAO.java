package com.craftedbytes.jdbc.dao;

import com.craftedbytes.domain.User;
import com.google.common.collect.Lists;
import org.apache.commons.dbcp.BasicDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.IntegerMapper;

import java.sql.SQLException;
import java.util.*;

/**
 * JDBC Based Data Access Class for the User POJO.
 */
public class UserDAO {

    private Handle handle;
    private BasicDataSource bds;

    public UserDAO() {
        bds = new BasicDataSource();
        bds.setDriverClassName("org.postgresql.Driver");
        bds.setUrl("jdbc:postgresql://localhost/codereview.stackexchange");
        DBI dbi = new DBI(bds);
        handle = dbi.open();
    }

    public HashSet<Integer> getUserKeys() {
        List<Integer> keys = handle.createQuery("select id from users where location = :location")
                .map(IntegerMapper.FIRST)
                .bind("location", "India")
                .list();
        return new HashSet<Integer>(keys);
    }

    public Collection<User> getUserByLocation(String location) {
        List<Map<String, Object>> rs = handle.select("select reputation," +
                "                                            creation_date," +
                "                                            display_name," +
                "                                            last_access_date," +
                "                                            website_url," +
                "                                            location," +
                "                                            age," +
                "                                            about_me," +
                "                                            views," +
                "                                            up_votes," +
                "                                            down_votes " +
                "                                     from users where location = :location", location);

        return buildUserResults(rs);
    }

    public Map<Integer, User> loadAll(Collection<Integer> userKeys) {

        List<List<Integer>> userKeysPartitions = Lists.partition(new ArrayList<Integer>(userKeys), 1000);

        Map<Integer, User> mapToReturn = new HashMap<Integer, User>();

        for (List<Integer> nextUserKeysPartition : userKeysPartitions) {
            Map<Integer, User> partitionMap = new HashMap<Integer, User>();
            Collection<User> users = getUsers(nextUserKeysPartition);
            for (User user : users) {
                partitionMap.put(new Integer(user.getId()), user);
            }
            mapToReturn.putAll(partitionMap);
        }

        System.out.println("Loaded @" + new Date());

        return mapToReturn;


    }

    public void insertUser(User user) {
        handle.execute("insert into users (reputation," +
                "                          creation_date," +
                "                          display_name," +
                "                          last_access_date," +
                "                          website_url," +
                "                          location," +
                "                          age," +
                "                          about_me," +
                "                          views," +
                "                          up_votes," +
                "                          down_votes) values (?,?,?,?,?,?,?,?,?,?,?)",

                user.getReputation(),
                user.getCreationDate(),
                user.getDisplayName(),
                user.getLastAccessDate(),
                user.getWebsiteURL(),
                user.getLocation(),
                user.getAge(),
                user.getAboutMe(),
                user.getViews(),
                user.getUpVotes(),
                user.getDownVotes());

    }

    private Collection<User> buildUserResults(List<Map<String, Object>> rs) {
        List<User> results = new ArrayList<User>();
        for (Map row : rs) {
            results.add(createUser(row));
        }
        return results;
    }

    public Collection<User> getUsers(Collection<Integer> userKeys) {

        StringBuilder sb = new StringBuilder();
        sb.append("select id," +
                "         reputation," +
                "         creation_date," +
                "         display_name," +
                "         last_access_date," +
                "         website_url," +
                "         location," +
                "         age," +
                "         about_me," +
                "         views," +
                "         up_votes," +
                "         down_votes from users where id in(");

        for (Integer userKey : userKeys) {
            sb.append(userKey).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");

        System.out.println(sb.toString());

        List<Map<String, Object>> rs = handle.select(sb.toString());

        return buildUserResults(rs);

    }

    public User getUser(Integer userKey) {

        List<Map<String, Object>> rs = handle.select("select reputation," +
                "                                            creation_date," +
                "                                            display_name," +
                "                                            last_access_date," +
                "                                            website_url," +
                "                                            location," +
                "                                            age," +
                "                                            about_me," +
                "                                            views," +
                "                                            up_votes," +
                "                                            down_votes from users where id = :id", userKey);

        if (rs.size() == 0) {
            System.out.println("no rows found");
            return null;
        }

        Map<String, Object> row = rs.get(0);

        return createUser(row);

    }

    private User createUser(Map<String, Object> row) {

        Integer id;
        String displayName;
        Integer reputation;
        Date creationDate;
        Date lastAccessDate;
        String websiteURL;
        String location;
        Integer age;
        String aboutMe;
        Integer views;
        Integer upVotes;
        Integer downVotes;

        if (row.get("id") != null) {
            id = (Integer) row.get("id");
        } else {
            id = 0;
        }

        if (row.get("display_name") != null) {
            displayName = (String) row.get("display_name");
        } else {
            displayName = "";
        }

        if (row.get("reputation") != null) {
            reputation = (Integer) row.get("reputation");
        } else {
            reputation = 0;
        }

        if (row.get("creation_date") != null) {
            creationDate = (Date) row.get("creation_date");
        } else {
            creationDate = new Date();
        }

        if (row.get("last_access_date") != null) {
            lastAccessDate = (Date) row.get("last_access_date");
        } else {
            lastAccessDate = new Date();
        }

        if (row.get("website_url") != null) {
            websiteURL = (String) row.get("website_url");
        } else {
            websiteURL = "";
        }

        if (row.get("location") != null) {
            location = (String) row.get("location");
        } else {
            location = "";
        }

        if (row.get("age") != null) {
            age = (Integer) row.get("age");
        } else {
            age = 0;
        }

        if (row.get("about_me") != null) {
            aboutMe = (String) row.get("about_me");
        } else {
            aboutMe = "";
        }

        if (row.get("views") != null) {
            views = (Integer) row.get("views");
        } else {
            views = 0;
        }

        if (row.get("up_votes") != null) {
            upVotes = (Integer) row.get("up_votes");
        } else {
            upVotes = 0;
        }

        if (row.get("down_votes") != null) {
            downVotes = (Integer) row.get("down_votes");
        } else {
            downVotes = 0;
        }

        User user = new User(id,
                displayName,
                reputation,
                creationDate,
                lastAccessDate,
                websiteURL,
                location,
                age,
                aboutMe,
                views,
                upVotes,
                downVotes);

        return user;
    }

    public void close() {
        handle.close();
        try {
            bds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
