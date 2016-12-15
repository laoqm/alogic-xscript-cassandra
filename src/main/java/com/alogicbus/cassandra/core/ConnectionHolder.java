package com.alogicbus.cassandra.core;

import java.util.List;

import com.anysoft.util.Factory;
import com.anysoft.util.Reportable;
import com.anysoft.util.XMLConfigurable;

public interface ConnectionHolder extends XMLConfigurable,AutoCloseable,Reportable{
    public String getId();
    public String getName();
	public List<String> getHosts();
    public int getPort();
    public String getDefaultKeySpace();
    public void create();
    public com.datastax.driver.core.Session getSession();
    public com.datastax.driver.core.Cluster getCluster();
 	public static class TheFacory extends Factory<ConnectionHolder>{
 	}
}
