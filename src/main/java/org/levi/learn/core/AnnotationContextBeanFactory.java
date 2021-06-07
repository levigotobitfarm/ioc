package org.levi.learn.core;

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
    private void init(){
        //获取包路径下所有class文件
        
    }

}
