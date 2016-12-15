package com.alogicbus.cassandra.service;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alogicbus.cassandra.context.CassandraSource;
import com.logicbus.backend.AbstractServant;
import com.logicbus.backend.Context;
import com.logicbus.backend.message.JsonMessage;
import com.logicbus.backend.message.XMLMessage;
import com.logicbus.models.servant.ServiceDescription;

/**
 * cassandra的数据源报告
 * 
 * @author laoqiming
 *
 */
public class CassandraSourceReport extends AbstractServant {

	@Override
	protected void onDestroy() {

	}

	@Override
	protected void onCreate(ServiceDescription sd) {
	}

	@Override
	protected int onXml(Context ctx) throws Exception {
		XMLMessage msg = (XMLMessage) ctx.asMessage(XMLMessage.class);
		Document doc = msg.getDocument();
		Element root = msg.getRoot();
		Element eleSource = doc.createElement("source");
		CassandraSource src = CassandraSource.get();
		src.report(eleSource);
		root.appendChild(eleSource);

		return 0;
	}

	@Override
	protected int onJson(Context ctx) throws Exception {
		JsonMessage msg = (JsonMessage) ctx.asMessage(JsonMessage.class);
		Map<String, Object> map = new HashMap<String, Object>();
		CassandraSource src = CassandraSource.get();
		src.report(map);
		msg.getRoot().put("source", map);

		return 0;
	}

}
