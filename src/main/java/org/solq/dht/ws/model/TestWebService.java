package org.solq.dht.ws.model;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * 测试 webservice
 * {@link http://www.blogjava.net/zjhiphop/archive/2009/04/29/webservice.html}
 * spring rim <br>
 * cxf<br>
 * 
 * @author solq
 */
@WebService
public class TestWebService implements ITest {
	@Override
	public String test(String msg) {
		System.out.println("msg : " + msg);
		return msg;
	}

	public static void main(String[] args) {
		// http://localhost:9527/test?wsdl
		Endpoint.publish("http://localhost:9527/test", new TestWebService());
		Endpoint.publish("http://localhost:9527/abc", new TestWebService());
		System.out.println(" start server ");
	}

}
