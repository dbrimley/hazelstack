package com.craftedbytes.hazelcast;

import com.hazelcast.core.Hazelcast;

public class HazelcastMemberBootstrap {

    public static void main(String args[]) {
        Hazelcast.newHazelcastInstance();
    }

}
