package com.alogicbus.cassandra.core;

import com.anysoft.util.Reportable;
import com.anysoft.util.XMLConfigurable;
import com.datastax.driver.core.Session;

public interface ConnectionHolder extends XMLConfigurable,AutoCloseable,Reportable{
    public Session getSession();
}
