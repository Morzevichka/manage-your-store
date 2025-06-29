package com.morzevichka.manageyourstore.repository.impl;

import com.morzevichka.manageyourstore.annotation.Query;
import com.morzevichka.manageyourstore.utils.HibernateUtil;
import jakarta.persistence.NoResultException;


import javax.naming.directory.NoSuchAttributeException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class GenericRepository<T, ID> implements CrudRepository<T, ID> {

    private final Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(HibernateUtil.getSessionFactory().fromSession(session ->
                    session.find(entityClass, id))
        );
    }

    public void save(T entity) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }

    public void update(T entity) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }

    public void delete(T entity) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            session.remove(entity);
        });
    }

    public List<T> findAll() {
        return HibernateUtil.getSessionFactory().fromSession(session ->
                session.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                        .setCacheable(true)
                        .getResultList()
        );
    }

    public Optional<T> getSingleResultQuery(Object... params) {
        try {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

            Class<?>[] paramTypes = Arrays.stream(params).map(Object::getClass).toArray(Class<?>[]::new);

            Method method = Class.forName(stackTraceElement.getClassName()).getMethod(stackTraceElement.getMethodName(), paramTypes);
            Query queryAnnotation = method.getAnnotation(Query.class);

            if (queryAnnotation == null) {
                throw new NoSuchAttributeException("No annotation @Query here");
            }
            String query = queryAnnotation.value();

            return Optional.of(HibernateUtil.getSessionFactory().fromSession(session -> {
                var SQLQuery = queryAnnotation.nativeQuery()
                        ? session.createNativeQuery(query, entityClass).setCacheable(true)
                        : session.createQuery(query, entityClass).setCacheable(true);
                for (int i = 0; i < params.length; i++) {
                    SQLQuery.setParameter(i + 1, params[i]);
                }
                return SQLQuery.getSingleResult();
            })
            );
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NoSuchAttributeException | NoSuchMethodException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<T> getResultListQuery(Object... params) {
        try {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
            Class<?>[] paramTypes = Arrays.stream(params).map(Object::getClass).toArray(Class<?>[]::new);

            Method method = Class.forName(stackTraceElement.getClassName()).getMethod(stackTraceElement.getMethodName(), paramTypes);
            Query queryAnnotation = method.getAnnotation(Query.class);

            if (queryAnnotation == null) {
                throw new NoSuchAttributeException("No annotation @Query here");
            }
            String query = queryAnnotation.value();

            return HibernateUtil.getSessionFactory().fromSession(session -> {
                var SQLQuery = queryAnnotation.nativeQuery()
                        ? session.createNativeQuery(query, entityClass).setCacheable(true)
                        : session.createQuery(query, entityClass).setCacheable(true);
                for (int i = 0; i < params.length; i++) {
                    SQLQuery.setParameter(i + 1, params[i]);
                }
                return SQLQuery.getResultList();
            });
        } catch (NoSuchAttributeException | NoSuchMethodException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
