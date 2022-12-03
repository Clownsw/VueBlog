package cn.smilex.vueblog.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author smilex
 * @date 2022年7月28日10:56:32
 */
public final class ClassUtil {
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

        if (CommonUtil.isInForArray(clazz, NOT_CHECK_FILED_CLASS_LIST)) {
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

            if (type.getSuperclass() == getSuperClassByClass(StringBuilder.class)) {
                CharSequence o = (CharSequence) field.get(obj);
                if (StringUtils.isBlank(o)) {
                    return true;
                }
            } else if (field.get(obj) == null) {
                return true;
            }
        }
        return false;
    }
}
