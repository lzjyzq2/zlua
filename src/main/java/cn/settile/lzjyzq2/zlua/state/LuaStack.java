package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.exception.LuaStackIndexOutException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackOverflowException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackUnderflowException;

public class LuaStack {

    private Object[] slots;

    private int top = 0;


    public LuaStack(int size) {
        this.slots = new Object[size];
    }

    public boolean check(int n) {
        if (slots.length - top < n) {
            if ((top & n) + ((top ^ n) >> 1) > Integer.MAX_VALUE / 2) {
                return false;
            }
            Object[] tmpValues = new Object[top + n];
            System.arraycopy(slots, 0, tmpValues, 0, slots.length);
            slots = tmpValues;
        }
        return true;
    }

    public static LuaStack newLuaStack(int size) {
        return new LuaStack(size);
    }


    public void push(Object value) {
        if (top == slots.length) {
            throw new LuaStackOverflowException();
        }
        slots[top] = value;
        top++;
    }

    public Object pop() {
        if (top < 1) {
            throw new LuaStackUnderflowException();
        }
        top--;
        Object value = slots[top];
        slots[top] = null;
        return value;
    }

    public int absIndex(int idx) {
        if (idx >= 0) {
            return idx;
        }
        return idx + top + 1;
    }

    public boolean isValid(int idx) {
        int absIndex = absIndex(idx);
        return absIndex > 0 && absIndex <= top;
    }

    public void set(int idx, Object value) {
        int absIndex = absIndex(idx);
        if (absIndex > 0 && absIndex <= top) {
            slots[absIndex - 1] = value;
            return;
        }
        throw new LuaStackIndexOutException();
    }

    public Object get(int idx) {
        int absIndex = absIndex(idx);
        if (absIndex > 0 && absIndex <= top) {
            return slots[absIndex - 1];
        }
        return null;
    }

    public int size() {
        return slots.length;
    }

    public int top() {
        return top;
    }

    public void reverse(int from, int to) {
        from = absIndex(from);
        to = absIndex(to);
        int max = Math.max(from, to);
        int min = Math.min(from, to);
        while (min < max) {
            Object tmpValue = slots[max];
            slots[max] = slots[min];
            slots[min] = tmpValue;
            min++;
            max--;
        }
    }
}
