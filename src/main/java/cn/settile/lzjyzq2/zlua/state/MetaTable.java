package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.LuaState;

public class MetaTable {

    public static void setMetaTable(Object val, LuaTable mt, LuaState luaState) {
        if (val instanceof LuaTable) {
            LuaTable t = (LuaTable) val;
            t.setMetatable(mt);
            return;
        }
        String key = "_MT" + LuaValue.typeOf(val);
        luaState.getRegistry().put(key, mt);
    }

    public static LuaTable getMetaTable(Object val, LuaState luaState) {
        if (val instanceof LuaTable) {
            LuaTable t = (LuaTable) val;
            return t.getMetatable();
        }
        String key = "_MT" + LuaValue.typeOf(val);
        Object mt = luaState.getRegistry().get(key);
        if (mt instanceof LuaTable) {
            return (LuaTable) mt;
        }
        return null;
    }
}
