package org.levi.learn.core;

import org.levi.learn.factory.BeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DevCenter
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    /**
     * 单例对象池
     */
    public Map<String,Object> singletonBeanMap = new HashMap<String, Object>();
}
