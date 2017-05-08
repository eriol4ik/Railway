package dao;

import java.io.Serializable;
import java.sql.SQLException;

public interface DAO<T, PK extends Serializable> {
    PK create(T entity) throws SQLException;
    T read(PK id) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(T entity) throws SQLException;
}
