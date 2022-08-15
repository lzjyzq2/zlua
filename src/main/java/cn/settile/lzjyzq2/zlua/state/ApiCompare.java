package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.CompareOp;
import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.exception.LuaInvalidCompareOpException;
import cn.settile.lzjyzq2.zlua.number.Math;
import cn.settile.lzjyzq2.zlua.number.Number;
import cn.settile.lzjyzq2.zlua.util.Pair;

public class ApiCompare {


    public static boolean eq(Object value1, Object value2, LuaState luaState) {
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
        } else if (value1 instanceof LuaTable) {
            if (value2 instanceof LuaTable && value1 != value2 && luaState != null) {
                Pair<Object, Boolean> result = luaState.callMetaMethod(value1, value2, "__eq");
                if (result.getRight()) {
                    return LuaValue.convertToBoolean(result.getLeft());
                }
            }
            return value1 == value2;
        }
        return value1 == value2;
    }

    public static boolean lt(Object value1, Object value2, LuaState luaState) {
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
        Pair<Object, Boolean> result = luaState.callMetaMethod(value1, value2, "__lt");
        if (result.getRight()) {
            return LuaValue.convertToBoolean(result.getLeft());
        }
        throw new LuaInvalidCompareOpException();
    }

    public static boolean le(Object value1, Object value2, LuaState luaState) {
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
        Pair<Object, Boolean> result = luaState.callMetaMethod(value1, value2, "__le");
        if (result.getRight()) {
            return LuaValue.convertToBoolean(result.getLeft());
        } else if ((result = luaState.callMetaMethod(value2, value1, "__lt")).getRight()) {
            return !LuaValue.convertToBoolean(result.getLeft());
        }
        throw new LuaInvalidCompareOpException();
    }
}
