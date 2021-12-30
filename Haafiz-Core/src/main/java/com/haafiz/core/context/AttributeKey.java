package com.haafiz.core.context;

import com.haafiz.common.config.ServiceInvoker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Anthony
 * @create 2021/12/30
 * @desc
 */
public abstract class AttributeKey<T> {

    private static final Map<String, AttributeKey<?>> namedMap = new HashMap<>();

    //	到负责均衡之前，要通过具体的服务，获取对应的服务实例列表
    public static final AttributeKey<Set<String>> MATCH_ADDRESS = create(Set.class);

    public static final AttributeKey<ServiceInvoker> HTTP_INVOKER = create(ServiceInvoker.class);

    public static final AttributeKey<ServiceInvoker> DUBBO_INVOKER = create(ServiceInvoker.class);

    static {
        namedMap.put("MATCH_ADDRESS", MATCH_ADDRESS);
        namedMap.put("HTTP_INVOKER", HTTP_INVOKER);
        namedMap.put("DUBBO_INVOKER", DUBBO_INVOKER);
    }

    public static AttributeKey<?> valueOf(String name) {
        return namedMap.get(name);
    }


    public abstract T cast(Object value);


    /**
     * create AttributeKey
     * @param valueClass
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> AttributeKey<T> create(final Class<? super T> valueClass) {
        return new SimpleAttributeKey(valueClass);
    }


    /**
     * key converter
     * @param <T>
     */
    public static class SimpleAttributeKey<T> extends AttributeKey<T> {

        private final Class<T> valueClass;

        SimpleAttributeKey(final Class<T> valueClass) {
            this.valueClass = valueClass;
        }

        @Override
        public T cast(Object value) {
            return valueClass.cast(value);
        }

        @Override
        public String toString() {
            if(valueClass != null) {
                StringBuilder sb = new StringBuilder(getClass().getName());
                sb.append("<");
                sb.append(valueClass.getName());
                sb.append(">");
                return sb.toString();
            }
            return super.toString();
        }

    }

}
