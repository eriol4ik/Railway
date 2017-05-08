package dao;

import dao.pool.ConnectionPool;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAOImpl<T, PK extends Serializable> implements DAO<T, PK> {
    protected Connection connection;

    public PK create(T entity) throws SQLException {
        if (!checkForCreate(entity)) return null;

        initConnection();

        execute(getCreateQuery(), getCreateParameters(entity));

        ResultSet resultSet = executeQuery(getSelectIdQuery());

        commitAndClose();

        PK id = parseForCreate(resultSet);

        setPK(entity, id);

        return id;
    }

    public T read(PK id) throws SQLException {
        if (!checkForRead(id)) return null;

        initConnection();

        ResultSet resultSet = executeQuery(getReadQuery(),
                                           id);
        commitAndClose();

        T entity = parseForRead(resultSet);

        setPK(entity, id);

        return entity;
    }

    public void update(T entity) throws SQLException {
        if (!checkForUpdate(entity)) return;

        initConnection();

        execute(getUpdateQuery(), getUpdateParameters(entity));

        commitAndClose();
    }

    public void delete(T entity) throws SQLException {
        if (!checkForDelete(entity)) return;

        initConnection();

        execute(getDeleteQuery(), getDeleteParameters(entity));

        setPK(entity, null);

        commitAndClose();
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

    protected abstract Object[] getCreateParameters(T entity) throws SQLException;
    protected abstract Object[] getUpdateParameters(T entity);
    protected abstract Object[] getDeleteParameters(T entity);

    protected abstract PK parseForCreate(ResultSet resultSet) throws SQLException;
    protected abstract void setPK(T entity, PK id);
    protected abstract T parseForRead(ResultSet resultSet) throws SQLException;

    protected ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);

        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }

        return statement.executeQuery();
    }

    protected Boolean execute(String query, Object... parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);

        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }

        return statement.execute();
    }

    protected void initConnection() throws SQLException {
        connection = ConnectionPool.getConnection();
        connection.setAutoCommit(false);
    }

    protected void commitAndClose() throws SQLException {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    protected void commit() throws SQLException {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }
    }
}
