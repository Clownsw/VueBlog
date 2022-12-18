package cn.smilex.vueblog.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author smilex
 * @date 2022/12/3/22:38
 */
public final class ListUtil {

    /**
     * 将values转为List
     *
     * @param values values
     * @param <T>    泛型
     * @return List
     */
    public static <T> List<T> of(T... values) {
        List<T> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        return list;
    }
}
