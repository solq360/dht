package org.solq.dht.db;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.exception.DataException;
import org.solq.dht.db.model.IEntity;

/***
 * 公开数据访问接口 
 * */
public interface Accessor {
	
	// CRUD 方法部分

	/**
	 * 从存储层加载指定的实体对象实例
	 * @param clz 实体类型
	 * @param id 实体主键
	 * @return 实体实例,不存在应该返回null
	 */
	<PK, T extends IEntity<PK>> T load(Class<T> clazz, PK id);

	/**
	 * 持久化指定的实体实例,并返回实体的主键值对象
	 * @param clz 实体类型
	 * @param entity 被持久化的实体实例(当持久化成功时,该实体的主键必须被设置为正确的主键值)
	 * @return 持久化实体的主键值对象
	 * @throws EntityExistsException 实体已经存在时抛出
	 * @throws DataException 实体数据不合法时抛出
	 */
	<PK, T extends IEntity<PK>> PK save(Class<T> clazz, T entity);

	/**
	 * 从存储层移除指定实体
	 * @param clz 实体类型
	 * @param id 实体主键
	 */
	<PK, T extends IEntity<PK>> void remove(Class<T> clazz, PK id);

	/**
	 * 从存储层移除指定实体
	 * @param clz 实体类型
	 * @param id 实体主键
	 */
	<PK, T extends IEntity<PK>> void remove(Class<T> clazz, T entity);

	
	/**
	 * 更新存储层的实体数据(不允许更新实体的主键值)
	 * @param entity 被更新实体对象实例
	 * @param clz 实体类型
	 * @throws EntityNotFoundException 被更新实体在存储层不存在时抛出
	 */
	<PK, T extends IEntity<PK>> T update(Class<T> clazz, T entity);
	
	/**
	 * 查询存储层的实体数据
	 * @param <PK>
	 * @param <T>
	 * @param clz 实体类型
	 * @param entities 填充的数据集合
	 * @param offset 查询偏移量
	 * @param size 查询数量
	 * @return 实体总数
	 */
	<PK, T extends IEntity<PK>> void listAll(Class<T> clazz, Collection<T> entities, Integer offset, Integer size);
	
	
	
	
	/**
	 * 从存储层加载指定的实体对象实例
	 * @param clz 实体类型
	 * @param id 实体主键
	 * @return 实体实例,不存在应该返回null
	 */
	<PK, T extends IEntity<PK>> T load(String table,Class<T> clazz, PK id);

	/**
	 * 持久化指定的实体实例,并返回实体的主键值对象
	 * @param clz 实体类型
	 * @param entity 被持久化的实体实例(当持久化成功时,该实体的主键必须被设置为正确的主键值)
	 * @return 持久化实体的主键值对象
	 * @throws EntityExistsException 实体已经存在时抛出
	 * @throws DataException 实体数据不合法时抛出
	 */
	<PK, T extends IEntity<PK>> PK save(String table,Class<T> clazz, T entity);

	/**
	 * 从存储层移除指定实体
	 * @param clz 实体类型
	 * @param id 实体主键
	 */
	<PK, T extends IEntity<PK>> void remove(String table,Class<T> clazz, PK id);

	/**
	 * 从存储层移除指定实体
	 * @param clz 实体类型
	 * @param id 实体主键
	 */
	<PK, T extends IEntity<PK>> void remove(String table,Class<T> clazz, T entity);

	
	/**
	 * 更新存储层的实体数据(不允许更新实体的主键值)
	 * @param entity 被更新实体对象实例
	 * @param clz 实体类型
	 * @throws EntityNotFoundException 被更新实体在存储层不存在时抛出
	 */
	<PK, T extends IEntity<PK>> T update(String table,Class<T> clazz, T entity);
	
	/**
	 * 查询存储层的实体数据
	 * @param <PK>
	 * @param <T>
	 * @param clz 实体类型
	 * @param entities 填充的数据集合
	 * @param offset 查询偏移量
	 * @param size 查询数量
	 * @return 实体总数
	 */
	<PK, T extends IEntity<PK>> void listAll(String table,Class<T> clazz, Collection<T> entities, Integer offset, Integer size);
	
}