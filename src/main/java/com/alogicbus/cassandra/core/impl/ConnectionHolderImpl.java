package com.alogicbus.cassandra.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.alogicbus.cassandra.core.ConnectionHolder;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.anysoft.util.Settings;
import com.anysoft.util.XmlElementProperties;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.Session;

public class ConnectionHolderImpl implements ConnectionHolder {

	protected static final Logger logger = LogManager.getLogger(ConnectionHolderImpl.class);

	protected static  Map<String,Compression> compression=new HashMap<String,Compression>();
	@Override
	public void configure(Element _e, Properties p) {
		XmlElementProperties props = new XmlElementProperties(_e, Settings.get());
		id = PropertiesConstants.getString(props, "id", "default");
		name = PropertiesConstants.getString(props, "name", "");
		port = PropertiesConstants.getInt(props, "port", 9042);
		compress= PropertiesConstants.getString(props, "compress", "NONE");
		defaultKeySpace= PropertiesConstants.getString(props, "keyspace", "");
		String hostStr = PropertiesConstants.getString(props, "hosts", "127.0.0.1");
		String[] splits =hostStr.split(",");
		for(String s:splits){
			if(StringUtils.isNotBlank(s)){
				hosts.add(s.trim());	
			}
		}
	}

	@Override
	public void close() throws Exception {
		if(!session.isClosed()){
			session.close();
		}
		if(!cluster.isClosed()){
		    cluster.close();
		}
	}

	@Override
	public void report(Element xml) {
		if (xml != null) {
			xml.setAttribute("id", id);
			xml.setAttribute("host", hosts.toString());
			xml.setAttribute("port", String.valueOf(port));
			xml.setAttribute("name", name);
			xml.setAttribute("compress", compress);
			xml.setAttribute("keyspace",defaultKeySpace);
		}
	}

	@Override
	public void report(Map<String, Object> json) {
		if (json != null) {
			json.put("id", id);
			json.put("host", hosts);
			json.put("port", port);
			json.put("name", name);
			json.put("compress",compress);
			json.put("keyspace",defaultKeySpace);
		}

	}

	protected String id;
	protected int port;
	protected String name;
	protected String defaultKeySpace;
	protected String compress;
	protected List<String> hosts=new ArrayList<String>();
	private Cluster cluster;
	private Session session;


	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getHosts() {
		return hosts;
	}

	@Override
	public int getPort() {
		return port;
	}


	@Override
	public Cluster getCluster() {
		if (cluster != null){
			return cluster;
		}
		synchronized (this){
			if (cluster == null){
				create();
			}
		}
		return cluster;
	}

	@Override
	public Session getSession() {
		if (session != null){
			return session;
		}
		synchronized (this){
			if (session == null){
				create();
			}
		}
		return session;
	}

	@Override
	public String getDefaultKeySpace() {
		return defaultKeySpace;
	}

	@Override
	public void create() {
		if (getHosts().isEmpty())return ;
		Builder builder = Cluster.builder();
		for (String host : getHosts()) {
			builder.addContactPoint(host);
		}
		builder.withCompression(ProtocolOptions.Compression.LZ4);
		cluster = builder.withPort(this.getPort()).build();
		if (StringUtils.isBlank(this.getDefaultKeySpace()))
			session = cluster.connect();
		else
			session = cluster.connect(this.getDefaultKeySpace());
	}


}
