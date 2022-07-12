package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.CompareOp;
import cn.settile.lzjyzq2.zlua.exception.LuaInvalidCompareOpException;
import cn.settile.lzjyzq2.zlua.number.Math;
import cn.settile.lzjyzq2.zlua.number.Number;

public class ApiCompare {


    public static boolean eq(Object value1, Object value2) {
        if (value1 == null) {
            return value2 == null;
        } else if (value1 instanceof Boolean || value2 instanceof String) {
            return value1.equals(value2);
        } else if (value1 instanceof Long) {
            return value1.equals(value2) ||
                    (value2 instanceof Double && value1.equals(Number.floatToInteger((Double) value2)));
        } else if (value1 instanceof Double) {
            return value1.equals(value2) ||
                    (value2 instanceof Long && value1.equals(((Long) value2).doubleValue()));
        }
        return value1 == value2;
    }

    public static boolean lt(Object value1, Object value2) {
        if (value1 instanceof String && value2 instanceof String) {
            return ((String) value1).compareTo((String) value2) < 0;
        }
        if (value1 instanceof Long) {
            if (value2 instanceof Long) {
                return ((Long) value1) < ((Long) value2);
            } else if (value2 instanceof Double) {
                return ((Long) value1).doubleValue() < ((Double) value2);
            }
        }
        if (value1 instanceof Double) {
            if (value2 instanceof Double) {
                return ((Double) value1) < ((Double) value2);
            } else if (value2 instanceof Long) {
                return ((Double) value1) < ((Long) value2).doubleValue();
            }
        }
        throw new LuaInvalidCompareOpException();
    }

    public static boolean le(Object value1, Object value2) {
        if (value1 instanceof String && value2 instanceof String) {
            return ((String) value1).compareTo((String) value2) <= 0;
        }
        if (value1 instanceof Long) {
            if (value2 instanceof Long) {
                return ((Long) value1) <= ((Long) value2);
            } else if (value2 instanceof Double) {
                return ((Long) value1).doubleValue() <= ((Double) value2);
            }
        }
        if (value1 instanceof Double) {
            if (value2 instanceof Double) {
                return ((Double) value1) <= ((Double) value2);
            } else if (value2 instanceof Long) {
                return ((Double) value1) <= ((Long) value2).doubleValue();
            }
        }
        throw new LuaInvalidCompareOpException();
    }
}
