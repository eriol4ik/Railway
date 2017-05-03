package dao;

import java.io.Serializable;

public interface DAO<T, PK extends Serializable> {
    void create(T entity);
    T read(PK id);
    void update(T entity);
    void delete(T entity);
}
