package org.solq.dht.dlock.model;

public class DLockResult<T> {
	public static final int MODULE = 1;

	/** 申请锁成功 **/
	public static final int APPLY_LOCK_SUCCEED = getCode(1);
	/** 申请锁失败 **/
	public static final int APPLY_LOCK_ERROR = getCode(-1);
	/** 锁已占有 **/
	public static final int APPLY_LOCK_USED = getCode(-2);
	/** 未找到锁 **/
	public static final int NOT_FIND_LOCK = getCode(-3);
	
	/** 解锁锁号不相同 **/
	public static final int LOCK_SEQUENCE_DISAFFINITY = getCode(-4);

	/**
	 * 返回状态码统一生成
	 */
	public static int getCode(int code) {
		return MODULE * 1000 + code;
	}

	private int code;

	private T result;

	public boolean verifly() {
		return code == 0;
	}

	public static <T> DLockResult<T> SUCCEED(T body) {
		DLockResult<T> result = new DLockResult<T>();
		result.code = 0;
		result.result = body;
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DLockResult ERROR(int code) {
		DLockResult result = new DLockResult();
		result.code = code;
		return result;
	}
	// getter

	public int getCode() {
		return code;
	}

	public T getResult() {
		return result;
	}

}
