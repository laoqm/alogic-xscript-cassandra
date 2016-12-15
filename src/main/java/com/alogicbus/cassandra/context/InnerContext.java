package com.alogicbus.cassandra.context;

import com.alogicbus.cassandra.core.ConnectionHolder;
import com.alogicbus.cassandra.core.impl.ConnectionHolderImpl;
import com.anysoft.context.Inner;

public class InnerContext extends Inner<ConnectionHolder> {

	@Override
	public String getObjectName() {
		return "connection";
	}

	@Override
	public String getDefaultClass() {
		return ConnectionHolderImpl.class.getName();
	}

	public int getCount(){
		return holder.getObjectCnt();
	}
}
