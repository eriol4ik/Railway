package service;

import dao.DAO;
import exception.PersistentException;
import exception.TransactionException;
import dao.pool.ConnectionPool;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class ServiceImpl<T, PK extends Serializable> implements Service<T, PK> {
    protected Connection connection;

    protected DAO<T, PK> dao;

    protected ServiceImpl(DAO<T, PK> dao) {
        this.dao = dao;
    }

    @Override
    public PK create(T entity) {
        try {
            initConnection(dao);

            PK id = dao.create(entity);

            commitAndCloseConnection();

            return id;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T read(PK id) {
        try {
            initConnection(dao);

            T entity = dao.read(id);

            commitAndCloseConnection();

            return entity;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(T entity) {
        try {
            initConnection(dao);

            dao.update(entity);

            commitAndCloseConnection();
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(T entity) {
        try {
            initConnection(dao);

            dao.delete(entity);

            commitAndCloseConnection();
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
        }
    }

    protected void initConnection(DAO<T, PK> dao) throws TransactionException {
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
            dao.setConnection(connection);
        } catch (SQLException e) {
            throw new TransactionException(TransactionException.INIT_CONN_ERROR, e);
        }
    }

    protected void commitAndCloseConnection() throws TransactionException {
        try {
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new TransactionException(TransactionException.COMMIT_ERROR, e);
        } finally {
            closeConnection();
        }
    }

    protected void commit() throws TransactionException {
        try {
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new TransactionException(TransactionException.COMMIT_ERROR, e);
        }
    }

    protected void rollback() throws TransactionException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException(TransactionException.ROLLBACK_ERROR, e);
        }
    }

    protected void closeConnection() throws TransactionException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new TransactionException(TransactionException.CLOSE_CONN_ERROR, e);
        }
    }
}
