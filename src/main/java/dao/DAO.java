package dao;

import exception.PersistentException;

import java.io.Serializable;
import java.sql.Connection;

public interface DAO<T, PK extends Serializable> {
    PK create(T entity) throws PersistentException;
    T read(PK id) throws PersistentException;
    void update(T entity) throws PersistentException;
    void delete(T entity) throws PersistentException;

    Connection getConnection();
    void setConnection(Connection connection);


}
