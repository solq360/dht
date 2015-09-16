package org.solq.dht.db;

import org.hibernate.EmptyInterceptor;

public class MyInterceptor extends EmptyInterceptor {
 
	private static final long serialVersionUID = 3171963853836648146L;
	private String targetTableName;// 目标母表名
	private String tempTableName;// 操作子表名

	public MyInterceptor() {
	}

	public java.lang.String onPrepareStatement(java.lang.String sql) {
		sql = sql.replaceAll(targetTableName, tempTableName);
		return sql;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getTempTableName() {
		return tempTableName;
	}

	public void setTempTableName(String tempTableName) {
		this.tempTableName = tempTableName;
	}

}