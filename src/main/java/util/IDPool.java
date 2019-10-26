package util;

import java.util.HashMap;
import java.util.Map;

public class IDPool {
    private Map<String, Integer> pool = new HashMap<>();

    public Integer idByName(String name) {
        if (pool.containsKey(name)) {
            return pool.get(name);
        }
        pool.put(name, pool.size() + 1);
        return pool.size();
    }

    public Integer getTempVar() {
        pool.put("@" + (pool.size() + 1), pool.size() + 1);
        return pool.size();
    }

    public String nameById(Integer id) {
        for (Map.Entry<String, Integer> entry : pool.entrySet()) {
            if (id.equals(entry.getValue())) {
                return entry.getKey();
            } else if (id.equals(-entry.getValue())) {
                return "!" + entry.getKey();
            }
        }
        return null;
    }

    public int size() {
        return pool.size();
    }
}
