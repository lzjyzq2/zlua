package cn.settile.lzjyzq2.zlua.util;

import cn.settile.lzjyzq2.zlua.api.LuaState;

public class LuaStateUtil {
    public static int luaUpvalueIndex(int i) {
        return LuaState.LUA_REGISTRYINDEX - i;
    }
}
