package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.exception.LuaStackIndexOutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LuaStackTest {

    @Test
    void check() {
        LuaStack luaStack = new LuaStack(20);
        for (int i = 0; i < 20; i++) {
            luaStack.push(i);
        }
        for (int i = 1; i <= luaStack.top(); i++) {
            System.out.printf("[%s]", luaStack.get(i));
        }
        System.out.println();
        Assertions.assertTrue(luaStack.check(21));
        luaStack.push(21);
        for (int i = 1; i <= luaStack.top(); i++) {
            System.out.printf("[%s]", luaStack.get(i));
        }
        System.out.println();
        System.out.printf("LuaStack size = %s", luaStack.size());
    }

    @Test
    void newLuaStack() {
        LuaStack luaStack = new LuaStack(20);
        Assertions.assertNotNull(luaStack);
        Assertions.assertEquals(luaStack.size(), 20);
    }

    @Test
    void push() {
        LuaStack luaStack = new LuaStack(2);
        luaStack.push(2);
        luaStack.push(1);
        Assertions.assertEquals(luaStack.get(1), 2);
        Assertions.assertEquals(luaStack.get(2), 1);
    }

    @Test
    void pop() {
        LuaStack luaStack = new LuaStack(2);
        luaStack.push(2);
        luaStack.push(1);
        Assertions.assertEquals(luaStack.pop(), 1);
        Assertions.assertEquals(luaStack.pop(), 2);
    }

    @Test
    void absIndex() {
        LuaStack luaStack = new LuaStack(10);
        for (int i = 0; i < 10; i++) {
            luaStack.push(i);
        }
        for (int i = 9; i >= 0; i--) {
            System.out.printf("%s:[%s]\n", i - 10, luaStack.get(i - 10));
            Assertions.assertEquals(i, luaStack.get(i - 10));
        }
    }

    @Test
    void isValid() {
        LuaStack luaStack = new LuaStack(10);
        Assertions.assertFalse(luaStack.isValid(1));
        for (int i = 0; i < 10; i++) {
            luaStack.push(i);
        }
        for (int i = 20; i > -20; i--) {
            if (i >= -10 && i <= 10 && i != 0) {
                Assertions.assertTrue(luaStack.isValid(i));
            } else {
                Assertions.assertFalse(luaStack.isValid(i));
            }
        }
    }

    @Test
    void set() {
        LuaStack luaStack = new LuaStack(10);
        Assertions.assertThrows(LuaStackIndexOutException.class, () -> luaStack.set(1, 1));
        for (int i = 0; i < 10; i++) {
            luaStack.push(i);
        }
        Assertions.assertEquals(0, luaStack.get(1));
        luaStack.set(1, 1);
        Assertions.assertEquals(1, luaStack.get(1));
    }

    @Test
    void get() {
        LuaStack luaStack = new LuaStack(10);
        for (int i = 0; i < 10; i++) {
            luaStack.push(i);
        }
        for (int i = 1; i <= 10; i++) {
            System.out.printf("[%s]", luaStack.get(i));
            Assertions.assertEquals(i - 1, luaStack.get(i));
        }
        System.out.println();
    }

    @Test
    void size() {
        LuaStack luaStack = new LuaStack(10);
        Assertions.assertEquals(10, luaStack.size());
    }

    @Test
    void top() {
        LuaStack luaStack = new LuaStack(10);
        Assertions.assertEquals(0, luaStack.top());
        for (int i = 0; i < 6; i++) {
            luaStack.push(i);
        }
        Assertions.assertEquals(6, luaStack.top());
    }

    @Test
    void reverse() {
        LuaStack luaStack = new LuaStack(10);
        for (int i = 0; i < 10; i++) {
            luaStack.push(i);
        }
        for (int i = 1; i <= 10; i++) {
            System.out.printf("[%s]", luaStack.get(i));
        }
        System.out.println();
        luaStack.reverse(0, 9);
        for (int i = 1; i <= 10; i++) {
            System.out.printf("[%s]", luaStack.get(i));
        }
        System.out.println();
        luaStack.reverse(2, 8);
        for (int i = 1; i <= 10; i++) {
            System.out.printf("[%s]", luaStack.get(i));
        }
        System.out.println();
    }
}