package com.craftedbytes.hazelcast;

import com.hazelcast.core.Hazelcast;

/**
 * Creates a Hazelcast Cluster member reading the /resources/hazelcast.xml to configure itself.
 */
public class HazelcastMemberBootstrap {

    public static void main(String args[]) {
        Hazelcast.newHazelcastInstance();
    }

}
