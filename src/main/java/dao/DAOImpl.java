package dao;

import exception.PersistentException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAOImpl<T, PK extends Serializable> implements DAO<T, PK> {
    protected Connection connection;

    public PK create(T entity) throws PersistentException {
        if (!checkForCreate(entity)) return null;

        execute(getCreateQuery(), getCreateParameters(entity));

        ResultSet resultSet = executeQuery(getSelectIdQuery());

        PK id = parseForCreate(resultSet);

        setPK(entity, id);

        return id;
    }

    public T read(PK id) throws PersistentException {
        if (!checkForRead(id)) return null;

        ResultSet resultSet = executeQuery(getReadQuery(),
                                           id);

        T entity = parseForRead(resultSet);

        setPK(entity, id);

        return entity;
    }

    public void update(T entity) throws PersistentException {
        if (!checkForUpdate(entity)) return;

        execute(getUpdateQuery(), getUpdateParameters(entity));
    }

    public void delete(T entity) throws PersistentException {
        if (!checkForDelete(entity)) return;

        execute(getDeleteQuery(), getDeleteParameters(entity));

        setPK(entity, null);
    }

    protected abstract Boolean checkForCreate(T entity);
    protected abstract Boolean checkForRead(PK id);
    protected abstract Boolean checkForUpdate(T entity);
    protected abstract Boolean checkForDelete(T entity);

    protected abstract String getCreateQuery();
    protected abstract String getSelectIdQuery();
    protected abstract String getReadQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();

    protected abstract Object[] getCreateParameters(T entity) throws PersistentException;
    protected abstract Object[] getUpdateParameters(T entity);
    protected abstract Object[] getDeleteParameters(T entity);

    protected abstract PK parseForCreate(ResultSet resultSet) throws PersistentException;
    protected abstract void setPK(T entity, PK id);
    protected abstract T parseForRead(ResultSet resultSet) throws PersistentException;

    protected ResultSet executeQuery(String query, Object... parameters) throws PersistentException {
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            return statement.executeQuery();
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.QUERY_ERROR, e);
        }
    }

    protected Boolean execute(String query, Object... parameters) throws PersistentException {
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            return statement.execute();
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.QUERY_ERROR, e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
