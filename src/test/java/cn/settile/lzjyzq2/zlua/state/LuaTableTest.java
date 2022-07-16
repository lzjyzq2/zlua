package cn.settile.lzjyzq2.zlua.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuaTableTest {

    @Test
    void get() {
    }

    @Test
    void put() {
    }

    @Test
    void len() {
    }

    @Test
    void test() {
        LuaTable luaTable = new LuaTable(0, 0);
        luaTable.put(1L, "a");
        luaTable.put(2L, "b");
        luaTable.put(3L, "c");
        for (int i = 1; i <= 3; i++) {
            System.out.printf("[%s]", luaTable.get((long)i));
        }
        System.out.println();
        luaTable.put(2L, "B");
        luaTable.put("foo", "Bar");
        System.out.printf("%s\n", luaTable.get(3L).toString() + luaTable.get(2L).toString() + luaTable.get(1L).toString() + luaTable.get("foo") + luaTable.len());
    }
}