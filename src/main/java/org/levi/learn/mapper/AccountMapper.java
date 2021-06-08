package org.levi.learn.mapper;

import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

    void updateById(@Param("amount") double amount, @Param("id") int id);
}
