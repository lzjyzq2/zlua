package cn.settile.lzjyzq2.zlua.state;


import cn.settile.lzjyzq2.zlua.api.LuaType;

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

}
