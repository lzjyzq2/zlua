package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.exception.LuaTableNaNKeyException;
import cn.settile.lzjyzq2.zlua.exception.LuaTableNullKeyException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LuaTable {

    private Object[] arr;

    private HashMap<Object, Object> map;

    private LuaTable metatable;

    /**
     * 存储键名
     */
    private Map<Object, Object> keys;
    private boolean changed;

    public LuaTable(int nArr, int nRec) {
        if (nArr > 0) {
            arr = new Object[nArr];
        }
        if (nRec > 0) {
            map = new HashMap<>(nRec);
        }
    }

    public Object get(Object key) {
        Long idx = LuaValue.convertToInteger(key);
        if (idx != null && arr != null) {
            if (idx >= 1 && idx <= arr.length) {
                return arr[idx.intValue() - 1];
            }
        }
        return map != null ? map.get(key) : null;
    }


    public void put(Object key, Object value) {
        if (key == null) {
            throw new LuaTableNullKeyException();
        }
        if (key instanceof Double && ((Double) key).isNaN()) {
            throw new LuaTableNaNKeyException();
        }
        Long idxRef = LuaValue.convertToInteger(key);
        if (idxRef != null) {
            int idx = idxRef.intValue();
            if (idx >= 1) {
                if (arr == null) {
                    arr = new Object[1];
                }
                int arrLen = arr.length;
                if (idx <= arrLen) {
                    arr[idx - 1] = value;
                    if (idx == arrLen && value == null) {
                        shrinkArray();
                    }
                    return;
                }
                if (idx == arrLen + 1) {
                    if (map != null) {
                        map.remove(key);
                    }
                    if (value != null) {
                        appendAndExpandArray(value);
                    }
                    return;
                }
            }
        }
        if (value != null) {
            if (map == null) {
                map = new HashMap<>(8);
            }
            map.put(key, value);
        } else {
            if (map != null) {
                map.remove(key);
            }
        }
    }

    private void shrinkArray() {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == null) {
                arr = Arrays.copyOf(arr, i + 1);
            }
        }
    }

    private void appendAndExpandArray(Object value) {
        int size = 0;
        Object[] tmpArr = null;
        if (map != null) {
            tmpArr = new Object[map.size()];
            for (int idx = arr.length + 1; ; idx++) {
                Object val = map.remove((long) idx);
                if (val != null) {
                    tmpArr[size] = val;
                    size++;
                } else {
                    break;
                }
            }
        }
        int start = arr.length;
        arr = Arrays.copyOf(arr, arr.length + 1 + size);
        arr[start] = value;
        if (size > 0) {
            System.arraycopy(tmpArr, 0, arr, start + 1, size);
        }

    }

    public int len() {
        return arr == null ? 0 : arr.length;
    }

    public void setMetatable(LuaTable metatable) {
        this.metatable = metatable;
    }

    public LuaTable getMetatable() {
        return metatable;
    }

    public boolean hasMetaField(String fieldName) {
        return metatable != null && metatable.get(fieldName) != null;
    }

    public Object nextKey(Object key) {
        if (this.keys == null || key == null) {
            initKeys();
            this.changed = false;
        }
        return keys.get(key);
    }

    private void initKeys() {
        if (keys == null) {
            keys = new HashMap<>();
        } else {
            keys.clear();
        }
        Object key = null;
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != null) {
                    long nextKey = i + 1;
                    keys.put(key, nextKey);
                    key = nextKey;
                }
            }
        }
        if (map != null) {
            for (Object k : map.keySet()) {
                Object v = map.get(k);
                if (v != null) {
                    keys.put(key, k);
                    key = k;
                }
            }
        }
    }
}