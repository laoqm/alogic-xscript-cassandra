package com.alogicbus.cassandra.core.impl;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.alogicbus.cassandra.core.ConnectionHolder;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;
import com.anysoft.util.Settings;
import com.anysoft.util.XMLConfigurable;
import com.anysoft.util.XmlElementProperties;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.Session;

public class ConnectionHolderImpl implements ConnectionHolder {

	protected static final Logger logger = LogManager.getLogger(ConnectionHolderImpl.class);
	protected ConnectionCfg cfg;
	protected CassandraInstance instance;

	@Override
	public void configure(Element e, Properties p) {
		cfg = new ConnectionCfg(e, p);
		instance = new CassandraInstance(cfg);
	}

	@Override
	public void close() throws Exception {
		instance.shutdown();
	}

	@Override
	public void report(Element xml) {
		if (xml != null) {
			xml.setAttribute("id", cfg.getId());
			xml.setAttribute("host", Arrays.asList(cfg.getHosts()).toString());
			xml.setAttribute("port", String.valueOf(cfg.getPort()));
			xml.setAttribute("name", cfg.getName());
			xml.setAttribute("compress", cfg.getCompress());
			xml.setAttribute("keyspace", cfg.getKeyspace());
		}
	}

	@Override
	public void report(Map<String, Object> json) {
		if (json != null) {
			json.put("id", cfg.getId());
			json.put("host", cfg.getHosts());
			json.put("port", cfg.getPort());
			json.put("name", cfg.getName());
			json.put("compress", cfg.getCompress());
			json.put("keyspace", cfg.getKeyspace());
		}

	}

	@Override
	public Session getSession() {
		return instance.connect().getSession();
	}

	private class ConnectionCfg implements XMLConfigurable {
		private String id;
		private int port;
		private String name;
		private String keyspace;
		private String compress;
		private String[] hosts;
		private String hostStr;
		private String username;
		private String password;

		public ConnectionCfg(Element e, Properties p) {
			configure(e, p);
		}

		public String getId() {
			return id;
		}

		public int getPort() {
			return port;
		}

		public String getName() {
			return name;
		}

		public String getKeyspace() {
			return keyspace;
		}

		public String getCompress() {
			return compress;
		}

		public String[] getHosts() {
			return hosts;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		@Override
		public void configure(Element _e, Properties p) {
			XmlElementProperties props = new XmlElementProperties(_e, Settings.get());
			id = PropertiesConstants.getString(props, "id", "default");
			name = PropertiesConstants.getString(props, "name", "");
			port = PropertiesConstants.getInt(props, "port", 9042);
			compress = PropertiesConstants.getString(props, "compress", "NONE");
			keyspace = PropertiesConstants.getString(props, "keyspace", "");
			username = PropertiesConstants.getString(props, "username", "");
			password = PropertiesConstants.getString(props, "password", "");
			hostStr = PropertiesConstants.getString(props, "hosts", "127.0.0.1");
			String[] hostsLst = hostStr.split(",");
			hosts = new String[hostsLst.length];
			int index = 0;
			for (String host : hostsLst) {
				if (StringUtils.isNotBlank(host)) {
					hosts[index++] = host.trim();
				}
			}
		}

		public Compression getCompression() {
			switch (compress.toLowerCase()) {
			case "lz4":
				return ProtocolOptions.Compression.LZ4;
			case "snappy":
				return ProtocolOptions.Compression.SNAPPY;
			default:
				return ProtocolOptions.Compression.NONE;
			}
		}

	}

	private class CassandraInstance {
		private Session session=null;
		private Cluster cluster=null;
		private ConnectionCfg cfg=null;
		private boolean init = false;

		private CassandraInstance(ConnectionCfg cfg) {
			this.cfg = cfg;
		}

		//connect操作只有第一次才会连接
		private CassandraInstance connect() {
			if (cfg==null) return null;
			if (init) {
				return this;
			}
			synchronized (this) {
				if (!init) {
					Builder builder = Cluster.builder();
					builder.addContactPoints(cfg.getHosts());// hosts

					builder.withPort(cfg.getPort());// port

					if (StringUtils.isNotBlank(cfg.getName())) {// name
						builder.withClusterName(cfg.getName());
					}
					builder.withCompression(cfg.getCompression());// compress

					if (StringUtils.isNotBlank(cfg.getUsername()) && StringUtils.isNotBlank(cfg.getPassword())) { // username,password
						builder.withCredentials(cfg.getUsername(), cfg.getPassword());
					}
					cluster = builder.build();

					if (StringUtils.isBlank(cfg.getKeyspace()))// keyspace
						session = cluster.connect();
					else
						session = cluster.connect(cfg.getKeyspace());

					Metadata metadata = cluster.getMetadata();
					logger.info("Connected to cluster: " + metadata.getClusterName() + " with partitioner: "
							+ metadata.getPartitioner());

					for (Host host : metadata.getAllHosts()) {
						logger.info("Cassandra datacenter: " + host.getDatacenter() + " | address: " + host.getAddress()
								+ " | rack: " + host.getRack());
					}
					init = true;
				}
			}
			return this;
		}

		private void shutdown() {
			logger.info("Shutting down the whole cassandra cluster");
			if (null != session) {
				session.close();
			}
			if (null != cluster) {
				cluster.close();
			}
		}

		private Session getSession() {
			if (session == null) {
				throw new IllegalStateException("No connection initialized");
			}
			return session;
		}
	}

}
