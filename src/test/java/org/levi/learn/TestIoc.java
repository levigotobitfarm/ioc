package org.levi.learn;

import org.junit.Test;
import org.levi.learn.bean.Person;
import org.levi.learn.core.AnnotationContextBeanFactory;

import java.io.IOException;

public class TestIoc {

    @Test
    public void testIoc() throws IllegalAccessException, IOException, InstantiationException {
        AnnotationContextBeanFactory annotationContextBeanFactory = new AnnotationContextBeanFactory("org.levi.learn.bean");
        annotationContextBeanFactory.init();
        Person person = (Person) annotationContextBeanFactory.getBean("person");
        person.getMyPhoneNum();
    }
}
