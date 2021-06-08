package org.levi.learn.service.impl;

import org.levi.learn.annotation.Autowired;
import org.levi.learn.annotation.Service;
import org.levi.learn.annotation.Transactional;
import org.levi.learn.mapper.AccountMapper;
import org.levi.learn.service.TransferService;
@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void transfer(int form, int to, int money) {
            int initalMoney = 10000;
            accountMapper.updateById(initalMoney+money,to);
//            int a = 1/0;
            accountMapper.updateById(initalMoney-money,form);
    }
}
