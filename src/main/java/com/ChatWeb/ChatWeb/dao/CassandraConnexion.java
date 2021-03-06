package com.ChatWeb.ChatWeb.dao;

import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster;

public class CassandraConnexion {

    private final Cluster cluster;
    private final Session session;
    public CassandraConnexion(){
        System.out.println("Cassandra Java Connection");
        //Connect to the cluster and Keyspace ecommerce
        cluster=Cluster.builder().addContactPoint("localhost").withQueryOptions(new QueryOptions().setFetchSize(2000)).build();
        session=cluster.connect("j2a");
    }
    public Session getSession()
    {
        return session;
    }

}
