package cn.smilex.vueblog.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author smilex
 * @date 2022年7月28日10:56:32
 */
public final class ClassUtils {
    private static final Class<?>[] NOT_CHECK_FILED_CLASS_LIST = new Class[]{Short.class, Integer.class, Long.class};

    /**
     * 获取指定类的的所有字段
     *
     * @param clazz class
     * @return fields
     */
    public static List<Field> getFieldsByClass(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return Arrays.stream(declaredFields)
                .filter(item -> !Modifier.isStatic(item.getModifiers()))
                .collect(Collectors.toList());
    }

    public static Class<?> getSuperClassByClass(Class<?> clazz) {
        return clazz.getSuperclass();
    }

    /**
     * 判断指定对象且字段是否为空
     *
     * @param obj   object
     * @param names 不检查的字段名称
     * @return is null
     */
    public static <T> boolean objIsNull(Class<?> clazz, T obj, String... names) throws IllegalAccessException {
        if (obj == null) {
            return true;
        }

        List<String> namesList = Arrays
                .stream(names)
                .collect(Collectors.toList());
        Class<?> superclass = clazz.getSuperclass();

        if (superclass != Object.class) {
            if (objIsNull(superclass, obj, names)) {
                return true;
            }
        }

        if (CommonUtils.isInForArray(clazz, NOT_CHECK_FILED_CLASS_LIST)) {
            return false;
        }

        List<Field> fields = getFieldsByClass(clazz);

        for (Field field : fields) {
            Class<?> type = field.getType();
            String fieldName = field.getName();
            field.setAccessible(true);

            if (namesList.size() > 0 && namesList.contains(fieldName)) {
                continue;
            }

            if (CommonUtils.isInForArray(CharSequence.class, type.getInterfaces())) {
                CharSequence o = (CharSequence) field.get(obj);
                if (StringUtils.isBlank(o)) {
                    return true;
                }
            }

            if (field.get(obj) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找Method
     *
     * @param clazz      clazz
     * @param methodName method name
     * @param paramTypes param types
     * @return method
     * @throws NoSuchMethodException unknown exception
     */
    public static Method getMethodByClass(Class<?> clazz, String methodName, Class<?>... paramTypes) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method;
    }

    /**
     * 获取字段值
     *
     * @param object    object
     * @param fieldName field name
     * @return value
     * @throws NoSuchFieldException   unknown exception
     * @throws IllegalAccessException unknown exception
     */
    public static Object getFieldValueByClassAndObject(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
