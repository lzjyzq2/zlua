package cn.settile.lzjyzq2.zlua.lib;

import cn.settile.lzjyzq2.zlua.api.Consts;
import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.api.LuaType;

public class Std {

    public static int next(LuaState luaState) {
        luaState.setTop(2);
        if (luaState.next(1)) {
            return 2;
        } else {
            luaState.pushNil();
            return 1;
        }
    }

    public static int pairs(LuaState luaState) {
        luaState.pushJavaFunction(Std::next);
        luaState.pushValue(1);
        luaState.pushNil();
        return 3;
    }

    public static int iPairs(LuaState luaState) {
        luaState.pushJavaFunction(Std::_iPairsAux);
        luaState.pushValue(1);
        luaState.pushInteger(0);
        return 3;
    }

    public static int _iPairsAux(LuaState luaState) {
        long i = luaState.toInteger(2) + 1;
        luaState.pushInteger(i);
        if (luaState.getI(1, i) == LuaType.LUA_TNIL) {
            return 1;
        } else {
            return 2;
        }
    }

    public static int error(LuaState luaState) {
        return luaState.error();
    }

    public static int pCall(LuaState luaState) {
        int nArgs = luaState.getTop() - 1;
        int status = luaState.pCall(nArgs, -1, 0);
        luaState.pushBoolean(status == Consts.LUA_OK.ordinal());
        luaState.insert(1);
        return luaState.getTop();
    }
}
