package org.levi.learn;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;
import org.levi.learn.mapper.AccountMapper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class TestMybatis {

    @Test
    public void testUpdate() throws IOException, SQLException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
        try {

            accountMapper.updateById(12000.0, 1);

            accountMapper.updateById(8000.0, 2);
            int a = 1 / 0;
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        }

    }
}
