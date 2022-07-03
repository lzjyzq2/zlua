package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.exception.LuaStackIndexOutException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackOverflowException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackUnderflowException;

import java.util.ArrayList;

public class LuaStack {

    private final ArrayList<LuaValue> slots;

    private int size;


    private LuaStack(int size) {
        this.slots = new ArrayList<>(size);
        this.size = size;
    }

    private int top() {
        return slots.size();
    }


    public static LuaStack newLuaStack(int size) {
        return new LuaStack(size);
    }


    public void push(LuaValue luaValue) {
        // todo 栈上溢
        slots.add(luaValue);
    }

    public LuaValue pop() {
        // todo 栈下溢
        return slots.remove(top() - 1);
    }

    public int absIndex(int idx) {
        if (idx >= 0) {
            return idx;
        }
        return idx + top() + 1;
    }

    public boolean isValid(int idx) {
        int absIndex = absIndex(idx);
        return absIndex > 0 && absIndex <= top();
    }

    public void set(int idx, LuaValue luaValue) {
        int absIndex = absIndex(idx);
        if (absIndex > 0 && absIndex <= top()) {
            slots.set(absIndex - 1, luaValue);
            return;
        }
        throw new LuaStackIndexOutException();
    }

    public LuaValue get(int idx) {
        int absIndex = absIndex(idx);
        if (absIndex > 0 && absIndex <= top()) {
            return slots.get(absIndex - 1);
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public void reSize() {
        size = slots.size();
    }

}
