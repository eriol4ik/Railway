package service;

import java.io.Serializable;

public interface Service<T, PK extends Serializable> {
    PK create(T entity);
    T read(PK id);
    void update(T entity);
    void delete(T entity);
}
