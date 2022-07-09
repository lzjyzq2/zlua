package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.api.LuaType;
import org.junit.jupiter.api.Test;

public class DefaultLuaStateTest {

    @Test
    void test() {
        LuaState luaState = new DefaultLuaState();
        luaState.pushBoolean(true);
        printStack(luaState);
        luaState.pushInteger(10);
        printStack(luaState);
        luaState.pushNil();
        printStack(luaState);
        luaState.pushString("hello");
        printStack(luaState);
        luaState.pushValue(-4);
        printStack(luaState);
        luaState.replace(3);
        printStack(luaState);
        luaState.setTop(6);
        printStack(luaState);
        luaState.remove(-3);
        printStack(luaState);
        luaState.setTop(-5);
        printStack(luaState);
    }

    void printStack(LuaState luaState) {
        int top = luaState.getTop();
        for (int i = 1; i <= top; i++) {
            LuaType type = luaState.type(i);
            switch (type) {
                case LUA_TBOOLEAN:
                    System.out.printf("[%b]", luaState.toBoolean(i));
                    break;
                case LUA_TNUMBER:
                    if (luaState.isInteger(i)) {
                        System.out.printf("[%d]", luaState.toInteger(i));
                    } else {
                        System.out.printf("[%f]", luaState.toNumber(i));
                    }
                    break;
                case LUA_TSTRING:
                    System.out.printf("[%s]", luaState.toString(i));
                    break;
                default:
                    System.out.printf("[%s]", luaState.typeName(type));
            }
        }
        System.out.println();
    }
}
