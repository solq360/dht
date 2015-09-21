package org.solq.dht.db.redis.model;

/**
 * 锁信息
 * 
 * @author solq
 */
public class RedisLock {

	/** 持有者 **/
	private String owne;

	/** 有效时间 **/
	private long expires;

	public static RedisLock of(String owne, long expires) {
		RedisLock result = new RedisLock();
		result.owne = owne;
		result.expires = expires;
		return result;
	}

	public boolean checkTimeOut(Long EMOTE_SYSTEM_TIME_DIF) {
		return (System.currentTimeMillis() -EMOTE_SYSTEM_TIME_DIF) > expires;
	}

	// get sett

	public long getExpires() {
		return expires;
	}

	public String getOwne() {
		return owne;
	}

	void setOwne(String owne) {
		this.owne = owne;
	}

	void setExpires(long expires) {
		this.expires = expires;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owne == null) ? 0 : owne.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedisLock other = (RedisLock) obj;
		if (owne == null) {
			if (other.owne != null)
				return false;
		} else if (!owne.equals(other.owne))
			return false;
		return true;
	}
}
