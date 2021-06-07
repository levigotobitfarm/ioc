package org.levi.learn.factory;

/**
 * @author DevCenter
 */
public interface BeanFactory {

    /**
     * 根据BeanId 获取一个Bean
     * @param beanId
     * @return
     */
    Object getBean(String beanId);

}
