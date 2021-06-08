package org.levi.learn.core;

import org.levi.learn.annotation.Service;
import org.levi.learn.util.AnnotationUtil;
import org.levi.learn.util.ClassPathResourceScanner;
import org.levi.learn.util.StringUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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

    @Override
    public Object getBean(String beanId) {
        if(beanId == null || "".equals(beanId)){
            return null;
        }
        return singletonBeanMap.get(beanId);
    }

    /**
     * 容器初始化
     */
    public void init() throws IOException, IllegalAccessException, InstantiationException {
        //获取包路径下所有class文件
        ClassPathResourceScanner classPathResourceScanner = new ClassPathResourceScanner();
        Set<Class<?>> candidateComponents = classPathResourceScanner.findCandidateComponents(this.componentScanPath);
        for (Class clazz : candidateComponents) {
            //判断是否带有@Services注解
            Annotation serviceAnnotation = clazz.getAnnotation(Service.class);
            if(serviceAnnotation != null){
                String value = (String) AnnotationUtil.getAnnotationValue(serviceAnnotation,"value");
                String[] packageArray = clazz.getName().split("[.]");
                String beanId = StringUtil.lowerFirst(packageArray[packageArray.length-1]);
                if(value != null && !"".equals(value)){
                    beanId = value;
                }
                Object bean = createBean(clazz);
                this.singletonBeanMap.put(beanId,bean);
            }

        }
//        clazz.getAnnotations()[0].annotationType().name
    }

    private Object createBean(Class clazz) throws IllegalAccessException, InstantiationException {

        Object bean = clazz.newInstance();
        //检查带有autowired注解的属性
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                if(declaredAnnotation.annotationType().getName().equals("org.levi.learn.annotation.Autowired")){
                    String[] packageArray = declaredField.getName().split("[.]");
                    String beanId = StringUtil.lowerFirst(packageArray[packageArray.length-1]);
                    Object fieldBean = this.singletonBeanMap.get(beanId);
                    if(fieldBean == null){
                        //判断是否已*Mapper结尾，如果受transaction控制，则在动态代理时重新获取一个对应mapper
                        if(beanId.endsWith("Mapper")){

                        }
                        fieldBean = this.createBean(declaredField.getType());
                        declaredField.set(bean,fieldBean);
                    }
                }
            }

        }
        return bean;
    }

}
