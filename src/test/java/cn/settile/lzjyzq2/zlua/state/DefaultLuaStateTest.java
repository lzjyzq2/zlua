package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.ArithOp;
import cn.settile.lzjyzq2.zlua.api.CompareOp;
import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.api.LuaType;
import org.junit.jupiter.api.Test;

public class DefaultLuaStateTest {

    @Test
    void test() {
        LuaState luaState = new DefaultLuaState(null);
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
                    System.out.printf("[\"%s\"]", luaState.toString(i));
                    break;
                default:
                    System.out.printf("[%s]", luaState.typeName(type));
            }
        }
        System.out.println();
    }

    @Test
    void test1(){
        LuaState luaState = new DefaultLuaState(20,null);
        luaState.pushInteger(1);
        luaState.pushString("2.0");
        luaState.pushString("3.0");
        luaState.pushNumber(4.0);
        printStack(luaState);

        luaState.arith(ArithOp.LUA_OPADD);
        printStack(luaState);
        luaState.arith(ArithOp.LUA_OPBNOT);
        printStack(luaState);
        luaState.len(2);
        printStack(luaState);
        luaState.concat(3);
        printStack(luaState);
        luaState.pushBoolean(luaState.compare(1,2, CompareOp.LUA_OPEQ));
        printStack(luaState);
    }
}
