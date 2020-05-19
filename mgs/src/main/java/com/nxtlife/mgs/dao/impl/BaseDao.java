package com.nxtlife.mgs.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;

public abstract class BaseDao<PK extends Serializable, T> {

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public BaseDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
	}

	@PersistenceContext
	private EntityManager entityManager;

	protected EntityManager getSession() {
		return this.entityManager;
	}

	public T getByKey(PK key) {
		return getSession().find(persistentClass, key);
	}

	public void persist(T entity) {
		getSession().persist(entity);
	}

	public T merge(T entity) {
		return getSession().merge(entity);
	}

	public T save(T entity) {
		Session session = entityManager.unwrap(Session.class);
		session.save(entity);
		session.flush();
		return entity;
	}

	public void saveOrUpdate(T entity) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(entity);
		session.flush();
	}

	public void delete(T entity) {
		getSession().remove(entity);
	}

	public Query query(String query) {
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery(query);
	}

}
