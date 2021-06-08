package org.levi.learn.bean;

import org.levi.learn.annotation.Autowired;
import org.levi.learn.annotation.Service;

/**
 * @author DevCenter
 */
@Service
public class Person {
    @Autowired
    private Mobile mobile;

    public void getMyPhoneNum(){
        this.mobile.getPhoneNum();
    }
}
