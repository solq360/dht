package org.solq.dht.core.protocol.bencode;

public class BObject {
	private Object value;
	private Class<?> clz;

	public static BObject of(Object value, Class<?> clz) {
		BObject result = new BObject();
		result.value = value;
		result.clz = clz;
		return result;
	}
	// getter

	public Object getValue() {
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> t) {
		return (T)value;
	}

	public Class<?> getClz() {
		return clz;
	}

}