package com.alogicbus.cassandra.context;

import com.alogicbus.cassandra.core.ConnectionHolder;
import com.alogicbus.cassandra.core.impl.ConnectionHolderImpl;
import com.anysoft.context.XMLResource;

public class XRC extends XMLResource<ConnectionHolder> {

	
	public String getObjectName() {
		return "cluster";
	}

	
	public String getDefaultClass() {
		return ConnectionHolderImpl.class.getName();
	}

	
	public String getDefaultXrc() {
		return "java:///com/alogicbus/cassandra/context/cassandra.default.xml#com.alogicbus.cassandra.context.XRC";
	}

}
