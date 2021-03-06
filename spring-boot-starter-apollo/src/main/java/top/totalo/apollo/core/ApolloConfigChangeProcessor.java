package top.totalo.apollo.core;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ReflectionUtils;
import top.totalo.apollo.ApolloValue;
import top.totalo.apollo.util.GsonUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Apollo config processor.
 */
public class ApolloConfigChangeProcessor implements BeanPostProcessor, PriorityOrdered {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApolloConfigChangeProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        for (Field field : findAllField(clazz)) {
            processField(bean, field);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
    
    private List<Field> findAllField(Class clazz) {
        final List<Field> fields = new LinkedList<>();
        ReflectionUtils.doWithFields(clazz, fields::add, this::fieldFilter);
        return fields;
    }
    
    private boolean fieldFilter(Field field) {
        return null != field.getAnnotation(ApolloValue.class);
    }

    protected void processField(Object bean, Field field) {
        // register @ApolloValue on field
        ApolloValue value = field.getAnnotation(ApolloValue.class);
        if (value == null) {
            return;
        }
        String configValue = getProperty(value.key(), value.namespace(), value.defaultValue(), field, bean);
        injectField(field, bean, configValue);
    }

    private String getProperty(String key, String namespace, String defaultValue, Field field, Object bean) {
        Config config = ConfigService.getConfig(namespace);
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent configChangeEvent) {
                for (String key : configChangeEvent.changedKeys()) {
                    ConfigChange change = configChangeEvent.getChange(key);
                    injectField(field, bean, change.getNewValue());
                }
            }
        });
        return config.getProperty(key, defaultValue);
    }

    private void injectField(Field field, Object bean, String configValue) {
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, GsonUtil.getInstance().fromJson(configValue, field.getGenericType()));
    }
}
