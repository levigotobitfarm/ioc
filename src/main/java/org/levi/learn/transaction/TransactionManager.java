package org.levi.learn.transaction;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class TransactionManager {

    private final ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();

    private final SqlSessionFactory sqlSessionFactory;

    private TransactionManager(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static TransactionManager newInstance(SqlSessionFactory sqlSessionFactory) {
        return new TransactionManager(sqlSessionFactory);
    }

    public void startTransaction(){
        localSqlSession.set(this.sqlSessionFactory.openSession(false));
    }

    public SqlSession getSqlSession(){
        return this.localSqlSession.get();
    }

    public void commit(){
        this.localSqlSession.get().commit();
    }

    public void rollback(){
        this.localSqlSession.get().rollback();
    }
}
