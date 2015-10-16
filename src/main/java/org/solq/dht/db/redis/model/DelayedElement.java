package org.solq.dht.db.redis.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时元素对象
 * 
 * @author solq
 */
public class DelayedElement<T> implements Delayed {

    /** 元素内容 */
    private final T content;
    /** 过期时间 */
    private final Date end;

    public static <T> DelayedElement<T> of(T content, Date end) {
	return new DelayedElement<T>(content, end);
    }

    public static <T> DelayedElement<T> of(T content, int delay) {
	Calendar ca = Calendar.getInstance();
	ca.setTime(new Date());
	ca.add(Calendar.MILLISECOND, delay);
	return new DelayedElement<T>(content, ca.getTime());
    }

    public DelayedElement(T content, Date end) {
	this.content = content;
	this.end = end;
    }

    public T getContent() {
	return content;
    }

    public Date getEnd() {
	return end;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
	long now = System.currentTimeMillis();
	long delay = end.getTime() - now;
	switch (timeUnit) {
	case MILLISECONDS:
	    return delay;
	case SECONDS:
	    return TimeUnit.MILLISECONDS.toSeconds(delay);
	case MINUTES:
	    return TimeUnit.MILLISECONDS.toMinutes(delay);
	case HOURS:
	    return TimeUnit.MILLISECONDS.toHours(delay);
	case DAYS:
	    return TimeUnit.MILLISECONDS.toDays(delay);
	case MICROSECONDS:
	    return TimeUnit.MILLISECONDS.toMicros(delay);
	case NANOSECONDS:
	    return TimeUnit.MILLISECONDS.toNanos(delay);
	}
	return delay;
    }

    @Override
    public int compareTo(Delayed o) {
	long delay1 = this.getDelay(TimeUnit.MILLISECONDS);
	long delay2 = o.getDelay(TimeUnit.MILLISECONDS);
	int result = Long.valueOf(delay1).compareTo(Long.valueOf(delay2));
	if (result != 0) {
	    return result;
	}
	// 时间判断无法区分时，执行如下判断(用于维持 compareTo 的使用约束)
	if (this.equals(o)) {
	    return 0;
	} else {
	    return this.hashCode() - o.hashCode();
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((content == null) ? 0 : content.hashCode());
	return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DelayedElement other = (DelayedElement) obj;
	if (content == null) {
	    if (other.content != null)
		return false;
	} else if (!content.equals(other.content))
	    return false;
	return true;
    }

}