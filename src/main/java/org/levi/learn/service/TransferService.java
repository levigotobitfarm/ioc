package org.levi.learn.service;

/**
 * @author DevCenter
 */
public interface TransferService {

    /**
     * @param form
     * @param to
     * @param money
     */
    void transfer(int form,int to,int money);
}
