package org.levi.learn.bean;

import org.levi.learn.annotation.Service;

@Service
public class Mobile {

    private String phoneNum = "11111";

    public void getPhoneNum(){
        System.out.println("my phone num is: "+this.phoneNum);
    }
}
