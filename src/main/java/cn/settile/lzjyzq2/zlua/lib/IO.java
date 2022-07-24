package cn.settile.lzjyzq2.zlua.lib;

import cn.settile.lzjyzq2.zlua.api.LuaState;

public class IO {

    public static int print(LuaState luaState) {
        int nArgs = luaState.getTop();
        for (int i = 1; i <= nArgs; i++) {
            if (luaState.isBoolean(i)) {
                System.out.print(luaState.toBoolean(i));
            } else if (luaState.isString(i)) {
                System.out.print(luaState.toString(i));
            } else {
                System.out.print(luaState.typeName(luaState.type(i)));
            }
            if (i < nArgs) {
                System.out.print("\t");
            }
        }
        System.out.println();
        return 0;
    }

}
