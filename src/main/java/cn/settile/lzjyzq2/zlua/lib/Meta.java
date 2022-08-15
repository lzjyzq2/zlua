package cn.settile.lzjyzq2.zlua.lib;

import cn.settile.lzjyzq2.zlua.api.LuaState;

public class Meta {

    /**
     * 仅测试用
     *
     * @param luaState LuaState
     * @return 执行结果
     */
    public static int getMetatable(LuaState luaState) {
        if ((!luaState.getMetaTable(1))) {
            luaState.pushNil();
        }
        return 1;
    }

    /**
     * 仅测试用
     *
     * @param luaState LuaState
     * @return 执行结果
     */
    public static int setMetatable(LuaState luaState) {
        luaState.setMetaTable(1);
        return 1;
    }
}
