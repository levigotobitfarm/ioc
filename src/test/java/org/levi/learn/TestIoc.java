package org.levi.learn;

import org.junit.Test;
import org.levi.learn.bean.Person;
import org.levi.learn.core.AnnotationContextBeanFactory;
import org.levi.learn.service.TransferService;

import java.io.IOException;

public class TestIoc {

    @Test
    public void testIoc() throws IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
        AnnotationContextBeanFactory annotationContextBeanFactory = new AnnotationContextBeanFactory("org.levi.learn.bean");
        annotationContextBeanFactory.init();
        Person person = (Person) annotationContextBeanFactory.getBean("person");
        person.getMyPhoneNum();
    }

    @Test
    public void testTransaction() throws IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
        AnnotationContextBeanFactory annotationContextBeanFactory = new AnnotationContextBeanFactory("org.levi.learn.service");
        annotationContextBeanFactory.init();
        TransferService transferService = (TransferService) annotationContextBeanFactory.getBean("transferServiceImpl");
        transferService.transfer(1,2,2000);
    }
}
