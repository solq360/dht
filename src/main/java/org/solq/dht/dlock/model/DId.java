package org.solq.dht.dlock.model;

public class DId {
	
	/**固定前缀**/
	private String preFix;
	/**数据ID**/
	private String id;
	
	/**数据ID原始记录**/
	private transient Object pk;

	/**申请模式 阻塞等侍，还是异步轮询*/
	private int model;
	// getter

	public String getPreFix() {
		return preFix;
	}

	public String getId() {
		return id;
	}

	public int getModel() {
		return model;
	}

	public Object getPk() {
		return pk;
	}

	public static DId of(String preFix, Object id,int model) {
		DId result = new DId();
		result.preFix = preFix;
		result.id = id.toString();
		result.pk =  id;
		result.model = model;
		return result;
	}

}
