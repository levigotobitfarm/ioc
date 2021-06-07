package org.levi.learn.core;

import org.levi.learn.annotation.Service;
import org.levi.learn.util.AnnotationUtil;
import org.levi.learn.util.ClassPathResourceScanner;
import org.levi.learn.util.StringUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * 基于注解的IOC容器
 * @author DevCenter
 */
public class AnnotationContextBeanFactory extends AbstractBeanFactory{

    private String componentScanPath;

    /**
     * @param componentScanPath
     */
    public AnnotationContextBeanFactory(String componentScanPath) {
        this.componentScanPath = componentScanPath;
    }

    /**
     * 无参构造
     */
    public AnnotationContextBeanFactory() {
    }

    public void setComponentScanPath(String componentScanPath) {
        this.componentScanPath = componentScanPath;
    }

    public Object getBean(String beanId) {
        if(beanId == null || "".equals(beanId)){
            return null;
        }
        return singletonBeanMap.get(beanId);
    }

    /**
     * 容器初始化
     */
    private void init() throws IOException {
        //获取包路径下所有class文件
        ClassPathResourceScanner classPathResourceScanner = new ClassPathResourceScanner();
        Set<Class<?>> candidateComponents = classPathResourceScanner.findCandidateComponents("org.levi.learn.ioc.service");
        for (Class clazz : candidateComponents) {
            //判断是否带有@Services注解
            Annotation serviceAnnotation = clazz.getAnnotation(Service.class);
            if(serviceAnnotation != null){
                String value = (String) AnnotationUtil.getAnnotationValue(serviceAnnotation,"value");
                String[] packageArray = clazz.getName().split("[.]");
                String beanId = StringUtil.lowerFirst(packageArray[packageArray.length-1]);
                if(value != null && value != ""){
                    beanId = value;
                }
            }

        }
//        clazz.getAnnotations()[0].annotationType().name
    }

}
