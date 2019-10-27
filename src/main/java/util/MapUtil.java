package util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    @SafeVarargs
    public static <T> Map<T, T> of(T... entries) {
        assert entries.length % 2 == 0 : "Entries count must be even";

        Map<T, T> result = new HashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            T key = entries[i];
            T value = entries[i + 1];
            result.put(key, value);
        }
        return result;
    }
}
