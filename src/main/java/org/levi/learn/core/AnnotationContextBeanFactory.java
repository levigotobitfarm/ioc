package org.levi.learn.core;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.ibatis.session.SqlSession;
import org.levi.learn.annotation.Autowired;
import org.levi.learn.annotation.Service;
import org.levi.learn.annotation.Transactional;
import org.levi.learn.transaction.TransactionManager;
import org.levi.learn.util.AnnotationUtil;
import org.levi.learn.util.ClassPathResourceScanner;
import org.levi.learn.util.SqlSessionUtil;
import org.levi.learn.util.StringUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
    public void init() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
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
                Annotation transactionAnnotation = clazz.getAnnotation(Transactional.class);
                if(transactionAnnotation != null){
                    bean = createProxy(bean,clazz);
                }
                this.singletonBeanMap.put(beanId,bean);
            }

        }
//        clazz.getAnnotations()[0].annotationType().name
    }

    private Object createProxy(Object bean, Class clazz) throws ClassNotFoundException {

        Class[] interfaces = clazz.getInterfaces();

        if(interfaces.length>0){
            //有接口的情况下 使用jdk动态代理

            Class<?> aClass = Class.forName(interfaces[0].getName());
            Object transactionProxyBean = Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{aClass}, new InvocationHandler() {
                TransactionManager transactionManager = TransactionManager.newInstance(SqlSessionUtil.getSqlSessionFactory());
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return ivokeMethod(method, args, transactionManager, clazz, bean);
                }


            });
            return transactionProxyBean;
        }else{
            //cglib 代理实现
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback(new MethodInterceptor() {
                TransactionManager transactionManager = TransactionManager.newInstance(SqlSessionUtil.getSqlSessionFactory());
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return ivokeMethod(method, objects, transactionManager, clazz, bean);
                }


            });
            return enhancer.create();
        }

    }
    private Object ivokeMethod(Method method, Object[] args, TransactionManager transactionManager, Class clazz, Object bean) {
        try{
            transactionManager.startTransaction();
            proxyMapper(transactionManager, clazz, bean);
            //设置属性代理
            Object result = method.invoke(bean,args);
            transactionManager.commit();
            return result;

        }catch (Exception e){
            transactionManager.rollback();
            return null;
        }
    }


    private void proxyMapper(TransactionManager transactionManager, Class clazz, Object bean) throws IllegalAccessException {

        SqlSession sqlSession = transactionManager.getSqlSession();
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if(declaredField.getAnnotationsByType(Autowired.class).length>0 && declaredField.getName().contains("Mapper")){
                Object mapper = sqlSession.getMapper(declaredField.getType());
                declaredField.setAccessible(true);
                declaredField.set(bean,mapper);
            }
        }
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
                            fieldBean = SqlSessionUtil.getSqlSession().getMapper(declaredField.getType());
                        }else{
                            fieldBean = this.createBean(declaredField.getType());
                        }
                        declaredField.set(bean,fieldBean);
                    }
                }
            }

        }
        return bean;
    }

}
