package cn.settile.lzjyzq2.zlua.state;


import cn.settile.lzjyzq2.zlua.api.LuaType;
import cn.settile.lzjyzq2.zlua.number.Number;

import static cn.settile.lzjyzq2.zlua.api.LuaType.*;

public class LuaValue {

    public static LuaType typeOf(Object val) {
        if (val == null) {
            return LUA_TNIL;
        } else if (val instanceof Boolean) {
            return LUA_TBOOLEAN;
        } else if (val instanceof Long || val instanceof Double) {
            return LUA_TNUMBER;
        } else if (val instanceof String) {
            return LUA_TSTRING;
        } else {
            throw new RuntimeException("TODO");
        }
    }

    public static boolean convertToBoolean(Object val) {
        if (val == null) {
            return false;
        } else if (val instanceof Boolean) {
            return (Boolean) val;
        } else {
            return true;
        }
    }

    public static Double convertToFloat(Object luaValue) {
        if (luaValue instanceof Double) {
            return (Double) luaValue;
        } else if (luaValue instanceof Long) {
            return ((Long) luaValue).doubleValue();
        } else if (luaValue instanceof String) {
            return Double.parseDouble((String) luaValue);
        } else {
            return null;
        }
    }

    public static Long convertToInteger(Object luaValue) {
        if (luaValue instanceof Double) {
            return ((Double) luaValue).longValue();
        } else if (luaValue instanceof Long) {
            return ((Long) luaValue);
        } else if (luaValue instanceof String) {
            return stringToInteger((String) luaValue);
        } else {
            return null;
        }
    }

    public static Long stringToInteger(String s) {
        Object value;
        if ((value = Number.parseInteger(s)) != null) {
            return (Long) value;
        } else if ((value = Number.parseFloat(s)) != null) {
            return Number.floatToInteger((double) value);
        }
        return null;
    }

}
