package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.exception.LuaStackIndexOutException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackOverflowException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackUnderflowException;

public class LuaStack {

    private Object[] slots;

    private int top = 0;

    private LuaStack prev;

    private Object[] varargs;

    private Closure closure;

    private int pc = 0;


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

    public void top(int top) {
        this.top = top;
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


    public Object[] popN(int n) {
        Object[] values = new Object[n];
        for (int i = n - 1; i >= 0; i--) {
            values[i] = pop();
        }
        return values;
    }

    public void pushN(Object[] values, int n) {
        int valuesLength = values.length;
        if (n < 0) {
            n = valuesLength;
        }
        for (int i = 0; i < n; i++) {
            if (i < valuesLength) {
                push(values[i]);
            } else {
                push(null);
            }
        }
    }

    public LuaStack getPrev() {
        return prev;
    }

    public void setPrev(LuaStack prev) {
        this.prev = prev;
    }

    public int getPc() {
        return pc;
    }

    public int addPc(int n) {
        return pc += n;
    }

    public Closure getClosure() {
        return closure;
    }

    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    public Object[] getVarargs() {
        return varargs;
    }

    public void setVarargs(Object[] varargs) {
        this.varargs = varargs;
    }
}
